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

package com.lishid.openinv.internal;

import org.bukkit.inventory.Inventory;

public interface IInventoryAccess {

    /**
     * Check if an Inventory is an ISpecialPlayerInventory implementation.
     * 
     * @param inventory the Inventory
     * @return true if the Inventory is backed by an ISpecialPlayerInventory
     */
    public boolean isSpecialPlayerInventory(Inventory inventory);

    /**
     * Gets an ISpecialPlayerInventory from an Inventory or null if the Inventory is not backed by
     * an ISpecialPlayerInventory.
     * 
     * @param inventory the Inventory
     * @return the ISpecialPlayerInventory or null
     */
    public ISpecialPlayerInventory getSpecialPlayerInventory(Inventory inventory);

    /**
     * Check if an Inventory is an ISpecialEnderChest implementation.
     * 
     * @param inventory the Inventory
     * @return true if the Inventory is backed by an ISpecialEnderChest
     */
    public boolean isSpecialEnderChest(Inventory inventory);

    /**
     * Gets an ISpecialEnderChest from an Inventory or null if the Inventory is not backed by an
     * ISpecialEnderChest.
     * 
     * @param inventory the Inventory
     * @return the ISpecialEnderChest or null
     */
    public ISpecialEnderChest getSpecialEnderChest(Inventory inventory);

}
