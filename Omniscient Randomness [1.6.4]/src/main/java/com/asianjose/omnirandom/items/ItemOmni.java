package com.asianjose.omnirandom.items;

import com.asianjose.omnirandom.OmniscientRandomness;
import com.asianjose.omnirandom.Reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.client.renderer.texture.IconRegister;

public class ItemOmni extends Item{

	public ItemOmni(int id) {
		super(id);
		this.setCreativeTab(OmniscientRandomness.OMNI_TAB);
	}
	/** Generic unlocalization processing (basically, the name of the item within the code) **/
	@Override
	public String getUnlocalizedName()
	{
		return String.format("item.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	// Returns  item.omnirandom:"insertItem".name
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return String.format("item.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}
	
	protected String getUnwrappedUnlocalizedName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
	
	/** Generic icon registering (using generic naming) **/
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) // 1.7: IconRegister -> IIconRegister
	{
		itemIcon = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1));
	// Returns omnirandom:"itemName"
		// The file should just be "itemName"
	}

}