/*
 * Copyright 2023 FrozenBlock
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
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2022 FrozenBlock
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2021-2022 FrozenBlock
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2023 FrozenBlock
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2021-2023 FrozenBlock
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2022-2023 FrozenBlock
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2022 QuiltMC
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2021-2022 QuiltMC
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2023 QuiltMC
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2021-2023 QuiltMC
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2022-2023 QuiltMC
 * ;;match_from: \/\/\/ Q[Uu][Ii][Ll][Tt]
 */

package net.frozenblock.lib.worldgen.biome.api.parameters;

import net.minecraft.world.level.biome.Climate;

public final class Temperature {
    public static final Climate.Parameter ICY = OverworldBiomeBuilderParameters.TEMPERATURES[0];
    public static final Climate.Parameter COOL = OverworldBiomeBuilderParameters.TEMPERATURES[1];
    public static final Climate.Parameter NEUTRAL = OverworldBiomeBuilderParameters.TEMPERATURES[2];
    public static final Climate.Parameter WARM = OverworldBiomeBuilderParameters.TEMPERATURES[3];
    public static final Climate.Parameter HOT = OverworldBiomeBuilderParameters.TEMPERATURES[4];

	public static final Climate.Parameter ONE = OverworldBiomeBuilderParameters.TEMPERATURES[0];
	public static final Climate.Parameter TWO = OverworldBiomeBuilderParameters.TEMPERATURES[1];
	public static final Climate.Parameter THREE = OverworldBiomeBuilderParameters.TEMPERATURES[2];
	public static final Climate.Parameter FOUR = OverworldBiomeBuilderParameters.TEMPERATURES[3];
	public static final Climate.Parameter FIVE = OverworldBiomeBuilderParameters.TEMPERATURES[4];

    public static final Climate.Parameter FULL_RANGE = OverworldBiomeBuilderParameters.FULL_RANGE;
}
