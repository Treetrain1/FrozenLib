package net.frozenblock.lib.networking.api.marshaller;

import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;

public class SoundEventMarshaller extends RegistryIdHolderMarshaller<SoundEvent> {

	public SoundEventMarshaller() {
		this.idMap = Registry.SOUND_EVENT;
	}
}
