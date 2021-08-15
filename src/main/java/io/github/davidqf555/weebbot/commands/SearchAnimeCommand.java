package io.github.davidqf555.weebbot.commands;

import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePageAnime;
import io.github.davidqf555.weebbot.Util;
import net.dv8tion.jda.api.entities.Message;

import java.util.Map;

public class SearchAnimeCommand extends Command {

    @Override
    public void onCommand(Message message, Map<String, String> args) {
        AnimePageAnime anime = Util.searchAnimePage(args.get("title"));
        message.reply(Util.createMessage(anime.synopsis).setTitle(anime.title, anime.url).setImage(anime.image_url).setAuthor(anime.source).build()).queue();
    }

    @Override
    public boolean onCheck(Message message, Map<String, String> args) {
        if (args.containsKey("title")) {
            return true;
        } else {
            message.reply(Util.createFailedMessage("Requires title argument").build()).queue();
            return false;
        }
    }
}
