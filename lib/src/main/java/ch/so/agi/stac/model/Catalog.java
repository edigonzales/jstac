package ch.so.agi.stac.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Catalog {
    private CatalogType type = CatalogType.CATALOG;
    
    private String id;
    
    @JsonProperty("stac_version")
    private String stacVersion;
    
    private String description;
    //private Collection<Link> links = new ArrayList<>();
}
