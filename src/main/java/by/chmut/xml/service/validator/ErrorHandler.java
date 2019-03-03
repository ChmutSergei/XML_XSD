package by.chmut.xml.service.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ErrorHandler extends DefaultHandler {

    private String error;

    private static final Logger logger = LogManager.getLogger();

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void warning(SAXParseException e) {
        logger.warn(getLineAddress(e) + "-" + e.getMessage());
    }
    public void error(SAXParseException e) {
        error = getLineAddress(e) + "-" + e.getMessage();
        logger.error(error);
    }
    public void fatalError(SAXParseException e) {
        error = getLineAddress(e) + "-" + e.getMessage();
        logger.fatal(error);
    }
    private String getLineAddress(SAXParseException e) {
        return e.getLineNumber() + " : " + e.getColumnNumber();
    }
}
