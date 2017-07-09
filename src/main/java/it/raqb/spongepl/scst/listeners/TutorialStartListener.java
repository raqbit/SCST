package it.raqb.spongepl.scst.listeners;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;
import it.raqb.spongepl.scst.SCST;
import it.raqb.spongepl.scst.config.StoredLocation;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.world.Location;

import java.util.Vector;

/**
 * Created by ramon on 8-7-17.
 */
public class TutorialStartListener {

    private SCST pluginInstance;

    private Location tutorialStartPos;
    private Vector3d tutorialStartRot;

    private boolean shouldCheck;

    public TutorialStartListener(SCST plugin) {
        pluginInstance = plugin;

        shouldCheck = true;
    }

    public void setupConfig() {
        ConfigurationNode startPosNode = pluginInstance.getConfigHelper().rootNode.getNode("tutorial", "start", "position");
        ConfigurationNode startRotNode = pluginInstance.getConfigHelper().rootNode.getNode("tutorial", "start", "rotation");

        StoredLocation placeholderLocation =
                new StoredLocation("world",
                new Vector3d(0,0,0));

        // Node doesn't exist
        if(startPosNode.isVirtual()) {

            try {
                startPosNode.setValue(
                        TypeToken.of(StoredLocation.class),
                        placeholderLocation
                );
                shouldCheck = false;
                pluginInstance.getLogger().info("Created placeholder values for tutorial spawnpoint");
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
        }

        // (Pitch,Yaw,Roll)
        Vector3d placeholderRotation = new Vector3d(0,0,0);

        if(startRotNode.isVirtual()){
            try {
                startRotNode.setValue(
                        TypeToken.of(Vector3d.class),
                        placeholderRotation
                );
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
        }

        try {
            StoredLocation storedLocation = startPosNode.getValue(TypeToken.of(StoredLocation.class));
            Vector3d storedRotation = startRotNode.getValue(TypeToken.of(Vector3d.class));
            this.tutorialStartPos = new Location(pluginInstance.getGame().getServer().getWorld(storedLocation.worldName).get(), storedLocation.position);
            this.tutorialStartRot = storedRotation;

            if(storedLocation.equals(placeholderLocation)){
                pluginInstance.getLogger().info("Tutorial Spawnpoint placeholder value detected");
                shouldCheck = false;
                return;
            }
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }
        pluginInstance.getConfigHelper().saveConfig();
    }

    @Listener
    public void onClientJoin(ClientConnectionEvent.Join event, @Root Player player) {

        if(!shouldCheck){
            return;
        }

        // Getting LuckPerms User object
        User lpUser = pluginInstance.getLuckPerms().getUser(player.getUniqueId());

        if (!lpUser.getPrimaryGroup().equals("default")) {
            return;
        }

        player.setLocationAndRotation(tutorialStartPos, tutorialStartRot);
    }
}
