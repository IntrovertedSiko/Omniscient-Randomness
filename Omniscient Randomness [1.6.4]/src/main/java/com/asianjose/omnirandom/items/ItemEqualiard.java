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
import net.minecraft.potion.PotionAbsoption;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import com.asianjose.omnirandom.ExtendedPlayer;
import com.asianjose.omnirandom.OmniscientRandomness;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemEqualiard extends ItemOmni{
	
	private Random rand = new Random();
	public byte element;
	//Only update the item every (few?) seconds
	public int tickCounter = 0;
	
	//Elements: Umbral (dark), Spectral (light), Terrastal (earth), Aeral (air), Aqueal (water), Ignisal (fire)
	public static final String[] ELEMENTS = {"umbral", "spectral", "terrastal", "aeral", "aqueal", "ignisal"};
	
	public ItemEqualiard(int id, String element) {
		super(id);
		this.setMaxStackSize(1);
		this.setMaxDamage(5);
		this.setNoRepair();
		this.setHasSubtypes(true);
		this.setUnlocalizedName(element + "Equaliard");
		
		//The "element ID" is in respect to the array. IE: umbral = 1, spectral = 2, etc
		this.element = (byte) ((Arrays.asList(ELEMENTS).indexOf(element)) + 1);
	}
	
	/** Creates the nbt data on crafted (modes of the core). Default: Off **/
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player)
	{
		itemStack.stackTagCompound = new NBTTagCompound();
		
		itemStack.stackTagCompound.setInteger("ElePoints", 0);
		//TODO: set damage to 0
	}
	
	/** Adds description to the item (IE grey words under some items) **/
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4)
	{
		if(itemStack.stackTagCompound != null) //Makes sure that the item is crafted before adding description
		{
			int ElePoints = itemStack.stackTagCompound.getInteger("ElePoints");
			
			switch(this.element){
				case 1:
					list.add(EnumChatFormatting.GRAY + "Umbral: " + ElePoints);
					break;
				case 2:
					list.add(EnumChatFormatting.WHITE + "Spectral: " + ElePoints);
					break;
				case 3:
					list.add(EnumChatFormatting.GREEN + "Terrastal: " + ElePoints);
					break;
				case 4:
					list.add(EnumChatFormatting.YELLOW + "Aeral: " + ElePoints);
					break;
				case 5:
					list.add(EnumChatFormatting.AQUA + "Aqueal: " + ElePoints);
					break;
				case 6:
					list.add(EnumChatFormatting.RED + "Ignisal: " + ElePoints);
					break;
				default:
					list.add("Something's wrong! No element?");
			}
		}
	}
	
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
	{
		//TODO: If sneaking, use special ability. Cooldown for "skills"? Or are these like, lvl 1 skills
		switch(this.element){
				case 1:	//Umbral
					player.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 60, 2));
					player.addPotionEffect(new PotionEffect(22, 60, 2)); //Absorption
					break;
				case 2:	//Spectral
					player.addPotionEffect(new PotionEffect(Potion.invisibility.getId(), 60, 2));
					player.addPotionEffect(new PotionEffect(Potion.regeneration.getId(), 60, 2));
					break;
				case 3:	//Terrastal
					player.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 60, 2));
					player.addPotionEffect(new PotionEffect(23, 60, 2)); //Saturation
					break;
				case 4:	//Aeral
					player.addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(), 60, 2));
					player.addPotionEffect(new PotionEffect(Potion.jump.getId(), 60, 2));
					break;
				case 5:	//Aqueal
					player.addPotionEffect(new PotionEffect(Potion.waterBreathing.getId(), 60, 2));
					player.addPotionEffect(new PotionEffect(Potion.resistance.getId(), 60, 2));
					break;
				case 6:	//Ignisal
					player.addPotionEffect(new PotionEffect(Potion.fireResistance.getId(), 60, 2));
					player.addPotionEffect(new PotionEffect(Potion.damageBoost.getId(), 60, 2));
					break;
				default:
					System.out.println("[Equaliards] Could not apply effects onRightClick?");
					break;
		}
		itemStack.stackTagCompound.setInteger("ElePoints", 10);
		return itemStack;
	}
	
	/** Updates the item every tick it's in the inventory **/
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5)
	{
		if(tickCounter < 100){
			tickCounter++;
		} else if(itemStack.stackTagCompound != null){
			tickCounter = rand.nextInt(100);
			int ElePoints = itemStack.stackTagCompound.getInteger("ElePoints");
			
			ElePoints++;
			if(ElePoints == 10 || ElePoints == 25 || ElePoints == 75 || ElePoints == 150) {
				//"un-damage" the item to "level it up"
			}
		}
	}
}
	

