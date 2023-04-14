package ch.so.agi.stac.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import ch.so.agi.stac.jackson.Interval;

public class Collection extends Catalog {
    
    public Collection() {
        super.type = CatalogType.COLLECTION;
        super.fileName = "collection.json";
    }
    
    private String license;
    
    @JsonProperty("extent")
    private Bbox bbox;
    
    @JsonProperty("temporal")
    private Interval interval;
    
    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Bbox getBbox() {
        return bbox;
    }

    public void setBbox(Bbox bbox) {
        this.bbox = bbox;
    }
        
    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }
    
    // fluent api

    public Collection bbox(Bbox bbox) {
        this.bbox = bbox;
        return this;
    }
    
    public Collection interval(Interval interval) {
        this.interval = interval;
        return this;
    }
    
    public Collection license(String license) {
        this.license = license;
        return this;
    }
    
    @Override
    public Collection id(String id) {
        super.id(id);
        return this;
    }
    
    @Override
    public Collection version(String version) {
        super.version(version);
        return this;
    }

    @Override
    public Collection title(String title) {
        super.title(title);
        return this;
    }

    @Override
    public Collection description(String description) {
        super.description(description);
        return this;
    }
}
