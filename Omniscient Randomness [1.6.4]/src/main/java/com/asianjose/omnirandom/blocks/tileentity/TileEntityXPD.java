package com.asianjose.omnirandom.blocks.tileentity;

import java.util.Random;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityXPD extends TileEntity implements ISidedInventory{

	// Initialize variables. Yayyyyy~~~~~
	public static final int[] slots_top = new int[]{0};
	public static final int[] notPoweredSlots = new int[0];
	
	private ItemStack[] slots = new ItemStack[1];
	
	private String localizedName;
	
	public Random rand = new Random();
	
	public int decomposeTime;
	public int maxDecomposeTime = 200;
	//////////////////////////////////////////////////////////// End Initialization o3o
	
	//////////////////////////////////////////// Buncha random (necessary) functions
	public String getInvName(){
		return this.isInvNameLocalized() ? this.localizedName : "container.xpDecomposer";
	}
	
	public boolean isInvNameLocalized() {	
		return this.localizedName != null && this.localizedName.length() > 0;
	}
	
	public void setGuiDisplayName(String displayName){
		this.localizedName = displayName;
	}

	public int getSizeInventory(){
		return 1;
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
		return worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) ? slots_top : notPoweredSlots;
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
		
		this.decomposeTime = nbt.getShort("DecomposeTime");
		
		if(nbt.hasKey("CustomName")){
			this.localizedName = nbt.getString("CustomName");
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		
		nbt.setShort("DecomposeTime", (short)this.decomposeTime);
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
			if(this.isDecomposing()){
				--this.decomposeTime;
				int m = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
				if(rand.nextInt(50)==1){	//Increase random number for less orbs
					if(m==4)this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.xCoord - 1, this.yCoord, this.zCoord, 1));
					if(m==2)this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.xCoord + 1, this.yCoord, this.zCoord, 1));
					if(m==5)this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.xCoord, this.yCoord, this.zCoord - 1, 1));
					if(m==4)this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.xCoord - 1, this.yCoord, this.zCoord + 1, 1));
				}
			}
			
			if(this.decomposeTime <= 0 && isItemFuel(this.slots[0])){ //Checks slot to start decomposing
				this.decomposeTime = this.maxDecomposeTime;
				--this.slots[0].stackSize;
				
				if(this.slots[0].stackSize == 0){
					this.slots[0] = this.slots[0].getItem().getContainerItemStack(this.slots[0]);
				}
			}
		}
	}
	
	public boolean isDecomposing(){		//True if decomposing
		return this.decomposeTime > 0;
	}
	
	public static int getDecomposeXpAmount(ItemStack itemstack){	//Add more items for more "decomposing" options
		if(itemstack == null){
			return 0;
		}else{
			if(itemstack.itemID == Item.rottenFlesh.itemID) return 100;
			if(itemstack.itemID == Item.bone.itemID) return 200;
			return 0;
		}
	}
	
	public static boolean isItemFuel(ItemStack itemstack){		//True if slot == decomposable
		return getDecomposeXpAmount(itemstack) > 0;
	}
	
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {	//True if... it's valid
		return i == 0 ? isItemFuel(itemstack) : (i > 0 ? this.slots[i].getItemDamage() > 0 : false);
	}
	
	public int getCoolTimeRemainingScaled(int i){	//For Gui thingy. 
		return this.decomposeTime * i / this.maxDecomposeTime;
	}
	/** 							Where the fun ends :/							**/


	///////////////////////// Can't get rid of these .-.
	public void openChest() {
		
	}

	public void closeChest() {
		
	}
}
