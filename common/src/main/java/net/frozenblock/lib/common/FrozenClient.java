package net.frozenblock.lib.common;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import net.frozenblock.lib.common.interfaces.CooldownInterface;
import net.frozenblock.lib.common.registry.FrozenRegistry;
import net.frozenblock.lib.common.sound.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class FrozenClient {

    public static void init() {
        ClientTickEvent.CLIENT_LEVEL_PRE.register(e -> {
            Minecraft client = Minecraft.getInstance();
            if (client.level != null) {
                FlyBySoundHub.update(client, client.player, true);
            }
        });

        if (Platform.isDevelopmentEnvironment()) {
            FlyBySoundHub.autoEntitiesAndSounds.put(EntityType.ARROW, new FlyBySoundHub.FlyBySound(1.0F, 1.0F, SoundSource.NEUTRAL, SoundEvents.AXE_SCRAPE));
        }

        receiveMovingRestrictionSoundPacket();
        receiveMovingRestrictionLoopingSoundPacket();
        receiveStartingMovingRestrictionLoopingSoundPacket();
        receiveFlybySoundPacket();
        receiveCooldownChangePacket();
    }

    private static void receiveMovingRestrictionSoundPacket() {
        NetworkManager.registerReceiver(NetworkManager.s2c(), FrozenMain.MOVING_RESTRICTION_SOUND_PACKET, (buf, context) -> {
            int id = buf.readVarInt();
            SoundEvent sound = buf.readById(Registry.SOUND_EVENT);
            SoundSource category = buf.readEnum(SoundSource.class);
            float volume = buf.readFloat();
            float pitch = buf.readFloat();
            ResourceLocation predicateId = buf.readResourceLocation();
            context.queue(() -> {
                ClientLevel level = Minecraft.getInstance().level;
                if (level != null) {
                    Entity entity = level.getEntity(id);
                    if (entity != null) {
                        FrozenSoundPredicates.LoopPredicate<?> predicate = FrozenSoundPredicates.getPredicate(predicateId);
                        Minecraft.getInstance().getSoundManager().play(new MovingSoundWithRestriction(entity, sound, category, volume, pitch, predicate));
                    }
                }
            });
        });
    }

    private static void receiveMovingRestrictionLoopingSoundPacket() {
        NetworkManager.registerReceiver(NetworkManager.s2c(), FrozenMain.MOVING_RESTRICTION_LOOPING_SOUND_PACKET, (buf, context) -> {
            int id = buf.readVarInt();
            SoundEvent sound = buf.readById(Registry.SOUND_EVENT);
            SoundSource category = buf.readEnum(SoundSource.class);
            float volume = buf.readFloat();
            float pitch = buf.readFloat();
            ResourceLocation predicateId = buf.readResourceLocation();
            context.queue(() -> {
                ClientLevel level = Minecraft.getInstance().level;
                if (level != null) {
                    Entity entity = level.getEntity(id);
                    if (entity != null) {
                        FrozenSoundPredicates.LoopPredicate<?> predicate = FrozenSoundPredicates.getPredicate(predicateId);
                        Minecraft.getInstance().getSoundManager().play(new MovingSoundLoopWithRestriction(entity, sound, category, volume, pitch, predicate));
                    }
                }
            });
        });
    }

    private static void receiveStartingMovingRestrictionLoopingSoundPacket() {
        NetworkManager.registerReceiver(NetworkManager.s2c(), FrozenMain.STARTING_RESTRICTION_LOOPING_SOUND_PACKET, (buf, context) -> {
            int id = buf.readVarInt();
            SoundEvent startingSound = buf.readById(FrozenRegistry.STARTING_SOUND);
            SoundEvent loopingSound = buf.readById(Registry.SOUND_EVENT);
            SoundSource category = buf.readEnum(SoundSource.class);
            float volume = buf.readFloat();
            float pitch = buf.readFloat();
            ResourceLocation predicateId = buf.readResourceLocation();
            context.queue(() -> {
                ClientLevel level = Minecraft.getInstance().level;
                if (level != null) {
                    Entity entity = level.getEntity(id);
                    if (entity != null) {
                        FrozenSoundPredicates.LoopPredicate<?> predicate = FrozenSoundPredicates.getPredicate(predicateId);
                        Minecraft.getInstance().getSoundManager().play(new StartingSoundInstance(entity, startingSound, loopingSound, category, volume, pitch, predicate, new MovingSoundLoopWithRestriction(entity, loopingSound, category, volume, pitch, predicate)));
                    }
                }
            });
        });
    }

    private static void receiveFlybySoundPacket() {
        NetworkManager.registerReceiver(NetworkManager.s2c(), FrozenMain.FLYBY_SOUND_PACKET, (buf, context) -> {
            int id = buf.readVarInt();
            SoundEvent sound = buf.readById(Registry.SOUND_EVENT);
            SoundSource category = buf.readEnum(SoundSource.class);
            float volume = buf.readFloat();
            float pitch = buf.readFloat();
            context.queue(() -> {
                ClientLevel level = Minecraft.getInstance().level;
                if (level != null) {
                    Entity entity = level.getEntity(id);
                    if (entity != null) {
                        FlyBySoundHub.FlyBySound flyBySound = new FlyBySoundHub.FlyBySound(pitch, volume, category, sound);
                        FlyBySoundHub.addEntity(entity, flyBySound);
                    }
                }
            });
        });
    }

    private static void receiveCooldownChangePacket() {
        NetworkManager.registerReceiver(NetworkManager.s2c(), FrozenMain.COOLDOWN_CHANGE_PACKET, (buf, context) -> {
            Item item = buf.readById(Registry.ITEM);
            int additional = buf.readVarInt();
            context.queue(() -> {
                ClientLevel level = Minecraft.getInstance().level;
                if (level != null && Minecraft.getInstance().player != null) {
                    ((CooldownInterface)Minecraft.getInstance().player.getCooldowns()).changeCooldown(item , additional);
                }
            });
        });
    }
}
