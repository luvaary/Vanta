package dev.vanta.metrics;

import dev.vanta.VantaMod;
import dev.vanta.config.VantaConfig;
import dev.vanta.core.CombatSnapshot;
import dev.vanta.util.NumberFormats;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.loader.api.FabricLoader;

public final class BenchmarkRecorder {
    private static final DateTimeFormatter FILE_TIME = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private final List<BenchmarkSample> samples = new ArrayList<>();
    private boolean active;
    private long startedAtMs;
    private int tickCounter;
    private boolean exportSvg = true;

    public boolean isActive() {
        return this.active;
    }

    public void start() {
        this.samples.clear();
        this.startedAtMs = System.currentTimeMillis();
        this.tickCounter = 0;
        this.active = true;
    }

    public Path stopAndExport() {
        this.active = false;

        Path directory = FabricLoader.getInstance().getConfigDir().resolve("vanta-benchmarks");
        String fileName = "vanta_benchmark_" + FILE_TIME.format(LocalDateTime.now()) + ".csv";
        Path file = directory.resolve(fileName);

        try {
            Files.createDirectories(directory);
            try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                writer.write("time_s,fps,frame_ema_ms,frame_p95_ms,crystals,tnt_minecarts,ping_ms,ping_jitter_ms,anchor_target,elytra_speed_bps,combat_load_high\n");

                for (BenchmarkSample sample : this.samples) {
                    writer.write(Integer.toString(sample.getElapsedSeconds()));
                    writer.write(',');
                    writer.write(Integer.toString(sample.getFps()));
                    writer.write(',');
                    writer.write(NumberFormats.oneDecimal(sample.getFrameEmaMs()));
                    writer.write(',');
                    writer.write(NumberFormats.oneDecimal(sample.getFrameP95Ms()));
                    writer.write(',');
                    writer.write(Integer.toString(sample.getCrystals()));
                    writer.write(',');
                    writer.write(Integer.toString(sample.getTntMinecarts()));
                    writer.write(',');
                    writer.write(Integer.toString(sample.getPingMs()));
                    writer.write(',');
                    writer.write(Integer.toString(sample.getPingJitterMs()));
                    writer.write(',');
                    writer.write(Boolean.toString(sample.isAnchorTarget()));
                    writer.write(',');
                    writer.write(NumberFormats.oneDecimal(sample.getElytraSpeed()));
                    writer.write(',');
                    writer.write(Boolean.toString(sample.isCombatLoadHigh()));
                    writer.write('\n');
                }
            }

            if (this.exportSvg) {
                String svgName = fileName.substring(0, fileName.length() - 4) + ".svg";
                BenchmarkSvgWriter.write(directory.resolve(svgName), this.samples);
            }

            return file;
        } catch (IOException ex) {
            VantaMod.LOGGER.warn("Failed to export benchmark", ex);
            return null;
        }
    }

    public void onClientTick(CombatSnapshot snapshot, VantaConfig.DiagnosticsSettings diagnosticsSettings) {
        this.exportSvg = diagnosticsSettings.exportSvg;

        if (!this.active) {
            snapshot.setBenchmarkActive(false);
            snapshot.setBenchmarkElapsedSeconds(0);
            return;
        }

        int elapsedSeconds = (int) ((System.currentTimeMillis() - this.startedAtMs) / 1000L);
        snapshot.setBenchmarkActive(true);
        snapshot.setBenchmarkElapsedSeconds(elapsedSeconds);

        this.tickCounter++;
        if (this.tickCounter % diagnosticsSettings.benchmarkSampleIntervalTicks != 0) {
            return;
        }

        this.samples.add(new BenchmarkSample(
                elapsedSeconds,
                snapshot.getFpsEstimate(),
                snapshot.getFrameTimeEmaMs(),
                snapshot.getFrameTimeP95Ms(),
                snapshot.getNearbyCrystalCount(),
                snapshot.getNearbyTntMinecartCount(),
            snapshot.getPingMs(),
            snapshot.getPingJitterMs(),
                snapshot.isTargetingAnchor(),
                snapshot.getElytraSpeed(),
                snapshot.isCombatLoadHigh()
        ));
    }
}
