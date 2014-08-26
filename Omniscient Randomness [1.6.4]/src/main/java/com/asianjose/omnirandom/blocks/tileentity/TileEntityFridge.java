package com.asianjose.omnirandom.blocks.tileentity;

import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFridge extends TileEntity implements ISidedInventory{

	// Initialize variables. Yayyyyy~~~~~
	public static final int[] slots_top = new int[]{0};
	
	private ItemStack[] slots = new ItemStack[10];
	
	private String localizedName;
	
	public Random rand = new Random();
	
	public int coolTime = 0;
	public int maxCoolTime = 200;
	public int repairItemDelay = 100 + (rand.nextInt(100));
	//////////////////////////////////////////////////////////// End Initialization o3o
	
	//////////////////////////////////////////// Buncha random (necessary) functions
	public String getInvName(){
		return this.isInvNameLocalized() ? this.localizedName : "container.fridge";
	}
	
	public boolean isInvNameLocalized() {	
		return this.localizedName != null && this.localizedName.length() > 0;
	}
	
	public void setGuiDisplayName(String displayName){
		this.localizedName = displayName;
	}

	public int getSizeInventory(){
		return 10;
	}

	public ItemStack getStackInSlot(int i) {
		return this.slots[i];
	}

	public ItemStack decrStackSize(int i, int j) {
		if(this.slots[i] != null){
			ItemStack itemstack;
			
			if(this.slots[i].stackSize <= j){
				itemstack = this.slots[i];
				
				this.slots[i] = null;
				return itemstack;
			}else{
				itemstack = this.slots[i].splitStack(j);
				
				if(this.slots[i].stackSize == 0){
					this.slots[i] = null;
				}
				
				return itemstack;
			}
		}
		return null;
	}

	public ItemStack getStackInSlotOnClosing(int i) {
		if(this.slots[i] != null){
			ItemStack itemstack = this.slots[i];
			this.slots[i] = null;
			
			return itemstack;
		}
		return null;
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.slots[i] = itemstack;
		
		if(itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()){
			itemstack.stackSize = this.getInventoryStackLimit();
		}
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityplayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
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
		this.slots = new ItemStack[this.getSizeInventory()];
		
		for(int i = 0; i < list.tagCount(); i++){
			NBTTagCompound compound = (NBTTagCompound) list.tagAt(i);
			byte b = compound.getByte("Slot");
			
			if(b >= 0 && b < this.slots.length){
				this.slots[b] = ItemStack.loadItemStackFromNBT(compound);
			}
		}
		
		this.coolTime = nbt.getShort("CoolTime");
		
		if(nbt.hasKey("CustomName")){
			this.localizedName = nbt.getString("CustomName");
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		
		nbt.setShort("CoolTime", (short)this.coolTime);
		NBTTagList list = new NBTTagList();
		
		for(int i = 0; i < this.slots.length; i++){
			if(this.slots[i] != null){
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte)i);
				this.slots[i].writeToNBT(compound);
				list.appendTag(compound);
			}
		}
		
		nbt.setTag("Items", list);
		
		if(this.isInvNameLocalized()){
			nbt.setString("Custom Name", this.localizedName);
		}
	}
	
	public void updateEntity(){
		if(!worldObj.isRemote){
			if(this.slots[0] != null){
				if(this.coolTime + getItemCoolTime(slots[0]) < this.maxCoolTime && getItemCoolTime(slots[0]) > 0){
					this.coolTime += getItemCoolTime(slots[0]);
					
					--this.slots[0].stackSize;
					if(this.slots[0].stackSize == 0){
						this.slots[0] = this.slots[0].getItem().getContainerItemStack(slots[0]);
					}
				}
			}
			
			if(this.repairItemDelay > 0){
				this.repairItemDelay--;
			}else if(this.repairItemDelay <= 0){
				for(int i = 1; i < 9; i++){
					if(this.coolTime > 0 && this.slots[i] != null){
						
						if(this.slots[i].getItemDamage() < this.slots[i].getMaxDamage()){
							
							this.slots[i] = new ItemStack(this.slots[i].getItem(), this.slots[i].stackSize, this.slots[i].getItemDamage() - 1);
							this.coolTime--;
						}
					}
				}
				this.repairItemDelay = 100 + (rand.nextInt(100));
			}
			
		}
	}
	
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
	
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return i == 0 ? isItemFuel(itemstack) : (i > 0 ? this.slots[i].getItemDamage() > 0 : false);
	}
	
	public int getCoolTimeRemainingScaled(int i){
		return this.coolTime * i / this.maxCoolTime;
	}
	/** 							Where the fun ends :/							**/


	///////////////////////// Can't get rid of these .-.
	public void openChest() {
		
	}

	public void closeChest() {
		
	}
}
