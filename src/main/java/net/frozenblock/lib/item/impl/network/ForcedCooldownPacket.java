/*
 * Copyright 2023 FrozenBlock
 * Copyright 2023 FrozenBlock
 * Modified to work on Fabric
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2022 FrozenBlock
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2021-2022 FrozenBlock
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2023 FrozenBlock
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2021-2023 FrozenBlock
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2022-2023 FrozenBlock
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2022 QuiltMC
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2021-2022 QuiltMC
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2023 QuiltMC
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2021-2023 QuiltMC
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2022-2023 QuiltMC
 * ;;match_from: \/\/\/ Q[Uu][Ii][Ll][Tt]
 */

package net.frozenblock.lib.item.impl.network;

import net.frozenblock.lib.FrozenSharedConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public record ForcedCooldownPacket(
	Item item,
	int startTime,
	int endTime
) implements CustomPacketPayload {

	public static final Type<ForcedCooldownPacket> PACKET_TYPE = CustomPacketPayload.createType(
		FrozenSharedConstants.string("forced_cooldown_packet")
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, ForcedCooldownPacket> CODEC = StreamCodec.ofMember(ForcedCooldownPacket::write, ForcedCooldownPacket::new);

	public ForcedCooldownPacket(RegistryFriendlyByteBuf buf) {
		this(ByteBufCodecs.registry(Registries.ITEM).decode(buf), buf.readVarInt(), buf.readVarInt());
	}

	public void write(RegistryFriendlyByteBuf buf) {
		ByteBufCodecs.registry(Registries.ITEM).encode(buf, this.item());
		buf.writeVarInt(this.startTime());
		buf.writeVarInt(this.endTime());
	}

	@Override
	@NotNull
	public Type<?> type() {
		return PACKET_TYPE;
	}
}
