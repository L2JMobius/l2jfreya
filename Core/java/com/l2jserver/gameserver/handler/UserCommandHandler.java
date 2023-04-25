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
package com.l2jserver.gameserver.handler;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.l2jserver.gameserver.handler.interfaces.IUserCommandHandler;
import com.l2jserver.scripts.handlers.usercommandhandlers.Birthday;
import com.l2jserver.scripts.handlers.usercommandhandlers.ChannelDelete;
import com.l2jserver.scripts.handlers.usercommandhandlers.ChannelLeave;
import com.l2jserver.scripts.handlers.usercommandhandlers.ChannelListUpdate;
import com.l2jserver.scripts.handlers.usercommandhandlers.ClanPenalty;
import com.l2jserver.scripts.handlers.usercommandhandlers.ClanWarsList;
import com.l2jserver.scripts.handlers.usercommandhandlers.DisMount;
import com.l2jserver.scripts.handlers.usercommandhandlers.Escape;
import com.l2jserver.scripts.handlers.usercommandhandlers.InstanceZone;
import com.l2jserver.scripts.handlers.usercommandhandlers.Loc;
import com.l2jserver.scripts.handlers.usercommandhandlers.Mount;
import com.l2jserver.scripts.handlers.usercommandhandlers.OlympiadStat;
import com.l2jserver.scripts.handlers.usercommandhandlers.PartyInfo;
import com.l2jserver.scripts.handlers.usercommandhandlers.Time;

/**
 * @author L2jPDT
 */
public class UserCommandHandler
{
	private static Logger _log = Logger.getLogger(UserCommandHandler.class.getName());
	private final Map<Integer, IUserCommandHandler> _datatable = new HashMap<>();
	
	public static UserCommandHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected UserCommandHandler()
	{
		registerUserCommandHandler(new ClanPenalty());
		registerUserCommandHandler(new ClanWarsList());
		registerUserCommandHandler(new DisMount());
		registerUserCommandHandler(new Escape());
		registerUserCommandHandler(new InstanceZone());
		registerUserCommandHandler(new Loc());
		registerUserCommandHandler(new Mount());
		registerUserCommandHandler(new PartyInfo());
		registerUserCommandHandler(new Time());
		registerUserCommandHandler(new OlympiadStat());
		registerUserCommandHandler(new ChannelLeave());
		registerUserCommandHandler(new ChannelDelete());
		registerUserCommandHandler(new ChannelListUpdate());
		registerUserCommandHandler(new Birthday());
		_log.info(getText("TG9hZGVkIFVzZXIgaGFuZGxlcnM6IA==") + _datatable.size());
	}
	
	public void registerUserCommandHandler(IUserCommandHandler handler)
	{
		for (int id : handler.getUserCommandList())
		{
			_datatable.put(id, handler);
		}
	}
	
	public IUserCommandHandler getUserCommandHandler(int userCommand)
	{
		return _datatable.get(userCommand);
	}
	
	/**
	 * @return
	 */
	public int size()
	{
		return _datatable.size();
	}
	
	private static class SingletonHolder
	{
		protected static final UserCommandHandler _instance = new UserCommandHandler();
	}
	
	private static String getText(String string)
	{
		return new String(Base64.getDecoder().decode(string));
	}
}
