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

package net.frozenblock.lib.sound.api;

import com.mojang.math.Vector3d;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.frozenblock.lib.FrozenMain;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class FrozenSoundPackets {
	private FrozenSoundPackets() {
		throw new UnsupportedOperationException("FrozenSoundPackets contains only static declarations.");
	}

    public static void createMovingRestrictionLoopingFadingDistanceSound(Level world, Entity entity, SoundEvent sound, SoundEvent sound2, SoundSource category, float volume, float pitch, ResourceLocation id, float fadeDist, float maxDist) {
        if (!world.isClientSide) {
            FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
            byteBuf.writeVarInt(entity.getId());
            byteBuf.writeId(Registry.SOUND_EVENT, sound);
            byteBuf.writeId(Registry.SOUND_EVENT, sound2);
            byteBuf.writeEnum(category);
            byteBuf.writeFloat(volume);
            byteBuf.writeFloat(pitch);
            byteBuf.writeFloat(fadeDist);
            byteBuf.writeFloat(maxDist);
            byteBuf.writeResourceLocation(id);
            for (ServerPlayer player : PlayerLookup.tracking((ServerLevel) world, entity.blockPosition())) {
                ServerPlayNetworking.send(player, FrozenMain.MOVING_RESTRICTION_LOOPING_FADING_DISTANCE_SOUND_PACKET, byteBuf);
            }
            if (entity instanceof LivingEntity living) {
                living.addSound(Registry.SOUND_EVENT.getKey(sound), category, volume, pitch, id);
            }
        }
    }

    public static void createMovingRestrictionLoopingFadingDistanceSound(ServerPlayer player, Entity entity, SoundEvent sound, SoundEvent sound2, SoundSource category, float volume, float pitch, ResourceLocation id, float fadeDist, float maxDist) {
        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        byteBuf.writeVarInt(entity.getId());
        byteBuf.writeId(Registry.SOUND_EVENT, sound);
        byteBuf.writeId(Registry.SOUND_EVENT, sound2);
        byteBuf.writeEnum(category);
        byteBuf.writeFloat(volume);
        byteBuf.writeFloat(pitch);
        byteBuf.writeFloat(fadeDist);
        byteBuf.writeFloat(maxDist);
        byteBuf.writeResourceLocation(id);
        ServerPlayNetworking.send(player, FrozenMain.MOVING_RESTRICTION_LOOPING_FADING_DISTANCE_SOUND_PACKET, byteBuf);
    }

    public static void createMovingRestrictionFadingDistanceSound(ServerPlayer player, Entity entity, SoundEvent sound, SoundEvent sound2, SoundSource category, float volume, float pitch, ResourceLocation id, float fadeDist, float maxDist) {
        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        byteBuf.writeVarInt(entity.getId());
        byteBuf.writeId(Registry.SOUND_EVENT, sound);
        byteBuf.writeId(Registry.SOUND_EVENT, sound2);
        byteBuf.writeEnum(category);
        byteBuf.writeFloat(volume);
        byteBuf.writeFloat(pitch);
        byteBuf.writeFloat(fadeDist);
        byteBuf.writeFloat(maxDist);
        byteBuf.writeResourceLocation(id);
        ServerPlayNetworking.send(player, FrozenMain.MOVING_FADING_DISTANCE_SOUND_PACKET, byteBuf);
    }

    public static void createStartingMovingRestrictionLoopingSound(Level world, Entity entity, SoundEvent startingSound, SoundEvent sound, SoundSource category, float volume, float pitch, ResourceLocation id) {
        if (!world.isClientSide) {
            FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
            byteBuf.writeVarInt(entity.getId());
            byteBuf.writeId(Registry.SOUND_EVENT, startingSound);
            byteBuf.writeId(Registry.SOUND_EVENT, sound);
            byteBuf.writeEnum(category);
            byteBuf.writeFloat(volume);
            byteBuf.writeFloat(pitch);
            byteBuf.writeResourceLocation(id);
            for (ServerPlayer player : PlayerLookup.tracking((ServerLevel) world, entity.blockPosition())) {
                ServerPlayNetworking.send(player, FrozenMain.STARTING_RESTRICTION_LOOPING_SOUND_PACKET, byteBuf);
            }
            if (entity instanceof LivingEntity living) {
                living.addSound(Registry.SOUND_EVENT.getKey(sound), category, volume, pitch, id);
            }
        }
    }

    public static void createStartingMovingRestrictionLoopingSound(ServerPlayer player, Entity entity, SoundEvent startingSound, SoundEvent sound, SoundSource category, float volume, float pitch, ResourceLocation id) {
        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        byteBuf.writeVarInt(entity.getId());
        byteBuf.writeId(Registry.SOUND_EVENT, startingSound);
        byteBuf.writeId(Registry.SOUND_EVENT, sound);
        byteBuf.writeEnum(category);
        byteBuf.writeFloat(volume);
        byteBuf.writeFloat(pitch);
        byteBuf.writeResourceLocation(id);
        ServerPlayNetworking.send(player, FrozenMain.STARTING_RESTRICTION_LOOPING_SOUND_PACKET, byteBuf);
    }

}
