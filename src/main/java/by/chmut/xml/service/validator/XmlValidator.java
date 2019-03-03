package by.chmut.xml.service.validator;

import by.chmut.xml.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.net.URL;

public class XmlValidator {

    private static final Logger logger = LogManager.getLogger();
    private static final String SCHEMA_NAME = "/papers.xsd";

    public static boolean validate(String fileName) throws ServiceException {
        Boolean result;
        try {
            result = validation(fileName);
        } catch (SAXException | IOException e) {
            logger.error(fileName + " is not valid because " + e.getMessage());
            return false;
        }
        return result;
    }

    private static boolean validation(String fileName) throws SAXException, IOException, ServiceException {
        String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
        SchemaFactory factory = SchemaFactory.newInstance(language);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(SCHEMA_NAME);
        if (url == null) {
            throw new ServiceException("Incorrect url file path for validation");
        }
        Schema schema = factory.newSchema(url);
        ErrorHandler handler = new ErrorHandler();
        Validator validator = schema.newValidator();
        validator.setErrorHandler(handler);
        Source source = new StreamSource(fileName);
        validator.validate(source);
        if (handler.getError() != null) {
            handler.setError(null);
            return false;
        }
        return true;
    }
}
