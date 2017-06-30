package it.raqb.spongepl.scst.commands;

import it.raqb.spongepl.scst.SCST;
import org.spongepowered.api.Sponge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raqbit on 29-6-17.
 */
public class Commands {

    public static List<ICommand> commands = new ArrayList<ICommand>();

    public static void addCommands() {
        commands.add(new CommandSClear());
    }

    public static void registerCommands() {
        addCommands();
        commands.forEach(iCommand -> Sponge.getCommandManager()
                .register(SCST.INSTANCE, iCommand.getCommand(), iCommand.getAliases()));
    }
}
