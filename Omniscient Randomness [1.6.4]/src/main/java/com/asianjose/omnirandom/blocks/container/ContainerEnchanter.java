package com.asianjose.omnirandom.blocks.container;

import com.asianjose.omnirandom.blocks.container.slot.SlotEnchanter;
import com.asianjose.omnirandom.blocks.container.slot.SlotEnchanterOutput;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityEnchanter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEnchanter extends Container {

	 private TileEntityEnchanter enchanter;
     
     private int lastEnchantTime;
 //    private int lastItemBurnTime;
    
    
     public ContainerEnchanter(InventoryPlayer inventory, TileEntityEnchanter tileentity)
     {
             this.enchanter = tileentity;
            
            //TODO: fix where the slots are
             this.addSlotToContainer(new SlotEnchanter(tileentity, 0, 16, 20, "Tool")); //Tool input
             this.addSlotToContainer(new SlotEnchanter(tileentity, 0, 24, 28, "Scroll")); //Scroll input
             this.addSlotToContainer(new SlotEnchanter(tileentity, 0, 32, 36, "ScrollActivator")); //Activator input
             this.addSlotToContainer(new SlotEnchanterOutput(inventory.player, tileentity, 0, 8, 12)); //output
             
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
             icrafting.sendProgressBarUpdate(this, 0, this.enchanter.enchantTime);
     }
    
     public void detectAndSendChanges()
     {
             super.detectAndSendChanges();
            
             for(int i = 0; i < this.crafters.size(); i++){
                     ICrafting icrafting = (ICrafting) this.crafters.get(i);
                    
                     if(this.lastEnchantTime != this.enchanter.enchantTime){
                             icrafting.sendProgressBarUpdate(this, 0, this.enchanter.enchantTime);
                     }
             }
            
             this.lastEnchantTime = this.enchanter.enchantTime;
     }
    
     @SideOnly(Side.CLIENT)
     public void updateProgressBar(int slot, int newValue)
     {
             if(slot == 0) this.enchanter.enchantTime = newValue;
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
                             if(TileEntityEnchanter.isItemFuel(itemstack))
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
             }
       return itemstack;
      }
    
     public boolean canInteractWith(EntityPlayer entityplayer) {
             return this.enchanter.isUseableByPlayer(entityplayer);
     }

}
