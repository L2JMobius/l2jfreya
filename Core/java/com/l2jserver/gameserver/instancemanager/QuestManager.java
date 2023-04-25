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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.l2jserver.gameserver.model.quest.Quest;

public class QuestManager
{
	protected static final Logger _log = Logger.getLogger(QuestManager.class.getName());
	
	public static final QuestManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private final Map<String, Quest> _quests = new ConcurrentHashMap<>();
	private final Map<String, Quest> _scripts = new ConcurrentHashMap<>();
	
	private QuestManager()
	{
	}
	
	public final void report()
	{
		_log.info("Loaded: " + _quests.size() + " quests");
		_log.info("Loaded: " + _scripts.size() + " scripts");
	}
	
	public final void save()
	{
		for (Quest q : _quests.values())
		{
			q.saveGlobalData();
		}
		
		for (Quest script : _scripts.values())
		{
			script.saveGlobalData();
		}
	}
	
	public Quest getQuest(String name)
	{
		if (_quests.containsKey(name))
		{
			return _quests.get(name);
		}
		return _scripts.get(name);
	}
	
	public final Quest getQuest(int questId)
	{
		for (Quest q : _quests.values())
		{
			if (q.getQuestId() == questId)
			{
				return q;
			}
		}
		return null;
	}
	
	public void addQuest(Quest quest)
	{
		if (quest == null)
		{
			throw new IllegalArgumentException("Quest argument cannot be null");
		}
		_quests.put(quest.getName(), quest);
	}
	
	public void addScript(Quest script)
	{
		if (script == null)
		{
			throw new IllegalArgumentException("Script argument cannot be null");
		}
		_scripts.put(script.getClass().getSimpleName(), script);
	}
	
	public Iterable<Quest> getAllManagedScripts()
	{
		return _quests.values();
	}
	
	/**
	 * Gets all the registered scripts.
	 * @return all the scripts
	 */
	public Map<String, Quest> getScripts()
	{
		return _scripts;
	}
	
	public Map<String, Quest> getQuests()
	{
		return _quests;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final QuestManager _instance = new QuestManager();
	}
}
