/*
 * Copyright 2012 McEvoy Software Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.milton.cloud.common;

import java.util.List;

/**
 *
 * @author brad
 */
public interface ITriplet {
    /**
     * Hex representation of the 160 bit SHA1 hash for this item
     * 
     * @return 
     */
    String getHash();

    /**
     * The local name of the item within its directory
     * 
     * @return 
     */
    String getName();

    /**
     * The item type, f=file, d=directory
     * 
     * @return 
     */
    String getType();
    
}
