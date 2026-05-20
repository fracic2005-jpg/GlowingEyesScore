package me.andreasmelone.glowingeyes.server.capability.eyes;

import me.andreasmelone.glowingeyes.server.packets.CapabilityUpdatePacket;
import me.andreasmelone.glowingeyes.server.packets.PacketManager;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.network.PacketDistributor;

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
    
    public static void setForcedByScore(Player player, boolean forced) {
        player.getCapability(INSTANCE).ifPresent(cap -> cap.setForcedByScore(forced));
    }

    public static HashMap<Point, Color> getGlowingEyesMap(Player player) {
        return player.getCapability(INSTANCE).map(IGlowingEyes::getGlowingEyesMap).orElse(new HashMap<>());
    }

    public static void setGlowingEyesMap(Player player, HashMap<Point, Color> map) {
        player.getCapability(INSTANCE).ifPresent(cap -> cap.setGlowingEyesMap(map));
    }

    // 1. Used for self-updates (Server to specific player)
    public static void sendUpdate(Player player) { 
        player.getCapability(INSTANCE).ifPresent(cap -> {
            if (player instanceof ServerPlayer serverPlayer) {
                PacketManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), 
                    new CapabilityUpdatePacket(player, cap));
            }
        });
    }

    // 2. The method for syncing target state to a specific receiver
    public static void sendUpdate(Player target, Player receiver) {
        target.getCapability(INSTANCE).ifPresent(cap -> {
            if (receiver instanceof ServerPlayer serverPlayer) {
                PacketManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), 
                    new CapabilityUpdatePacket(target, cap));
            }
        });
    }

    // 3. Used for UI/Screen updates
    public static void sendUpdate() {
        Player player = Minecraft.getInstance().player;
        if (player != null) sendUpdate(player);
    }
}
