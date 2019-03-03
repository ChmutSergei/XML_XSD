package by.chmut.xml.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Papers {

    private List<Publication> publication;

    public Papers() {
        publication = new ArrayList<>();
    }

    public boolean add(Publication publication) {
        return this.publication.add(publication);
    }

    public List<Publication> getPublication() {
        return Collections.unmodifiableList(this.publication);
    }
}
