package me.telesphoreo.electrum.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import me.telesphoreo.electrum.Electrum;
import me.telesphoreo.electrum.rank.Rank;

import java.lang.reflect.InvocationTargetException;

public class BlankCommand extends FreedomCommand
{
    Class clazz;
    Object object;

    public BlankCommand(String commandName, String description, String usage, String[] aliases, Rank rank, SourceType source, Class clazz)
    {
        super(commandName, description, usage, aliases, rank, source);

        this.clazz = clazz;
        try
        {
            this.object = clazz.getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException ex)
        {
            Electrum.plugin.getLogger().critical("", ex);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        try
        {
            return (boolean) clazz.getMethod("onCommand", CommandSender.class, Command.class, String.class, String[].class).invoke(object, sender, cmd, string, args);
        }
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            Electrum.plugin.getLogger().critical("", ex);
        }
        return false;
    }
}
