package com.asianjose.omnirandom.blocks.container;

import com.asianjose.omnirandom.blocks.tileentity.TileEntityXPD;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerXPD extends Container {

	 private TileEntityXPD xpDecomposer;
     
     private int lastDecomposeTime;
    
     public ContainerXPD(InventoryPlayer inventory, TileEntityXPD tileentity)
     {
             this.xpDecomposer = tileentity;
            
             this.addSlotToContainer(new Slot(tileentity, 0, 81, 53));
             
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
             icrafting.sendProgressBarUpdate(this, 0, this.xpDecomposer.decomposeTime);
     }
    
     public void detectAndSendChanges()
     {
             super.detectAndSendChanges();
            
             for(int i = 0; i < this.crafters.size(); i++){
                     ICrafting icrafting = (ICrafting) this.crafters.get(i);
                    
                     if(this.lastDecomposeTime != this.xpDecomposer.decomposeTime){
                             icrafting.sendProgressBarUpdate(this, 0, this.xpDecomposer.decomposeTime);
                     }
             }
            
             this.lastDecomposeTime = this.xpDecomposer.decomposeTime;
     }
    
     @SideOnly(Side.CLIENT)
     public void updateProgressBar(int slot, int newValue)
     {
             if(slot == 0) this.xpDecomposer.decomposeTime = newValue;
     }
    
     public ItemStack transferStackInSlot(EntityPlayer player, int clickedSlot)
     {
             ItemStack itemstack = null;
             Slot slot = (Slot) this.inventorySlots.get(clickedSlot);
            
             if(slot != null && slot.getHasStack())
             {
                     ItemStack itemstack1 = slot.getStack();
                     itemstack = itemstack1.copy();
                    
                     if(clickedSlot == 0)
                     {
                             if(!this.mergeItemStack(itemstack1, 1, 37, true))
                             {
                             return null;   
                             }
                             slot.onSlotChange(itemstack1, itemstack);
                     }
                     else if(clickedSlot != 0){
                             if(TileEntityXPD.isItemFuel(itemstack))
                             {
                                     if(!this.mergeItemStack(itemstack1, 0, 1, false))
                                     {
                                     return null;
                                     }
                             }else if(clickedSlot >= 1 && clickedSlot <= 27){
                                     if(!this.mergeItemStack(itemstack1, 28, 37, false))
                                     {
                                             return null;
                                     }
                             }else if(clickedSlot >= 28 && clickedSlot <= 36){
                                     if(!this.mergeItemStack(itemstack1, 1, 28, false))
                                     {
                                             return null;
                                     }
                             }
                     }else if(!this.mergeItemStack(itemstack1, 1, 37, false)){
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
             return this.xpDecomposer.isUseableByPlayer(entityplayer);
     }

}
