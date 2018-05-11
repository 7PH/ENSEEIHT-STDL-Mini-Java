/**
 * 
 */
package fr.n7.stl.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marc Pantel
 *
 */
public class Logger {

    private Logger() { }

    private static List<String> errors = new ArrayList<>();

    private static List<String> warnings = new ArrayList<>();

    private static List<String> infos = new ArrayList<>();

    public static void error(String message) {
        errors.add(message);
        flush();
    }

    public static void warning(String message) {
        warnings.add(message);
    }

    public static void info(String message) {
        infos.add(message);
    }

    public static void flush() {
        for (String info: infos)
            System.err.println("Info: " + info);
        for (String warning: warnings)
            System.err.println("Warning: " + warning);
        for (String error: errors)
            System.err.println("Error: " + error);

        if (errors.size() > 0) {
            System.err.println("Could not run program because multiple exceptions occurred");
            throw new RuntimeException("Mini-Java Exception: " + errors.get(0));
        }
    }

}
