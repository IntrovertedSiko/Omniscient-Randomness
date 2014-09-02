package com.asianjose.omnirandom.items;

import com.asianjose.omnirandom.OmniscientRandomness;
import com.asianjose.omnirandom.Reference;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemIndustrialPotato extends ItemFood{

	@SideOnly(Side.CLIENT)
	private Icon[] icon;
	
	public ItemIndustrialPotato(int id, int healAmount, float saturation, boolean wolfFav) {
		super(id, healAmount, saturation, wolfFav);
		this.setMaxDamage(512);
		this.setNoRepair();
		this.setMaxStackSize(1);
		this.setCreativeTab(OmniscientRandomness.OMNI_TAB);
		this.setUnlocalizedName("indPotato");
	}
	
	@Override
	public String getUnlocalizedName() {
		return String.format("item.%s%s", Reference.MOD_ID.toLowerCase() + ":", ItemOmni.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return String.format("item.%s%s", Reference.MOD_ID.toLowerCase() + ":", ItemOmni.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack itemstack) {
		return 8;
	}
	
	@Override
	public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer player) {
		player.getFoodStats().addStats(this);
		return new ItemStack(itemstack.getItem(), 1, itemstack.getItemDamage() + 1);
	}
	
	@Override
	public void registerIcons(IconRegister icon) {
		itemIcon = icon.registerIcon(ItemOmni.getUnwrappedUnlocalizedName(this.getUnlocalizedName()));
	}

}
