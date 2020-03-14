package me.telesphoreo.electrum.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import com.google.common.collect.Lists;
import me.telesphoreo.electrum.Electrum;
import me.telesphoreo.electrum.rank.Rank;
import me.telesphoreo.electrum.util.ElectrumBase;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@CommandParameters(description = "Shows online players", usage = "/<command> [-a | -i]", aliases = "who", rank = Rank.IMPOSTER, source = SourceType.BOTH)
public class Command_list extends ElectrumBase
{
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        sender.sendMessage(TextFormat.BLUE + "There are "
                + TextFormat.RED + Server.getInstance().getOnlinePlayers().size() + "/" + Server.getInstance().getMaxPlayers()
                + TextFormat.BLUE + " players online.");

        if (args.length == 0)
        {
            final List<String> names = Lists.newArrayList();
            for (Player player : Server.getInstance().getOnlinePlayers().values())
            {
                String tag = Rank.getDisplay(player).getTag() + " ";
                names.add(tag + player.getName());
            }

            sender.sendMessage(TextFormat.WHITE + "Connected players: " + StringUtils.join(names, TextFormat.WHITE + ", "));
            return true;
        }

        if (args.length == 1)
        {
            switch (args[0])
            {
                case "-a":
                {
                    final List<String> names = Lists.newArrayList();
                    for (Player player : Server.getInstance().getOnlinePlayers().values())
                    {
                        String tag = Rank.getDisplay(player).getTag() + " ";
                        if (plugin.al.isAdmin(player))
                        {
                            names.add(tag + player.getName());
                        }
                    }

                    sender.sendMessage(TextFormat.WHITE + "Connected admins: " + StringUtils.join(names, TextFormat.WHITE + ", "));
                    return true;
                }
                case "-i":
                {
                    final List<String> names = Lists.newArrayList();
                    for (Player player : Server.getInstance().getOnlinePlayers().values())
                    {
                        String tag = Rank.getDisplay(player).getTag() + " ";
                        if (plugin.al.isImpostor(player))
                        {
                            names.add(tag + player.getName());
                        }
                    }

                    sender.sendMessage(TextFormat.WHITE + "Connected impostors: " + StringUtils.join(names, TextFormat.WHITE + ", "));
                    return true;
                }
                default:
                {
                    return false;
                }
            }
        }

        return false;
    }
}
