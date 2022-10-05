package net.frozenblock.lib.fabric.worldgen.biome.api;

public interface FrozenBiomeSourceAccess {

    boolean frozenLib_shouldModifyBiomeEntries();

    void frozenLib_setModifyBiomeEntries(boolean modifyBiomeEntries);
}
