package me.telesphoreo.electrum.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.utils.TextFormat;
import me.telesphoreo.electrum.Electrum;
import me.telesphoreo.electrum.player.PlayerData;
import me.telesphoreo.electrum.util.ElectrumBase;

public class ChatListener extends ElectrumBase implements Listener
{
    public ChatListener(Electrum plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChat(PlayerChatEvent event)
    {
        Player player = event.getPlayer();

        if (PlayerData.getData(player).isMuted())
        {
            player.sendMessage(TextFormat.RED + "You may not chat while muted!");
            event.setCancelled(true);
            return;
        }

        String tag = PlayerData.getData(player).getTag() != null ? PlayerData.getData(player).getTag() + " " : "";
        String name = player.getDisplayName();
        event.setFormat(tag + TextFormat.WHITE + " " + name + ": " + event.getMessage());
    }
}