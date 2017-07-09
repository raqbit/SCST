package it.raqb.spongepl.scst.listeners;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;
import it.raqb.spongepl.scst.SCST;
import it.raqb.spongepl.scst.config.StoredLocation;
import it.raqb.spongepl.scst.util.VectorUtils;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * Created by Raqbit on 07-09-2017.
 */
public class SCBlockBanListener {

    private SCST pluginInstance;

    private Location firstConfigLoc, secondConfigLoc;

    // Should we actually check
    private boolean shouldCheckPosition;


    public SCBlockBanListener(SCST plugin){
        pluginInstance = plugin;
    }

    public void setupConfig(){
        ConfigurationNode firstPosNode = pluginInstance.getConfigHelper().rootNode.getNode("scblockban", "firstPos");
        ConfigurationNode secondPosNode = pluginInstance.getConfigHelper().rootNode.getNode("scblockban", "secondPos");

        // Placeholder location so you can easily edit the config
        StoredLocation placeHolderLocation = new StoredLocation("world", new Vector3i(0, 0, 0));

        // Config nodes do not exist on disk
        if (firstPosNode.isVirtual() || secondPosNode.isVirtual()) {
            try {
                // Setting place holder info
                firstPosNode.setValue(TypeToken.of(StoredLocation.class), placeHolderLocation);
                secondPosNode.setValue(TypeToken.of(StoredLocation.class), placeHolderLocation);
            } catch (ObjectMappingException e) {
                pluginInstance.getLogger().error("Could not map stored location");
                e.printStackTrace();
            }
            // Saving config
            pluginInstance.getConfigHelper().saveConfig();

            pluginInstance.getLogger().info("Created placeholder values for SC block ban detection");

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
                pluginInstance.getLogger().info("SC block ban positions still have placeholder values, please change.");
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

    }

    @Listener
    public void onBlockPlace(ChangeBlockEvent.Place event, @Root Player player){

        // If we know this is not setup, we don't wanna do any checks
        if (!shouldCheckPosition) {
            return;
        }

        BlockSnapshot snapshot = event.getTransactions().get(0).getFinal();

        Location blockPlacementLocation = snapshot.getLocation().get();

        World configWorld = (World) firstConfigLoc.getExtent();
        World playerWorld = (World) blockPlacementLocation.getExtent();

        if(!configWorld.getName().equals(playerWorld.getName())){
         return;
        }

        String blockId = snapshot.getState().getId();

        if(!shouldBlockPlacement(blockId)){
            return;
        }

        if (!VectorUtils.isInside3DSpace(firstConfigLoc.getBlockPosition()
                , secondConfigLoc.getBlockPosition()
                , blockPlacementLocation.getBlockPosition())) {
            return;
        }
        event.setCancelled(true);
    }

    private boolean shouldBlockPlacement(String blockId){
        return blockId.startsWith("securitycraft:reinforced")
                && !blockId.contains("gate")
                && !blockId.contains("door");
    }
}
