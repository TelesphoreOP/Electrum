package me.telesphoreo.electrum.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.telesphoreo.electrum.banning.BanManager;
import me.telesphoreo.electrum.rank.Rank;
import me.telesphoreo.electrum.util.ElectrumBase;
import me.telesphoreo.electrum.util.FUtil;

@CommandParameters(description = "Unbans a player", usage = "/<command> <player>", rank = Rank.SUPER_ADMIN, source = SourceType.BOTH)
public class Command_unban extends ElectrumBase
{
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length != 1)
        {
            return false;
        }

        if (plugin.bm.getBan(args[0], false) == null)
        {
            sender.sendMessage(TextFormat.RED + "That player doesn't seem to be banned!");
            return true;
        }

        FUtil.adminAction(sender.getName(), "Unbanning " + args[0], true);
        plugin.bm.removeBan(plugin.bm.getBan(args[0], false));
        return true;
    }
}