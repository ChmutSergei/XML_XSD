package by.chmut.xml.builder;

import by.chmut.xml.bean.Publication;
import by.chmut.xml.builder.impl.DOMBuilder;
import by.chmut.xml.builder.impl.SAXBuilder;
import by.chmut.xml.builder.impl.StAXBuilder;

import java.util.List;

public class PublicationBuilder {

    private static BaseBuilder builder = new StAXBuilder();

    public static List<Publication> createPublications(String builderType, String fileName) throws BuilderException {
        setUpBuilder(builderType);
        builder.buildPublications(fileName);
        return builder.getPublications();
    }

    private static void setUpBuilder(String type) throws BuilderException {
        BuilderType builderType = BuilderType.valueOf(type.trim().toUpperCase());
        switch (builderType) {
            case SAX:
                builder = new SAXBuilder();
                break;
            case STAX:
                builder = new StAXBuilder();
                break;
            case DOM:
                builder = new DOMBuilder();
                break;
            default:
                throw new BuilderException("Error no such builder type");
        }
    }
}
