package me.andreasmelone.glowingeyes.server.capability.eyes;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import java.awt.*;
import java.util.HashMap;

public class GlowingEyesCapability {

    // 1. RESTORED: This is the standard Forge 1.20.1 injection. 
    // If your project used a different variable name, change GLOWING_EYES to match it.
    @CapabilityInject(IGlowingEyes.class)
    public static Capability<IGlowingEyes> GLOWING_EYES = null;

    // 2. FIXED: Dynamic scoreboard check for the specific player being rendered
    public static boolean isToggledOn(Player player) {
        if (player == null) return false;

        // Check capability toggle
        boolean capToggle = player.getCapability(GLOWING_EYES)
                .map(IGlowingEyes::isToggledOn)
                .orElse(false);

        // Check scoreboard objective 'gloweyes'
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective("gloweyes");
        int scoreValue = 0;

        if (objective != null && scoreboard.hasPlayerScore(player.getScoreboardName(), objective)) {
            scoreValue = scoreboard.getOrCreatePlayerScore(player.getScoreboardName(), objective).getScore();
        }

        return capToggle && (scoreValue == 1);
    }

    // 3. RESTORED: Keep these EXACTLY as they were in your original project
    public static void setToggledOn(Player player, boolean toggled) {
        player.getCapability(GLOWING_EYES).ifPresent(cap -> cap.setToggledOn(toggled));
    }

    public static HashMap<Point, Color> getGlowingEyesMap(Player player) {
        return player.getCapability(GLOWING_EYES).map(IGlowingEyes::getGlowingEyesMap).orElse(new HashMap<>());
    }

    public static void setGlowingEyesMap(Player player, HashMap<Point, Color> map) {
        player.getCapability(GLOWING_EYES).ifPresent(cap -> cap.setGlowingEyesMap(map));
    }

    // 4. RESTORED: These methods must be put back to match your original project
    // (If you have a 'PacketHandler' class, call that here)
    public static void sendUpdate(Player player) {
        // CALL YOUR ORIGINAL PACKET SENDING METHOD HERE
    }

    public static void sendUpdate(Player target, Player receiver) {
        // CALL YOUR ORIGINAL PACKET SENDING METHOD HERE
    }
}
