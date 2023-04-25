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

import com.l2jserver.Config;
import com.l2jserver.gameserver.handler.interfaces.IVoicedCommandHandler;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.Banking;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.BuffShopHandler;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.CTFCmd;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.CastleManagerInfo;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.ChangePassword;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.ChatAdmin;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.Debug;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.DressMe;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.Hellbound;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.Lang;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.MonsterRushRegistration;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.OpenAtod;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.PremiumInfo;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.Teleports;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.TvTRoundVoicedInfo;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.TvTRoundVoicedRegistration;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.TvTVoicedInfo;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.TvTVoicedRegistration;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.Wedding;
import com.l2jserver.scripts.handlers.voicedcommandhandlers.stats;

/**
 * @author L2jPDT
 */
public class VoicedCommandHandler
{
	private static Logger _log = Logger.getLogger(VoicedCommandHandler.class.getName());
	private final Map<Integer, IVoicedCommandHandler> _datatable = new HashMap<>();
	
	public static VoicedCommandHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected VoicedCommandHandler()
	{
		if (Config.L2JMOD_ALLOW_WEDDING)
		{
			registerVoicedCommandHandler(new Wedding());
		}
		if (Config.BANKING_SYSTEM_ENABLED)
		{
			registerVoicedCommandHandler(new Banking());
		}
		if (Config.TVT_ALLOW_VOICED_COMMAND)
		{
			registerVoicedCommandHandler(new TvTVoicedInfo());
		}
		if (Config.TVT_ROUND_ALLOW_VOICED_COMMAND)
		{
			registerVoicedCommandHandler(new TvTRoundVoicedInfo());
		}
		if (Config.L2JMOD_CHAT_ADMIN)
		{
			registerVoicedCommandHandler(new ChatAdmin());
		}
		if (Config.L2JMOD_MULTILANG_ENABLE && Config.L2JMOD_MULTILANG_VOICED_ALLOW)
		{
			registerVoicedCommandHandler(new Lang());
		}
		if (Config.L2JMOD_DEBUG_VOICE_COMMAND)
		{
			registerVoicedCommandHandler(new Debug());
		}
		if (Config.CTF_ALLOW_VOICE_COMMAND)
		{
			registerVoicedCommandHandler(new CTFCmd());
		}
		registerVoicedCommandHandler(new Teleports());
		registerVoicedCommandHandler(new PremiumInfo());
		registerVoicedCommandHandler(new ChangePassword());
		registerVoicedCommandHandler(new CastleManagerInfo());
		registerVoicedCommandHandler(new stats());
		registerVoicedCommandHandler(new Hellbound());
		registerVoicedCommandHandler(new OpenAtod());
		registerVoicedCommandHandler(new TvTVoicedRegistration());
		registerVoicedCommandHandler(new TvTRoundVoicedRegistration());
		registerVoicedCommandHandler(new MonsterRushRegistration());
		if (Config.ALLOW_DRESS_ME_SYSTEM)
		{
			registerVoicedCommandHandler(new DressMe());
		}
		registerVoicedCommandHandler(new BuffShopHandler());
		_log.info(getText("TG9hZGVkIFZvaWNlZCBoYW5kbGVyczog") + _datatable.size());
	}
	
	public void registerVoicedCommandHandler(IVoicedCommandHandler handler)
	{
		for (String id : handler.getVoicedCommandList())
		{
			_datatable.put(id.hashCode(), handler);
		}
	}
	
	public IVoicedCommandHandler getVoicedCommandHandler(String voicedCommand)
	{
		if (voicedCommand.indexOf(" ") != -1)
		{
			voicedCommand = voicedCommand.substring(0, voicedCommand.indexOf(" "));
		}
		return _datatable.get(voicedCommand.hashCode());
	}
	
	public int size()
	{
		return _datatable.size();
	}
	
	private static class SingletonHolder
	{
		protected static final VoicedCommandHandler _instance = new VoicedCommandHandler();
	}
	
	private static String getText(String string)
	{
		return new String(Base64.getDecoder().decode(string));
	}
}
