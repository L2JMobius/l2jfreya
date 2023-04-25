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
package com.l2jserver.scripts.handlers.communityboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.Shutdown;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.communitybbs.Managers.BaseBBSManager;
import com.l2jserver.gameserver.datatables.CharNameTable;
import com.l2jserver.gameserver.handler.interfaces.IBypassHandler;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author L2jPDT
 */
public class AdminControl implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"grvall",
		"grv",
		"admin_server_shutdown",
		"admin_server_restart",
		"admin_server_abort"
	};
	
	@Override
	public boolean useBypass(String bypass, L2PcInstance activeChar, L2Character target)
	{
		if ((bypass.startsWith("grv")) || (bypass.startsWith("grvall")))
		{
			if (activeChar.isGM())
			{
				if (bypass.startsWith("grvall"))
				{
					bypass = "grv all 1";
					parsecmd(bypass, activeChar);
				}
				else if (bypass.startsWith("grv"))
				{
					parsecmd(bypass, activeChar);
				}
			}
			else
			{
				activeChar.sendMessage("Admin Control: You are not allowed to use this function.");
			}
		}
		else if (bypass.startsWith("admin_server_shutdown"))
		{
			try
			{
				int val = Integer.parseInt(bypass.substring(22));
				Shutdown.getInstance().startShutdown(activeChar, val, false);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Admin Control: Something bad. Server cannot be shutdown.");
			}
		}
		else if (bypass.startsWith("admin_server_restart"))
		{
			try
			{
				int val = Integer.parseInt(bypass.substring(21));
				Shutdown.getInstance().startShutdown(activeChar, val, true);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Admin Control: Something bad. Server cannot be restarted.");
			}
		}
		else if (bypass.startsWith("admin_server_abort"))
		{
			Shutdown.getInstance().abort(activeChar);
		}
		return true;
		
	}
	
	/**
	 * @param bypass
	 * @param activeChar
	 */
	private void parsecmd(String bypass, L2PcInstance activeChar)
	{
		String html = HtmCache.getInstance().getHtm(activeChar, "data/html/CommunityBoard/report_bug_bot_messages.htm");
		html = html.replace("%playername%", activeChar.getName());
		html = html.replace("%servername%", Config.SERVER_NAME);
		String[] event = bypass.split(" ");
		if (bypass.startsWith("grv delete "))
		{
			String[] params = bypass.split(" ");
			String num = (params[2]);
			L2DatabaseFactory.simpleExcuter("DELETE FROM gm_message_box WHERE number = ?", num);
			html = html.replace("%list%", showTopicList(activeChar, 1, "all", "view"));
			activeChar.sendMessage("You have delete topic ID: " + num);
		}
		else if (event[1].equals("all"))
		{
			int page = Integer.parseInt(event[2]);
			html = html.replace("%list%", showTopicList(activeChar, page, "all", "view"));
		}
		else if (event[1].equals("view"))
		{
			int num = Integer.parseInt(event[2]);
			String type = event[3];
			int page = Integer.parseInt(event[4]);
			html = HtmCache.getInstance().getHtm(activeChar, "data/html/CommunityBoard/report_bug_message.htm");
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement("SELECT * FROM gm_message_box WHERE number = ?"))
			{
				stmt.setInt(1, num);
				try (ResultSet rset = stmt.executeQuery())
				{
					if (rset.next())
					{
						html = html.replace("%title%", rset.getString("title"));
						html = html.replace("%content%", rset.getString("content"));
						html = html.replace("%date%", rset.getString("date"));
						html = html.replace("%author%", CharNameTable.getInstance().getNameById(rset.getInt("author")) == null ? "NONE" : CharNameTable.getInstance().getNameById(rset.getInt("author")));
						html = html.replace("%type%", type);
						html = html.replace("%page%", String.valueOf(page));
						html = html.replace("%MESSAGE_ID%", rset.getString("number"));
						html = html.replace("%DELETE%", "<button action=\"bypass -h grv delete " + rset.getString("number") + "\" value=\"Delete\" width=100 height=23 back=\"Button_DF_Large_Down\" fore=\"Button_DF_Large_Over\">");
					}
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			int page = Integer.parseInt(event[2]);
			html = html.replace("%list%", showTopicList(activeChar, page, event[1], "view"));
		}
		BaseBBSManager.separateAndSend(html, activeChar);
	}
	
	private String showTopicList(L2PcInstance activeChar, int page, String type, String operation)
	{
		StringBuilder builder = new StringBuilder(768);
		String sql = "SELECT * FROM gm_message_box order by number DESC Limit ?,12";
		String sql1 = "SELECT count(author) FROM gm_message_box";
		if (type.equals("my"))
		{
			sql = "SELECT * FROM bbs_article WHERE author=? order by number";
		}
		else if (!type.equals("all"))
		{
			sql = "SELECT * FROM gm_message_box WHERE type=? order by number DESC Limit ?,12";
			sql1 = "SELECT count(author) FROM gm_message_box WHERE type=?";
		}
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			PreparedStatement stmt1 = con.prepareStatement(sql1))
		{
			int start = (page - 1) * 12;
			if (type.equals("all"))
			{
				stmt.setInt(1, start);
			}
			else
			{
				stmt.setString(1, type);
				stmt.setInt(2, start);
				stmt1.setString(1, type);
			}
			
			try (ResultSet rset = stmt.executeQuery())
			{
				Article aritcle;
				while (rset.next())
				{
					aritcle = new Article(rset);
					drawTopic(builder, aritcle, page, type, operation);
				}
			}
			
			int count = 0;
			try (ResultSet rset = stmt1.executeQuery())
			{
				if (rset.next())
				{
					count = rset.getInt("count(author)");
				}
			}
			
			int pages = (count % 12) == 0 ? count / 12 : (count / 12) + 1;
			
			builder.append("<br>");
			builder.append("<table>");
			if ((page > 1) && (page < pages))
			{
				builder.append("<tr>");
				builder.append("<td>").append("<button action=\"bypass grv " + type + " " + (page - 1) + "\" value=\"Prev\" width=40 height=25 back=\"L2UI_CT1.Button_DF_Msn_down\" fore=\"L2UI_CT1.Button_DF_Msn\">").append("</td>");
				builder.append("<td>").append("<button action=\"bypass grv " + type + " " + (page + 1) + "\" value=\"Next\" width=40 height=25 back=\"L2UI_CT1.Button_DF_Msn_down\" fore=\"L2UI_CT1.Button_DF_Msn\">").append("</td>");
				builder.append("</tr>");
			}
			else if ((page == pages) && (pages > 1)) // last page
			{
				builder.append("<tr>");
				builder.append("<td>").append("<button action=\"bypass grv " + type + " " + (page - 1) + "\" value=\"Prev\" width=40 height=25 back=\"L2UI_CT1.Button_DF_Msn_down\" fore=\"L2UI_CT1.Button_DF_Msn\">").append("</td>");
				builder.append("</tr>");
			}
			else if ((page == 1) && (pages > 1)) // first page and there are more than one page
			{
				builder.append("<tr>");
				builder.append("<td>").append("<button action=\"bypass grv " + type + " " + (page + 1) + "\" value=\"Next\" width=40 height=25 back=\"L2UI_CT1.Button_DF_Msn_down\" fore=\"L2UI_CT1.Button_DF_Msn\">").append("</td>");
				builder.append("</tr>");
			}
			builder.append("</table>");
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		return builder.toString();
	}
	
	private String drawAricleType(Article article)
	{
		String type = article.getType();
		if (type.equals("Bug"))
		{
			type = "[<font color=00FF00>" + type + "</font>]";
		}
		else if (type.equals("Bot"))
		{
			type = "[<font color=1C86EE>" + type + "</font>]";
		}
		else if (type.equals("Missing"))
		{
			type = "[<font color=FF3030>" + type + "</font>]";
		}
		else if (type.equals("Other"))
		{
			type = "[<font color=9F79EE>" + type + "</font>]";
		}
		else if (type.equals("Idea"))
		{
			type = "[<font color=LEVEL>" + type + "</font>]";
		}
		return type;
	}
	
	private String drawTitle(Article article)
	{
		String title = article.getTitle();
		if (title.length() > 30)
		{
			title = title.substring(0, 29);
			title += "...";
		}
		
		return title;
	}
	
	/**
	 * @param builder
	 * @param article
	 * @param page
	 * @param type
	 * @param operation
	 */
	private void drawTopic(StringBuilder builder, Article article, int page, String type, String operation)
	{
		builder.append("<table width=730 height=22 cellspacing=0 cellpadding=-1>");
		builder.append("<tr>");
		builder.append("<td fixwidth=10></td>");
		builder.append("<td fixwidth=100>").append(drawAricleType(article)).append("</td>");
		builder.append("<td fixwidth=340>").append(drawTitle(article)).append("</td>");
		builder.append("<td fixwidth=140>").append(CharNameTable.getInstance().getNameById(article.getAuthor())).append("</td>");
		builder.append("<td fixwidth=100>").append(article.getDate()).append("</td>");
		builder.append("<td fixwidth=60>").append("<a action=\"bypass -h grv " + operation + " " + article.getNum() + " " + type + " " + page + "\">" + operation + "</a>").append("</td>");
		builder.append("</tr>");
		builder.append("</table>");
		builder.append("<img src=\"L2UI.SquareGray\" width=755 height=1>");
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
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}