package com.asianjose.omnirandom.blocks.tileentity;

import com.asianjose.omnirandom.ExtendedPlayer;
import com.asianjose.omnirandom.blocks.gui.GuiTPShop;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public class TileEntityTPShop extends TileEntityOmni implements ISidedInventory{

	public static final int INVENTORY_SIZE = 9;
	private String localizedName;
	private EntityPlayer player;
	private ExtendedPlayer props;
	
	public TileEntityTPShop() {
		inventory = new ItemStack[INVENTORY_SIZE];
	}
	
	//Gets the user of the tileEntity, so it knows which player's Time Points to track
	public void onBlockOpened(EntityPlayer player) {
		if(player != null){
			this.player = player;
			this.props = ExtendedPlayer.get(player);
		}
	}
	
	public String getInvName(){
		return this.isInvNameLocalized() ? this.localizedName : "container.tpShop";
	}

	public void setGuiDisplayName(String displayName){
		this.localizedName = displayName;
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if(inventory[slot] != null) {
			ItemStack itemstack;
			if(inventory[slot].stackSize <= amount) {
				itemstack = inventory[slot];
				System.out.println("[TileEntityTPShop] Player: " + player + " has taken something. Deduct points! He has: " + props.getCurrentTP());
				return itemstack;
			} else {
				itemstack = inventory[slot].splitStack(amount);
				return itemstack;
			}
		}
		return null;
	}
}
