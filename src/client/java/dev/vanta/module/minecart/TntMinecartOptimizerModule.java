package dev.vanta.module.minecart;

import dev.vanta.config.VantaConfig;
import dev.vanta.core.CombatSnapshot;
import dev.vanta.core.VantaContext;
import dev.vanta.core.VantaModule;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.vehicle.TntMinecartEntity;

public final class TntMinecartOptimizerModule implements VantaModule {
    private VantaContext context;
    private int scanCursor;

    public String id() {
        return "tnt_minecart_optimizer";
    }

    public int priority() {
        return 150;
    }

    public void onInitialize(VantaContext context) {
        this.context = context;
    }

    public void onClientTick(MinecraftClient client) {
        if (!this.context.config().enabled) {
            return;
        }

        VantaConfig.MinecartSettings settings = this.context.config().minecart;
        VantaConfig.PerformanceSettings performance = this.context.config().performance;
        CombatSnapshot snapshot = this.context.snapshot();

        if (!settings.enabled || client.player == null || client.world == null) {
            snapshot.setNearbyTntMinecartCount(0);
            snapshot.setNearestTntMinecartDistance(-1.0);
            snapshot.setTntMinecartHighDensity(false);
            return;
        }

        int interval = snapshot.isCombatLoadHigh()
                ? performance.minecartScanIntervalCombatTicks
                : performance.minecartScanIntervalNormalTicks;
        this.scanCursor++;
        if (this.scanCursor % interval != 0) {
            return;
        }

        ClientPlayerEntity player = client.player;
        List<TntMinecartEntity> minecarts = client.world.getEntitiesByClass(
                TntMinecartEntity.class,
                player.getBoundingBox().expand(settings.scanRange),
                minecart -> true
        );

        int count = minecarts.size();
        double nearestDistanceSquared = Double.POSITIVE_INFINITY;

        for (TntMinecartEntity minecart : minecarts) {
            double distanceSquared = minecart.squaredDistanceTo(player);
            if (distanceSquared < nearestDistanceSquared) {
                nearestDistanceSquared = distanceSquared;
            }
        }

        snapshot.setNearbyTntMinecartCount(count);
        snapshot.setNearestTntMinecartDistance(count > 0 ? Math.sqrt(nearestDistanceSquared) : -1.0);
        snapshot.setTntMinecartHighDensity(count >= settings.highDensityThreshold);
    }

    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
    }
}
