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
package io.milton.cloud.server.web.resources;

import io.milton.cloud.server.web.CommonResource;
import io.milton.cloud.server.web.ContentDirectoryResource;
import io.milton.cloud.server.web.NodeChildUtils;
import io.milton.cloud.server.apps.DataResourceApplication;
import io.milton.vfs.data.DataSession;

/**
 * Interface for instantiating webdav resources from a DataNode.
 *
 * @author brad
 */
public interface ChildResourceInstantiator {
    CommonResource toResource(ContentDirectoryResource parent, DataSession.DataNode contentNode, boolean renderMode, DataResourceApplication resourceCreator);
}