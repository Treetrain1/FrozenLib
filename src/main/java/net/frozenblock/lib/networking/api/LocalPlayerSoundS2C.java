package net.frozenblock.lib.networking.api;

import com.unascribed.lib39.tunnel.api.NetworkContext;
import com.unascribed.lib39.tunnel.api.S2CMessage;
import com.unascribed.lib39.tunnel.api.annotation.field.MarshalledAs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.FrozenMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class LocalPlayerSoundS2C extends S2CMessage {

	@MarshalledAs("net.frozenblock.lib.networking.api.marshaller.SoundEventMarshaller")
	public RegistryIdHolder<SoundEvent> sound;
	@MarshalledAs("float")
	public float volume;
	@MarshalledAs("float")
	public float pitch;

	public LocalPlayerSoundS2C(NetworkContext ctx) {
		super(ctx);
	}

	public LocalPlayerSoundS2C(SoundEvent sound, float volume, float pitch) {
		super(FrozenMain.NETWORKING);
		this.sound = new RegistryIdHolder<>(Registry.SOUND_EVENT, sound);
		this.volume = volume;
		this.pitch = pitch;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void handle(Minecraft client, LocalPlayer player) {
		assert client.level != null;
		client.getSoundManager().play(new EntityBoundSoundInstance(this.sound.object(), SoundSource.PLAYERS, this.volume, this.pitch, player, client.level.random.nextLong()));
	}

	public void send(ServerPlayer player) {
		this.sendTo(player);
	}
}
