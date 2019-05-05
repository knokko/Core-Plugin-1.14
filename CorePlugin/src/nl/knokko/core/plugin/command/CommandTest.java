/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 *  This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package nl.knokko.core.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import nl.knokko.core.plugin.CorePlugin;
import nl.knokko.core.plugin.menu.Menu;

public class CommandTest implements CommandExecutor {
	
	private Menu testMenu;
	
	public CommandTest() {
		testMenu = new Menu("Test Menu", 7);
		testMenu.setItem(0, Material.BARRIER, (Player player) -> {
			player.closeInventory();
		});
		testMenu.setItem(2, Material.STONE, "Broadcast stone", (Player player) -> {
			Bukkit.broadcastMessage("STONE");
			player.closeInventory();
		});
		testMenu.setItem(8, Material.BLAZE_ROD, "Fire", (Player player) -> {
			player.launchProjectile(Fireball.class);
			player.closeInventory();
		}, "Shoot a fireball", "in the direction", "you are facing");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			CorePlugin.getInstance().getMenuHandler().openMenu(((Player) sender), testMenu);
			sender.sendMessage("Open the test menu");
		} else {
			sender.sendMessage("This command is for players");
		}
		return false;
	}
}