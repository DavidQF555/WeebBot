package io.github.davidqf555.weebbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import io.github.davidqf555.weebbot.Bot;
import io.github.davidqf555.weebbot.JikanUtil;
import io.github.davidqf555.weebbot.Reference;
import io.github.davidqf555.weebbot.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.LinkedList;
import java.util.Queue;

public class AnimeOpeningScheduler extends AudioEventAdapter {

    private final Queue<AudioTrack> queue;
    private final Guild guild;

    public AnimeOpeningScheduler(Guild guild) {
        this.guild = guild;
        queue = new LinkedList<>();
        checkQueue();
    }

    private void playNextTrack() {
        checkQueue();
        getPlayer().playTrack(queue.poll());
    }

    private void checkQueue() {
        if (queue.isEmpty()) {
            JikanUtil.randomOpenings(Reference.QUEUE_SIZE).forEach(name -> Bot.manager.loadItem(name, new AnimeOpeningLoadResultHandler(name)));
        }
    }

    private AudioPlayer getPlayer() {
        return Bot.info.get(guild).getPlayer();
    }

    private TextChannel getTextChannel() {
        return Bot.info.get(guild).getTextChannel();
    }

    private void queue(AudioTrack track) {
        queue.add(track);
        if (getPlayer().getPlayingTrack() == null) {
            playNextTrack();
        }
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        AudioTrackInfo info = track.getInfo();
        getTextChannel().sendMessage(Util.createMessage("Playing \"" + info.title + "\" by " + info.author).build()).queue();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason == AudioTrackEndReason.LOAD_FAILED) {
            AudioTrackInfo info = track.getInfo();
            getTextChannel().sendMessage(Util.createFailedMessage("\"" + info.title + "\" by " + info.author + " is failing to load").build()).queue();
        }
        if (endReason.mayStartNext) {
            playNextTrack();
        }
    }

    public void skip() {
        getPlayer().stopTrack();
        playNextTrack();
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        AudioTrackInfo info = track.getInfo();
        getTextChannel().sendMessage(Util.createFailedMessage("\"" + info.title + "\" by " + info.author + " is failing to provide audio").build()).queue();
        playNextTrack();
    }

    private class AnimeOpeningLoadResultHandler implements AudioLoadResultHandler {

        private final String search;

        public AnimeOpeningLoadResultHandler(String search) {
            this.search = search;
        }

        @Override
        public void trackLoaded(AudioTrack track) {
            queue(track);
        }

        @Override
        public void playlistLoaded(AudioPlaylist playlist) {
            trackLoaded(playlist.getTracks().get(0));
        }

        @Override
        public void noMatches() {
            Bot.manager.loadItem("ytsearch:" + search, this);
        }

        @Override
        public void loadFailed(FriendlyException exception) {
            getTextChannel().sendMessage(Util.createFailedMessage("Could not load audio of \"" + search + "\"").build()).queue();
            checkQueue();
        }

    }
}
