package me.telesphoreo.electrum.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.telesphoreo.electrum.rank.Rank;
import me.telesphoreo.electrum.util.ElectrumBase;

@CommandParameters(description = "Show information about Electrum", rank = Rank.OP, source = SourceType.BOTH)
public class Command_electrum extends ElectrumBase
{
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(TextFormat.GOLD + plugin.pluginName + ": A freedom plugin for Nukkit-based servers.");
            sender.sendMessage(TextFormat.GOLD + "Originally created by _Fleek, now maintained by Telesphoreo.");
            sender.sendMessage(TextFormat.BLUE + "Version: " + plugin.pluginVersion);
            sender.sendMessage(TextFormat.GREEN + "Visit https://github.com/Telesphoreo/Electrum for more information.");
            return true;
        }
        switch (args[0])
        {
            case "reload":
            {
                if (!plugin.al.isAdmin(sender))
                {
                    sender.sendMessage(NO_PERMISSION);
                    return true;
                }
                sender.sendMessage(TextFormat.GRAY + "All configuration files successfully reloaded.");
                plugin.reloadConfigs();
                return true;
            }
        }
        return true;
    }
}
