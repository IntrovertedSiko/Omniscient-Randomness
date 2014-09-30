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

public class ItemScroll extends ItemOmni{
	
	private Random rand = new Random();
	
	//Scroll Types. Add magic scroll? maybe it can interact with equailards? I'd need to add an attribute huh?
	public static final String[] SCROLLS = {"Defense", "Speed", "Attack"};
	
	public ItemScroll(int id) {
		super(id);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setUnlocalizedName("scroll");
	}
}
	

