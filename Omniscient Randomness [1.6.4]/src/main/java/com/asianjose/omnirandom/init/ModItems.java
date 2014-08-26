package com.asianjose.omnirandom.init;

import com.asianjose.omnirandom.Names;
import com.asianjose.omnirandom.OmniscientRandomness;
import com.asianjose.omnirandom.items.ItemCoreOfSteve;
import com.asianjose.omnirandom.items.ItemOmni;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

	/** Class where all MY items are initialized **/
	static int itemIdStart = OmniscientRandomness.itemIdStart;
	public static final ItemOmni coreOfSteve = new ItemCoreOfSteve(itemIdStart);
	
	/** Begin initialization **/
	public static void init()
	{
		
		GameRegistry.registerItem(coreOfSteve, Names.Items.CORE_OF_STEVE);
	}
}
