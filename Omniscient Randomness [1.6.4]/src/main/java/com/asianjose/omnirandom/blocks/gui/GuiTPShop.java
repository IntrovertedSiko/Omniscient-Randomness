package com.asianjose.omnirandom.blocks.gui;

import org.lwjgl.opengl.GL11;

import com.asianjose.omnirandom.ExtendedPlayer;
import com.asianjose.omnirandom.blocks.container.ContainerFridge;
import com.asianjose.omnirandom.blocks.container.ContainerTPShop;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityTPShop;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiTPShop extends GuiContainer{

	 public static ResourceLocation texture = new ResourceLocation("omnirandom", "textures/gui/tpshopGui.png");     
     
     public TileEntityTPShop shop;
     private Minecraft mc;
     //BUTTON!
     private GuiButton previous, next;
    
     public GuiTPShop(InventoryPlayer inventoryPlayer, TileEntityTPShop entity) {
             super(new ContainerTPShop(inventoryPlayer, entity));
            
             this.shop = entity;
            
             int xSize = 176;
             int ySize = 164;
     }

     public void drawGuiContainerForegroundLayer(int par1, int par2)
     {
             String name = this.shop.isInvNameLocalized() ? this.shop.getInvName() : I18n.getString(this.shop.getInvName());
            
             this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
             this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
             //Writes how much TP the player has
             String playerTP = this.mc.getMinecraft().thePlayer.username + ": " + ExtendedPlayer.get(this.mc.getMinecraft().thePlayer).getCurrentTP();
             this.fontRenderer.drawString(playerTP, this.xSize / 2 - this.fontRenderer.getStringWidth(playerTP), 20, 4210752);
             
     }
    
     public void drawGuiContainerBackgroundLayer(float f, int i, int j) {
             GL11.glColor4f(1F, 1F, 1F, 1F);
            
             Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
             drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
     }
     
     //BUTTONS! for switching pages in TPShop
    /* @Override
     public void initGui() {
    	 buttonList.clear();
    	 this.buttonList.add(previous = new GuiButton(1, 7, 21, 36, 16, "Previous"));
    	 this.buttonList.add(next = new GuiButton(2, 43, 21, 36, 16, "Next"));
     }
     */
     /*
     @Override
     public void actionPerformed(GuiButton button) {
    	 switch(button.id) {
    	 case 1:
    		 //Go back a page if it's not already at page 1
    		 if(shop.getShopPage() > 1) {
    			 shop.updateShopPage(-1);
    		 }
    		 break;
    		 
    	 case 2:
    		 //Go forward a page if it's not already at page max
    		 if(shop.getShopPage() < shop.getShopPageMax()) {
    			 shop.updateShopPage(1);
    		 }
    	 }
     }*/
}
