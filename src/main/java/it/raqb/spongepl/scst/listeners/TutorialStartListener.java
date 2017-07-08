package it.raqb.spongepl.scst.listeners;

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
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.world.Location;

/**
 * Created by ramon on 8-7-17.
 */
public class TutorialStartListener {

    SCST pluginInstance;

    Location tutorialStartPos;

    public TutorialStartListener(SCST plugin) {
        pluginInstance = plugin;
    }

    public void setupConfig() {
        ConfigurationNode startPosNode = pluginInstance.getConfigHelper().rootNode.getNode("tutorial", "start", "position");

        // Node doesn't exist
        if(startPosNode.isVirtual()){
            try {
                startPosNode.setValue(
                        TypeToken.of(StoredLocation.class),
                        new StoredLocation("world",
                                new Vector3i(0,0,0)
                        )
                );
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                StoredLocation storedLocation = startPosNode.getValue(TypeToken.of(StoredLocation.class));
                this.tutorialStartPos = new Location(pluginInstance.getGame().getServer().getWorld(storedLocation.worldName).get(), storedLocation.position);
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
        }
    }

    @Listener
    public void onClientJoin(ClientConnectionEvent.Join event) {
        LuckPermsApi luckPerms = pluginInstance.getLuckPerms();
        Player player = event.getCause().first(Player.class).get();

        // Getting LuckPerms User object
        User lpUser = luckPerms.getUser(player.getUniqueId());

        if (!lpUser.getPrimaryGroup().equals("default")) {
            return;
        }

        player.setLocation(tutorialStartPos);
    }
}
