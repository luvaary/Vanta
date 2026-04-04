package dev.vanta.config;

public final class VantaConfig {
    public boolean enabled = true;
    public VantaProfile profile = VantaProfile.COMPETITIVE;
    public CrystalSettings crystal = new CrystalSettings();
    public MinecartSettings minecart = new MinecartSettings();
    public PingSettings ping = new PingSettings();
    public AnchorSettings anchor = new AnchorSettings();
    public ShieldSettings shield = new ShieldSettings();
    public ElytraSettings elytra = new ElytraSettings();
    public InputSettings input = new InputSettings();
    public HudSettings hud = new HudSettings();
    public PerformanceSettings performance = new PerformanceSettings();
    public DiagnosticsSettings diagnostics = new DiagnosticsSettings();

    public static final class CrystalSettings {
        public boolean enabled = true;
        public double scanRange = 12.0;
        public int highDensityThreshold = 8;
        public boolean cullDenseCrystalRenders = true;
        public double denseRenderCullDistance = 10.0;
    }

    public static final class AnchorSettings {
        public boolean enabled = true;
        public boolean showTargetState = true;
    }

    public static final class PingSettings {
        public boolean enabled = true;
        public int highPingThresholdMs = 95;
        public int highJitterThresholdMs = 18;
        public double jitterEmaAlpha = 0.25;
    }

    public static final class MinecartSettings {
        public boolean enabled = true;
        public double scanRange = 18.0;
        public int highDensityThreshold = 6;
        public boolean cullDenseMinecartRenders = true;
        public double denseRenderCullDistance = 14.0;
    }

    public static final class ShieldSettings {
        public boolean enabled = true;
        public boolean trackOffhand = true;
    }

    public static final class ElytraSettings {
        public boolean enabled = true;
        public boolean showFlightStats = true;
    }

    public static final class HudSettings {
        public boolean enabled = true;
        public int x = 6;
        public int y = 6;
        public int width = 248;
        public int lineHeight = 10;
        public int padding = 4;
        public int lowItemWarningThreshold = 2;
        public boolean compactWhenHighLoad = true;
        public boolean showCrosshairCues = true;
        public boolean showCompatibility = true;
        public int backgroundColor = 0x96000000;
        public int accentColor = 0xFF101010;
        public int textColor = 0xFFE8E8E8;
        public int dimTextColor = 0xFF9C9C9C;
        public int warningColor = 0xFFFF6464;
    }

    public static final class InputSettings {
        public boolean enabled = true;
        public boolean actionBarNotifications = true;
    }

    public static final class PerformanceSettings {
        public boolean enabled = true;
        public double frameEmaAlpha = 0.17;
        public int highLoadCrystalThreshold = 10;
        public int highLoadMinecartThreshold = 6;
        public double highLoadElytraSpeed = 25.0;
        public boolean reduceHudDetailUnderLoad = true;
        public int crystalScanIntervalNormalTicks = 1;
        public int crystalScanIntervalCombatTicks = 3;
        public int inventoryScanIntervalNormalTicks = 1;
        public int inventoryScanIntervalCombatTicks = 4;
        public int frameP95Window = 180;
        public boolean dropAllParticles = true;
        public boolean particleBudgetEnabled = true;
        public int maxParticlesPerTickNormal = 320;
        public int maxParticlesPerTickCombat = 140;
        public boolean soundDedupeEnabled = true;
        public int soundCooldownNormalMs = 40;
        public int soundCooldownCombatMs = 95;
    }

    public static final class DiagnosticsSettings {
        public boolean enabled = true;
        public int benchmarkSampleIntervalTicks = 20;
        public boolean exportSvg = true;
    }
}
