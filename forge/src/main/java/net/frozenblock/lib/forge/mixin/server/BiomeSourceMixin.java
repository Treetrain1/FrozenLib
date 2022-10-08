package net.frozenblock.lib.forge.mixin.server;

import com.google.common.base.Suppliers;
import net.frozenblock.lib.forge.worldgen.biome.ForgeBiomeInterface;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;
import java.util.function.Supplier;

@Mixin(BiomeSource.class)
public class BiomeSourceMixin implements ForgeBiomeInterface {
    @Shadow(remap = false) @Final @Mutable
    private Supplier<Set<Holder<Biome>>> lazyPossibleBiomes;

    @Override
    public void setPossibleBiomes(Set<Holder<Biome>> value) {
        this.lazyPossibleBiomes = Suppliers.memoize(() -> value);
    }
}
