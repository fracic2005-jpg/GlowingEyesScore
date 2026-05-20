package me.andreasmelone.glowingeyes.server.capability.eyes;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import java.awt.*;
import java.util.HashMap;

public class GlowingEyesCapability {

    // Matches GlowingEyesHandler access
    public static final Capability<IGlowingEyes> INSTANCE = CapabilityToken.get(new CapabilityToken<>(){});

    // Matches GlowingEyes::register listener
    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IGlowingEyes.class);
    }

    public static boolean isToggledOn(Player player) {
        if (player == null) return false;
        boolean capToggle = player.getCapability(INSTANCE).map(IGlowingEyes::isToggledOn).orElse(false);
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective("gloweyes");
        if (objective != null && scoreboard.hasPlayerScore(player.getScoreboardName(), objective)) {
            int score = scoreboard.getOrCreatePlayerScore(player.getScoreboardName(), objective).getScore();
            return capToggle && (score == 1);
        }
        return capToggle;
    }

    public static void setToggledOn(Player player, boolean toggled) {
        player.getCapability(INSTANCE).ifPresent(cap -> cap.setToggledOn(toggled));
    }

    public static HashMap<Point, Color> getGlowingEyesMap(Player player) {
        return player.getCapability(INSTANCE).map(IGlowingEyes::getGlowingEyesMap).orElse(new HashMap<>());
    }

    public static void setGlowingEyesMap(Player player, HashMap<Point, Color> map) {
        player.getCapability(INSTANCE).ifPresent(cap -> cap.setGlowingEyesMap(map));
    }

    // Packet methods - CALL YOUR ORIGINAL PACKET LOGIC HERE
    public static void sendUpdate(Player player) { /* YOUR ORIGINAL PACKET LOGIC */ }
    public static void sendUpdate(Player target, Player receiver) { /* YOUR ORIGINAL PACKET LOGIC */ }

    // Overload for Editor/PresetManager
    public static void sendUpdate() {
        Player player = Minecraft.getInstance().player;
        if (player != null) sendUpdate(player);
    }
}
