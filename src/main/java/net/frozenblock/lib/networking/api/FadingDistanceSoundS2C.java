package net.frozenblock.lib.networking.api;

import com.unascribed.lib39.tunnel.api.NetworkContext;
import com.unascribed.lib39.tunnel.api.S2CMessage;
import com.unascribed.lib39.tunnel.api.annotation.field.MarshalledAs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.frozenblock.lib.FrozenMain;
import net.frozenblock.lib.sound.api.instances.distance_based.FadingDistanceSwitchingSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class FadingDistanceSoundS2C extends S2CMessage {

	@MarshalledAs("double")
	public double x;
	@MarshalledAs("double")
	public double y;
	@MarshalledAs("double")
	public double z;
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

	public FadingDistanceSoundS2C(NetworkContext ctx) {
		super(ctx);
	}

	public FadingDistanceSoundS2C(double x, double y, double z, SoundEvent sound, SoundEvent sound2, SoundSource category, float volume, float pitch, float fadeDist, float maxDist, ResourceLocation id) {
		super(FrozenMain.NETWORKING);
		this.x = x;
		this.y = y;
		this.z = z;
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
		ClientLevel level = client.level;
		client.getSoundManager().play(new FadingDistanceSwitchingSound(this.sound.object(), this.category, this.volume, this.pitch, this.fadeDist, this.maxDist, this.volume, false, this.x, this.y, this.z));
		client.getSoundManager().play(new FadingDistanceSwitchingSound(this.sound2.object(), this.category, this.volume, this.pitch, this.fadeDist, this.maxDist, this.volume, true, this.x, this.y, this.z));
	}

	public void send(ServerLevel level) {
		for (ServerPlayer player : PlayerLookup.tracking(level, new BlockPos(this.x, this.y, this.z)))
			this.sendTo(player);
	}
}
