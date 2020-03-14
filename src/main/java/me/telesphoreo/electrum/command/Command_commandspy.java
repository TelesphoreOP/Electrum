package me.telesphoreo.electrum.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.telesphoreo.electrum.admin.AdminList;
import me.telesphoreo.electrum.rank.Rank;
import me.telesphoreo.electrum.util.ElectrumBase;

@CommandParameters(description = "Toggles command spy", aliases = "cmdspy", rank = Rank.SUPER_ADMIN, source = SourceType.PLAYER)
public class Command_commandspy extends ElectrumBase
{
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length > 0)
        {
            return false;
        }

        boolean toggle = plugin.al.getAdmin((Player) sender).isCommandSpyEnabled();
        sender.sendMessage(TextFormat.GRAY + "CommandSpy has been " + (toggle ? TextFormat.RED + "disabled" : TextFormat.GREEN + "enabled"));
        plugin.al.getAdmin((Player) sender).setCommandSpyEnabled(!toggle);
        return true;
    }
}
