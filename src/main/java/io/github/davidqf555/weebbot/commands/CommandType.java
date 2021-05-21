package io.github.davidqf555.weebbot.commands;

public enum CommandType {

    PLAY_ANIME_OPENINGS(new PlayAnimeOpeningsCommand(), "play"),
    SKIP_ANIME_OPENINGS(new SkipAnimeOpeningCommand(), "skip"),
    LEAVE_COMMAND(new LeaveCommand(), "leave");

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
