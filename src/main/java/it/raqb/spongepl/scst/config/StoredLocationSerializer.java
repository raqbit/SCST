package it.raqb.spongepl.scst.config;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

/**
 * Created by ramon on 8-7-17.
 */
public class StoredLocationSerializer implements TypeSerializer<StoredLocation> {
    private final String worldNameNodeName = "worldName";
    private final String positionNodeName = "position";
    @Override

    public StoredLocation deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        String worldName = value.getNode(worldNameNodeName).getString();
        Vector3i position = value.getNode(positionNodeName).getValue(TypeToken.of(Vector3i.class));
        return new StoredLocation(worldName, position);
    }

    @Override
    public void serialize(TypeToken<?> type, StoredLocation location, ConfigurationNode value) throws ObjectMappingException {
        value.getNode(worldNameNodeName).setValue(location.worldName);
        value.getNode(positionNodeName).setValue(TypeToken.of(Vector3i.class), location.position);
    }
}
