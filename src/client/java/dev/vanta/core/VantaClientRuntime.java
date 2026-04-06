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
import dev.vanta.module.render.CombatVisualThrottle;
import dev.vanta.module.render.CrystalAttackPrediction;
import dev.vanta.module.shield.ShieldOptimizerModule;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

public final class VantaClientRuntime {
    private static final VantaClientRuntime INSTANCE = new VantaClientRuntime();

    private final VantaModuleRegistry moduleRegistry = new VantaModuleRegistry();
    private VantaContext context;
    private boolean initialized;
    private ClientWorld lastWorld;

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
        config.diagnostics.enabled = true;
        config.diagnostics.exportSvg = true;
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

        if (client.world != this.lastWorld) {
            this.lastWorld = client.world;
            CombatVisualThrottle.resetTemporalState();
            CrystalAttackPrediction.reset();
        }

        this.moduleRegistry.onClientTick(client);
    }
}
