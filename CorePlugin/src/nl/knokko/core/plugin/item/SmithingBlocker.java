package nl.knokko.core.plugin.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;

import nl.knokko.core.plugin.CorePlugin;

import java.util.function.Predicate;

public class SmithingBlocker {
    
    public static void blockSmithingTableUpgrades(Predicate<ItemStack> shouldBeBlocked) {
        Bukkit.getPluginManager().registerEvents(
        		new SmithingBlockEventHandler(shouldBeBlocked), 
        		CorePlugin.getInstance()
        );
    }
    
    private static class SmithingBlockEventHandler implements Listener {
    	
    	private final Predicate<ItemStack> shouldBeBlocked;
    	
    	SmithingBlockEventHandler(Predicate<ItemStack> shouldBeBlocked) {
    		this.shouldBeBlocked = shouldBeBlocked;
    	}
    	
    	@EventHandler
    	public void blockSmithingTableUpgrades(PrepareSmithingEvent event) {
    		ItemStack toUpgrade = event.getInventory().getItem(0);
    		if (shouldBeBlocked.test(toUpgrade)) {
    			event.setResult(new ItemStack(Material.AIR));
    		}
    	}
    }
}