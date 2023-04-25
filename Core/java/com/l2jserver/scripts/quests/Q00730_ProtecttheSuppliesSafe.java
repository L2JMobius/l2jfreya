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
package com.l2jserver.scripts.quests;

/**
 * @author L2jPDT
 */
public class Q00730_ProtecttheSuppliesSafe extends TerritoryWarSuperClass
{
	public static String qn1 = "730_ProtecttheSuppliesSafe.";
	
	public Q00730_ProtecttheSuppliesSafe()
	{
		super(730, Q00730_ProtecttheSuppliesSafe.class.getSimpleName(), "Protect the Supplies Safe");
		NPC_IDS = new int[]
		{
			36591,
			36592,
			36593,
			36594,
			36595,
			36596,
			36597,
			36598,
			36599
		};
		qn = qn1;
		registerAttackIds();
	}
	
	@Override
	public int getTerritoryIdForThisNPCId(int npcid)
	{
		return npcid - 36510;
	}
}
