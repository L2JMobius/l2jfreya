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

import com.l2jserver.gameserver.handler.interfaces.IChatHandler;
import com.l2jserver.scripts.handlers.chathandlers.ChatAll;
import com.l2jserver.scripts.handlers.chathandlers.ChatAlliance;
import com.l2jserver.scripts.handlers.chathandlers.ChatBattlefield;
import com.l2jserver.scripts.handlers.chathandlers.ChatClan;
import com.l2jserver.scripts.handlers.chathandlers.ChatHeroVoice;
import com.l2jserver.scripts.handlers.chathandlers.ChatParty;
import com.l2jserver.scripts.handlers.chathandlers.ChatPartyMatchRoom;
import com.l2jserver.scripts.handlers.chathandlers.ChatPartyRoomAll;
import com.l2jserver.scripts.handlers.chathandlers.ChatPartyRoomCommander;
import com.l2jserver.scripts.handlers.chathandlers.ChatPetition;
import com.l2jserver.scripts.handlers.chathandlers.ChatShout;
import com.l2jserver.scripts.handlers.chathandlers.ChatTell;
import com.l2jserver.scripts.handlers.chathandlers.ChatTrade;

/**
 * @author L2jPDT
 */
public class ChatHandler
{
	private static Logger _log = Logger.getLogger(ChatHandler.class.getName());
	private final Map<Integer, IChatHandler> _datatable = new HashMap<>();
	
	public static ChatHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected ChatHandler()
	{
		registerChatHandler(new ChatAll());
		registerChatHandler(new ChatAlliance());
		registerChatHandler(new ChatBattlefield());
		registerChatHandler(new ChatClan());
		registerChatHandler(new ChatHeroVoice());
		registerChatHandler(new ChatParty());
		registerChatHandler(new ChatPartyMatchRoom());
		registerChatHandler(new ChatPartyRoomAll());
		registerChatHandler(new ChatPartyRoomCommander());
		registerChatHandler(new ChatPetition());
		registerChatHandler(new ChatShout());
		registerChatHandler(new ChatTell());
		registerChatHandler(new ChatTrade());
		_log.info(getText("TG9hZGVkIENoYXQgaGFuZGxlcnM6IA==") + _datatable.size());
	}
	
	public void registerChatHandler(IChatHandler handler)
	{
		for (int id : handler.getChatTypeList())
		{
			_datatable.put(id, handler);
		}
	}
	
	public IChatHandler getChatHandler(int chatType)
	{
		return _datatable.get(chatType);
	}
	
	public int size()
	{
		return _datatable.size();
	}
	
	private static class SingletonHolder
	{
		protected static final ChatHandler _instance = new ChatHandler();
	}
	
	private static String getText(String string)
	{
		return new String(Base64.getDecoder().decode(string));
	}
}