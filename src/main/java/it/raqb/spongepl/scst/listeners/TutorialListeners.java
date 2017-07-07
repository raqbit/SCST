package it.raqb.spongepl.scst.listeners;

import com.google.common.reflect.TypeToken;
import it.raqb.spongepl.scst.SCST;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.world.Location;

import java.util.Optional;

/**
 * Created by Raqbit on 07-07-2017.
 */
public class TutorialListeners {

    private SCST pluginInstance;

    public TutorialListeners(SCST plugin) {
        pluginInstance = plugin;
    }

    @Listener
    public void onClientJoin(ClientConnectionEvent.Join event) {
        LuckPermsApi luckPerms = pluginInstance.getLuckPerms();
        Player player = event.getCause().first(Player.class).get();

        // Getting LuckPerms User object
        User lpUser = luckPerms.getUser(player.getUniqueId());

        String primaryGroup = lpUser.getPrimaryGroup();

        pluginInstance.getLogger().debug(player.getName() + "'s primary group is \"" + primaryGroup + "\"");
        // TODO: Make auto-change to different group, or set default to be limited (bad idea)
        // TODO: Teleport to Tutorial Room (Configurable)
    }

//    @Listener
    public void onEntityMove(MoveEntityEvent event) {

        Optional<Player> optionalPlayer = event.getCause().first(Player.class);

        // It's not a player, do nothing
        if (!optionalPlayer.isPresent()) {
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

        CommentedConfigurationNode firstPosNode = pluginInstance.getConfigHelper().rootNode.getNode("tutorial", "firstPos");
        CommentedConfigurationNode secondPosNode = pluginInstance.getConfigHelper().rootNode.getNode("tutorial", "secondPos");

        Location mockLocation = new Location(event.getFromTransform().getExtent(), 0, 0, 0);

        if (firstPosNode.isVirtual() || secondPosNode.isVirtual()) {
            firstPosNode.setValue(mockLocation);
            secondPosNode.setValue(mockLocation);
            pluginInstance.getConfigHelper().saveConfig();
            return;
        }

        Location firstPos, secondPos;
        try {
            firstPos = firstPosNode.getValue(TypeToken.of(Location.class));
            secondPos = secondPosNode.getValue(TypeToken.of(Location.class));
        } catch (ObjectMappingException e) {
            pluginInstance.getLogger().error("Could not map Location");
            e.printStackTrace();
            return;
        }

        if (firstPos.equals(mockLocation)) {
            return;
        }

        Player player = optionalPlayer.get();

        // TODO: Check if player is between x1,y1,z1 and x2,y2,z2
        // TODO: Make x1,y1,z1 and x2,y2,z2 configurable

    }
}
