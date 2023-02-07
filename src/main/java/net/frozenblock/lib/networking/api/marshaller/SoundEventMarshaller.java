package net.frozenblock.lib.networking.api.marshaller;

import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;

public class SoundEventMarshaller extends RegistryIdHolderMarshaller<SoundEvent> {

	public SoundEventMarshaller() {
		super(Registry.SOUND_EVENT);
	}
}
