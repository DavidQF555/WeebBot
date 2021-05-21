package io.github.davidqf555.weebbot.commands;

import net.dv8tion.jda.api.entities.Message;

public abstract class Command {

    public void onCommand(Message message) {
    }

    public boolean onCheck(Message message) {
        return true;
    }

}
