package dev.vanta.core;

public final class CombatSnapshot {
    private int nearbyCrystalCount;
    private double nearestCrystalDistance = -1.0;
    private boolean crystalHighDensity;
    private int nearbyTntMinecartCount;
    private double nearestTntMinecartDistance = -1.0;
    private boolean tntMinecartHighDensity;
    private int pingMs;
    private int pingJitterMs;
    private boolean pingUnstable;
    private boolean targetingAnchor;
    private int anchorCharges;
    private double anchorDistance = -1.0;
    private boolean hasShieldOffhand;
    private boolean shieldBlocking;
    private int shieldCooldownPercent;
    private long shieldBlockingDurationMs;
    private boolean fallFlying;
    private double elytraSpeed;
    private double elytraHorizontalSpeed;
    private float elytraPitch;
    private int elytraDurabilityRemaining;
    private int elytraDurabilityMax;
    private int totemCount;
    private int pearlCount;
    private int crystalItemCount;
    private int tntMinecartItemCount;
    private int anchorItemCount;
    private int gappleCount;
    private int obsidianCount;
    private int glowstoneCount;
    private double frameTimeEmaMs;
    private double frameTimeP95Ms;
    private int fpsEstimate;
    private boolean combatLoadHigh;
    private boolean reducedHudDetail;
    private int lastAttackIntervalMs;
    private int lastUseIntervalMs;
    private String profileName = "COMPETITIVE";
    private boolean benchmarkActive;
    private int benchmarkElapsedSeconds;

    public int getNearbyCrystalCount() {
        return nearbyCrystalCount;
    }

    public void setNearbyCrystalCount(int nearbyCrystalCount) {
        this.nearbyCrystalCount = nearbyCrystalCount;
    }

    public double getNearestCrystalDistance() {
        return nearestCrystalDistance;
    }

    public void setNearestCrystalDistance(double nearestCrystalDistance) {
        this.nearestCrystalDistance = nearestCrystalDistance;
    }

    public boolean isCrystalHighDensity() {
        return crystalHighDensity;
    }

    public void setCrystalHighDensity(boolean crystalHighDensity) {
        this.crystalHighDensity = crystalHighDensity;
    }

    public int getNearbyTntMinecartCount() {
        return nearbyTntMinecartCount;
    }

    public void setNearbyTntMinecartCount(int nearbyTntMinecartCount) {
        this.nearbyTntMinecartCount = nearbyTntMinecartCount;
    }

    public double getNearestTntMinecartDistance() {
        return nearestTntMinecartDistance;
    }

    public void setNearestTntMinecartDistance(double nearestTntMinecartDistance) {
        this.nearestTntMinecartDistance = nearestTntMinecartDistance;
    }

    public boolean isTntMinecartHighDensity() {
        return tntMinecartHighDensity;
    }

    public void setTntMinecartHighDensity(boolean tntMinecartHighDensity) {
        this.tntMinecartHighDensity = tntMinecartHighDensity;
    }

    public int getPingMs() {
        return pingMs;
    }

    public void setPingMs(int pingMs) {
        this.pingMs = pingMs;
    }

    public int getPingJitterMs() {
        return pingJitterMs;
    }

    public void setPingJitterMs(int pingJitterMs) {
        this.pingJitterMs = pingJitterMs;
    }

    public boolean isPingUnstable() {
        return pingUnstable;
    }

    public void setPingUnstable(boolean pingUnstable) {
        this.pingUnstable = pingUnstable;
    }

    public boolean isTargetingAnchor() {
        return targetingAnchor;
    }

    public void setTargetingAnchor(boolean targetingAnchor) {
        this.targetingAnchor = targetingAnchor;
    }

    public int getAnchorCharges() {
        return anchorCharges;
    }

    public void setAnchorCharges(int anchorCharges) {
        this.anchorCharges = anchorCharges;
    }

    public double getAnchorDistance() {
        return anchorDistance;
    }

    public void setAnchorDistance(double anchorDistance) {
        this.anchorDistance = anchorDistance;
    }

    public boolean hasShieldOffhand() {
        return hasShieldOffhand;
    }

    public void setHasShieldOffhand(boolean hasShieldOffhand) {
        this.hasShieldOffhand = hasShieldOffhand;
    }

    public boolean isShieldBlocking() {
        return shieldBlocking;
    }

    public void setShieldBlocking(boolean shieldBlocking) {
        this.shieldBlocking = shieldBlocking;
    }

    public int getShieldCooldownPercent() {
        return shieldCooldownPercent;
    }

    public void setShieldCooldownPercent(int shieldCooldownPercent) {
        this.shieldCooldownPercent = shieldCooldownPercent;
    }

    public long getShieldBlockingDurationMs() {
        return shieldBlockingDurationMs;
    }

    public void setShieldBlockingDurationMs(long shieldBlockingDurationMs) {
        this.shieldBlockingDurationMs = shieldBlockingDurationMs;
    }

    public boolean isFallFlying() {
        return fallFlying;
    }

    public void setFallFlying(boolean fallFlying) {
        this.fallFlying = fallFlying;
    }

    public double getElytraSpeed() {
        return elytraSpeed;
    }

    public void setElytraSpeed(double elytraSpeed) {
        this.elytraSpeed = elytraSpeed;
    }

    public double getElytraHorizontalSpeed() {
        return elytraHorizontalSpeed;
    }

    public void setElytraHorizontalSpeed(double elytraHorizontalSpeed) {
        this.elytraHorizontalSpeed = elytraHorizontalSpeed;
    }

    public float getElytraPitch() {
        return elytraPitch;
    }

    public void setElytraPitch(float elytraPitch) {
        this.elytraPitch = elytraPitch;
    }

    public int getElytraDurabilityRemaining() {
        return elytraDurabilityRemaining;
    }

    public void setElytraDurabilityRemaining(int elytraDurabilityRemaining) {
        this.elytraDurabilityRemaining = elytraDurabilityRemaining;
    }

    public int getElytraDurabilityMax() {
        return elytraDurabilityMax;
    }

    public void setElytraDurabilityMax(int elytraDurabilityMax) {
        this.elytraDurabilityMax = elytraDurabilityMax;
    }

    public int getTotemCount() {
        return totemCount;
    }

    public void setTotemCount(int totemCount) {
        this.totemCount = totemCount;
    }

    public int getPearlCount() {
        return pearlCount;
    }

    public void setPearlCount(int pearlCount) {
        this.pearlCount = pearlCount;
    }

    public int getCrystalItemCount() {
        return crystalItemCount;
    }

    public void setCrystalItemCount(int crystalItemCount) {
        this.crystalItemCount = crystalItemCount;
    }

    public int getTntMinecartItemCount() {
        return tntMinecartItemCount;
    }

    public void setTntMinecartItemCount(int tntMinecartItemCount) {
        this.tntMinecartItemCount = tntMinecartItemCount;
    }

    public int getAnchorItemCount() {
        return anchorItemCount;
    }

    public void setAnchorItemCount(int anchorItemCount) {
        this.anchorItemCount = anchorItemCount;
    }

    public int getGappleCount() {
        return gappleCount;
    }

    public void setGappleCount(int gappleCount) {
        this.gappleCount = gappleCount;
    }

    public int getObsidianCount() {
        return obsidianCount;
    }

    public void setObsidianCount(int obsidianCount) {
        this.obsidianCount = obsidianCount;
    }

    public int getGlowstoneCount() {
        return glowstoneCount;
    }

    public void setGlowstoneCount(int glowstoneCount) {
        this.glowstoneCount = glowstoneCount;
    }

    public double getFrameTimeEmaMs() {
        return frameTimeEmaMs;
    }

    public void setFrameTimeEmaMs(double frameTimeEmaMs) {
        this.frameTimeEmaMs = frameTimeEmaMs;
    }

    public double getFrameTimeP95Ms() {
        return frameTimeP95Ms;
    }

    public void setFrameTimeP95Ms(double frameTimeP95Ms) {
        this.frameTimeP95Ms = frameTimeP95Ms;
    }

    public int getFpsEstimate() {
        return fpsEstimate;
    }

    public void setFpsEstimate(int fpsEstimate) {
        this.fpsEstimate = fpsEstimate;
    }

    public boolean isCombatLoadHigh() {
        return combatLoadHigh;
    }

    public void setCombatLoadHigh(boolean combatLoadHigh) {
        this.combatLoadHigh = combatLoadHigh;
    }

    public boolean isReducedHudDetail() {
        return reducedHudDetail;
    }

    public void setReducedHudDetail(boolean reducedHudDetail) {
        this.reducedHudDetail = reducedHudDetail;
    }

    public int getLastAttackIntervalMs() {
        return lastAttackIntervalMs;
    }

    public void setLastAttackIntervalMs(int lastAttackIntervalMs) {
        this.lastAttackIntervalMs = lastAttackIntervalMs;
    }

    public int getLastUseIntervalMs() {
        return lastUseIntervalMs;
    }

    public void setLastUseIntervalMs(int lastUseIntervalMs) {
        this.lastUseIntervalMs = lastUseIntervalMs;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public boolean isBenchmarkActive() {
        return benchmarkActive;
    }

    public void setBenchmarkActive(boolean benchmarkActive) {
        this.benchmarkActive = benchmarkActive;
    }

    public int getBenchmarkElapsedSeconds() {
        return benchmarkElapsedSeconds;
    }

    public void setBenchmarkElapsedSeconds(int benchmarkElapsedSeconds) {
        this.benchmarkElapsedSeconds = benchmarkElapsedSeconds;
    }
}
