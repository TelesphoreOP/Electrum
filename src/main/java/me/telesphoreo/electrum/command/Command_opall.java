package me.telesphoreo.electrum.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.telesphoreo.electrum.rank.Rank;
import me.telesphoreo.electrum.util.FUtil;


@CommandParameters(description = "Ops everyone", rank = Rank.SUPER_ADMIN, source = SourceType.BOTH)
public class Command_opall
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length > 0)
        {
            return false;
        }


        FUtil.adminAction(sender.getName(), "Opping all players on the server", false);
        for (Player players : Server.getInstance().getOnlinePlayers().values())
        {
            players.setOp(true);
            players.sendMessage(TextFormat.YELLOW + "You are now op!");
        }

        return true;
    }
}
