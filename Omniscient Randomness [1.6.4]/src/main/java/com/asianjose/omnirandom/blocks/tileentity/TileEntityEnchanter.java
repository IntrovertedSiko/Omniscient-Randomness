package com.asianjose.omnirandom.blocks.tileentity;

import java.util.Random;

import com.asianjose.omnirandom.items.ItemScroll;
import com.asianjose.omnirandom.items.ItemScrollActivator;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityEnchanter extends TileEntityOmni implements IInventory {

	/** Usual variables **/
	public static final int[] slots_top = new int[]{0}; //TODO: input from hopper slots
	/**
	 * Slots: [0 & 1] = input (scroll/tool); [2] = activator input; [3] = output
	 */
	public static final int INVENTORY_SIZE = 4;
	private String localizedName;
	
	/** Variables used for the functionality of the machine **/
	public Random rand = new Random();
	public int maxEnchantTime = 400;
	public int enchantTime = 0;
	//////////////////////////////////////////////////////////// End Initialization o3o
	
	public TileEntityEnchanter() {
		inventory = new ItemStack[INVENTORY_SIZE];
	}
	
	//////////////////////////////////////////// Buncha random (necessary) functions
	public String getInvName(){
		return this.isInvNameLocalized() ? this.localizedName : "container.enchanter";
	}

	public void setGuiDisplayName(String displayName){
		this.localizedName = displayName;
	}

	public int[] getAccessibleSlotsFromSide(int var1) {
		return slots_top;
	}

	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return isItemValidForSlot(i, itemstack);
	}
	
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return false;
	}

	/**                            Where the fun starts :D                         **/
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		
		NBTTagList list = nbt.getTagList("Items");
		this.inventory = new ItemStack[this.getSizeInventory()];
		
		for(int i = 0; i < list.tagCount(); i++){
			NBTTagCompound compound = (NBTTagCompound) list.tagAt(i);
			byte b = compound.getByte("Slot");
			
			if(b >= 0 && b < this.inventory.length){
				this.inventory[b] = ItemStack.loadItemStackFromNBT(compound);
			}
		}
		
		if(nbt.hasKey("CustomName")){
			this.localizedName = nbt.getString("CustomName");
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		
		NBTTagList list = new NBTTagList();
		
		for(int i = 0; i < this.inventory.length; i++){
			if(this.inventory[i] != null){
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte)i);
				this.inventory[i].writeToNBT(compound);
				list.appendTag(compound);
			}
		}
		
		nbt.setTag("Items", list);
		
		if(this.isInvNameLocalized()){
			nbt.setString("Custom Name", this.localizedName);
		}
	}
	
	@Override
	public boolean canUpdate() {
		return true;
	}
	
	public void updateEntity(){
		if(this.isEnchanting()) {
			this.enchantTime--;
		}
		if(!worldObj.isRemote){
			if(this.canEnchant() && this.enchantTime < this.maxEnchantTime) {
				this.enchantTime++;
				if(this.enchantTime >= this.maxEnchantTime) {
					this.enchantTime = 0;
					this.enchant();
				}
			}
		}
	}
	
	/**
	 * Strict criteria for if the block is allowed to enchant:
	 * Slot 1: any tool
	 * Slot 2: any scroll
	 * Slot 3: any activator
	 * Slot 4: Empty [output slot]
	 */
	private boolean canEnchant(){
		for(int i=0; i < inventory.length; i++) {
			if(inventory[i] == null) return false;
		}
		if((inventory[0].getItem() instanceof ItemTool) && (inventory[1].getItem() instanceof ItemScroll) &&
			(inventory[2].getItem() instanceof ItemScrollActivator) && (inventory[3] == null)) {
				return true;
			} else {
				return false;
			}
	}
	
	private void enchant() {
		if(this.canEnchant()) {
			//TODO: placeholder
			this.inventory[0].addEnchantment(this.getScrollEnchantment(this.inventory[1]), this.getActivatorLevel(this.inventory[2]));
			this.inventory[3] = this.inventory[0].copy();
			this.inventory[0] = null;
			this.inventory[1] = null;
			this.inventory[2].setItemDamage(this.inventory[2].getItemDamage() + 1);
		}
	}
	
	//TODO: placeholder
	private Enchantment getScrollEnchantment(ItemStack itemstack) {
		if(itemstack.getItem() instanceof ItemScroll) return Enchantment.efficiency;
		return null;
	}
	
	private int getActivatorLevel(ItemStack itemstack) {
		if(itemstack.getItem() instanceof ItemScrollActivator) return 3;
		return 1;
	}
	
	public boolean isEnchanting() {
		return this.enchantTime > 0;
	}
	
	public int getEnchantingTimeScaled(int scale) {
		return (this.enchantTime * scale / this.maxEnchantTime);
	}
	
	/*
	public static int getItemCoolTime(ItemStack itemstack){
		if(itemstack == null){
			return 0;
		}else{
			if(itemstack.itemID == Item.ghastTear.itemID) return 100;
			if(itemstack.itemID == Block.ice.blockID) return 10;
			if(itemstack.itemID == Item.bucketWater.itemID) return 5;
            if(itemstack.itemID == Block.snow.blockID) return 4;          
            if(itemstack.itemID == Item.snowball.itemID) return 1;
            return GameRegistry.getFuelValue(itemstack);
		}
	}
	
	public static boolean isItemFuel(ItemStack itemstack){
		return getItemCoolTime(itemstack) > 0;
	}
	
	public boolean isCooling(){
		return this.coolTime > 0;
	}
	
	public int getCoolTimeRemainingScaled(int i){
		return this.coolTime * i / this.maxCoolTime;
	}
	*/
	/** 							Where the fun ends :/							**/

	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		Item item = itemstack.getItem();
		return i == 0 ? (item instanceof ItemTool) : (i==1 ? (item instanceof ItemScroll) : (i==2) ? (item instanceof ItemScrollActivator) : (item==null));
	}

	///////////////////////// Can't get rid of these .-.
	public void openChest() {
		
	}

	public void closeChest() {
		
	}
}
