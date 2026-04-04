package dev.vanta.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.vanta.VantaMod;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public final class VantaConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path configPath;

    public VantaConfigManager() {
        this.configPath = FabricLoader.getInstance().getConfigDir().resolve("vanta.json");
    }

    public VantaConfig load() {
        if (Files.exists(this.configPath)) {
            try (Reader reader = Files.newBufferedReader(this.configPath, StandardCharsets.UTF_8)) {
                VantaConfig config = GSON.fromJson(reader, VantaConfig.class);
                if (config != null) {
                    return sanitize(config);
                }
            } catch (IOException | RuntimeException ex) {
                VantaMod.LOGGER.warn("Failed to read config, using defaults", ex);
            }
        }

        VantaConfig defaults = sanitize(new VantaConfig());
        save(defaults);
        return defaults;
    }

    public void save(VantaConfig config) {
        VantaConfig sanitized = sanitize(config);

        try {
            Path parent = this.configPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (Writer writer = Files.newBufferedWriter(this.configPath, StandardCharsets.UTF_8)) {
                GSON.toJson(sanitized, writer);
            }
        } catch (IOException ex) {
            VantaMod.LOGGER.warn("Failed to save config", ex);
        }
    }

    private VantaConfig sanitize(VantaConfig config) {
        if (config.profile == null) {
            config.profile = VantaProfile.COMPETITIVE;
        }
        if (config.crystal == null) {
            config.crystal = new VantaConfig.CrystalSettings();
        }
        if (config.minecart == null) {
            config.minecart = new VantaConfig.MinecartSettings();
        }
        if (config.ping == null) {
            config.ping = new VantaConfig.PingSettings();
        }
        if (config.anchor == null) {
            config.anchor = new VantaConfig.AnchorSettings();
        }
        if (config.shield == null) {
            config.shield = new VantaConfig.ShieldSettings();
        }
        if (config.elytra == null) {
            config.elytra = new VantaConfig.ElytraSettings();
        }
        if (config.input == null) {
            config.input = new VantaConfig.InputSettings();
        }
        if (config.hud == null) {
            config.hud = new VantaConfig.HudSettings();
        }
        if (config.performance == null) {
            config.performance = new VantaConfig.PerformanceSettings();
        }
        if (config.diagnostics == null) {
            config.diagnostics = new VantaConfig.DiagnosticsSettings();
        }

        config.crystal.scanRange = clamp(config.crystal.scanRange, 4.0, 24.0);
        config.crystal.highDensityThreshold = clamp(config.crystal.highDensityThreshold, 1, 64);
        config.crystal.denseRenderCullDistance = clamp(config.crystal.denseRenderCullDistance, 4.0, 32.0);

        config.minecart.scanRange = clamp(config.minecart.scanRange, 6.0, 40.0);
        config.minecart.highDensityThreshold = clamp(config.minecart.highDensityThreshold, 1, 64);
        config.minecart.denseRenderCullDistance = clamp(config.minecart.denseRenderCullDistance, 4.0, 48.0);

        config.ping.highPingThresholdMs = clamp(config.ping.highPingThresholdMs, 20, 500);
        config.ping.highJitterThresholdMs = clamp(config.ping.highJitterThresholdMs, 2, 200);
        config.ping.jitterEmaAlpha = clamp(config.ping.jitterEmaAlpha, 0.05, 1.0);

        config.hud.width = clamp(config.hud.width, 160, 480);
        config.hud.lineHeight = clamp(config.hud.lineHeight, 8, 18);
        config.hud.padding = clamp(config.hud.padding, 2, 10);
        config.hud.lowItemWarningThreshold = clamp(config.hud.lowItemWarningThreshold, 0, 32);

        config.performance.frameEmaAlpha = clamp(config.performance.frameEmaAlpha, 0.01, 1.0);
        config.performance.highLoadCrystalThreshold = clamp(config.performance.highLoadCrystalThreshold, 1, 128);
        config.performance.highLoadMinecartThreshold = clamp(config.performance.highLoadMinecartThreshold, 1, 128);
        config.performance.highLoadElytraSpeed = clamp(config.performance.highLoadElytraSpeed, 4.0, 120.0);
        config.performance.crystalScanIntervalNormalTicks = clamp(config.performance.crystalScanIntervalNormalTicks, 1, 20);
        config.performance.crystalScanIntervalCombatTicks = clamp(config.performance.crystalScanIntervalCombatTicks, 1, 60);
        config.performance.inventoryScanIntervalNormalTicks = clamp(config.performance.inventoryScanIntervalNormalTicks, 1, 20);
        config.performance.inventoryScanIntervalCombatTicks = clamp(config.performance.inventoryScanIntervalCombatTicks, 1, 40);
        config.performance.frameP95Window = clamp(config.performance.frameP95Window, 30, 600);
        config.performance.maxParticlesPerTickNormal = clamp(config.performance.maxParticlesPerTickNormal, 32, 4096);
        config.performance.maxParticlesPerTickCombat = clamp(config.performance.maxParticlesPerTickCombat, 16, 4096);
        config.performance.soundCooldownNormalMs = clamp(config.performance.soundCooldownNormalMs, 0, 1000);
        config.performance.soundCooldownCombatMs = clamp(config.performance.soundCooldownCombatMs, 0, 2000);

        config.diagnostics.benchmarkSampleIntervalTicks = clamp(config.diagnostics.benchmarkSampleIntervalTicks, 1, 200);

        return config;
    }

    private static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    private static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }
}
