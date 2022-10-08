package net.frozenblock.lib.common.registry;

import com.mojang.serialization.Codec;
import dev.architectury.registry.registries.DeferredRegister;
import net.frozenblock.lib.common.FrozenMain;
import net.frozenblock.lib.common.sound.StartingSounds;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.material.Fluid;
import org.quiltmc.qsl.frozenblock.common.worldgen.surface_rule.impl.QuiltSurfaceRuleInitializer;

public class FrozenRegistry {

    public static final ResourceKey<Registry<SoundEvent>> STARTING_SOUND_REGISTRY = createRegistryKey(FrozenMain.id("starting_sound"));

    public static final Registry<SoundEvent> STARTING_SOUND = Registry.registerSimple(STARTING_SOUND_REGISTRY, registry -> StartingSounds.EMPTY_SOUND);

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(FrozenMain.MOD_ID, Registry.BLOCK_REGISTRY);

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(FrozenMain.MOD_ID, Registry.ENTITY_TYPE_REGISTRY);

    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(FrozenMain.MOD_ID, Registry.FLUID_REGISTRY);

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(FrozenMain.MOD_ID, Registry.ITEM_REGISTRY);

    public static final DeferredRegister<Codec<? extends SurfaceRules.RuleSource>> SURFACE_RULES =
            DeferredRegister.create(FrozenMain.MOD_ID, Registry.RULE_REGISTRY);

    public static final DeferredRegister<SoundEvent> DEFERRED_STARTING_SOUND =
            DeferredRegister.create(FrozenMain.MOD_ID, FrozenRegistry.STARTING_SOUND_REGISTRY);

    public static void init() {
        BLOCKS.register();
        ENTITIES.register();
        FLUIDS.register();
        ITEMS.register();
        SURFACE_RULES.register();
        DEFERRED_STARTING_SOUND.register();
    }

    public static <T> ResourceKey<Registry<T>> createRegistryKey(ResourceLocation key) {
        return ResourceKey.createRegistryKey(key);
    }
}
