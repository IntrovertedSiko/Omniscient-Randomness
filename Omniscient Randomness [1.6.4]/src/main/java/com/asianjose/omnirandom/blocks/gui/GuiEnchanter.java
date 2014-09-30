package com.asianjose.omnirandom.blocks.gui;

import org.lwjgl.opengl.GL11;

import com.asianjose.omnirandom.blocks.container.ContainerEnchanter;
import com.asianjose.omnirandom.blocks.container.ContainerFridge;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityEnchanter;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityFridge;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiEnchanter extends GuiContainer{

	 public static ResourceLocation texture = new ResourceLocation("omnirandom", "textures/gui/enchanterGui.png");     
     
     public TileEntityEnchanter enchanter;
    
     public GuiEnchanter(InventoryPlayer inventoryPlayer, TileEntityEnchanter entity) {
             super(new ContainerEnchanter(inventoryPlayer, entity));
            
             this.enchanter = entity;
            
             int xSize = 176;
             int ySize = 166;
     }

     public void drawGuiContainerForegroundLayer(int par1, int par2)
     {
             String name = this.enchanter.isInvNameLocalized() ? this.enchanter.getInvName() : I18n.getString(this.enchanter.getInvName());
            
             this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
             this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
     }
    
     public void drawGuiContainerBackgroundLayer(float f, int i, int j) {
             GL11.glColor4f(1F, 1F, 1F, 1F);
            
             Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
             drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
            
            if(this.enchanter.isEnchanting()) {
            	//TODO
            	int k = this.enchanter.getEnchantingTimeScaled(24);
                drawTexturedModalRect(guiLeft + 33, guiTop + 13, 0, 166, k + 1, 16);
            }
     }
}
