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

import java.lang.reflect.Constructor;

import com.lishid.openinv.OpenInv;

import org.bukkit.Server;
import org.bukkit.entity.Player;

public class InternalAccessor {

    private final OpenInv plugin;

    private String version;

    public InternalAccessor(OpenInv plugin) {
        this.plugin = plugin;
    }

    /**
     * Check if the current server version is supported, and, if it is, prepare to load version-specific code.
     * 
     * @param server the Server
     * 
     * @return true if supported
     */
    public boolean initialize(Server server) {
        String packageName = server.getClass().getPackage().getName();
        version = packageName.substring(packageName.lastIndexOf('.') + 1);

        try {
            Class.forName("com.lishid.openinv.internal." + version + ".PlayerDataManager");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the server implementation version. If not initialized, returns the string "null"
     * instead.
     * 
     * @return the version, or "null"
     */
    public String getVersion() {
        return this.version != null ? this.version : "null";
    }

    /**
     * Creates an instance of the IPlayerDataManager implementation for the current server version,
     * or null if unsupported.
     * 
     * @return the IPlayerDataManager
     */
    public IPlayerDataManager newPlayerDataManager() {
        return createObject(IPlayerDataManager.class, "PlayerDataManager");
    }

    /**
     * Creates an instance of the IInventoryAccess implementation for the current server version, or
     * null if unsupported.
     * 
     * @return the IInventoryAccess
     */
    public IInventoryAccess newInventoryAccess() {
        return createObject(IInventoryAccess.class, "InventoryAccess");
    }

    /**
     * Creates an instance of the IAnySilentContainer implementation for the current server version,
     * or null if unsupported.
     * 
     * @return the IAnySilentContainer
     */
    public IAnySilentContainer newAnySilentContainer() {
        return createObject(IAnySilentContainer.class, "AnySilentContainer");
    }

    /**
     * @deprecated Use {@link #newAnySilentContainer()}
     */
    @Deprecated
    public IAnySilentChest newAnySilentChest() {
        return newAnySilentContainer();
    }

    /**
     * Creates an instance of the ISpecialPlayerInventory implementation for the given Player, or
     * null if the current version is unsupported.
     * 
     * @param player the Player
     * @param online true if the Player is online
     * @return the ISpecialPlayerInventory created
     */
    public ISpecialPlayerInventory newSpecialPlayerInventory(Player player, boolean online) {
        return createObject(ISpecialPlayerInventory.class, "SpecialPlayerInventory", player, online);
    }

    /**
     * Creates an instance of the ISpecialEnderChest implementation for the given Player, or
     * null if the current version is unsupported.
     * 
     * @param player the Player
     * @param online true if the Player is online
     * @return the ISpecialEnderChest created
     */
    public ISpecialEnderChest newSpecialEnderChest(Player player, boolean online) {
        return createObject(ISpecialEnderChest.class, "SpecialEnderChest", player, online);
    }

    private <T> T createObject(Class<? extends T> assignableClass, String className, Object... params) {
        try {
            // Check if internal versioned class exists
            Class<?> internalClass = Class.forName("com.lishid.openinv.internal." + version + "." + className);
            if (!assignableClass.isAssignableFrom(internalClass)) {
                plugin.getLogger().warning("Found class " + internalClass.getName() + " but cannot cast to " + assignableClass.getName());
                return null;
            }

            // Quick return: no parameters, no need to fiddle about finding the correct constructor.
            if (params.length == 0) {
                return assignableClass.cast(internalClass.getConstructor().newInstance());
            }

            // Search constructors for one matching the given parameters
            nextConstructor: for (Constructor<?> constructor : internalClass.getConstructors()) {
                Class<?>[] requiredClasses = constructor.getParameterTypes();
                if (requiredClasses.length != params.length) {
                    continue;
                }
                for (int i = 0; i < params.length; ++i) {
                    if (!requiredClasses[i].isAssignableFrom(params[i].getClass())) {
                        continue nextConstructor;
                    }
                }
                return assignableClass.cast(constructor.newInstance(params));
            }

            StringBuilder message = new StringBuilder("Found class ").append(internalClass.getName())
                    .append(" but cannot find any matching constructors for [");
            for (Object object : params) {
                message.append(object.getClass().getName()).append(", ");
            }
            if (params.length > 0) {
                message.delete(message.length() - 2, message.length());
            }

            plugin.getLogger().warning(message.append(']').toString());
        } catch (Exception e) {
            plugin.getLogger().warning("OpenInv encountered an error with the CraftBukkit version \"" + version + "\". Please look for an updated version of OpenInv.");
            e.printStackTrace();
        }

        return null;
    }

}
