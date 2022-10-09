package net.frozenblock.lib.quilt.mixin.worldgen.biome;

import net.frozenblock.lib.common.worldgen.biome.api.FrozenBiomeSourceAccess;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(BiomeSource.class)
public class BiomeSourceMixin implements FrozenBiomeSourceAccess {
    @Mutable
    @Shadow @Final private Set<Holder<Biome>> lazyPossibleBiomes;

    @Override
    public void frozenLib_setPossibleBiomes(Set<Holder<Biome>> value) {
        this.lazyPossibleBiomes = value;
    }
}
