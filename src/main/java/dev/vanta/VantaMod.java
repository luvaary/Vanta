package dev.vanta;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class VantaMod implements ModInitializer {
    public static final String MOD_ID = "vanta";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public void onInitialize() {
        LOGGER.info("Vanta initialized");
    }
}
