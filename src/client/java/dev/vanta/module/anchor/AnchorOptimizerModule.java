package dev.vanta.module.anchor;

import dev.vanta.config.VantaConfig;
import dev.vanta.core.CombatSnapshot;
import dev.vanta.core.VantaContext;
import dev.vanta.core.VantaModule;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public final class AnchorOptimizerModule implements VantaModule {
    private VantaContext context;

    public String id() {
        return "anchor_optimizer";
    }

    public int priority() {
        return 200;
    }

    public void onInitialize(VantaContext context) {
        this.context = context;
    }

    public void onClientTick(MinecraftClient client) {
        if (!this.context.config().enabled) {
            return;
        }

        VantaConfig.AnchorSettings settings = this.context.config().anchor;
        CombatSnapshot snapshot = this.context.snapshot();

        if (!settings.enabled || !settings.showTargetState || client.player == null || client.world == null) {
            snapshot.setTargetingAnchor(false);
            snapshot.setAnchorCharges(0);
            snapshot.setAnchorDistance(-1.0);
            return;
        }

        HitResult target = client.crosshairTarget;
        if (!(target instanceof BlockHitResult blockHitResult) || target.getType() != HitResult.Type.BLOCK) {
            snapshot.setTargetingAnchor(false);
            snapshot.setAnchorCharges(0);
            snapshot.setAnchorDistance(-1.0);
            return;
        }

        BlockPos pos = blockHitResult.getBlockPos();
        BlockState state = client.world.getBlockState(pos);

        if (!state.isOf(Blocks.RESPAWN_ANCHOR)) {
            snapshot.setTargetingAnchor(false);
            snapshot.setAnchorCharges(0);
            snapshot.setAnchorDistance(-1.0);
            return;
        }

        int charges = state.contains(RespawnAnchorBlock.CHARGES) ? state.get(RespawnAnchorBlock.CHARGES) : 0;
        double dx = client.player.getX() - (pos.getX() + 0.5);
        double dy = client.player.getEyeY() - (pos.getY() + 0.5);
        double dz = client.player.getZ() - (pos.getZ() + 0.5);
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        snapshot.setTargetingAnchor(true);
        snapshot.setAnchorCharges(charges);
        snapshot.setAnchorDistance(distance);
    }

    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
    }
}
