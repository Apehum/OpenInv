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

package com.lishid.openinv;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class OpenInvPlayerListener implements Listener {

    private final OpenInv plugin;

    public OpenInvPlayerListener(OpenInv plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        plugin.setPlayerOnline(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.setPlayerOffline(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getPlayer().isSneaking()
                || event.useInteractedBlock() == Result.DENY
                || !plugin.getAnySilentContainer().isAnySilentContainer(event.getClickedBlock())) {
            return;
        }

        Player player = event.getPlayer();
        boolean anychest = Permissions.ANYCHEST.hasPermission(player) && plugin.getPlayerAnyChestStatus(player);
        boolean needsAnyChest = plugin.getAnySilentContainer().isAnyContainerNeeded(player, event.getClickedBlock());

        if (!anychest && needsAnyChest) {
            return;
        }

        boolean silentchest = Permissions.SILENT.hasPermission(player) && plugin.getPlayerSilentChestStatus(player);

        // If anychest or silentchest is active
        if ((anychest || silentchest) && plugin.getAnySilentContainer().activateContainer(player, silentchest, event.getClickedBlock())) {
            if (silentchest && plugin.notifySilentChest() && needsAnyChest && plugin.notifyAnyChest()) {
                player.sendMessage("You are opening a blocked container silently.");
            } else if (silentchest && plugin.notifySilentChest()) {
                player.sendMessage("You are opening a container silently.");
            } else if (needsAnyChest && plugin.notifyAnyChest()) {
                player.sendMessage("You are opening a blocked container.");
            }
            event.setCancelled(true);
        }
    }

}
