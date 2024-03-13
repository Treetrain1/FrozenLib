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
package org.quiltmc.qsl.frozenblock.misc.datafixerupper.impl;

import com.google.common.collect.Lists;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.functions.PointFreeRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.FabricDataFixerUpper;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.ModUpgrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

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
    private final Map<String, QuiltDataFixesInternals.DataFixerEntry> modDataFixers;

	protected CombinedDataFixerUpper(final Map<String, QuiltDataFixesInternals.DataFixerEntry> modDataFixers) {
        this.modDataFixers = modDataFixers;
    }

    @Override
    public <T> Dynamic<T> update(final DSL.TypeReference type, final Dynamic<T> input, final Map<QuiltDataFixesInternals.DataFixerEntry, Integer> versionUpgrades) {
		if (this.modDataFixers().isEmpty())
			return input;

        List<TypeRewriteRule> rules = Lists.newArrayList();
		Type<?> dataType = null;
		Type<?> newType = null;

        for (Map.Entry<QuiltDataFixesInternals.DataFixerEntry, Integer> upgrade : versionUpgrades.entrySet()) {
            QuiltDataFixesInternals.DataFixerEntry entry = upgrade.getKey();
			FabricDataFixerUpper dataFixer = entry.dataFixer();

			int version = upgrade.getValue();
			int newVersion = entry.currentVersion();
            if (version < newVersion) {
				Type<?> dataType1 = dataFixer.getType(type, version);
				Type<?> newType1 = dataFixer.getType(type, newVersion);

				if (dataType == null)
					dataType = dataType1;
				else if (dataType != dataType1)
					continue;

				if (newType == null)
					newType = newType1;
				else if (newType != newType1)
					continue;

                dataFixer.addRules(rules, version, newVersion);
            }
        }

		if (dataType == null || newType == null)
			return input;

        final DataResult<T> read = dataType.readAndWrite(input.getOps(), newType, TypeRewriteRule.seq(rules), OPTIMIZATION_RULE, input.getValue());
		final T result = read.resultOrPartial(LOGGER::error).orElse(input.getValue());
        return new Dynamic<>(input.getOps(), result);
    }

	@Override
	public Schema getSchema(String modId, int version) {
		return this.modDataFixers().get(modId).dataFixer().getSchema(version);
	}

	@Override
	public Schema getSchema(String modId, @Nullable String key, int version) {
		String finalKey = modId;
		if (key != null)
			finalKey += ('_' + key);

		return this.modDataFixers().get(finalKey).dataFixer().getSchema(version);
	}

	protected Map<String, QuiltDataFixesInternals.DataFixerEntry> modDataFixers() {
        return this.modDataFixers;
    }
}
