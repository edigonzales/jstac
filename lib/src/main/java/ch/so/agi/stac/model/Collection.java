package ch.so.agi.stac.model;

public class Collection extends Catalog {
    
    public Collection() {
        super.type = CatalogType.COLLECTION;
        super.fileName = "collection.json";
    }

}
