package nl.knokko.core.plugin.particles;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleHelper {
	
	public static void spawnColoredParticle(Location location, double red, double green, double blue) {
		location.getWorld().spawnParticle(Particle.REDSTONE, 
				location.getX(), location.getY(), location.getZ(), 0, 0, 0, 0, 
				new Particle.DustOptions(Color.fromRGB((int) (red * 255), (int) (green * 255), (int) (blue * 255)), 1.0f));
	}
}
