package com.asianjose.omnirandom.blocks;

import java.util.Random;

import com.asianjose.omnirandom.OmniscientRandomness;
import com.asianjose.omnirandom.init.ModBlocks;
import com.asianjose.omnirandom.reference.ModNames;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockOmniContainer extends BlockContainer {

	//Sets generic values (IE the hardness/material)
	public BlockOmniContainer(int id, Material par2Material) {
		super(id, Material.rock);
		this.setHardness(1F);
		this.setResistance(1F);
		this.setStepSound(Block.soundStoneFootstep);
		this.setCreativeTab(OmniscientRandomness.OMNI_TAB);
	}
	
	public int idDropped(int par1, Random rand, int par3){
		return this.blockID;
	}
	
	public int quantityDropped(Random rand){
		return 1;
	}
	
	public int idPicked(World world, int x, int y, int z){
		return this.blockID;
	}
	
	
	/** Common methods for CONTAINER BLOCKS (IE face the player) **/
	public void onBlockAdded(World world, int x, int y, int z){
		super.onBlockAdded(world, x, y, z);
		this.setDefaultDirection(world, x, y, z);
	}

	private void setDefaultDirection(World world, int x, int y, int z) {
		if(!world.isRemote){
			int l = world.getBlockId(x, y, z - 1);
			int il = world.getBlockId(x, y, z + 1);
			int jl = world.getBlockId(x - 1, y, z);
			int kl = world.getBlockId(x + 1, y, z);
			byte b0 = 3;
			
			if(Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[il]) b0 = 3;
			if(Block.opaqueCubeLookup[il] && !Block.opaqueCubeLookup[l]) b0 = 2;
			if(Block.opaqueCubeLookup[jl] && !Block.opaqueCubeLookup[kl]) b0 = 5;
			if(Block.opaqueCubeLookup[kl] && !Block.opaqueCubeLookup[jl]) b0 = 4;
			
			world.setBlockMetadataWithNotify(x, y, z, b0, 2);
		}
	}
	
	/** Common methods for internal names/icons/stuff  **/
	
	@Override
	public String getUnlocalizedName()
	{
		return String.format("tile.%s%s", ModNames.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}
	
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return String.format("tile.%s%s", ModNames.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}
	
	protected String getUnwrappedUnlocalizedName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) // 1.7: IconRegister -> IIconRegister
	{
		blockIcon = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1));
	// Returns omnirandom:"blockName"
		// The file should just be "blockName"
	}
	@Override
	public TileEntity createNewTileEntity(World world) {
		return null; //Null because it's overriden in the sub-classes
	}
}
