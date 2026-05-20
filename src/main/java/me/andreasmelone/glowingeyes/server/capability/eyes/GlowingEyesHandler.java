package me.andreasmelone.glowingeyes.server.capability.eyes;

import me.andreasmelone.glowingeyes.GlowingEyes;
import me.andreasmelone.glowingeyes.server.util.Util;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GlowingEyesHandler implements INBTSerializable<CompoundTag>, ICapabilityProvider {
    public static final ResourceLocation IDENTIFIER = new ResourceLocation(GlowingEyes.MOD_ID, "glowingeyes");

    IGlowingEyes glowingeyes = new GlowingEyesImpl();
    LazyOptional<IGlowingEyes> instance = LazyOptional.of(() -> glowingeyes);

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("toggledOn", glowingeyes.isToggledOn());
        tag.putBoolean("forcedByScore", glowingeyes.isForcedByScore());
        tag.putByteArray("glowingEyesMap", Util.serializeMap(glowingeyes.getGlowingEyesMap()));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        glowingeyes.setToggledOn(compoundTag.getBoolean("toggledOn"));
        glowingeyes.setForcedByScore(compoundTag.getBoolean("forcedByScore"));
        glowingeyes.setGlowingEyesMap(Util.deserializeMap(compoundTag.getByteArray("glowingEyesMap")));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if (capability == GlowingEyesCapability.INSTANCE) {
            return instance.cast();
        }
        return LazyOptional.empty();
    }

    @SubscribeEvent
    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            event.addCapability(GlowingEyesHandler.IDENTIFIER, new GlowingEyesHandler());
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            Scoreboard scoreboard = player.getScoreboard();
            Objective objective = scoreboard.getObjective("gloweyes");
            
            // Calculate current state: true only if objective exists AND score is >= 1
            int score = 0;
            if (objective != null && scoreboard.hasPlayerScore(player.getScoreboardName(), objective)) {
                score = scoreboard.getOrCreatePlayerScore(player.getScoreboardName(), objective).getScore();
            }
            final boolean scoreState = (score >= 1);

            player.getCapability(GlowingEyesCapability.INSTANCE).ifPresent(cap -> {
                // If the state changes (even from true to false), update and sync
                if (cap.isForcedByScore() != scoreState) {
                    cap.setForcedByScore(scoreState);
                    GlowingEyesCapability.sendUpdate(player); // Sync state to client
                }
            });
        }
    }
}
