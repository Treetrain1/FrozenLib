package net.frozenblock.lib.config.api.instance.xjs;

import net.frozenblock.lib.config.api.entry.TypedEntry;
import net.frozenblock.lib.config.api.entry.TypedEntryType;

@FunctionalInterface
public interface CustomTypedEntryFunction {
    static CustomTypedEntryFunction DEFAULT = new CustomTypedEntryFunction((type, value) -> TypedEntry.create(type, value));

    <T> TypedEntry<T> createTypedEntry(TypedEntryType<T> type, T value);
}