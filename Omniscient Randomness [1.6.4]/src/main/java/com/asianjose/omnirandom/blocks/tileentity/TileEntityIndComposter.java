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

public class TileEntityIndComposter extends TileEntityOmni {

	// Initialize variables. Yayyyyy~~~~~
	public static final int INVENTORY_SIZE = 2;
	public static final int[] slots_top = new int[]{0, 1};
	
	
	
	private String localizedName;
	
	public Random rand = new Random();
	
	public int compostProgress;
	public int maxCompostProgress = 200;
	//////////////////////////////////////////////////////////// End Initialization o3o
	
	public TileEntityIndComposter() {
		inventory = new ItemStack[INVENTORY_SIZE];
	}
	
	//////////////////////////////////////////// Buncha random (necessary) functions
	public String getInvName(){
		return this.isInvNameLocalized() ? this.localizedName : "container.indComposter";
	}
	
	public void setGuiDisplayName(String displayName){
		this.localizedName = displayName;
	}

	@Override
	public int getSizeInventory(){
		return 2;
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
		this.inventory = new ItemStack[this.getSizeInventory()];
		
		for(int i = 0; i < list.tagCount(); i++){
			NBTTagCompound compound = (NBTTagCompound) list.tagAt(i);
			byte b = compound.getByte("Slot");
			
			if(b >= 0 && b < this.inventory.length){
				this.inventory[b] = ItemStack.loadItemStackFromNBT(compound);
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
		if(this.inventory[0] == null){
			return false;
		}else{
			ItemStack resultItem = getCompostResults(this.inventory[0]);
			
			if(resultItem == null) return false;
			if(this.inventory[1] == null) return true;
			if(!this.inventory[1].isItemEqual(resultItem)) return false;
			int result = inventory[1].stackSize + resultItem.stackSize;
            return (result <= getInventoryStackLimit() && result <= resultItem.getMaxStackSize());
		}
		
	}
	
	public void compost(){	//Compost the item in input if compostable
		if(canCompost()){
			ItemStack inputItem = this.inventory[0];
			ItemStack resultItem = getCompostResults(this.inventory[0]); 
			
			if(this.inventory[1] == null){
				this.inventory[1] = resultItem.copy();
			}else if(this.inventory[1].isItemEqual(resultItem)){
				this.inventory[1].stackSize += resultItem.stackSize;
			}
			
			if(inputItem.itemID == Block.sapling.blockID){
				this.inventory[0].stackSize -= 4;
			}else{
				this.inventory[0].stackSize -= resultItem.stackSize;
			}
				
				if(this.inventory[0].stackSize <= 0){
					this.inventory[0] = this.inventory[0].getItem().getContainerItemStack(this.inventory[0]);
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
