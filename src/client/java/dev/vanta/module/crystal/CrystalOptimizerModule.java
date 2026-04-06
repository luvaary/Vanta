package dev.vanta.module.crystal;

import dev.vanta.config.VantaConfig;
import dev.vanta.core.CombatSnapshot;
import dev.vanta.core.VantaContext;
import dev.vanta.core.VantaModule;
import dev.vanta.module.render.CrystalAttackPrediction;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.decoration.EndCrystalEntity;

public final class CrystalOptimizerModule implements VantaModule {
    private VantaContext context;
    private int scanCursor;

    public String id() {
        return "crystal_optimizer";
    }

    public int priority() {
        return 100;
    }

    public void onInitialize(VantaContext context) {
        this.context = context;
    }

    public void onClientTick(MinecraftClient client) {
        if (!this.context.config().enabled) {
            return;
        }

        CrystalAttackPrediction.onClientTick(client);

        VantaConfig.CrystalSettings settings = this.context.config().crystal;
        VantaConfig.PerformanceSettings performance = this.context.config().performance;
        CombatSnapshot snapshot = this.context.snapshot();

        if (!settings.enabled || client.player == null || client.world == null) {
            snapshot.setNearbyCrystalCount(0);
            snapshot.setNearestCrystalDistance(-1.0);
            snapshot.setCrystalHighDensity(false);
            return;
        }

        int interval = snapshot.isCombatLoadHigh()
                ? performance.crystalScanIntervalCombatTicks
                : performance.crystalScanIntervalNormalTicks;
        this.scanCursor++;
        if (this.scanCursor % interval != 0) {
            return;
        }

        ClientPlayerEntity player = client.player;
        List<EndCrystalEntity> crystals = client.world.getEntitiesByClass(
                EndCrystalEntity.class,
                player.getBoundingBox().expand(settings.scanRange),
                crystal -> true
        );

        int count = crystals.size();
        double nearestDistanceSquared = Double.POSITIVE_INFINITY;

        for (EndCrystalEntity crystal : crystals) {
            double distanceSquared = crystal.squaredDistanceTo(player);
            if (distanceSquared < nearestDistanceSquared) {
                nearestDistanceSquared = distanceSquared;
            }
        }

        snapshot.setNearbyCrystalCount(count);
        snapshot.setNearestCrystalDistance(count > 0 ? Math.sqrt(nearestDistanceSquared) : -1.0);
        snapshot.setCrystalHighDensity(count >= settings.highDensityThreshold);
    }

    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
    }
}
