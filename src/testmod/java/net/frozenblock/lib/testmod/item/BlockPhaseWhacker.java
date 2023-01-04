package net.frozenblock.lib.testmod.item;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.frozenblock.lib.damagesource.api.FrozenDamageSource;
import net.frozenblock.lib.networking.api.BlockPhaseS2C;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BlockPhaseWhacker extends Item {
	public BlockPhaseWhacker(Properties properties) {
		super(properties);
	}

	@Override
	@NotNull
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		level.phaseBlock(pos, 100, 0, FrozenDamageSource.BLOCK_PHASE);
		return InteractionResult.SUCCESS;
	}
}
