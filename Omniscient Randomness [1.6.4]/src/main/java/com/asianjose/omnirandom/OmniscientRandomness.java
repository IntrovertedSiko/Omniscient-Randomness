package com.asianjose.omnirandom;

import com.asianjose.omnirandom.blocks.BlockFridge;
import com.asianjose.omnirandom.blocks.BlockIndComposter;
import com.asianjose.omnirandom.blocks.BlockXPD;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityFridge;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityIndComposter;
import com.asianjose.omnirandom.blocks.tileentity.TileEntityXPD;
import com.asianjose.omnirandom.client.GuiTimePointBar;
import com.asianjose.omnirandom.handlers.ConfigurationHandler;
import com.asianjose.omnirandom.handlers.GuiHandler;
import com.asianjose.omnirandom.handlers.MainEventHandler;
import com.asianjose.omnirandom.handlers.PacketHandler;
import com.asianjose.omnirandom.init.ModBlocks;
import com.asianjose.omnirandom.init.ModItems;
import com.asianjose.omnirandom.proxy.ClientProxy;
import com.asianjose.omnirandom.proxy.CommonProxy;
import com.asianjose.omnirandom.reference.ModNames;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = ModNames.MOD_ID, name = ModNames.MOD_NAME, version = ModNames.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {"timeChannel"}, packetHandler = PacketHandler.class)
public class OmniscientRandomness {
	//////////////////////////////////////////// Randomness!
	@Instance("omnirandom")
	public static OmniscientRandomness instance;
	
	@SidedProxy(clientSide=ModNames.CLIENT_PROXY_CLASS, serverSide=ModNames.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	//////////////////////////////////////////// Initialization!
	private GuiHandler guiHandler = new GuiHandler();	
	public static final int guiIdFridge = 0;
	public static final int guiIdXPD = 1;
	public static final int guiIdIndComposter = 2;
	public static final int guiIdTPShop = 3;
	public static final int guiIdPocketFurnace = 4;
	public static final int guiIdEnchanter = 5;
	
	/*
	public static Block fridge;
	public static Block xpDecomposer;
	public static Block indComposter;
	
	public static int fridgeId;
	public static int xpDecomposerId;
	public static int indComposterId;
	*/
	public static int itemIdStart; //Starting item ID. Each consecutive itemid is 1 higher
	public static int blockIdStart; //Starting blockID. Each consecutive block is 1 higher than prev
	
	/* New tool material
	EnumHelper.addToolMaterial("name", harvestLevel, durability, eficiencyOnProperMaterial, dmgVsEntity, enchantability);
	*/
	
	/** Creative Tab = "Omniscient Randomness" **/
	public static final CreativeTabs OMNI_TAB = new CreativeTabs(CreativeTabs.getNextID(), "Omniscient Randomness")
	{
		@SideOnly(Side.CLIENT)
		public int getTabIconItemIndex() //The itemid of the icon to be used
		{
			return ModItems.coreOfSteve.itemID;
		}
		
		public String getTranslatedTabLabel() //Name of tab
		{
			return "Omniscient Randomness";
		}
	};
	
	/** Pre Initialization: config **/
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		ConfigurationHandler.init(event.getSuggestedConfigurationFile());
	/*	config.load();
		
		fridgeId = config.getBlock("fridge Id", 2000).getInt();
		xpDecomposerId = config.getBlock("XpDecomposer Id", 2001).getInt();
		indComposterId = config.getBlock("industriousComposter Id", 2002).getInt();
		
		config.save();*/
	}
	
	/** Initialization: init items, blocks, tileentities, recipes, misc **/
	@EventHandler
	public void init(FMLInitializationEvent event){
		
		MinecraftForge.EVENT_BUS.register(new MainEventHandler());
		
		ModItems.init(); //Initialize all my items
		
		ModBlocks.init();
		
		/** Thrown in favor for ModBlocks & BlockOmni(Container) & lang-file **/
	/*	// Initialize Blocks
		fridge = new BlockFridge(fridgeId, Material.rock).setHardness(1F).setResistance(1F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("fridge");
		xpDecomposer = new BlockXPD(xpDecomposerId, Material.rock).setHardness(1F).setResistance(1F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("xpDecomposer");
		indComposter = new BlockIndComposter(indComposterId, Material.rock).setHardness(1F).setResistance(1F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("indComposter");
		
		// Register Blocks
		GameRegistry.registerBlock(fridge, "fridge");
		GameRegistry.registerBlock(xpDecomposer, "xpDecomposer");
		GameRegistry.registerBlock(indComposter, "indComposter");
		
		LanguageRegistry.addName(fridge, "Refrigerator");
		LanguageRegistry.addName(xpDecomposer, "XP Decomposer");
		LanguageRegistry.addName(indComposter, "Industrious Composter");
		
		// Register Tile Entities
		GameRegistry.registerTileEntity(TileEntityFridge.class, "tileEntityFridge");
		GameRegistry.registerTileEntity(TileEntityXPD.class, "tileEntityXPD");
		GameRegistry.registerTileEntity(TileEntityIndComposter.class, "tileEntityIndComposter");
		*/
		
		// Miscellaneous
		NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
		
		// Crafting recipes!!! TODO: RecipeHandler/ModRecipes
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.fridge), "xyx","yiy", "xyx", 'x', Item.redstone, 'y', Block.ice, 'i', Block.blockIron);
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.fridge), "yxy", "xix", "yxy", 'x', Item.redstone, 'y', Block.ice, 'i', Block.blockIron);
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.xpDecomposer), "yxy", "ziz", "yxy", 'x', Item.diamond, 'y', Item.ghastTear, 'z', Block.dispenser, 'i', Block.blockIron);
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.xpDecomposer), "yzy", "xix", "yzy", 'x', Item.diamond, 'y', Item.ghastTear, 'z', Block.dispenser, 'i', Block.blockIron);
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.indComposter), "xyx", "ziz", "xyx", 'x', Block.cobblestoneMossy, 'y', Item.netherQuartz, 'z', Block.dirt, 'i', Block.blockIron);
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.indComposter), "xzx", "yiy", "xzx", 'x', Block.cobblestoneMossy, 'y', Item.netherQuartz, 'z', Block.dirt, 'i', Block.blockIron);
		
	}
	
	/** Post initialization **/
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(new GuiTimePointBar(Minecraft.getMinecraft()));
	}
}
