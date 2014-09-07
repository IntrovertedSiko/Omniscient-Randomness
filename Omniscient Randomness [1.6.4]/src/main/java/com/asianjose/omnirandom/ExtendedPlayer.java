package com.asianjose.omnirandom;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import com.asianjose.omnirandom.proxy.CommonProxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedPlayer implements IExtendedEntityProperties{

	/** Lots of thanks to coolAlias for his IExtendedEntityProperties! **/
	public static final String EXT_PROP_NAME = "TimePoints";
	//For Reference
	private final EntityPlayer player;
	private static final int TP_WATCHER = 20;
	private int maxTP;
	/*
	 * etc Variables here
	 */
	
	// Original constructer takes no parameters. coolAlias added EntityPlayer to initialize "player"
	public ExtendedPlayer(EntityPlayer player) {
		this.player = player;
		this.maxTP = 500;
		this.player.getDataWatcher().addObject(TP_WATCHER, 5);	//All players start w/ 0 TP, but can go up to 500 (TODO: see what value is best [config option?])
	}
	
	//Used for convenience. Creates an instance of this ExtendedPlayer for each player
	public static final void register(EntityPlayer player) {
		player.registerExtendedProperties(ExtendedPlayer.EXT_PROP_NAME, new ExtendedPlayer(player));
	}
	
	//Used for convenience. Returns the instance of the given player's extendedProperties
	public static final ExtendedPlayer get(EntityPlayer player) {
		return (ExtendedPlayer) player.getExtendedProperties(EXT_PROP_NAME);
	}
	
	//Wut
	@Override
	public void init(Entity entity, World world) {
	}

	//Save stuff...
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = new NBTTagCompound();
		
		properties.setInteger("CurrentTP", this.player.getDataWatcher().getWatchableObjectInt(TP_WATCHER));
		properties.setInteger("MaxTP", 500);
		
		//Add these properties to the player's tag. Use a unique name!
		//IE if we try to use "Items," it'll conflict w/ vanilla
		compound.setTag(EXT_PROP_NAME, properties);
	}

	//Load stuff...
	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
		
		try {
		this.player.getDataWatcher().updateObject(TP_WATCHER, properties.getInteger("CurrentTP"));
		this.maxTP = properties.getInteger("MaxTP");
		} catch(NullPointerException exception) {
			System.out.println("[ExtendedPlayer] Could not load data!");
		}
	}

	private static String getSaveKey(EntityPlayer player) {
		return player.username + ":" + EXT_PROP_NAME;
		}
	
	/**
	* Does everything in onLivingDeathEvent and it's static,
	* so you now only need to use the following in the event:
	* ExtendedPlayer.saveProxyData((EntityPlayer) event.entity));
	*/
	public static void saveProxyData(EntityPlayer player) {
		ExtendedPlayer playerData = ExtendedPlayer.get(player);
		NBTTagCompound savedData = new NBTTagCompound();
		
		playerData.saveNBTData(savedData);
		CommonProxy.storeEntityData(getSaveKey(player), savedData);
	}
	
	public static void loadProxyData(EntityPlayer player) {
		ExtendedPlayer playerData = ExtendedPlayer.get(player);
		NBTTagCompound savedData = CommonProxy.getEntityData(getSaveKey(player));
		
		if(savedData != null) {
			playerData.loadNBTData(savedData);
		}
		playerData.sync();
	}
	
	
	
	/** Sync data w/ client. Called whenever TP is changed (IE all the methods) **/
	public final void sync() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
		DataOutputStream outputStream = new DataOutputStream(bos);
		
		try {
			outputStream.writeInt(this.maxTP);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * TODO: Add whatever methods needed to utilize the Time Points. Basic ones sort of covered
	 * 
	 **/
	
	/** Attempts to consume gven amount of Time Points
	 * 
	 * @param amount
	 * @return true if amount is successfully taken; false if not enough TP
	 */
	public boolean consumeTP(int amount) {
		int time = this.player.getDataWatcher().getWatchableObjectInt(TP_WATCHER);
		if(amount >= time) {
			return false; //If the ideal amount exceeds the current amount of Time Points
		} 
		time -= amount; //Takes away amount, since there's enough
		this.player.getDataWatcher().updateObject(TP_WATCHER, time);
		return true;
	}
	
	//NOTE: The method below isn't the complete opposite of the above, because 
	// 		I don't know how to break out of ".updateObject()" if player doesn't have enough points
	
	/** Attempts to add given amount of Time Points
	 * 
	 * @param amount
	 * @return true if amount is successfully added; false if there's leftovers
	 */
	public final void addTP(int amount) {
		int time = this.player.getDataWatcher().getWatchableObjectInt(TP_WATCHER);
		amount = (amount + time) > this.maxTP ? this.maxTP : amount + time;
		this.player.getDataWatcher().updateObject(TP_WATCHER, (amount));
		//TODO: Get leftovers and do something with it?
	}
	
	/** Sets player's current Time Points to given amount. If it's more than max, then set to max **/
	public void setTP(int amount) {
		this.player.getDataWatcher().updateObject(TP_WATCHER, (amount < this.maxTP ? amount : this.maxTP));
	}
	
	/** Sets possible max Time Points to given amount. Used by PacketHandler **/
	public void setMaxTP(int amount) {
		this.maxTP = amount;
	}
	
	/**
	 * @return Player's current Time Points
	 */
	public int getCurrentTP() {
		return this.player.getDataWatcher().getWatchableObjectInt(TP_WATCHER);
	}
	
	/**
	 * @return Player's possible max Time Points
	 */
	public int getMaxTP() {
		return this.maxTP;
	}
}
