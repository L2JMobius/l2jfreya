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

public final class TutorialShowHtml extends L2GameServerPacket
{
	private final String _html;
	
	public TutorialShowHtml(String html)
	{
		_html = html;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jserver.gameserver.serverpackets.ServerBasePacket#writeImpl()
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0xa6);
		writeS(_html);
	}
}