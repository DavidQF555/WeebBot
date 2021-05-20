package io.github.davidqf555.weebbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Reference {

    public final static String TOKEN = getStringSetting("token");
    public final static String COMMAND = getStringSetting("command");

    private static String getStringSetting(String tag) {
        try {
            BufferedReader br = Files.newBufferedReader(Paths.get("settings.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(tag + "=")) {
                    return line.substring(tag.length() + 1);
                }
            }
            Bot.LOGGER.error(tag + " tag could not be found in settings.txt");
        } catch (IOException exception) {
            Bot.LOGGER.error("settings.txt could not be found", exception);
        }
        System.exit(0);
        return null;
    }
}
