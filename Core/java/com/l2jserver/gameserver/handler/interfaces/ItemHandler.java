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
package com.l2jserver.gameserver.handler.interfaces;

import java.util.HashMap;
import java.util.Map;

import com.l2jserver.gameserver.templates.item.L2EtcItem;

/**
 * @author L2jPDT
 */
public class ItemHandler
{
	private final Map<Integer, IItemHandler> _datatable;
	
	public static ItemHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public int size()
	{
		return _datatable.size();
	}
	
	private ItemHandler()
	{
		_datatable = new HashMap<>();
	}
	
	public void registerItemHandler(IItemHandler handler)
	{
		_datatable.put(handler.getClass().getSimpleName().intern().hashCode(), handler);
	}
	
	public IItemHandler getItemHandler(L2EtcItem item)
	{
		if ((item == null) || (item.getHandlerName() == null))
		{
			return null;
		}
		return _datatable.get(item.getHandlerName().hashCode());
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final ItemHandler _instance = new ItemHandler();
	}
}
