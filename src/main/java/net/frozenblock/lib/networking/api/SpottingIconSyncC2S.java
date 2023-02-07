package net.frozenblock.lib.networking.api;

import com.unascribed.lib39.tunnel.api.C2SMessage;
import com.unascribed.lib39.tunnel.api.NetworkContext;
import com.unascribed.lib39.tunnel.api.annotation.field.MarshalledAs;
import net.frozenblock.lib.FrozenMain;
import net.frozenblock.lib.spotting_icons.impl.EntitySpottingIconInterface;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class SpottingIconSyncC2S extends C2SMessage {

	@MarshalledAs("varint")
	public int entityId;
	@MarshalledAs("string")
	public String namespace;
	@MarshalledAs("string")
	public String path;

	public SpottingIconSyncC2S(NetworkContext ctx) {
		super(ctx);
	}

	public SpottingIconSyncC2S(int entityId, ResourceKey<Level> level) {
		super(FrozenMain.NETWORKING);
		this.entityId = entityId;
		var location = level.location();
		this.namespace = location.getNamespace();
		this.path = location.getPath();
	}

	@Override
	protected void handle(ServerPlayer player) {
		var server = player.server;
		var key = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(this.namespace, this.path));
		var level = server.getLevel(key);
		if (level != null) {
			var entity = level.getEntity(this.entityId);
			if (entity instanceof EntitySpottingIconInterface livingEntity) {
				livingEntity.getSpottingIconManager().sendIconPacket(player);
			}
		}
	}
}
