package dev.vanta.mixin.client;

import dev.vanta.config.VantaConfig;
import dev.vanta.core.CombatSnapshot;
import dev.vanta.core.VantaContext;
import dev.vanta.core.VantaRuntimeAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndCrystalEntityRenderer.class)
public abstract class EndCrystalEntityRendererMixin {
    @Inject(method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void vanta$cullDenseCrystalRenders(EndCrystalEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        VantaContext context = VantaRuntimeAccess.getContext();
        if (context == null) {
            return;
        }

        VantaConfig config = context.config();
        if (!config.enabled || !config.crystal.enabled || !config.crystal.cullDenseCrystalRenders) {
            return;
        }

        CombatSnapshot snapshot = context.snapshot();
        if (!snapshot.isCrystalHighDensity()) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return;
        }

        double cullDistanceSq = config.crystal.denseRenderCullDistance * config.crystal.denseRenderCullDistance;
        if (entity.squaredDistanceTo(client.player) > cullDistanceSq) {
            ci.cancel();
        }
    }
}
