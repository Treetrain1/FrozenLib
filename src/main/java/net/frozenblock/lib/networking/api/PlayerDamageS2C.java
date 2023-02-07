package net.frozenblock.lib.networking.api;

import com.unascribed.lib39.tunnel.api.NetworkContext;
import com.unascribed.lib39.tunnel.api.S2CMessage;
import com.unascribed.lib39.tunnel.api.annotation.field.MarshalledAs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.FrozenMain;
import net.frozenblock.lib.damagesource.api.PlayerDamageSourceSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;

public class PlayerDamageS2C extends S2CMessage {

	@MarshalledAs("varint")
	public int entityId;
	@MarshalledAs("string")
	public String namesapce;
	@MarshalledAs("string")
	public String path;
	@MarshalledAs("float")
	public float volume;

	public PlayerDamageS2C(NetworkContext ctx) {
		super(ctx);
	}

	public PlayerDamageS2C(int entityId, ResourceLocation damageLocation, float volume) {
		super(FrozenMain.NETWORKING);
		this.entityId = entityId;
		this.namesapce = damageLocation.getNamespace();
		this.path = damageLocation.getPath();
		this.volume = volume;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void handle(Minecraft client, LocalPlayer localPlayer) {
		var level = localPlayer.level;
		var entity = level.getEntity(this.entityId);
		var damageLocation = new ResourceLocation(this.namesapce, this.path);
		if (entity instanceof Player player) {
			SoundEvent soundEvent = PlayerDamageSourceSounds.getDamageSound(damageLocation);
			player.playSound(soundEvent, volume, (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.2F + 1.0F);
		}
	}
}
