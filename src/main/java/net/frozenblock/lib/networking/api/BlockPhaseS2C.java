package net.frozenblock.lib.networking.api;

import com.unascribed.lib39.tunnel.api.NetworkContext;
import com.unascribed.lib39.tunnel.api.S2CMessage;
import com.unascribed.lib39.tunnel.api.annotation.field.MarshalledAs;
import com.unascribed.lib39.tunnel.api.annotation.field.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.FrozenMain;
import net.frozenblock.lib.damagesource.api.FrozenDamageSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;

public class BlockPhaseS2C extends S2CMessage {

	@MarshalledAs("blockpos")
	public BlockPos pos;
	@MarshalledAs("u8")
	public int tickDuration;
	@MarshalledAs("u8")
	@Optional
	public int delay;

	public BlockPhaseS2C(NetworkContext ctx) {
		super(ctx);
	}

	public BlockPhaseS2C(BlockPos pos, int tickDuration, int delay) {
		super(FrozenMain.NETWORKING);
		this.pos = pos;
		this.tickDuration = tickDuration;
		this.delay = delay;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void handle(Minecraft client, LocalPlayer player) {
		player.level.phaseBlock(this.pos, this.tickDuration, this.delay, FrozenDamageSource.BLOCK_PHASE);
		FrozenMain.log("Block Phase packet received", FrozenMain.UNSTABLE_LOGGING);
	}
}
