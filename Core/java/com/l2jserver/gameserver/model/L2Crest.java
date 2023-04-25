package com.l2jserver.gameserver.model;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.inferfaces.IIdentifiable;
import com.l2jserver.gameserver.network.serverpackets.AllyCrest;
import com.l2jserver.gameserver.network.serverpackets.ExPledgeCrestLarge;
import com.l2jserver.gameserver.network.serverpackets.PledgeCrest;

/**
 * @author L2jPDT
 */
public final class L2Crest implements IIdentifiable
{
	public enum CrestType
	{
		PLEDGE(1),
		PLEDGE_LARGE(2),
		ALLY(3);
		
		private final int _id;
		
		private CrestType(int id)
		{
			_id = id;
		}
		
		public int getId()
		{
			return _id;
		}
		
		public static CrestType getById(int id)
		{
			for (CrestType crestType : values())
			{
				if (crestType.getId() == id)
				{
					return crestType;
				}
			}
			return null;
		}
	}
	
	private final int _id;
	private final byte[] _data;
	private final CrestType _type;
	
	public L2Crest(int id, byte[] data, CrestType type)
	{
		_id = id;
		_data = data;
		_type = type;
	}
	
	@Override
	public int getId()
	{
		return _id;
	}
	
	public byte[] getData()
	{
		return _data;
	}
	
	public CrestType getType()
	{
		return _type;
	}
	
	/**
	 * Gets the client path to crest for use in html and sends the crest to {@code L2PcInstance}
	 * @param activeChar the @{code L2PcInstance} where html is send to.
	 * @return the client path to crest
	 */
	public String getClientPath(L2PcInstance activeChar)
	{
		String path = null;
		switch (getType())
		{
			case PLEDGE:
			{
				activeChar.sendPacket(new PledgeCrest(getId(), getData()));
				path = "Crest.crest_" + Config.SERVER_ID + "_" + getId();
				break;
			}
			case PLEDGE_LARGE:
			{
				activeChar.sendPacket(new ExPledgeCrestLarge(getId(), getData()));
				path = "Crest.crest_" + Config.SERVER_ID + "_" + getId() + "_l";
				break;
			}
			case ALLY:
			{
				activeChar.sendPacket(new AllyCrest(getId(), getData()));
				path = "Crest.crest_" + Config.SERVER_ID + "_" + getId();
				break;
			}
		}
		return path;
	}
}