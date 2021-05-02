package nl.knokko.core.plugin.block;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;

public class MushroomBlocks {
	
	public static boolean areEnabled() {
		// Custom mushroom blocks are enabled in mc 1.14
		return true;
	}

	public static void place(Block destination, boolean[] directions, Type type) {
		MultipleFacing mushroomData = (MultipleFacing) Bukkit.createBlockData(fromType(type));
		mushroomData.setFace(BlockFace.DOWN, directions[0]);
		mushroomData.setFace(BlockFace.EAST, directions[1]);
		mushroomData.setFace(BlockFace.NORTH, directions[2]);
		mushroomData.setFace(BlockFace.SOUTH, directions[3]);
		mushroomData.setFace(BlockFace.UP, directions[4]);
		mushroomData.setFace(BlockFace.WEST, directions[5]);
		destination.setBlockData(mushroomData);
	}
	
	public static boolean[] getDirections(Block toCheck) {
		MultipleFacing mushroomData = (MultipleFacing) toCheck.getBlockData();
		boolean[] result = {
				mushroomData.hasFace(BlockFace.DOWN),
				mushroomData.hasFace(BlockFace.EAST),
				mushroomData.hasFace(BlockFace.NORTH),
				mushroomData.hasFace(BlockFace.SOUTH),
				mushroomData.hasFace(BlockFace.UP),
				mushroomData.hasFace(BlockFace.WEST),
		};
		return result;
	}
	
	private static Material fromType(Type type) {
		if (type == Type.STEM) return Material.MUSHROOM_STEM;
		if (type == Type.BROWN) return Material.BROWN_MUSHROOM_BLOCK;
		return Material.RED_MUSHROOM_BLOCK;
	}
	
	public enum Type {
		STEM,
		RED,
		BROWN
	}
}
