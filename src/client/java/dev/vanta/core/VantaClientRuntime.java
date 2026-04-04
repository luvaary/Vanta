package dev.vanta.core;

import dev.vanta.VantaMod;
import dev.vanta.compat.CompatibilityService;
import dev.vanta.config.VantaProfileApplier;
import dev.vanta.config.VantaConfig;
import dev.vanta.config.VantaConfigManager;
import dev.vanta.config.VantaProfile;
import dev.vanta.module.anchor.AnchorOptimizerModule;
import dev.vanta.module.crystal.CrystalOptimizerModule;
import dev.vanta.module.diagnostics.AutoBenchmarkModule;
import dev.vanta.module.elytra.ElytraOptimizerModule;
import dev.vanta.module.minecart.TntMinecartOptimizerModule;
import dev.vanta.module.ping.PingOptimizerModule;
import dev.vanta.module.performance.CombatPerformanceModule;
import dev.vanta.module.shield.ShieldOptimizerModule;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public final class VantaClientRuntime {
    private static final VantaClientRuntime INSTANCE = new VantaClientRuntime();

    private final VantaModuleRegistry moduleRegistry = new VantaModuleRegistry();
    private VantaContext context;
    private boolean initialized;

    private VantaClientRuntime() {
    }

    public static VantaClientRuntime getInstance() {
        return INSTANCE;
    }

    public void initialize() {
        if (this.initialized) {
            return;
        }

        VantaConfigManager configManager = new VantaConfigManager();
        VantaConfig config = new VantaConfig();
        VantaProfileApplier.apply(config, VantaProfile.COMPETITIVE);
        config.enabled = true;
        config.input.enabled = false;
        config.hud.enabled = false;
        config.performance.dropAllParticles = true;
        config.performance.soundCooldownNormalMs = Math.max(config.performance.soundCooldownNormalMs, 80);
        config.performance.soundCooldownCombatMs = Math.max(config.performance.soundCooldownCombatMs, 180);
        config.crystal.scanRange = Math.min(config.crystal.scanRange, 10.0);
        config.crystal.denseRenderCullDistance = Math.min(config.crystal.denseRenderCullDistance, 8.0);
        config.minecart.scanRange = Math.min(config.minecart.scanRange, 14.0);
        config.minecart.denseRenderCullDistance = Math.min(config.minecart.denseRenderCullDistance, 10.0);
        config.diagnostics.enabled = false;
        config.diagnostics.exportSvg = false;
        this.context = new VantaContext(configManager, config, new CombatSnapshot(), CompatibilityService.detect());
        VantaRuntimeAccess.setContext(this.context);

        registerModules();
        this.moduleRegistry.initialize(this.context);

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);

        this.initialized = true;
        VantaMod.LOGGER.info("Vanta renderer compatibility: {}", this.context.compatibility().describeRenderStack());
        VantaMod.LOGGER.info("Vanta client runtime initialized");
    }

    private void registerModules() {
        this.moduleRegistry.register(new CrystalOptimizerModule());
        this.moduleRegistry.register(new TntMinecartOptimizerModule());
        this.moduleRegistry.register(new PingOptimizerModule());
        this.moduleRegistry.register(new AnchorOptimizerModule());
        this.moduleRegistry.register(new ShieldOptimizerModule());
        this.moduleRegistry.register(new ElytraOptimizerModule());
        this.moduleRegistry.register(new CombatPerformanceModule());
        this.moduleRegistry.register(new AutoBenchmarkModule());
    }

    private void onClientTick(MinecraftClient client) {
        if (this.context == null) {
            return;
        }
        this.moduleRegistry.onClientTick(client);
    }
}
