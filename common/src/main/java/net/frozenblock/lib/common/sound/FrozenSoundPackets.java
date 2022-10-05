package net.frozenblock.lib.common.sound;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.frozenblock.lib.common.interfaces.EntityLoopingSoundInterface;
import net.frozenblock.lib.common.network.PlayerLookup;
import net.frozenblock.lib.common.FrozenMain;
import net.frozenblock.lib.common.registry.FrozenRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FrozenSoundPackets {

    public static void createMovingRestrictionSound(Level world, Entity entity, SoundEvent sound, SoundSource category, float volume, float pitch, ResourceLocation id) {
        if (!world.isClientSide) {
            FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
            byteBuf.writeVarInt(entity.getId());
            byteBuf.writeId(Registry.SOUND_EVENT, sound);
            byteBuf.writeEnum(category);
            byteBuf.writeFloat(volume);
            byteBuf.writeFloat(pitch);
            byteBuf.writeResourceLocation(id);
            NetworkManager.sendToPlayers(PlayerLookup.tracking((ServerLevel) world, entity.blockPosition()),
                    FrozenMain.MOVING_RESTRICTION_SOUND_PACKET, byteBuf
            );

        }
    }

    public static void createMovingRestrictionLoopingSound(Level world, Entity entity, SoundEvent sound, SoundSource category, float volume, float pitch, ResourceLocation id) {
        if (!world.isClientSide) {
            FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
            byteBuf.writeVarInt(entity.getId());
            byteBuf.writeId(Registry.SOUND_EVENT, sound);
            byteBuf.writeEnum(category);
            byteBuf.writeFloat(volume);
            byteBuf.writeFloat(pitch);
            byteBuf.writeResourceLocation(id);
            NetworkManager.sendToPlayers(PlayerLookup.tracking((ServerLevel) world, entity.blockPosition()),
                    FrozenMain.MOVING_RESTRICTION_LOOPING_SOUND_PACKET, byteBuf
            );
            if (entity instanceof LivingEntity living) {
                ((EntityLoopingSoundInterface)living).addSound(Registry.SOUND_EVENT.getKey(sound), category, volume, pitch, id);
            }
        }
    }

    public static void createMovingRestrictionLoopingSound(ServerPlayer player, Entity entity, SoundEvent sound, SoundSource category, float volume, float pitch, ResourceLocation id) {
        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        byteBuf.writeVarInt(entity.getId());
        byteBuf.writeId(Registry.SOUND_EVENT, sound);
        byteBuf.writeEnum(category);
        byteBuf.writeFloat(volume);
        byteBuf.writeFloat(pitch);
        byteBuf.writeResourceLocation(id);
        NetworkManager.sendToPlayer(player, FrozenMain.MOVING_RESTRICTION_LOOPING_SOUND_PACKET, byteBuf);
    }

    public static void createStartingMovingRestrictionLoopingSound(Level world, Entity entity, SoundEvent startingSound, SoundEvent sound, SoundSource category, float volume, float pitch, ResourceLocation id) {
        if (!world.isClientSide) {
            FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
            byteBuf.writeVarInt(entity.getId());
            byteBuf.writeId(FrozenRegistry.STARTING_SOUND, startingSound);
            byteBuf.writeId(Registry.SOUND_EVENT, sound);
            byteBuf.writeEnum(category);
            byteBuf.writeFloat(volume);
            byteBuf.writeFloat(pitch);
            byteBuf.writeResourceLocation(id);
            NetworkManager.sendToPlayers(PlayerLookup.tracking((ServerLevel) world, entity.blockPosition()),
                    FrozenMain.STARTING_RESTRICTION_LOOPING_SOUND_PACKET, byteBuf
            );
            if (entity instanceof LivingEntity living) {
                ((EntityLoopingSoundInterface)living).addSound(Registry.SOUND_EVENT.getKey(sound), category, volume, pitch, id);
            }
        }
    }

    public static void createStartingMovingRestrictionLoopingSound(ServerPlayer player, Entity entity, SoundEvent startingSound, SoundEvent sound, SoundSource category, float volume, float pitch, ResourceLocation id) {
        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        byteBuf.writeVarInt(entity.getId());
        byteBuf.writeId(FrozenRegistry.STARTING_SOUND, startingSound);
        byteBuf.writeId(Registry.SOUND_EVENT, sound);
        byteBuf.writeEnum(category);
        byteBuf.writeFloat(volume);
        byteBuf.writeFloat(pitch);
        byteBuf.writeResourceLocation(id);
        NetworkManager.sendToPlayer(player, FrozenMain.STARTING_RESTRICTION_LOOPING_SOUND_PACKET, byteBuf);
    }

}
