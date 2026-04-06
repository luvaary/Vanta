package dev.vanta.module.render;

import dev.vanta.config.VantaConfig;
import dev.vanta.core.CombatSnapshot;
import dev.vanta.core.VantaContext;
import dev.vanta.core.VantaRuntimeAccess;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.util.Identifier;

public final class CombatVisualThrottle {
    private static final Map<String, Long> SOUND_LAST_PLAYED_MS = new HashMap<>();

    private static long particleWindowTick = Long.MIN_VALUE;
    private static int particleWindowCount;

    private CombatVisualThrottle() {
    }

    public static boolean shouldDropParticle() {
        VantaContext context = VantaRuntimeAccess.getContext();
        if (context == null) {
            return false;
        }

        VantaConfig config = context.config();
        if (!config.enabled || !config.performance.enabled || !config.performance.particleBudgetEnabled) {
            return false;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) {
            return false;
        }

        CombatSnapshot snapshot = context.snapshot();
        int budget = snapshot.isCombatLoadHigh()
                ? config.performance.maxParticlesPerTickCombat
                : config.performance.maxParticlesPerTickNormal;

        long currentTick = client.world.getTime();
        if (currentTick != particleWindowTick) {
            particleWindowTick = currentTick;
            particleWindowCount = 0;
        }

        particleWindowCount++;
        return particleWindowCount > budget;
    }

    public static boolean shouldDedupeSound(Identifier soundId) {
        if (soundId == null) {
            return false;
        }

        VantaContext context = VantaRuntimeAccess.getContext();
        if (context == null) {
            return false;
        }

        VantaConfig config = context.config();
        if (!config.enabled || !config.performance.enabled || !config.performance.soundDedupeEnabled) {
            return false;
        }

        CombatSnapshot snapshot = context.snapshot();
        int cooldown = snapshot.isCombatLoadHigh()
                ? config.performance.soundCooldownCombatMs
                : config.performance.soundCooldownNormalMs;

        if (cooldown <= 0) {
            return false;
        }

        long now = System.currentTimeMillis();
        String key = soundId.toString();
        Long last = SOUND_LAST_PLAYED_MS.get(key);
        SOUND_LAST_PLAYED_MS.put(key, now);

        if (SOUND_LAST_PLAYED_MS.size() > 512) {
            long trimBefore = now - 1_500L;
            Iterator<Map.Entry<String, Long>> iterator = SOUND_LAST_PLAYED_MS.entrySet().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getValue() < trimBefore) {
                    iterator.remove();
                }
            }
        }

        return last != null && now - last < cooldown;
    }

    public static boolean shouldCullTntMinecartRender(TntMinecartEntity minecart) {
        if (minecart == null) {
            return false;
        }

        VantaContext context = VantaRuntimeAccess.getContext();
        if (context == null) {
            return false;
        }

        VantaConfig config = context.config();
        if (!config.enabled || !config.minecart.enabled || !config.minecart.cullDenseMinecartRenders) {
            return false;
        }

        CombatSnapshot snapshot = context.snapshot();
        if (!snapshot.isTntMinecartHighDensity()) {
            return false;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return false;
        }

        double cullDistanceSq = config.minecart.denseRenderCullDistance * config.minecart.denseRenderCullDistance;
        return minecart.squaredDistanceTo(client.player) > cullDistanceSq;
    }

    public static void resetTemporalState() {
        SOUND_LAST_PLAYED_MS.clear();
        particleWindowTick = Long.MIN_VALUE;
        particleWindowCount = 0;
    }
}
