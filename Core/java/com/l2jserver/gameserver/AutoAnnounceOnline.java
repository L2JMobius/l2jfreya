package com.l2jserver.gameserver;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.L2World;

/**
 * @author L2jPrivateDevelopersTeam
 */
public class AutoAnnounceOnline
{
	
	protected static void StartAnnounce()
	{
		int OnlinePlayers = L2World.getInstance().getAllPlayersCount();
		
		if (OnlinePlayers >= 1)
		{
			if (Config.CRITICAL_ONLINE_ANNOUNCE)
			{
				Announcements.getInstance().announceToAll(Config.SERVER_NAME + ": " + OnlinePlayers + " players are online.", true);
			}
			else
			{
				Announcements.getInstance().announceToAll(Config.SERVER_NAME + ": " + OnlinePlayers + " players are online.");
			}
		}
	}
	
	public static void getInstance()
	{
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(() -> StartAnnounce(), 0, Config.ANNOUNCE_ONLINE_PLAYERS_DELAY * 1000);
	}
}