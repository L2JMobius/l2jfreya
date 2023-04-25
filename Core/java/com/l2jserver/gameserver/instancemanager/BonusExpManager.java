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
package com.l2jserver.gameserver.instancemanager;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.L2ItemInstance.ItemLocation;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.PcInventory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class BonusExpManager
{
	private final Logger _log = Logger.getLogger(getClass().getName());
	private final Map<Integer, BonusItem> _bonusItems = new ConcurrentHashMap<>();
	
	public BonusExpManager()
	{
		load();
	}
	
	public static final BonusExpManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private void load()
	{
		try
		{
			int itemId = 0;
			double bonusExp = 0;
			double bonusSp = 0;
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			
			File file = new File(Config.DATAPACK_ROOT + "/data/xml/BonusExpItems/BonusExpItems.xml");
			if (!file.exists())
			{
				_log.log(Level.INFO, "Missing " + Config.DATAPACK_ROOT + "/data/xml/BonusExpItems.xml - The script wont work without it!");
				return;
			}
			
			Document doc = factory.newDocumentBuilder().parse(file);
			Node first = doc.getFirstChild();
			if ((first != null) && "list".equalsIgnoreCase(first.getNodeName()))
			{
				for (Node n = first.getFirstChild(); n != null; n = n.getNextSibling())
				{
					if ("bonus".equalsIgnoreCase(n.getNodeName()))
					{
						Node att;
						
						for (Node cd = n.getFirstChild(); cd != null; cd = cd.getNextSibling())
						{
							if ("item".equalsIgnoreCase(cd.getNodeName()))
							{
								att = cd.getAttributes().getNamedItem("id");
								if (att != null)
								{
									itemId = Integer.parseInt(att.getNodeValue());
								}
								else
								{
									_log.severe("[" + getClass().getSimpleName() + "]Missing: Itemid -> skipping");
									continue;
								}
								
								att = cd.getAttributes().getNamedItem("exp");
								if (att != null)
								{
									bonusExp = Double.parseDouble(att.getNodeValue());
								}
								else
								{
									_log.severe("[" + getClass().getSimpleName() + "]Missing: exp -> skipping");
									continue;
								}
								
								att = cd.getAttributes().getNamedItem("sp");
								if (att != null)
								{
									bonusSp = Double.parseDouble(att.getNodeValue());
								}
								else
								{
									_log.severe("[" + getClass().getSimpleName() + "]Missing: sp -> skipping");
									continue;
								}
								
								_bonusItems.put(itemId, new BonusItem(bonusExp, bonusSp));
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		_log.info("Loaded: " + _bonusItems.size() + " Bonus Xp/Sp items.");
	}
	
	public double[] getBonusExpAndSp(L2PcInstance player)
	{
		double bonusExp = 0;
		double bonusSp = 0;
		if (player != null)
		{
			PcInventory inv = player.getInventory();
			
			for (int itemId : _bonusItems.keySet())
			{
				L2ItemInstance item = inv.getItemByItemId(itemId);
				if ((item != null) && (item.getLocation() == ItemLocation.INVENTORY))
				{
					BonusItem bonus = _bonusItems.get(itemId);
					bonusExp = Math.max(bonusExp, bonus.getBonusExpMultiplyer());
					bonusSp = Math.max(bonusSp, bonus.getBonusSpMultiplyer());
				}
			}
		}
		return new double[]
		{
			bonusExp,
			bonusSp
		};
	}
	
	private final class BonusItem
	{
		private final double _bonusExp;
		private final double _bonusSp;
		
		public BonusItem(double exp, double sp)
		{
			_bonusExp = exp;
			_bonusSp = sp;
		}
		
		public double getBonusExpMultiplyer()
		{
			return _bonusExp;
		}
		
		public double getBonusSpMultiplyer()
		{
			return _bonusSp;
		}
	}
	
	private static class SingletonHolder
	{
		protected static final BonusExpManager _instance = new BonusExpManager();
	}
}