package me.telesphoreo.electrum.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.level.Location;
import me.telesphoreo.electrum.Electrum;
import me.telesphoreo.electrum.player.PlayerData;
import me.telesphoreo.electrum.util.ElectrumBase;

public class FreezeListener extends ElectrumBase implements Listener
{
    public FreezeListener(Electrum plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        if (PlayerData.getData(player).isFrozen())
        {
            player.teleport(location);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTeleport(PlayerTeleportEvent event)
    {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        if (PlayerData.getData(player).isFrozen())
        {
            player.teleport(location);
            event.setCancelled(true);
        }
    }
}
