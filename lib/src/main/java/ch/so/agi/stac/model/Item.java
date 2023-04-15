package ch.so.agi.stac.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    private String type = "Feature";
    
    @JsonProperty("stac_version")
    private String stacVersion;

    private String id;

//    private Geometry geometry;
    
    private Bbox bbox;

    private Map<String,String> properties;
    
    private List<Link> links = new ArrayList<>();
    
    private Link parentLink;
    
//    private Map<String, Asset> assets;
    
    private String collection;
    
    public Bbox getBbox() {
        return bbox;
    }

    public void setBbox(Bbox bbox) {
        this.bbox = bbox;
    }
    
    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public Link getParentLink() {
        return parentLink;
    }

    public void setParentLink(Link parentLink) {
        this.parentLink = parentLink;
    }

    public void addLink(Link link) {
        links.add(link);
    }
    
    // fluent api
    
    public Item id(String id) {
        this.id = id;
        return this;
    }
    
    public Item version(String version) {
        this.stacVersion = version;
        return this;
    }
    
    public Item bbox(Bbox bbox) {
        this.bbox = bbox;
        return this;
    }
}
