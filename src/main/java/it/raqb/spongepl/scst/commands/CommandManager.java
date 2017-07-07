package it.raqb.spongepl.scst.commands;

import it.raqb.spongepl.scst.SCST;
import org.spongepowered.api.Sponge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raqbit on 6-29-17.
 */
public class CommandManager {

    private SCST pluginInstance;

    public List<Command> commands = new ArrayList<Command>();

    public CommandManager(SCST plugin){
        pluginInstance = plugin;

        // Main command
        commands.add(new CommandSCST(plugin));

        // Other commands
        commands.add(new CommandSClear(plugin));
        commands.add(new CommandOutfit(plugin));
        commands.add(new CommandAddToGroup(plugin));
        commands.add(new CommandRemoveFromGroup(plugin));
    }

    public void registerCommands() {
        commands.forEach(iCommand -> Sponge.getCommandManager()
                .register(pluginInstance, iCommand.getCommand(), iCommand.getAliases()));
    }
}
