package me.telesphoreo.electrum.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.*;
import cn.nukkit.level.Location;
import cn.nukkit.utils.TextFormat;
import me.telesphoreo.electrum.player.PlayerData;
import me.telesphoreo.electrum.Electrum;
import me.telesphoreo.electrum.admin.AdminList;
import me.telesphoreo.electrum.rank.Rank;

import java.lang.reflect.Field;
import me.telesphoreo.electrum.util.ElectrumBase;

public class PlayerListener extends ElectrumBase implements Listener
{
    private SimpleCommandMap cmap = getCommandMap();

    public PlayerListener(Electrum plugin)
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
                server.broadcastMessage(TextFormat.RED + player.getName() + " has been flagged as an imposter!");
                player.getInventory().clearAll();
                player.setGamemode(0);
                player.sendMessage(TextFormat.RED + "You have been marked as an imposter, please verify yourself.");
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
        event.setFormat(tag + TextFormat.WHITE + "<" + name + "> " + event.getMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        String command = event.getMessage();
        boolean isAdmin = plugin.al.isAdmin(player);

        plugin.getLogger().info(TextFormat.LIGHT_PURPLE + "[COMMAND] " + TextFormat.WHITE + player.getName() + " has executed " + command.replaceFirst("/", ""));

        for (Player players : server.getOnlinePlayers().values())
        {
            if (plugin.al.isAdmin(players) && Rank.getRank(players).getLevel() > Rank.getRank(player).getLevel() && players != player)
            {
                players.sendMessage(TextFormat.GRAY + player.getName() + ": " + command);
            }
        }

        for (String string : plugin.config.getStringList("commands.default"))
        {
            if (command.equalsIgnoreCase(string) || command.split(" ")[0].equalsIgnoreCase(string))
            {
                player.sendMessage(TextFormat.RED + "That command is blocked!");
                event.setCancelled(true);
            }

            if (cmap.getCommand(string) == null)
            {
                continue;
            }

            if (cmap.getCommand(string).getAliases() == null)
            {
                continue;
            }

            for (String alias : cmap.getCommand(string).getAliases())
            {
                if (command.equalsIgnoreCase(alias) || command.split(" ")[0].equalsIgnoreCase(alias))
                {
                    player.sendMessage(TextFormat.RED + "That command is blocked!");
                    event.setCancelled(true);
                }
            }
        }

        for (String string : plugin.config.getStringList("commands.admins"))
        {
            if ((command.equalsIgnoreCase(string) || command.split(" ")[0].equalsIgnoreCase(string)) && !isAdmin)
            {
                player.sendMessage(TextFormat.RED + "That command is blocked!");
                event.setCancelled(true);
            }

            if (cmap.getCommand(string) == null)
            {
                continue;
            }

            if (cmap.getCommand(string).getAliases() == null)
            {
                continue;
            }

            for (String alias : cmap.getCommand(string).getAliases())
            {
                if ((command.equalsIgnoreCase(alias) || command.split(" ")[0].equalsIgnoreCase(alias)) && !isAdmin)
                {
                    player.sendMessage(TextFormat.RED + "That command is blocked!");
                    event.setCancelled(true);
                }
            }
        }
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

    private SimpleCommandMap getCommandMap()
    {
        if (cmap == null)
        {
            try
            {
                final Field f = Server.getInstance().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (SimpleCommandMap) f.get(Server.getInstance());
                return getCommandMap();
            }
            catch (NoSuchFieldException | IllegalAccessException ex)
            {
                plugin.getLogger().critical(ex.getMessage());
            }
        }
        else
        {
            return cmap;
        }
        return getCommandMap();
    }
}
