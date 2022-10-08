package net.frozenblock.lib.forge.worldgen.biome;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.Set;

public interface ForgeBiomeInterface {
    void setPossibleBiomes(Set<Holder<Biome>> value);
}
