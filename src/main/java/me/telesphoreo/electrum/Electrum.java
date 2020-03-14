package me.telesphoreo.electrum;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import me.telesphoreo.electrum.admin.AdminList;
import me.telesphoreo.electrum.banning.BanManager;
import me.telesphoreo.electrum.command.CommandLoader;
import me.telesphoreo.electrum.listener.BanListener;
import me.telesphoreo.electrum.listener.PlayerListener;
import me.telesphoreo.electrum.player.PlayerData;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class Electrum extends PluginBase
{
    public static Electrum plugin;
    public String pluginName;
    public String pluginVersion;
    public static Server server;

    public Config config;
    public Config admins;
    public Config bans;
    public AdminList al;
    public BanListener bl;
    public BanManager bm;
    public CommandLoader cl;
    public PlayerListener pl;

    @Override
    public void onLoad()
    {
        plugin = this;
        server = this.getServer();
        initConfig();
    }

    @Override
    public void onEnable()
    {
        plugin = this;
        server = this.getServer();

        pluginName = plugin.getName();
        pluginVersion = plugin.getDescription().getVersion();

        al = new AdminList(plugin);
        bl = new BanListener(plugin);
        bm = new BanManager(plugin);
        cl = new CommandLoader();
        pl = new PlayerListener(plugin);

        this.getLogger().info(TextFormat.WHITE + "Created by " + StringUtils.join(this.getDescription().getAuthors(), ", "));
        this.getLogger().info(TextFormat.WHITE + "Version: " + this.getDescription().getVersion());
        this.getLogger().info(TextFormat.WHITE + "The plugin has been enabled.");
    }

    @Override
    public void onDisable()
    {
        plugin = null;
        server = null;

        al.save();
        bm.save();

        config.save();
        admins.save();
        bans.save();
        this.getLogger().info(TextFormat.WHITE + "The plugin has been disabled.");
    }

    public void reloadConfigs()
    {
        config.reload();
        admins.reload();
        bans.reload();
    }

    private void initConfig()
    {
        if (getResource("config.yml") != null)
        {
            saveResource("config.yml");
        }

        if (getResource("admins.yml") != null)
        {
            saveResource("admins.yml");
        }

        if (getResource("bans.yml") != null)
        {
            saveResource("bans.yml");
        }

        config = new Config(new File(getDataFolder() + "/config.yml"), Config.YAML);
        admins = new Config(new File(getDataFolder() + "/admins.yml"), Config.YAML);
        bans = new Config(new File(getDataFolder() + "/bans.yml"), Config.YAML);
    }
}
