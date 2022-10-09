package net.frozenblock.lib.common.worldgen.biome.api;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.Set;

public interface FrozenMultiNoiseBiomeSourceAccess {

    boolean frozenLib_shouldModifyBiomeEntries();

    void frozenLib_setModifyBiomeEntries(boolean modifyBiomeEntries);
}
