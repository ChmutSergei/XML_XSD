package by.chmut.xml.command;

import by.chmut.xml.command.impl.DefaultCommand;
import by.chmut.xml.command.impl.ResultsCommand;

public enum CommandType {

    HOME(new DefaultCommand()),
    RESULTS(new ResultsCommand());

    private Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
