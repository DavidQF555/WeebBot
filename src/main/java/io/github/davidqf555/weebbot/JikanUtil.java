package io.github.davidqf555.weebbot;

import com.github.doomsdayrs.jikan4java.core.search.animemanga.AnimeSearch;
import com.github.doomsdayrs.jikan4java.enums.SortBy;
import com.github.doomsdayrs.jikan4java.enums.search.animemanga.orderby.AnimeOrderBy;
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime;
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePage;
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePageAnime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JikanUtil {

    public static List<String> randomOpenings(int amount) {
        List<String> out = new ArrayList<>();
        while (out.size() < amount) {
            AnimeSearch as = new AnimeSearch().orderBy(AnimeOrderBy.MEMBERS).sortBy(SortBy.DESCENDING).setLimit(Reference.SEARCH_LIMIT);
            AnimePage page = as.get().join();
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException ignored) {
            }
            while (out.size() < amount && !page.animes.isEmpty()) {
                AnimePageAnime future = page.animes.get((int) (Math.random() * page.animes.size()));
                Anime anime = new AnimeSearch().getByID(future.mal_id).join();
                try {
                    TimeUnit.SECONDS.sleep(4);
                } catch (InterruptedException ignored) {
                }
                if (anime.opening_themes.isEmpty()) {
                    page.animes.remove(future);
                } else {
                    out.add(anime.opening_themes.get((int) (Math.random() * anime.opening_themes.size())));
                }
            }
        }
        return out;
    }
}
