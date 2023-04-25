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

import com.l2jserver.gameserver.handler.interfaces.IActionHandler;
import com.l2jserver.gameserver.model.L2Object.InstanceType;
import com.l2jserver.scripts.handlers.actionhandlers.L2ArtefactInstanceAction;
import com.l2jserver.scripts.handlers.actionhandlers.L2DecoyAction;
import com.l2jserver.scripts.handlers.actionhandlers.L2DoorInstanceAction;
import com.l2jserver.scripts.handlers.actionhandlers.L2DoorInstanceActionShift;
import com.l2jserver.scripts.handlers.actionhandlers.L2ItemInstanceAction;
import com.l2jserver.scripts.handlers.actionhandlers.L2ItemInstanceActionShift;
import com.l2jserver.scripts.handlers.actionhandlers.L2NpcAction;
import com.l2jserver.scripts.handlers.actionhandlers.L2NpcActionShift;
import com.l2jserver.scripts.handlers.actionhandlers.L2PcInstanceAction;
import com.l2jserver.scripts.handlers.actionhandlers.L2PcInstanceActionShift;
import com.l2jserver.scripts.handlers.actionhandlers.L2PetInstanceAction;
import com.l2jserver.scripts.handlers.actionhandlers.L2StaticObjectInstanceAction;
import com.l2jserver.scripts.handlers.actionhandlers.L2StaticObjectInstanceActionShift;
import com.l2jserver.scripts.handlers.actionhandlers.L2SummonAction;
import com.l2jserver.scripts.handlers.actionhandlers.L2SummonActionShift;
import com.l2jserver.scripts.handlers.actionhandlers.L2TrapAction;

/**
 * @author L2jPDT
 */
public class ActionHandler
{
	private static Logger _log = Logger.getLogger(ActionHandler.class.getName());
	private final Map<InstanceType, IActionHandler> _actions = new HashMap<>();
	private final Map<InstanceType, IActionHandler> _actionsShift = new HashMap<>();
	
	public static ActionHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected ActionHandler()
	{
		registerActionHandler(new L2ArtefactInstanceAction());
		registerActionHandler(new L2DecoyAction());
		registerActionHandler(new L2DoorInstanceAction());
		registerActionHandler(new L2ItemInstanceAction());
		registerActionHandler(new L2NpcAction());
		registerActionHandler(new L2PcInstanceAction());
		registerActionHandler(new L2PetInstanceAction());
		registerActionHandler(new L2StaticObjectInstanceAction());
		registerActionHandler(new L2SummonAction());
		registerActionHandler(new L2TrapAction());
		registerActionShiftHandler(new L2DoorInstanceActionShift());
		registerActionShiftHandler(new L2ItemInstanceActionShift());
		registerActionShiftHandler(new L2NpcActionShift());
		registerActionShiftHandler(new L2PcInstanceActionShift());
		registerActionShiftHandler(new L2StaticObjectInstanceActionShift());
		registerActionShiftHandler(new L2SummonActionShift());
		_log.info(getText("TG9hZGVkIEFjdGlvbiBoYW5kbGVyczog") + _actions.size());
		_log.info(getText("TG9hZGVkIEFjdGlvbiBTaGlmdCBoYW5kbGVyczog") + _actionsShift.size());
	}
	
	public void registerActionHandler(IActionHandler handler)
	{
		_actions.put(handler.getInstanceType(), handler);
	}
	
	public void registerActionShiftHandler(IActionHandler handler)
	{
		_actionsShift.put(handler.getInstanceType(), handler);
	}
	
	public IActionHandler getActionHandler(InstanceType iType)
	{
		IActionHandler result = null;
		for (InstanceType t = iType; t != null; t = t.getParent())
		{
			result = _actions.get(t);
			if (result != null)
			{
				break;
			}
		}
		return result;
	}
	
	public IActionHandler getActionShiftHandler(InstanceType iType)
	{
		IActionHandler result = null;
		for (InstanceType t = iType; t != null; t = t.getParent())
		{
			result = _actionsShift.get(t);
			if (result != null)
			{
				break;
			}
		}
		return result;
	}
	
	public int size()
	{
		return _actions.size();
	}
	
	public int sizeShift()
	{
		return _actionsShift.size();
	}
	
	private static class SingletonHolder
	{
		protected static final ActionHandler _instance = new ActionHandler();
	}
	
	private static String getText(String string)
	{
		return new String(Base64.getDecoder().decode(string));
	}
}