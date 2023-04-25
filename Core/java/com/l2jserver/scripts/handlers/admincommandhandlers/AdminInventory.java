package com.l2jserver.scripts.handlers.admincommandhandlers;

import com.l2jserver.gameserver.handler.interfaces.IAdminCommandHandler;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.util.Util;

/**
 * This class handles following admin commands:
 * <ul>
 * <li>show_ivetory</li>
 * <li>delete_item</li>
 * </ul>
 * @author Zealar
 */
public class AdminInventory implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_show_inventory",
		"admin_delete_item"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if ((activeChar.getTarget() == null))
		{
			activeChar.sendMessage("Select a target");
			return false;
		}
		
		L2PcInstance player = activeChar.getTarget().getActingPlayer();
		
		if (command.startsWith(ADMIN_COMMANDS[0]))
		{
			if (command.length() > ADMIN_COMMANDS[0].length())
			{
				String com = command.substring(ADMIN_COMMANDS[0].length() + 1);
				if (Util.isDigit(com))
				{
					showItemsPage(activeChar, Integer.parseInt(com));
				}
			}
			
			else
			{
				showItemsPage(activeChar, 0);
			}
		}
		else if (command.contains(ADMIN_COMMANDS[1]))
		{
			String val = command.substring(ADMIN_COMMANDS[1].length() + 1);
			
			player.destroyItem("GM Destroy", Integer.parseInt(val), player.getInventory().getItemByObjectId(Integer.parseInt(val)).getCount(), null, true);
			showItemsPage(activeChar, 0);
		}
		
		return true;
	}
	
	private void showItemsPage(L2PcInstance activeChar, int page)
	{
		final L2PcInstance target = activeChar.getTarget().getActingPlayer();
		
		final L2ItemInstance[] items = target.getInventory().getItems();
		
		int maxItemsPerPage = 10;
		int maxPages = items.length / maxItemsPerPage;
		if (items.length > (maxItemsPerPage * maxPages))
		{
			maxPages++;
		}
		
		if (page > maxPages)
		{
			page = maxPages;
		}
		
		int itemsStart = maxItemsPerPage * page;
		int itemsEnd = items.length;
		if ((itemsEnd - itemsStart) > maxItemsPerPage)
		{
			itemsEnd = itemsStart + maxItemsPerPage;
		}
		
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(5, 1);
		adminReply.setFile(activeChar, "data/html/admin/inventory.htm");
		adminReply.replace("%PLAYER_NAME%", target.getName());
		
		StringBuilder sbPages = new StringBuilder();
		for (int x = 0; x < maxPages; x++)
		{
			int pagenr = x + 1;
			sbPages.append("<td><button value=\"" + String.valueOf(pagenr) + "\" action=\"bypass -h admin_show_inventory " + String.valueOf(x) + "\" width=20 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		}
		
		adminReply.replace("%PAGES%", sbPages.toString());
		
		StringBuilder sbItems = new StringBuilder();
		
		for (int i = itemsStart; i < itemsEnd; i++)
		{
			sbItems.append("<tr><td><img src=\"" + items[i].getItem().getIcon() + "\" width=32 height=32></td>");
			sbItems.append("<td width=60>" + items[i].getName() + "</td>");
			sbItems.append("<td><button action=\"bypass -h admin_delete_item " + String.valueOf(items[i].getObjectId()) + "\" width=16 height=16 back=\"L2UI_ct1.Button_DF_Delete\" fore=\"L2UI_ct1.Button_DF_Delete\">" + "</td></tr>");
		}
		
		adminReply.replace("%ITEMS%", sbItems.toString());
		
		activeChar.sendPacket(adminReply);
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
