package com.asianjose.omnirandom.blocks.container;

import com.asianjose.omnirandom.blocks.tileentity.TileEntityFridge;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFridge extends Container {

	 private TileEntityFridge fridge;
     
     private int lastBurnTime;
     private int lastItemBurnTime;
    
     public ContainerFridge(InventoryPlayer inventory, TileEntityFridge tileentity)
     {
             this.fridge = tileentity;
            
             this.addSlotToContainer(new Slot(tileentity, 0, 8, 12));
             for(int i = 0; i < 9; i++)
             {
                     this.addSlotToContainer(new Slot(tileentity, i + 1, i * 18 + 8, 40));
             }
            
             for(int i = 0; i < 3; i++)
             {
                     for(int j = 0; j < 9; j++)
                     {
                             this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
                     }
             }
            
             for(int i = 0; i < 9; i++)
             {
                     this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
             }
     }
    
     public void addCraftingtoCrafters(ICrafting icrafting)
     {
             super.addCraftingToCrafters(icrafting);
             icrafting.sendProgressBarUpdate(this, 0, this.fridge.coolTime);
     }
    
     public void detectAndSendChanges()
     {
             super.detectAndSendChanges();
            
             for(int i = 0; i < this.crafters.size(); i++){
                     ICrafting icrafting = (ICrafting) this.crafters.get(i);
                    
                     if(this.lastBurnTime != this.fridge.coolTime){
                             icrafting.sendProgressBarUpdate(this, 0, this.fridge.coolTime);
                     }
             }
            
             this.lastBurnTime = this.fridge.coolTime;
     }
    
     @SideOnly(Side.CLIENT)
     public void updateProgressBar(int slot, int newValue)
     {
             if(slot == 0) this.fridge.coolTime = newValue;
     }
    
     public ItemStack transferStackInSlot(EntityPlayer player, int clickedSlot)
     {
             ItemStack itemstack = null;
             Slot slot = (Slot) this.inventorySlots.get(clickedSlot);
            
             if(slot != null && slot.getHasStack())
             {
                     ItemStack itemstack1 = slot.getStack();
                     itemstack = itemstack1.copy();
                    
                     if(clickedSlot <= 9)
                     {
                             if(!this.mergeItemStack(itemstack1, 10, 45, true))
                             {
                             return null;   
                             }
                             slot.onSlotChange(itemstack1, itemstack);
                     }
                     else if(!(clickedSlot <= 9)){
                             if(TileEntityFridge.isItemFuel(itemstack))
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
                             }else if(clickedSlot >= 7 && clickedSlot <= 33){
                                     if(!this.mergeItemStack(itemstack1, 34, 42, false))
                                     {
                                             return null;
                                     }
                             }else if(clickedSlot >= 34 && clickedSlot <= 42){
                                     if(!this.mergeItemStack(itemstack1, 7, 33, false))
                                     {
                                             return null;
                                     }
                             }
                     }else if(!this.mergeItemStack(itemstack1, 7, 42, false)){
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
             }
       return itemstack;
      }
    
     public boolean canInteractWith(EntityPlayer entityplayer) {
             return this.fridge.isUseableByPlayer(entityplayer);
     }

}
