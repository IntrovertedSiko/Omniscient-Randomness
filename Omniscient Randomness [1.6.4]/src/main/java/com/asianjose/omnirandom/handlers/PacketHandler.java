package com.asianjose.omnirandom.handlers;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.asianjose.omnirandom.ExtendedPlayer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler{

	public PacketHandler(){}
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		//Good place to parse thru channels if there's multiple
		if(packet.channel.equals("timeChannel")) {
			handleExtendedProperties(packet, player);
		}
	}

	private void handleExtendedProperties(Packet250CustomPayload packet, Player player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		ExtendedPlayer props = ExtendedPlayer.get((EntityPlayer) player);
		
		// Everything we read here should match EXACTLY the order in which we wrote it
		// to the output stream in our ExtendedPlayer sync() method.
		try{
			props.setMaxTP(inputStream.readInt());
		} catch(IOException e) {
			e.printStackTrace();
			return;
		}
		// Just so you can see in the console that it's working:
		System.out.println("[PACKET] Mana from packet: " + props.getCurrentTP() + "/" + props.getMaxTP());
		
	}

}
