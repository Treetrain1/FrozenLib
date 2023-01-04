package net.frozenblock.lib.networking.api;

import com.unascribed.lib39.tunnel.api.NetworkContext;
import com.unascribed.lib39.tunnel.api.S2CMessage;
import com.unascribed.lib39.tunnel.api.annotation.field.MarshalledAs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.frozenblock.lib.FrozenMain;
import net.frozenblock.lib.sound.api.FlyBySoundHub;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class LocalSoundS2C extends S2CMessage {

	@MarshalledAs("double")
	public double x;
	@MarshalledAs("double")
	public double y;
	@MarshalledAs("double")
	public double z;
	@MarshalledAs("net.frozenblock.lib.networking.api.marshaller.SoundEventMarshaller")
	public RegistryIdHolder<SoundEvent> sound;
	public SoundSource category;
	@MarshalledAs("float")
	public float volume;
	@MarshalledAs("float")
	public float pitch;
	public boolean distanceDelay;

	public LocalSoundS2C(NetworkContext ctx) {
		super(ctx);
	}

	public LocalSoundS2C(double x, double y, double z, SoundEvent sound, SoundSource category, float volume, float pitch, boolean distanceDelay) {
		super(FrozenMain.NETWORKING);
		this.x = x;
		this.y = y;
		this.z = z;
		this.sound = new RegistryIdHolder<>(Registry.SOUND_EVENT, sound);
		this.category = category;
		this.volume = volume;
		this.pitch = pitch;
		this.distanceDelay = distanceDelay;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void handle(Minecraft client, LocalPlayer player) {
		ClientLevel level = client.level;
		if (level != null) {
			level.playLocalSound(this.x, this.y, this.z, this.sound.object(), this.category, this.volume, this.pitch, this.distanceDelay);
		}
		FrozenMain.log("Local sound packet received", FrozenMain.UNSTABLE_LOGGING);
	}

	public void send(Level level) {
		for (ServerPlayer player : PlayerLookup.tracking((ServerLevel) level, new BlockPos(this.x, this.y, this.z)))
			this.sendTo(player);
	}
}
