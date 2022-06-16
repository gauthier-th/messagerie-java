package fr.gauthierth.messageriejava.server;

import java.util.UUID;

/**
 * This class contains utility functions.
 */
public class Utils {

    public static String getUUID() { // Returns a simple UUID.
        return UUID.randomUUID().toString().split("-")[0];
    }

}
