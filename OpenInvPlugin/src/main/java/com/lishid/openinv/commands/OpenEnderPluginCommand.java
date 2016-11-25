/*
 * Copyright (C) 2011-2014 lishid.  All rights reserved.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation,  version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.lishid.openinv.commands;

import java.util.HashMap;

import com.lishid.openinv.OpenInv;
import com.lishid.openinv.Permissions;
import com.lishid.openinv.internal.ISpecialEnderChest;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class OpenEnderPluginCommand implements CommandExecutor {

    private final OpenInv plugin;
    private final HashMap<Player, String> openEnderHistory = new HashMap<Player, String>();

    public OpenEnderPluginCommand(OpenInv plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You can't use this from the console.");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("?")) {
            plugin.showHelp((Player) sender);
            return true;
        }

        final Player player = (Player) sender;

        // History management
        String history = openEnderHistory.get(player);

        if (history == null || history == "") {
            history = player.getName();
            openEnderHistory.put(player, history);
        }

        final String name;

        // Read from history if target is not named
        if (args.length < 1) {
            name = history;
        } else {
            name = args[0];
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                final OfflinePlayer offlinePlayer = plugin.matchPlayer(name);

                if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore() && !offlinePlayer.isOnline()) {
                    player.sendMessage(ChatColor.RED + "Player not found!");
                    return;
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!player.isOnline()) {
                            return;
                        }
                        openInventory(player, offlinePlayer);
                    }
                }.runTask(plugin);

            }
        }.runTaskAsynchronously(plugin);

        return true;
    }

    private void openInventory(Player player, OfflinePlayer target) {

        Player onlineTarget;
        boolean online = target.isOnline();

        if (!online) {
            // Try loading the player's data
            onlineTarget = plugin.loadPlayer(target);

            if (onlineTarget == null) {
                player.sendMessage(ChatColor.RED + "Player not found!");
                return;
            }
        } else {
            onlineTarget = target.getPlayer();
        }


        if (!onlineTarget.equals(player) && !Permissions.ENDERCHEST_ALL.hasPermission(player)) {
            player.sendMessage(ChatColor.RED + "You do not have permission to access other player's enderchest");
            return;
        }

        // Record the target
        openEnderHistory.put(player, onlineTarget.getName());

        // Create the inventory
        ISpecialEnderChest chest = plugin.getEnderChest(onlineTarget, online);

        // Open the inventory
        player.openInventory(chest.getBukkitInventory());
    }

}
