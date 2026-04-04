package dev.vanta.core;

public final class VantaRuntimeAccess {
    private static volatile VantaContext context;

    private VantaRuntimeAccess() {
    }

    public static void setContext(VantaContext runtimeContext) {
        context = runtimeContext;
    }

    public static VantaContext getContext() {
        return context;
    }
}
