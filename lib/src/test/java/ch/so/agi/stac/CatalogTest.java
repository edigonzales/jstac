package ch.so.agi.stac;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.so.agi.stac.model.Catalog;

public class CatalogTest {
    static Logger log = LoggerFactory.getLogger(CatalogTest.class);
    
    @Test
    public void dummy() throws Exception {
        Catalog catalog = new Catalog();
        catalog.version("1.0.0")
                .id("ch.so.geo.stac")
                .title("Geodaten des Kantons Solothurn")
                .description("Geodaten des Kantons Solothurn als STAC");
        
        catalog.save(new File("/Users/stefan/tmp/catalog.json"));
    }

}
