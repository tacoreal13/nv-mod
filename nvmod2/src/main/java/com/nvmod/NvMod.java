package com.nvmod;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class NvMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(this::registerCommands);
    }

    private void registerCommands(
            CommandDispatcher<ServerCommandSource> dispatcher,
            CommandRegistryAccess registryAccess,
            CommandManager.RegistrationEnvironment environment) {

        dispatcher.register(
            CommandManager.literal("nv")
                .executes(ctx -> {
                    ServerCommandSource source = ctx.getSource();
                    ServerPlayerEntity player = source.getPlayer();
                    if (player == null) {
                        source.sendError(Text.literal("This command can only be run by a player."));
                        return 0;
                    }
                    player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.NIGHT_VISION,
                        -1,    // infinite
                        0,     // amplifier (level 1)
                        true,  // ambient
                        false  // showParticles
                    ));
                    source.sendFeedback(() -> Text.literal("Night vision enabled."), false);
                    return 1;
                })
        );
    }
}
