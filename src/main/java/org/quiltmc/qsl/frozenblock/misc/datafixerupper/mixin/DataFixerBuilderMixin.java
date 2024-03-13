package org.quiltmc.qsl.frozenblock.misc.datafixerupper.mixin;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.DataFixerUpper;
import com.mojang.datafixers.schemas.Schema;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.FabricDataFixerUpper;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixerBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

@Mixin(DataFixerBuilder.class)
public class DataFixerBuilderMixin {

	@Shadow
	@Final
	private Int2ObjectSortedMap<Schema> schemas;

	@Shadow
	@Final
	private List<DataFix> globalList;

	@Shadow
	@Final
	private IntSortedSet fixerVersions;

	@Inject(method = "build", at = @At("HEAD"), cancellable = true)
	private void buildFabric(CallbackInfoReturnable<DataFixerUpper> cir) {
		DataFixerBuilder builder = DataFixerBuilder.class.cast(this);

		if (builder instanceof QuiltDataFixerBuilder) {
			cir.setReturnValue(new FabricDataFixerUpper(this.schemas, this.globalList, this.fixerVersions));
		}
	}
}
