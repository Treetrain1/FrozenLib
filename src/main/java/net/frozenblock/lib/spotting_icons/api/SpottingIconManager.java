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

package net.frozenblock.lib.spotting_icons.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.Unpooled;
import java.util.Objects;
import java.util.Optional;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.frozenblock.lib.FrozenSharedConstants;
import net.frozenblock.lib.networking.FrozenNetworking;
import net.frozenblock.lib.spotting_icons.impl.SpottingIconPacket;
import net.frozenblock.lib.spotting_icons.impl.SpottingIconRemovePacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;

public class SpottingIconManager {
	public Entity entity;
	public int ticksToCheck;
	public SpottingIcon icon;
	public boolean clientHasIconResource;

	public SpottingIconManager(Entity entity) {
		this.entity = entity;
	}

	public void tick() {
		if (this.ticksToCheck > 0) {
			--this.ticksToCheck;
		} else {
			this.ticksToCheck = 20;
			if (this.icon != null) {
				if (this.entity.level().isClientSide) {
					this.clientHasIconResource = ClientSpottingIconMethods.hasTexture(this.icon.texture());
				}
				if (!SpottingIconPredicate.getPredicate(this.icon.restrictionID).test(this.entity)) {
					this.removeIcon();
				}
			}
		}
	}

	public void setIcon(ResourceLocation texture, float startFade, float endFade, ResourceLocation restrictionID) {
		this.icon = new SpottingIcon(texture, startFade, endFade, restrictionID);
		if (!this.entity.level().isClientSide) {
			FabricPacket packet = new SpottingIconPacket(this.entity.getId(), texture, startFade, endFade, restrictionID);
			for (ServerPlayer player : PlayerLookup.tracking(this.entity)) {
				ServerPlayNetworking.send(player, packet);
			}
		} else {
			this.clientHasIconResource = ClientSpottingIconMethods.hasTexture(this.icon.texture());
		}
		SpottingIconPredicate.getPredicate(this.icon.restrictionID).onAdded(this.entity);
	}

	public void removeIcon() {
		SpottingIconPredicate.getPredicate(this.icon.restrictionID).onRemoved(this.entity);
		this.icon = null;
		if (!this.entity.level().isClientSide) {
			FabricPacket packet = new SpottingIconRemovePacket(this.entity.getId());
			for (ServerPlayer player : PlayerLookup.tracking(this.entity)) {
				ServerPlayNetworking.send(player, packet);
			}
		}
	}

	public void sendIconPacket(ServerPlayer player) {
		if (this.icon != null) {
			FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
			byteBuf.writeVarInt(this.entity.getId());
			byteBuf.writeResourceLocation(this.icon.texture);
			byteBuf.writeFloat(this.icon.startFadeDist);
			byteBuf.writeFloat(this.icon.endFadeDist);
			byteBuf.writeResourceLocation(this.icon.restrictionID);
			ServerPlayNetworking.send(
				player,
				new SpottingIconPacket(
					this.entity.getId(),
					this.icon.texture,
					this.icon.startFadeDist(),
					this.icon.endFadeDist(),
					this.icon.restrictionID()
				)
			);
		}
	}

	public void load(CompoundTag nbt) {
		this.ticksToCheck = nbt.getInt("frozenSpottingIconTicksToCheck");
		if (nbt.contains("frozenSpottingIcons")) {
			this.icon = null;
			DataResult<SpottingIcon> var10000 = SpottingIcon.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, nbt.getCompound("frozenSpottingIcons")));
			Logger var10001 = FrozenSharedConstants.LOGGER4;
			Objects.requireNonNull(var10001);
			Optional<SpottingIcon> icon = var10000.resultOrPartial(var10001::error);
			icon.ifPresent(spottingIcon -> this.icon = spottingIcon);
		}
	}

	public void save(CompoundTag nbt) {
		nbt.putInt("frozenSpottingIconTicksToCheck", this.ticksToCheck);
		if (this.icon != null) {
			DataResult<Tag> var10000 = SpottingIcon.CODEC.encodeStart(NbtOps.INSTANCE, this.icon);
			Logger var10001 = FrozenSharedConstants.LOGGER4;
			Objects.requireNonNull(var10001);
			var10000.resultOrPartial(var10001::error).ifPresent((iconNBT) -> nbt.put("frozenSpottingIcons", iconNBT));
		} else if (nbt.contains("frozenSpottingIcons")) {
			nbt.remove("frozenSpottingIcons");
		}
	}

	public record SpottingIcon(ResourceLocation texture, float startFadeDist, float endFadeDist, ResourceLocation restrictionID) {
			public static final Codec<SpottingIcon> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
				ResourceLocation.CODEC.fieldOf("texture").forGetter(SpottingIcon::texture),
				Codec.FLOAT.fieldOf("startFadeDist").forGetter(SpottingIcon::startFadeDist),
				Codec.FLOAT.fieldOf("endFadeDist").forGetter(SpottingIcon::endFadeDist),
				ResourceLocation.CODEC.fieldOf("restrictionID").forGetter(SpottingIcon::restrictionID)
			).apply(instance, SpottingIcon::new));
	}
}
