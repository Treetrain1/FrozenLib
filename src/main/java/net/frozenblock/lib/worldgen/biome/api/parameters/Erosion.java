/*
 * Copyright 2023 The Quilt Project
 * Copyright 2023 FrozenBlock
 * Modified to work on Fabric
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2022 The Quilt Project
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2021-2022 The Quilt Project
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2023 The Quilt Project
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2021-2023 The Quilt Project
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2022-2023 The Quilt Project
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2022 QuiltMC
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2021-2022 QuiltMC
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2023 QuiltMC
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2021-2023 QuiltMC
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2022-2023 QuiltMC
 * ;;match_from: \/\/\/ Q[Uu][Ii][Ll][Tt]
 */

package net.frozenblock.lib.worldgen.biome.api.parameters;

import net.minecraft.world.level.biome.Climate;

public final class Erosion {
    public static final Climate.Parameter EROSION_0 = OverworldBiomeBuilderParameters.EROSIONS[0];
    public static final Climate.Parameter EROSION_1 = OverworldBiomeBuilderParameters.EROSIONS[1];
    public static final Climate.Parameter EROSION_2 = OverworldBiomeBuilderParameters.EROSIONS[2];
    public static final Climate.Parameter EROSION_3 = OverworldBiomeBuilderParameters.EROSIONS[3];
    public static final Climate.Parameter EROSION_4 = OverworldBiomeBuilderParameters.EROSIONS[4];
    public static final Climate.Parameter EROSION_5 = OverworldBiomeBuilderParameters.EROSIONS[5];
    public static final Climate.Parameter EROSION_6 = OverworldBiomeBuilderParameters.EROSIONS[6];
    public static final Climate.Parameter FULL_RANGE = OverworldBiomeBuilderParameters.FULL_RANGE;
}
