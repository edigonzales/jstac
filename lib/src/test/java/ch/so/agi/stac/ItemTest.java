package ch.so.agi.stac;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import ch.so.agi.stac.model.Collection_V1;
import ch.so.agi.stac.model.Item;
import ch.so.agi.stac.model.Link;
import ch.so.agi.stac.model.LinkMimeType;
import ch.so.agi.stac.model.PublicationType;

public class ItemTest {
    static Logger log = LoggerFactory.getLogger(Catalog_V1Test.class);
    
    private static final String TEST_OUT="build/test/ItemTest/";

    @BeforeAll
    public static void setupFolder() {
        new File(TEST_OUT).mkdirs();
    }
    
    @Test
    public void dummy() throws Exception {
        // Prepare
//        Path outputDirectory = Paths.get(TEST_OUT, "dummy");
//        outputDirectory.toFile().mkdirs();
//        
//        // Run
//        
//        String datetime = "2023-04-13";
//        String rootSelfHref = "http://localhost:8080/stac/";
//
//        // Run
//        Bbox bbox = new Bbox().west(7.340693492284002)
//                .south(47.074299169536175)
//                .east(8.03269288687543)
//                .north(47.50119805032911);       
//        
//        Interval interval = new Interval().endInterval("2023-04-13");
//
//        Collection collection = new Collection();
//        collection.version("1.0.0")
//                .id("ch.so.afu.abbaustellen")
//                .title("Abbaustellen")
//                .description(
//                        "Die Abbaustellen umfassen die Flächen folgender Objekte: <br/><ul><li>sämtliche grösseren Abbaugebiete (Kiesgruben, Kalksteinbrüche sowie Tongruben), für welche ein Gestaltungsplan vorliegt. Die dargestellten Flächen umfassen jeweils den gesamten Perimeter der genehmigten Gestaltungspläne, und nicht einzelne Abbauetappen.</li><li>Kleinabbaustellen. Es handelt sich üblicherweise um kleinere, gemeindeeigene Mergelgruben, in welchen Material für den Bau und Unterhalt von Wald- und Flurwegen abgebaut wird. Kleinabbaustellen erfordern keinen Gestaltungsplan. Die dargestellten Flächen umfassen hier jeweils den auf Stufe Bau-, bzw. Abbaubewilligung genehmigten Perimeter.</li><li>alle künftigen Erweiterungs- und Ersatzstandorte, welche im kantonalen Richtplan (Kap. E-3.1 bis E-3.4) enthalten sind.</li></ul><br>Die Flächen wurden von verschiedenen Planvorlagen und bestehenden Flächendaten mit unterschiedlichem Massstab digitalisiert, bzw. übernommen.")
//                .license("https://files.geo.so.ch/nutzungsbedingungen.html")
//                .bbox(bbox)
//                .interval(interval)
//                .selfHref(rootSelfHref)
//                .publicationType(PublicationType.ABSOLUTE_PUBLISHED)
//                .outputDirectory(outputDirectory);
//        
//        Item item = new Item().version("1.0.0").id("ch.so.afu.abbaustellen");
//        collection.addItem(item);
//
//        collection.save();
        
        // Validate
    }


}
