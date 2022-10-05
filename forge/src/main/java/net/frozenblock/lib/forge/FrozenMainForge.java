package net.frozenblock.lib.forge;

import dev.architectury.platform.forge.EventBuses;
import net.frozenblock.lib.common.FrozenClient;
import net.frozenblock.lib.common.FrozenMain;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FrozenMain.MOD_ID)
public class FrozenMainForge {

    public FrozenMainForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(FrozenMain.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        FrozenMain.init();
    }

    @Mod.EventBusSubscriber(modid = FrozenMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class FrozenClientForge {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            FrozenClient.init();


        }
    }
}
