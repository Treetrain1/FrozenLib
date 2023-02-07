package net.frozenblock.lib.networking.api.marshaller;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

public class ItemMarshaller extends RegistryIdHolderMarshaller<Item> {

	public ItemMarshaller() {
		super(Registry.ITEM);
	}
}
