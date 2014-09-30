package com.asianjose.omnirandom.handlers;

import java.util.Random;

import com.asianjose.omnirandom.ExtendedPlayer;
import com.asianjose.omnirandom.init.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent;

public class MainEventHandler {

	private Random rand = new Random();
	
	//TODO: Should I centralize all the events?
	
	//"Registers"(?) the IExtendedEntityProperties
	@ForgeSubscribe
	public void onEntityConstructing(EntityConstructing event) {
		//Make sure the entity exists & (optional) the properties don't exist yet
		if(event.entity != null && event.entity instanceof EntityPlayer) {
			if(ExtendedPlayer.get((EntityPlayer) event.entity) == null) {
				ExtendedPlayer.register((EntityPlayer) event.entity);
			}
		}
	}
	
	@ForgeSubscribe
	public void onLivingDeathEvent(LivingDeathEvent event) {
		if(!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
			ExtendedPlayer.saveProxyData((EntityPlayer) event.entity);
		}
	}
	
	/** Called when a player first enters a world. Used to give them the soul-weapons (uncraftable)
	 *  & to sync/load players' extendedProperties (after death)
	 **/
	// TODO: Link to extended properties: current dupe -- drop item, relog, repeat
	@ForgeSubscribe
	public void JoinWorldEvent(EntityJoinWorldEvent event) {
		if(event.entity != null && event.entity instanceof EntityPlayer && !event.world.isRemote) {
			EntityPlayer player = (EntityPlayer) event.entity;
			if(!event.world.isRemote && !player.inventory.hasItem(ModItems.coreOfSteve.itemID)) {
				System.out.println("Hello World! Player: " + event.entity);
				player.inventory.addItemStackToInventory(new ItemStack(ModItems.coreOfSteve));
			}
			if(ExtendedPlayer.get((EntityPlayer) event.entity) == null) {
				ExtendedPlayer.register((EntityPlayer) event.entity);
			}
			ExtendedPlayer.loadProxyData(player);
			ExtendedPlayer.get(player).sync();
		}
	}
	
	//Add 1TP for killing a HOSTILE mob
	@ForgeSubscribe
	public void killMob(LivingDeathEvent event) {
		Entity mob = event.entity;
		
		if(event.source.getEntity() != null && event.source.getEntity() instanceof EntityPlayer && mob instanceof EntityMob) {
			EntityPlayer player = (EntityPlayer) event.source.getEntity();
			ExtendedPlayer props = ExtendedPlayer.get(player);
			props.addTP(1);
		}
	}
	
	/** Current Uses: drops multiple items when broken with a "seeking" pickaxe & adds 1TP
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


