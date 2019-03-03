package by.chmut.xml.builder.impl;

import by.chmut.xml.bean.*;
import by.chmut.xml.builder.BaseBuilder;
import by.chmut.xml.builder.BuilderException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static by.chmut.xml.builder.impl.PapersTagName.*;

public class DOMBuilder implements BaseBuilder {

    private List<Publication> publications;
    private DocumentBuilder documentBuilder;

    public DOMBuilder() throws BuilderException {
        this.publications = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new BuilderException("Error when configuring DOM parser : ", e);
        }
    }

    @Override
    public List<Publication> getPublications() {
        return publications;
    }

    @Override
    public void buildPublications(String fileName) throws BuilderException {
        Document document;
        try {
            document = documentBuilder.parse(fileName);
            Element root = document.getDocumentElement();
            NodeList publicationsList = root.getElementsByTagName(PUBLICATION.name().toLowerCase());
            for (int i = 0; i < publicationsList.getLength(); i++) {
                Element publicationElement = (Element) publicationsList.item(i);
                Publication publication = buildPublication(publicationElement);
                publications.add(publication);
            }
        } catch (IOException e) {
            throw new BuilderException("File or I/O error: ", e);
        } catch (SAXException e) {
            throw new BuilderException("Error when parsing with DOM parser: ", e);
        }
    }

    private Publication buildPublication(Element element) throws BuilderException {
        Publication publication = new Publication();
        publication.setType(element.getAttribute(TYPE.name().toLowerCase()));
        publication.setId(element.getAttribute(ID.name().toLowerCase()));
        publication.setMonthly(element.getAttribute(MONTHLY.name().toLowerCase()));
        publication.setTitle(getElementTextContent(element, TITLE.name().toLowerCase()));
        String text = "";
        text = getElementTextContent(element, DATE.name().toLowerCase());
        publication.setDate(LocalDate.parse(text));
        Element charsElement = (Element) element.getElementsByTagName(CHARS.name().toLowerCase()).item(0);
        String charsType = publication.getType();
        Chars chars = buildChars(charsType, charsElement);
        publication.setChars(chars);
        return publication;
    }

    private Chars buildChars(String charsType, Element charsElement) throws BuilderException {
        PapersTagName type = PapersTagName.valueOf(charsType.toUpperCase());
        Chars chars;
        switch (type) {
            case NEWSPAPER:
                NewspaperChars newspaperChars = new NewspaperChars();
                String newspaperIndex = charsElement.getAttribute(INDEX.name().toLowerCase());
                newspaperChars.setIndex(Integer.valueOf(newspaperIndex));
                chars = newspaperChars;
                break;
            case MAGAZINE:
                MagazineChars magazineChars = new MagazineChars();
                String magazineIndex = charsElement.getAttribute(INDEX.name().toLowerCase());
                magazineChars.setIndex(Integer.valueOf(magazineIndex));
                chars = magazineChars;
                break;
            case BOOKLET:
                chars = new BookletChars();
                break;
            default:
                throw new BuilderException("Unknown type of Chars " + type);
        }
        chars.setColor(Boolean.valueOf(getElementTextContent(charsElement, COLOR.name().toLowerCase())));
        chars.setSize(Integer.valueOf(getElementTextContent(charsElement, SIZE.name().toLowerCase())));
        return chars;
    }

    private static String getElementTextContent(Element element, String childName) {
        NodeList nodeList = element.getElementsByTagName(childName);
        Node node = nodeList.item(0);
        return node.getTextContent();
    }
}