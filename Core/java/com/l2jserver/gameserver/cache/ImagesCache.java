package com.l2jserver.gameserver.cache;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.gameserver.idfactory.IdFactory;

import gov.nasa.worldwind.formats.dds.DDSConverter;

public class ImagesCache
{
	private static Logger _log = Logger.getLogger(ImagesCache.class.getName());
	
	private static final ImagesCache _instance = new ImagesCache();
	private final static Map<String, Integer> _imagesId = new HashMap<>();
	private final static Map<Integer, byte[]> _images = new HashMap<>();
	
	public static final ImagesCache getInstance()
	{
		return _instance;
	}
	
	private ImagesCache()
	{
		load();
	}
	
	public void reload()
	{
		try
		{
			if ((_imagesId != null) && (_images != null))
			{
				_imagesId.clear();
				_images.clear();
				load();
			}
		}
		catch (Exception e)
		{
			_log.info("Images Chache: Error while Reloading image cache." + e);
		}
		
	}
	
	public static void load()
	{
		try
		{
			_log.info("Images Chache: Loading images...");
			
			File folder = new File(Config.DATAPACK_ROOT, "data/images");
			
			if (!folder.exists() || !folder.isDirectory())
			{
				_log.info("Images Chache: Files missing, loading aborted.");
				return;
			}
			
			int count = 0;
			for (File file : folder.listFiles())
			{
				if (!file.isDirectory())
				{
					if (checkImageFormat(file))
					{
						count++;
						
						String fileName = file.getName();
						try
						{
							ByteBuffer bf = DDSConverter.convertToDDS(file);
							byte[] image = bf.array();
							int imageId = IdFactory.getInstance().getNextId();
							
							_imagesId.put(fileName.toLowerCase(), Integer.valueOf(imageId));
							_images.put(imageId, image);
						}
						catch (IOException ioe)
						{
							_log.info("Images Chache: Error while loading " + fileName + " image.");
						}
					}
				}
			}
			
			_log.info("Images Chache: Loaded " + count + " images");
		}
		catch (Exception e)
		{
		}
	}
	
	private static boolean checkImageFormat(File file)
	{
		String filename = file.getName();
		int dotPos = filename.lastIndexOf(".");
		String format = filename.substring(dotPos);
		if (format.equalsIgnoreCase(".jpg") || format.equalsIgnoreCase(".png") || format.equalsIgnoreCase(".bmp"))
		{
			return true;
		}
		return false;
	}
}
