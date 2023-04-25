/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2jserver.gameserver.model.buffshop;

import java.util.LinkedHashMap;
import java.util.Map;

import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.util.IConvert;

/**
 * @author Administrator
 */
public class ShopObject implements IConvert
{
	private final Map<Integer, PrivateBuff> buffs = new LinkedHashMap<>();
	private final int ownerId;
	private int objectId;
	private String title = "";
	private final Location location = new Location(0, 0, 0);
	
	public void setXYZ(int x, int y, int z, int heading)
	{
		location.setXYZ(x, y, z);
		location.setHeading(heading);
	}
	
	public int getX()
	{
		return location.getX();
	}
	
	public int getY()
	{
		return location.getY();
	}
	
	public int getZ()
	{
		return location.getZ();
	}
	
	public int getHeading()
	{
		return location.getHeading();
	}
	
	public ShopObject(int ownerId)
	{
		this.ownerId = ownerId;
	}
	
	public ShopObject(L2PcInstance owner)
	{
		this.ownerId = owner.getObjectId();
	}
	
	public void addBuff(String line)
	{
		String[] tmp = line.split(",");
		buffs.put(toInteger(tmp[0]), new PrivateBuff(toInteger(tmp[2]), toInteger(tmp[0]), toInteger(tmp[1])));
	}
	
	public void addBuff(int buffId, int lvl, int price)
	{
		buffs.put(buffId, new PrivateBuff(price, buffId, lvl));
	}
	
	public void removeBuff(Integer buffId)
	{
		buffs.remove(buffId);
	}
	
	public int getPrice(int buffId)
	{
		return buffs.get(buffId).price;
	}
	
	public void setPrice(int buffId, int price)
	{
		buffs.get(buffId).price = price;
	}
	
	public int getOwnerId()
	{
		return ownerId;
	}
	
	public Map<Integer, PrivateBuff> getBuffList()
	{
		return buffs;
	}
	
	public String getBuffLine()
	{
		StringBuilder sb = new StringBuilder();
		for (PrivateBuff buff : buffs.values())
		{
			if (sb.length() > 0)
			{
				sb.append(";");
			}
			sb.append(buff.skillid).append(",").append(buff.skillLevel).append(",").append(buff.price);
		}
		return sb.toString();
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * @return the objectId
	 */
	public int getSellerObjectId()
	{
		return objectId;
	}
	
	/**
	 * @param objectId the objectId to set
	 */
	public void setSellerObjectId(int objectId)
	{
		this.objectId = objectId;
	}
	
	static class PrivateBuff
	{
		int price;
		int skillid;
		int skillLevel;
		
		/**
		 * @param price
		 * @param skillid
		 * @param skillLevel
		 */
		public PrivateBuff(int price, int skillid, int skillLevel)
		{
			super();
			this.price = price;
			this.skillid = skillid;
			this.skillLevel = skillLevel;
		}
	}
}