package dev.vanta.compat;

public final class CompatibilityState {
    private final boolean sodiumLoaded;
    private final boolean irisLoaded;
    private final boolean immediatelyFastLoaded;

    public CompatibilityState(boolean sodiumLoaded, boolean irisLoaded, boolean immediatelyFastLoaded) {
        this.sodiumLoaded = sodiumLoaded;
        this.irisLoaded = irisLoaded;
        this.immediatelyFastLoaded = immediatelyFastLoaded;
    }

    public boolean isSodiumLoaded() {
        return sodiumLoaded;
    }

    public boolean isIrisLoaded() {
        return irisLoaded;
    }

    public boolean isImmediatelyFastLoaded() {
        return immediatelyFastLoaded;
    }

    public String describeRenderStack() {
        StringBuilder builder = new StringBuilder();

        if (this.sodiumLoaded) {
            builder.append("SODIUM ");
        }
        if (this.irisLoaded) {
            builder.append("IRIS ");
        }
        if (this.immediatelyFastLoaded) {
            builder.append("IMMEDIATELYFAST ");
        }

        if (builder.length() == 0) {
            return "VANILLA";
        }

        return builder.toString().trim();
    }
}
