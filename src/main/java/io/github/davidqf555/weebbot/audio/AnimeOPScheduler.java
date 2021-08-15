package io.github.davidqf555.weebbot.audio;

import com.github.doomsdayrs.jikan4java.core.search.animemanga.AnimeSearch;
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime;
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePageAnime;
import io.github.davidqf555.weebbot.Settings;
import io.github.davidqf555.weebbot.Util;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;

public class AnimeOPScheduler extends AudioScheduler {

    private final List<AnimePageAnime> anime;

    public AnimeOPScheduler(Guild guild) {
        super(guild);
        anime = Util.searchAnimePages(Settings.SEARCH_LIMIT);
    }

    @Override
    protected String getNextSearch() {
        while (!anime.isEmpty()) {
            AnimePageAnime page = anime.get((int) (Math.random() * anime.size()));
            Anime play = new AnimeSearch().getByID(page.mal_id).join();
            if (play.opening_themes.isEmpty()) {
                anime.remove(page);
            } else {
                return "ytsearch:" + play.title_english + " opening " + (int) (Math.random() * play.opening_themes.size() + 1);
            }
        }
        throw new RuntimeException("Could not find any anime openings in the " + Settings.SEARCH_LIMIT + " most popular anime");
    }

}
