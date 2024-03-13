package org.quiltmc.qsl.frozenblock.misc.datafixerupper.api;

public record ModUpgrade(String modId, @Nullable String key, int version, int newVersion) {

    public String getFullKey() {
        String fullKey = modId;
        String additionalKey = this.key();

        if (additionalKey != null) {
            fullKey += ('_' + additionalKey);
        }

        return fullKey;
    }
}