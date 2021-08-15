package io.github.davidqf555.weebbot.commands;

import io.github.davidqf555.weebbot.audio.AnimeOPScheduler;
import io.github.davidqf555.weebbot.audio.AudioScheduler;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Map;

public class PlayAnimeOPsCommand extends PlayAudioCommand {

    @Override
    protected AudioScheduler createAudioScheduler(Guild guild, Map<String, String> args) {
        return new AnimeOPScheduler(guild);
    }
}
