package me.telesphoreo.electrum.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.telesphoreo.electrum.admin.Admin;
import me.telesphoreo.electrum.admin.AdminList;
import me.telesphoreo.electrum.rank.Rank;
import me.telesphoreo.electrum.util.ElectrumBase;
import me.telesphoreo.electrum.util.FUtil;

@CommandParameters(description = "Manage an admin", usage = "/<command> <add <player> | remove <player> | info <player> | setrank <player> <rank>>", rank = Rank.SUPER_ADMIN, source = SourceType.BOTH)
public class Command_saconfig extends ElectrumBase
{
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        switch (args[0])
        {
            case "add":
            {
                if (args.length < 2)
                {
                    return false;
                }

                if (sender instanceof Player)
                {
                    sender.sendMessage(TextFormat.RED + "You must be on console!");
                    return true;
                }

                final Player player = Server.getInstance().getPlayer(args[1]);

                if (player == null)
                {
                    sender.sendMessage(TextFormat.RED + "That player does not exist!");
                    return true;
                }

                Admin admin = plugin.al.getAdmin(player);

                if (plugin.al.isImpostor(player) && admin != null)
                {
                    FUtil.adminAction(sender.getName(), "Re-adding " + admin.getName() + " to the admin list", true);
                    AdminList.getImpostors().remove(admin.getName());
                    plugin.al.addIp(player, player.getAddress());
                    return true;
                }

                if (admin != null)
                {
                    sender.sendMessage(TextFormat.RED + "That player is already an admin!");
                    return true;
                }

                FUtil.adminAction(sender.getName(), "Adding " + player.getName() + " to the admin list", true);
                plugin.al.addAdmin(new Admin(player));
                return true;
            }

            case "remove":
            {
                if (args.length < 2)
                {
                    return false;
                }

                if (sender instanceof Player)
                {
                    sender.sendMessage(TextFormat.RED + "You must be on console!");
                    return true;
                }

                final Player player = Server.getInstance().getPlayer(args[1]);

                if (player == null)
                {
                    sender.sendMessage(TextFormat.RED + "That player does not exist!");
                    return true;
                }

                Admin admin = plugin.al.getAdmin(player);

                if (admin == null)
                {
                    sender.sendMessage(TextFormat.RED + "That player is not an admin!");
                    return true;
                }

                FUtil.adminAction(sender.getName(), "Removing " + admin.getName() + " from the admin list", true);
                plugin.al.deactivateAdmin(admin);
                return true;
            }

            case "info":
            {
                if (args.length < 2)
                {
                    return false;
                }

                final Player player = Server.getInstance().getPlayer(args[1]);

                if (player == null)
                {
                    sender.sendMessage(TextFormat.RED + "That player does not exist!");
                    return true;
                }

                Admin admin = plugin.al.getAdmin(player);

                if (admin == null)
                {
                    sender.sendMessage(TextFormat.RED + "That player is not an admin!");
                    return true;
                }

                sender.sendMessage(TextFormat.GRAY + admin.toString());
                return true;
            }

            case "setrank":
            {
                if (args.length < 3)
                {
                    return false;
                }

                if (sender instanceof Player)
                {
                    sender.sendMessage(TextFormat.RED + "You must be on console!");
                    return true;
                }

                final Player player = Server.getInstance().getPlayer(args[1]);

                if (player == null)
                {
                    sender.sendMessage(TextFormat.RED + "That player does not exist!");
                    return true;
                }

                Rank rank = Rank.findRank(args[2]);

                if (rank == null)
                {
                    sender.sendMessage(TextFormat.RED + "Invalid rank!");
                    return true;
                }

                if (!rank.isAtLeast(Rank.SUPER_ADMIN))
                {
                    sender.sendMessage(TextFormat.RED + "The rank must be at least Super Admin!");
                    return true;
                }

                Admin admin = plugin.al.getAdmin(player);

                if (admin == null)
                {
                    sender.sendMessage(TextFormat.RED + "That player is not an admin!");
                    return true;
                }

                FUtil.adminAction(sender.getName(), "Setting " + admin.getName() + "'s rank to " + rank.getName(), true);
                plugin.al.updateRank(admin, rank);
                return true;
            }

            default:
            {
                return false;
            }
        }
    }
}
