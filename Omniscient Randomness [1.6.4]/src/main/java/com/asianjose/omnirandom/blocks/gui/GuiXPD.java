package com.asianjose.omnirandom.blocks.gui;

import org.lwjgl.opengl.GL11;

import com.asianjose.omnirandom.blocks.container.ContainerXPD;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityXPD;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiXPD extends GuiContainer{

	 public static ResourceLocation texture = new ResourceLocation("omnirandom", "textures/blocks/xpdGui.png");     
     
     public TileEntityXPD XPD;
    
     public GuiXPD(InventoryPlayer inventoryPlayer, TileEntityXPD entity) {
             super(new ContainerXPD(inventoryPlayer, entity));
            
             this.XPD = entity;
            
             int xSize = 176;
             int ySize = 166;
     }

     public void drawGuiContainerForegroundLayer(int par1, int par2)
     {
             String name = this.XPD.isInvNameLocalized() ? this.XPD.getInvName() : I18n.getString(this.XPD.getInvName());
            
             this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
             this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
     }
    
     public void drawGuiContainerBackgroundLayer(float f, int i, int j) {
             GL11.glColor4f(1F, 1F, 1F, 1F);
            
             Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
             drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
            
             if(this.XPD.isDecomposing())
             {
                     int k = this.XPD.getCoolTimeRemainingScaled(26);
                     this.drawTexturedModalRect(guiLeft + 84, guiTop + 24 + k, 176, k, 10, 27);
             }
            
     }
}
