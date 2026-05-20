package me.andreasmelone.glowingeyes.server.capability.eyes;

import me.andreasmelone.glowingeyes.GlowingEyes;
import me.andreasmelone.glowingeyes.server.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player; // Added import to find player scoreboard context
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;

import javax.annotation.Nonnull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.UUID;

public class GlowingEyesImpl implements IGlowingEyes {
    private boolean toggledOn = true;
    private HashMap<Point, Color> glowingEyesMap = new HashMap<>();

    @Nonnull
    @Override
    public HashMap<Point, Color> getGlowingEyesMap() {
        return this.glowingEyesMap;
    }

    @Override
    public void setGlowingEyesMap(@Nonnull HashMap<Point, Color> glowingEyesMap) {
        // When updating the map via capability syncing, it populates correctly for all clients
        this.glowingEyesMap = glowingEyesMap;
    }

    @Override
    public boolean isToggledOn() {
        // We override this to seamlessly integrate with your existing rendering logic.
        // It checks if we are on the client side, reads the local player's tracked scoreboard,
        // and checks if the 'gloweyes' objective matches exactly 1.
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            Scoreboard scoreboard = player.getScoreboard();
            Objective objective = scoreboard.getObjective("gloweyes");
            int scoreValue = 0;

            if (objective != null && scoreboard.hasPlayerScore(player.getScoreboardName(), objective)) {
                scoreValue = scoreboard.getOrCreatePlayerScore(player.getScoreboardName(), objective).getScore();
            }

            // The eyes will only glow if the capability itself is enabled AND the scoreboard score is 1
            return toggledOn && (scoreValue == 1);
        }
        
        return toggledOn;
    }

    @Override
    public void setToggledOn(boolean toggledOn) {
        this.toggledOn = toggledOn;
    }
}
