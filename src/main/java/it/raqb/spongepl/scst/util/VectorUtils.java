package it.raqb.spongepl.scst.util;

import com.flowpowered.math.vector.Vector3i;

/**
 * Created by Raqbit on 07-09-2017.
 */
public class VectorUtils {

    public static boolean isInside3DSpace(Vector3i cornerA, Vector3i cornerB, Vector3i point) {
        int x = point.getX();
        int y = point.getY();
        int z = point.getZ();

        return (x >= cornerA.getX() && x < cornerB.getX() + 1 &&
                z >= cornerA.getZ() && z < cornerB.getZ() + 1 &&
                y >= cornerA.getY() && y < cornerB.getY() + 1);
    }

}
