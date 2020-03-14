package me.telesphoreo.electrum.banning;

import cn.nukkit.Nukkit;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import com.google.common.collect.Lists;
import lombok.Getter;
import me.telesphoreo.electrum.Electrum;

import java.util.List;
import me.telesphoreo.electrum.util.ElectrumBase;

public class BanManager extends ElectrumBase
{
    @Getter
    private static List<Ban> bans;
    private static Config config;

    public BanManager(Electrum plugin)
    {
        bans = Lists.newArrayList();
        bans.clear();
        config = plugin.bans;
        load();
    }

    public void load()
    {
        bans.clear();

        for (String key : config.getKeys(false))
        {
            Ban ban = new Ban(key);
            ban.loadFrom(config.getSection(key));

            bans.add(ban);
        }

        Electrum.plugin.getLogger().info("Successfully loaded " + bans.size() + " bans!");
    }

    public void save()
    {
        for (String key : config.getKeys(false))
        {
            config.set(key, null);
        }

        for (Ban ban : bans)
        {
            config.set(ban.getConfigKey() + ".username", ban.getName());
            ban.saveTo(config.getSection(ban.getConfigKey()));
            config.save();
        }
    }

    public void addBan(CommandSender sender, Player player, String reason, long expiry)
    {
        if (isBanned(player))
        {
            return;
        }

        Ban ban = new Ban(player);
        ban.setBy(sender.getName());
        ban.setReason(reason);
        ban.setExpiry(expiry);
        addBan(ban);
    }

    public void addBan(CommandSender sender, String name, String ip, String reason, long expiry)
    {
        if (getBan(name, ip) != null)
        {
            return;
        }

        Ban ban = new Ban(name);
        ban.setIps(Lists.newArrayList(ip));
        ban.setBy(sender.getName());
        ban.setReason(reason);
        ban.setExpiry(expiry);
        addBan(ban);
    }

    public void removeBan(Ban ban)
    {
        if (!isBanned(ban))
        {
            return;
        }

        bans.remove(ban);
    }

    public void removeExpiredBan()
    {
        for (Ban ban : bans)
        {
            if (ban.isExpired())
            {
                bans.remove(ban);
            }
        }
    }

    public void addBan(Ban ban)
    {
        if (isBanned(ban))
        {
            return;
        }

        bans.add(ban);
        ban.saveTo(config.getSection(ban.getConfigKey()));
    }

    public Ban getBan(Player player)
    {
        for (Ban ban : bans)
        {
            if (ban.getName() != null)
            {
                if (ban.getName().equalsIgnoreCase(player.getName()))
                {
                    return ban;
                }
            }

            if (ban.getIps() != null)
            {
                if (ban.getIps().contains(player.getAddress()))
                {
                    return ban;
                }
            }
        }
        return null;
    }

    public Ban getBan(String name, String ip)
    {
        for (Ban ban : bans)
        {
            if (ban.getName() != null)
            {
                if (ban.getName().equalsIgnoreCase(name))
                {
                    return ban;
                }
            }

            if (ban.getIps() != null)
            {
                if (ban.getIps().contains(ip))
                {
                    return ban;
                }
            }
        }
        return null;
    }

    public Ban getBan(String string, boolean ip)
    {
        for (Ban ban : bans)
        {
            if (ip)
            {
                if (ban.getIps().contains(string))
                {
                    return ban;
                }
            }
            else
            {
                if (ban.getName().equalsIgnoreCase(string))
                {
                    return ban;
                }
            }
        }
        return null;
    }

    public boolean isBanned(Ban ban)
    {
        removeExpiredBan();
        for (Ban b : bans)
        {
            if (b.getName().equals(ban.getName()) || b.getIps().contains(ban.getIps().get(0)))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isBanned(Player player)
    {
        removeExpiredBan();
        return getBan(player) != null;
    }
}
