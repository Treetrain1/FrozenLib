package net.frozenblock.lib.networking.api.marshaller;

import com.unascribed.lib39.tunnel.api.Marshaller;
import net.frozenblock.lib.networking.api.RegistryIdHolder;
import net.minecraft.core.IdMap;
import net.minecraft.network.FriendlyByteBuf;

public abstract class RegistryIdHolderMarshaller<T> implements Marshaller<RegistryIdHolder<T>> {

	public IdMap<T> idMap;

	@Override
	public RegistryIdHolder<T> unmarshal(FriendlyByteBuf in) {
		T element = in.readById(this.idMap);

		return new RegistryIdHolder<>(this.idMap, element);
	}

	@Override
	public void marshal(FriendlyByteBuf out, RegistryIdHolder<T> tRegistryIdHolder) {
		out.writeId(tRegistryIdHolder.idMap(), tRegistryIdHolder.object());
	}
}
