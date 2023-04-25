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
package com.l2jserver.gameserver;

import java.util.logging.Logger;

/**
 * @author L2jPDT
 */
public class DressMeData
{
	public static Logger _log = Logger.getLogger(DressMeData.class.getName());
	
	private int chestId, legsId, glovesId, feetId, weapId, cloakId, agathionId, transformId;
	
	private double radius, height;
	
	public static boolean _transformed;
	
	public DressMeData()
	{
		chestId = 0;
		legsId = 0;
		glovesId = 0;
		feetId = 0;
		weapId = 0;
		cloakId = 0;
		agathionId = 0;
		transformId = 0;
		radius = 0;
		height = 0;
	}
	
	public int getChestId()
	{
		return chestId;
	}
	
	public int getLegsId()
	{
		return legsId;
	}
	
	public int getGlovesId()
	{
		return glovesId;
	}
	
	public int getBootsId()
	{
		return feetId;
	}
	
	public int getWeapId()
	{
		return weapId;
	}
	
	public int getCloakId()
	{
		return cloakId;
	}
	
	public int getAgathionId()
	{
		return agathionId;
	}
	
	public int getTransformId()
	{
		return transformId;
	}
	
	public void setChestId(int val)
	{
		chestId = val;
	}
	
	public void setLegsId(int val)
	{
		legsId = val;
	}
	
	public void setGlovesId(int val)
	{
		glovesId = val;
	}
	
	public void setBootsId(int val)
	{
		feetId = val;
	}
	
	public void setWeapId(int val)
	{
		weapId = val;
	}
	
	public void setCloakId(int val)
	{
		cloakId = val;
	}
	
	public void setAgathionId(int val)
	{
		agathionId = val;
	}
	
	public void setTransformId(int val)
	{
		transformId = val;
	}
	
	public void setRadius(double val)
	{
		radius = val;
	}
	
	public void setHeight(double val)
	{
		height = val;
	}
	
	public double getRadius()
	{
		return radius;
	}
	
	public double getHeight()
	{
		return height;
	}
}