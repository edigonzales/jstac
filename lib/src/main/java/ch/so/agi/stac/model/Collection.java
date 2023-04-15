package ch.so.agi.stac.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.so.agi.stac.jackson.Interval;
import ch.so.agi.stac.jackson.JacksonObjectMapperHolder;

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
    
    
    public static Collection readFromFile(File collectionFile) throws IOException {
        ObjectMapper objectMapper = JacksonObjectMapperHolder.getInstance().getObjectMapper();
        Collection collection = objectMapper.readValue(collectionFile, Collection.class);

        // TODO items... müssen eruiert werden (rel=item). Theoretisch ist ja auch wieder rel=child (= collection?)
        // möglich. Dokumentieren, dass das mal out-of-scope ist.
        
        return collection;
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
    
    @Override
    public Collection publicationType(PublicationType publicationType) {
        super.publicationType(publicationType);
        return this;
    }
    
    @Override
    public Collection selfHref(String selfHref) {
        super.selfHref(selfHref);
        return this;
    }
    
    @Override
    public Collection outputDirectory(Path outputDirectory) {
        super.outputDirectory(outputDirectory);
        return this;
    } 


}
