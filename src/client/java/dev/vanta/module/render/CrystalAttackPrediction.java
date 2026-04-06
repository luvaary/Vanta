package dev.vanta.module.render;

import dev.vanta.config.VantaConfig;
import dev.vanta.core.VantaContext;
import dev.vanta.core.VantaRuntimeAccess;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.decoration.EndCrystalEntity;

public final class CrystalAttackPrediction {
    private static final Map<Integer, Long> PREDICTED_BREAK_TICKS = new HashMap<>();
    private static ClientWorld trackedWorld;

    private CrystalAttackPrediction() {
    }

    public static void recordAttack(EndCrystalEntity crystal) {
        if (crystal == null) {
            return;
        }

        VantaContext context = VantaRuntimeAccess.getContext();
        if (context == null) {
            return;
        }

        VantaConfig config = context.config();
        if (!config.enabled || !config.crystal.enabled || !config.crystal.instantBreakPredictionEnabled) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) {
            return;
        }

        ensureWorld(client.world);
        PREDICTED_BREAK_TICKS.put(crystal.getId(), client.world.getTime());
    }

    public static boolean shouldSuppressRender(EndCrystalEntity crystal) {
        if (crystal == null) {
            return false;
        }

        VantaContext context = VantaRuntimeAccess.getContext();
        if (context == null) {
            return false;
        }

        VantaConfig config = context.config();
        if (!config.enabled || !config.crystal.enabled || !config.crystal.instantBreakPredictionEnabled) {
            return false;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) {
            return false;
        }

        ensureWorld(client.world);

        int entityId = crystal.getId();
        Long predictedAtTick = PREDICTED_BREAK_TICKS.get(entityId);
        if (predictedAtTick == null) {
            return false;
        }

        if (client.world.getEntityById(entityId) == null) {
            PREDICTED_BREAK_TICKS.remove(entityId);
            return false;
        }

        long ageTicks = client.world.getTime() - predictedAtTick;
        if (ageTicks > config.crystal.predictionResyncTicks) {
            PREDICTED_BREAK_TICKS.remove(entityId);
            return false;
        }

        return true;
    }

    public static void onClientTick(MinecraftClient client) {
        if (client.world == null) {
            reset();
            return;
        }

        ensureWorld(client.world);

        VantaContext context = VantaRuntimeAccess.getContext();
        if (context == null) {
            return;
        }

        int resyncTicks = Math.max(1, context.config().crystal.predictionResyncTicks);
        long now = client.world.getTime();

        Iterator<Map.Entry<Integer, Long>> iterator = PREDICTED_BREAK_TICKS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Long> entry = iterator.next();
            if (now - entry.getValue() > resyncTicks || client.world.getEntityById(entry.getKey()) == null) {
                iterator.remove();
            }
        }
    }

    public static void reset() {
        trackedWorld = null;
        PREDICTED_BREAK_TICKS.clear();
    }

    private static void ensureWorld(ClientWorld world) {
        if (trackedWorld != world) {
            trackedWorld = world;
            PREDICTED_BREAK_TICKS.clear();
        }
    }
}
