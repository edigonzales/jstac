package ch.so.agi.stac.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class Link {
    @JsonProperty("rel")
    private String rel;
    
    @JsonProperty("href")
    private String href;
    
    private LinkMimeType type;
    
    @JsonProperty("title")
    private String title;
    
    public String getType() {
        return type.toString();
    }

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
