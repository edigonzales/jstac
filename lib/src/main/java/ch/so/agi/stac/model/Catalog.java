package ch.so.agi.stac.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.so.agi.stac.jackson.JacksonObjectMapperHolder;

public class Catalog {
    protected CatalogType type = CatalogType.CATALOG;
    @JsonProperty("stac_version")
    private String stacVersion;
    private String id;
    private String title;
    private String description;
    @JsonIgnore
    private PublicationType publicationType;
    private Path outputDirectoryPath; // z.B. "/opt/stac/". Dynamisch, d.h. ändert sich in den Kindern.
    @JsonIgnore
    private String rootHref; // Kann auch als Basis-Url des Rootelementes betrachtet werden.
    
    private String fileName;
    private Link selfLink;
    private Link rootLink;
    private Link parentLink;
    
    private boolean hasParent = false;
    @JsonIgnore
    private Path rootFilePath; // z.B. "/opt/stac/catalog.json". Statisch, d.h. wird im Root-Katalog gesetzt und ist immer gleich für die Kinder.    
    
    
    
    // FIXME
    // Nochmals anderer Ansatz: jedes Objekt bekommt:
    // - beim Erstellen einen self link (egal ob outputdirectory gesetzt ist, dann ist es das new File()-Dir) 
    // und root link
    // - Beim Hinzufügen bekommt es den Parent, Der Parent das Child. Root wird nicht angepasst
    // Aber dann ist es egal, ob man das macht oder nicht, da man sowieso im "normalizen" muss.
    // Ah vielleicht doch sinnvoll, weil man exportieren kann (immer).
    
    
    // Mit einem Item durchspielen, weil nochmals Komplexität.
    // Ohne Links/Href, nur mit outputDirectoryPath (resp. file). Wenn dieser korrekt funktioniert, kann ich alles
    // von dort ableiten. Falls dieser nicht gesetzt wird (z.B. beim Item oder Collection, die geadded wird), muss
    // dieser Pfad angenommen werden(?) oder spätestens beim Adden berechnet werden. Wenn das Mutterelement keinen hat,
    // wird einfach new File()-Dir angenommen.
    
    
    public Catalog (CatalogBuilder builder) {}
//        this.type = builder.type;
//        this.stacVersion = builder.stacVersion;
//        this.id = builder.id;
//        this.title = builder.title;
//        this.description = builder.description;
//        this.publicationType = builder.publicationType;
//        this.outputDirectoryPath = builder.outputDirectoryPath;
//        this.rootHref = builder.rootHref;
//        
//        if (this.type.equals(CatalogType.CATALOG)) {
//            this.fileName = "catalog.json";
//        } else {
//            this.fileName = "collection.json";
//        }
//        
//        if (outputDirectoryPath != null) {
//            rootFilePath = Paths.get(outputDirectoryPath.toFile().getAbsolutePath(), fileName);           
//
//            Path rootDirectoryPath = rootFilePath.getParent();
//            Path currentDirectoryPath = outputDirectoryPath.toAbsolutePath();
//            Path currentFilePath = currentDirectoryPath.resolve(fileName).toAbsolutePath();
//
//            
//            // self link
//            selfLink = createSelfLink(rootHref, publicationType, rootDirectoryPath, currentFilePath);
//            System.out.println(this.id + ":selfLink: " + selfLink.getHref());
//            
//            // root link
//            rootLink = createRootLink(rootHref, publicationType, currentDirectoryPath);
//            System.out.println(this.id + ":rootLink: " + rootLink.getHref());            
//        }
//
//
//        
//    }
//    
//    public void addChild(Catalog child) {
//        System.out.println(child.id);
//        System.out.println("------------------");
//       
//        child.outputDirectoryPath = this.outputDirectoryPath.resolve(child.id);
//        child.rootFilePath = this.rootFilePath;
//        
//        // root link
//        Link rootLink = new Link();
//        rootLink.setRel(this.rootLink.getRel());
//        rootLink.setType(this.rootLink.getType());
//        
//        if (this.publicationType.equals(PublicationType.ABSOLUTE_PUBLISHED)) {
//            rootLink.setHref(this.rootLink.getHref());
//        } else {
//            rootLink.setHref("../"+this.rootLink.getHref());
//        }
//        
//        child.rootLink = rootLink;
//        
//        // self link
//        Link selfLink = createSelfLink(rootHref, publicationType, this.rootFilePath.getParent(), child.outputDirectoryPath);
//        child.selfLink = selfLink;
//        
//        // parent link
//        Link parentLink = createParentLink(rootHref, publicationType, this.rootFilePath.getParent(), this.outputDirectoryPath.resolve(this.fileName), child.outputDirectoryPath);
//        child.parentLink = parentLink;
//
//        System.out.println(child.outputDirectoryPath);
//        System.out.println(child.rootFilePath);
//        System.out.println(child.parentLink.getHref());
//        
//        // TODO ggf hier flushen
//        
//    }
//    
//    private Link createParentLink(String rootHref, PublicationType publicationType, Path rootDirectoryPath, Path currentFilePath, Path childTarget) {
//        String parentHref;
//        if (publicationType.equals(PublicationType.ABSOLUTE_PUBLISHED)) {
//            parentHref = rootHref + rootDirectoryPath.relativize(currentFilePath).toString();
//            
//        } else {
//            Path parentFilePath = childTarget.relativize(currentFilePath);
//            parentHref = parentFilePath.toString();
//        }
//        
//        Link parentLink = new Link().rel("parent").href(parentHref).type(LinkMimeType.APPLICATION_JSON);
//        return parentLink;
//    }
//
//    private Link createChildLink(Catalog_V1 child, String rootHref, PublicationType publicationType, Path rootDirectoryPath, Path currentDirectoryPath, Path childTarget) {
//        String childHref;
//        if (publicationType.equals(PublicationType.ABSOLUTE_PUBLISHED)) {
//            childHref = rootHref + rootDirectoryPath.relativize(childTarget.resolve(child.getFileName())).toString();
//        } else {
//            Path relativeChildPath = currentDirectoryPath.relativize(childTarget).resolve(child.getFileName());            
//            childHref = relativeChildPath.toString();
//        }
//        Link childLink = new Link();
//        childLink.rel("child").href(childHref).type(LinkMimeType.APPLICATION_JSON).title(child.getTitle());
//        
//        return childLink;
//    }
//
//    private Link createRootLink(String rootHref, PublicationType publicationType, Path currentDirectoryPath) {
//        String rootLinkHref;
//        if (publicationType.equals(PublicationType.ABSOLUTE_PUBLISHED)) {
//            rootLinkHref = rootHref + rootFilePath.toFile().getName();
//        } else {  
//            rootLinkHref = currentDirectoryPath.relativize(rootFilePath).toString();
//        } 
//        
//        Link rootLink = new Link().rel("root").href(rootLinkHref).type(LinkMimeType.APPLICATION_JSON);
//        return rootLink;
//    }
//
//    private Link createSelfLink(String rootHref, PublicationType publicationType, Path rootDirectoryPath, Path currentFilePath) {
////        if (!publicationType.equals(PublicationType.SELF_CONTAINED)) {
////            if ((!hasParent && publicationType.equals(PublicationType.RELATIVE_PUBLISHED) || publicationType.equals(PublicationType.ABSOLUTE_PUBLISHED))) {
////                String currentHref = selfHref + rootDirectoryPath.relativize(currentFilePath).toString();
////
////                Link selfLink = new Link();
////                selfLink.rel("self").href(currentHref).type(LinkMimeType.APPLICATION_JSON); 
////                return selfLink;
////            } 
////        }
////        return null;
//
//        String currentHref;
//        if (publicationType.equals(PublicationType.ABSOLUTE_PUBLISHED)) {
//            currentHref = rootHref + rootDirectoryPath.relativize(currentFilePath).toString();
//        } else {
//            currentHref = rootDirectoryPath.relativize(currentFilePath).toString();
//        }
//        
//        Link selfLink = new Link();
//        selfLink.rel("self").href(currentHref).type(LinkMimeType.APPLICATION_JSON); 
//        return selfLink;
//    }
//
//    
//    
//    public void save() throws IOException {
//        
//        ObjectMapper objectMapper = JacksonObjectMapperHolder.getInstance().getObjectMapper();
//        
//        File resultFile = Paths.get(outputDirectoryPath.toFile().getAbsolutePath(), fileName).toFile();
//        objectMapper.writeValue(resultFile, this);
//    }
    
    
    public static class CatalogBuilder {
        private CatalogType type;
        private String stacVersion;
        private String id;
        private String title;
        private String description;
        private PublicationType publicationType;
        private Path outputDirectoryPath; 
        private String rootHref;
        
        public CatalogBuilder type(CatalogType type) {
            this.type = type; 
            return this;
        }

        public CatalogBuilder version(String version) {
            this.stacVersion = version;
            return this;
        }
        
        public CatalogBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        public CatalogBuilder title(String title) {
            this.title = title;
            return this;
        }

        public CatalogBuilder description(String description) {
            this.description = description;
            return this;
        }
        
        public CatalogBuilder publicationType(PublicationType publicationType) {
            this.publicationType = publicationType;
            return this;
        }
        
        public CatalogBuilder rootHref(String rootHref) {
            this.rootHref = rootHref;
            return this;
        }
        
        public CatalogBuilder outputDirectory(Path outputDirectory) {
            this.outputDirectoryPath = outputDirectory.toAbsolutePath();
            return this;
        } 

        public Catalog build() {
            return new Catalog(this);
        }

    }
    

}
