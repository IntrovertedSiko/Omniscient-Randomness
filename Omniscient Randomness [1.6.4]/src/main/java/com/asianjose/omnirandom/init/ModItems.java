package com.asianjose.omnirandom.init;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;

import com.asianjose.omnirandom.Names;
import com.asianjose.omnirandom.OmniscientRandomness;
import com.asianjose.omnirandom.items.ItemCoreOfSteve;
import com.asianjose.omnirandom.items.ItemIndustrialPotato;
import com.asianjose.omnirandom.items.ItemOmni;
import com.asianjose.omnirandom.items.tools.ItemPickaxeSeeker;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

	/** Class where all MY items are initialized **/
	static int itemIdStart = OmniscientRandomness.itemIdStart;
	public static final ItemOmni coreOfSteve = new ItemCoreOfSteve(itemIdStart);
	public static final ItemFood industrialPotato = new ItemIndustrialPotato(itemIdStart + 1, 1, 1F, false);
	// Note about industrialPotato: It doesn't extend ItemOmni & parameters = id, healAmount, saturation, wolfFav (able to feed wolves)
	public static final ItemPickaxe seekerPickaxe = new ItemPickaxeSeeker(itemIdStart + 2, EnumToolMaterial.IRON);
	/** Begin initialization **/
	public static void init()
	{
		GameRegistry.registerItem(coreOfSteve, Names.Items.CORE_OF_STEVE);
		GameRegistry.registerItem(industrialPotato, Names.Items.INDUSTRIAL_POTATO);
		GameRegistry.registerItem(seekerPickaxe, Names.Items.SEEKER_PICKAXE);
	}
}
