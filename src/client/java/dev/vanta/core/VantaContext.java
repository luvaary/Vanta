package dev.vanta.core;

import dev.vanta.compat.CompatibilityState;
import dev.vanta.config.VantaConfig;
import dev.vanta.config.VantaConfigManager;

public final class VantaContext {
    private final VantaConfigManager configManager;
    private final VantaConfig config;
    private final CombatSnapshot snapshot;
    private final CompatibilityState compatibility;

    public VantaContext(VantaConfigManager configManager, VantaConfig config, CombatSnapshot snapshot, CompatibilityState compatibility) {
        this.configManager = configManager;
        this.config = config;
        this.snapshot = snapshot;
        this.compatibility = compatibility;
    }

    public VantaConfigManager configManager() {
        return configManager;
    }

    public VantaConfig config() {
        return config;
    }

    public CombatSnapshot snapshot() {
        return snapshot;
    }

    public CompatibilityState compatibility() {
        return compatibility;
    }

    public void saveConfig() {
        this.configManager.save(this.config);
    }
}
