package com.asianjose.omnirandom.blocks.gui;

import org.lwjgl.opengl.GL11;

import com.asianjose.omnirandom.blocks.container.ContainerFridge;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityFridge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiFridge extends GuiContainer{

	 public static ResourceLocation texture = new ResourceLocation("omnirandom", "textures/blocks/fridgeGui.png");     
     
     public TileEntityFridge fridge;
    
     public GuiFridge(InventoryPlayer inventoryPlayer, TileEntityFridge entity) {
             super(new ContainerFridge(inventoryPlayer, entity));
            
             this.fridge = entity;
            
             int xSize = 176;
             int ySize = 166;
     }

     public void drawGuiContainerForegroundLayer(int par1, int par2)
     {
             String name = this.fridge.isInvNameLocalized() ? this.fridge.getInvName() : I18n.getString(this.fridge.getInvName());
            
             this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
             this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
     }
    
     public void drawGuiContainerBackgroundLayer(float f, int i, int j) {
             GL11.glColor4f(1F, 1F, 1F, 1F);
            
             Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
             drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
            
             if(this.fridge.isCooling())
             {
                     int k = this.fridge.getCoolTimeRemainingScaled(100);
                     drawTexturedModalRect(guiLeft + 33, guiTop + 13, 0, 166, k + 1, 16);
             }
            
     }
}
