/*
 * Copyright 2023 FrozenBlock
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

package net.frozenblock.lib.advancement.api;

import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AdvancementContext {

	ResourceLocation key();

	Optional<ResourceLocation> parent();

	Optional<DisplayInfo> display();

	AdvancementRewards rewards();

	Map<String, Criterion<?>> criteria();

	AdvancementRequirements requirements();

	boolean sendsTelemetryEvent();

	Optional<Component> name();

	Optional<Component> title();

	Optional<Component> description();

	Optional<ItemStack> icon();

	Optional<ResourceLocation> background();

	Optional<FrameType> frame();

	Optional<Boolean> showToast();

	Optional<Boolean> announceChat();

	Optional<Boolean> hidden();

	Optional<Float> x();

	Optional<Float> y();

	void setParent(Optional<ResourceLocation> parentLocation);

	void addCriteria(String key, Criterion<?> criteria);

	void addRequirements(AdvancementRequirements requirements);

	void addLootTables(List<ResourceLocation> lootTables);

	void addRecipes(Collection<ResourceLocation> recipes);

	void setExperience(int experience);

	void setTelemetry(boolean telemetry);

	void setTitle(Component title);

	void setDescription(Component description);

	void setIcon(ItemStack icon);

	void setBackground(@Nullable ResourceLocation background);

	void setFrame(FrameType frame);

	void setShowsToast(boolean showToast);

	void setAnnounceChat(boolean announceChat);

	void setHidden(boolean hidden);

	void setX(float x);

	void setY(float y);
}
