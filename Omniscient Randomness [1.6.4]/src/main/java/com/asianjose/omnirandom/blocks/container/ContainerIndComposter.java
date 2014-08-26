package com.asianjose.omnirandom.blocks.container;

import com.asianjose.omnirandom.blocks.tileentity.TileEntityIndComposter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerIndComposter extends Container {

	 private TileEntityIndComposter indComposter;
     
     private int lastDecomposeTime;
    
     public ContainerIndComposter(InventoryPlayer inventory, TileEntityIndComposter tileentity)
     {
             this.indComposter = tileentity;
            
             this.addSlotToContainer(new Slot(tileentity, 0, 41, 27));
             this.addSlotToContainer(new Slot(tileentity, 1, 120, 25));
             
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
             icrafting.sendProgressBarUpdate(this, 0, this.indComposter.compostProgress);
     }
    
     public void detectAndSendChanges()
     {
             super.detectAndSendChanges();
            
             for(int i = 0; i < this.crafters.size(); i++){
                     ICrafting icrafting = (ICrafting) this.crafters.get(i);
                    
                     if(this.lastDecomposeTime != this.indComposter.compostProgress){
                             icrafting.sendProgressBarUpdate(this, 0, this.indComposter.compostProgress);
                     }
             }
            
             this.lastDecomposeTime = this.indComposter.compostProgress;
     }
    
     @SideOnly(Side.CLIENT)
     public void updateProgressBar(int slot, int newValue)
     {
             if(slot == 0) this.indComposter.compostProgress = newValue;
     }
    
     public ItemStack transferStackInSlot(EntityPlayer player, int clickedSlot)
     {
             ItemStack itemstack = null;
             Slot slot = (Slot) this.inventorySlots.get(clickedSlot);
            
             if(slot != null && slot.getHasStack())
             {
                     ItemStack itemstack1 = slot.getStack();
                     itemstack = itemstack1.copy();
                    
                    if(clickedSlot <= 1)
                     {
                             if(!this.mergeItemStack(itemstack1, 2, 38, true))
                             {
                             return null;   
                             }
                             slot.onSlotChange(itemstack1, itemstack);
                     }
                     else if(clickedSlot >= 2){
                             if(TileEntityIndComposter.isItemFuel(itemstack))
                             {
                                     if(!this.mergeItemStack(itemstack1, 0, 1, false))
                                     {
                                     return null;
                                     }
                             }else if(clickedSlot >= 2 && clickedSlot <= 28){
                                     if(!this.mergeItemStack(itemstack1, 29, 38, false))
                                     {
                                             return null;
                                     }
                             }else if(clickedSlot >= 28 && clickedSlot <= 37){
                                     if(!this.mergeItemStack(itemstack1, 2, 29, false))
                                     {
                                             return null;
                                     }
                             }
                     }else if(!this.mergeItemStack(itemstack1, 2, 38, false)){
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
             return this.indComposter.isUseableByPlayer(entityplayer);
     }

}
