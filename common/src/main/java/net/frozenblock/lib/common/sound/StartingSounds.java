package net.frozenblock.lib.common.sound;

import dev.architectury.registry.registries.RegistrySupplier;
import net.frozenblock.lib.common.FrozenMain;
import net.frozenblock.lib.common.registry.FrozenRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;

import java.util.HashMap;

public class StartingSounds {

    /**
     * Use this to associate a Starting Sound to a {@link ResourceKey} for later use.
     */
    public static HashMap<ResourceKey<?>, SoundEvent> startingSounds = new HashMap<>();

    public static final RegistrySupplier<SoundEvent> EMPTY_SOUND = register("empty_sound");

    public static RegistrySupplier<SoundEvent> register(String key) {
        return FrozenRegistry.DEFERRED_STARTING_SOUND.register(key, () -> new SoundEvent(FrozenMain.id(key)));
    }

}
