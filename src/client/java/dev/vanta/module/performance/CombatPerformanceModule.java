package dev.vanta.module.performance;

import dev.vanta.config.VantaConfig;
import dev.vanta.core.CombatSnapshot;
import dev.vanta.core.VantaContext;
import dev.vanta.core.VantaModule;
import java.util.Arrays;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public final class CombatPerformanceModule implements VantaModule {
    private VantaContext context;
    private long lastFrameNanos;
    private double[] frameWindow = new double[180];
    private int frameWindowCursor;
    private int frameWindowSize;
    private int p95RecomputeCursor;

    public String id() {
        return "combat_performance";
    }

    public int priority() {
        return 500;
    }

    public void onInitialize(VantaContext context) {
        this.context = context;
    }

    public void onClientTick(MinecraftClient client) {
        if (!this.context.config().enabled) {
            return;
        }

        VantaConfig.PerformanceSettings settings = this.context.config().performance;
        CombatSnapshot snapshot = this.context.snapshot();

        snapshot.setProfileName(this.context.config().profile.name());

        if (!settings.enabled) {
            snapshot.setCombatLoadHigh(false);
            snapshot.setReducedHudDetail(false);
            return;
        }

        boolean crystalLoad = snapshot.getNearbyCrystalCount() >= settings.highLoadCrystalThreshold;
        boolean minecartLoad = snapshot.getNearbyTntMinecartCount() >= settings.highLoadMinecartThreshold;
        boolean pingLoad = snapshot.isPingUnstable();
        boolean anchorLoad = snapshot.isTargetingAnchor();
        boolean flightLoad = snapshot.isFallFlying() && snapshot.getElytraSpeed() >= settings.highLoadElytraSpeed;
        boolean highLoad = crystalLoad || minecartLoad || pingLoad || anchorLoad || flightLoad;

        snapshot.setCombatLoadHigh(highLoad);
        snapshot.setReducedHudDetail(highLoad && settings.reduceHudDetailUnderLoad);
    }

    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        if (!this.context.config().enabled) {
            return;
        }

        VantaConfig.PerformanceSettings settings = this.context.config().performance;
        if (!settings.enabled) {
            return;
        }

        if (this.frameWindow.length != settings.frameP95Window) {
            this.frameWindow = new double[settings.frameP95Window];
            this.frameWindowCursor = 0;
            this.frameWindowSize = 0;
            this.p95RecomputeCursor = 0;
        }

        long now = System.nanoTime();
        if (this.lastFrameNanos == 0L) {
            this.lastFrameNanos = now;
            return;
        }

        double frameMs = (now - this.lastFrameNanos) / 1_000_000.0;
        this.lastFrameNanos = now;

        this.frameWindow[this.frameWindowCursor] = frameMs;
        this.frameWindowCursor = (this.frameWindowCursor + 1) % this.frameWindow.length;
        if (this.frameWindowSize < this.frameWindow.length) {
            this.frameWindowSize++;
        }

        CombatSnapshot snapshot = this.context.snapshot();
        double ema = snapshot.getFrameTimeEmaMs();
        if (ema <= 0.0) {
            ema = frameMs;
        } else {
            ema = ema * (1.0 - settings.frameEmaAlpha) + frameMs * settings.frameEmaAlpha;
        }

        snapshot.setFrameTimeEmaMs(ema);
        snapshot.setFpsEstimate((int) Math.max(1L, Math.round(1000.0 / Math.max(0.01, ema))));

        this.p95RecomputeCursor++;
        if (this.p95RecomputeCursor >= 12) {
            this.p95RecomputeCursor = 0;
            snapshot.setFrameTimeP95Ms(computeP95());
        }
    }

    private double computeP95() {
        if (this.frameWindowSize == 0) {
            return 0.0;
        }

        double[] sorted = Arrays.copyOf(this.frameWindow, this.frameWindowSize);
        Arrays.sort(sorted);
        int index = (int) Math.ceil(this.frameWindowSize * 0.95) - 1;
        if (index < 0) {
            index = 0;
        }
        if (index >= sorted.length) {
            index = sorted.length - 1;
        }
        return sorted[index];
    }
}
