package me.andreasmelone.glowingeyes.server.capability.eyes;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import java.awt.*;
import java.util.HashMap;

public class GlowingEyesCapability {
    // 1. RESTORE YOUR ORIGINAL CONSTANTS
    public static final Capability<IGlowingEyes> GLOWING_EYES = CapabilityManager.get(new CapabilityToken<>(){});

    // 2. ADD THE NEW DYNAMIC TOGGLE CHECK
    public static boolean isToggledOn(Player player) {
        if (player == null) return false;

        boolean capToggle = player.getCapability(GLOWING_EYES)
                .map(IGlowingEyes::isToggledOn)
                .orElse(false);

        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective("gloweyes");
        int scoreValue = 0;

        if (objective != null && scoreboard.hasPlayerScore(player.getScoreboardName(), objective)) {
            scoreValue = scoreboard.getOrCreatePlayerScore(player.getScoreboardName(), objective).getScore();
        }

        return capToggle && (scoreValue == 1);
    }

    // 3. RESTORE YOUR ORIGINAL METHODS (DO NOT DELETE THESE)
    public static void setToggledOn(Player player, boolean toggled) {
        player.getCapability(GLOWING_EYES).ifPresent(cap -> cap.setToggledOn(toggled));
    }

    public static HashMap<Point, Color> getGlowingEyesMap(Player player) {
        return player.getCapability(GLOWING_EYES).map(IGlowingEyes::getGlowingEyesMap).orElse(new HashMap<>());
    }

    public static void setGlowingEyesMap(Player player, HashMap<Point, Color> map) {
        player.getCapability(GLOWING_EYES).ifPresent(cap -> cap.setGlowingEyesMap(map));
    }

    // RESTORE YOUR ORIGINAL PACKET/SYNC METHODS
    public static void sendUpdate(Player player) { /* YOUR ORIGINAL PACKET LOGIC HERE */ }
    public static void sendUpdate(Player target, Player receiver) { /* YOUR ORIGINAL PACKET LOGIC HERE */ }
    public static void register(RegisterCapabilitiesEvent event) { /* YOUR ORIGINAL REGISTRATION */ }
}
