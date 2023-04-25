package com.l2jserver.configurator.files;

import com.l2jserver.Config;
import com.l2jserver.configurator.AbstractConfig;

/**
 * @author L2jPDT
 */
public class BuffShopConfig extends AbstractConfig
{
	public final String file = "./config/Custom/BuffShop.properties";
	
	@Override
	protected String getFilePath()
	{
		return file;
	}
	
	@Override
	public void load()
	{
		Config.ENABLE_BUFF_SHOP_MODE = getParser().getBoolean("Enable", false);
		Config.ENABLE_BUFF_SHOP_ZONE = getParser().getBoolean("OnlyInZone", false);
		Config.BUFFSHOP_ALLOWED_BUFFS = getParser().getIntArray("AllowedSkills", "1,1", ",");
		Config.MAXIMUM_BUFF = getParser().getInt("MaxBuffs", 20);
		Config.MIN_PRICE = getParser().getInt("MinPrice", 1000);
		Config.MIN_LETTERS_FOR_TITLE = getParser().getInt("MinLetters", 3);
		Config.MAX_LETTERS_FOR_TITLE = getParser().getInt("MaxLetters", 20);
		Config.MIN_DISTANCE_NEAR_THE_SHOP = getParser().getInt("MinDistance", 50);
		Config.MAX_RANGE_FOR_CAST = getParser().getInt("MaxRange", 100);
	}
}
