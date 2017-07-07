package it.raqb.spongepl.scst;

import com.google.inject.Inject;
import it.raqb.spongepl.scst.commands.CommandManager;
import it.raqb.spongepl.scst.listeners.TutorialListeners;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import java.util.Optional;

@Plugin(
        id = "scst",
        name = "SCST",
        description = "Tweaks for the SecurityCraft Server",
        url = "http://raqb.it",
        authors = {
                "Raqbit", "Vauff"
        },
        version = "${VERSION}",
        dependencies = {
                @Dependency(id= "luckperms")
        }
)
public class SCST {

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    @Inject
    public Logger logger;

    public ConfigHelper configHelper;

    public LuckPermsApi luckPerms;

    private CommandManager commandManager;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        // Setup basic listeners
        registerEventListeners();

        // Creating ConfigHelper
        configHelper = new ConfigHelper(this, configManager);

        // Creating Command Manager
        commandManager = new CommandManager(this);

        // Registering commands
        commandManager.registerCommands();

        // Setup LuckPerms API
        setupLuckPermsAPI();
    }

    private void registerEventListeners() {
        Sponge.getEventManager().registerListeners(this, new TutorialListeners(this));
    }

    private void setupLuckPermsAPI(){
        Optional<LuckPermsApi> provider = LuckPerms.getApiSafe();
        if(provider.isPresent()){
            this.luckPerms = provider.get();
        }
    }
}
