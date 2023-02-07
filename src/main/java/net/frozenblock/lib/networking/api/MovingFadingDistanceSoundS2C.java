package net.frozenblock.lib.networking.api;

import com.unascribed.lib39.tunnel.api.NetworkContext;
import com.unascribed.lib39.tunnel.api.S2CMessage;
import com.unascribed.lib39.tunnel.api.annotation.field.MarshalledAs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.FrozenMain;
import net.frozenblock.lib.sound.api.instances.distance_based.RestrictedMovingFadingDistanceSwitchingSoundLoop;
import net.frozenblock.lib.sound.api.predicate.SoundPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class MovingFadingDistanceSoundS2C extends S2CMessage {

	@MarshalledAs("varint")
	public int entityId;
	@MarshalledAs("net.frozenblock.lib.networking.api.marshaller.SoundEventMarshaller")
	public RegistryIdHolder<SoundEvent> sound;
	@MarshalledAs("net.frozenblock.lib.networking.api.marshaller.SoundEventMarshaller")
	public RegistryIdHolder<SoundEvent> sound2;
	public SoundSource category;
	@MarshalledAs("float")
	public float volume;
	@MarshalledAs("float")
	public float pitch;
	@MarshalledAs("float")
	public float fadeDist;
	@MarshalledAs("float")
	public float maxDist;
	@MarshalledAs("string")
	public String namespace;
	@MarshalledAs("string")
	public String path;

	public MovingFadingDistanceSoundS2C(NetworkContext ctx) {
		super(ctx);
	}

	public MovingFadingDistanceSoundS2C(int entityId, SoundEvent sound, SoundEvent sound2, SoundSource category, float volume, float pitch, float fadeDist, float maxDist, ResourceLocation id) {
		super(FrozenMain.NETWORKING);
		this.entityId = entityId;
		this.sound = new RegistryIdHolder<>(Registry.SOUND_EVENT, sound);
		this.sound2 = new RegistryIdHolder<>(Registry.SOUND_EVENT, sound2);
		this.category = category;
		this.volume = volume;
		this.pitch = pitch;
		this.fadeDist = fadeDist;
		this.maxDist = maxDist;
		this.namespace = id.getNamespace();
		this.path = id.getPath();
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void handle(Minecraft client, LocalPlayer player) {
		var level = player.level;
		var entity = level.getEntity(this.entityId);
		var location = new ResourceLocation(this.namespace, this.path);
		var predicate = SoundPredicate.getPredicate(location);
		if (entity != null) {
			client.getSoundManager().play(new RestrictedMovingFadingDistanceSwitchingSoundLoop<>(entity, this.sound.object(), this.category, this.volume, this.pitch, predicate, this.fadeDist, this.maxDist, this.volume, false));
			client.getSoundManager().play(new RestrictedMovingFadingDistanceSwitchingSoundLoop<>(entity, this.sound2.object(), this.category, this.volume, this.pitch, predicate, this.fadeDist, this.maxDist, this.volume, true));
		}
	}
}
