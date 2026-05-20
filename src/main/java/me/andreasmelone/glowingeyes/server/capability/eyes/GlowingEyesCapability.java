package me.andreasmelone.glowingeyes.server.capability.eyes;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import java.awt.*;
import java.util.HashMap;

public class GlowingEyesCapability {

    // ... Keep your existing GLOWING_EYES registration and Provider hooks here ...

    public static boolean isToggledOn(Player player) {
        if (player == null) return false;

        // 1. Grab the raw capability toggle state safely
        boolean capToggle = player.getCapability(GlowingEyesProvider.GLOWING_EYES)
                .map(IGlowingEyes::isToggledOn)
                .orElse(false);

        // 2. Dynamically grab the exact scoreboard data for the player currently being rendered
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective("gloweyes");
        int scoreValue = 0;

        if (objective != null && scoreboard.hasPlayerScore(player.getScoreboardName(), objective)) {
            scoreValue = scoreboard.getOrCreatePlayerScore(player.getScoreboardName(), objective).getScore();
        }

        // Both conditions must pass to make the layer visible
        return capToggle && (scoreValue == 1);
    }

    public static HashMap<Point, Color> getGlowingEyesMap(Player player) {
        return player.getCapability(GlowingEyesProvider.GLOWING_EYES)
                .map(IGlowingEyes::getGlowingEyesMap)
                .orElse(new HashMap<>());
    }

    public static void setGlowingEyesMap(Player player, HashMap<Point, Color> map) {
        player.getCapability(GlowingEyesProvider.GLOWING_EYES).ifPresent(cap -> cap.setGlowingEyesMap(map));
    }
    
    // ... Keep your existing sendUpdate(player) or other network syncing methods below ...
}
