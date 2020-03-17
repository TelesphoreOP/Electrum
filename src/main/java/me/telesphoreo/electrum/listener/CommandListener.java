package me.telesphoreo.electrum.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.utils.TextFormat;
import java.lang.reflect.Field;
import me.telesphoreo.electrum.Electrum;
import me.telesphoreo.electrum.rank.Rank;
import me.telesphoreo.electrum.util.ElectrumBase;

public class CommandListener extends ElectrumBase implements Listener
{
    private SimpleCommandMap cmap = getCommandMap();

    public CommandListener(Electrum plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
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
