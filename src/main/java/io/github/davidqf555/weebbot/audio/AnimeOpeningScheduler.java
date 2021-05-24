package io.github.davidqf555.weebbot.audio;

import com.github.doomsdayrs.jikan4java.core.search.animemanga.AnimeSearch;
import com.github.doomsdayrs.jikan4java.enums.SortBy;
import com.github.doomsdayrs.jikan4java.enums.search.animemanga.orderby.AnimeOrderBy;
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime;
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePageAnime;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import io.github.davidqf555.weebbot.Bot;
import io.github.davidqf555.weebbot.Settings;
import io.github.davidqf555.weebbot.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public class AnimeOpeningScheduler extends AudioEventAdapter {

    private static final int ANIME_PER_PAGE = 50;
    private final Guild guild;

    public AnimeOpeningScheduler(Guild guild) {
        this.guild = guild;
    }

    private static String getRandomOpeningSearch() {
        List<AnimePageAnime> all = new ArrayList<>();
        int pages = (int) Math.ceil(Settings.SEARCH_LIMIT * 1.0 / ANIME_PER_PAGE);
        for (int page = 1; page <= pages; page++) {
            AnimeSearch as = new AnimeSearch().orderBy(AnimeOrderBy.MEMBERS).sortBy(SortBy.DESCENDING).setLimit(Math.min(Settings.SEARCH_LIMIT - page * ANIME_PER_PAGE + ANIME_PER_PAGE, ANIME_PER_PAGE)).setPage(page);
            all.addAll(as.get().join().animes);
        }
        while (!all.isEmpty()) {
            AnimePageAnime animePage = all.get((int) (Math.random() * all.size()));
            Anime anime = new AnimeSearch().getByID(animePage.mal_id).join();
            if (anime.opening_themes.isEmpty()) {
                all.remove(animePage);
            } else {
                return anime.title_english + " opening " + (int) (Math.random() * anime.opening_themes.size() + 1);
            }
        }
        throw new RuntimeException("Could not find any anime openings in the " + Settings.SEARCH_LIMIT + " most popular anime");
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        AudioTrackInfo info = track.getInfo();
        getTextChannel().sendMessage(Util.createMessage("Playing [" + info.title + "](" + info.uri + ")").build()).queue();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (endReason == AudioTrackEndReason.LOAD_FAILED) {
                AudioTrackInfo info = track.getInfo();
                getTextChannel().sendMessage(Util.createFailedMessage("[" + info.title + "](" + info.uri + ") is failing to load").build()).queue();
            }
            loadNextTrack();
        }
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        AudioTrackInfo info = track.getInfo();
        getTextChannel().sendMessage(Util.createFailedMessage("[" + info.title + "](" + info.uri + ") is failing to provide audio").build()).queue();
        loadNextTrack();
    }

    public void skip() {
        getPlayer().stopTrack();
        loadNextTrack();
    }

    public void loadNextTrack() {
        Bot.manager.loadItem("ytsearch:" + getRandomOpeningSearch(), new AnimeOpeningLoadResultHandler());
    }

    private AudioPlayer getPlayer() {
        return Bot.info.get(guild).getPlayer();
    }

    private TextChannel getTextChannel() {
        return Bot.info.get(guild).getTextChannel();
    }


    private class AnimeOpeningLoadResultHandler implements AudioLoadResultHandler {

        @Override
        public void trackLoaded(AudioTrack track) {
            getPlayer().playTrack(track);
        }

        @Override
        public void playlistLoaded(AudioPlaylist playlist) {
            trackLoaded(playlist.getTracks().get(0));
        }

        @Override
        public void noMatches() {
            loadNextTrack();
        }

        @Override
        public void loadFailed(FriendlyException exception) {
            loadNextTrack();
        }

    }
}
