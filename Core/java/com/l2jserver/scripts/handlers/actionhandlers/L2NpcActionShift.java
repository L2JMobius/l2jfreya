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
package com.l2jserver.scripts.handlers.actionhandlers;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.l2jserver.Config;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.communitybbs.Managers.BaseBBSManager;
import com.l2jserver.gameserver.handler.interfaces.IActionHandler;
import com.l2jserver.gameserver.idfactory.IdFactory;
import com.l2jserver.gameserver.instancemanager.WalkingManager;
import com.l2jserver.gameserver.model.Elementals;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2Object.InstanceType;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2MerchantInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.MyTargetSelected;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.PledgeCrest;
import com.l2jserver.gameserver.network.serverpackets.StatusUpdate;

import gov.nasa.worldwind.formats.dds.DDSConverter;

public class L2NpcActionShift implements IActionHandler
{
	/**
	 * Manage and Display the GM console to modify the L2NpcInstance (GM only).<BR>
	 * <BR>
	 * <B><U> Actions (If the L2PcInstance is a GM only)</U> :</B><BR>
	 * <BR>
	 * <li>Set the L2NpcInstance as target of the L2PcInstance player (if necessary)</li>
	 * <li>Send a Server->Client packet MyTargetSelected to the L2PcInstance player (display the select window)</li>
	 * <li>If L2NpcInstance is autoAttackable, send a Server->Client packet StatusUpdate to the L2PcInstance in order to update L2NpcInstance HP bar</li>
	 * <li>Send a Server->Client NpcHtmlMessage() containing the GM console about this L2NpcInstance</li><BR>
	 * <BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : Each group of Server->Client packet must be terminated by a ActionFailed packet in order to avoid that client wait an other packet</B></FONT><BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Client packet : Action</li><BR>
	 * <BR>
	 */
	@Override
	public boolean action(L2PcInstance activeChar, L2Object target, boolean interact)
	{
		// Check if the L2PcInstance is a GM
		if (activeChar.getAccessLevel().isGm())
		{
			// Set the target of the L2PcInstance activeChar
			activeChar.setTarget(target);
			
			// Send a Server->Client packet MyTargetSelected to the L2PcInstance activeChar
			// The activeChar.getLevel() - getLevel() permit to display the correct color in the select window
			MyTargetSelected my = new MyTargetSelected(target.getObjectId(), activeChar.getLevel() - ((L2Character) target).getLevel());
			activeChar.sendPacket(my);
			
			// Check if the activeChar is attackable (without a forced attack)
			if (target.isAutoAttackable(activeChar))
			{
				// Send a Server->Client packet StatusUpdate of the L2NpcInstance to the L2PcInstance to update its HP bar
				StatusUpdate su = new StatusUpdate(target);
				su.addAttribute(StatusUpdate.CUR_HP, (int) ((L2Character) target).getCurrentHp());
				su.addAttribute(StatusUpdate.MAX_HP, ((L2Character) target).getMaxHp());
				activeChar.sendPacket(su);
			}
			
			String html = HtmCache.getInstance().getHtm(activeChar, "data/html/admin/npcinfo.htm");
			
			try
			{
				int npcId = ((L2Npc) target).getTemplate().npcId;
				int imageOID = 999000000 + npcId;
				
				File image = new File("data/images/" + npcId + ".jpg");
				BufferedImage bi = ImageIO.read(image);
				ImageIO.write(bi, "jpg", image);
				PledgeCrest packet = new PledgeCrest(imageOID, DDSConverter.convertToDDS(image).array());
				activeChar.sendPacket(packet);
				html = html.replace("%img%", "Crest.crest_" + Config.SERVER_ID + "_" + imageOID);
			}
			catch (Exception e)
			{
				_log.warning(e.getMessage());
			}
			
			html = html.replace("%objid%", String.valueOf(target.getObjectId()));
			html = html.replace("%class%", target.getClass().getSimpleName());
			html = html.replace("%id%", String.valueOf(((L2Npc) target).getTemplate().npcId));
			html = html.replace("%lvl%", String.valueOf(((L2Npc) target).getTemplate().level));
			html = html.replace("%name%", String.valueOf(((L2Npc) target).getTemplate().name));
			html = html.replace("%tmplid%", String.valueOf(((L2Npc) target).getTemplate().npcId));
			html = html.replace("%aggro%", String.valueOf((target instanceof L2Attackable) ? ((L2Attackable) target).getAggroRange() : 0));
			
			html = html.replace("%get_hp%", "" + (int) ((L2Character) target).getCurrentHp());
			html = html.replace("%get_max_hp%", "" + ((L2Character) target).getMaxHp());
			html = html.replace("%p_get_hp%", percent((int) ((L2Character) target).getCurrentHp(), ((L2Character) target).getMaxHp()));
			
			html = html.replace("%get_mp%", "" + (int) ((L2Character) target).getCurrentMp());
			html = html.replace("%get_max_mp%", "" + ((L2Character) target).getMaxMp());
			html = html.replace("%p_get_mp%", percent((int) ((L2Character) target).getCurrentMp(), ((L2Character) target).getMaxMp()));
			
			html = html.replace("%patk%", String.valueOf(((L2Character) target).getPAtk(null)));
			html = html.replace("%matk%", String.valueOf(((L2Character) target).getMAtk(null, null)));
			html = html.replace("%pdef%", String.valueOf(((L2Character) target).getPDef(null)));
			html = html.replace("%mdef%", String.valueOf(((L2Character) target).getMDef(null, null)));
			html = html.replace("%accu%", String.valueOf(((L2Character) target).getAccuracy()));
			html = html.replace("%evas%", String.valueOf(((L2Character) target).getEvasionRate(null)));
			html = html.replace("%crit%", String.valueOf(((L2Character) target).getCriticalHit(null, null)));
			html = html.replace("%rspd%", String.valueOf(((L2Character) target).getRunSpeed()));
			html = html.replace("%aspd%", String.valueOf(((L2Character) target).getPAtkSpd()));
			html = html.replace("%cspd%", String.valueOf(((L2Character) target).getMAtkSpd()));
			html = html.replace("%str%", String.valueOf(((L2Character) target).getSTR()));
			html = html.replace("%dex%", String.valueOf(((L2Character) target).getDEX()));
			html = html.replace("%con%", String.valueOf(((L2Character) target).getCON()));
			html = html.replace("%int%", String.valueOf(((L2Character) target).getINT()));
			html = html.replace("%wit%", String.valueOf(((L2Character) target).getWIT()));
			html = html.replace("%men%", String.valueOf(((L2Character) target).getMEN()));
			html = html.replace("%loc%", String.valueOf(target.getX() + " " + target.getY() + " " + target.getZ()));
			html = html.replace("%dist%", String.valueOf((int) Math.sqrt(activeChar.getDistanceSq(target))));
			
			byte attackAttribute = ((L2Character) target).getAttackElement();
			html = html.replace("%ele_atk%", Elementals.getElementName(attackAttribute));
			html = html.replace("%ele_atk_value%", String.valueOf(((L2Character) target).getAttackElementValue(attackAttribute)));
			html = html.replace("%ele_dfire%", String.valueOf(((L2Character) target).getDefenseElementValue(Elementals.FIRE)));
			html = html.replace("%ele_dwater%", String.valueOf(((L2Character) target).getDefenseElementValue(Elementals.WATER)));
			html = html.replace("%ele_dwind%", String.valueOf(((L2Character) target).getDefenseElementValue(Elementals.WIND)));
			html = html.replace("%ele_dearth%", String.valueOf(((L2Character) target).getDefenseElementValue(Elementals.EARTH)));
			html = html.replace("%ele_dholy%", String.valueOf(((L2Character) target).getDefenseElementValue(Elementals.HOLY)));
			html = html.replace("%ele_ddark%", String.valueOf(((L2Character) target).getDefenseElementValue(Elementals.DARK)));
			
			if (((L2Npc) target).getSpawn() != null)
			{
				html = html.replace("%spawn%", ((L2Npc) target).getSpawn().getLocx() + " " + ((L2Npc) target).getSpawn().getLocy() + " " + ((L2Npc) target).getSpawn().getLocz());
				html = html.replace("%loc2d%", String.valueOf((int) Math.sqrt(((L2Character) target).getPlanDistanceSq(((L2Npc) target).getSpawn().getLocx(), ((L2Npc) target).getSpawn().getLocy()))));
				html = html.replace("%loc3d%", String.valueOf((int) Math.sqrt(((L2Character) target).getDistanceSq(((L2Npc) target).getSpawn().getLocx(), ((L2Npc) target).getSpawn().getLocy(), ((L2Npc) target).getSpawn().getLocz()))));
				html = html.replace("%resp%", String.valueOf(((L2Npc) target).getSpawn().getRespawnDelay() / 1000));
			}
			else
			{
				html = html.replace("%spawn%", "<font color=FF0000>null</font>");
				html = html.replace("%loc2d%", "<font color=FF0000>--</font>");
				html = html.replace("%loc3d%", "<font color=FF0000>--</font>");
				html = html.replace("%resp%", "<font color=FF0000>--</font>");
			}
			
			html = html.replace("%spawn_name%", "" + ((L2Npc) target).getSpawn().getName());
			html = html.replace("%ai_intention%", "" + String.valueOf(((L2Npc) target).getAI().getIntention().name()));
			html = html.replace("%ai%", "" + ((L2Npc) target).getAI().getClass().getSimpleName());
			html = html.replace("%ai_type%", "" + String.valueOf(((L2Npc) target).getAiType()));
			html = html.replace("%ai_clan%", "" + String.valueOf(((L2Npc) target).getTemplate().getAIDataStatic().getClan()) + " " + String.valueOf(((L2Npc) target).getTemplate().getAIDataStatic().getClanRange()));
			html = html.replace("%ai_enemy_clan%", "" + String.valueOf(((L2Npc) target).getTemplate().getAIDataStatic().getEnemyClan()) + " " + String.valueOf(((L2Npc) target).getTemplate().getAIDataStatic().getEnemyRange()));
			html = html.replace("%ai_visible%", "" + ((L2Npc) target).isVisible());
			html = html.replace("%ai_script_value%", "" + ((L2Npc) target).getScriptValue());
			html = html.replace("%ai_script_name%", "" + ((L2Npc) target).getScriptValue());
			
			final String routeName = WalkingManager.getInstance().getRouteName((L2Npc) target);
			if (!routeName.isEmpty())
			{
				html = html.replace("%route%", "<tr><td><table width=270 border=0><tr><td width=100><font color=LEVEL>Route:</font></td><td align=right width=170>" + routeName + "</td></tr></table></td></tr>");
			}
			else
			{
				html = html.replace("%route%", "");
			}
			if (target instanceof L2MerchantInstance)
			{
				html = html.replace("%butt%", "<button value=\"Shop\" action=\"bypass -h admin_showShop " + String.valueOf(((L2Npc) target).getTemplate().npcId) + "\" width=60 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
			}
			else
			{
				html = html.replace("%butt%", "");
			}
			
			BaseBBSManager.separateAndSend(html, activeChar);
		}
		else if (Config.ALT_GAME_VIEWNPC)
		{
			
			// Set the target of the L2PcInstance activeChar
			activeChar.setTarget(target);
			
			// Send a Server->Client packet MyTargetSelected to the L2PcInstance activeChar
			// The activeChar.getLevel() - getLevel() permit to display the correct color in the select window
			MyTargetSelected my = new MyTargetSelected(target.getObjectId(), activeChar.getLevel() - ((L2Character) target).getLevel());
			activeChar.sendPacket(my);
			
			// Check if the activeChar is attackable (without a forced attack)
			if (target.isAutoAttackable(activeChar))
			{
				// Send a Server->Client packet StatusUpdate of the L2NpcInstance to the L2PcInstance to update its HP bar
				StatusUpdate su = new StatusUpdate(target);
				su.addAttribute(StatusUpdate.CUR_HP, (int) ((L2Character) target).getCurrentHp());
				su.addAttribute(StatusUpdate.MAX_HP, ((L2Character) target).getMaxHp());
				activeChar.sendPacket(su);
			}
			if (target.isMonster())
			{
				NpcHtmlMessage html = new NpcHtmlMessage(5);
				html.setFile(activeChar, "data/html/npcinfo.htm");
				try
				{
					int npcId = ((L2Npc) target).getTemplate().npcId;
					int imageOID = IdFactory.getInstance().getNextId();
					
					File image = new File("data/images/" + npcId + ".jpg");
					BufferedImage bi = ImageIO.read(image);
					ImageIO.write(bi, "jpg", image);
					PledgeCrest packet = new PledgeCrest(imageOID, DDSConverter.convertToDDS(image).array());
					activeChar.sendPacket(packet);
					html.replace("%img%", "Crest.crest_" + Config.SERVER_ID + "_" + imageOID);
				}
				catch (Exception e)
				{
					_log.warning(e.getMessage());
				}
				
				html.replace("%lvl%", String.valueOf(((L2Npc) target).getTemplate().level));
				html.replace("%name%", String.valueOf(((L2Npc) target).getTemplate().name));
				html.replace("%aggro%", String.valueOf((target instanceof L2Attackable) ? ((L2Attackable) target).getAggroRange() : 0));
				
				html.replace("%get_hp%", "" + (int) ((L2Character) target).getCurrentHp());
				html.replace("%get_max_hp%", "" + ((L2Character) target).getMaxHp());
				html.replace("%p_get_hp%", percent((int) ((L2Character) target).getCurrentHp(), ((L2Character) target).getMaxHp()));
				
				html.replace("%get_mp%", "" + (int) ((L2Character) target).getCurrentMp());
				html.replace("%get_max_mp%", "" + ((L2Character) target).getMaxMp());
				html.replace("%p_get_mp%", percent((int) ((L2Character) target).getCurrentMp(), ((L2Character) target).getMaxMp()));
				
				html.replace("%patk%", String.valueOf(((L2Character) target).getPAtk(null)));
				html.replace("%matk%", String.valueOf(((L2Character) target).getMAtk(null, null)));
				html.replace("%pdef%", String.valueOf(((L2Character) target).getPDef(null)));
				html.replace("%mdef%", String.valueOf(((L2Character) target).getMDef(null, null)));
				html.replace("%accu%", String.valueOf(((L2Character) target).getAccuracy()));
				html.replace("%evas%", String.valueOf(((L2Character) target).getEvasionRate(null)));
				html.replace("%crit%", String.valueOf(((L2Character) target).getCriticalHit(null, null)));
				html.replace("%rspd%", String.valueOf(((L2Character) target).getRunSpeed()));
				html.replace("%aspd%", String.valueOf(((L2Character) target).getPAtkSpd()));
				html.replace("%cspd%", String.valueOf(((L2Character) target).getMAtkSpd()));
				html.replace("%str%", String.valueOf(((L2Character) target).getSTR()));
				html.replace("%dex%", String.valueOf(((L2Character) target).getDEX()));
				html.replace("%con%", String.valueOf(((L2Character) target).getCON()));
				html.replace("%int%", String.valueOf(((L2Character) target).getINT()));
				html.replace("%wit%", String.valueOf(((L2Character) target).getWIT()));
				html.replace("%men%", String.valueOf(((L2Character) target).getMEN()));
				html.replace("%loc%", String.valueOf(target.getX() + " " + target.getY() + " " + target.getZ()));
				html.replace("%dist%", String.valueOf((int) Math.sqrt(activeChar.getDistanceSq(target))));
				
				byte attackAttribute = ((L2Character) target).getAttackElement();
				html.replace("%ele_atk%", Elementals.getElementName(attackAttribute));
				html.replace("%ele_atk_value%", String.valueOf(((L2Character) target).getAttackElementValue(attackAttribute)));
				html.replace("%ele_dfire%", String.valueOf(((L2Character) target).getDefenseElementValue(Elementals.FIRE)));
				html.replace("%ele_dwater%", String.valueOf(((L2Character) target).getDefenseElementValue(Elementals.WATER)));
				html.replace("%ele_dwind%", String.valueOf(((L2Character) target).getDefenseElementValue(Elementals.WIND)));
				html.replace("%ele_dearth%", String.valueOf(((L2Character) target).getDefenseElementValue(Elementals.EARTH)));
				html.replace("%ele_dholy%", String.valueOf(((L2Character) target).getDefenseElementValue(Elementals.HOLY)));
				html.replace("%ele_ddark%", String.valueOf(((L2Character) target).getDefenseElementValue(Elementals.DARK)));
				
				if (((L2Npc) target).getSpawn() != null)
				{
					html.replace("%resp%", String.valueOf(((L2Npc) target).getSpawn().getRespawnDelay() / 1000));
				}
				else
				{
					html.replace("%resp%", "<font color=FF0000>--</font>");
				}
				
				html.replace("%base_xp%", String.valueOf(((L2Npc) target).getTemplate().rewardExp));
				html.replace("%base_sp%", String.valueOf(((L2Npc) target).getTemplate().rewardSp));
				html.replace("%current_rate_xp%", String.valueOf(((L2Npc) target).getTemplate().rewardExp * Config.RATE_XP));
				html.replace("%current_rate_sp%", String.valueOf(((L2Npc) target).getTemplate().rewardSp * Config.RATE_SP));
				if (((L2Npc) target).getLevel() < activeChar.getLevel())
				{
					html.replace("%chance_to_kill%", "<font color=LEVEL> Higher</font>");
				}
				else if (((L2Npc) target).getLevel() > activeChar.getLevel())
				{
					html.replace("%chance_to_kill%", "<font color=LEVEL> Lower</font>");
				}
				else if (((L2Npc) target).getLevel() == activeChar.getLevel())
				{
					html.replace("%chance_to_kill%", "<font color=LEVEL> Normal</font>");
				}
				
				html.replace("%playerhelp%", "<button value=\"SoS\" action=\"bypass -h _playerhelp\" width=60 height=25 back=\"Button_DF_Down\" fore=\"Button_DF\">");
				html.replace("%droplist%", "<button value=\"Drop\" action=\"bypass -h _npc_droplist\" width=60 height=25 back=\"Button_DF_Down\" fore=\"Button_DF\">");
				BaseBBSManager.separateAndSend(html.getHtm(), activeChar);
			}
		}
		return true;
	}
	
	private String percent(int current, int max)
	{
		if (max == 0)
		{
			return "0";
		}
		return String.valueOf((current * 100) / max);
	}
	
	@Override
	public InstanceType getInstanceType()
	{
		return InstanceType.L2Npc;
	}
}
