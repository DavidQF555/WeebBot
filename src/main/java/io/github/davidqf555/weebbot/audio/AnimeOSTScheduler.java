package io.github.davidqf555.weebbot.audio;

import com.github.doomsdayrs.jikan4java.core.search.animemanga.AnimeSearch;
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime;
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePageAnime;
import io.github.davidqf555.weebbot.Settings;
import io.github.davidqf555.weebbot.Util;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class AnimeOSTScheduler extends AudioScheduler {

    private final List<AnimePageAnime> anime;

    public AnimeOSTScheduler(Guild guild) {
        super(guild);
        anime = new ArrayList<>();
    }

    @Override
    protected String getNextSearch() {
        if (anime.isEmpty()) {
            refillAnime();
        }
        int index = (int) (Math.random() * anime.size());
        AnimePageAnime page = anime.get(index);
        anime.remove(index);
        Anime anime = new AnimeSearch().getByID(page.mal_id).join();
        return "ytsearch:" + anime.title_english + " ost";
    }

    private void refillAnime() {
        anime.clear();
        anime.addAll(Util.searchAnimePages(Settings.SEARCH_LIMIT));
    }
}
