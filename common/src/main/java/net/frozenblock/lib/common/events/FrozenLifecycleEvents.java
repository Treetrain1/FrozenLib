package net.frozenblock.lib.common.events;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.frozenblock.lib.common.entrypoints.CommonEventEntrypoint;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.Nullable;

/**
 * Contains events for the common environment.
 */
public final class FrozenLifecycleEvents {
    private FrozenLifecycleEvents() {
        throw new UnsupportedOperationException("FrozenLifecycleEvents only contains static definitions.");
    }

    public static final Event<StartDataPackReload> START_DATA_PACK_RELOAD = EventFactory.createLoop();

    public static final Event<EndDataPackReload> END_DATA_PACK_RELOAD = EventFactory.createLoop();

    @FunctionalInterface
    public interface StartDataPackReload extends CommonEventEntrypoint {

        void onStartDataPackReload(@Nullable MinecraftServer server, @Nullable ResourceManager oldResourceManager);
    }

    @FunctionalInterface
    public interface EndDataPackReload extends CommonEventEntrypoint {

        void onEndDataPackReload(@Nullable MinecraftServer server, ResourceManager resourceManager, boolean success);
    }
}
