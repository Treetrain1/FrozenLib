package net.frozenblock.lib.networking.api;

import com.unascribed.lib39.tunnel.api.NetworkContext;
import com.unascribed.lib39.tunnel.api.S2CMessage;
import com.unascribed.lib39.tunnel.api.annotation.field.MarshalledAs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.FrozenMain;
import net.frozenblock.lib.spotting_icons.impl.EntitySpottingIconInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

public class SpottingIconS2C extends S2CMessage {

	@MarshalledAs("varint")
	public int entityId;
	@MarshalledAs("string")
	public String textureNamespace;
	@MarshalledAs("string")
	public String texturePath;
	@MarshalledAs("float")
	public float startFade;
	@MarshalledAs("float")
	public float endFade;
	@MarshalledAs("string")
	public String namespace;
	@MarshalledAs("string")
	public String path;

	public SpottingIconS2C(NetworkContext ctx) {
		super(ctx);
	}

	public SpottingIconS2C(int entityId, ResourceLocation texture, float startFade, float endFade, ResourceLocation predicate) {
		super(FrozenMain.NETWORKING);
		this.entityId = entityId;
		this.textureNamespace = texture.getNamespace();
		this.texturePath = texture.getPath();
		this.startFade = startFade;
		this.endFade = endFade;
		this.namespace = predicate.getNamespace();
		this.path = predicate.getPath();
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void handle(Minecraft client, LocalPlayer player) {
		var level = player.level;
		var entity = level.getEntity(this.entityId);
		var texture = new ResourceLocation(this.textureNamespace, this.texturePath);
		var predicate = new ResourceLocation(this.namespace, this.path);
		if (entity instanceof EntitySpottingIconInterface livingEntity) {
			livingEntity.getSpottingIconManager().setIcon(texture, this.startFade, this.endFade, predicate);
		}
	}
}
