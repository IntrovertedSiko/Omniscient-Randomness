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

public class TileEntityFridge extends TileEntityOmni {

	/** Logical variables **/
	public static final int[] slots_top = new int[]{0};
	public static final int INVENTORY_SIZE = 10;
	private String localizedName;
	
	/** Variables used for the functionality of the machine **/
	public Random rand = new Random();
	public int coolTime = 0;
	public int maxCoolTime = 200;
	public int repairItemDelay = 100 + (rand.nextInt(100));
	//////////////////////////////////////////////////////////// End Initialization o3o
	
	public TileEntityFridge() {
		inventory = new ItemStack[INVENTORY_SIZE];
	}
	
	//////////////////////////////////////////// Buncha random (necessary) functions
	public String getInvName(){
		return this.isInvNameLocalized() ? this.localizedName : "container.fridge";
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
		
		this.coolTime = nbt.getShort("CoolTime");
		
		if(nbt.hasKey("CustomName")){
			this.localizedName = nbt.getString("CustomName");
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		
		nbt.setShort("CoolTime", (short)this.coolTime);
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
		if(!worldObj.isRemote){
			if(this.inventory[0] != null){
				if(this.coolTime + getItemCoolTime(inventory[0]) < this.maxCoolTime && getItemCoolTime(inventory[0]) > 0){
					this.coolTime += getItemCoolTime(inventory[0]);
					
					--this.inventory[0].stackSize;
					if(this.inventory[0].stackSize == 0){
						this.inventory[0] = this.inventory[0].getItem().getContainerItemStack(inventory[0]);
					}
				}
			}
			
			if(this.repairItemDelay > 0){
				this.repairItemDelay--;
			}else if(this.repairItemDelay <= 0){
				for(int i = 1; i < 9; i++){
					if(this.coolTime > 0 && this.inventory[i] != null){
						
						if(this.inventory[i].getItemDamage() < this.inventory[i].getMaxDamage()){
							
							this.inventory[i] = new ItemStack(this.inventory[i].getItem(), this.inventory[i].stackSize, this.inventory[i].getItemDamage() - 1);
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
		return i == 0 ? isItemFuel(itemstack) : (i > 0 ? this.inventory[i].getItemDamage() > 0 : false);
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
