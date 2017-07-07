package it.raqb.spongepl.scst;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;

/**
 * Created by Raqbit on 1-7-17.
 */
public class ConfigHelper {

    private SCST pluginInstance;

    private ConfigurationLoader<CommentedConfigurationNode> configManager;
    public CommentedConfigurationNode rootNode;

    public ConfigHelper(SCST plugin, ConfigurationLoader<CommentedConfigurationNode> _configManager){

        pluginInstance = plugin;

        this.configManager = _configManager;
        try {
            this.rootNode = _configManager.load();
        } catch (IOException e) {
            pluginInstance.logger.error("Could not load config file, see stacktrace:");
            e.printStackTrace();
        }
    }

    public void saveConfig(){
        try {
            this.configManager.save(rootNode);
        } catch (IOException err) {
            pluginInstance.logger.error("Could not save config file, see stacktrace:");
            err.printStackTrace();
        }
    }
}
