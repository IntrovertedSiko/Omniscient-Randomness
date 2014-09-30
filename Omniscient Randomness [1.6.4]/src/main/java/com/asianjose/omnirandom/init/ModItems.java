package com.asianjose.omnirandom.init;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;

import com.asianjose.omnirandom.Names;
import com.asianjose.omnirandom.OmniscientRandomness;
import com.asianjose.omnirandom.items.ItemCoreOfSteve;
import com.asianjose.omnirandom.items.ItemEqualiard;
import com.asianjose.omnirandom.items.ItemIndustrialPotato;
import com.asianjose.omnirandom.items.ItemOmni;
import com.asianjose.omnirandom.items.ItemScroll;
import com.asianjose.omnirandom.items.ItemScrollActivator;
import com.asianjose.omnirandom.items.tools.ItemPickaxeSeeker;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

	/** Class where all MY items are initialized **/
	static int itemIdStart = OmniscientRandomness.itemIdStart;
	public static final ItemOmni coreOfSteve = new ItemCoreOfSteve(itemIdStart);
	public static final ItemFood industrialPotato = new ItemIndustrialPotato(itemIdStart + 1, 1, 1F, false);
	
	// Note: It doesn't extend ItemOmni & parameters = id, healAmount, saturation, wolfFav (able to feed wolves)
	public static final ItemPickaxe seekerPickaxe = new ItemPickaxeSeeker(itemIdStart + 2, EnumToolMaterial.IRON);
	
	//Equaliard constructor: (int id, String element)
	public static final ItemOmni umbralEqualiard = new ItemEqualiard(itemIdStart + 3, "umbral");
	public static final ItemOmni spectralEqualiard = new ItemEqualiard(itemIdStart + 3, "spectral");
	public static final ItemOmni terrastalEqualiard = new ItemEqualiard(itemIdStart + 3, "terrastal");
	public static final ItemOmni aeralEqualiard = new ItemEqualiard(itemIdStart + 3, "aeral");
	public static final ItemOmni aquealEqualiard = new ItemEqualiard(itemIdStart + 3, "aqueal");
	public static final ItemOmni ignisalEqualiard = new ItemEqualiard(itemIdStart + 3, "ignisal");
	
	public static final ItemOmni scroll = new ItemScroll(itemIdStart + 4);
	public static final ItemOmni scrollActivator = new ItemScrollActivator(itemIdStart + 5);
	
	/** Begin initialization **/
	public static void init()
	{
		GameRegistry.registerItem(coreOfSteve, Names.Items.CORE_OF_STEVE);
		GameRegistry.registerItem(industrialPotato, Names.Items.INDUSTRIAL_POTATO);
		GameRegistry.registerItem(seekerPickaxe, Names.Items.SEEKER_PICKAXE);
		
		GameRegistry.registerItem(umbralEqualiard, Names.Items.EQUALIARD_TYPES[0]);
		GameRegistry.registerItem(spectralEqualiard, Names.Items.EQUALIARD_TYPES[1]);
		GameRegistry.registerItem(terrastalEqualiard, Names.Items.EQUALIARD_TYPES[2]);
		GameRegistry.registerItem(aeralEqualiard, Names.Items.EQUALIARD_TYPES[3]);
		GameRegistry.registerItem(aquealEqualiard, Names.Items.EQUALIARD_TYPES[4]);
		GameRegistry.registerItem(ignisalEqualiard, Names.Items.EQUALIARD_TYPES[5]);
		
		GameRegistry.registerItem(scroll, Names.Items.SCROLL);
		GameRegistry.registerItem(scrollActivator, Names.Items.SCROLL_ACTIVATOR);
	}
}
