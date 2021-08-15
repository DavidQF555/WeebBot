package io.github.davidqf555.weebbot.commands;

public enum CommandType {

    PLAY_ANIME_OPS(new PlayAnimeOPsCommand(), "op"),
    PLAY_ANIME_OSTS(new PlayAnimeOSTsCommand(), "ost"),
    SKIP(new SkipCommand(), "skip"),
    PAUSE(new PauseCommand(), "pause"),
    LEAVE(new LeaveCommand(), "leave"),
    SEARCH_ANIME(new SearchAnimeCommand(), "anime");

    private final Command command;
    private final String[] names;

    CommandType(Command command, String... names) {
        this.command = command;
        this.names = names;
    }

    public Command getCommand() {
        return command;
    }

    public String[] getNames() {
        return names;
    }
}
