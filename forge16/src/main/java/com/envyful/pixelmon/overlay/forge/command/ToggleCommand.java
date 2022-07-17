package com.envyful.pixelmon.overlay.forge.command;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.pixelmon.overlay.forge.PixelmonOverlayForge;
import com.envyful.pixelmon.overlay.forge.impl.OverlayAttribute;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;

@Command(
        value = "toggle",
        description = "Toggle command"
)
@Permissible("pixelmon.overlays.command.toggle")
@Child
public class ToggleCommand {

    @CommandProcessor
    public void onCommand(@Sender ServerPlayerEntity sender, String[] args) {
        ForgeEnvyPlayer player = PixelmonOverlayForge.getInstance().getPlayerManager().getPlayer(sender);
        OverlayAttribute attribute = player.getAttribute(PixelmonOverlayForge.class);

        if (attribute == null) {
            return;
        }

        attribute.setToggled(!attribute.isToggled());
        sender.sendMessage(UtilChatColour.colour("&e&l(!) &" + (attribute.isToggled() ? "cDisabled&e" : "aEnabled&e") + " pixelmon overlay!"), Util.NIL_UUID);
    }
}
