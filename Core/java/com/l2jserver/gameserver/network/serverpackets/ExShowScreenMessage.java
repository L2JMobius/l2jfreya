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

/**
 * @author Kerberos
 */
public class ExShowScreenMessage extends L2GameServerPacket
{
	private final int _type;
	private final int _sysMessageId;
	private final int _unk1;
	private final int _unk2;
	private final int _unk3;
	private final int _unk4;
	private final int _size;
	private final int _position;
	private boolean _effect;
	private final String _text;
	private final int _time;
	
	public ExShowScreenMessage(String text, int time)
	{
		_type = 1;
		_sysMessageId = -1;
		_unk1 = 0;
		_unk2 = 0;
		_unk3 = 0;
		_unk4 = 0;
		_position = 0x02;
		_text = text;
		_time = time;
		_size = 0;
		_effect = false;
	}
	
	public ExShowScreenMessage(int type, int messageId, int position, int unk1, int size, int unk2, int unk3, boolean showEffect, int time, int unk4, String text)
	{
		_type = type;
		_sysMessageId = messageId;
		_position = position;
		_unk1 = unk1;
		_size = size;
		_unk2 = unk2;
		_unk3 = unk3;
		_effect = showEffect;
		_time = time;
		_unk4 = unk4;
		_text = text;
	}
	
	public ExShowScreenMessage setUpperEffect(boolean value)
	{
		_effect = value;
		return this;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x39);
		writeD(_type); // 0 - system messages, 1 - your defined text
		writeD(_sysMessageId); // system message id (_type must be 0 otherwise no effect)
		writeD(_position); // message position
		writeD(_unk1); // ?
		writeD(_size); // font size 0 - normal, 1 - small
		writeD(_unk2); // ?
		writeD(_unk3); // ?
		writeD(_effect == true ? 1 : 0); // upper effect (0 - disabled, 1 enabled) - _position must be 2 (center) otherwise no effect
		writeD(_time); // time
		writeD(_unk4); // ?
		writeD(0x00); // TODO: npcString
		writeS(_text); // your text (_type must be 1, otherwise no effect)
	}
}