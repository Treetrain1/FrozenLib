/*
 * Copyright 2024 FrozenBlock
 * This file is part of FrozenLib.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, see <https://www.gnu.org/licenses/>.
 */

package org.quiltmc.qsl.frozenblock.misc.datafixerupper.api;

public class FabricDataFixerUpper extends DataFixerUpper {
    private final Int2ObjectSortedMap<Schema> schemas;
    private final List<DataFix> globalList;
    private final IntSortedSet fixerVersions;
    private final Long2ObjectMap<List<TypeRewriteRule>> rules = Long2ObjectMaps.synchronize(new Long2ObjectOpenHashMap<>());

    public FabricDataFixerUpper(final Int2ObjectSortedMap<Schema> schemas, final List<DataFix> globalList, final IntSortedSet fixerVersions) {
        super(schemas, globalList, fixerVersions);
        this.schemas = schemas;
        this.globalList = globalList;
        this.fixerVersions = fixerVersions;
    }

    @Override
    public Type<?> getType(final DSL.TypeReference type, final int version) {
        return super.getType(type, version);
    }

    public static int lowestSchemaSameVersion(final Int2ObjectSortedMap<Schema> schemas, final int versionKey) {
        return getLowestSchemaSameVersion(schemas, versionKey);
    }

    public List<TypeRewriteRule> addRules(List<TypeRewriteRule> list, final int version, final int newVersion) {
        if (version >= newVersion) {
            return list;
        }

        final long key = (long) version << 32 | newVersion;
        list.addAll(this.rules.computeIfAbsent(key, k -> {
            final int expandedVersion = lowestSchemaSameVersion(DataFixUtils.makeKey(version));

            final List<TypeRewriteRule> rules = Lists.newArrayList();
            for (final DataFix fix : globalList) {
                final int expandedFixVersion = fix.getVersionKey();
                final int fixVersion = DataFixUtils.getVersion(expandedFixVersion);
                if (expandedFixVersion > expandedVersion && fixVersion <= newVersion) {
                    final TypeRewriteRule fixRule = fix.getRule();
                    if (fixRule == TypeRewriteRule.nop()) {
                        continue;
                    }
                    rules.add(fixRule);
                }
            }

            return rules;
        }));
        return list;
    }
}