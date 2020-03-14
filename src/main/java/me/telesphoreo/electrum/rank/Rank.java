package me.telesphoreo.electrum.rank;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import lombok.Getter;
import me.telesphoreo.electrum.Electrum;
import me.telesphoreo.electrum.util.FUtil;

public enum Rank implements Displayable
{
    IMPOSTER("an", "Imposter", "IMP", TextFormat.YELLOW),
    NON_OP("a", "Non-Op", "", TextFormat.WHITE),
    OP("an", "Op", "Op", TextFormat.GREEN),
    SUPER_ADMIN("a", "Super Admin", "SA", TextFormat.AQUA),
    TELNET_ADMIN("a", "Telnet Admin", "STA", TextFormat.DARK_GREEN),
    SENIOR_ADMIN("a", "Senior Admin", "SrA", TextFormat.GOLD),
    CONSOLE("the", "Console", "Console", TextFormat.DARK_PURPLE);

    private final String determiner;
    @Getter
    private final String name;
    @Getter
    private final String tag;
    @Getter
    private final TextFormat color;

    Rank(String determiner, String name, String tag, TextFormat color)
    {
        this.determiner = determiner;
        this.name = name;
        this.color = color;
        this.tag = TextFormat.DARK_GRAY + "[" + color + tag + TextFormat.DARK_GRAY + "]" + color;
    }

    public static Rank findRank(String string)
    {
        try
        {
            return Rank.valueOf(string.toUpperCase());
        }
        catch (Exception none)
        {
        }
        return Rank.NON_OP;
    }

    public static Rank getRank(Player player)
    {
        if (Electrum.plugin.al.isImpostor(player))
        {
            return Rank.IMPOSTER;
        }

        if (Electrum.plugin.al.isAdmin(player))
        {
            return Electrum.plugin.al.getAdmin(player).getRank();
        }

        return player.isOp() ? Rank.OP : Rank.NON_OP;
    }

    public static Rank getRank(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return getRank((Player)sender);
        }

        return Rank.CONSOLE;
    }

    public int getLevel()
    {
        return ordinal();
    }

    public boolean isAtLeast(Rank rank)
    {
        return getLevel() >= rank.getLevel();
    }

    public String getLoginMessage()
    {
        return determiner + " " + color + TextFormat.ITALIC + name;
    }

    public static Displayable getDisplay(Player player)
    {
        if (Electrum.plugin.al.isImpostor(player))
        {
            return Rank.IMPOSTER;
        }

        if (FUtil.DEVELOPERS.contains(player.getName()))
        {
            return Title.DEVELOPER;
        }

        if (Electrum.plugin.config.getList("server.executives").contains(player.getName())
                && Electrum.plugin.al.isAdmin(player))
        {
            return Title.EXECUTIVE;
        }

        if (Electrum.plugin.config.getList("server.owners").contains(player.getName()))
        {
            return Title.OWNER;
        }

        if (Electrum.plugin.config.getList("server.masterbuilders").contains(player.getName())
                && !Electrum.plugin.al.isAdmin(player))
        {
            return Title.MASTER_BUILDER;
        }

        return Rank.getRank(player);
    }
}
