package net.frozenblock.lib.core.interfaces;

import net.frozenblock.lib.core.sound.MovingLoopingSoundEntityManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

public interface EntityLoopingSoundInterface {

    boolean hasSyncedClient();

    MovingLoopingSoundEntityManager getSounds();

    void addSound(ResourceLocation soundID, SoundSource category, float volume, float pitch, ResourceLocation restrictionId);

}
