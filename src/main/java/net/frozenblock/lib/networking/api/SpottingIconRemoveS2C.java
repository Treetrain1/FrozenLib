package net.frozenblock.lib.networking.api;

import com.unascribed.lib39.tunnel.api.NetworkContext;
import com.unascribed.lib39.tunnel.api.S2CMessage;
import com.unascribed.lib39.tunnel.api.annotation.field.MarshalledAs;
import net.frozenblock.lib.FrozenMain;
import net.frozenblock.lib.spotting_icons.impl.EntitySpottingIconInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class SpottingIconRemoveS2C extends S2CMessage {

	@MarshalledAs("varint")
	public int entityId;

	public SpottingIconRemoveS2C(NetworkContext ctx) {
		super(ctx);
	}

	public SpottingIconRemoveS2C(int entityId) {
		super(FrozenMain.NETWORKING);
		this.entityId = entityId;
	}

	@Override
	protected void handle(Minecraft client, LocalPlayer player) {
		var level = player.level;
		var entity = level.getEntity(this.entityId);
		if (entity instanceof EntitySpottingIconInterface livingEntity) {
			livingEntity.getSpottingIconManager().icon = null;
		}
	}
}
