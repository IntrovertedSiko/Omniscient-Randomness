package com.asianjose.omnirandom.blocks;

import java.util.Random;

import com.asianjose.omnirandom.OmniscientRandomness;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityIndComposter;
import com.asianjose.omnirandom.init.ModBlocks;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
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

public class BlockIndComposter extends BlockOmniContainer{

	@SideOnly(Side.CLIENT)
	private Icon iconFront;
	
	Random rand = new Random();
	
	public BlockIndComposter(int id, Material material) {
		super(id, material);
	}


	public int idDropped(int par1, Random rand, int par3){
		return ModBlocks.indComposter.blockID;
	}
	
	public int quantityDropped(Random rand){
		return 1;
	}
	
	public int idPicked(World world, int x, int y, int z){
		return ModBlocks.indComposter.blockID;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister reg){
		this.iconFront = reg.registerIcon("omnirandom:indcFront");
		this.blockIcon = reg.registerIcon("omnirandom:indc");
	}
	
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata){
		return side == metadata ?  this.iconFront : blockIcon; // It only uses front texture when placed certain way. Look up Scratch's tut to see metadata thingy
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ){
		if(!world.isRemote){
			FMLNetworkHandler.openGui(player, OmniscientRandomness.instance, OmniscientRandomness.guiIdIndComposter, world, x, y, z);
		}
		return true;
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack){
		int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360F) + 0.5D) & 3;
		
		if(l==0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		if(l==1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		if(l==2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		if(l==3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		
		if(itemstack.hasDisplayName()) ((TileEntityIndComposter)world.getBlockTileEntity(x, y, z)).setGuiDisplayName(itemstack.getDisplayName());
	}
	
	public void breakBlock(World world, int x, int y, int z, int oldBlockID, int oldMetadata){
		TileEntityIndComposter tileentity = (TileEntityIndComposter) world.getBlockTileEntity(x, y, z);
		
		if(tileentity != null){
			for(int i = 0; i < tileentity.getSizeInventory(); i++){
				ItemStack itemstack = tileentity.getStackInSlot(i);
				
				if(itemstack != null){
					float f = this.rand.nextFloat() * 0.5F + 0.1F;
					float f1 = this.rand.nextFloat() * 0.5F + 0.1F;
					float f2 = this.rand.nextFloat() * 0.5F + 0.1F;
					
					while(itemstack.stackSize > 0){
						int j = this.rand.nextInt(21) + 10;
						
						if(j > itemstack.stackSize){
							j = itemstack.stackSize;
						}
						
						itemstack.stackSize -= j;
						
						EntityItem item = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.itemID, j, itemstack.getItemDamage()));
						
						if(itemstack.hasTagCompound()){
							item.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
						}
						
						float f3 = 0.05F;
						
						item.motionX = (double)((float)this.rand.nextGaussian() * f3);
						item.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
						item.motionZ = (double)((float)this.rand.nextGaussian() * f3);
						
						world.spawnEntityInWorld(item);
					}
				}
			}
			world.func_96440_m(x, y, z, oldBlockID);
		}
		super.breakBlock(world, x, y, z, oldBlockID, oldMetadata);
	}
	
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityIndComposter(); 
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
