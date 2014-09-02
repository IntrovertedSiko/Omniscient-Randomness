package com.asianjose.omnirandom.events;

import java.util.Random;

import com.asianjose.omnirandom.ExtendedPlayer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class BreakBlockEvent {

	private Random rand = new Random();
	
	/** Current Uses: drops multiple items when broken with a "seeking" pickaxe
	 *  NOTE: If you're trying to make an event... make sure it's registered
	 **/
	@ForgeSubscribe
	public void BreakEvent(BlockEvent.HarvestDropsEvent event) {
		EntityPlayer player = event.harvester;
		if(player != null) {
			Block brokenBlock = event.block;
			int brokenBlockMeta = event.blockMetadata;
			ItemStack heldItem = event.harvester.getCurrentEquippedItem();
			if((brokenBlock instanceof BlockOre || brokenBlock instanceof BlockRedstoneOre) && !event.world.isRemote && heldItem != null) {
				if(brokenBlock != Block.oreIron && brokenBlock != Block.oreGold) {
					if(heldItem.stackTagCompound.getBoolean("Seeking") == true) {
						ExtendedPlayer props = ExtendedPlayer.get(player);
						props.addTP(1);
						event.drops.add(new ItemStack(brokenBlock.idDropped(brokenBlockMeta, rand, 0), rand.nextInt(5), 4));
					}
				}
			}
		}
	}
}
