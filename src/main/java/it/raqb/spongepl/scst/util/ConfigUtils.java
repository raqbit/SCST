package it.raqb.spongepl.scst.util;

import com.google.common.reflect.TypeToken;
import it.raqb.spongepl.scst.SCST;
import it.raqb.spongepl.scst.config.StoredLocation;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class ConfigUtils {

    public static void setupPlaceholderNodes(SCST pluginInstance, ConfigurationNode firstPosNode, ConfigurationNode secondPosNode, StoredLocation placeHolderLocation) {
        try {
            // Setting place holder info
            firstPosNode.setValue(TypeToken.of(StoredLocation.class), placeHolderLocation);
            secondPosNode.setValue(TypeToken.of(StoredLocation.class), placeHolderLocation);
        } catch (ObjectMappingException e) {
            pluginInstance.getLogger().error("Could not map stored location");
            e.printStackTrace();
        }
    }
}
