package com.asianjose.omnirandom.blocks.container;

import com.asianjose.omnirandom.blocks.tileentity.TileEntityTPShop;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerTPShop extends Container {

	 private TileEntityTPShop shop;
    
     public ContainerTPShop(InventoryPlayer inventory, TileEntityTPShop tileentity)
     {
             this.shop = tileentity;
            
             for(int i = 0; i < 9; i++)
             {
                     this.addSlotToContainer(new Slot(tileentity, i, i * 18 + 8, 38));
             }
            
             for(int i = 0; i < 3; i++)
             {
                     for(int j = 0; j < 9; j++)
                     {
                             this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 82 + i * 18));
                     }
             }
            
             for(int i = 0; i < 9; i++)
             {
                     this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 140));
             }
     }
     
     public ItemStack transferStackInSlot(EntityPlayer player, int clickedSlot)
     {
             ItemStack itemstack = null;
             Slot slot = (Slot) this.inventorySlots.get(clickedSlot);
            /*
             if(slot != null && slot.getHasStack())
             {
                     ItemStack itemstack1 = slot.getStack();
                     itemstack = itemstack1.copy();
                    /*
                     if(clickedSlot <= 9)
                     {
                             if(!this.mergeItemStack(itemstack1, 10, 45, true))
                             {
                             return null;   
                             }
                             slot.onSlotChange(itemstack1, itemstack);
                     }
                     else if(!(clickedSlot <= 9)){
                             if(TileEntityTPShop.isItemFuel(itemstack))
                             {
                                     if(!this.mergeItemStack(itemstack1, 0, 1, false))
                                     {
                                     return null;
                                     }
                             }else if(itemstack1.isItemDamaged()){
                                     if(!this.mergeItemStack(itemstack1, 1, 10, false))
                                     {
                                             return null;
                                     }
                             }else if(clickedSlot >= 10 && clickedSlot <= 36){
                                     if(!this.mergeItemStack(itemstack1, 37, 46, false))
                                     {
                                             return null;
                                     }
                             }else if(clickedSlot >= 37 && clickedSlot <= 45){
                                     if(!this.mergeItemStack(itemstack1, 10, 37, false))
                                     {
                                             return null;
                                     }
                             }
                     }else if(!this.mergeItemStack(itemstack1, 10, 46, false)){
                             return null;
                     }
                    
                     if(itemstack1.stackSize == 0)
                     {
                             slot.putStack((ItemStack) null);
                     }else{
                             slot.onSlotChanged();
                     }
                    
                     if(itemstack1.stackSize == itemstack.stackSize)
                     {
                             return null;
                     }
                    
                     slot.onPickupFromSlot(player, itemstack1);
             }*/
       return itemstack;
      }
    
     public boolean canInteractWith(EntityPlayer entityplayer) {
             return this.shop.isUseableByPlayer(entityplayer);
     }

}
