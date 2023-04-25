/*
 * Copyright (C) 2004-2014 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.communitybbs.Managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author L2jPrivateDevelopersTeam
 */
public class GMReportFromPlayer extends BaseBBSManager
{
	private static GMReportFromPlayer instance = new GMReportFromPlayer();
	private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY");
	
	public static GMReportFromPlayer getInstance()
	{
		return instance;
	}
	
	private GMReportFromPlayer()
	{
	}
	
	@Override
	public void parsecmd(String command, L2PcInstance activeChar)
	{
		String html = HtmCache.getInstance().getHtm(activeChar, "data/html/CommunityBoard/report_bug_bot.htm");
		String[] event = command.split(" ");
		if (event[1].equals("new"))
		{
			String type = event[2];
			String[] topic = command.split(" / ");
			
			if (topic.length != 3)
			{
				html = "<html><body><br><br><center><font name=\"hs12\" color=\"LEVEL\">Please fill in complete information!</font></center></body></html>";
				separateAndSend(html, activeChar);
				return;
			}
			
			String subject = topic[1];
			String content = topic[2];
			
			if ((content.length() < 255) && (subject.length() < 30))
			{
				content = content.replaceAll("[\\r][\\n]", "<br1>");
				
				if (L2DatabaseFactory.simpleExcuter("INSERT INTO gm_message_box (author,type,title,content,date) values(?,?,?,?,?)", activeChar.getObjectId(), type, subject, content, formatter.format(new Date())) > 0)
				{
					html = "<html><body><br><br><center><font name=\"hs12\" color=\"LEVEL\">Thank you for your post!</font></center></body></html>";
				}
				else
				{
					html = "<html><body><br><br><center><font name=\"hs12\" color=\"LEVEL\">Post Failure!</font></center></body></html>";
				}
			}
			else
			{
				html = "<html><body><br><br><center><font name=\"hs12\" color=\"LEVEL\">Charactes is to long!</font></center></body></html>";
			}
		}
		separateAndSend(html, activeChar);
	}
	
	class Article
	{
		int num;
		int author;
		String type;
		String title;
		String date;
		
		/**
		 * @return the date
		 */
		public String getDate()
		{
			return date;
		}
		
		Article(ResultSet rset) throws SQLException
		{
			num = rset.getInt("number");
			author = rset.getInt("author");
			type = rset.getString("type");
			title = rset.getString("title");
			date = rset.getString("date");
		}
		
		/**
		 * @param num
		 * @param author
		 * @param type
		 * @param title
		 * @param date
		 */
		public Article(int num, int author, String type, String title, String date)
		{
			super();
			this.num = num;
			this.author = author;
			this.type = type;
			this.title = title;
			this.date = date;
		}
		
		/**
		 * @return the num
		 */
		public int getNum()
		{
			return num;
		}
		
		/**
		 * @return the author
		 */
		public int getAuthor()
		{
			return author;
		}
		
		/**
		 * @return the type
		 */
		public String getType()
		{
			return type;
		}
		
		/**
		 * @return the title
		 */
		public String getTitle()
		{
			return title;
		}
	}
	
	@Override
	public void parsewrite(String ar1, String ar2, String ar3, String ar4, String ar5, L2PcInstance activeChar)
	{
		// TODO Auto-generated method stub
	}
}
