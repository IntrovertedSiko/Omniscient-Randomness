package com.asianjose.omnirandom.blocks;

import java.util.Random;

import com.asianjose.omnirandom.OmniscientRandomness;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityEnchanter;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityTPShop;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockEnchanter extends BlockOmniContainer{
	
	@SideOnly(Side.CLIENT)
	private Icon iconFront;
	
	private Random rand = new Random();
	
	public BlockEnchanter(int id, Material par2Material) {
		super(id, par2Material);
	}

	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata){
		return side == metadata ?  this.iconFront : blockIcon; // It only uses front texture when placed certain way. Look up Scratch's tut to see metadata thingy
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ){
		if(!world.isRemote){
			FMLNetworkHandler.openGui(player, OmniscientRandomness.instance, OmniscientRandomness.guiIdEnchanter, world, x, y, z);
		}
		return true;
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack){
		int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360F) + 0.5D) & 3;
		
		if(l==0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		if(l==1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		if(l==2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		if(l==3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		
		if(itemstack.hasDisplayName()) ((TileEntityEnchanter)world.getBlockTileEntity(x, y, z)).setGuiDisplayName(itemstack.getDisplayName());
	}
	
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityEnchanter(); 
	}
	
	public boolean hasComparatorInputOverride()
    {
            return true;
    }
   
    public int getComparatorInpurOverride(World world, int x, int y, int z, int i)
    {
    return Container.calcRedstoneFromInventory((IInventory)world.getBlockTileEntity(x, y, z));     
    }
	
}
