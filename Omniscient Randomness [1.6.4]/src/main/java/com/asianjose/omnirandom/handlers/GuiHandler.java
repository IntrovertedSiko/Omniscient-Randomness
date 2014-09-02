package com.asianjose.omnirandom.handlers;

import com.asianjose.omnirandom.OmniscientRandomness;
import com.asianjose.omnirandom.blocks.container.ContainerFridge;
import com.asianjose.omnirandom.blocks.container.ContainerIndComposter;
import com.asianjose.omnirandom.blocks.container.ContainerTPShop;
import com.asianjose.omnirandom.blocks.container.ContainerXPD;
import com.asianjose.omnirandom.blocks.gui.GuiFridge;
import com.asianjose.omnirandom.blocks.gui.GuiIndComposter;
import com.asianjose.omnirandom.blocks.gui.GuiTPShop;
import com.asianjose.omnirandom.blocks.gui.GuiXPD;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityFridge;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityIndComposter;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityTPShop;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityXPD;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	 public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
         TileEntity entity = world.getBlockTileEntity(x, y, z);
        
         if(entity != null){
                 switch(ID){
                         case OmniscientRandomness.guiIdXPD:
                                 if(entity instanceof TileEntityXPD){
                                         return new ContainerXPD(player.inventory, (TileEntityXPD) entity);
                                 }
                         case OmniscientRandomness.guiIdFridge:
                                 if(entity instanceof TileEntityFridge){
                                         return new ContainerFridge(player.inventory, (TileEntityFridge) entity);
                                 }
                         case OmniscientRandomness.guiIdIndComposter:
                        	 	if(entity instanceof TileEntityIndComposter){
                        	 			return new ContainerIndComposter(player.inventory, (TileEntityIndComposter) entity);
                        	 	}
                         case OmniscientRandomness.guiIdTPShop:
                        	 	if(entity instanceof TileEntityTPShop){
                        	 			return new ContainerTPShop(player.inventory, (TileEntityTPShop) entity);
                        	 	}
                 }
         }
        
         return null;
 }

 public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
         TileEntity entity = world.getBlockTileEntity(x, y, z);
        
         if(entity != null){
                 switch(ID){
                         case OmniscientRandomness.guiIdXPD:
                                 if(entity instanceof TileEntityXPD){
                                         return new GuiXPD(player.inventory, (TileEntityXPD) entity);
                                 }
                         case OmniscientRandomness.guiIdFridge:
                                 if(entity instanceof TileEntityFridge){
                                         return new GuiFridge(player.inventory, (TileEntityFridge) entity);
                                 }
                         case OmniscientRandomness.guiIdIndComposter:
                        	 	if(entity instanceof TileEntityIndComposter){
                        	 			return new GuiIndComposter(player.inventory, (TileEntityIndComposter) entity);
                        	 	}
                         case OmniscientRandomness.guiIdTPShop:
                     	 	if(entity instanceof TileEntityTPShop){
                     	 			return new GuiTPShop(player.inventory, (TileEntityTPShop) entity);
                     	 	}
                 }
         }
         return null;
 }
}
