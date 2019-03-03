package by.chmut.xml.controller;

import by.chmut.xml.command.CommandType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.chmut.xml.command.CommandType.HOME;
import static by.chmut.xml.constant.AttributeName.COMMAND_PARAMETER_NAME;
import static by.chmut.xml.constant.AttributeName.TITLE_ATTRIBUTE_NAME;

    public class RequestHandler {

    public static CommandType get(HttpServletRequest req) {
        String command = req.getParameter(COMMAND_PARAMETER_NAME);
        CommandType commandType = getCommand(command);
        HttpSession session = req.getSession();
        session.setAttribute(TITLE_ATTRIBUTE_NAME, commandType.name());
        return commandType;
    }

    private static CommandType getCommand(String command) {
        if (!(command == null || command.isEmpty())) {
            for (CommandType type : CommandType.values()) {
                String commandName = type.name();
                if (commandName.equalsIgnoreCase(command)) {
                    return type;
                }
            }
        }
        return HOME;
    }
}
