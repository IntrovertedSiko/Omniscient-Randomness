package com.asianjose.omnirandom.handlers;

import java.io.File;

import net.minecraftforge.common.Configuration;
import com.asianjose.omnirandom.OmniscientRandomness;

public class ConfigurationHandler {

	public static Configuration configuration;
	public static boolean testValue = false;
	
	public static void init(File configFile){
		
		// Create the config object from the given config file
		if(configuration == null)
		{
			configuration = new Configuration(configFile);
			loadConfiguration(); // Move onto loading the config file
		}
	}
	
	private static void loadConfiguration()
	{
		/*OmniscientRandomness.fridgeId = configuration.getBlock("fridge Id", 2000).getInt();
		OmniscientRandomness.xpDecomposerId = configuration.getBlock("XpDecomposer Id", 2001).getInt();
		OmniscientRandomness.indComposterId = configuration.getBlock("industriousComposter Id", 2002).getInt();
		*/
		OmniscientRandomness.blockIdStart = configuration.getBlock("Block ID Start", 2000, "default 2000").getInt();
		OmniscientRandomness.itemIdStart = configuration.getItem("Item ID Start", 1000, "default 1000").getInt();
		
		if(configuration.hasChanged())
		{
			configuration.save();
		}
	}
}
