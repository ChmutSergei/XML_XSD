package by.chmut.xml.command.impl;

import by.chmut.xml.bean.Publication;
import by.chmut.xml.command.Command;
import by.chmut.xml.service.PublicationService;
import by.chmut.xml.service.ServiceException;
import by.chmut.xml.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static by.chmut.xml.constant.AttributeName.*;
import static by.chmut.xml.constant.PagePath.ERROR_PAGE;
import static by.chmut.xml.constant.PagePath.RESULTS_PAGE;


public class ResultsCommand implements Command {

    private static final Logger logger = LogManager.getLogger();
    private static final String DEFAULT_PARSER = "SAX";
    private static final PublicationService service = ServiceFactory.getInstance().getPublicationService();

    @Override
    public String execute(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String errorMessage = (String) session.getAttribute(MESSAGE_PARAMETER_NAME);
        if (!errorMessage.isEmpty()) {
            return ERROR_PAGE;
        }
        String parserType = req.getParameter(PARSER_ATTRIBUTE_NAME);
        if (parserType == null) {
            parserType = DEFAULT_PARSER;
        }
        req.getSession().setAttribute(PARSER_ATTRIBUTE_NAME, parserType);
        String pageForForward = RESULTS_PAGE;
        String filePath = (String) session.getAttribute(UPLOAD_FILE_ATTRIBUTE_NAME);
        try {
            List<Publication> publications = service.createPublication(parserType, filePath);
            req.getSession().setAttribute(RESULT_ATTRIBUTE_NAME, publications);
        } catch (ServiceException e) {
            logger.error(e);
            pageForForward = ERROR_PAGE;
            errorMessage = e.getMessage();
        }
        req.getSession().setAttribute(MESSAGE_PARAMETER_NAME, errorMessage);
        return pageForForward;
    }

}
