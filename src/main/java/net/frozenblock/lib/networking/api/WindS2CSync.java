package net.frozenblock.lib.networking.api;

import com.unascribed.lib39.tunnel.api.NetworkContext;
import com.unascribed.lib39.tunnel.api.S2CMessage;
import com.unascribed.lib39.tunnel.api.annotation.field.MarshalledAs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.FrozenMain;
import net.frozenblock.lib.wind.api.ClientWindManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public class WindS2CSync extends S2CMessage {

	@MarshalledAs("long")
	public long time;
	@MarshalledAs("double")
	public double x;
	@MarshalledAs("double")
	public double y;
	@MarshalledAs("double")
	public double z;
	@MarshalledAs("long")
	public long seed;
	public boolean overrideWind;
	@MarshalledAs("double")
	public double commandX;
	@MarshalledAs("double")
	public double commandY;
	@MarshalledAs("double")
	public double commandZ;

	public WindS2CSync(NetworkContext ctx) {
		super(ctx);
	}

	public WindS2CSync(long time, double x, double y, double z, long seed, boolean overrideWind, double commandX, double commandY, double commandZ) {
		super(FrozenMain.NETWORKING);
		this.time = time;
		this.x = x;
		this.y = y;
		this.z = z;
		this.seed = seed;
		this.overrideWind = overrideWind;
		this.commandX = commandX;
		this.commandY = commandY;
		this.commandZ = commandZ;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void handle(Minecraft client, LocalPlayer player) {
		if (client.level != null) {
			ClientWindManager.time = this.time;
			ClientWindManager.cloudX = this.x;
			ClientWindManager.cloudY = this.y;
			ClientWindManager.cloudZ = this.z;
			ClientWindManager.setSeed(this.seed);
			ClientWindManager.overrideWind = this.overrideWind;
			ClientWindManager.commandWind = new Vec3(this.commandX, this.commandY, this.commandZ);
			ClientWindManager.hasInitialized = true;
		}
		FrozenMain.log("Wind sync packet received", FrozenMain.UNSTABLE_LOGGING);
	}
}
