/*
 * Copyright 2012 McEvoy Software Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.milton.cloud.server.apps.fileserver;

import io.milton.cloud.server.apps.fileserver.TcpChannelHub.Client;
import java.io.Serializable;

/**
 *
 * @author brad
 */
public interface ClientListener {
    /**
     * Called on receipt of a message from the channel
     *
     * @param msg
     */
    void handleNotification( Client client, Serializable msg );    
}
