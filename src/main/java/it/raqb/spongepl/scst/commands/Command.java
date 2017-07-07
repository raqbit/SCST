package it.raqb.spongepl.scst.commands;

import it.raqb.spongepl.scst.SCST;
import org.spongepowered.api.command.spec.CommandSpec;

/**
 * Created by Raqbit on 29-6-17.
 */
public abstract class Command {

    public Command(SCST plugin) {}

    abstract CommandSpec getCommand();

    abstract String[] getAliases();


}
