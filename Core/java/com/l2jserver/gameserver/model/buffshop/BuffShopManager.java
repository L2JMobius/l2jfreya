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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.datatables.CharTemplateTable;
import com.l2jserver.gameserver.datatables.OfflineTradersTable;
import com.l2jserver.gameserver.instancemanager.TransformationManager;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.L2Skill.SkillTargetType;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.appearance.PcAppearance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.ClassId;
import com.l2jserver.gameserver.model.buffshop.ShopObject.PrivateBuff;
import com.l2jserver.gameserver.model.quest.Quest.QuestSound;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.PlaySound;
import com.l2jserver.gameserver.network.serverpackets.PrivateStoreMsgSell;
import com.l2jserver.gameserver.util.HtmlUtil;
import com.l2jserver.gameserver.util.Util;
import com.l2jserver.util.Rnd;

/**
 * @author Administrator
 */
public class BuffShopManager
{
	// CONFIGURATIONS
	public static final boolean ENABLE_BUFF_SHOP_MODE = Config.ENABLE_BUFF_SHOP_MODE;
	public static final boolean ENABLE_BUFF_SHOP_ZONE = Config.ENABLE_BUFF_SHOP_ZONE;
	private static final int MAXIMUM_BUFF = Config.MAXIMUM_BUFF;
	public static final int MIN_PRICE = Config.MIN_PRICE;
	public static final int MIN_LETTERS_FOR_TITLE = Config.MIN_LETTERS_FOR_TITLE;
	public static final int MAX_LETTERS_FOR_TITLE = Config.MAX_LETTERS_FOR_TITLE;
	public static final int MIN_DISTANCE_NEAR_THE_SHOP = Config.MIN_DISTANCE_NEAR_THE_SHOP;// 30
	public static final int MAX_RANGE_FOR_CAST = Config.MAX_RANGE_FOR_CAST;// 100
	
	// LOGGER
	private final static Logger _log = Logger.getLogger(OfflineTradersTable.class.getName());
	
	// OTHERS
	private final Map<Integer, ShopObject> shops = new ConcurrentHashMap<>();
	private final Map<Integer, Integer> sellers = new ConcurrentHashMap<>();
	private final Map<Integer, Integer> offlineMoney = new ConcurrentHashMap<>();
	
	public static final Set<SkillTargetType> targetCheck = new HashSet<>();
	static
	{
		targetCheck.add(SkillTargetType.TARGET_SELF);
		targetCheck.add(SkillTargetType.TARGET_SUMMON);
		targetCheck.add(SkillTargetType.TARGET_SUMMON);
		targetCheck.add(SkillTargetType.TARGET_CORPSE_MOB);
		targetCheck.add(SkillTargetType.TARGET_AURA);
		targetCheck.add(SkillTargetType.TARGET_AREA);
		targetCheck.add(SkillTargetType.TARGET_AREA_CORPSE_MOB);
	}
	
	/**
	 * @return the sellers
	 */
	public Map<Integer, Integer> getSellers()
	{
		return sellers;
	}
	
	private static final ClassId[] ALLOW_CLASS = ClassId.values();
	// private static final int[] FORBIDDEN_SKILL = Config.BUFFSHOP_FORBIDDEN_SKILL;
	private static final int PAGE_SIZE = 8;
	private static final String SAVE_SHOP = "REPLACE INTO buffshop (`ownerId`,`buffs`,`title`,`x`,`y`,`z`,`heading`) VALUES (?,?,?,?,?,?,?)";
	private static final String LOAD_SHOPS = "SELECT ownerId,buffs,title,x,y,z,heading FROM buffshop";
	
	private static class SingletonHolder
	{
		protected static final BuffShopManager _instance = new BuffShopManager();
	}
	
	public static BuffShopManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	BuffShopManager()
	{
	}
	
	public void setShop(L2PcInstance player)
	{
		if (!Util.contains(ALLOW_CLASS, player.getClassId()))
		{
			player.sendPacket(new ExShowScreenMessage("Your class cannot sell buff.", 4000));
			return;
		}
		
		if (!shops.containsKey(player.getObjectId()))
		{
			shops.put(player.getObjectId(), new ShopObject(player));
		}
		
		player.sendPacket(new NpcHtmlMessage(0, HtmCache.getInstance().getHtm(player, "data/html/shopBuffMain.htm")));
	}
	
	/**
	 * @param activeChar
	 * @param page
	 */
	public void list(L2PcInstance activeChar, int page)
	{
		String html = HtmCache.getInstance().getHtm(activeChar, "data/html/shopBuffList.htm");
		StringBuilder builder = new StringBuilder();
		List<L2Skill> skills = new ArrayList<>();
		ShopObject shopObject = shops.get(activeChar.getObjectId());
		
		if (shopObject == null)
		{
			shopObject = new ShopObject(activeChar);
		}
		
		for (L2Skill skill : activeChar.getSkills().values())
		{
			if (Util.contains(Config.BUFFSHOP_ALLOWED_BUFFS, skill.getId()))
			{
				skills.add(skill);
			}
		}
		
		int size = skills.size();
		int startIndex = (page - 1) * PAGE_SIZE;
		startIndex = startIndex > (size - 1) ? size - 1 : startIndex;// check index
		int endIndex = startIndex + PAGE_SIZE;
		endIndex = endIndex > (size - 1) ? size - 1 : endIndex; // check index
		
		L2Skill skill;
		
		if (!skills.isEmpty())
		{
			for (int index = startIndex; index <= endIndex; index++)
			{
				skill = skills.get(index);
				builder.append("<tr>").append("<td>").append("<img src=").append(skill.getIcon()).append(" width=32 height=32/>").append("</td>");
				builder.append("<td fixwidth=180>").append(skill.getName()).append("</td>");
				if (shopObject.getBuffList().containsKey(skill.getId()))
				{
					builder.append("<td align=\"right\">").append("<button width=50 height=20 value=\"Delete\" action=\"bypass -h buffshop del " + skill.getId() + "\" back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">").append("</td>");
				}
				else
				{
					builder.append("<td align=\"right\">").append("<button width=50 height=20 value=\"Add\" action=\"bypass -h buffshop add " + skill.getId() + " " + skill.getLevel() + " $price\" back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">").append("</td>");
				}
				builder.append("</tr>");
			}
		}
		else
		{
			builder.append("<tr>").append("<td> There is currently no magic to be put on the shelf. </td>").append("</tr>");
		}
		
		html = html.replace("%list%", builder.toString());
		
		if (shopObject.getTitle().length() == 0)
		{
			html = html.replace("%title%", "You have to set a name");
		}
		else
		{
			html = html.replace("%title%", shopObject.getTitle());
		}
		
		if (startIndex > 0)// not firstPage
		{
			html = html.replace("%prev%", "<button width=80 height=20 value=\"Back\" action=\"bypass -h buffshop list " + (page - 1) + "\" back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">");
		}
		else
		{
			html = html.replace("%prev%", "");
		}
		
		if (endIndex < (size - 1))// has more page
		{
			html = html.replace("%next%", "<button width=80 height=20 value=\"Next\" action=\"bypass -h buffshop list " + (page + 1) + "\" back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">");
		}
		else
		{
			html = html.replace("%next%", "");
		}
		activeChar.sendPacket(new NpcHtmlMessage(endIndex, html));
	}
	
	/**
	 * @param skill
	 * @param shopObject
	 * @return
	 */
	/*
	 * private boolean checkBuffCondition(L2Skill skill, ShopObject shopObject) { if (!skill.hasEffectType(L2EffectType.BUFF, L2EffectType.NOBLESSE_BLESSING) || skill.isPassive() || targetCheck.contains(skill.getTargetType()) || skill.isToggle() || Util.contains(FORBIDDEN_SKILL, skill.getId())) {
	 * return false; } return true; }
	 */
	
	public void showShop(L2PcInstance seller, L2PcInstance player)
	{
		ShopObject shop = shops.get(sellers.get(seller.getObjectId()));
		StringBuilder sb = new StringBuilder();
		NpcHtmlMessage html = new NpcHtmlMessage(0);
		sb.append("<html><title> Buff Shop </title><body><center>");
		sb.append(HtmlUtil.getMpGauge(270, (long) seller.getCurrentMp(), seller.getMaxMp(), true));
		sb.append("<table width=270>");
		
		for (Entry<Integer, PrivateBuff> buff : shop.getBuffList().entrySet())
		{
			L2Skill skill = L2Skill.valueOf(buff.getValue().skillid, buff.getValue().skillLevel);
			
			if (skill == null)
			{
				continue;
			}
			
			sb.append("<tr><td><img src=\"" + skill.getIcon() + "\" width=32 height=32></td><td fixwidth=170><table><tr><td width=168>" + skill.getName() + " - Lv " + parseSkillLevel(skill.getLevel()) + "</td></tr><tr><td><font color=FF0000>Price: </font><font color=LEVEL>" + buff.getValue().price + "</font></td></tr></table></td><td valign=middle><button action=\"bypass -h buffshop cast " + shop.getSellerObjectId() + " " + skill.getId() + "\" value=\"Buy\" width=60 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>");
		}
		
		sb.append("</table>");
		sb.append("</center></body></html>");
		
		html.setHtml(sb.toString());
		player.sendPacket(html);
	}
	
	private String parseSkillLevel(int level)
	{
		String result = String.valueOf(level % 100);
		if (level > 100)
		{
			result = "<font color=FFFF00>+ " + result + "</font>";
		}
		return result;
	}
	
	public void addBuff(L2PcInstance player, int skillId, int skillLevel, int price)
	{
		ShopObject shopObject = shops.get(player.getObjectId());
		if (shopObject == null)
		{
			shopObject = new ShopObject(player);
			shops.put(player.getObjectId(), shopObject);
		}
		if (shopObject.getBuffList().size() < MAXIMUM_BUFF)
		{
			shopObject.addBuff(skillId, skillLevel, price);
		}
		else
		{
			player.sendPacket(new ExShowScreenMessage("The sale limit has been reached and cannot be added!", 5000));
		}
		list(player, 1);
	}
	
	public void removeBuff(L2PcInstance player, int skillId)
	{
		shops.get(player.getObjectId()).removeBuff(skillId);
		list(player, 1);
	}
	
	public void startShop(L2PcInstance player)
	{
		ShopObject shopObject = shops.get(player.getObjectId());
		
		if (shopObject.getBuffList().isEmpty())
		{
			player.sendPacket(new ExShowScreenMessage("You have to put at least one buff!", 5000));
			return;
		}
		
		if (shopObject.getTitle().length() == 0)
		{
			player.sendPacket(new ExShowScreenMessage("You have to set a title!", 5000));
			return;
		}
		
		stopShop(player);
		L2PcInstance seller;
		seller = L2PcInstance.createDummy(CharTemplateTable.getInstance().getTemplate(15), "BUFF SHOP", "", new PcAppearance((byte) 1, (byte) 1, (byte) 1, true), Collections.emptyList());
		seller.addSkill(L2Skill.valueOf(10005, 1));// Must enable custom skill;
		seller.getSellList().setTitle(shopObject.getTitle());
		seller.setPrivateStoreType(L2PcInstance.STORE_PRIVATE_PACKAGE_SELL);
		seller.broadcastPacket(new PrivateStoreMsgSell(seller));
		seller.spawnMe(player.getX(), player.getY(), player.getZ());
		seller.setHeading(player.getHeading());
		seller.setCurrentHpMp(seller.getMaxHp(), seller.getMaxMp());
		shopObject.setXYZ(player.getX(), player.getY(), player.getZ(), player.getHeading());
		shopObject.setSellerObjectId(seller.getObjectId());
		sellers.put(seller.getObjectId(), player.getObjectId());
		TransformationManager.getInstance().transformPlayer(Rnd.get(255, 257), seller);
	}
	
	public void stopShop(L2PcInstance player)
	{
		ShopObject shopObject = shops.get(player.getObjectId());
		if (shopObject != null)
		{
			L2PcInstance seller = L2World.getInstance().getPlayer(shopObject.getSellerObjectId());
			if (seller != null)
			{
				sellers.remove(seller.getObjectId());
				seller.decayMe();
				seller = null;
			}
			shopObject.setSellerObjectId(0);
		}
	}
	
	public void sellBuff(L2PcInstance seller, L2PcInstance player, int buff)
	{
		ShopObject shop = shops.get(sellers.get(seller.getObjectId()));
		if (shop == null)
		{
			return;
		}
		
		if (!shop.getBuffList().containsKey(buff))
		{
			return;
		}
		
		PrivateBuff privatebuff = shop.getBuffList().get(buff);
		L2Skill buffSkill = L2Skill.valueOf(privatebuff.skillid, privatebuff.skillLevel);
		
		if (buffSkill == null)
		{
			return;
		}
		
		int mpconsume = buffSkill.getMpConsume();
		int price = privatebuff.price;
		
		if (seller.getCurrentMp() < mpconsume)
		{
			player.sendPacket(new ExShowScreenMessage("The seller dont has mana!", 5000));
			showShop(seller, player);
			return;
		}
		
		if (player.getAdena() < price)
		{
			player.sendPacket(new ExShowScreenMessage("You don't have enough money!", 5000));
			showShop(seller, player);
			return;
		}
		
		player.sendPacket(new MagicSkillUse(seller, player, privatebuff.skillid, privatebuff.skillLevel, 1500, 1500));
		seller.setCurrentMp(seller.getCurrentMp() - mpconsume);
		player.reduceAdena("BuffShop", price, seller, true);
		buffSkill.getEffects(seller, player);
		rewardAdena(shop, price);
		showShop(seller, player);
	}
	
	/**
	 * @param seller
	 * @param price
	 */
	private void rewardAdena(ShopObject seller, int price)
	{
		L2PcInstance owner;
		int ownerId = seller.getOwnerId();
		owner = L2World.getInstance().getPlayer(ownerId);
		
		if (owner != null)
		{
			owner.addAdena("BuffShop", price, null, true);
			owner.sendMessage("You've got Adena for buff sell.");
			owner.sendPacket(new PlaySound(QuestSound.ITEMSOUND_QUEST_MIDDLE.getSoundName()));
		}
		else
		{
			offlineMoney.put(ownerId, offlineMoney.containsKey(ownerId) ? offlineMoney.get(ownerId) + price : price);
		}
	}
	
	public void sendOffLinePrice()
	{
		for (Entry<Integer, Integer> entry : offlineMoney.entrySet())
		{
			L2DatabaseFactory.simpleExcuter("INSERT INTO character_premium_items (charId,itemId,itemCount,itemSender) values(?,?,?,?)", entry.getKey(), 57, entry.getValue(), "BuffÓŻŔű");
		}
	}
	
	public void sendOffLinePrice(L2PcInstance activeChar)
	{
		if (offlineMoney.containsKey(activeChar.getObjectId()))
		{
			L2DatabaseFactory.simpleExcuter("INSERT INTO character_premium_items (charId,itemId,itemCount,itemSender) values(?,?,?,?)", activeChar.getObjectId(), 57, offlineMoney.get(activeChar.getObjectId()), "BuffÓŻŔű");
			offlineMoney.remove(activeChar.getObjectId());
		}
	}
	
	public void storeOffliners()
	{
		for (ShopObject shop : shops.values())
		{
			if (!shop.getBuffList().isEmpty())
			{
				L2DatabaseFactory.simpleExcuter(SAVE_SHOP, shop.getOwnerId(), shop.getBuffLine(), shop.getTitle(), shop.getX(), shop.getY(), shop.getZ(), shop.getHeading());
			}
		}
	}
	
	public void restoreOfflineTraders()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(LOAD_SHOPS);
			ResultSet rset = stmt.executeQuery())
		{
			int ownerId;
			String title;
			String[] tmp;
			while (rset.next())
			{
				ownerId = rset.getInt("ownerId");
				tmp = rset.getString("buffs").split(";");
				title = rset.getString("title");
				ShopObject object = new ShopObject(ownerId);
				object.setTitle(title);
				for (String line : tmp)
				{
					object.addBuff(line);
				}
				object.setXYZ(rset.getInt("x"), rset.getInt("y"), rset.getInt("z"), rset.getInt("heading"));
				shops.put(ownerId, object);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		_log.info("Loaded: " + shops.size() + " buffshop trader(s).");
		
		if (!shops.isEmpty()) // spawn shop
		{
			shops.forEach((ownerId, shop) ->
			{
				L2PcInstance seller;
				seller = L2PcInstance.createDummy(CharTemplateTable.getInstance().getTemplate(15), "BUFF SELLER", "", new PcAppearance((byte) 1, (byte) 1, (byte) 1, true), Collections.emptyList());
				seller.getSellList().setTitle(shop.getTitle());
				seller.addSkill(L2Skill.valueOf(10005, 1));// Must enable custom skill;
				seller.setPrivateStoreType(L2PcInstance.STORE_PRIVATE_PACKAGE_SELL);
				seller.broadcastPacket(new PrivateStoreMsgSell(seller));
				seller.spawnMe(shop.getX(), shop.getY(), shop.getZ());
				seller.setHeading(shop.getHeading());
				seller.setCurrentHpMp(seller.getMaxHp(), seller.getMaxMp());
				shop.setSellerObjectId(seller.getObjectId());
				sellers.put(seller.getObjectId(), ownerId);
				TransformationManager.getInstance().transformPlayer(Rnd.get(255, 257), seller);
			});
		}
	}
	
	/**
	 * @return the shops
	 */
	public Map<Integer, ShopObject> getShops()
	{
		return shops;
	}
}