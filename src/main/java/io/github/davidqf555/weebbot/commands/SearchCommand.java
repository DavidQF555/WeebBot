package io.github.davidqf555.weebbot.commands;

import com.github.doomsdayrs.jikan4java.core.search.animemanga.AnimeSearch;
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePageAnime;
import io.github.davidqf555.weebbot.Util;
import net.dv8tion.jda.api.entities.Message;

import java.util.Map;

public class SearchCommand extends Command {

    @Override
    public void onCommand(Message message, Map<String, String> args) {
        AnimeSearch search = new AnimeSearch().setQuery(args.get("title"));
        AnimePageAnime anime = search.get().join().animes.get(0);
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
