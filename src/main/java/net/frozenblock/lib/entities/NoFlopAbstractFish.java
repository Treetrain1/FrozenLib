package net.frozenblock.lib.entities;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.level.Level;

/**
 * This is the same as {@link AbstractFish} but the entity will not flop when on land.
 */
public abstract class NoFlopAbstractFish extends AbstractFish {

	public NoFlopAbstractFish(EntityType<? extends NoFlopAbstractFish> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	protected SoundEvent getFlopSound() {
		return null;
	}

	/**
	 * Acts as a form of access widener.
	 */
	public boolean canRandomSwim() {
		return super.canRandomSwim();
	}
}
