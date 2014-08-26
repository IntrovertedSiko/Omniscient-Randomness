package com.asianjose.omnirandom.blocks.tileentity;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityIndComposter extends TileEntity implements ISidedInventory{

	// Initialize variables. Yayyyyy~~~~~
	public static final int[] slots_top = new int[]{0, 1};
	
	private ItemStack[] slots = new ItemStack[2];
	
	private String localizedName;
	
	public Random rand = new Random();
	
	public int compostProgress;
	public int maxCompostProgress = 200;
	//////////////////////////////////////////////////////////// End Initialization o3o
	
	//////////////////////////////////////////// Buncha random (necessary) functions
	public String getInvName(){
		return this.isInvNameLocalized() ? this.localizedName : "container.indComposter";
	}
	
	public boolean isInvNameLocalized() {	
		return this.localizedName != null && this.localizedName.length() > 0;
	}
	
	public void setGuiDisplayName(String displayName){
		this.localizedName = displayName;
	}

	public int getSizeInventory(){
		return 2;
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
		return i == 1;
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
		
		this.compostProgress = nbt.getShort("compostProgress");
		
		if(nbt.hasKey("CustomName")){
			this.localizedName = nbt.getString("CustomName");
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		
		nbt.setShort("compostProgress", (short)this.compostProgress);
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
	
	public void updateEntity(){		//Updates the entity (20 ticks -> 1 second)
		if(!this.worldObj.isRemote){
		/*	this.worldObj.scheduleBlockUpdate(xCoord - 1, yCoord, zCoord, worldObj.getBlockId(xCoord - 1, yCoord, zCoord), 0);
			this.worldObj.scheduleBlockUpdate(xCoord + 1, yCoord, zCoord, worldObj.getBlockId(xCoord + 1, yCoord, zCoord), 0);
			this.worldObj.scheduleBlockUpdate(xCoord - 1, yCoord, zCoord - 1, worldObj.getBlockId(xCoord - 1, yCoord, zCoord - 1), 0);
			this.worldObj.scheduleBlockUpdate(xCoord - 1, yCoord, zCoord + 1, worldObj.getBlockId(xCoord - 1, yCoord, zCoord + 1), 0);
			this.worldObj.scheduleBlockUpdate(xCoord + 1, yCoord, zCoord - 1, worldObj.getBlockId(xCoord + 1, yCoord, zCoord - 1), 0);
			this.worldObj.scheduleBlockUpdate(xCoord + 1, yCoord, zCoord + 1, worldObj.getBlockId(xCoord + 1, yCoord, zCoord + 1), 0);
			this.worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord - 1, worldObj.getBlockId(xCoord, yCoord, zCoord - 1), 0);
			this.worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord + 1, worldObj.getBlockId(xCoord, yCoord, zCoord + 1), 0);
			*/
			if(this.canCompost()){
				++this.compostProgress;
				
				if(this.compostProgress >= this.maxCompostProgress){
					this.compostProgress = 0;
					this.compost();
				}
			}else{
				this.compostProgress = 0;
			}
		}
	}
	
	public boolean isComposting(){		//True if composting
		return this.compostProgress > 0;
	}
	
	public static ItemStack getCompostResults(ItemStack itemstack){	//Add more items for more "composting" options
		if(itemstack == null){
			return null;
		}else{
			if(itemstack.itemID == Item.rottenFlesh.itemID) return new ItemStack(Item.appleGold, 5);
			if(itemstack.itemID == Block.leaves.blockID) return new ItemStack(Block.sapling, 1, itemstack.getItemDamage());
			if(itemstack.itemID == Block.sapling.blockID) return new ItemStack(Block.dirt, 1);
			if(itemstack.itemID == Item.glowstone.itemID) return new ItemStack(Item.goldNugget, 2);
			if(itemstack.itemID == Item.netherStalkSeeds.itemID) return new ItemStack(Block.slowSand, 1);
			if(itemstack.itemID == Block.slowSand.blockID) return new ItemStack(Item.netherQuartz, 1);
			if(itemstack.itemID == Block.gravel.blockID) return new ItemStack(Item.flint, 1);
			if(itemstack.itemID == Item.bucketMilk.itemID) return new ItemStack(Item.slimeBall, 2);
			return null;
		}
	}
	
	public boolean canCompost(){	//True if the tile entity is able to compost the input
		if(this.slots[0] == null){
			return false;
		}else{
			ItemStack resultItem = getCompostResults(this.slots[0]);
			
			if(resultItem == null) return false;
			if(this.slots[1] == null) return true;
			if(!this.slots[1].isItemEqual(resultItem)) return false;
			int result = slots[1].stackSize + resultItem.stackSize;
            return (result <= getInventoryStackLimit() && result <= resultItem.getMaxStackSize());
		}
		
	}
	
	public void compost(){	//Compost the item in input if compostable
		if(canCompost()){
			ItemStack inputItem = this.slots[0];
			ItemStack resultItem = getCompostResults(this.slots[0]); 
			
			if(this.slots[1] == null){
				this.slots[1] = resultItem.copy();
			}else if(this.slots[1].isItemEqual(resultItem)){
				this.slots[1].stackSize += resultItem.stackSize;
			}
			
			if(inputItem.itemID == Block.sapling.blockID){
				this.slots[0].stackSize -= 4;
			}else{
				this.slots[0].stackSize -= resultItem.stackSize;
			}
				
				if(this.slots[0].stackSize <= 0){
					this.slots[0] = this.slots[0].getItem().getContainerItemStack(this.slots[0]);
				}
			}
		}
	
	public static boolean isItemFuel(ItemStack itemstack){		//True if slot == compostable
		return getCompostResults(itemstack) != null;
	}
	
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {	//True if... it's valid
		return i == 1 ? false : isItemFuel(itemstack);
	}
	
	public int getcompostProgressRemainingScaled(int i){	//For Gui thingy. 
		return this.compostProgress * i / this.maxCompostProgress;
	}
	/** 							Where the fun ends :/							**/


	///////////////////////// Can't get rid of these .-.
	public void openChest() {
		
	}

	public void closeChest() {
		
	}
}
