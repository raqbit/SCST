package it.raqb.spongepl.scst.commands;

import org.spongepowered.api.command.spec.CommandSpec;

/**
 * Created by Raqbit on 29-6-17.
 */
public interface ICommand {

    public CommandSpec getCommand();

    public String[] getAliases();

}
