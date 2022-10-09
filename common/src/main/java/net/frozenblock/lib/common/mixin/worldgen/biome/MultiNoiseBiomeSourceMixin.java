package net.frozenblock.lib.common.mixin.worldgen.biome;

import net.frozenblock.lib.common.worldgen.biome.api.FrozenMultiNoiseBiomeSourceAccess;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MultiNoiseBiomeSource.class)
public class MultiNoiseBiomeSourceMixin implements FrozenMultiNoiseBiomeSourceAccess {

    @Unique
    private boolean modifyBiomeEntries = true;

    @Override
    public void frozenLib_setModifyBiomeEntries(boolean modifyBiomeEntries) {
        this.modifyBiomeEntries = modifyBiomeEntries;
    }

    @Override
    public boolean frozenLib_shouldModifyBiomeEntries() {
        return this.modifyBiomeEntries;
    }
}
