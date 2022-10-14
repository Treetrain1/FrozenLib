package net.frozenblock.lib.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.frozenblock.lib.FrozenMain;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class ScheduledBlockEvents {



    /**
     * Called when a block is placed.
     */
    public static final Event<OnBlockPlace> ON_BLOCK_PLACE = FrozenEvents.createEnvironmentEvent(OnBlockPlace.class, callbacks -> (state, level, pos, oldState, isMoving) -> {
        if (EventFactory.isProfilingEnabled()) {
            final ProfilerFiller profiler = level.getProfiler();
            profiler.push("frozenLibOnBlockPlace");

            for (OnBlockPlace event : callbacks) {
                profiler.push(EventFactory.getHandlerName(event));
                event.onBlockPlace(state, level, pos, oldState, isMoving);
                FrozenMain.log("Block Place Event", FrozenMain.UNSTABLE_LOGGING);
                profiler.pop();
            }

            profiler.pop();
        } else {
            for (OnBlockPlace event : callbacks) {
                event.onBlockPlace(state, level, pos, oldState, isMoving);
                FrozenMain.log("Block Place Event", FrozenMain.UNSTABLE_LOGGING);
            }
        }
    });

    /**
     * Called at the end of a block's scheduled tick.
     * <p>
     * Can be used to set blocks when dripstone
     */
    public static final Event<OnScheduledTick> ON_SCHEDULED_TICK = FrozenEvents.createEnvironmentEvent(OnScheduledTick.class, callbacks -> ((state, level, pos, random) -> {
        if (EventFactory.isProfilingEnabled()) {
            final ProfilerFiller profiler = level.getProfiler();
            profiler.push("frozenLibOnScheduledBlockTick");

            for (OnScheduledTick event : callbacks) {
                profiler.push(EventFactory.getHandlerName(event));
                event.onScheduledTick(state, level, pos, random);
                FrozenMain.log("On Scheduled Block Tick Event", FrozenMain.UNSTABLE_LOGGING);
                profiler.pop();
            }

            profiler.pop();
        } else {
            for (OnScheduledTick event : callbacks) {
                event.onScheduledTick(state, level, pos, random);
                FrozenMain.log("On Scheduled Block Tick Event", FrozenMain.UNSTABLE_LOGGING);
            }
        }
    }));

    private ScheduledBlockEvents() {
        throw new RuntimeException("ScheduledBlockEvents only contains static declarations.");
    }

    @FunctionalInterface
    public interface OnBlockPlace {
        void onBlockPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving);
    }

    @FunctionalInterface
    public interface OnScheduledTick {
        void onScheduledTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random);
    }
}
