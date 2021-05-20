package io.github.davidqf555.weebbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;

public class Bot {

    public static final Logger LOGGER = JDALogger.getLog(Bot.class);
    private static JDA jda;

    public static void main(String[] args) {
        try {
            jda = JDABuilder.createDefault(Reference.TOKEN).build();
        } catch (LoginException exception) {
            LOGGER.error("Invalid Token", exception);
            System.exit(0);
        }
    }
}
