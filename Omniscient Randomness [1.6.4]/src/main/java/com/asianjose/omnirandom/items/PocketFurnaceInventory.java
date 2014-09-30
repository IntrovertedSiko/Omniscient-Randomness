package com.asianjose.omnirandom.items;

import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class PocketFurnaceInventory implements IInventory{
	private String name = "Inventory Item";

	public static final int INV_SIZE = 3;

	/** Inventory's size must be same as number of slots you add to the Container class */
	private ItemStack[] inventory = new ItemStack[INV_SIZE];

	/** Provides NBT Tag Compound to reference */
	private final ItemStack invItem;

	private int furnaceBurnTime;
	private int furnaceCookTime;
	private int currentBurnTime;
	private int furnaceSpeed = 200;
	
	/**
	* @param itemstack - the ItemStack to which this inventory belongs
	*/
	public PocketFurnaceInventory(ItemStack stack)
	{
		this.invItem = stack;
		
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		
		readFromNBT(stack.getTagCompound());
	}

	@Override
	public int getSizeInventory()
	{
	return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
	return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		ItemStack stack = getStackInSlot(slot);
		if(stack != null)
		{
			if(stack.stackSize > amount)
			{
				stack = stack.splitStack(amount);
				// 1.7.2 -> markDirty()
				onInventoryChanged();
			} else {
				setInventorySlotContents(slot, null);
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		ItemStack stack = getStackInSlot(slot);
		if(stack != null)
		{
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack)
	{
		this.inventory[slot] = itemstack;
	
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
		{
			itemstack.stackSize = this.getInventoryStackLimit();
		}
	
		// 1.7.2 -> markDirty()
		onInventoryChanged();
	}

	// 1.7.2 -> getInventoryName
	@Override
	public String getInvName()
	{
		return name;
	}

	// 1.7.2 -> hasCustomInventoryName
	@Override
	public boolean isInvNameLocalized()
	{
		return name.length() > 0;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	// 1.7.2 -> markDirty
	@Override
	public void onInventoryChanged()
	{
		for (int i = 0; i < getSizeInventory(); ++i)
		{
			if (getStackInSlot(i) != null && getStackInSlot(i).stackSize == 0)
				inventory[i] = null;
		}
		// be sure to write to NBT when the inventory changes!
		writeToNBT(invItem.getTagCompound());
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		//Only usable as long as the player is holding the item (so they won't make a paradox)
		return player.getHeldItem() == invItem;
	}

	// 1.7.2 -> openInventory
	@Override
	public void openChest() {}

	// 1.7.2 -> closeInventory
	@Override
	public void closeChest() {}

	/**
	* This method doesn't seem to do what it claims to do, as
	* items can still be left-clicked and placed in the inventory
	* even when this returns false
	*/
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		//TODO: not ItemCoreOfSteve
		return !(itemstack.getItem() instanceof ItemCoreOfSteve);
	}

	/**
	* A custom method to read our inventory from an ItemStack's NBT compound
	*/
	public void readFromNBT(NBTTagCompound tagcompound)
	{
		// 1.7.2 -> compound.getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);
		NBTTagList items = tagcompound.getTagList("ItemInventory");
	
		for (int i = 0; i < items.tagCount(); ++i)
		{
			// 1.7.2 -> items.getCompoundTagAt(i)
			NBTTagCompound item = (NBTTagCompound) items.tagAt(i);
			byte slot = item.getByte("Slot");
		
			if (slot >= 0 && slot < getSizeInventory()) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(item);
			}
		}
	}

	/**
	* A custom method to write our inventory to an ItemStack's NBT compound
	*/
	public void writeToNBT(NBTTagCompound tagcompound)
	{
		NBTTagList items = new NBTTagList();
	
		for (int i = 0; i < getSizeInventory(); ++i)
		{
			// Only write stacks that contain items
			if (getStackInSlot(i) != null)
			{
				NBTTagCompound item = new NBTTagCompound();
				item.setByte("Slot", (byte) i);
				getStackInSlot(i).writeToNBT(item);
			
				items.appendTag(item);
			}
		}
		tagcompound.setTag("ItemInventory", items);
	}
	
	public void updateEntity(World world, Player player) {
			
			boolean flag = this.furnaceBurnTime > 0;
	        boolean flag1 = false;

	        if (this.furnaceBurnTime > 0)
	        {
	            --this.furnaceBurnTime;
	        }

	        if (!world.isRemote)
	        {
	            if (this.furnaceBurnTime == 0 && this.canSmelt())
	            {
	                this.currentBurnTime = this.furnaceBurnTime = getItemBurnTime(this.inventory[1]);

	                if (this.furnaceBurnTime > 0)
	                {
	                    flag1 = true;

	                    if (this.inventory[1] != null)
	                    {
	                        --this.inventory[1].stackSize;

	                        if (this.inventory[1].stackSize == 0)
	                        {
	                            this.inventory[1] = this.inventory[1].getItem().getContainerItemStack(inventory[1]);
	                        }
	                    }
	                }
	            }

	            if (this.isBurning() && this.canSmelt())
	            {
	                ++this.furnaceCookTime;

	                if (this.furnaceCookTime == 200)
	                {
	                    this.furnaceCookTime = 0;
	                    this.smeltItem();
	                    flag1 = true;
	                }
	            }
	            else
	            {
	                this.furnaceCookTime = 0;
	            }
	        }

	        if (flag1)
	        {
	            this.onInventoryChanged();
	        }
	}
	/**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt()
    {
        if (this.inventory[0] == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.inventory[0]);
            if (itemstack == null) return false;
            if (this.inventory[2] == null) return true;
            if (!this.inventory[2].isItemEqual(itemstack)) return false;
            int result = inventory[2].stackSize + itemstack.stackSize;
            return (result <= getInventoryStackLimit() && result <= itemstack.getMaxStackSize());
        }
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.inventory[0]);

            if (this.inventory[2] == null)
            {
                this.inventory[2] = itemstack.copy();
            }
            else if (this.inventory[2].isItemEqual(itemstack))
            {
                inventory[2].stackSize += itemstack.stackSize;
            }

            --this.inventory[0].stackSize;

            if (this.inventory[0].stackSize <= 0)
            {
                this.inventory[0] = null;
            }
        }
    }

    /**
     * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
     * fuel
     */
    public static int getItemBurnTime(ItemStack par0ItemStack)
    {
        if (par0ItemStack == null)
        {
            return 0;
        }
        else
        {
            int i = par0ItemStack.getItem().itemID;
            Item item = par0ItemStack.getItem();

            if (par0ItemStack.getItem() instanceof ItemBlock && Block.blocksList[i] != null)
            {
                Block block = Block.blocksList[i];

                if (block == Block.woodSingleSlab)
                {
                    return 150;
                }

                if (block.blockMaterial == Material.wood)
                {
                    return 300;
                }

                if (block == Block.coalBlock)
                {
                    return 16000;
                }
            }

            if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemHoe && ((ItemHoe) item).getMaterialName().equals("WOOD")) return 200;
            if (i == Item.stick.itemID) return 100;
            if (i == Item.coal.itemID) return 1600;
            if (i == Item.bucketLava.itemID) return 20000;
            if (i == Block.sapling.blockID) return 100;
            if (i == Item.blazeRod.itemID) return 2400;
            return GameRegistry.getFuelValue(par0ItemStack);
        }
    }

    /**
     * Return true if item is a fuel source (getItemBurnTime() > 0).
     */
    public static boolean isItemFuel(ItemStack itemstack)
    {
        return getItemBurnTime(itemstack) > 0;
    }
    
    public boolean isBurning() {
    	return furnaceBurnTime > 0;
    }
    
    @SideOnly(Side.CLIENT)

    /**
     * Returns an integer between 0 and the passed value representing how close the current item is to being completely
     * cooked
     */
    public int getCookProgressScaled(int par1)
    {
        return this.furnaceCookTime * par1 / 200;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Returns an integer between 0 and the passed value representing how much burn time is left on the current fuel
     * item, where 0 means that the item is exhausted and the passed value means that the item is fresh
     */
    public int getBurnTimeRemainingScaled(int par1)
    {
        if (this.currentBurnTime == 0)
        {
            this.currentBurnTime = 200;
        }

        return this.furnaceBurnTime * par1 / this.currentBurnTime;
    }
}
