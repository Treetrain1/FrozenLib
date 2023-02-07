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

package net.frozenblock.lib.screenshake.api;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.frozenblock.lib.FrozenMain;
import net.frozenblock.lib.networking.api.ScreenShakeEntityS2C;
import net.frozenblock.lib.networking.api.ScreenShakeS2C;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public final class ScreenShakePackets {
	private ScreenShakePackets() {
		throw new UnsupportedOperationException("ScreenShakePackets contains only static declarations.");
	}

	public static void createScreenShakePacket(Level level, float intensity, double x, double y, double z, float maxDistance) {
		createScreenShakePacket(level, intensity, 20, 5, x, y, z, maxDistance);
	}

	public static void createScreenShakePacket(Level level, float intensity, int duration, double x, double y, double z, float maxDistance) {
		createScreenShakePacket(level, intensity, duration, 1, x, y, z, maxDistance);
	}

	public static void createScreenShakePacket(Level level, float intensity, int duration, int falloffStart, double x, double y, double z, float maxDistance) {
		if (!level.isClientSide) {
			var packet = new ScreenShakeS2C(intensity, duration, falloffStart, x, y, z, maxDistance);
			for (ServerPlayer player : PlayerLookup.world((ServerLevel) level)) {
				packet.sendTo(player);
			}
		}
	}

	//With Entity
	public static void createScreenShakePacketEntity(Entity entity, Level level, float intensity, float maxDistance) {
		createScreenShakePacketEntity(entity, level, intensity, 5, 1, maxDistance);
	}

	public static void createScreenShakePacketEntity(Entity entity, Level level, float intensity, int duration, float maxDistance) {
		createScreenShakePacketEntity(entity, level, intensity, duration, 1, maxDistance);
	}

	public static void createScreenShakePacketEntity(Entity entity, Level level, float intensity, int duration, int falloffStart, float maxDistance) {
		if (!level.isClientSide) {
			var packet = new ScreenShakeEntityS2C(entity.getId(), intensity, duration, falloffStart, maxDistance);
			for (ServerPlayer player : PlayerLookup.world((ServerLevel) level)) {
				packet.sendTo(player);
			}
		}
	}
}
