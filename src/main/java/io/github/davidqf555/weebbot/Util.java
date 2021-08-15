package io.github.davidqf555.weebbot;

import com.github.doomsdayrs.jikan4java.core.search.animemanga.AnimeSearch;
import com.github.doomsdayrs.jikan4java.enums.SortBy;
import com.github.doomsdayrs.jikan4java.enums.search.animemanga.orderby.AnimeOrderBy;
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePageAnime;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Util {

    private static final int ANIME_PER_PAGE = 50;

    public static EmbedBuilder createMessage(String desc, Color c) {
        return createMessage(desc).setColor(c);
    }

    public static EmbedBuilder createMessage(String desc) {
        return new EmbedBuilder().setDescription(desc).setColor(Color.GREEN);
    }

    public static EmbedBuilder createFailedMessage(String desc) {
        return createMessage(desc, Color.RED);
    }

    public static AnimePageAnime searchAnimePage(String title) {
        AnimeSearch search = new AnimeSearch().setQuery(title);
        return search.get().join().animes.get(0);
    }

    public static List<AnimePageAnime> searchAnimePages(int amount) {
        List<AnimePageAnime> anime = new ArrayList<>();
        int pages = (int) Math.ceil(amount * 1.0 / ANIME_PER_PAGE);
        for (int page = 1; page <= pages; page++) {
            AnimeSearch as = new AnimeSearch().orderBy(AnimeOrderBy.MEMBERS).sortBy(SortBy.DESCENDING).setLimit(Math.min(amount - page * ANIME_PER_PAGE + ANIME_PER_PAGE, ANIME_PER_PAGE)).setPage(page);
            anime.addAll(as.get().join().animes);
        }
        return anime;
    }

}
