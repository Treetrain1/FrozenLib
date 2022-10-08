package net.frozenblock.lib.forge.worldgen.biome;

import com.mojang.datafixers.util.Pair;
import net.frozenblock.lib.common.worldgen.biome.api.FrozenBiomeSourceAccess;
import net.frozenblock.lib.common.worldgen.biome.impl.OverworldBiomeData;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;

import java.util.stream.Collectors;

public class ForgeOverworldBiomeData {

    public static void modifyBiomeSource(Registry<Biome> biomeRegistry, BiomeSource biomeSource) {
        if (biomeSource instanceof MultiNoiseBiomeSource multiNoiseBiomeSource) {
            if (((FrozenBiomeSourceAccess) multiNoiseBiomeSource).frozenLib_shouldModifyBiomeEntries() && multiNoiseBiomeSource.stable(MultiNoiseBiomeSource.Preset.OVERWORLD)) {
                multiNoiseBiomeSource.parameters = OverworldBiomeData.withModdedBiomeEntries(
                        MultiNoiseBiomeSource.Preset.OVERWORLD.parameterSource.apply(biomeRegistry),
                        biomeRegistry);
                ((ForgeBiomeInterface) multiNoiseBiomeSource).setPossibleBiomes(multiNoiseBiomeSource.parameters.values().stream().map(Pair::getSecond).collect(Collectors.toSet()));
                ((FrozenBiomeSourceAccess) multiNoiseBiomeSource).frozenLib_setModifyBiomeEntries(false);
            }
        }
    }
}
