package ch.so.agi.stac;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.so.agi.stac.jackson.Interval;
import ch.so.agi.stac.model.Bbox;
import ch.so.agi.stac.model.Catalog;
import ch.so.agi.stac.model.Collection;
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
        
//        catalog.setSelfHref("http://localhost:8080/stac/catalog.json");
//        catalog.save(PublicationType.SELF_CONTAINED, new File("/Users/stefan/tmp/"));
        
        
        // TODO write test for nested
//        Catalog catalogNested = new Catalog();
//        catalogNested.version("1.0.0")
//                .id("ch.so.geo.stac.nested")
//                .title("Nested Geodaten des Kantons Solothurn")
//                .description("Nested Geodaten des Kantons Solothurn als STAC");
//        catalog.addChild(catalogNested);
                
        Bbox bbox = new Bbox().west(7.340693492284002)
                .south(47.074299169536175)
                .east(8.03269288687543)
                .north(47.50119805032911);       
        
        // TODO write test
        //Interval interval = new Interval().startInterval("1977-09-23").endInterval("2023-04-13");
        Interval interval = new Interval().endInterval("2023-04-13");

        Collection collection = new Collection();
        collection.version("1.0.0")
                .id("ch.so.afu.abbaustellen")
                .title("Abbaustellen")
                .description(
                        "Die Abbaustellen umfassen die Flächen folgender Objekte: <br/><ul><li>sämtliche grösseren Abbaugebiete (Kiesgruben, Kalksteinbrüche sowie Tongruben), für welche ein Gestaltungsplan vorliegt. Die dargestellten Flächen umfassen jeweils den gesamten Perimeter der genehmigten Gestaltungspläne, und nicht einzelne Abbauetappen.</li><li>Kleinabbaustellen. Es handelt sich üblicherweise um kleinere, gemeindeeigene Mergelgruben, in welchen Material für den Bau und Unterhalt von Wald- und Flurwegen abgebaut wird. Kleinabbaustellen erfordern keinen Gestaltungsplan. Die dargestellten Flächen umfassen hier jeweils den auf Stufe Bau-, bzw. Abbaubewilligung genehmigten Perimeter.</li><li>alle künftigen Erweiterungs- und Ersatzstandorte, welche im kantonalen Richtplan (Kap. E-3.1 bis E-3.4) enthalten sind.</li></ul><br>Die Flächen wurden von verschiedenen Planvorlagen und bestehenden Flächendaten mit unterschiedlichem Massstab digitalisiert, bzw. übernommen.")
                .license("https://files.geo.so.ch/nutzungsbedingungen.html")
                .bbox(bbox)
                .interval(interval);
        
                
//        catalogNested.addChild(collection);
        catalog.addChild(collection);
        
//        catalog.save(PublicationType.SELF_CONTAINED, new File("/Users/stefan/tmp/jstac").toPath());
        
//        catalog.setSelfHref("http://localhost:8080/stac/");
//        catalog.save(PublicationType.RELATIVE_PUBLISHED, new File("/Users/stefan/tmp/jstac").toPath());

        catalog.setSelfHref("http://localhost:8080/stac/");
        catalog.save(PublicationType.ABSOLUTE_PUBLISHED, new File("/Users/stefan/tmp/jstac").toPath());

        
        
        
        
//        collection.save(PublicationType.SELF_CONTAINED, new File("/Users/stefan/tmp/gaga"));
    }

}
