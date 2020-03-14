package me.telesphoreo.electrum.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.telesphoreo.electrum.rank.Rank;
import me.telesphoreo.electrum.util.FUtil;

@CommandParameters(description = "Ops a player", usage = "/<command> <player>", rank = Rank.OP, source = SourceType.BOTH)
public class Command_op
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length != 1)
        {
            return false;
        }

        Player player = Server.getInstance().getPlayer(args[0]);

        if (player == null)
        {
            sender.sendMessage(TextFormat.RED + "That player does not exist!");
            return true;
        }

        FUtil.adminAction(sender.getName(), "Opping " + player.getName(), false);
        player.setOp(true);
        return true;
    }
}
