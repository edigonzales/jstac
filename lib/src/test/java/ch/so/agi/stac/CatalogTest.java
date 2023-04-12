package ch.so.agi.stac;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.so.agi.stac.model.Catalog;
import ch.so.agi.stac.model.PublicationType;

public class CatalogTest {
    static Logger log = LoggerFactory.getLogger(CatalogTest.class);
    
    @Test
    public void dummy() throws Exception {
        Catalog catalog = new Catalog();
        catalog.version("1.0.0")
                .id("ch.so.geo.stac")
                .title("Geodaten des Kantons Solothurn")
                .description("Geodaten des Kantons Solothurn als STAC");
        
        catalog.setSelfHref("http://localhost:8080/stac/catalog.json");
        catalog.save(PublicationType.SELF_CONTAINED, new File("/Users/stefan/tmp/"));
    }

}
