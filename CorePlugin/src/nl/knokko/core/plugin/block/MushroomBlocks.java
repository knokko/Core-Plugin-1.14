package nl.knokko.core.plugin.block;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;

public class MushroomBlocks {
	
	public static boolean areEnabled() {
		// Custom mushroom blocks are enabled in mc 1.16
		return true;
	}

	public static void place(Block destination, boolean[] directions, String materialName) {
		MultipleFacing mushroomData = (MultipleFacing) Bukkit.createBlockData(Material.valueOf(materialName));
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
}
