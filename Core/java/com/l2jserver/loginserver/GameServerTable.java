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
package com.l2jserver.loginserver;

import java.io.File;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAKeyGenParameterSpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.loginserver.gameserverpackets.ServerStatus;
import com.l2jserver.util.IPSubnet;
import com.l2jserver.util.Rnd;
import com.l2jserver.util.XMLDocumentFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author KenM
 */
public class GameServerTable
{
	private static Logger _log = Logger.getLogger(GameServerTable.class.getName());
	
	// Server Names Config
	private static Map<Integer, String> _serverNames = new ConcurrentHashMap<>();
	
	// Game Server Table
	private final Map<Integer, GameServerInfo> _gameServerTable = new ConcurrentHashMap<>();
	
	// RSA Config
	private static final int KEYS_SIZE = 10;
	private KeyPair[] _keyPairs;
	
	protected GameServerTable()
	{
		loadServerNames();
		_log.info("Loaded " + _serverNames.size() + " server names");
		
		loadRegisteredGameServers();
		_log.info("Loaded " + _gameServerTable.size() + " registered Game Servers");
		
		loadRSAKeys();
		_log.info("Cached " + _keyPairs.length + " RSA keys for Game Server communication.");
	}
	
	private void loadRSAKeys()
	{
		try
		{
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(new RSAKeyGenParameterSpec(512, RSAKeyGenParameterSpec.F4));
			
			_keyPairs = new KeyPair[KEYS_SIZE];
			for (int i = 0; i < KEYS_SIZE; i++)
			{
				_keyPairs[i] = keyGen.genKeyPair();
			}
		}
		catch (Exception e)
		{
			_log.severe("GameServerTable: Error loading RSA keys for Game Server communication!");
		}
	}
	
	private static void loadServerNames()
	{
		try
		{
			File f = new File("data/servername.xml");
			Document doc = XMLDocumentFactory.getInstance().loadDocument(f);
			
			Node n = doc.getFirstChild();
			for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
			{
				if (d.getNodeName().equalsIgnoreCase("server"))
				{
					NamedNodeMap attrs = d.getAttributes();
					
					int id = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
					String name = attrs.getNamedItem("name").getNodeValue();
					
					_serverNames.put(id, name);
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("GameServerTable: servername.xml could not be loaded.");
			_log.warning("" + e);
		}
	}
	
	private void loadRegisteredGameServers()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM gameservers"))
		{
			int id;
			try (ResultSet rset = statement.executeQuery())
			{
				while (rset.next())
				{
					id = rset.getInt("server_id");
					_gameServerTable.put(id, new GameServerInfo(id, stringToHex(rset.getString("hexid"))));
				}
			}
		}
		catch (Exception e)
		{
			_log.severe("GameServerTable: Error loading registered game servers!");
		}
	}
	
	public Map<Integer, GameServerInfo> getRegisteredGameServers()
	{
		return _gameServerTable;
	}
	
	public GameServerInfo getRegisteredGameServerById(int id)
	{
		return _gameServerTable.get(id);
	}
	
	public boolean hasRegisteredGameServerOnId(int id)
	{
		return _gameServerTable.containsKey(id);
	}
	
	public boolean registerWithFirstAvaliableId(GameServerInfo gsi)
	{
		// avoid two servers registering with the same "free" id
		synchronized (_gameServerTable)
		{
			for (Entry<Integer, String> entry : _serverNames.entrySet())
			{
				if (!_gameServerTable.containsKey(entry.getKey()))
				{
					_gameServerTable.put(entry.getKey(), gsi);
					gsi.setId(entry.getKey());
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean register(int id, GameServerInfo gsi)
	{
		// avoid two servers registering with the same id
		synchronized (_gameServerTable)
		{
			if (!_gameServerTable.containsKey(id))
			{
				_gameServerTable.put(id, gsi);
				gsi.setId(id);
				return true;
			}
		}
		return false;
	}
	
	public void registerServerOnDB(GameServerInfo gsi)
	{
		this.registerServerOnDB(gsi.getHexId(), gsi.getId(), gsi.getExternalHost());
	}
	
	public void registerServerOnDB(byte[] hexId, int id, String externalHost)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("INSERT INTO gameservers (hexid,server_id,host) values (?,?,?)"))
		{
			statement.setString(1, hexToString(hexId));
			statement.setInt(2, id);
			statement.setString(3, externalHost);
			statement.executeUpdate();
		}
		catch (SQLException e)
		{
			_log.warning("GameServerTable: SQL error while saving gameserver: " + e);
		}
	}
	
	public String getServerNameById(int id)
	{
		return getServerNames().get(id);
	}
	
	public Map<Integer, String> getServerNames()
	{
		return _serverNames;
	}
	
	public KeyPair getKeyPair()
	{
		return _keyPairs[Rnd.nextInt(10)];
	}
	
	private byte[] stringToHex(String string)
	{
		return new BigInteger(string, 16).toByteArray();
	}
	
	private String hexToString(byte[] hex)
	{
		if (hex == null)
		{
			return "null";
		}
		return new BigInteger(hex).toString(16);
	}
	
	public static class GameServerInfo
	{
		// auth
		private int _id;
		private final byte[] _hexId;
		private boolean _isAuthed;
		
		// status
		private GameServerThread _gst;
		private int _status;
		
		// network
		private final ArrayList<GameServerAddress> _addrs = new ArrayList<>(5);
		private int _port;
		
		// config
		private final boolean _isPvp = true;
		private int _serverType;
		private int _ageLimit;
		private boolean _isShowingBrackets;
		private int _maxPlayers;
		
		public GameServerInfo(int id, byte[] hexId, GameServerThread gst)
		{
			_id = id;
			_hexId = hexId;
			_gst = gst;
			_status = ServerStatus.STATUS_DOWN;
		}
		
		public GameServerInfo(int id, byte[] hexId)
		{
			this(id, hexId, null);
		}
		
		public void setId(int id)
		{
			_id = id;
		}
		
		public int getId()
		{
			return _id;
		}
		
		public byte[] getHexId()
		{
			return _hexId;
		}
		
		public void setAuthed(boolean isAuthed)
		{
			_isAuthed = isAuthed;
		}
		
		public boolean isAuthed()
		{
			return _isAuthed;
		}
		
		public void setGameServerThread(GameServerThread gst)
		{
			_gst = gst;
		}
		
		public GameServerThread getGameServerThread()
		{
			return _gst;
		}
		
		public void setStatus(int status)
		{
			_status = status;
		}
		
		public int getStatus()
		{
			return _status;
		}
		
		public int getCurrentPlayerCount()
		{
			if (_gst == null)
			{
				return 0;
			}
			return _gst.getPlayerCount();
		}
		
		public String getExternalHost()
		{
			try
			{
				return getServerAddress(InetAddress.getByName("0.0.0.0"));
			}
			catch (Exception e)
			{
				
			}
			return null;
		}
		
		public int getPort()
		{
			return _port;
		}
		
		public void setPort(int port)
		{
			_port = port;
		}
		
		public void setMaxPlayers(int maxPlayers)
		{
			_maxPlayers = maxPlayers;
		}
		
		public int getMaxPlayers()
		{
			return _maxPlayers;
		}
		
		public boolean isPvp()
		{
			return _isPvp;
		}
		
		public void setAgeLimit(int val)
		{
			_ageLimit = val;
		}
		
		public int getAgeLimit()
		{
			return _ageLimit;
		}
		
		public void setServerType(int val)
		{
			_serverType = val;
		}
		
		public int getServerType()
		{
			return _serverType;
		}
		
		public void setShowingBrackets(boolean val)
		{
			_isShowingBrackets = val;
		}
		
		public boolean isShowingBrackets()
		{
			return _isShowingBrackets;
		}
		
		public void setDown()
		{
			setAuthed(false);
			setPort(0);
			setGameServerThread(null);
			setStatus(ServerStatus.STATUS_DOWN);
		}
		
		public void addServerAddress(String subnet, String addr) throws UnknownHostException
		{
			_addrs.add(new GameServerAddress(subnet, addr));
		}
		
		public String getServerAddress(InetAddress addr)
		{
			for (GameServerAddress a : _addrs)
			{
				if (a.equals(addr))
				{
					return a.getServerAddress();
				}
			}
			return null; // should not happens
		}
		
		public String[] getServerAddresses()
		{
			String[] result = new String[_addrs.size()];
			for (int i = 0; i < result.length; i++)
			{
				result[i] = _addrs.get(i).toString();
			}
			
			return result;
		}
		
		public void clearServerAddresses()
		{
			_addrs.clear();
		}
		
		private class GameServerAddress extends IPSubnet
		{
			private final String _serverAddress;
			
			public GameServerAddress(String subnet, String address) throws UnknownHostException
			{
				super(subnet);
				_serverAddress = address;
			}
			
			public String getServerAddress()
			{
				return _serverAddress;
			}
			
			@Override
			public String toString()
			{
				return _serverAddress + super.toString();
			}
		}
	}
	
	/**
	 * Gets the single instance of GameServerTable.
	 * @return single instance of GameServerTable
	 */
	public static GameServerTable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	/**
	 * The Class SingletonHolder.
	 */
	private static class SingletonHolder
	{
		protected static final GameServerTable _instance = new GameServerTable();
	}
}
