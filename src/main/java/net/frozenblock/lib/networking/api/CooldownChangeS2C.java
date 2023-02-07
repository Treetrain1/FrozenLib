package net.frozenblock.lib.networking.api;

import com.unascribed.lib39.tunnel.api.NetworkContext;
import com.unascribed.lib39.tunnel.api.S2CMessage;
import com.unascribed.lib39.tunnel.api.annotation.field.MarshalledAs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.FrozenMain;
import net.frozenblock.lib.item.impl.CooldownInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

public class CooldownChangeS2C extends S2CMessage {

	@MarshalledAs("net.frozenblock.lib.networking.api.marshaller.ItemMarshaller")
	public RegistryIdHolder<Item> item;
	@MarshalledAs("varint")
	public int additionalCooldown;

	public CooldownChangeS2C(NetworkContext ctx) {
		super(ctx);
	}

	public CooldownChangeS2C(Item item, int additionalCooldown) {
		super(FrozenMain.NETWORKING);
		this.item = new RegistryIdHolder<>(Registry.ITEM, item);
		this.additionalCooldown = additionalCooldown;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void handle(Minecraft client, LocalPlayer player) {
		var level = player.level;
		((CooldownInterface) player.getCooldowns()).changeCooldown(this.item.object(), this.additionalCooldown);
	}
}
