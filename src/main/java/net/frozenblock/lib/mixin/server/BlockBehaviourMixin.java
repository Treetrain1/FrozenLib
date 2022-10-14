package net.frozenblock.lib.mixin.server;

import net.frozenblock.lib.events.ScheduledBlockEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo info) {
        ScheduledBlockEvents.ON_SCHEDULED_TICK.invoker().onScheduledTick(state, level, pos, random);
    }

    @Mixin(BlockBehaviour.BlockStateBase.class)
    private static class BlockStateBaseMixin {
        // i was testing doing this with BlockStateBase
        @Shadow
        protected BlockState asState() {
            throw new AssertionError("Mixin injection failed.");
        }

        @Inject(method = "onPlace", at = @At("TAIL"))
        private void frozenLib_onPlace(Level level, BlockPos pos, BlockState oldState, boolean isMoving, CallbackInfo ci) {
            var state = this.asState();
            ScheduledBlockEvents.ON_BLOCK_PLACE.invoker().onBlockPlace(state, level, pos, oldState, isMoving);
        }

        /*@Inject(method = "tick", at = @At("TAIL"))
        private void frozenLib_scheduledTick(ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
            var state = this.asState();
            ScheduledBlockEvents.ON_SCHEDULED_TICK.invoker().onScheduledTick(state, level, pos, random);
        }*/
    }

}
