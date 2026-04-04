package dev.vanta.core;

import dev.vanta.VantaMod;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public final class VantaModuleRegistry {
    private final List<VantaModule> modules = new ArrayList<>();
    private final Set<String> disabledModules = new HashSet<>();

    public void register(VantaModule module) {
        this.modules.add(module);
        this.modules.sort(Comparator.comparingInt(VantaModule::priority));
    }

    public void initialize(VantaContext context) {
        for (VantaModule module : this.modules) {
            invoke(module, () -> module.onInitialize(context), "initialize");
        }
    }

    public void onClientTick(MinecraftClient client) {
        for (VantaModule module : this.modules) {
            invoke(module, () -> module.onClientTick(client), "tick");
        }
    }

    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        for (VantaModule module : this.modules) {
            invoke(module, () -> module.onHudRender(context, tickCounter), "hud");
        }
    }

    private void invoke(VantaModule module, Runnable runnable, String stage) {
        if (this.disabledModules.contains(module.id())) {
            return;
        }

        try {
            runnable.run();
        } catch (Throwable throwable) {
            this.disabledModules.add(module.id());
            VantaMod.LOGGER.error("Vanta disabled module {} after {} failure", module.id(), stage, throwable);
        }
    }
}
