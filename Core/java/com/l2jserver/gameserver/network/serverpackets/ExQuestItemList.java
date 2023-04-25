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
package com.l2jserver.gameserver.network.serverpackets;

import java.util.List;

import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.itemcontainer.PcInventory;

/**
 * Structure:<BR>
 * FE C5 00 01 00 29 95 15 40 8B 3C 00 00 00 00 00 00 E2 01 00 00 00 00 00 00 03 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FF FF FF FF F1 D8 FF FF FE FF 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 <BR>
 * @author JIV
 */
public class ExQuestItemList extends L2GameServerPacket
{
	private final List<L2ItemInstance> _items;
	private final PcInventory _inventory;
	
	public ExQuestItemList(List<L2ItemInstance> items, PcInventory inv)
	{
		_items = items;
		_inventory = inv;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xC5);
		writeH(_items.size());
		for (L2ItemInstance item : _items)
		{
			writeD(item.getObjectId());
			writeD(item.getItemId());
			writeD(item.getLocationSlot());
			writeQ(item.getCount());
			writeD(item.getItem().getType2());
			writeH(item.getCustomType1()); // item type3
			// writeH(item.isEquipped() ? 0x01 : 0x00);
			writeD(item.getItem().getBodyPart());
			writeH(item.getEnchantLevel()); // enchant level
			writeH(item.getCustomType2()); // item type3
			if (item.isAugmented())
			{
				writeD(item.getAugmentation().getAugmentationId());
			}
			else
			{
				writeD(0x00);
			}
			writeD(item.getMana());
			writeD(item.isTimeLimitedItem() ? (int) (item.getRemainingTime() / 1000) : -9999);
			writeH(item.getAttackElementType());
			writeH(item.getAttackElementPower());
			for (byte i = 0; i < 6; i++)
			{
				writeH(item.getElementDefAttr(i));
			}
			// Enchant Effects
			writeH(0x00);
			writeH(0x00);
			writeH(0x00);
		}
		if (_inventory.hasInventoryBlock())
		{
			writeH(_inventory.getBlockItems().length);
			writeC(_inventory.getBlockMode());
			for (int i : _inventory.getBlockItems())
			{
				writeD(i);
			}
		}
		else
		{
			writeH(0x00);
		}
	}
}
