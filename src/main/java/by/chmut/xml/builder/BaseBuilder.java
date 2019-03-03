package by.chmut.xml.builder;

import by.chmut.xml.bean.Publication;

import java.util.List;

public interface BaseBuilder {

    List<Publication> getPublications();
    void buildPublications(String fileName) throws BuilderException;
}
