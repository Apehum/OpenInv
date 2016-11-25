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

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * @deprecated Use {@link IAnySilentContainer}
 */
@Deprecated
public interface IAnySilentChest {

    /**
     * @deprecated Use {@link IAnySilentContainer#activateContainer(Player, boolean, Block)}.
     */
    @Deprecated
    public boolean activateChest(Player player, boolean anychest, boolean silentchest, int x, int y, int z);

    /**
     * @deprecated Use {@link IAnySilentContainer#isAnyContainerNeeded(Player, Block)}.
     */
    @Deprecated
    public boolean isAnyChestNeeded(Player player, int x, int y, int z);

}
