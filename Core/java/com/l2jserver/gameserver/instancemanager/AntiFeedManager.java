/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.instancemanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.L2GameClient;

public class AntiFeedManager
{
	public static final int GAME_ID = 0;
	public static final int OLYMPIAD_ID = 1;
	public static final int TVT_ID = 2;
	public static final int TVT_ROUND_ID = 3;
	
	private final Map<Integer, Long> _lastDeathTimes = new ConcurrentHashMap<>();
	private final Map<Integer, Map<Integer, AtomicInteger>> _eventIPs = new ConcurrentHashMap<>();
	
	public static final AntiFeedManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected AntiFeedManager()
	{
		
	}
	
	/**
	 * Set time of the last player's death to current
	 * @param objectId Player's objectId
	 */
	public final void setLastDeathTime(int objectId)
	{
		_lastDeathTimes.put(objectId, System.currentTimeMillis());
	}
	
	/**
	 * Check if current kill should be counted as non-feeded.
	 * @param attacker Attacker character
	 * @param target Target character
	 * @return True if kill is non-feeded.
	 */
	public final boolean check(L2Character attacker, L2Character target)
	{
		if (!Config.L2JMOD_ANTIFEED_ENABLE)
		{
			return true;
		}
		
		if (target == null)
		{
			return false;
		}
		
		final L2PcInstance targetPlayer = target.getActingPlayer();
		if (targetPlayer == null)
		{
			return false;
		}
		
		if ((Config.L2JMOD_ANTIFEED_INTERVAL > 0) && _lastDeathTimes.containsKey(targetPlayer.getObjectId()))
		{
			return (System.currentTimeMillis() - _lastDeathTimes.get(targetPlayer.getObjectId())) > Config.L2JMOD_ANTIFEED_INTERVAL;
		}
		
		if (Config.L2JMOD_ANTIFEED_DUALBOX && (attacker != null))
		{
			final L2PcInstance attackerPlayer = attacker.getActingPlayer();
			if (attackerPlayer == null)
			{
				return false;
			}
			
			final L2GameClient targetClient = targetPlayer.getClient();
			final L2GameClient attackerClient = attackerPlayer.getClient();
			if ((targetClient == null) || (attackerClient == null) || targetClient.isDetached() || attackerClient.isDetached())
			{
				// unable to check ip address
				return !Config.L2JMOD_ANTIFEED_DISCONNECTED_AS_DUALBOX;
			}
			
			return !targetClient.getConnectionAddress().equals(attackerClient.getConnectionAddress());
		}
		
		return true;
	}
	
	/**
	 * Clears all timestamps
	 */
	public final void clear()
	{
		_lastDeathTimes.clear();
	}
	
	/**
	 * Register new event for dualbox check. Should be called only once.
	 * @param eventId
	 */
	public final void registerEvent(int eventId)
	{
		_eventIPs.putIfAbsent(eventId, new ConcurrentHashMap<Integer, AtomicInteger>());
	}
	
	/**
	 * If number of all simultaneous connections from player's IP address lower than max then increment connection count and return true. Returns false if number of all simultaneous connections from player's IP address higher than max.
	 * @param eventId
	 * @param player
	 * @param max
	 * @return
	 */
	public final boolean tryAddPlayer(int eventId, L2PcInstance player, int max)
	{
		return tryAddClient(eventId, player.getClient(), max);
	}
	
	/**
	 * If number of all simultaneous connections from player's IP address lower than max then increment connection count and return true. Returns false if number of all simultaneous connections from player's IP address higher than max.
	 * @param eventId
	 * @param player
	 * @param max
	 * @return
	 */
	public final boolean tryAddClient(int eventId, L2GameClient client, int max)
	{
		if (client == null)
		{
			return false; // unable to determine IP address
		}
		
		final Map<Integer, AtomicInteger> event = _eventIPs.get(eventId);
		if (event == null)
		{
			return false; // no such event registered
		}
		
		final Integer addrHash = Integer.valueOf(client.getConnectionAddress().hashCode());
		
		final AtomicInteger connectionCount = event.computeIfAbsent(addrHash, k -> new AtomicInteger());
		int whiteListCount = Config.L2JMOD_DUALBOX_CHECK_WHITELIST.getOrDefault(addrHash, 0);
		if ((whiteListCount < 0) || ((connectionCount.get() + 1) <= (max + whiteListCount)))
		{
			connectionCount.incrementAndGet();
			return true;
		}
		return false;
	}
	
	/**
	 * Decreasing number of active connection from player's IP address Returns true if success and false if any problem detected.
	 * @param eventId
	 * @param player
	 * @return
	 */
	public final boolean removePlayer(int eventId, L2PcInstance player)
	{
		final L2GameClient client = player.getClient();
		if (client == null)
		{
			return false; // unable to determine IP address
		}
		
		final Map<Integer, AtomicInteger> event = _eventIPs.get(eventId);
		if (event == null)
		{
			return false; // no such event registered
		}
		
		final Integer addrHash = Integer.valueOf(client.getConnectionAddress().hashCode());
		
		return event.computeIfPresent(addrHash, (k, v) ->
		{
			if ((v == null) || (v.decrementAndGet() == 0))
			{
				return null;
			}
			return v;
		}) != null;
	}
	
	/*
	 * Connections conns = event.get(addrHash); if (conns == null) { return false; // address not registered } synchronized (event) { if (conns.testAndDecrement()) { event.remove(addrHash); } } return true;/* } /** Remove player connection IP address from all registered events lists.
	 * @param player
	 */
	public final void onDisconnect(L2GameClient client)
	{
		if (client == null)
		{
			return;
		}
		
		_eventIPs.forEach((k, v) ->
		{
			removeClient(k, client);
		});
	}
	
	// sem to dej na dalsi radek
	public final boolean removeClient(int eventId, L2GameClient client)
	{
		if (client == null)
		// smaz ty mezery mavic
		{
			return false; // unable to determine IP address
		}
		
		final Map<Integer, AtomicInteger> event = _eventIPs.get(eventId);
		
		if (event == null)
		{
			return false; // no such event registered
		}
		
		final Integer addrHash = Integer.valueOf(client.getConnectionAddress().hashCode());
		
		return event.computeIfPresent(addrHash, (k, v) ->
		{
			if ((v == null) || (v.decrementAndGet() == 0))
			{
				return null;
			}
			return v;
		}) != null;
		
	}
	
	/**
	 * Clear all entries for this eventId.
	 * @param eventId
	 */
	public final void clear(int eventId)
	{
		final Map<Integer, AtomicInteger> event = _eventIPs.get(eventId);
		if (event != null)
		{
			event.clear();
		}
	}
	
	/**
	 * Returns maximum number of allowed connections (whitelist + max)
	 * @param player
	 * @param max
	 * @return
	 */
	public final int getLimit(L2PcInstance player, int max)
	{
		return getLimit(player.getClient(), max);
	}
	
	/**
	 * Returns maximum number of allowed connections (whitelist + max)
	 * @param client
	 * @param max
	 * @return
	 */
	public final int getLimit(L2GameClient client, int max)
	{
		if (client == null)
		{
			return max;
		}
		
		final Integer addrHash = Integer.valueOf(client.getConnectionAddress().hashCode());
		final int limit = Config.L2JMOD_DUALBOX_CHECK_WHITELIST.get(addrHash);
		return limit < 0 ? 0 : limit + max;
	}
	
	protected static final class Connections
	{
		private int _num = 0;
		
		/**
		 * Returns true if successfully incremented number of connections and false if maximum number is reached.
		 */
		public final synchronized boolean testAndIncrement(int max)
		{
			if (_num < max)
			{
				_num++;
				return true;
			}
			return false;
		}
		
		/**
		 * Returns true if all connections are removed
		 */
		public final synchronized boolean testAndDecrement()
		{
			if (_num > 0)
			{
				_num--;
			}
			
			return _num == 0;
		}
	}
	
	private static class SingletonHolder
	{
		protected static final AntiFeedManager _instance = new AntiFeedManager();
	}
}