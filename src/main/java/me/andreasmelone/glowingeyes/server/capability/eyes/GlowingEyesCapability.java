package me.andreasmelone.glowingeyes.server.capability.eyes;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import java.awt.*;
import java.util.HashMap;

public class GlowingEyesCapability {

    // RESTORED: Your original way of holding the capability reference
    public static final Capability<IGlowingEyes> INSTANCE = CapabilityToken.get(new CapabilityToken<>(){});

    public static boolean isToggledOn(Player player) {
        if (player == null) return false;

        boolean capToggle = player.getCapability(INSTANCE)
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

    // RESTORE THESE EXACTLY AS THEY WERE IN YOUR OLD FILE
    public static void setToggledOn(Player player, boolean toggled) {
        player.getCapability(INSTANCE).ifPresent(cap -> cap.setToggledOn(toggled));
    }

    public static HashMap<Point, Color> getGlowingEyesMap(Player player) {
        return player.getCapability(INSTANCE).map(IGlowingEyes::getGlowingEyesMap).orElse(new HashMap<>());
    }

    public static void setGlowingEyesMap(Player player, HashMap<Point, Color> map) {
        player.getCapability(INSTANCE).ifPresent(cap -> cap.setGlowingEyesMap(map));
    }
    
    // RESTORE YOUR ORIGINAL SYNC METHODS
    public static void sendUpdate(Player player) { /* YOUR ORIGINAL LOGIC */ }
    public static void sendUpdate(Player target, Player receiver) { /* YOUR ORIGINAL LOGIC */ }
    public static void register(net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent event) {
        event.register(IGlowingEyes.class);
    }
}
