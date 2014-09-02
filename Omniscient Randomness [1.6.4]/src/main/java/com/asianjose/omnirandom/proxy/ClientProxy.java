package com.asianjose.omnirandom.proxy;

import com.asianjose.omnirandom.client.GuiTimePointBar;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy{

	//Registers rendering stuff
	@Override
	public void registerRenderers() {
	//	System.out.println("GUI registered"); 	I don't know how to use proxies! Why is it not working?
		//MinecraftForge.EVENT_BUS.register(new GuiTimePointBar(Minecraft.getMinecraft())); 	Moved to main class
	}
}
