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

package net.frozenblock.lib;

import com.unascribed.lib39.tunnel.api.NetworkContext;
import io.netty.buffer.Unpooled;
import java.util.List;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.frozenblock.lib.entrypoint.api.FrozenMainEntrypoint;
import net.frozenblock.lib.event.api.PlayerJoinEvent;
import net.frozenblock.lib.feature.FrozenFeatures;
import net.frozenblock.lib.math.api.EasyNoiseSampler;
import net.frozenblock.lib.networking.api.BlockPhaseS2C;
import net.frozenblock.lib.networking.api.CooldownChangeS2C;
import net.frozenblock.lib.networking.api.FadingDistanceSoundS2C;
import net.frozenblock.lib.networking.api.FlybySoundS2C;
import net.frozenblock.lib.networking.api.FrozenPackets;
import net.frozenblock.lib.networking.api.LocalPlayerSoundS2C;
import net.frozenblock.lib.networking.api.LocalSoundS2C;
import net.frozenblock.lib.networking.api.MovingRestrictionSoundS2C;
import net.frozenblock.lib.networking.api.PlayerDamageS2C;
import net.frozenblock.lib.networking.api.ScreenShakeEntityS2C;
import net.frozenblock.lib.networking.api.ScreenShakeS2C;
import net.frozenblock.lib.networking.api.SmallWindS2CSync;
import net.frozenblock.lib.networking.api.SpottingIconRemoveS2C;
import net.frozenblock.lib.networking.api.SpottingIconS2C;
import net.frozenblock.lib.networking.api.SpottingIconSyncC2S;
import net.frozenblock.lib.networking.api.WindS2CSync;
import net.frozenblock.lib.registry.api.FrozenRegistry;
import net.frozenblock.lib.sound.api.FrozenSoundPackets;
import net.frozenblock.lib.sound.api.MovingLoopingFadingDistanceSoundEntityManager;
import net.frozenblock.lib.sound.api.MovingLoopingSoundEntityManager;
import net.frozenblock.lib.sound.api.predicate.SoundPredicate;
import net.frozenblock.lib.spotting_icons.api.SpottingIconPredicate;
import net.frozenblock.lib.spotting_icons.impl.EntitySpottingIconInterface;
import net.frozenblock.lib.wind.api.WindManager;
import net.frozenblock.lib.wind.command.OverrideWindCommand;
import net.frozenblock.lib.worldgen.surface.api.FrozenSurfaceRuleEntrypoint;
import net.frozenblock.lib.worldgen.surface.impl.BiomeTagConditionSource;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.impl.ServerFreezer;
import org.quiltmc.qsl.frozenblock.worldgen.surface_rule.impl.QuiltSurfaceRuleInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;

public final class FrozenMain implements ModInitializer {
	public static final String MOD_ID = "frozenlib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final NOPLogger LOGGER4 = NOPLogger.NOP_LOGGER;
	public static final NetworkContext NETWORKING = NetworkContext.forChannel(id("main"));
	public static boolean DEV_LOGGING = false;
	public static boolean areConfigsInit;

	/**
	 * Used for features that may be unstable and crash in public builds.
	 * <p>
	 * It's smart to use this for at least registries.
	 */
	public static boolean UNSTABLE_LOGGING = FabricLoader.getInstance().isDevelopmentEnvironment();

	public static final List<EntrypointContainer<FrozenSurfaceRuleEntrypoint>> SURFACE_RULE_ENTRYPOINTS = FabricLoader.getInstance().getEntrypointContainers("frozenlib:surfacerules", FrozenSurfaceRuleEntrypoint.class);

	@Override
	public void onInitialize() {
		createPackets();
		FrozenRegistry.initRegistry();
		ServerFreezer.onInitialize();
		QuiltSurfaceRuleInitializer.onInitialize();
		SoundPredicate.init();
		SpottingIconPredicate.init();
		FrozenFeatures.init();

		receiveSoundSyncPacket();

		Registry.register(Registry.CONDITION, FrozenMain.id("biome_tag_condition_source"), BiomeTagConditionSource.CODEC.codec());

		FabricLoader.getInstance().getEntrypointContainers("frozenlib:main", FrozenMainEntrypoint.class).forEach(entrypoint -> {
			try {
				FrozenMainEntrypoint mainPoint = entrypoint.getEntrypoint();
				mainPoint.init();
				if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
					mainPoint.initDevOnly();
				}
			} catch (Throwable ignored) {

			}
		});

		if (UNSTABLE_LOGGING) {
			CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> OverrideWindCommand.register(dispatcher));
		}

		ServerWorldEvents.LOAD.register((server, level) -> {
			if (server != null) {
				var seed = server.overworld().getSeed();
				EasyNoiseSampler.setSeed(seed);
				WindManager.setSeed(seed);
			}
		});

		ServerTickEvents.START_SERVER_TICK.register((server) -> WindManager.tick(server, server.overworld()));

		PlayerJoinEvent.ON_JOIN.register((server, player) ->
				FrozenPackets.windSync(server, WindManager.overrideWind).sendTo(player));

	}

	//IDENTIFIERS
	public static final ResourceLocation STARTING_RESTRICTION_LOOPING_SOUND_PACKET = id("starting_moving_restriction_looping_sound_packet");
	public static final ResourceLocation MOVING_RESTRICTION_LOOPING_FADING_DISTANCE_SOUND_PACKET = id("moving_restriction_looping_fading_distance_sound_packet");
	public static final ResourceLocation MOVING_FADING_DISTANCE_SOUND_PACKET = id("moving_fading_distance_sound_packet");
	public static final ResourceLocation REQUEST_LOOPING_SOUND_SYNC_PACKET = id("request_looping_sound_sync_packet");

	private static void createPackets() {
		NETWORKING.register(LocalSoundS2C.class);
		NETWORKING.register(FlybySoundS2C.class);
		NETWORKING.register(MovingRestrictionSoundS2C.class);
		NETWORKING.register(FadingDistanceSoundS2C.class);
		NETWORKING.register(LocalPlayerSoundS2C.class);
		NETWORKING.register(WindS2CSync.class);
		NETWORKING.register(SmallWindS2CSync.class);
		NETWORKING.register(BlockPhaseS2C.class);
		NETWORKING.register(ScreenShakeS2C.class);
		NETWORKING.register(ScreenShakeEntityS2C.class);
		NETWORKING.register(SpottingIconS2C.class);
		NETWORKING.register(SpottingIconRemoveS2C.class);
		NETWORKING.register(SpottingIconSyncC2S.class);
		NETWORKING.register(CooldownChangeS2C.class);
		NETWORKING.register(PlayerDamageS2C.class);
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

	public static String string(String path) {
		return id(path).toString();
	}

	public static void log(String string, boolean should) {
		if (should) {
			LOGGER.info(string);
		}
	}

	public static void warn(String string, boolean should) {
		if (should) {
			LOGGER.warn(string);
		}
	}

	public static void error(String string, boolean should) {
		if (should) {
			LOGGER.error(string);
		}
	}

	private static void receiveSoundSyncPacket() {
		ServerPlayNetworking.registerGlobalReceiver(FrozenMain.REQUEST_LOOPING_SOUND_SYNC_PACKET, (ctx, player, handler, byteBuf, responseSender) -> {
			int id = byteBuf.readVarInt();
			Level dimension = ctx.getLevel(byteBuf.readResourceKey(Registry.DIMENSION_REGISTRY));
			ctx.execute(() -> {
				if (dimension != null) {
					Entity entity = dimension.getEntity(id);
					if (entity instanceof LivingEntity livingEntity) {
						for (MovingLoopingSoundEntityManager.SoundLoopData nbt : livingEntity.getSounds().getSounds()) {
							FrozenPackets.movingRestrictionSoundS2C(entity, Registry.SOUND_EVENT.get(nbt.getSoundEventID()), SoundSource.valueOf(SoundSource.class, nbt.getOrdinal()), nbt.volume, nbt.pitch, nbt.restrictionID, true).sendTo(player);
						}
						for (MovingLoopingFadingDistanceSoundEntityManager.FadingDistanceSoundLoopNBT nbt : livingEntity.getFadingDistanceSounds().getSounds()) {
							FrozenSoundPackets.createMovingRestrictionLoopingFadingDistanceSound(player, entity, Registry.SOUND_EVENT.get(nbt.getSoundEventID()), Registry.SOUND_EVENT.get(nbt.getSound2EventID()), SoundSource.valueOf(SoundSource.class, nbt.getOrdinal()), nbt.volume, nbt.pitch, nbt.restrictionID, nbt.fadeDist, nbt.maxDist);
						}
					}
				}
			});
		});
	}
}
