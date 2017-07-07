package it.raqb.spongepl.scst.config;

import com.google.common.reflect.TypeToken;
import it.raqb.spongepl.scst.SCST;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

/**
 * Created by Raqbit on 07-07-2017.
 */
public class LocationSerializer implements TypeSerializer<Location> {

    private SCST pluginInstance;

    public LocationSerializer(SCST plugin){
        pluginInstance = plugin;
    }
    @Override
    public Location deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        String worldUUID = value.getNode("world").getString();
        int x = value.getNode("x").getInt();
        int y = value.getNode("y").getInt();
        int z = value.getNode("z").getInt();

        Optional<World> optionalWorld = pluginInstance.getGame().getServer().getWorld(worldUUID);

        if(!optionalWorld.isPresent()) {
            throw new ObjectMappingException();
        }

        return new Location(optionalWorld.get(),x,y,z);
    }

    @Override
    public void serialize(TypeToken<?> type, Location location, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("world").setValue(location.getExtent().getUniqueId());
        value.getNode("x").setValue(location.getBlockX());
        value.getNode("y").setValue(location.getBlockY());
        value.getNode("z").setValue(location.getBlockZ());
    }
}
