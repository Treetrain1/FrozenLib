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

package net.frozenblock.lib.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.frozenblock.lib.config.impl.network.ConfigSyncPacket;
import net.frozenblock.lib.event.api.PlayerJoinEvents;
import net.frozenblock.lib.item.impl.network.CooldownChangePacket;
import net.frozenblock.lib.item.impl.network.CooldownTickCountPacket;
import net.frozenblock.lib.item.impl.network.ForcedCooldownPacket;
import net.frozenblock.lib.screenshake.impl.network.EntityScreenShakePacket;
import net.frozenblock.lib.screenshake.impl.network.RemoveEntityScreenShakePacket;
import net.frozenblock.lib.screenshake.impl.network.RemoveScreenShakePacket;
import net.frozenblock.lib.screenshake.impl.network.ScreenShakePacket;
import net.frozenblock.lib.sound.api.networking.FadingDistanceSwitchingSoundPacket;
import net.frozenblock.lib.sound.api.networking.LocalPlayerSoundPacket;
import net.frozenblock.lib.sound.api.networking.LocalSoundPacket;
import net.frozenblock.lib.sound.api.networking.MovingFadingDistanceSwitchingRestrictionSoundPacket;
import net.frozenblock.lib.sound.api.networking.MovingRestrictionSoundPacket;
import net.frozenblock.lib.sound.api.networking.StartingMovingRestrictionSoundLoopPacket;
import net.frozenblock.lib.spotting_icons.impl.SpottingIconPacket;
import net.frozenblock.lib.spotting_icons.impl.SpottingIconRemovePacket;
import net.frozenblock.lib.wind.api.WindManager;
import net.frozenblock.lib.wind.impl.networking.WindDisturbancePacket;
import net.frozenblock.lib.wind.impl.networking.WindSyncPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.quiltmc.qsl.frozenblock.resource.loader.api.ResourceLoaderEvents;

public final class FrozenNetworking {
	private FrozenNetworking() {}

	public static void registerNetworking() {
		PayloadTypeRegistry<RegistryFriendlyByteBuf> registry = PayloadTypeRegistry.playS2C();

		PlayerJoinEvents.ON_PLAYER_ADDED_TO_LEVEL.register(((server, serverLevel, player) -> {
			WindManager windManager = WindManager.getWindManager(serverLevel);
			windManager.sendSyncToPlayer(windManager.createSyncPacket(), player);
		}));

		PlayerJoinEvents.ON_JOIN_SERVER.register((server, player) -> {
			ConfigSyncPacket.sendS2C(player);
		});

		ResourceLoaderEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, error) -> {
			if (error != null || server == null) return;
			for (ServerPlayer player : PlayerLookup.all(server)) {
				ConfigSyncPacket.sendS2C(player);
			}
		});

		PayloadTypeRegistry.playC2S().register(ConfigSyncPacket.PACKET_TYPE, ConfigSyncPacket.CODEC);
		registry.register(ConfigSyncPacket.PACKET_TYPE, ConfigSyncPacket.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(ConfigSyncPacket.PACKET_TYPE, ((packet, ctx) -> {
			if (ConfigSyncPacket.hasPermissionsToSendSync(ctx.player(), true))
				ConfigSyncPacket.receive(packet, ctx.player().server);
		}));


		registry.register(LocalPlayerSoundPacket.PACKET_TYPE, LocalPlayerSoundPacket.CODEC);
		registry.register(LocalSoundPacket.PACKET_TYPE, LocalSoundPacket.CODEC);
		registry.register(StartingMovingRestrictionSoundLoopPacket.PACKET_TYPE, StartingMovingRestrictionSoundLoopPacket.CODEC);
		registry.register(MovingRestrictionSoundPacket.PACKET_TYPE, MovingRestrictionSoundPacket.CODEC);

		registry.register(FadingDistanceSwitchingSoundPacket.PACKET_TYPE, FadingDistanceSwitchingSoundPacket.CODEC);
		registry.register(MovingFadingDistanceSwitchingRestrictionSoundPacket.PACKET_TYPE, MovingFadingDistanceSwitchingRestrictionSoundPacket.CODEC);
		registry.register(CooldownChangePacket.PACKET_TYPE, CooldownChangePacket.CODEC);
		registry.register(ForcedCooldownPacket.PACKET_TYPE, ForcedCooldownPacket.CODEC);
		registry.register(CooldownTickCountPacket.PACKET_TYPE, CooldownTickCountPacket.CODEC);
		registry.register(ScreenShakePacket.PACKET_TYPE, ScreenShakePacket.CODEC);
		registry.register(EntityScreenShakePacket.PACKET_TYPE, EntityScreenShakePacket.CODEC);
		registry.register(RemoveScreenShakePacket.PACKET_TYPE, RemoveScreenShakePacket.CODEC);
		registry.register(RemoveEntityScreenShakePacket.PACKET_TYPE, RemoveEntityScreenShakePacket.CODEC);
		registry.register(SpottingIconPacket.PACKET_TYPE, SpottingIconPacket.CODEC);
		registry.register(SpottingIconRemovePacket.PACKET_TYPE, SpottingIconRemovePacket.CODEC);
		registry.register(WindSyncPacket.PACKET_TYPE, WindSyncPacket.CODEC);
		registry.register(WindDisturbancePacket.PACKET_TYPE, WindDisturbancePacket.CODEC);
	}

	public static boolean isLocalPlayer(Player player) {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
			return false;

		return Minecraft.getInstance().isLocalPlayer(player.getGameProfile().getId());
	}

	public static boolean connectedToIntegratedServer() {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
			return false;
		Minecraft minecraft = Minecraft.getInstance();
		return minecraft.hasSingleplayerServer();
	}

	/**
	 * @return if the client is connected to any server
	 */
	public static boolean connectedToServer() {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
			return false;
		Minecraft minecraft = Minecraft.getInstance();
		ClientPacketListener listener = minecraft.getConnection();
		if (listener == null)
			return false;
		return listener.getConnection().isConnected();
	}

	/**
	 * @return if the current server is multiplayer (LAN/dedicated) or not (singleplayer)
	 */
	public static boolean isMultiplayer() {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
			return true;

		return !Minecraft.getInstance().isSingleplayer();
	}

}
