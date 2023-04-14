package ch.so.agi.stac.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class Link {
    private String rel;
    
    private String href;
    
    private LinkMimeType type;
    
    private String title;
    
    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getType() {
        return type.toString();
    }

    public void setType(String type) {
        this.type = LinkMimeType.fromString(type);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    // fluent api
    
    public Link rel(String rel) {
        this.rel = rel;
        return this;
    }
    
    public Link href(String href) {
        this.href = href;
        return this;
    }
    
    public Link type(LinkMimeType type) {
        this.type = type;
        return this;
    }
    
    public Link title(String title) {
        this.title = title;
        return this;
    }
}
