package ch.so.agi.stac.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Catalog {   
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("stac_version")
    private String stacVersion;
    
    private CatalogType type = CatalogType.CATALOG;

    @JsonProperty("title")
    private String title;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("links")
    private Collection<Link> links = new ArrayList<>();
    
    private String selfHref;
    
    private String fileName = "catalog.json";
    
    public String getType() {
        return type.toString();
    }

    public void setSelfHref(String selfHref) {
        this.selfHref = selfHref;
    }
    
    public Catalog id(String id) {
        this.id = id;
        return this;
    }
    
    public Catalog version(String version) {
        this.stacVersion = version;
        return this;
    }

    public Catalog title(String title) {
        this.title = title;
        return this;
    }

    public Catalog description(String description) {
        this.description = description;
        return this;
    }
    
    // TODO nötig?
    public Catalog links(Collection<Link> links) {
        this.links = links;
        return this;
    }
    
    public void save(PublicationType publicationType, File file) throws StreamWriteException, DatabindException, IOException {
        if (!publicationType.equals(PublicationType.SELF_CONTAINED)) {
            System.out.println("*********");
            
            Link selfLink = new Link();
            selfLink.rel("self").href(selfHref).type(LinkMimeType.APPLICATION_JSON);
            links.add(selfLink);
            
            fileName = selfHref.substring(selfHref.lastIndexOf('/')+1, selfHref.length());
        }
        
        File resultFile = Paths.get(file.getAbsolutePath(), fileName).toFile();
        
        ObjectMapper objectMapper = new ObjectMapper(); // TODO: Singleton o.ä.?
        objectMapper.writeValue(resultFile, this);
    }
}
