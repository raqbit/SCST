package it.raqb.spongepl.scst.config;

import com.google.common.reflect.TypeToken;
import it.raqb.spongepl.scst.SCST;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

import java.io.IOException;

/**
 * Created by Raqbit on 7-1-17.
 */
public class ConfigHelper {

    private SCST pluginInstance;

    private ConfigurationLoader<CommentedConfigurationNode> configManager;
    public CommentedConfigurationNode rootNode;

    public ConfigHelper(SCST plugin, ConfigurationLoader<CommentedConfigurationNode> _configManager){

        pluginInstance = plugin;

        configManager = _configManager;

        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(StoredLocation.class), new StoredLocationSerializer());

        try {
            rootNode = configManager.load();
        } catch (IOException e) {
            pluginInstance.getLogger().error("Could not load config file, see stacktrace:");
            e.printStackTrace();
        }
    }

    public void saveConfig(){
        try {
            configManager.save(rootNode);
        } catch (IOException err) {
            pluginInstance.getLogger().error("Could not save config file, see stacktrace:");
            err.printStackTrace();
        }
    }
}
