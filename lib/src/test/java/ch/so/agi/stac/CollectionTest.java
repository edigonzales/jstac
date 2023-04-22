package ch.so.agi.stac;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.so.agi.stac.jackson.JacksonObjectMapperHolder;
import ch.so.agi.stac.model.Bbox;
import ch.so.agi.stac.model.Catalog;
import ch.so.agi.stac.model.Catalog_V1;
import ch.so.agi.stac.model.Collection;
import ch.so.agi.stac.model.CatalogType;
import ch.so.agi.stac.model.Collection_V1;
import ch.so.agi.stac.model.Interval;
import ch.so.agi.stac.model.Link;
import ch.so.agi.stac.model.LinkMimeType;
import ch.so.agi.stac.model.PublicationType;

public class CollectionTest {
    static Logger log = LoggerFactory.getLogger(CollectionTest.class);
    
    private static final String TEST_OUT="build/test/CatalogTest/";

    @BeforeAll
    public static void setupFolder() {
        new File(TEST_OUT).mkdirs();
    }
    
    
    @Test
    public void dummy() throws Exception {
        // Prepare
        Path outputDirectory = Paths.get(TEST_OUT, "dummy");
        outputDirectory.toFile().mkdirs();
        
        // Run
        Collection collection = new Collection.CollectionBuilder().id("ch.so.afu.abbaustellen").title("Abbaustellen").build();
        System.out.println(collection.getLinks().get(0).getRel());
        
        
//        Catalog catalog = new Catalog.CatalogBuilder()
//                .type(CatalogType.CATALOG)
//                .version("1.0.0")
//                .id("ch.so.geo.stac")
//                .title("Geodaten des Kantons Solothurn")
//                .description("Geodaten des Kantons Solothurn als STAC")
//                .publicationType(PublicationType.SELF_CONTAINED)
//                .outputDirectory(outputDirectory).build();

//        catalog.save();
        
        // Validate
//        ObjectMapper objectMapper = JacksonObjectMapperHolder.getInstance().getObjectMapper();
//        Catalog_V1 resultCatalog = objectMapper.readValue(Paths.get(outputDirectory.toFile().getAbsolutePath(), "catalog.json").toFile(), Catalog_V1.class);
//        
//        assertEquals(resultCatalog.getStacVersion(), "1.0.0");
//        assertEquals(resultCatalog.getId(), "ch.so.geo.stac");
//        assertEquals(resultCatalog.getTitle(), "Geodaten des Kantons Solothurn");
//        assertEquals(resultCatalog.getDescription(), "Geodaten des Kantons Solothurn als STAC");
//        assertEquals(resultCatalog.getType(), CatalogType.CATALOG.toString());
//        
//        List<Link> links = resultCatalog.getLinks();
//        assertTrue(links.size()==1);
//        
//        assertEquals(links.get(0).getRel(), "root");
//        assertEquals(links.get(0).getHref(), "catalog.json");
//        assertEquals(links.get(0).getType(), LinkMimeType.APPLICATION_JSON.toString());
    }
}
