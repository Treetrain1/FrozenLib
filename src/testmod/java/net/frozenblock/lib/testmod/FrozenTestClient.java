package net.frozenblock.lib.testmod;

import net.fabricmc.api.ClientModInitializer;
import net.frozenblock.lib.sound.FlyBySoundHub;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;

@ClientOnly
public final class FrozenTestClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FlyBySoundHub.autoEntitiesAndSounds.put(EntityType.ARROW, new FlyBySoundHub.FlyBySound(1.0F, 1.0F, SoundSource.NEUTRAL, SoundEvents.AXE_SCRAPE));
    }
}
