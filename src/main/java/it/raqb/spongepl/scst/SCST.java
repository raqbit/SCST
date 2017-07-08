package it.raqb.spongepl.scst;

import com.google.inject.Inject;
import it.raqb.spongepl.scst.commands.CommandManager;
import it.raqb.spongepl.scst.config.ConfigHelper;
import it.raqb.spongepl.scst.listeners.TutorialEndListener;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Optional;

/**
 * Created by Raqbit on 6-29-17.
 */
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
    private Game game;

    @Inject
    private PluginContainer pluginContainer;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    @Inject
    private Logger logger;

    private ConfigHelper configHelper;

    private CommandManager commandManager;

    private LuckPermsApi luckPerms;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        // Creating ConfigHelper
        configHelper = new ConfigHelper(this, configManager);

        // Setup basic listeners
        registerEventListeners();

        // Creating Command Manager
        commandManager = new CommandManager(this);

        // Registering commands
        commandManager.registerCommands();

        // Setup plugin apis
        setupPluginAPIs();
    }

    private void registerEventListeners() {
        // Register eventlisteners
        TutorialEndListener tutorialEndListener = new TutorialEndListener(this);
        tutorialEndListener.setupConfig();
        Sponge.getEventManager().registerListeners(this, tutorialEndListener);
    }

    private void setupPluginAPIs(){
        // Setup LuckPerms API
        Optional<LuckPermsApi> provider = LuckPerms.getApiSafe();
        if(provider.isPresent()){
            luckPerms = provider.get();
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public ConfigHelper getConfigHelper() {
        return configHelper;
    }

    public LuckPermsApi getLuckPerms() {
        return luckPerms;
    }

    public PluginContainer getPluginContainer() {
        return pluginContainer;
    }

    public Game getGame() {
        return game;
    }
}
