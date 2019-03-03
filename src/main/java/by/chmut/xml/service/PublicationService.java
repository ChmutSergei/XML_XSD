package by.chmut.xml.service;

import by.chmut.xml.bean.Publication;

import java.util.List;

public interface PublicationService {

    List<Publication> createPublication(String parserVersion, String fileName) throws ServiceException;
}
