package com.asianjose.omnirandom.blocks.gui;

import org.lwjgl.opengl.GL11;

import com.asianjose.omnirandom.blocks.container.ContainerIndComposter;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityIndComposter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiIndComposter extends GuiContainer{

	 public static ResourceLocation texture = new ResourceLocation("omnirandom", "textures/gui/indcGui.png");     
     
     public TileEntityIndComposter indComposter;
    
     public GuiIndComposter(InventoryPlayer inventoryPlayer, TileEntityIndComposter entity) {
             super(new ContainerIndComposter(inventoryPlayer, entity));
            
             this.indComposter = entity;
            
             int xSize = 176;
             int ySize = 166;
     }

     public void drawGuiContainerForegroundLayer(int par1, int par2)
     {
             String name = this.indComposter.isInvNameLocalized() ? this.indComposter.getInvName() : I18n.getString(this.indComposter.getInvName());
            
             this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
             this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
     }
    
     public void drawGuiContainerBackgroundLayer(float f, int i, int j) {
             GL11.glColor4f(1F, 1F, 1F, 1F);
            
             Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
             drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
            
             if(this.indComposter.isComposting())
             {
                     int k = this.indComposter.getcompostProgressRemainingScaled(33);
                     this.drawTexturedModalRect(guiLeft + 71, guiTop + 16, 176, 0, k, 34);
          //   }else{
            //	 this.drawTexturedModalRect(guiLeft + 71, guiTop + 16, 176, 0, 0, 34);
             }
            
     }
}
