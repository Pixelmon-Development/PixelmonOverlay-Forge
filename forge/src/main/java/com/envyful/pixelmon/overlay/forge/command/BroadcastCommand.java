package com.envyful.pixelmon.overlay.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.pixelmon.overlay.api.BroadcastFactory;
import com.envyful.pixelmon.overlay.forge.PixelmonOverlayForge;
import com.envyful.pixelmon.overlay.forge.config.PixelmonOverlayConfig;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.overlay.notice.EnumOverlayLayout;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;

@Command(
        value = "bc",
        description = "Sends a specific broadcast to a target",
        aliases = {
                "broadcast"
        }
)
@Permissible("pixelmon.overlay.command.broadcast")
public class BroadcastCommand {

    @CommandProcessor
    public void onCommand(@Sender ICommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes('&',
                    "&c&l(!) &cInsufficient args! /overlay bc <id> (targets...)")));
            return;
        }

        List<EnvyPlayer<?>> targets = Lists.newArrayList();

        if (args.length == 1) {
            for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
                targets.add(PixelmonOverlayForge.getInstance().getPlayerManager().getPlayer(player));
            }
        } else {
            for (int i = 1; i < args.length; i++) {
                EntityPlayerMP target = UtilPlayer.findByName(args[i]);

                if (target == null) {
                    continue;
                }

                targets.add(PixelmonOverlayForge.getInstance().getPlayerManager().getPlayer(target));
            }
        }

        PixelmonOverlayConfig.BroadcastConfig broadcastConfig = PixelmonOverlayForge.getInstance().getConfig().getOptions().get(args[1]);

        BroadcastFactory.builder(broadcastConfig.getConfigData() == null ? "" : broadcastConfig.getConfigData().build())
                .lines(broadcastConfig.getText())
                .duration((int) broadcastConfig.getDurationSeconds())
                .layout(EnumOverlayLayout.valueOf(broadcastConfig.getLayoutType().toUpperCase()))
                .build().send(targets.toArray(new EnvyPlayer<?>[0]));
    }
}
