package it.raqb.spongepl.scst.commands;

import it.raqb.spongepl.scst.SCST;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

/**
 * Created by Raqbit on 07-07-2017.
 */
public class CommandSCST extends Command {

    private SCST pluginInstance;

    public CommandSCST(SCST plugin) {
        super(plugin);

        pluginInstance = plugin;
    }

    @Override
    CommandSpec getCommand() {
        CommandSpec versionCommand = CommandSpec.builder()
                .description(Text.of("Gives you the SCST version"))
                .executor(new VersionExecutor())
                .build();

        return CommandSpec.builder()
                .description(Text.of("Main SCST command"))
                .permission("scst.command.scst")
                .child(versionCommand, "version", "v")
                .build();
    }

    @Override
    String[] getAliases() {
        return new String[]{"scst"};
    }

    private class VersionExecutor implements CommandExecutor {

        @Override
        public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
            String version = pluginInstance.getPluginContainer().getVersion().get();
            src.sendMessage(Text.of("SCST Version: " + version));
            return CommandResult.success();
        }
    }
}
