package dev.vanta.metrics;

import dev.vanta.VantaMod;
import dev.vanta.util.NumberFormats;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class BenchmarkSvgWriter {
    private BenchmarkSvgWriter() {
    }

    public static void write(Path file, List<BenchmarkSample> samples) {
        try {
            try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                writer.write(buildSvg(samples));
            }
        } catch (IOException ex) {
            VantaMod.LOGGER.warn("Failed to write benchmark SVG", ex);
        }
    }

    private static String buildSvg(List<BenchmarkSample> samples) {
        int width = 1200;
        int height = 760;

        int marginLeft = 72;
        int marginRight = 28;
        int fpsTop = 76;
        int chartWidth = width - marginLeft - marginRight;
        int fpsHeight = 240;
        int frameTop = 420;
        int frameHeight = 240;

        StringBuilder svg = new StringBuilder();
        svg.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"").append(width).append("\" height=\"").append(height).append("\" viewBox=\"0 0 ").append(width).append(' ').append(height).append("\">\n");
        svg.append("  <rect width=\"100%\" height=\"100%\" fill=\"#050505\"/>\n");
        svg.append("  <text x=\"72\" y=\"38\" fill=\"#f2f2f2\" font-family=\"Consolas,Menlo,monospace\" font-size=\"24\">Vanta Benchmark</text>\n");
        svg.append("  <text x=\"72\" y=\"60\" fill=\"#9a9a9a\" font-family=\"Consolas,Menlo,monospace\" font-size=\"13\">Top: FPS, Bottom: Frametime EMA/P95 (ms)</text>\n");

        svg.append("  <rect x=\"").append(marginLeft).append("\" y=\"").append(fpsTop).append("\" width=\"").append(chartWidth).append("\" height=\"").append(fpsHeight).append("\" fill=\"#0e0e0e\" stroke=\"#232323\"/>\n");
        svg.append("  <rect x=\"").append(marginLeft).append("\" y=\"").append(frameTop).append("\" width=\"").append(chartWidth).append("\" height=\"").append(frameHeight).append("\" fill=\"#0e0e0e\" stroke=\"#232323\"/>\n");

        appendGrid(svg, marginLeft, fpsTop, chartWidth, fpsHeight);
        appendGrid(svg, marginLeft, frameTop, chartWidth, frameHeight);

        if (samples == null || samples.isEmpty()) {
            svg.append("  <text x=\"72\" y=\"352\" fill=\"#c0c0c0\" font-family=\"Consolas,Menlo,monospace\" font-size=\"14\">No samples captured.</text>\n");
            svg.append("</svg>\n");
            return svg.toString();
        }

        double maxFps = 1.0;
        double maxFrame = 1.0;
        int maxMinecarts = 0;
        int maxCrystals = 0;
        int maxPing = 0;
        int maxJitter = 0;
        int highLoadSeconds = 0;

        for (BenchmarkSample sample : samples) {
            maxFps = Math.max(maxFps, sample.getFps());
            maxFrame = Math.max(maxFrame, Math.max(sample.getFrameEmaMs(), sample.getFrameP95Ms()));
            maxMinecarts = Math.max(maxMinecarts, sample.getTntMinecarts());
            maxCrystals = Math.max(maxCrystals, sample.getCrystals());
            maxPing = Math.max(maxPing, sample.getPingMs());
            maxJitter = Math.max(maxJitter, sample.getPingJitterMs());
            if (sample.isCombatLoadHigh()) {
                highLoadSeconds = Math.max(highLoadSeconds, sample.getElapsedSeconds());
            }
        }

        appendPath(svg, samples, marginLeft, chartWidth, fpsTop, fpsHeight, maxFps, 0, "#68d8ff", 2.5, true, false);
        appendPath(svg, samples, marginLeft, chartWidth, frameTop, frameHeight, maxFrame, 1, "#66ffb5", 2.0, false, false);
        appendPath(svg, samples, marginLeft, chartWidth, frameTop, frameHeight, maxFrame, 2, "#ff9a63", 2.0, false, true);

        svg.append("  <text x=\"78\" y=\"").append(fpsTop + 18).append("\" fill=\"#68d8ff\" font-family=\"Consolas,Menlo,monospace\" font-size=\"13\">FPS</text>\n");
        svg.append("  <text x=\"140\" y=\"").append(frameTop + 18).append("\" fill=\"#66ffb5\" font-family=\"Consolas,Menlo,monospace\" font-size=\"13\">Frame EMA</text>\n");
        svg.append("  <text x=\"250\" y=\"").append(frameTop + 18).append("\" fill=\"#ff9a63\" font-family=\"Consolas,Menlo,monospace\" font-size=\"13\">Frame P95</text>\n");

        BenchmarkSample last = samples.get(samples.size() - 1);
        int elapsed = last.getElapsedSeconds();
        svg.append("  <text x=\"72\" y=\"704\" fill=\"#d6d6d6\" font-family=\"Consolas,Menlo,monospace\" font-size=\"13\">Duration: ").append(elapsed).append("s</text>\n");
        svg.append("  <text x=\"240\" y=\"704\" fill=\"#d6d6d6\" font-family=\"Consolas,Menlo,monospace\" font-size=\"13\">Max crystals: ").append(maxCrystals).append("</text>\n");
        svg.append("  <text x=\"420\" y=\"704\" fill=\"#d6d6d6\" font-family=\"Consolas,Menlo,monospace\" font-size=\"13\">Max TNT minecarts: ").append(maxMinecarts).append("</text>\n");
        svg.append("  <text x=\"680\" y=\"704\" fill=\"#d6d6d6\" font-family=\"Consolas,Menlo,monospace\" font-size=\"13\">Last FPS: ").append(last.getFps()).append("</text>\n");
        svg.append("  <text x=\"820\" y=\"704\" fill=\"#d6d6d6\" font-family=\"Consolas,Menlo,monospace\" font-size=\"13\">Last EMA: ").append(NumberFormats.oneDecimal(last.getFrameEmaMs())).append("ms</text>\n");
        svg.append("  <text x=\"1020\" y=\"704\" fill=\"#d6d6d6\" font-family=\"Consolas,Menlo,monospace\" font-size=\"13\">Load>=").append(highLoadSeconds).append("s</text>\n");
        svg.append("  <text x=\"72\" y=\"726\" fill=\"#d6d6d6\" font-family=\"Consolas,Menlo,monospace\" font-size=\"13\">Max ping: ").append(maxPing).append("ms  Max jitter: ").append(maxJitter).append("ms</text>\n");

        svg.append("</svg>\n");
        return svg.toString();
    }

    private static void appendGrid(StringBuilder svg, int x, int y, int width, int height) {
        int hLines = 4;
        int vLines = 10;

        for (int i = 1; i < hLines; i++) {
            int gy = y + (height * i / hLines);
            svg.append("  <line x1=\"").append(x).append("\" y1=\"").append(gy).append("\" x2=\"").append(x + width).append("\" y2=\"").append(gy).append("\" stroke=\"#1f1f1f\" stroke-width=\"1\"/>\n");
        }

        for (int i = 1; i < vLines; i++) {
            int gx = x + (width * i / vLines);
            svg.append("  <line x1=\"").append(gx).append("\" y1=\"").append(y).append("\" x2=\"").append(gx).append("\" y2=\"").append(y + height).append("\" stroke=\"#171717\" stroke-width=\"1\"/>\n");
        }
    }

    private static void appendPath(StringBuilder svg, List<BenchmarkSample> samples, int chartLeft, int chartWidth, int chartTop, int chartHeight, double maxValue, int mode, String color, double strokeWidth, boolean smooth, boolean dashed) {
        if (samples.size() < 2) {
            return;
        }

        StringBuilder path = new StringBuilder();
        for (int i = 0; i < samples.size(); i++) {
            BenchmarkSample sample = samples.get(i);
            double raw;
            if (mode == 0) {
                raw = sample.getFps();
            } else if (mode == 1) {
                raw = sample.getFrameEmaMs();
            } else {
                raw = sample.getFrameP95Ms();
            }

            double norm = maxValue <= 0.0 ? 0.0 : raw / maxValue;
            double x = chartLeft + (chartWidth * i / (double) (samples.size() - 1));
            double y = chartTop + chartHeight - (chartHeight * norm);

            if (i == 0) {
                path.append('M').append(NumberFormats.oneDecimal(x)).append(' ').append(NumberFormats.oneDecimal(y));
            } else {
                if (smooth) {
                    BenchmarkSample prev = samples.get(i - 1);
                    double prevRaw;
                    if (mode == 0) {
                        prevRaw = prev.getFps();
                    } else if (mode == 1) {
                        prevRaw = prev.getFrameEmaMs();
                    } else {
                        prevRaw = prev.getFrameP95Ms();
                    }
                    double prevNorm = maxValue <= 0.0 ? 0.0 : prevRaw / maxValue;
                    double px = chartLeft + (chartWidth * (i - 1) / (double) (samples.size() - 1));
                    double py = chartTop + chartHeight - (chartHeight * prevNorm);
                    double cx = (px + x) / 2.0;
                    path.append(" Q").append(NumberFormats.oneDecimal(cx)).append(' ').append(NumberFormats.oneDecimal(py)).append(' ').append(NumberFormats.oneDecimal(x)).append(' ').append(NumberFormats.oneDecimal(y));
                } else {
                    path.append(" L").append(NumberFormats.oneDecimal(x)).append(' ').append(NumberFormats.oneDecimal(y));
                }
            }
        }

        svg.append("  <path d=\"").append(path).append("\" fill=\"none\" stroke=\"").append(color).append("\" stroke-width=\"").append(NumberFormats.oneDecimal(strokeWidth)).append("\"");
        if (dashed) {
            svg.append(" stroke-dasharray=\"8 6\"");
        }
        svg.append("/>\n");
    }
}
