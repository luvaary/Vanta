package dev.vanta.module.diagnostics;

import dev.vanta.VantaMod;
import dev.vanta.config.VantaConfig;
import dev.vanta.core.CombatSnapshot;
import dev.vanta.core.VantaContext;
import dev.vanta.core.VantaModule;
import dev.vanta.metrics.BenchmarkRecorder;
import java.nio.file.Path;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public final class AutoBenchmarkModule implements VantaModule {
    private VantaContext context;
    private final BenchmarkRecorder recorder = new BenchmarkRecorder();
    private boolean wasInWorld;

    public String id() {
        return "auto_benchmark";
    }

    public int priority() {
        return 700;
    }

    public void onInitialize(VantaContext context) {
        this.context = context;
    }

    public void onClientTick(MinecraftClient client) {
        VantaConfig.DiagnosticsSettings diagnostics = this.context.config().diagnostics;
        CombatSnapshot snapshot = this.context.snapshot();

        boolean inWorld = client.world != null && client.player != null;

        if (!diagnostics.enabled) {
            if (this.recorder.isActive()) {
                this.recorder.stopAndExport();
            }
            this.wasInWorld = inWorld;
            snapshot.setBenchmarkActive(false);
            snapshot.setBenchmarkElapsedSeconds(0);
            return;
        }

        if (inWorld && !this.wasInWorld) {
            this.recorder.start();
        }

        if (!inWorld && this.wasInWorld && this.recorder.isActive()) {
            Path exported = this.recorder.stopAndExport();
            if (exported != null) {
                VantaMod.LOGGER.info("Vanta benchmark exported: {}", exported.toAbsolutePath());
            }
        }

        if (inWorld && this.recorder.isActive()) {
            this.recorder.onClientTick(snapshot, diagnostics);
        } else {
            snapshot.setBenchmarkActive(false);
            snapshot.setBenchmarkElapsedSeconds(0);
        }

        this.wasInWorld = inWorld;
    }

    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
    }
}
