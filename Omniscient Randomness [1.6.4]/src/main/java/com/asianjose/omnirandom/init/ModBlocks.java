package com.asianjose.omnirandom.init;

import net.minecraft.block.material.Material;

import com.asianjose.omnirandom.Names;
import com.asianjose.omnirandom.OmniscientRandomness;
import com.asianjose.omnirandom.blocks.BlockFridge;
import com.asianjose.omnirandom.blocks.BlockIndComposter;
import com.asianjose.omnirandom.blocks.BlockOmniContainer;
import com.asianjose.omnirandom.blocks.BlockTPShop;
import com.asianjose.omnirandom.blocks.BlockXPD;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityFridge;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityIndComposter;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityTPShop;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityXPD;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
	
	/** Initialize all them blocks [Note: the initial 3 blocks I made are all containers,
	 *  so I figured I'll just make it BlockOmniContainer & if needed, BlockOmni
	 **/
	static int blockIdStart = OmniscientRandomness.blockIdStart;
	public static final BlockOmniContainer fridge = new BlockFridge(blockIdStart, Material.rock);
	public static final BlockOmniContainer xpDecomposer = new BlockXPD(blockIdStart + 1, Material.rock);
	public static final BlockOmniContainer indComposter = new BlockIndComposter(blockIdStart + 2, Material.rock);
	public static final BlockOmniContainer tpShop = new BlockTPShop(blockIdStart + 3, Material.rock);
	
	//Register all the blocks/etc
	public static void init() {
		GameRegistry.registerBlock(fridge, Names.Blocks.FRIDGE);
		GameRegistry.registerBlock(xpDecomposer, Names.Blocks.XPDECOMPOSER);
		GameRegistry.registerBlock(indComposter, Names.Blocks.INDCOMPOSTER);
		GameRegistry.registerBlock(tpShop, Names.Blocks.TPSHOP);
		
		GameRegistry.registerTileEntity(TileEntityFridge.class, Names.Blocks.TILE_ENTITY_FRIDGE);
		GameRegistry.registerTileEntity(TileEntityXPD.class, Names.Blocks.TILE_ENTITY_XPDECOMPOSER);
		GameRegistry.registerTileEntity(TileEntityIndComposter.class, Names.Blocks.TILE_ENTITY_INDCOMPOSTER);
		GameRegistry.registerTileEntity(TileEntityTPShop.class, Names.Blocks.TILE_ENTITY_TPSHOP);
	}

}
