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
public class Q00738_DestroyKeyTargets extends TerritoryWarSuperClass
{
	public static String qn1 = "738_DestroyKeyTargets";
	
	public Q00738_DestroyKeyTargets()
	{
		super(738, Q00738_DestroyKeyTargets.class.getSimpleName(), "Destroy Key Targets");
		CLASS_IDS = new int[]
		{
			51,
			115,
			57,
			118
		};
		qn = qn1;
		RANDOM_MIN = 3;
		RANDOM_MAX = 8;
		Text = new String[]
		{
			"Out of MAX Production and Curse you have defeated KILL.",
			"You weakened the enemy's attack!"
		};
	}
}
