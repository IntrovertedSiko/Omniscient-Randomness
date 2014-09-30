package com.asianjose.omnirandom.items.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.asianjose.omnirandom.Names;
import com.asianjose.omnirandom.OmniscientRandomness;
import com.asianjose.omnirandom.items.ItemOmni;
import com.asianjose.omnirandom.reference.ModNames;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemPickaxeSeeker extends ItemPickaxe {

	private Random rand = new Random();
	
	@SideOnly(Side.CLIENT)
	private Icon[] icon;
	
	//TODO: Multiplier for "Seeking" in config?
	
	/** TODO: Create ItemOmniTools w/ basic methods (onCreated/onBlockDestroyed/etc) **/
	public ItemPickaxeSeeker(int par1, EnumToolMaterial enumToolMaterial) {
		super(par1, enumToolMaterial);
		this.setMaxDamage(100);
		this.setUnlocalizedName(Names.Items.SEEKER_PICKAXE);
		this.setCreativeTab(OmniscientRandomness.OMNI_TAB);
	}
	
	/** These following methods restrict the tool from being damaged **/
	@Override
	public boolean getIsRepairable(ItemStack itemstack, ItemStack itemstack2) {
		return false;
	}
	
	//Don't damage the pickaxe if it's mining stone/cobbleStone
	//TODO: What effect should the pickaxe really have? Right Now: Passive fortune (stacking)
	@Override
	public boolean onBlockDestroyed(ItemStack itemstack, World world, int blockId, int x, int y, int z, EntityLivingBase entity) {
		if(blockId==Block.stone.blockID || blockId==Block.cobblestone.blockID) {
			return true;
		}
		/** Deprecated in favor for BreakBlockEvent (class in /events)... Planned features can
		 *  be used alongside that class, & it seems more efficient? (huh, what do I know?)
		 **/
	/*	if(itemstack.stackTagCompound.getBoolean("Seeking") == true){
			if(Block.blocksList[blockId] instanceof BlockOre && !world.isRemote) {
				if(blockId == Block.oreLapis.blockID) {
					EntityItem item = new EntityItem(world, (double) x, (double) y, (double) z, new ItemStack(Block.blocksList[blockId].getBlockDropped(world, x, y, z, 4, 3).get(0).getItem(), rand.nextInt(9), 4));
					world.spawnEntityInWorld(item);
				} else if(blockId != Block.oreIron.blockID && blockId != Block.oreGold.blockID) {
					EntityItem item = new EntityItem(world, (double) x, (double) y, (double) z, new ItemStack(Block.blocksList[blockId].getBlockDropped(world, x, y, z, 0, 3).get(0).getItem(), rand.nextInt(3)));
					world.spawnEntityInWorld(item);
				}
			}
		}*/
		if((double)Block.blocksList[blockId].getBlockHardness(world, x, y, z) != 0.0D) itemstack.damageItem(1, entity);
		return true;
	}

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase entity, EntityLivingBase entity2) {
		return true;
	}
	
	@Override
	public boolean isDamageable() {
		return false;
	}
	
	/** End: methods that restrict the tool from being damaged **/
	
	//Harvest (cobble)stone 5x faster [20x faster if seeking]
	//Everything else either 2x slower or 2x faster [THAN IRON]
	//TODO: How to "instantly" break block? (IE Angel block)
	@Override
	public float getStrVsBlock(ItemStack itemstack, Block block, int meta) {
		if(block==Block.stone || block==Block.cobblestone) { 
			if(itemstack.stackTagCompound.getBoolean("Seeking") == true) return 80F;
			return 20F;
		}
		return itemstack.stackTagCompound.getBoolean("Seeking") ? (super.getStrVsBlock(itemstack, block))/2F : (super.getStrVsBlock(itemstack, block)) * 2F;
	}
	
	//Defaults "Seeking Mode" to off when crafted
	@Override
	public void onCreated(ItemStack itemstack, World world, EntityPlayer player) {
		itemstack.stackTagCompound = new NBTTagCompound();
		
		itemstack.stackTagCompound.setBoolean("Seeking", false);
	}
	
	//Displays whether or not "Seeking" is true/false
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4) {
		if(itemstack.stackTagCompound != null) {
			list.add(EnumChatFormatting.AQUA + "Seeking: " + itemstack.stackTagCompound.getBoolean("Seeking"));
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
		// "Inverts" the mode. False -> True && True -> False
		if(player.isSneaking()) {
			if(itemstack.stackTagCompound != null) {
				itemstack.stackTagCompound.setBoolean("Seeking", !(itemstack.stackTagCompound.getBoolean("Seeking")));
			} else {
				itemstack.stackTagCompound = new NBTTagCompound();
			}
		}
		return itemstack;
	}
	
	/** Name/textures **/
	
	@Override
	public String getUnlocalizedName() {
		return String.format("item.%s%s", ModNames.MOD_ID.toLowerCase() + ":", ItemOmni.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return String.format("item.%s%s", ModNames.MOD_ID.toLowerCase() + ":", ItemOmni.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}
	
	/** icon[0] = regular | icon[1] = seeking-mode **/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister icon) {
		this.icon = new Icon[2];
		this.icon[0] = icon.registerIcon(ItemOmni.getUnwrappedUnlocalizedName(this.getUnlocalizedName()));
		this.icon[1] = icon.registerIcon(ItemOmni.getUnwrappedUnlocalizedName(this.getUnlocalizedName() + "_seeking"));
	}
	
	/** Misc icon registering stuff. YOU NEED BOTH "getIcon"'s to correctly render in inv & in hand!! **/
	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIconIndex(ItemStack itemStack) 
	{
		return itemStack.stackTagCompound.getBoolean("Seeking") ? this.icon[1] : this.icon[0];
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIcon(ItemStack itemStack, int pass)
	{
		return getIconIndex(itemStack);
	}
	
	//Only have the pickaxe w/ default "Seeking" in creative tabs
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs tab, List subItems) {
		ItemStack item = new ItemStack(this);
		
		item.stackTagCompound = new NBTTagCompound();
		item.stackTagCompound.setBoolean("Seeking", false);
		subItems.add(item);
	}
}
