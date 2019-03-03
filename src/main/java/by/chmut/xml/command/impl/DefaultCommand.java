package by.chmut.xml.command.impl;

import by.chmut.xml.command.Command;

import javax.servlet.http.HttpServletRequest;

import static by.chmut.xml.constant.PagePath.HOME_PAGE;

public class DefaultCommand implements Command {

    @Override
    public String execute(HttpServletRequest req) {
        return HOME_PAGE;
    }
}
