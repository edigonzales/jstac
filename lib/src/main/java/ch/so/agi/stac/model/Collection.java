package ch.so.agi.stac.model;

import java.util.ArrayList;
import java.util.List;

public class Collection extends STACObject {
    private String stacVersion = "1.0.0";
    private String id;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    
}
