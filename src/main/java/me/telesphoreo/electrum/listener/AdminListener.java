package me.telesphoreo.electrum.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.utils.TextFormat;
import me.telesphoreo.electrum.Electrum;
import me.telesphoreo.electrum.admin.AdminList;
import me.telesphoreo.electrum.player.PlayerData;
import me.telesphoreo.electrum.rank.Rank;
import me.telesphoreo.electrum.util.ElectrumBase;

public class AdminListener extends ElectrumBase implements Listener
{
    public AdminListener(Electrum plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        boolean isAdmin = plugin.al.isAdmin(player);

        if (isAdmin)
        {
            if (!plugin.al.getAdmin(player).getIps().contains(player.getAddress()))
            {
                AdminList.getImpostors().add(player.getName());
                server.broadcastMessage(TextFormat.RED + player.getName() + " has been flagged as an impostor!");
                player.getInventory().clearAll();
                player.setGamemode(0);
                player.sendMessage(TextFormat.RED + "You have been marked as an impostor, please verify yourself.");
                return;
            }

            server.broadcastMessage(TextFormat.AQUA + player.getName() + " is " + Rank.getDisplay(player).getLoginMessage());
        }

        PlayerData.getData(player).setTag(Rank.getDisplay(player).getTag());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event)
    {
        if (plugin.al.isImpostor(event.getPlayer()))
        {
            AdminList.getImpostors().remove(event.getPlayer().getName());
        }
    }
}
