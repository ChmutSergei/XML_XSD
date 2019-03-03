package by.chmut.xml.builder.impl;

import by.chmut.xml.bean.Publication;
import by.chmut.xml.builder.BaseBuilder;
import by.chmut.xml.builder.BuilderException;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.util.List;

public class SAXBuilder implements BaseBuilder {

    private List<Publication> publications;
    private PublicationHandler publicationHandler;
    private XMLReader reader;

    public SAXBuilder() throws BuilderException {
        publicationHandler = new PublicationHandler();
        try {
            reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(publicationHandler);
        } catch (SAXException e) {
            throw new BuilderException("Impossible to init SAX parser", e);
        }
    }

    @Override
    public List<Publication> getPublications() {
        return publications;
    }

    @Override
    public void buildPublications(String fileName) throws BuilderException {
        try {
            reader.parse(fileName);
        } catch (SAXException e) {
            throw new BuilderException("Error SAX parser: ", e);
        } catch (IOException e) {
            throw new BuilderException("Error I/О with SAX parser: ", e);
        }
        publications = publicationHandler.getPublications();
    }
}
