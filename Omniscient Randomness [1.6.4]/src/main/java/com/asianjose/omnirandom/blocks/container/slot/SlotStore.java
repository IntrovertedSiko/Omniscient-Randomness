package com.asianjose.omnirandom.blocks.container.slot;

import com.asianjose.omnirandom.ExtendedPlayer;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityTPShop;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class SlotStore extends Slot
{
	private ExtendedPlayer props;
	private TileEntityTPShop inventory;
     public SlotStore(EntityPlayer player, IInventory inventory, int slotId, int x, int y)
     {
    	super(inventory, slotId, x, y);
     	this.props = ExtendedPlayer.get(player);
     	this.inventory = (TileEntityTPShop) inventory;
     }
     
     /**
      * Can't put items in! 
      */
     public boolean isItemValid(ItemStack itemstack)
     {
        return false;
     }
     
     /**
     * Don't decrease itemstack... just give them the item
     */
    public ItemStack decrStackSize(int par1)
    {
        return super.decrStackSize(par1);
    }
    
    /**
     * Subtract TP when an Item is taken
     */
    public void onPickupFromSlot(EntityPlayer player, ItemStack itemStack)
    {
    	if(itemStack != null){
    		props.consumeTP(inventory.getTPCost(itemStack.getItem()));
    	}
        this.onSlotChanged();
    }
}