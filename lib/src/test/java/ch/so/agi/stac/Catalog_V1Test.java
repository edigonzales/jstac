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

import ch.so.agi.stac.jackson.Interval;
import ch.so.agi.stac.jackson.JacksonObjectMapperHolder;
import ch.so.agi.stac.model.Bbox;
import ch.so.agi.stac.model.Catalog_V1;
import ch.so.agi.stac.model.CatalogType;
import ch.so.agi.stac.model.Collection;
import ch.so.agi.stac.model.Link;
import ch.so.agi.stac.model.LinkMimeType;
import ch.so.agi.stac.model.PublicationType;

public class Catalog_V1Test {
    static Logger log = LoggerFactory.getLogger(Catalog_V1Test.class);
    
    private static final String TEST_OUT="build/test/CatalogTest/";

    @BeforeAll
    public static void setupFolder() {
        new File(TEST_OUT).mkdirs();
    }
    
    // TODO
    // - absoluter Katalog (nur noch relations prüfen)
    // - Intervall mit nur einem Wert
    
    
    @Test
    public void empty_catalog_self_Ok() throws Exception {
        // Prepare
        Path outputDirectory = Paths.get(TEST_OUT, "empty_catalog_Ok");
        outputDirectory.toFile().mkdirs();
        
        // Run
        Catalog_V1 catalog = new Catalog_V1();
        catalog.version("1.0.0")
                .id("ch.so.geo.stac")
                .title("Geodaten des Kantons Solothurn")
                .description("Geodaten des Kantons Solothurn als STAC")
                .publicationType(PublicationType.SELF_CONTAINED)
                .outputDirectory(outputDirectory);

        catalog.save();
        
        // Validate
        ObjectMapper objectMapper = JacksonObjectMapperHolder.getInstance().getObjectMapper();
        Catalog_V1 resultCatalog = objectMapper.readValue(Paths.get(outputDirectory.toFile().getAbsolutePath(), "catalog.json").toFile(), Catalog_V1.class);
        
        assertEquals(resultCatalog.getStacVersion(), "1.0.0");
        assertEquals(resultCatalog.getId(), "ch.so.geo.stac");
        assertEquals(resultCatalog.getTitle(), "Geodaten des Kantons Solothurn");
        assertEquals(resultCatalog.getDescription(), "Geodaten des Kantons Solothurn als STAC");
        assertEquals(resultCatalog.getType(), CatalogType.CATALOG.toString());
        
        List<Link> links = resultCatalog.getLinks();
        assertTrue(links.size()==1);
        
        assertEquals(links.get(0).getRel(), "root");
        assertEquals(links.get(0).getHref(), "catalog.json");
        assertEquals(links.get(0).getType(), LinkMimeType.APPLICATION_JSON.toString());
    }
    
    @Test
    public void collection_absolut_Ok() throws Exception {
        // Prepare
        Path outputDirectory = Paths.get(TEST_OUT, "collection_absolut_Ok");
        outputDirectory.toFile().mkdirs();
        
        double westlimit = 7.34;
        String endInterval = "2023-04-13";
        String rootSelfHref = "http://localhost:8080/stac/";

        // Run
        Bbox bbox = new Bbox().west(westlimit)
                .south(47.074299169536175)
                .east(8.03269288687543)
                .north(47.50119805032911);       
        
        Interval interval = new Interval().endInterval(endInterval);

        Collection collection = new Collection();
        collection.version("1.0.0")
                .id("ch.so.afu.abbaustellen")
                .title("Abbaustellen")
                .description(
                        "Die Abbaustellen umfassen die Flächen folgender Objekte: <br/><ul><li>sämtliche grösseren Abbaugebiete (Kiesgruben, Kalksteinbrüche sowie Tongruben), für welche ein Gestaltungsplan vorliegt. Die dargestellten Flächen umfassen jeweils den gesamten Perimeter der genehmigten Gestaltungspläne, und nicht einzelne Abbauetappen.</li><li>Kleinabbaustellen. Es handelt sich üblicherweise um kleinere, gemeindeeigene Mergelgruben, in welchen Material für den Bau und Unterhalt von Wald- und Flurwegen abgebaut wird. Kleinabbaustellen erfordern keinen Gestaltungsplan. Die dargestellten Flächen umfassen hier jeweils den auf Stufe Bau-, bzw. Abbaubewilligung genehmigten Perimeter.</li><li>alle künftigen Erweiterungs- und Ersatzstandorte, welche im kantonalen Richtplan (Kap. E-3.1 bis E-3.4) enthalten sind.</li></ul><br>Die Flächen wurden von verschiedenen Planvorlagen und bestehenden Flächendaten mit unterschiedlichem Massstab digitalisiert, bzw. übernommen.")
                .license("https://files.geo.so.ch/nutzungsbedingungen.html")
                .bbox(bbox)
                .interval(interval)
                .selfHref(rootSelfHref)
                .publicationType(PublicationType.ABSOLUTE_PUBLISHED)
                .outputDirectory(outputDirectory);

        collection.save();

        // Validate
        ObjectMapper objectMapper = JacksonObjectMapperHolder.getInstance().getObjectMapper();
        Collection resultCollection = objectMapper.readValue(Paths.get(outputDirectory.toFile().getAbsolutePath(), "collection.json").toFile(), Collection.class);

        assertTrue(resultCollection.getDescription().startsWith("Die Abbaustellen umfassen die Flächen"));

        List<Link> links = resultCollection.getLinks();
        assertTrue(links.size()==2);
        
        for (Link link : links) {
            String rel = link.getRel();
            if (rel.equalsIgnoreCase("self")) {
                assertEquals(link.getHref(), "http://localhost:8080/stac/collection.json");
            } else if (rel.equalsIgnoreCase("root")) {
                assertEquals(link.getHref(), "http://localhost:8080/stac/collection.json");
            } else {
                throw new Exception("unknown link relation found");
            }
        }

        Bbox resultBbox = resultCollection.getBbox();
        assertEquals(resultBbox.getWest(), westlimit);

        Interval resultInterval = resultCollection.getInterval();
        assertEquals(resultInterval.getEndInterval().toLocalDate().format(DateTimeFormatter.ISO_DATE), endInterval);
    }
    
    @Test
    public void catalog_with_collection_absolute_Ok() throws Exception {
        // Prepare
        Path outputDirectory = Paths.get(TEST_OUT, "catalog_with_collection_absolute_Ok");
        outputDirectory.toFile().mkdirs();
        
        double westlimit = 7.34;
        String endInterval = "2023-04-13";
        String selfHref = "http://localhost:8080/stac/";
        
        // Run
        Catalog_V1 catalog = new Catalog_V1();
        catalog.version("1.0.0")
                .id("ch.so.geo.stac")
                .title("Geodaten des Kantons Solothurn")
                .description("Geodaten des Kantons Solothurn als STAC")
                .selfHref(selfHref)
                .publicationType(PublicationType.ABSOLUTE_PUBLISHED)
                .outputDirectory(outputDirectory);
        
        Bbox bbox = new Bbox().west(westlimit)
                .south(47.074299169536175)
                .east(8.03269288687543)
                .north(47.50119805032911);       
        
        Interval interval = new Interval().endInterval(endInterval);

        Collection collection = new Collection();
        collection.version("1.0.0")
                .id("ch.so.afu.abbaustellen")
                .title("Abbaustellen")
                .description(
                        "Die Abbaustellen umfassen die Flächen folgender Objekte: <br/><ul><li>sämtliche grösseren Abbaugebiete (Kiesgruben, Kalksteinbrüche sowie Tongruben), für welche ein Gestaltungsplan vorliegt. Die dargestellten Flächen umfassen jeweils den gesamten Perimeter der genehmigten Gestaltungspläne, und nicht einzelne Abbauetappen.</li><li>Kleinabbaustellen. Es handelt sich üblicherweise um kleinere, gemeindeeigene Mergelgruben, in welchen Material für den Bau und Unterhalt von Wald- und Flurwegen abgebaut wird. Kleinabbaustellen erfordern keinen Gestaltungsplan. Die dargestellten Flächen umfassen hier jeweils den auf Stufe Bau-, bzw. Abbaubewilligung genehmigten Perimeter.</li><li>alle künftigen Erweiterungs- und Ersatzstandorte, welche im kantonalen Richtplan (Kap. E-3.1 bis E-3.4) enthalten sind.</li></ul><br>Die Flächen wurden von verschiedenen Planvorlagen und bestehenden Flächendaten mit unterschiedlichem Massstab digitalisiert, bzw. übernommen.")
                .license("https://files.geo.so.ch/nutzungsbedingungen.html")
                .bbox(bbox)
                .interval(interval);
        catalog.addChild(collection);

        catalog.save();
        
        // Validate
        ObjectMapper objectMapper = JacksonObjectMapperHolder.getInstance().getObjectMapper();
        Catalog_V1 resultCatalog = objectMapper.readValue(Paths.get(outputDirectory.toFile().getAbsolutePath(), "catalog.json").toFile(), Catalog_V1.class);
        
        List<Link> links = resultCatalog.getLinks();
        assertTrue(links.size()==3);
        
        String childHref = null;
        for (Link link : links) {
            String rel = link.getRel();
            if (rel.equalsIgnoreCase("self")) {
                assertEquals(link.getHref(), "http://localhost:8080/stac/catalog.json");
            } else if (rel.equalsIgnoreCase("root")) {
                assertEquals(link.getHref(), "http://localhost:8080/stac/catalog.json");
            } else if (rel.equalsIgnoreCase("child")) {
                assertEquals(link.getHref(), "http://localhost:8080/stac/ch.so.afu.abbaustellen/collection.json");
                childHref = link.getHref();
            } else {
                throw new Exception("unknown link relation found");
            }
        }
        assertTrue(childHref!=null);
        
        String childFilePathElement = childHref.replace(selfHref, "");
        
        File childFile = Paths.get(outputDirectory.toFile().getAbsolutePath(), childFilePathElement).toFile();
        Collection resultCollection = objectMapper.readValue(childFile, Collection.class);

        List<Link> collectionLinks = resultCollection.getLinks();
        assertTrue(collectionLinks.size()==3);

        boolean hasParent = false;
        for (Link link : collectionLinks) {
            String rel = link.getRel();
            if (rel.equalsIgnoreCase("self")) {
                assertEquals(link.getHref(), "http://localhost:8080/stac/ch.so.afu.abbaustellen/collection.json");
            } else if (rel.equalsIgnoreCase("root")) {
                assertEquals(link.getHref(), "http://localhost:8080/stac/catalog.json");
            } else if (rel.equalsIgnoreCase("parent")) {
                assertEquals(link.getHref(), "http://localhost:8080/stac/catalog.json");
                hasParent = true;
            } else {
                throw new Exception("unknown link relation found");
            }
        }
        assertTrue(hasParent);
    }
    
    @Test
    public void flushChildren_Ok() throws Exception {
        // Prepare
        Path outputDirectory = Paths.get(TEST_OUT, "flushChildren_Ok");
        outputDirectory.toFile().mkdirs();
        
        String endInterval = "2023-04-13";
        String selfHref = "http://localhost:8080/stac/";

        // Run
        Catalog_V1 catalog = new Catalog_V1();
        catalog.version("1.0.0")
                .id("ch.so.geo.stac")
                .title("Geodaten des Kantons Solothurn")
                .description("Geodaten des Kantons Solothurn als STAC")
                .selfHref(selfHref)
                .publicationType(PublicationType.ABSOLUTE_PUBLISHED)
                .outputDirectory(outputDirectory);
        
        Bbox bbox = new Bbox().west(7.340693492284002)
                .south(47.074299169536175)
                .east(8.03269288687543)
                .north(47.50119805032911);       
        
        Interval interval = new Interval().endInterval(endInterval);

        {
            Collection collection = new Collection();
            collection.version("1.0.0")
                    .id("ch.so.afu.abbaustellen")
                    .title("Abbaustellen")
                    .description(
                            "Die Abbaustellen umfassen die Flächen folgender Objekte: <br/><ul><li>sämtliche grösseren Abbaugebiete (Kiesgruben, Kalksteinbrüche sowie Tongruben), für welche ein Gestaltungsplan vorliegt. Die dargestellten Flächen umfassen jeweils den gesamten Perimeter der genehmigten Gestaltungspläne, und nicht einzelne Abbauetappen.</li><li>Kleinabbaustellen. Es handelt sich üblicherweise um kleinere, gemeindeeigene Mergelgruben, in welchen Material für den Bau und Unterhalt von Wald- und Flurwegen abgebaut wird. Kleinabbaustellen erfordern keinen Gestaltungsplan. Die dargestellten Flächen umfassen hier jeweils den auf Stufe Bau-, bzw. Abbaubewilligung genehmigten Perimeter.</li><li>alle künftigen Erweiterungs- und Ersatzstandorte, welche im kantonalen Richtplan (Kap. E-3.1 bis E-3.4) enthalten sind.</li></ul><br>Die Flächen wurden von verschiedenen Planvorlagen und bestehenden Flächendaten mit unterschiedlichem Massstab digitalisiert, bzw. übernommen.")
                    .license("https://files.geo.so.ch/nutzungsbedingungen.html")
                    .bbox(bbox)
                    .interval(interval);
                    
            catalog.addChild(collection);
            catalog.flushChildren();
        }

        {
            Collection collection = new Collection();
            collection.version("1.0.0")
                    .id("ch.so.agi.av.administrative_einteilung")
                    .title("Administrative Einteilung AV und Grundbuch")
                    .description(
                            "Administrativen Einteilungen der amtlichen Vermessung und des Grundbuchs.")
                    .license("https://files.geo.so.ch/nutzungsbedingungen.html")
                    .bbox(bbox)
                    .interval(interval);
                    
            catalog.addChild(collection);
            catalog.flushChildren();
        }
        
        catalog.save();

        // Validate
        ObjectMapper objectMapper = JacksonObjectMapperHolder.getInstance().getObjectMapper();
        {
            Catalog_V1 resultCatalog = objectMapper.readValue(Paths.get(outputDirectory.toFile().getAbsolutePath(), "catalog.json").toFile(), Catalog_V1.class);
            
            List<Link> links = resultCatalog.getLinks();
            assertTrue(links.size()==4);
            
            int childHrefCount = 0;
            for (Link link : links) {
                String rel = link.getRel();
                if (rel.equalsIgnoreCase("self")) {
                    assertEquals(link.getHref(), "http://localhost:8080/stac/catalog.json");
                } else if (rel.equalsIgnoreCase("root")) {
                    assertEquals(link.getHref(), "http://localhost:8080/stac/catalog.json");
                } else if (rel.equalsIgnoreCase("child")) {
                    childHrefCount++;
                    assertTrue(link.getHref().equalsIgnoreCase("http://localhost:8080/stac/ch.so.agi.av.administrative_einteilung/collection.json") || link.getHref().equalsIgnoreCase("http://localhost:8080/stac/ch.so.afu.abbaustellen/collection.json"));
                } else {
                    throw new Exception("unknown link relation found");
                }
            }
            assertEquals(childHrefCount,2);
        }
        
        {
            File childFile = Paths.get(outputDirectory.toFile().getAbsolutePath(), "ch.so.agi.av.administrative_einteilung/collection.json").toFile();
            Collection resultCollection = objectMapper.readValue(childFile, Collection.class);

            List<Link> collectionLinks = resultCollection.getLinks();
            assertTrue(collectionLinks.size()==3);

            boolean hasParent = false;
            for (Link link : collectionLinks) {
                String rel = link.getRel();
                if (rel.equalsIgnoreCase("self")) {
                    assertEquals(link.getHref(), "http://localhost:8080/stac/ch.so.agi.av.administrative_einteilung/collection.json");
                } else if (rel.equalsIgnoreCase("root")) {
                    assertEquals(link.getHref(), "http://localhost:8080/stac/catalog.json");
                } else if (rel.equalsIgnoreCase("parent")) {
                    assertEquals(link.getHref(), "http://localhost:8080/stac/catalog.json");
                    hasParent = true;
                } else {
                    throw new Exception("unknown link relation found");
                }
            }
            assertTrue(hasParent);
        }

        
        {
            File childFile = Paths.get(outputDirectory.toFile().getAbsolutePath(), "ch.so.afu.abbaustellen/collection.json").toFile();
            Collection resultCollection = objectMapper.readValue(childFile, Collection.class);

            List<Link> collectionLinks = resultCollection.getLinks();
            assertTrue(collectionLinks.size()==3);

            boolean hasParent = false;
            for (Link link : collectionLinks) {
                String rel = link.getRel();
                if (rel.equalsIgnoreCase("self")) {
                    assertEquals(link.getHref(), "http://localhost:8080/stac/ch.so.afu.abbaustellen/collection.json");
                } else if (rel.equalsIgnoreCase("root")) {
                    assertEquals(link.getHref(), "http://localhost:8080/stac/catalog.json");
                } else if (rel.equalsIgnoreCase("parent")) {
                    assertEquals(link.getHref(), "http://localhost:8080/stac/catalog.json");
                    hasParent = true;
                } else {
                    throw new Exception("unknown link relation found");
                }
            }
            assertTrue(hasParent);
        }

    }
    

    @Test
    public void dummy() throws Exception {
        Catalog_V1 catalog = new Catalog_V1();
        catalog.version("1.0.0")
                .id("ch.so.geo.stac")
                .title("Geodaten des Kantons Solothurn")
                .description("Geodaten des Kantons Solothurn als STAC")
                .selfHref("http://localhost:8080/stac/")
                .publicationType(PublicationType.ABSOLUTE_PUBLISHED)
                .outputDirectory(new File("/Users/stefan/tmp/jstac/normalizedcatalog").toPath());
        
        Bbox bbox = new Bbox().west(7.340693492284002)
                .south(47.074299169536175)
                .east(8.03269288687543)
                .north(47.50119805032911);       
        
        Interval interval = new Interval().endInterval("2023-04-13");

        {
            Collection collection = new Collection();
            collection.version("1.0.0")
                    .id("ch.so.afu.abbaustellen")
                    .title("Abbaustellen")
                    .description(
                            "Die Abbaustellen umfassen die Flächen folgender Objekte: <br/><ul><li>sämtliche grösseren Abbaugebiete (Kiesgruben, Kalksteinbrüche sowie Tongruben), für welche ein Gestaltungsplan vorliegt. Die dargestellten Flächen umfassen jeweils den gesamten Perimeter der genehmigten Gestaltungspläne, und nicht einzelne Abbauetappen.</li><li>Kleinabbaustellen. Es handelt sich üblicherweise um kleinere, gemeindeeigene Mergelgruben, in welchen Material für den Bau und Unterhalt von Wald- und Flurwegen abgebaut wird. Kleinabbaustellen erfordern keinen Gestaltungsplan. Die dargestellten Flächen umfassen hier jeweils den auf Stufe Bau-, bzw. Abbaubewilligung genehmigten Perimeter.</li><li>alle künftigen Erweiterungs- und Ersatzstandorte, welche im kantonalen Richtplan (Kap. E-3.1 bis E-3.4) enthalten sind.</li></ul><br>Die Flächen wurden von verschiedenen Planvorlagen und bestehenden Flächendaten mit unterschiedlichem Massstab digitalisiert, bzw. übernommen.")
                    .license("https://files.geo.so.ch/nutzungsbedingungen.html")
                    .bbox(bbox)
                    .interval(interval);
                    
            //collection.save(PublicationType.SELF_CONTAINED, outputDirectoryPath);   
            
//            Collection collectionFromFile = Collection.readFromFile(resultFilePath.toFile());
            catalog.addChild(collection);
            catalog.flushChildren();
        }

        {
            Collection collection = new Collection();
            collection.version("1.0.0")
                    .id("ch.so.agi.av.administrative_einteilung")
                    .title("Administrative Einteilung AV und Grundbuch")
                    .description(
                            "Administrativen Einteilungen der amtlichen Vermessung und des Grundbuchs.")
                    .license("https://files.geo.so.ch/nutzungsbedingungen.html")
                    .bbox(bbox)
                    .interval(interval);
                    
            //collection.save(PublicationType.SELF_CONTAINED, outputDirectoryPath);     
            
//            Collection collectionFromFile = Collection.readFromFile(resultFilePath.toFile());
            catalog.addChild(collection);
            catalog.flushChildren();
        }
        
        catalog.save();
        
//        catalog.save(PublicationType.SELF_CONTAINED, new File("/Users/stefan/tmp/jstac/normalizedcatalog").toPath());
        
//        catalog.setSelfHref("http://localhost:8080/stac/");
//        catalog.save(PublicationType.RELATIVE_PUBLISHED, new File("/Users/stefan/tmp/jstac").toPath());

//        catalog.addFromFile(resultFilePathOne.toFile());
//        catalog.addFromFile(resultFilePathTwo.toFile());
        
//        catalog.save(PublicationType.ABSOLUTE_PUBLISHED, new File("/Users/stefan/tmp/jstac/normalizedcatalog").toPath());

        
        
        
        
//        collection.save(PublicationType.SELF_CONTAINED, new File("/Users/stefan/tmp/gaga"));
    }
    
    
    

}
