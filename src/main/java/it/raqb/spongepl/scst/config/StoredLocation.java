package it.raqb.spongepl.scst.config;

import com.flowpowered.math.vector.Vector3i;

/**
 * Created by ramon on 8-7-17.
 */
public class StoredLocation {
    public String worldName;
    public Vector3i position;

    public StoredLocation(String worldName, Vector3i position){
        this.worldName = worldName;
        this.position = position;
    }

    public boolean equals(StoredLocation otherLocation) {
        Boolean worldNameEquals = otherLocation.worldName.equals(this.worldName);
        Boolean positionEquals = otherLocation.position.equals(this.position);

        return worldNameEquals && positionEquals;
    }
}
