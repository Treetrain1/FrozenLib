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
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class FlybySoundS2C extends S2CMessage {

	@MarshalledAs("varint")
	public int entityId;
	@MarshalledAs("net.frozenblock.lib.networking.api.marshaller.SoundEventMarshaller")
	public RegistryIdHolder<SoundEvent> sound;
	public SoundSource category;
	@MarshalledAs("float")
	public float volume;
	@MarshalledAs("float")
	public float pitch;

	public FlybySoundS2C(NetworkContext ctx) {
		super(ctx);
	}

	public FlybySoundS2C(int entityId, SoundEvent sound, SoundSource category, float volume, float pitch) {
		super(FrozenMain.NETWORKING);
		this.entityId = entityId;
		this.sound = new RegistryIdHolder<>(Registry.SOUND_EVENT, sound);
		this.category = category;
		this.volume = volume;
		this.pitch = pitch;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void handle(Minecraft client, LocalPlayer player) {
		Entity entity = player.level.getEntity(this.entityId);
		FlyBySoundHub.FlyBySound flyBySound = new FlyBySoundHub.FlyBySound(this.pitch, this.volume, this.category, this.sound.object());
		FlyBySoundHub.addEntity(entity, flyBySound);
		FrozenMain.log("Flyby sound packet received", FrozenMain.UNSTABLE_LOGGING);
	}

	public void send(Level level) {
		Entity entity = level.getEntity(this.entityId);
		assert entity != null;
		for (ServerPlayer player : PlayerLookup.around((ServerLevel) level, entity.blockPosition(), 128))
			this.sendTo(player);
	}
}
