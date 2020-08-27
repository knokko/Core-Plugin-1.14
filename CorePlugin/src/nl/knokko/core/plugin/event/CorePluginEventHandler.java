package nl.knokko.core.plugin.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiPredicate;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;

public class CorePluginEventHandler implements Listener {
	
	private static Collection<BiPredicate<ItemStack, ItemStack>> smithingPredicates = new ArrayList<>(1);
	
	public static void preventSmithing(BiPredicate<ItemStack, ItemStack> preventIf) {
		smithingPredicates.add(preventIf);
	}
	
	public static void clearSmithingPredicates() {
		smithingPredicates.clear();
	}

	@EventHandler
	public void onSmithing(PrepareSmithingEvent event) {
		ItemStack[] contents = event.getInventory().getContents();
		for (BiPredicate<ItemStack, ItemStack> preventIf : smithingPredicates) {
			if (preventIf.test(contents[0], contents[1])) {
				event.setResult(null);
				break;
				/*
				 * This method seems to block the smithing on the server side, but
				 * doesn't notify the player until the player actually tries it.
				 * I'm afraid this can't be fixed.
				 */
			}
		}
	}
}
