package net.frozenblock.lib.core.testmod.config;

import net.frozenblock.lib.core.FrozenBools;

public final class ClothConfigInteractionHandler {

    public static boolean testBoolean() {
        if (FrozenBools.hasCloth) {
            return TestConfig.get().testBoolean;
        }
        return true;
    }

    public static boolean testSubMenuBoolean() {
        if (FrozenBools.hasCloth) {
            return TestConfig.get().subMenu.testSubMenuBoolean;
        }
        return true;
    }
}
