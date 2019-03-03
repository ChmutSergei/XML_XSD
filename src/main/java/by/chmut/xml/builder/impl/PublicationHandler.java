package by.chmut.xml.builder.impl;

import by.chmut.xml.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static by.chmut.xml.builder.impl.PapersTagName.*;

public class PublicationHandler extends DefaultHandler {

    private static final Logger logger = LogManager.getLogger();
    private static final int NUMBER_ATTRIBUTE_TYPE = 0;
    private static final int NUMBER_ATTRIBUTE_ID = 1;
    private static final int NUMBER_ATTRIBUTE_MONTHLY = 2;
    private static final int NUMBER_ATTRIBUTE_INDEX = 2;

    private List<Publication> publications;
    private Publication current;
    private Chars currentChars;
    private StringBuilder text;
    private boolean isExistIndex;

    public PublicationHandler() {
        publications = new ArrayList<>();
    }

    public List<Publication> getPublications() {
        return publications;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes){
        if (PUBLICATION.name().equalsIgnoreCase(localName)) {
            current = new Publication();
            PapersTagName type = PapersTagName.valueOf(attributes.getValue(NUMBER_ATTRIBUTE_TYPE).toUpperCase());
            createNewChars(type);
            current.setId(attributes.getValue(NUMBER_ATTRIBUTE_ID));
            current.setMonthly(attributes.getValue(NUMBER_ATTRIBUTE_MONTHLY));
        } else if (isExistIndex && CHARS.name().equalsIgnoreCase(localName)) {
            setIndex(attributes.getValue(NUMBER_ATTRIBUTE_INDEX));
        }
        text = new StringBuilder();
    }

    public void characters(char[] buffer, int start, int length) {
        text.append(buffer, start, length);
    }

    public void endElement(String uri, String localName, String qName) {

        PapersTagName tagName = PapersTagName.valueOf(localName.toUpperCase());
        switch (tagName) {
            case TITLE:
                current.setTitle(text.toString());
                break;
            case DATE:
                current.setDate(LocalDate.parse(text.toString()));
                break;
            case COLOR:
                currentChars.setColor(Boolean.valueOf(text.toString()));
                break;
            case SIZE:
                currentChars.setSize(Integer.parseInt(text.toString()));
                break;
            case CHARS:
                current.setChars(currentChars);
                break;
            case PUBLICATION:
                publications.add(current);
                break;
        }

    }

    private void createNewChars(PapersTagName type) {
        switch (type) {
            case NEWSPAPER:
                current.setType(NEWSPAPER.name().toLowerCase());
                currentChars = new NewspaperChars();
                isExistIndex = true;
                break;
            case MAGAZINE:
                current.setType(MAGAZINE.name().toLowerCase());
                currentChars = new MagazineChars();
                isExistIndex = true;
                break;
            case BOOKLET:
                current.setType(BOOKLET.name().toLowerCase());
                currentChars = new BookletChars();
                isExistIndex = false;
                break;
            default:
                logger.error("Incorrect type publication " + type);
        }
    }

    private void setIndex(String value) {
        if (current.getType().equals(NEWSPAPER.name().toLowerCase())) {
            ((NewspaperChars) currentChars).setIndex(Integer.parseInt(value));
        } else {
            ((MagazineChars) currentChars).setIndex(Integer.parseInt(value));
        }
    }

    public void warning(SAXParseException exception) {
        logger.warn("WARNING: line " + exception.getLineNumber() + ": ", exception);
    }

    public void error(SAXParseException exception) {
        logger.error("ERROR: line " + exception.getLineNumber() + ": ", exception);
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        logger.fatal("FATAL: line " + exception.getLineNumber() + ": ", exception);
    }
}
