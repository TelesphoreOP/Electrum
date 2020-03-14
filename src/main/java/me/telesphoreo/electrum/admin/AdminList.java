package me.telesphoreo.electrum.admin;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import com.google.common.collect.Lists;
import lombok.Getter;
import me.telesphoreo.electrum.Electrum;
import me.telesphoreo.electrum.rank.Rank;

import java.util.List;
import me.telesphoreo.electrum.util.ElectrumBase;

public class AdminList extends ElectrumBase
{
    @Getter
    public static List<String> impostors;
    @Getter
    private static List<Admin> admins;
    private static Config config;

    public AdminList(Electrum plugin)
    {
        impostors = Lists.newArrayList();
        impostors.clear();
        admins = Lists.newArrayList();
        admins.clear();
        config = plugin.admins;
        load();
    }

    public void load()
    {
        admins.clear();

        for (String key : config.getKeys(false))
        {
            if (config.getSection(key) == null)
            {
                plugin.getLogger().warning("Could not load admin: " + key + ". Invalid section format!");
                continue;
            }

            Admin admin = new Admin(key);
            admin.loadFrom(config.getSection(key));

            if (!admin.isValid())
            {
                plugin.getLogger().warning("Could not load admin: " + key + ". Missing information!");
                continue;
            }

            admins.add(admin);
        }

        plugin.getLogger().info("Successfully loaded " + admins.size() + " admins!");
    }

    public boolean isAdmin(Admin admin)
    {
        final String key = admin.getConfigKey();
        for (Admin check : admins)
        {
            return check.equals(key);
        }
        return false;
    }

    public boolean isAdmin(Player player)
    {
        return getAdmin(player) != null
                && getAdmin(player).isActive();
    }

    public boolean isAdmin(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return isAdmin((Player) sender);
        }
        return true;
    }

    public Admin getAdmin(Player player)
    {
        for (Admin admin : admins)
        {
            if (player.getName().equals(admin.getName()))
            {
                return admin;
            }
        }
        return null;
    }

    public void addAdmin(Admin admin)
    {
        if (isAdmin(admin))
        {
            return;
        }

        admins.add(admin);
        config.set(admin.getConfigKey() + ".username", admin.getName());
        admin.saveTo(config.getSection(admin.getConfigKey()));
        config.save();
    }

    // Remove an admin's entry
    public void removeAdmin(Admin admin)
    {
        if (!isAdmin(admin))
        {
            return;
        }

        admins.remove(admin);
        config.set(admin.getConfigKey(), null);
        config.save();
    }

    // Disable an admin's entry
    public void deactivateAdmin(Admin admin)
    {
        admins.remove(admin);
        admin.setActive(false);
        admin.saveTo(config.getSection(admin.getConfigKey()));
        admins.add(admin);
    }

    public void updateRank(Admin admin, Rank rank)
    {
        admins.remove(admin);
        admin.setRank(rank);
        admin.saveTo(config.getSection(admin.getConfigKey()));
        admins.add(admin);
    }

    public void addIp(Player player, String ip)
    {
        Admin admin = getAdmin(player);

        if (admin == null)
        {
            return;
        }

        if (admin.getIps().contains(ip))
        {
            return;
        }

        admins.remove(admin);
        admin.addIp(ip);
        admin.saveTo(config.getSection(admin.getConfigKey()));
        admins.add(admin);
    }

    public boolean isImpostor(Player player)
    {
        return impostors.contains(player.getName());
    }

    public void save()
    {
        for (String key : config.getKeys(false))
        {
            config.set(key, null);
        }

        for (Admin admin : admins)
        {
            config.set(admin.getConfigKey() + ".username", admin.getName());
            admin.saveTo(config.getSection(admin.getConfigKey()));
            config.save();
        }
    }
}
