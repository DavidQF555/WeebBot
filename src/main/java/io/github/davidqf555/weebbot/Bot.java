package io.github.davidqf555.weebbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

public class Bot {

    public static final Logger LOGGER = JDALogger.getLog(Bot.class);
    public static final Map<Guild, GuildInfo> info = new HashMap<>();
    public static final AudioPlayerManager manager = new DefaultAudioPlayerManager();
    public static JDA jda;

    public static void main(String[] args) {
        try {
            jda = JDABuilder.createDefault(Reference.TOKEN).build();
        } catch (LoginException exception) {
            LOGGER.error("Invalid Token", exception);
            System.exit(0);
        }
        manager.registerSourceManager(new YoutubeAudioSourceManager(true));
        AudioSourceManagers.registerRemoteSources(manager);
        Bot.jda.addEventListener(new EventListener());
    }
}
