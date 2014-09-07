package com.asianjose.omnirandom.blocks.tileentity;

import com.asianjose.omnirandom.ExtendedPlayer;
import com.asianjose.omnirandom.blocks.gui.GuiTPShop;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileEntityTPShop extends TileEntityOmni implements ISidedInventory{

	public static final int INVENTORY_SIZE = 9;
	private String localizedName;
	private EntityPlayer player;
	private ExtendedPlayer props;
	private int shopPage;
	private int shopPageMax;
	
	//Page 1 of the store
	private final Item[] STORE_1_MATERIALS = {Item.redstone, Item.glowstone, Item.ingotIron, 
											Item.ingotGold, Item.diamond, Item.bakedPotato, 
											Item.beefCooked, Item.cake, Item.appleGold};/*, Item.bakedPotato,
											Item.reed, Item.diamond, Item.redstone};*/
	private final Item[] STORE_2_FOOD = {Item.appleRed, Item.egg, Item.bakedPotato, 
										Item.beefCooked, Item.cake, Item.appleGold};
	
	public TileEntityTPShop() {
		inventory = new ItemStack[INVENTORY_SIZE];
		shopPage = 1;
		shopPageMax = 2; //Change according to how many STORES there are
	}
	
	//Gets the user of the tileEntity, so it knows which player's Time Points to track
	public void onBlockOpened(EntityPlayer player) {
		if(player != null){
			updateShop();
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
			ItemStack itemstack = inventory[slot];
			System.out.println("[TileEntityTPShop] Player: " + player); //" has taken something. Deduct points! He has: " + props.getCurrentTP());
			if(player != null) {
				if(props.consumeTP(getTPCost(itemstack.getItem()))) return itemstack;
			}
			/*if(inventory[slot].stackSize <= amount) {
				itemstack = inventory[slot];
				System.out.println("[TileEntityTPShop] Player: " + player + " has taken something. Deduct points! He has: " + props.getCurrentTP());
				return itemstack;
			} else {
				itemstack = inventory[slot].splitStack(amount);
				return itemstack;
			}*/
		}
		return null;
	}
	
	/**
	 * @return The page the shop is currently on
	 */
	public int getShopPage() {
		return this.shopPage;
	}
	
	/**
	 * @return The maximum page it can go to. Used for ease of access (so I only have to add pages, not change anything else)
	 */
	public int getShopPageMax() {
		return this.shopPageMax;
	}
	
	/**
	 * @param changeAmount = how much to change the shop page by (1 or -1)
	 */
	public void updateShopPage(int changeAmount) {
		this.shopPage += changeAmount;
		updateShop();
	}
	
	//Sets the shop's contents (slots) based on what page it's on
	private void updateShop() {
		switch(getShopPage()) {
		case 1:
			for(int i=0; i < STORE_1_MATERIALS.length; i++) {
				setInventorySlotContents(i, new ItemStack(STORE_1_MATERIALS[i], 1));
			}
			break;
			
		case 2:
			for(int i=0; i < STORE_2_FOOD.length; i++) {
				setInventorySlotContents(i, new ItemStack(STORE_2_FOOD[i], 1));
			}
			break;
		//Default: nothing in any of the slots
		default:
			for(int i=0; i < inventory.length; i++) {
				setInventorySlotContents(i, null);
			}
		}
	}
	
	//Returns the amount of TP that a given item costs
	private int getTPCost(Item item) {
		if(item != null) {
			if(item == Item.redstone) return 2;
			if(item == Item.glowstone) return 5;
			if(item == Item.ingotIron) return 5;
			if(item == Item.ingotGold) return 20;
			if(item == Item.diamond) return 50;
			
			if(item == Item.bakedPotato) return 2;
			if(item == Item.beefCooked) return 4;
			if(item == Item.cake) return 6;
			if(item == Item.appleGold) return 15;
		}
		return 0;
	}
}
