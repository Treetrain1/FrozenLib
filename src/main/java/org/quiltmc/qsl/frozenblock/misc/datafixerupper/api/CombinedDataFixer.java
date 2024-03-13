package org.quiltmc.qsl.frozenblock.misc.datafixerupper.api;

public interface CombinedDataFixer {
    <T> Dynamic<T> update(DSL.TypeReference type, Dynamic<T> input, List<ModUpgrade> versionUpgrades);

    default Schema getSchema(String modId, int version) {
        return getSchema(modId, null, version);
    }

    Schema getSchema(String modId, @Nullable String key, int version);
}