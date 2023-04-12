package ch.so.agi.stac.model;

import java.io.File;
import java.io.IOException;
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
    
    @JsonProperty("type")
    private CatalogType type = CatalogType.CATALOG;

    @JsonProperty("title")
    private String title;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("links")
    private Collection<Link> links = new ArrayList<>();
    
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

    
    public Catalog links(Collection<Link> links) {
        this.links = links;
        return this;
    }
    
    public void save(File file) throws StreamWriteException, DatabindException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(file, this);
    }
}
