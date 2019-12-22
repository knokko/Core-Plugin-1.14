package nl.knokko.core.plugin.world;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Raytracer {

	/**
	 * <p>Performs a raytrace from {@code startLocation} towards {@code startLocation + vector}. 
	 * The {@code vector} determines both the direction and the maximum distance of the raytrace!</p>
	 * 
	 * <p>If an intersection with any block or entity was found, a RaytraceResult representing the intersection
	 * that is closest to {@code startLocation} will be returned. If no such intersection was found, this 
	 * method will return null.</p>
	 * 
	 * <p>Entities included in {@code entitiesToExclude} and dropped item entities will be ignored by
	 * the raytrace.</p>
	 * 
	 * @param startLocation The location from which the raytrace will start
	 * @param vector The direction and maximum distance of the raytrace
	 * @param entitiesToExclude An array of entities that will be ignored by this raytrace, may contain null
	 * @return A RaytraceResult for the nearest intersection, or null if no intersection was found
	 */
	public static RaytraceResult raytrace(Location startLocation, Vector vector, Entity...entitiesToExclude) {
		
		// TODO Determine proper raysize
		double raySize = 0.1;
		
		// I'm glade my own class is called RaytraceResult, which prevents naming clashes
		RayTraceResult bukkitResult = startLocation.getWorld().rayTrace(
				startLocation, vector, vector.length(), FluidCollisionMode.NEVER, true, raySize, 
				(Entity toCheck) -> {
					
					// Ignore dropped items (especially important for projectile covers)
					if (toCheck instanceof Item) {
						return false;
					}
					
					// Also ignore all entities in entitiesToExclude
					for (Entity exclude : entitiesToExclude) {
						if (toCheck.equals(exclude)) {
							return false;
						}
					}
					
					// Accept all other entities
					return true;
				});
		if (bukkitResult == null) {
			return null;
		} else {
			if (bukkitResult.getHitEntity() != null) {
				return RaytraceResult.hitEntity(bukkitResult.getHitEntity(), 
						bukkitResult.getHitPosition().toLocation(startLocation.getWorld()));
			} else {
				return RaytraceResult.hitBlock(bukkitResult.getHitPosition().toLocation(startLocation.getWorld()));
			}
		}
	}
}
