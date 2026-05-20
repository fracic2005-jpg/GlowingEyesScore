package me.andreasmelone.glowingeyes.server.capability.eyes;

import me.andreasmelone.glowingeyes.GlowingEyes;
import me.andreasmelone.glowingeyes.server.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Properties;

public class GlowingEyesImpl implements IGlowingEyes {
    private boolean toggledOn = true;
    private HashMap<Point, Color> glowingEyesMap = new HashMap<>();
    private static final File PERSISTENT_FILE = new File(FMLPaths.CONFIGDIR.get().toFile(), "glowing_eyes_pattern.properties");

    public GlowingEyesImpl() {
        // Automatically load your cross-world pattern from the global config folder on init
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
        // Save your newly drawn pattern to the global file whenever the GUI saves it
        saveGlobalPattern();
    }

    @Override
    public boolean isToggledOn() {
        Player targetedPlayer = Minecraft.getInstance().player;

        if (targetedPlayer != null) {
            Scoreboard scoreboard = targetedPlayer.getScoreboard();
            Objective objective = scoreboard.getObjective("gloweyes");
            int scoreValue = 0;

            if (objective != null && scoreboard.hasPlayerScore(targetedPlayer.getScoreboardName(), objective)) {
                scoreValue = scoreboard.getOrCreatePlayerScore(targetedPlayer.getScoreboardName(), objective).getScore();
            }

            return toggledOn && (scoreValue == 1);
        }
        
        return toggledOn;
    }

    @Override
    public void setToggledOn(boolean toggledOn) {
        this.toggledOn = toggledOn;
    }

    // Helper to save pattern across all worlds using standard flat property layout
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

    // Helper to read pattern across all worlds
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
