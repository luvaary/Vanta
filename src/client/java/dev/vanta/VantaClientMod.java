package dev.vanta;

import dev.vanta.core.VantaClientRuntime;
import net.fabricmc.api.ClientModInitializer;

public final class VantaClientMod implements ClientModInitializer {
    public void onInitializeClient() {
        VantaClientRuntime.getInstance().initialize();
    }
}
