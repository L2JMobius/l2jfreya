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
package com.l2jserver.loginserver.gameserverpackets;

import java.util.ArrayList;
import java.util.List;

import com.l2jserver.util.network.BaseRecievePacket;

/**
 * @author -Wooden-
 */
public class PlayerInGame extends BaseRecievePacket
{
	private final List<String> _accounts;
	
	/**
	 * @param decrypt
	 */
	public PlayerInGame(byte[] decrypt)
	{
		super(decrypt);
		_accounts = new ArrayList<>();
		int size = readH();
		for (int i = 0; i < size; i++)
		{
			_accounts.add(readS());
		}
	}
	
	/**
	 * @return Returns the accounts.
	 */
	public List<String> getAccounts()
	{
		return _accounts;
	}
	
}