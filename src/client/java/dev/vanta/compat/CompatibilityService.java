package dev.vanta.compat;

import net.fabricmc.loader.api.FabricLoader;

public final class CompatibilityService {
    private CompatibilityService() {
    }

    public static CompatibilityState detect() {
        FabricLoader loader = FabricLoader.getInstance();
        boolean sodium = loader.isModLoaded("sodium");
        boolean iris = loader.isModLoaded("iris");
        boolean immediatelyFast = loader.isModLoaded("immediatelyfast");
        return new CompatibilityState(sodium, iris, immediatelyFast);
    }
}
