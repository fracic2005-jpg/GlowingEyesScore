package me.andreasmelone.glowingeyes.server.capability.eyes;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import java.awt.*;
import java.util.HashMap;

public class GlowingEyesCapability {

    public static final Capability<IGlowingEyes> INSTANCE = net.minecraftforge.common.capabilities.CapabilityManager.get(new CapabilityToken<>(){});

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IGlowingEyes.class);
    }

    public static boolean isToggledOn(Player player) {
        if (player == null) return false;
        return player.getCapability(INSTANCE).map(IGlowingEyes::isToggledOn).orElse(false);
    }

    public static void setToggledOn(Player player, boolean toggled) {
        player.getCapability(INSTANCE).ifPresent(cap -> cap.setToggledOn(toggled));
    }
    
    // New helper method to set the forced state
    public static void setForcedByScore(Player player, boolean forced) {
        player.getCapability(INSTANCE).ifPresent(cap -> cap.setForcedByScore(forced));
    }

    public static HashMap<Point, Color> getGlowingEyesMap(Player player) {
        return player.getCapability(INSTANCE).map(IGlowingEyes::getGlowingEyesMap).orElse(new HashMap<>());
    }

    public static void setGlowingEyesMap(Player player, HashMap<Point, Color> map) {
        player.getCapability(INSTANCE).ifPresent(cap -> cap.setGlowingEyesMap(map));
    }

    // 1. Used for self-updates
    public static void sendUpdate(Player player) { 
        // Keep your original packet sync logic here 
    }

    // 2. The method your Packet class was missing (accepts two players)
    public static void sendUpdate(Player target, Player receiver) {
        // Keep your original packet sync logic here 
    }

    // 3. Used for UI/Screen updates
    public static void sendUpdate() {
        Player player = Minecraft.getInstance().player;
        if (player != null) sendUpdate(player);
    }
}
