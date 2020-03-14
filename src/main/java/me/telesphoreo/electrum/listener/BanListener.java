package me.telesphoreo.electrum.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerAsyncPreLoginEvent;
import me.telesphoreo.electrum.Electrum;
import me.telesphoreo.electrum.admin.AdminList;
import me.telesphoreo.electrum.banning.Ban;
import me.telesphoreo.electrum.banning.BanManager;
import me.telesphoreo.electrum.util.ElectrumBase;

public class BanListener extends ElectrumBase implements Listener
{
    public BanListener(Electrum plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreLogin(PlayerAsyncPreLoginEvent event)
    {
        Ban ban = plugin.bm.getBan(event.getName(), false);

        if (ban == null)
        {
            ban = plugin.bm.getBan(event.getAddress(), true);
        }

        if (ban != null && !ban.isExpired())
        {
            if (plugin.al.isAdmin(event.getPlayer()))
            {
                event.allow();
                return;
            }

            event.disAllow(ban.getKickMessage());
        }
    }
}
