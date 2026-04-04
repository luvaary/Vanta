package dev.vanta.util;

public final class NumberFormats {
    private NumberFormats() {
    }

    public static String oneDecimal(double value) {
        long scaled = Math.round(value * 10.0);
        long whole = scaled / 10;
        long fraction = Math.abs(scaled % 10);
        return whole + "." + fraction;
    }
}
