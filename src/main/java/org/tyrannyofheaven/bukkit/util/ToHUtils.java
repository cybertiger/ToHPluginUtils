/*
 * Copyright 2011 ZerothAngel <zerothangel@tyrannyofheaven.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tyrannyofheaven.bukkit.util;

import static org.tyrannyofheaven.bukkit.util.ToHLoggingUtils.log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;

/**
 * Miscellaneous utility methods.
 * 
 * @author zerothangel
 */
public class ToHUtils {

    private static final Map<String, Material> materialMap;

    static {
        // Build materialMap consisting of lowercased names without underscores
        Map<String, Material> mm = new HashMap<String, Material>();
        for (Material material : Material.values()) {
            String name = material.name().toLowerCase().replaceAll("_", "");
            mm.put(name, material);
        }
        materialMap = Collections.unmodifiableMap(mm);
    }

    private ToHUtils() {
        throw new AssertionError("Don't instantiate me!");
    }

    /**
     * Throws an AssertionError if test is false.
     * 
     * @param test the test
     * @param message assertion message
     */
    public static void assertTrue(boolean test, String message) {
        if (!test)
            throw new AssertionError(message);
    }

    /**
     * Throws an AssertionError if test is false.
     * 
     * @param test the test
     */
    public static void assertTrue(boolean test) {
        if (!test)
            throw new AssertionError();
    }

    /**
     * Throws an AssertionError if test is true.
     * 
     * @param test the test
     * @param message assertion message
     */
    public static void assertFalse(boolean test, String message) {
        if (test)
            throw new AssertionError(message);
    }

    /**
     * Throws an AssertionError if test is true.
     * 
     * @param test the test
     */
    public static void assertFalse(boolean test) {
        if (test)
            throw new AssertionError();
    }

    /**
     * Registers an event by name to the given executor.
     * 
     * @param type name of the Event.Type enum
     * @param listener Listener to register
     * @param priority Priority of this listener
     * @param plugin owning Plugin
     * @return true if successfully registered
     */
    public static boolean registerEvent(String type, Listener listener, Priority priority, Plugin plugin) {
        Type eventType;
        try {
            eventType = Type.valueOf(type);
        }
        catch (IllegalArgumentException e) {
            log(plugin, Level.SEVERE, "Unknown event type: %s", type, e);
            return false;
        }

        plugin.getServer().getPluginManager().registerEvent(eventType, listener, priority, plugin);
        return true;
    }

    /**
     * Similar to {@link Material#matchMaterial(String)} but also accepts names
     * without underscores or spaces, e.g. woodpickaxe.
     * 
     * @param name name of the material to match
     * @return Material or null if not found
     */
    public static Material matchMaterial(String name) {
        Material material = Material.matchMaterial(name);
        if (material == null) {
            // Use our map
            material = materialMap.get(name.toLowerCase());
        }
        return material;
    }

}
