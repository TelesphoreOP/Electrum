package me.telesphoreo.electrum.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.telesphoreo.electrum.rank.Rank;
import me.telesphoreo.electrum.util.ElectrumBase;

@CommandParameters(description = "Find a player's rank", usage = "/<command> [player]", rank = Rank.OP, source = SourceType.BOTH)
public class Command_rank extends ElectrumBase
{
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 1)
        {
            Rank rank = Rank.getRank(sender);
            sender.sendMessage(TextFormat.BLUE + "Your rank is: " + rank.getName());
            return true;
        }
        else
        {
            Player player = server.getPlayer(args[0]);
            Rank rank = Rank.getRank(player);
            if (player == null)
            {
                sender.sendMessage(PLAYER_NOT_FOUND);
                return true;
            }
            sender.sendMessage(TextFormat.BLUE + "" + player + "'s rank is " + rank.getName());
        }
        return true;
    }
}
