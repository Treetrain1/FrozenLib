package net.frozenblock.lib.networking.api;

import com.unascribed.lib39.tunnel.api.NetworkContext;
import com.unascribed.lib39.tunnel.api.S2CMessage;
import com.unascribed.lib39.tunnel.api.annotation.field.MarshalledAs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.frozenblock.lib.FrozenMain;
import net.frozenblock.lib.sound.api.FlyBySoundHub;
import net.frozenblock.lib.sound.api.instances.RestrictedMovingSound;
import net.frozenblock.lib.sound.api.instances.RestrictedMovingSoundLoop;
import net.frozenblock.lib.sound.api.predicate.SoundPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class MovingRestrictionSoundS2C extends S2CMessage {

	@MarshalledAs("varint")
	public int entityId;
	@MarshalledAs("net.frozenblock.lib.networking.api.marshaller.SoundEventMarshaller")
	public RegistryIdHolder<SoundEvent> sound;
	public SoundSource category;
	@MarshalledAs("float")
	public float volume;
	@MarshalledAs("float")
	public float pitch;
	@MarshalledAs("string")
	public String namespace;
	@MarshalledAs("string")
	public String path;
	public boolean looping;

	public MovingRestrictionSoundS2C(NetworkContext ctx) {
		super(ctx);
	}

	public MovingRestrictionSoundS2C(int entityId, SoundEvent sound, SoundSource category, float volume, float pitch, ResourceLocation resourceLocation, boolean looping) {
		super(FrozenMain.NETWORKING);
		this.entityId = entityId;
		this.sound = new RegistryIdHolder<>(Registry.SOUND_EVENT, sound);
		this.category = category;
		this.volume = volume;
		this.pitch = pitch;
		this.namespace = resourceLocation.getNamespace();
		this.path = resourceLocation.getPath();
		this.looping = looping;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void handle(Minecraft client, LocalPlayer player) {
		Entity entity = player.level.getEntity(this.entityId);
		var predicateLocation = new ResourceLocation(this.namespace, this.path);
		SoundPredicate.LoopPredicate<Entity> predicate = SoundPredicate.getPredicate(predicateLocation);
		SoundInstance soundInstance;
		assert entity != null;
		if (this.looping) {
			soundInstance = new RestrictedMovingSoundLoop<>(entity, this.sound.object(), this.category, this.volume, this.pitch, predicate);
		} else {
			soundInstance = new RestrictedMovingSound<>(entity, this.sound.object(), this.category, this.volume, this.pitch, predicate);
		}
		client.getSoundManager().play(soundInstance);
		FrozenMain.log("Moving restriction sound packet received", FrozenMain.UNSTABLE_LOGGING);
	}

	public void send(Level level) {
		Entity entity = level.getEntity(this.entityId);
		var sound = this.sound.object();
		assert entity != null;
		for (ServerPlayer player : PlayerLookup.tracking((ServerLevel) level, entity.blockPosition()))
			this.sendTo(player);
		if (this.looping && entity instanceof LivingEntity living)
			living.addSound(Registry.SOUND_EVENT.getKey(sound), this.category, this.volume, this.pitch, new ResourceLocation(this.namespace, this.path));
	}

	public void send(ServerPlayer player) {
		ServerLevel level = player.getLevel();
		Entity entity = level.getEntity(this.entityId);
		var sound = this.sound.object();
		assert entity != null;
		this.sendTo(player);
		if (entity instanceof LivingEntity living) {
			living.addSound(Registry.SOUND_EVENT.getKey(sound), category, volume, pitch, new ResourceLocation(this.namespace, this.path));
		}
	}
}
