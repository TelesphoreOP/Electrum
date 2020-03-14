package me.telesphoreo.electrum.util;

import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;
import me.telesphoreo.electrum.Electrum;

public class ElectrumBase
{
    protected Electrum plugin = Electrum.plugin;
    protected Server server = Electrum.server;
    protected String PLAYER_NOT_FOUND = TextFormat.RED + "Player not found.";
    protected String NO_PERMISSION = TextFormat.RED + "You do not have permission to execute this command.";
}
