package net.frozenblock.lib.networking.api;

import net.minecraft.core.IdMap;

public record RegistryIdHolder<T>(IdMap<T> idMap, T object) {
}
