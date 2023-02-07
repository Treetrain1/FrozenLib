package net.frozenblock.lib.networking.api;

import com.unascribed.lib39.tunnel.api.NetworkContext;
import com.unascribed.lib39.tunnel.api.S2CMessage;
import com.unascribed.lib39.tunnel.api.annotation.field.MarshalledAs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.FrozenMain;
import net.frozenblock.lib.screenshake.api.ScreenShaker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class ScreenShakeEntityS2C extends S2CMessage {

	@MarshalledAs("varint")
	public int entityId;
	@MarshalledAs("float")
	public float intensity;
	@MarshalledAs("int")
	public int duration;
	@MarshalledAs("int")
	public int falloffStart;
	@MarshalledAs("float")
	public float maxDistance;

	public ScreenShakeEntityS2C(NetworkContext ctx) {
		super(ctx);
	}

	public ScreenShakeEntityS2C(int entityId, float intensity, int duration, int falloffStart, float maxDistance) {
		super(FrozenMain.NETWORKING);
		this.entityId = entityId;
		this.intensity = intensity;
		this.duration = duration;
		this.falloffStart = falloffStart;
		this.maxDistance = maxDistance;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void handle(Minecraft client, LocalPlayer player) {
		var level = player.level;
		var entity = level.getEntity(this.entityId);
		ScreenShaker.addShake(entity, this.intensity, this.duration, this.falloffStart, this.maxDistance);
	}
}
