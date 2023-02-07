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
import net.minecraft.world.phys.Vec3;

public class ScreenShakeS2C extends S2CMessage {

	@MarshalledAs("float")
	public float intensity;
	@MarshalledAs("int")
	public int duration;
	@MarshalledAs("float")
	public int fallOffStart;
	@MarshalledAs("double")
	public double x;
	@MarshalledAs("double")
	public double y;
	@MarshalledAs("double")
	public double z;
	@MarshalledAs("float")
	public float maxDistance;

	public ScreenShakeS2C(NetworkContext ctx) {
		super(ctx);
	}

	public ScreenShakeS2C(float intensity, int duration, int fallOffStart, double x, double y, double z, float maxDistance) {
		super(FrozenMain.NETWORKING);
		this.intensity = intensity;
		this.duration = duration;
		this.fallOffStart = fallOffStart;
		this.x = x;
		this.y = y;
		this.z = z;
		this.maxDistance = maxDistance;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void handle(Minecraft client, LocalPlayer player) {
		Vec3 pos = new Vec3(this.x, this.y, this.z);
		ScreenShaker.addShake(this.intensity, this.duration, this.fallOffStart, pos, this.maxDistance);
	}
}
