package com.asianjose.omnirandom.items;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import com.asianjose.omnirandom.ExtendedPlayer;
import com.asianjose.omnirandom.OmniscientRandomness;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCoreOfSteve extends ItemOmni{
	
	private Random rand = new Random();
	
	//Used for icon registering
	private static final String[] MODES = new String[]{"Neutral", "Adventuring", "Mining", "Farming"};
	private static final List<String> MODES_LIST = Arrays.asList(MODES);
	
	@SideOnly(Side.CLIENT)
	private Icon[] icons;
	
	public ItemCoreOfSteve(int id) {
		super(id);
		this.setMaxStackSize(1);
		this.setUnlocalizedName("coreOfSteve");
	}
	
	
	/** Creates the nbt data on crafted (modes of the core). Default: Off **/
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player)
	{
		itemStack.stackTagCompound = new NBTTagCompound();
		
		itemStack.stackTagCompound.setString("Mode", "Neutral");
	}
	
	/** Adds description to the item (IE grey words under some items) **/
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4)
	{
		if(itemStack.stackTagCompound != null) //Makes sure that the item is crafted before adding description
		{
			String mode = itemStack.stackTagCompound.getString("Mode");
			
			if(mode.equals("Neutral")) //Different color for each mode
			{
				list.add(EnumChatFormatting.BLUE + "Mode: " + mode);
			} else if(mode.equals("Adventuring")) 
			{
				list.add(EnumChatFormatting.GOLD + "Mode: " + mode);
			} else if(mode.equals("Mining"))
			{
				list.add(EnumChatFormatting.GRAY + "Mode: " + mode);
			} else if(mode.equals("Farming"))
			{
				list.add(EnumChatFormatting.GREEN + "Mode: " + mode);
			}
		}
		ExtendedPlayer props = ExtendedPlayer.get(player);
		list.add(EnumChatFormatting.RED + "Time Points: " + props.getCurrentTP());
	}
	
	/** Cycles thru modes when the item is right clicked **/ // TODO: only when shift-clicked?
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
	{
		if(itemStack.stackTagCompound != null) //Saftey precautions?
		{
			String mode = itemStack.stackTagCompound.getString("Mode");
			
			//Cycles thru *Neutral -> Adventuring -> Mining -> Farming*
			if(mode.equals("Neutral")) 
			{
				itemStack.stackTagCompound.setString("Mode", "Adventuring");
			} else if(mode.equals("Adventuring")) 
			{
				itemStack.stackTagCompound.setString("Mode", "Mining");
			} else if(mode.equals("Mining"))
			{
				itemStack.stackTagCompound.setString("Mode", "Farming");
			} else if(mode.equals("Farming"))
			{
				itemStack.stackTagCompound.setString("Mode", "Neutral");
			}
			world.playSoundAtEntity(player, "random.orb", 1F, 1F);
		}
		
		return itemStack;
	}
	
	/** Updates the item every tick it's in the inventory **/
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5)
	{
		if(entity instanceof EntityPlayer && !world.isRemote)
		{
			if(itemStack.stackTagCompound == null) //If it doesn't have nbt (IE out of creative tab)
			{
				itemStack.stackTagCompound = new NBTTagCompound(); //New nbt-compound
				
				itemStack.stackTagCompound.setString("Mode", "Neutral"); //Mode DEFAULT: neutral
			}
			
			String mode = itemStack.stackTagCompound.getString("Mode");
			EntityPlayer player = (EntityPlayer) entity;
			
			if(mode.equals("Neutral")) //Does stuff depending on the mode
			{
				player.addPotionEffect(new PotionEffect(Potion.resistance.id, 6, 3));
				player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 6, 3));
			} else if(mode.equals("Adventuring")) 
			{
				player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 6, 3));
				player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 6, 3));
				player.addPotionEffect(new PotionEffect(Potion.jump.id, 6, 3));
				player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 6, 3));
			} else if(mode.equals("Mining"))
			{
				player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 6, 3));
				player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 6, 3));
				if(player.getHeldItem() != null)
				{
					if(player.getHeldItem().getItem() instanceof ItemPickaxe)
					{
						if(rand.nextInt(50) == 1) player.getHeldItem().setItemDamage(player.getHeldItem().getItemDamage() - 1);
					}
				}
			} else if(mode.equals("Farming")) //TODO: Instead of faster growing? How about auto-harvest?
			{
				player.addPotionEffect(new PotionEffect(23, 6, 1));
				if(rand.nextInt(10) == 1)
				{
					for(int i=-2; i<1; i++)
					{
						for(int j=-2; j<1; j++)
						{
							int id = world.getBlockId((int)player.posX + i, (int)player.posY, (int)player.posZ + j);
							if(Block.blocksList[id] instanceof IPlantable)
							{
								if(Block.blocksList[id] == Block.reed || Block.blocksList[id] == Block.cactus)
								{
									world.scheduleBlockUpdate((int)player.posX + i, (int)player.posY + 1, (int)player.posZ + j, id, 1);
								}
								world.scheduleBlockUpdate((int)player.posX + i, (int)player.posY, (int)player.posZ + j, id, 1);
							}
						}
					}
					
				}
			}
		}
	}
	
	/** Adds different modes to creative tab
	 * Why won't they add???? 4 "Farming" cores show up in the tab
	 **/
	public void getSubItems(int par1, CreativeTabs tab, List subItems){
	        ItemStack core = new ItemStack(this);

	        core.stackTagCompound = new NBTTagCompound();
	        core.stackTagCompound.setString("Mode", "Neutral");
	        subItems.add(core);
	        core.stackTagCompound.setString("Mode", "Adventuring");
	        subItems.add(core);
	        core.stackTagCompound.setString("Mode", "Mining");
	        subItems.add(core);
	        core.stackTagCompound.setString("Mode", "Farming");
	        subItems.add(core);
	}
	
	/** Misc icon registering stuff. YOU NEED BOTH "getIcon"'s to correctly render in inv & in hand!! **/
	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIconIndex(ItemStack itemStack) 
	{
		
		if(itemStack.hasTagCompound())
		{
			String mode = itemStack.stackTagCompound.getString("Mode");
			
			/*if(mode.equals("Neutral")) 
			{
				return this.icons[0];
			} else if(mode.equals("Adventuring")) 
			{
				return this.icons[1];
			} else if(mode.equals("Mining"))
			{
				return this.icons[2];
			} else if(mode.equals("Farming"))
			{
				return this.icons[3];
			}*/
			return this.icons[this.MODES_LIST.indexOf(mode)];
		}
		return this.icons[0];
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIcon(ItemStack itemStack, int pass)
	{
		/*if(itemStack.hasTagCompound())
		{
			String mode = itemStack.stackTagCompound.getString("Mode");
			
			return this.icons[this.MODES_LIST.indexOf(mode)];
		}
		return this.icons[0];*/
		return getIconIndex(itemStack);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister register)
	{
		this.icons = new Icon[MODES.length];
		
		for(int i=0; i<this.icons.length; i++)
		{
			this.icons[i] = register.registerIcon(super.getUnlocalizedName().substring(super.getUnlocalizedName().indexOf(".") + 1) + this.MODES[i]);
			System.out.println(super.getUnlocalizedName().substring(super.getUnlocalizedName().indexOf(".") + 1) + this.MODES[i]);
		}
	}
}
	

