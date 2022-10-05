package net.frozenblock.lib.common.sound;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.frozenblock.lib.common.FrozenMain;
import net.frozenblock.lib.common.network.PlayerLookup;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class FlyBySoundPacket {

    public static void createFlybySound(Level world, Entity entity, SoundEvent sound, SoundSource category, float volume, float pitch) {
        if (!world.isClientSide) {
            FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
            byteBuf.writeVarInt(entity.getId());
            byteBuf.writeId(Registry.SOUND_EVENT, sound);
            byteBuf.writeEnum(category);
            byteBuf.writeFloat(volume);
            byteBuf.writeFloat(pitch);
            for (ServerPlayer player : PlayerLookup.around((ServerLevel) world, entity.blockPosition(), 128)) {
                NetworkManager.sendToPlayer(player, FrozenMain.FLYBY_SOUND_PACKET, byteBuf);
            }
        }
    }

}
