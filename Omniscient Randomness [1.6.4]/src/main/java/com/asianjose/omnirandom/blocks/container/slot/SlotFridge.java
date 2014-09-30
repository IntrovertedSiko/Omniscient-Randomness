package com.asianjose.omnirandom.blocks.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class SlotFridge extends Slot
{
     public SlotFridge(IInventory inventory, int slotId, int x, int y)
     {
         super (inventory, slotId, x, y);
     }
     
     public boolean isItemValid(ItemStack itemstack)
     {
        //TODO: return true if itemstack.getItem() is damagable; Should I put a null check?
    	 return itemstack.isItemStackDamageable();
     }
}