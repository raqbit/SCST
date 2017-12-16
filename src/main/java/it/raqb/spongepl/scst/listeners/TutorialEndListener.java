package it.raqb.spongepl.scst.listeners;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;
import it.raqb.spongepl.scst.SCST;
import it.raqb.spongepl.scst.config.StoredLocation;
import it.raqb.spongepl.scst.util.ConfigUtils;
import it.raqb.spongepl.scst.util.VectorUtils;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;


/**
 * Created by Raqbit on 07-07-2017.
 */
public class TutorialEndListener {

    private SCST pluginInstance;

    private LuckPermsApi luckperms;

    // First and second corners
    private Location firstConfigLoc, secondConfigLoc;

    // Should we actually check
    private boolean shouldCheckPosition;

    public TutorialEndListener(SCST plugin) {
        pluginInstance = plugin;
        luckperms = pluginInstance.getLuckPerms();

        // By default the config does not have a placeholder location
        shouldCheckPosition = true;
    }

    public void setupConfig() {
        // Getting config nodes
        ConfigurationNode firstPosNode = pluginInstance.getConfigHelper().rootNode.getNode("tutorial", "end", "firstPos");
        ConfigurationNode secondPosNode = pluginInstance.getConfigHelper().rootNode.getNode("tutorial", "end", "secondPos");

        // Placeholder location so you can easily edit the config
        StoredLocation placeHolderLocation = new StoredLocation("world", new Vector3i(0, 0, 0));

        // Config nodes do not exist on disk
        if (firstPosNode.isVirtual() || secondPosNode.isVirtual()) {
            ConfigUtils.setupPlaceholderNodes(pluginInstance, firstPosNode, secondPosNode, placeHolderLocation);
            // Saving config
            pluginInstance.getConfigHelper().saveConfig();

            pluginInstance.getLogger().info("Created placeholder values for tutorial area detection");

            // Config has placeholder, don't check
            shouldCheckPosition = false;
            return;
        }

        try {
            // Getting stored position from config
            StoredLocation firstStoredPos = firstPosNode.getValue(TypeToken.of(StoredLocation.class));

            // Setting the firstConfigLoc Location to a newly created location based on the stored one.
            // Not using Location objects in config in the first place since they also specify blocktype
            firstConfigLoc = new Location(
                    pluginInstance.getGame().getServer().getWorld(firstStoredPos.worldName).get(),
                    firstStoredPos.position
            );

            // If firstStoredPos is equal to the placeholder one,
            // we can assume the second one is aswell
            // also saves some memory :)
            if (firstStoredPos.equals(placeHolderLocation)) {
                pluginInstance.getLogger().info("Tutorial positions still have placeholder values, please change.");
                shouldCheckPosition = false;
                return;
            }

            StoredLocation secondStoredPos = secondPosNode.getValue(TypeToken.of(StoredLocation.class));
            secondConfigLoc = new Location(
                    pluginInstance.getGame().getServer().getWorld(secondStoredPos.worldName).get(),
                    secondStoredPos.position
            );

        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }

        // TODO: Check if worlds are actually the same

    }

    @Listener
    public void onEntityMove(MoveEntityEvent event, @Root Player player) {

        // If we know this is not setup, we don't wanna do any checks
        if (!shouldCheckPosition) {
            return;
        }

        // Getting old and new location
        Location oldLoc = event.getFromTransform().getLocation();
        Location newLoc = event.getToTransform().getLocation();

        // If locations are the same, player hasn't moved,
        // meaning that the movement is rotation
        if (newLoc.equals(oldLoc)) {
            return;
        }

        Vector3i newPlayerPosition = newLoc.getBlockPosition();

        User lpUser = luckperms.getUser(player.getUniqueId());

        // Player isn't newbie, we shouldn't do anything
        if (!lpUser.getPrimaryGroup().equals("default")) {
            return;
        }

        World configWorld = (World) firstConfigLoc.getExtent();
        World playerWorld = (World) newLoc.getExtent();

        if (!configWorld.getName().equals(playerWorld.getName())) {
            return;
        }

        // If player isn't inside space
        if (!VectorUtils.isInside3DSpace(firstConfigLoc.getBlockPosition()
                , secondConfigLoc.getBlockPosition()
                , newPlayerPosition)) {
            return;
        }

        Node groupNode = luckperms.getNodeFactory()
                .makeGroupNode(luckperms.getGroup("player"))
                .setValue(true).build();

        lpUser.setPermission(groupNode);

        luckperms.getStorage().saveUser(lpUser);
        lpUser.refreshCachedData();

    }
}
