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
package com.l2jserver.gameserver.model.entity;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author Gnacik
 */
public final class RecoBonus
{
	private static final int[][] _recoBonus =
	{
		//@formatter:off
		{25,50,50,50,50,50,50,50,50,50}, //0-9
		{16,33,50,50,50,50,50,50,50,50},//10-19
		{12,25,37,50,50,50,50,50,50,50},//20-29
		{10,20,30,40,50,50,50,50,50,50},//30-39
		{8,16,25,33,41,50,50,50,50,50},//40-49
		{7,14,21,28,35,42,50,50,50,50},//40-59
		{6,12,18,25,31,37,43,50,50,50},//60-69
		{5,11,16,22,27,33,38,44,50,50},//70-79
		{5,10,15,20,25,30,35,40,45,50},//80-89
		{5,10,15,20,25,30,35,40,45,50},//90-99
		//@formatter:on
	};
	
	public static int getRecoBonus(L2PcInstance activeChar)
	{
		if ((activeChar != null) && activeChar.isOnline() && (activeChar.getRecomHave() != 0) && (activeChar.getRecomBonusTime() > 0))
		{
			final int lvl = activeChar.getLevel() / 10;
			final int exp = (Math.min(100, activeChar.getRecomHave()) - 1) / 10;
			
			return _recoBonus[lvl][exp];
		}
		return 0;
	}
	
	public static double getRecoMultiplier(L2PcInstance activeChar)
	{
		return getRecoBonus(activeChar) / 100d;
	}
}