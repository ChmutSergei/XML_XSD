package by.chmut.xml.service;

import by.chmut.xml.bean.Publication;
import by.chmut.xml.builder.BuilderException;
import by.chmut.xml.builder.PublicationBuilder;
import by.chmut.xml.service.validator.XmlValidator;
import org.apache.commons.io.FilenameUtils;

import java.util.List;

public class PublicationServiceImpl implements PublicationService{

    private static final String XML_TYPE = "xml";

    @Override
    public List<Publication> createPublication(String parserVersion, String fileName) throws ServiceException {
        if (!FilenameUtils.getExtension(fileName).equals(XML_TYPE)) {
            throw new ServiceException("Only XML files accepted");
        }
        if (!XmlValidator.validate(fileName)) {
            throw new ServiceException("The XML file does not conform to the XSD schema");
        }
        List<Publication> result;
        try {
            result = PublicationBuilder.createPublications(parserVersion,fileName);
        } catch (BuilderException exception) {
            throw new ServiceException(exception);
        }
        return result;
    }
}
