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
package com.l2jserver.gameserver.taskmanager.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.taskmanager.Task;
import com.l2jserver.gameserver.taskmanager.TaskManager;
import com.l2jserver.gameserver.taskmanager.TaskManager.ExecutedTask;
import com.l2jserver.gameserver.taskmanager.TaskTypes;
import com.l2jserver.scripts.quests.Q00458_PerfectForm;
import com.l2jserver.scripts.quests.Q00461_RumbleInTheBase;
import com.l2jserver.scripts.quests.Q00463_IMustBeaGenius;
import com.l2jserver.scripts.quests.Q00464_Oath;

/**
 ** @author Gnacik
 */
public class TaskDailyQuestClean extends Task
{
	private static final Logger _log = Logger.getLogger(TaskDailyQuestClean.class.getName());
	
	private static final String NAME = "daily_quest_clean";
	
	private static final String[] _daily_names =
	{
		Q00463_IMustBeaGenius.class.getSimpleName(),
		Q00464_Oath.class.getSimpleName(),
		Q00458_PerfectForm.class.getSimpleName(),
		Q00461_RumbleInTheBase.class.getSimpleName(),
	};
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("DELETE FROM character_quests WHERE name=? AND var='<state>' AND value='Completed';"))
		{
			for (String name : _daily_names)
			{
				statement.setString(1, name);
				statement.execute();
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not reset daily quests: " + e);
		}
		_log.config("Daily quests cleared");
	}
	
	/**
	 * @see com.l2jserver.gameserver.taskmanager.Task#initializate()
	 */
	@Override
	public void initializate()
	{
		super.initializate();
		TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_GLOBAL_TASK, "1", "06:30:00", "");
	}
	
}
