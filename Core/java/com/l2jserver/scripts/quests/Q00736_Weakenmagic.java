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
public class Q00736_Weakenmagic extends TerritoryWarSuperClass
{
	public static String qn1 = "736_Weakenmagic";
	
	public Q00736_Weakenmagic()
	{
		super(736, Q00736_Weakenmagic.class.getSimpleName(), "Weaken magic");
		CLASS_IDS = new int[]
		{
			40,
			110,
			27,
			103,
			13,
			95,
			12,
			94,
			41,
			111,
			28,
			104,
			14,
			96
		};
		qn = qn1;
		RANDOM_MIN = 10;
		RANDOM_MAX = 15;
		Text = new String[]
		{
			"Out of MAX Wizards and Summoners you have defeated KILL.",
			"You weakened the enemy's attack!"
		};
	}
}
