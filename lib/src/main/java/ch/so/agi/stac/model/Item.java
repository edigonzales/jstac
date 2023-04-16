package ch.so.agi.stac.model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ch.so.agi.stac.model.Link.LinkBuilder;
import ch.so.agi.stac.model.Link.MediaType;

public class Item extends STACObject {
    private String type = "Feature";
    private String stacVersion = "1.0.0";
    private String id;
//    private Geometry geometry;
    private Bbox bbox;
    private ZonedDateTime datetime ;
    private Map<String,String> properties;
//    private Map<String, Asset> assets;
    private Object collection;
    private String href;
    
    private String collectionId;
    
    public Item(ItemBuilder builder) {
        this.id = builder.id;
        this.bbox = builder.bbox;
        this.datetime = builder.datetime;
        this.properties = builder.properties;
        this.collection = builder.collection;
        this.href = builder.href;
        
        if (collection instanceof STACObject) {
            setCollection((Collection) collection);
        } else {
            this.collectionId = (String) collection;
        }
    }
    
    public void setCollection(Collection collection) {
        this.removeLinks(RelType.COLLECTION);
        this.collectionId = null;
        if (collection != null) {
            Link collectionLink = new Link.LinkBuilder().rel(RelType.COLLECTION)
                    .target(collection)
                    .mediaType(MediaType.APPLICATION_JSON)
                    .build();
            this.addLink(collectionLink);
            this.collectionId = collection.getId();
        }
    }
    
    public static class ItemBuilder {
        private String id;
        private Bbox bbox;
        private ZonedDateTime datetime;
        private Map<String,String> properties;
        private Object collection;
        private String href;
        
        public ItemBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        public ItemBuilder bbox(Bbox bbox) {
            this.bbox = bbox;
            return this;
        }
        
        public ItemBuilder datetime(ZonedDateTime datetime) {
            this.datetime = datetime;
            return this;
        }
        
        public ItemBuilder properties(Map<String,String> properties) {
            this.properties = properties;
            return this;
        }
        
        public ItemBuilder collection(Object collection) {
            this.collection = collection;
            return this;
        }
        
        public ItemBuilder href(String href) {
            this.href = href;
            return this;
        }
        
        public Item build() {
            return new Item(this);
        }
    }
}
