package dev.vanta.config;

public final class VantaProfileApplier {
    private VantaProfileApplier() {
    }

    public static void apply(VantaConfig config, VantaProfile profile) {
        config.profile = profile;

        if (profile == VantaProfile.COMPETITIVE) {
            config.crystal.scanRange = 12.0;
            config.crystal.highDensityThreshold = 8;
            config.crystal.cullDenseCrystalRenders = true;
            config.crystal.denseRenderCullDistance = 10.0;

            config.minecart.scanRange = 18.0;
            config.minecart.highDensityThreshold = 6;
            config.minecart.cullDenseMinecartRenders = true;
            config.minecart.denseRenderCullDistance = 14.0;

            config.performance.highLoadCrystalThreshold = 10;
            config.performance.highLoadMinecartThreshold = 6;
            config.performance.highLoadElytraSpeed = 25.0;
            config.performance.crystalScanIntervalNormalTicks = 1;
            config.performance.crystalScanIntervalCombatTicks = 3;
            config.performance.inventoryScanIntervalNormalTicks = 1;
            config.performance.inventoryScanIntervalCombatTicks = 4;
            config.performance.maxParticlesPerTickNormal = 320;
            config.performance.maxParticlesPerTickCombat = 140;
            config.performance.soundCooldownNormalMs = 40;
            config.performance.soundCooldownCombatMs = 95;

            config.ping.highPingThresholdMs = 95;
            config.ping.highJitterThresholdMs = 18;
            config.ping.jitterEmaAlpha = 0.25;

            config.hud.compactWhenHighLoad = true;
            config.hud.showCrosshairCues = true;
            config.hud.lowItemWarningThreshold = 2;
            return;
        }

        if (profile == VantaProfile.BALANCED) {
            config.crystal.scanRange = 13.0;
            config.crystal.highDensityThreshold = 10;
            config.crystal.cullDenseCrystalRenders = true;
            config.crystal.denseRenderCullDistance = 11.5;

            config.minecart.scanRange = 20.0;
            config.minecart.highDensityThreshold = 8;
            config.minecart.cullDenseMinecartRenders = true;
            config.minecart.denseRenderCullDistance = 16.0;

            config.performance.highLoadCrystalThreshold = 12;
            config.performance.highLoadMinecartThreshold = 8;
            config.performance.highLoadElytraSpeed = 28.0;
            config.performance.crystalScanIntervalNormalTicks = 2;
            config.performance.crystalScanIntervalCombatTicks = 4;
            config.performance.inventoryScanIntervalNormalTicks = 2;
            config.performance.inventoryScanIntervalCombatTicks = 5;
            config.performance.maxParticlesPerTickNormal = 380;
            config.performance.maxParticlesPerTickCombat = 220;
            config.performance.soundCooldownNormalMs = 28;
            config.performance.soundCooldownCombatMs = 72;

            config.ping.highPingThresholdMs = 110;
            config.ping.highJitterThresholdMs = 22;
            config.ping.jitterEmaAlpha = 0.22;

            config.hud.compactWhenHighLoad = true;
            config.hud.showCrosshairCues = true;
            config.hud.lowItemWarningThreshold = 2;
            return;
        }

        config.crystal.scanRange = 14.0;
        config.crystal.highDensityThreshold = 12;
        config.crystal.cullDenseCrystalRenders = false;
        config.crystal.denseRenderCullDistance = 14.0;

        config.minecart.scanRange = 22.0;
        config.minecart.highDensityThreshold = 10;
        config.minecart.cullDenseMinecartRenders = false;
        config.minecart.denseRenderCullDistance = 18.0;

        config.performance.highLoadCrystalThreshold = 14;
        config.performance.highLoadMinecartThreshold = 10;
        config.performance.highLoadElytraSpeed = 32.0;
        config.performance.crystalScanIntervalNormalTicks = 2;
        config.performance.crystalScanIntervalCombatTicks = 5;
        config.performance.inventoryScanIntervalNormalTicks = 2;
        config.performance.inventoryScanIntervalCombatTicks = 6;
        config.performance.maxParticlesPerTickNormal = 500;
        config.performance.maxParticlesPerTickCombat = 300;
        config.performance.soundCooldownNormalMs = 18;
        config.performance.soundCooldownCombatMs = 50;

        config.ping.highPingThresholdMs = 130;
        config.ping.highJitterThresholdMs = 26;
        config.ping.jitterEmaAlpha = 0.20;

        config.hud.compactWhenHighLoad = false;
        config.hud.showCrosshairCues = true;
        config.hud.lowItemWarningThreshold = 3;
    }

    public static VantaProfile next(VantaProfile current) {
        if (current == null) {
            return VantaProfile.COMPETITIVE;
        }
        if (current == VantaProfile.COMPETITIVE) {
            return VantaProfile.BALANCED;
        }
        if (current == VantaProfile.BALANCED) {
            return VantaProfile.CLARITY;
        }
        return VantaProfile.COMPETITIVE;
    }
}
