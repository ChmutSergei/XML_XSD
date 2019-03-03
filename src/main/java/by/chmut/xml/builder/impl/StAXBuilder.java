package by.chmut.xml.builder.impl;

import by.chmut.xml.bean.*;
import by.chmut.xml.builder.BaseBuilder;
import by.chmut.xml.builder.BuilderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StAXBuilder implements BaseBuilder {

    private static final Logger logger = LogManager.getLogger();

    private List<Publication> publications = new ArrayList<>();
    private XMLInputFactory inputFactory;

    public StAXBuilder() {
        inputFactory = XMLInputFactory.newInstance();
    }

    @Override
    public List<Publication> getPublications() {
        return publications;
    }

    @Override
    public void buildPublications(String fileName) throws BuilderException {
        FileInputStream inputStream = null;
        XMLStreamReader reader;
        String name;
        try {
            inputStream = new FileInputStream(new File(fileName));
            reader = inputFactory.createXMLStreamReader(inputStream);
            while (reader.hasNext()) {
                int type = reader.next();
                if (type == XMLStreamConstants.START_ELEMENT) {
                    name = reader.getLocalName();
                    if (PapersTagName.valueOf(name.toUpperCase()) == PapersTagName.PUBLICATION) {
                        Publication publication = parsePublication(reader);
                        publications.add(publication);
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new BuilderException("StAX parsing error ", e);
        } catch (FileNotFoundException e) {
            throw new BuilderException("File " + fileName + " not found! ", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.error("Impossible close file " + fileName + " : ", e);
            }
        }
    }

    private Publication parsePublication(XMLStreamReader reader) throws XMLStreamException, BuilderException {
        Publication publication = new Publication();
        publication.setType(reader.getAttributeValue(null, PapersTagName.TYPE.name().toLowerCase()));
        publication.setId(reader.getAttributeValue(null, PapersTagName.ID.name().toLowerCase()));
        publication.setMonthly(reader.getAttributeValue(null, PapersTagName.MONTHLY.name().toLowerCase()));
        String tagName;
        while (reader.hasNext()) {
            int typeOfEvent = reader.next();
            switch (typeOfEvent) {
                case XMLStreamConstants.START_ELEMENT:
                    tagName = reader.getLocalName();
                    PapersTagName tagType = PapersTagName.valueOf(tagName.toUpperCase());
                    switch (tagType) {
                        case TITLE:
                            publication.setTitle(getXMLText(reader));
                            break;
                        case DATE:
                            String text = getXMLText(reader);
                            publication.setDate(LocalDate.parse(text));
                            break;
                        case CHARS:
                            publication.setChars(getXMLChars(reader, publication.getType().toUpperCase()));
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    tagName = reader.getLocalName();
                    PapersTagName tagType2 = PapersTagName.valueOf(tagName.toUpperCase());
                    if (tagType2 == PapersTagName.PUBLICATION) {
                        return publication;
                    }
                    break;
            }
        }
        throw new XMLStreamException("Unknown element in tag Publication");
    }

    private Chars getXMLChars(XMLStreamReader reader, String typeChars) throws XMLStreamException, BuilderException {
        PapersTagName type = PapersTagName.valueOf(typeChars);
        Chars chars = createNewChars(reader, type);
        int typeOfEvent;
        String tagName;
        while (reader.hasNext()) {
            typeOfEvent = reader.next();
            switch (typeOfEvent) {
                case XMLStreamConstants.START_ELEMENT:
                    tagName = reader.getLocalName();
                    PapersTagName tagType = PapersTagName.valueOf(tagName.toUpperCase());
                    switch (tagType) {
                        case COLOR:
                            chars.setColor(Boolean.valueOf(getXMLText(reader)));
                            break;
                        case SIZE:
                            chars.setSize(Integer.valueOf(getXMLText(reader)));
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    tagName = reader.getLocalName();
                    PapersTagName tagType2 = PapersTagName.valueOf(tagName.toUpperCase());
                    if (tagType2 == PapersTagName.CHARS) {
                        return chars;
                    }
                    break;
            }
        }
        throw new XMLStreamException("Unknown element in tag Chars");
    }

    private Chars createNewChars(XMLStreamReader reader, PapersTagName type) throws BuilderException {
        switch (type) {
            case NEWSPAPER:
                NewspaperChars newspaperChars = new NewspaperChars();
                String newspaperIndex = reader.getAttributeValue(null, PapersTagName.INDEX.name().toLowerCase());
                newspaperChars.setIndex(Integer.valueOf(newspaperIndex));
                return newspaperChars;
            case MAGAZINE:
                MagazineChars magazineChars = new MagazineChars();
                String magazineIndex = reader.getAttributeValue(null, PapersTagName.INDEX.name().toLowerCase());
                magazineChars.setIndex(Integer.valueOf(magazineIndex));
                return magazineChars;
            case BOOKLET:
                BookletChars bookletChars = new BookletChars();
                return bookletChars;
            default:
                throw new BuilderException("Unknown type of Chars " + type);
        }
    }

    private String getXMLText(XMLStreamReader reader) throws XMLStreamException {
        String text = null;
        if (reader.hasNext()) {
            reader.next();
            text = reader.getText();
        }
        return text;
    }
}

