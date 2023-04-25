/*
 * Copyright (C) 2004-2018 L2J Server
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
package com.l2jserver.scripts.l2jpdt_npcs.MonsterAchievements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.l2jserver.gameserver.model.quest.Quest;

/**
 * @author YuKun (L2jPDT)
 */
public class AchievementData
{
	static final Logger LOG = Logger.getLogger(Quest.class.getSimpleName());
	static final File DATA_FOLDER = new File("data/achievement");
	static Map<Integer, Achievement> ACHIEVEMENT = new HashMap<>();
	
	public static void loadData()
	{
		Gson gson = new Gson();
		ACHIEVEMENT.clear();
		for (File file : DATA_FOLDER.listFiles(f -> f.getName().endsWith("json")))
		{
			try
			{
				Achievement achievement = gson.fromJson(new JsonReader(new FileReader(file)), Achievement.class);
				ACHIEVEMENT.put(achievement.mobId, achievement);
			}
			catch (JsonIOException | JsonSyntaxException | FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		LOG.warning("L2jPDT: Monsters Achievements loaded " + ACHIEVEMENT.size() + " Data ");
	}
}
