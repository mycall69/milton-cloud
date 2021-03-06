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
package io.milton.cloud.server.web;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author brad
 */
public class UtilsTest {
    


    /**
     * Test of stripSuffix method, of class Utils.
     */
    @Test
    public void testStripSuffix() {
        System.out.println("stripSuffix");
        String expResult = "admin.myorg";
        String result = Utils.stripSuffix("admin.myorg.milton.io", ".milton.io");
        assertEquals(expResult, result);
    }

    /**
     * Test of stripPrefix method, of class Utils.
     */
    @Test
    public void testStripPrefix() {
        System.out.println("stripPrefix");
        String subdomain = "admin.myorg";
        String admin = "admin.";
        String expResult = "myorg";
        String result = Utils.stripPrefix(subdomain, admin);
        assertEquals(expResult, result);
    }
}
