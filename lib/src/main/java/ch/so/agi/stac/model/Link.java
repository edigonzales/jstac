package ch.so.agi.stac.model;

public class Link {
    private String rel;
    private String href;
    private String type;
    private String title;
    
    public Link rel(String rel) {
        this.rel = rel;
        return this;
    }
    
    public Link href(String href) {
        this.href = href;
        return this;
    }
    
    public Link type(String type) {
        this.type = type;
        return this;
    }
    
    public Link title(String title) {
        this.title = title;
        return this;
    }
}
