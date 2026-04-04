package dev.vanta.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public interface VantaModule {
    String id();

    int priority();

    void onInitialize(VantaContext context);

    void onClientTick(MinecraftClient client);

    void onHudRender(DrawContext context, RenderTickCounter tickCounter);
}
