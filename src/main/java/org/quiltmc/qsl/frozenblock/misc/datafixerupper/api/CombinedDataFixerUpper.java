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

// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.
package org.quiltmc.qsl.frozenblock.misc.datafixerupper.api;

import com.google.common.collect.Lists;
import com.mojang.datafixers.functions.PointFreeRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/*
 * Optimizing functions
 *   Cunha, A., & Pinto, J. S. (2005). Point-free program transformation
 *   Lämmel, R., Visser, E., & Visser, J. (2002). The essence of strategic programming
 *
 * How to handle recursive types
 *   Cunha, A., & Pacheco, H. (2011). Algebraic specialization of generic functions for recursive types
 *   Yakushev, A. R., Holdermans, S., Löh, A., & Jeuring, J. (2009, August). Generic programming with fixed points for mutually recursive datatypes
 *   Magalhães, J. P., & Löh, A. (2012). A formal comparison of approaches to datatype-generic programming
 *
 * Optics
 *   Pickering, M., Gibbons, J., & Wu, N. (2017). Profunctor Optics: Modular Data Accessors
 *   Pacheco, H., & Cunha, A. (2010, June). Generic point-free lenses
 *
 * Tying it together
 *   Cunha, A., Oliveira, J. N., & Visser, J. (2006, August). Type-safe two-level data transformation
 *   Cunha, A., & Visser, J. (2011). Transformation of structure-shy programs with application to XPath queries and strategic functions
 *   Pacheco, H., & Cunha, A. (2011, January). Calculating with lenses: optimising bidirectional transformations
 */
public class CombinedDataFixerUpper implements CombinedDataFixer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CombinedDataFixerUpper.class);

    protected static final PointFreeRule OPTIMIZATION_RULE = DataFixUtils.make(() -> PointFreeRule.everywhere(
        // Top-down: these rules produce new compositions that also need to be rewritten
        PointFreeRule.seq(
            // Applying CataFuseDifferent before CataFuseSame would prevent some merges from happening, but not the other way around
            PointFreeRule.CataFuseSame.INSTANCE,
            PointFreeRule.CataFuseDifferent.INSTANCE,
            // Apply all of these together exhaustively because each change can allow another rule to apply
            PointFreeRule.CompRewrite.together(
                // Merge functions applying to identical optics, must run before merging nested applied functions
                PointFreeRule.LensComp.INSTANCE,
                PointFreeRule.SortProj.INSTANCE,
                PointFreeRule.SortInj.INSTANCE
            )
        ),
        // Bottom-up: ensure we nest the full tree in a single pass
        PointFreeRule.AppNest.INSTANCE
    ));

    // map of datafixer key -> datafixer
    private final Map<String, FabricDataFixerUpper> modDataFixers;
    private final Long2ObjectMap<TypeRewriteRule> rules = Long2ObjectMaps.synchronize(new Long2ObjectOpenHashMap<>());

    protected CombinedDataFixerUpper(final Map<String, FabricDataFixerUpper> modDataFixers) {
        this.modDataFixers = modDataFixers;
    }

    @Override
    public <T> Dynamic<T> update(final DSL.TypeReference type, final Dynamic<T> input, final List<ModUpgrade> versionUpgrades) {
        Dynamic<T> output = input;
        List<TypeRewriteRule> rules = Lists.newArrayList();

        for (ModUpgrade upgrade : versionUpgrades) {
            String key = upgrade.getFullKey();
            int version = upgrade.version();
            int newVersion = upgrade.newVersion();
            if (version < newVersion) {
                FabricDataFixerUpper dataFixer = this.modDataFixers().get(key);

                final Type<?> dataType = dataFixer.getType(type, version);
                final DataResult<T> read = dataType.readAndWrite(input.getOps(), getType(type, newVersion), getRule(version, newVersion), OPTIMIZATION_RULE, input.getValue());
                final T result = read.resultOrPartial(LOGGER::error).orElse(input.getValue());
                output = new Dynamic<>(input.getOps(), result);
            }
        }

        final DataResult<T> read = dataType.readAndWrite(input.getOps(), getType(type))
        return output;
    }

    @Override
    public Schema getSchema(final int key) {
        return schemas.get(getLowestSchemaSameVersion(schemas, key));
    }

    protected Type<?> getType(final DSL.TypeReference type, final int version) {
        return getSchema(DataFixUtils.makeKey(version)).getTypeRaw(type);
    }

    protected static int getLowestSchemaSameVersion(final Int2ObjectSortedMap<Schema> schemas, final int versionKey) {
        if (versionKey < schemas.firstIntKey()) {
            // can't have a data type before anything else
            return schemas.firstIntKey();
        }
        return schemas.subMap(0, versionKey + 1).lastIntKey();
    }

    protected TypeRewriteRule getRule() {
        if (version >= newVersion) {
            return TypeRewriteRule.nop();
        }

        final long key = (long) version << 32 | newVersion;
        return rules.computeIfAbsent(key, k -> {
            final int expandedVersion = getLowestFixSameVersion(DataFixUtils.makeKey(version));

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

            return TypeRewriteRule.seq(rules);
        });
    }

    protected Map<String, DataFixerUpper> modDataFixers() {
        return this.modDataFixers;
    }
}