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

    public static HashMap<Point, Color> getGlowingEyesMap(Player player) {
        return player.getCapability(INSTANCE).map(IGlowingEyes::getGlowingEyesMap).orElse(new HashMap<>());
    }

    public static void setGlowingEyesMap(Player player, HashMap<Point, Color> map) {
        player.getCapability(INSTANCE).ifPresent(cap -> cap.setGlowingEyesMap(map));
    }

    public static void sendUpdate(Player player) { 
        // Keep your original packet sync logic here 
    }

    public static void sendUpdate() {
        Player player = Minecraft.getInstance().player;
        if (player != null) sendUpdate(player);
    }
}
