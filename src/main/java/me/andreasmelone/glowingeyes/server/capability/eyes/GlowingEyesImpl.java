package me.andreasmelone.glowingeyes.server.capability.eyes;

import net.minecraftforge.fml.loading.FMLPaths;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class GlowingEyesImpl implements IGlowingEyes {
    // Set to false by default to ensure no interference with the scoreboard
    private boolean toggledOn = false; 
    private boolean forcedByScore = false; 
    private HashMap<Point, Color> glowingEyesMap = new HashMap<>();
    private static final File PERSISTENT_FILE = new File(FMLPaths.CONFIGDIR.get().toFile(), "glowing_eyes_pattern.properties");

    public GlowingEyesImpl() {
        loadGlobalPattern();
    }

    @Nonnull
    @Override
    public HashMap<Point, Color> getGlowingEyesMap() {
        if (this.glowingEyesMap.isEmpty()) {
            loadGlobalPattern();
        }
        return this.glowingEyesMap;
    }

    @Override
    public void setGlowingEyesMap(@Nonnull HashMap<Point, Color> glowingEyesMap) {
        this.glowingEyesMap = glowingEyesMap;
        saveGlobalPattern();
    }

    @Override
    public boolean isToggledOn() {
        // Strictly return the score-based state
        return this.forcedByScore;
    }

    @Override
    public void setToggledOn(boolean toggledOn) {
        this.toggledOn = toggledOn;
    }

    @Override
    public boolean isForcedByScore() {
        return this.forcedByScore;
    }

    @Override
    public void setForcedByScore(boolean forcedByScore) {
        this.forcedByScore = forcedByScore;
    }

    private void saveGlobalPattern() {
        try {
            Properties props = new Properties();
            for (Map.Entry<Point, Color> entry : this.glowingEyesMap.entrySet()) {
                String key = entry.getKey().x + "," + entry.getKey().y;
                props.setProperty(key, String.valueOf(entry.getValue().getRGB()));
            }
            try (FileWriter writer = new FileWriter(PERSISTENT_FILE)) {
                props.store(writer, "Global Glowing Eyes Pixel Storage");
            }
        } catch (Exception ignored) {}
    }

    private void loadGlobalPattern() {
        if (!PERSISTENT_FILE.exists()) return;
        try {
            Properties props = new Properties();
            try (FileReader reader = new FileReader(PERSISTENT_FILE)) {
                props.load(reader);
            }
            for (String key : props.stringPropertyNames()) {
                String[] coords = key.split(",");
                Point p = new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
                Color c = new Color(Integer.parseInt(props.getProperty(key)), true);
                this.glowingEyesMap.put(p, c);
            }
        } catch (Exception ignored) {}
    }
}
