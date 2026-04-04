package dev.vanta.metrics;

public final class BenchmarkSample {
    private final int elapsedSeconds;
    private final int fps;
    private final double frameEmaMs;
    private final double frameP95Ms;
    private final int crystals;
    private final int tntMinecarts;
    private final int pingMs;
    private final int pingJitterMs;
    private final boolean anchorTarget;
    private final double elytraSpeed;
    private final boolean combatLoadHigh;

    public BenchmarkSample(int elapsedSeconds, int fps, double frameEmaMs, double frameP95Ms, int crystals, int tntMinecarts, int pingMs, int pingJitterMs, boolean anchorTarget, double elytraSpeed, boolean combatLoadHigh) {
        this.elapsedSeconds = elapsedSeconds;
        this.fps = fps;
        this.frameEmaMs = frameEmaMs;
        this.frameP95Ms = frameP95Ms;
        this.crystals = crystals;
        this.tntMinecarts = tntMinecarts;
        this.pingMs = pingMs;
        this.pingJitterMs = pingJitterMs;
        this.anchorTarget = anchorTarget;
        this.elytraSpeed = elytraSpeed;
        this.combatLoadHigh = combatLoadHigh;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public int getFps() {
        return fps;
    }

    public double getFrameEmaMs() {
        return frameEmaMs;
    }

    public double getFrameP95Ms() {
        return frameP95Ms;
    }

    public int getCrystals() {
        return crystals;
    }

    public int getTntMinecarts() {
        return tntMinecarts;
    }

    public int getPingMs() {
        return pingMs;
    }

    public int getPingJitterMs() {
        return pingJitterMs;
    }

    public boolean isAnchorTarget() {
        return anchorTarget;
    }

    public double getElytraSpeed() {
        return elytraSpeed;
    }

    public boolean isCombatLoadHigh() {
        return combatLoadHigh;
    }
}
