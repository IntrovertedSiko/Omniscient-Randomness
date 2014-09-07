package com.asianjose.omnirandom.client;


import org.lwjgl.opengl.GL11;

import com.asianjose.omnirandom.ExtendedPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTimePointBar extends Gui{

	private Minecraft mc;
	
	//Texture
	private static final ResourceLocation texturePath = new ResourceLocation("omnirandom", "textures/gui/tp_bar.png");
	
	public GuiTimePointBar(Minecraft mc) {
		super();
		//Invokes render engine
		this.mc = mc;
	}
	/** Credits to coolAlias (creditor's credits: Minecraftforge.wiki) **/
	// Event called by GuiIngameForge during each frame by 
	// GuiIngameForge.pre() & GuiIngameForge.post()
	@ForgeSubscribe(priority = EventPriority.NORMAL)
	public void onRenderExperienceBar(RenderGameOverlayEvent event) {
		// We draw after the ExperienceBar has drawn. The event raised by GuiIngameForge.pre()
		// will return true from isCancelable. If you call event.setCanceled(true) in
		// that case, the portion of rendering which this event represents will be canceled.
		// We want to draw *after* the experience bar is drawn, so we make sure isCancelable() returns
		// false and that the eventType represents the ExperienceBar event.
		if (event.isCancelable() || event.type != ElementType.EXPERIENCE)
		{
		return;
		}
		
		ExtendedPlayer props = ExtendedPlayer.get(this.mc.thePlayer);
		
		// Don't render if (for some reason) it doesn't exist
		if(props == null || props.getMaxTP() == 0) {
			return;
		}
		
		// Where the gui should start rendering. TODO: find the perf location
		int xPos = 2;
		int yPos = 2;

		// setting all color values to 1.0F will render the texture as it looks in your texture file
		//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		// Somewhere in Minecraft vanilla code it says to do this because of a lighting bug
		//GL11.glDisable(GL11.GL_LIGHTING);

		// Bind your texture to the render engine
		this.mc.getTextureManager().bindTexture(texturePath);

		// Add this block of code before you draw the section of your texture containing TRANSPARENCY
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		// Here we draw the background bar which contains a transparent section; note the new size
		drawTexturedModalRect(xPos, yPos, 0, 0, 8, 50);
		
		// TODO: Change "48" according to how tall the texture is
		int timePointsHeight = (int)(((float) props.getCurrentTP() / props.getMaxTP()) * 48);
		drawTexturedModalRect(xPos + 1, yPos + 1, 8, timePointsHeight - 48, 6, 48);
		
		// NOTE: be sure to reset the openGL settings after you're done or your character model will be messed up
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
	}
}
