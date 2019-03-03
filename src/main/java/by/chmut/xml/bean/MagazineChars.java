package by.chmut.xml.bean;

public class MagazineChars extends Chars {

    private static final String GLOSSY = "yes";
    private int index;

    public String getGlossy() {
        return GLOSSY;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int value) {
        this.index = value;
    }

}
