package ch.so.agi.stac.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.so.agi.stac.model.Catalog.CatalogBuilder;
import ch.so.agi.stac.model.Link.MediaType;

public class Collection extends STACObject {
    private String stacVersion = "1.0.0";
    private String id;
    private String description;
    private String title;
    private String href;
    
    public Collection (CollectionBuilder builder) {
        this.id = builder.id;
        this.description = builder.description;
        this.title = builder.title;
        this.href = builder.href;
        
        this.addLink(Link.root(this));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public void addItem(Item item) {
        item.setCollection(this);
        
        item.setRoot(this.getRoot());
        item.setParent(this);
        
        String selfHref = getSelfHref();
        if (selfHref != null) {
            System.out.println("item self href TODO...");
        } 
        
        addLink(Link.item(item));
    }
    
    public void addChild(Collection child) {
        child.setRoot(getRoot());
        child.setParent(this);
        
        String selfHref = this.getSelfHref();
        if (selfHref != null) {
            System.out.println("child self href TODO");
        }
        
        addLink(Link.child(child));
    }
    
    public static class CollectionBuilder {
        private CatalogType type;
        private String stacVersion;
        private String id;
        private String title;
        private String description;
        private String href;
        
        public CollectionBuilder type(CatalogType type) {
            this.type = type; 
            return this;
        }

        public CollectionBuilder version(String version) {
            this.stacVersion = version;
            return this;
        }
        
        public CollectionBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        public CollectionBuilder title(String title) {
            this.title = title;
            return this;
        }

        public CollectionBuilder description(String description) {
            this.description = description;
            return this;
        }
        
        public CollectionBuilder href(String href) {
            this.href = href;
            return this;
        }
        
        public Collection build() {
            return new Collection(this);
        }
    }

    
    
}
