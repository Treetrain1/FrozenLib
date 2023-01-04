package net.frozenblock.lib.networking.api;

import com.mojang.math.Vector3d;
import net.frozenblock.lib.wind.api.WindManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public final class FrozenPackets {

	private FrozenPackets() {
	}

	@Nullable
	public static LocalSoundS2C localSoundS2C(Level level, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch, boolean distanceDelay) {
		if (!level.isClientSide) {
			return new LocalSoundS2C(
					pos.getX(),
					pos.getY(),
					pos.getZ(),
					sound,
					source,
					volume,
					pitch,
					distanceDelay
			);
		}
		return null;
	}

	@Nullable
	public static LocalSoundS2C localSoundS2C(Level level, double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, boolean distanceDelay) {
		if (!level.isClientSide) {
			return new LocalSoundS2C(
					x,
					y,
					z,
					sound,
					source,
					volume,
					pitch,
					distanceDelay
			);
		}
		return null;
	}

	@Nullable
	public static FlybySoundS2C flyBySoundS2C(Level level, Entity entity, SoundEvent sound, SoundSource category, float volume, float pitch) {
		if (!level.isClientSide) {
			return new FlybySoundS2C(
					entity.getId(),
					sound,
					category,
					volume,
					pitch
			);
		}
		return null;
	}

	@Nullable
	public static MovingRestrictionSoundS2C movingRestrictionSoundS2C(Level level, Entity entity, SoundEvent sound, SoundSource category, float volume, float pitch, ResourceLocation id, boolean looping) {
		if (!level.isClientSide) {
			return movingRestrictionSoundS2C(entity, sound, category, volume, pitch, id, looping);
		}
		return null;
	}

	public static MovingRestrictionSoundS2C movingRestrictionSoundS2C(Entity entity, SoundEvent sound, SoundSource category, float volume, float pitch, ResourceLocation id, boolean looping) {
		return new MovingRestrictionSoundS2C(
				entity.getId(),
				sound,
				category,
				volume,
				pitch,
				id,
				looping
		);
	}

	@Nullable
	public static FadingDistanceSoundS2C fadingDistanceSoundS2C(Level level, Vector3d pos, SoundEvent sound, SoundEvent sound2, SoundSource category, float volume, float pitch, ResourceLocation id, float fadeDist, float maxDist) {
		if (!level.isClientSide)
			return new FadingDistanceSoundS2C(
					pos.x,
					pos.y,
					pos.z,
					sound,
					sound2,
					category,
					volume,
					pitch,
					fadeDist,
					maxDist,
					id
			);
		return null;
	}

	public static LocalPlayerSoundS2C localPlayerSoundS2C(SoundEvent sound, float volume, float pitch) {
		return new LocalPlayerSoundS2C(sound, volume, pitch);
	}

	public static WindS2CSync windSync(MinecraftServer server, boolean overrideWind) {
		return new WindS2CSync(
				WindManager.time,
				WindManager.cloudX,
				WindManager.cloudY,
				WindManager.cloudZ,
				server.overworld().getSeed(),
				overrideWind,
				WindManager.commandWind.x(),
				WindManager.commandWind.y(),
				WindManager.commandWind.z()
		);
	}

	public static SmallWindS2CSync smallWindSync() {
		return new SmallWindS2CSync(
				WindManager.time,
				WindManager.cloudX,
				WindManager.cloudY,
				WindManager.cloudZ
		);
	}
}
