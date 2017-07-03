package it.raqb.spongepl.scst;

import com.google.inject.Inject;
import it.raqb.spongepl.scst.commands.Commands;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "scst",
        name = "SCST",
        description = "Tweaks for the SecurityCraft Server",
        url = "http://raqb.it",
        authors = {
                "Raqbit", "Vauff"
        },
        version = "1.2"
)
public class SCST {

    @Inject
    public Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    public static ConfigHelper configHelper;

    public static SCST INSTANCE;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        INSTANCE = this;
        this.configHelper = new ConfigHelper(configManager);
        Commands.registerCommands();
    }

    @Listener
    public void onServerClose(GameStoppedServerEvent event){
        INSTANCE = null;
    }
}
