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
public class Q00729_Protecttheterritorycatapult extends TerritoryWarSuperClass
{
	public static String qn1 = "729_Protecttheterritorycatapult";
	
	public Q00729_Protecttheterritorycatapult()
	{
		super(729, Q00729_Protecttheterritorycatapult.class.getSimpleName(), "Protect the territory catapult");
		NPC_IDS = new int[]
		{
			36499,
			36500,
			36501,
			36502,
			36503,
			36504,
			36505,
			36506,
			36507
		};
		qn = qn1;
		registerAttackIds();
	}
	
	@Override
	public int getTerritoryIdForThisNPCId(int npcid)
	{
		return npcid - 36418;
	}
}
