package ch.so.agi.stac.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.so.agi.stac.jackson.JacksonObjectMapperHolder;

public class Catalog {   
    // json
    
    private String id;
    
    @JsonProperty("stac_version")
    private String stacVersion;
    
    private String title;
    
    private String description;
        
    protected CatalogType type = CatalogType.CATALOG;
    
    private Collection<Link> links = new ArrayList<>();
    
    // api
        
    @JsonIgnore
    private Path rootFilePath;
    
    @JsonIgnore
    private Path parentPath;
    
    @JsonIgnore
    private String selfHref; // Eher sowas wie rootSelfHref aka baseUrl.
    
    private Collection<Catalog> children = new ArrayList<>();
        
    @JsonIgnore
    protected String fileName = "catalog.json";
  
    private PublicationType publicationType = PublicationType.SELF_CONTAINED;
    
    @JsonIgnore
    private boolean hasParent = false;
    
    // getters / setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStacVersion() {
        return stacVersion;
    }

    public void setStacVersion(String stacVersion) {
        this.stacVersion = stacVersion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type.toString();
    }

    public void setType(CatalogType type) {
        this.type = type;
    }

    public Collection<Link> getLinks() {
        return links;
    }

    public void setLinks(Collection<Link> links) {
        this.links = links;
    }
        
    public void setRootFilePath(Path rootFilePath) {
        this.rootFilePath = rootFilePath;
    }
    
    public Path getRootFilePath() {
        return rootFilePath;
    }

    public void setParentPath(Path parentPath) {
        this.parentPath = parentPath;
    }
    
    public Path getParentPath() {
        return parentPath;
    }
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getSelfHref() {
        return selfHref;
    }

    public void setSelfHref(String selfHref) {
        this.selfHref = selfHref;
    }

    public boolean isHasParent() {
        return hasParent;
    }

    public void setHasParent(boolean hasParent) {
        this.hasParent = hasParent;
    }
            
    // fluent api
   
    public Catalog id(String id) {
        this.id = id;
        return this;
    }
    
    public Catalog version(String version) {
        this.stacVersion = version;
        return this;
    }

    public Catalog title(String title) {
        this.title = title;
        return this;
    }

    public Catalog description(String description) {
        this.description = description;
        return this;
    }
        
    // TODO nötig?
    public Catalog links(Collection<Link> links) {
        this.links = links;
        return this;
    }
    
    public<T extends Catalog> void addChild(Catalog child) {
        child.setHasParent(true);
        children.add(child);
    }
    
    public void save(PublicationType publicationType, Path target) throws StreamWriteException, DatabindException, IOException {
        this.publicationType = publicationType; // TODO Brauche ich das noch als globale Variable?

        // Man darf den rootPath nur verändern, wenn es sich um das wirkliche
        // Root-Element handelt.
        if (!hasParent) {
            rootFilePath = Paths.get(target.toFile().getAbsolutePath(), fileName);           
        }

        Path currentDirectoryPath = target.toAbsolutePath();
                
        Path rootDirectoryPath = rootFilePath.getParent();
        Path currentFilePath = currentDirectoryPath.resolve(fileName).toAbsolutePath();
        String currentHref = selfHref + rootDirectoryPath.relativize(currentFilePath).toString();        
//        System.out.println("currentHref: " + currentHref);

        
        // self relation
        // Wenn der Katalog self-contained ist, gibt es keinen self-link.
        if (!this.publicationType.equals(PublicationType.SELF_CONTAINED)) {
            Link selfLink = new Link();

            if (!hasParent && publicationType.equals(PublicationType.RELATIVE_PUBLISHED)) {
                System.out.println("relativ. self muss absolut sein aber nur bei root element vorhanden.");
                selfLink.rel("self").href(selfHref + fileName).type(LinkMimeType.APPLICATION_JSON); 

            } else {
                System.out.println("absolut. self muss immer absolut vorhanden sein.");
                
                // TODO Das stimmt noch nicht. Muss eben ander sein für Kinder.
                //selfLink.rel("self").href(selfHref + fileName).type(LinkMimeType.APPLICATION_JSON); 


            }
            
            links.add(selfLink);
        }
                
        for (Catalog child : children) {
            child.setRootFilePath(rootFilePath);
            
            Path childTarget = Paths.get(target.toFile().getAbsolutePath()).resolve(Paths.get(child.getId()));
            
//            System.out.println("childTarget: " + childTarget);
            
            if (!childTarget.toFile().exists()) {
                childTarget.toFile().mkdirs();
            }
             
            
            //TODO Name und sowieso anders lösen.
            Path relativeParentPath = childTarget.relativize(currentDirectoryPath.resolve(fileName));
            child.setParentPath(relativeParentPath);
            
            child.setSelfHref(selfHref);
            
            child.save(this.publicationType,  childTarget);
            
            Path relativeChildPath = currentDirectoryPath.relativize(childTarget).resolve(child.getFileName());            
            Link childLink = new Link();
            childLink.rel("child").href(relativeChildPath.toString()).type(LinkMimeType.APPLICATION_JSON).title(child.getTitle());
            links.add(childLink);
        }
        
        // root relation
        // Nur absolut hat absolute href. 
        String rootLinkHref;
        if (!this.publicationType.equals(PublicationType.SELF_CONTAINED)) {
            rootLinkHref = selfHref + rootDirectoryPath.relativize(currentFilePath).toString();
        } else {
            rootLinkHref = currentDirectoryPath.relativize(rootFilePath).toString();
        } 
        
        Link rootLink = new Link().rel("root").href(rootLinkHref).type(LinkMimeType.APPLICATION_JSON);
        links.add(rootLink);
        
        // parent relation
        if (this.parentPath != null) {
            Link parentLink = new Link().rel("parent").href(this.parentPath.toString()).type(LinkMimeType.APPLICATION_JSON);
            links.add(parentLink);            
        }
        
        
        File resultFile = Paths.get(target.toFile().getAbsolutePath(), fileName).toFile();

        ObjectMapper objectMapper = JacksonObjectMapperHolder.getInstance().getObjectMapper();
        objectMapper.writeValue(resultFile, this);
    }
    
//    public void save(Path target) throws StreamWriteException, DatabindException, IOException {
//        this.save(this.publicationType, target);
//    }
    
//    /*
//     * Wandelt den (lokalen) Dateipfad in eine absolute Url um.
//     * 
//     * currentPath: Absoluter Pfad, der umgewandelt werden muss.
//     */
//    private String getAbsoluteHref(Path rootDirectoryPath, Path currentFilePath, String baseUrl) {
//        System.out.println("rootDirectoryPath: " + rootDirectoryPath);
//        System.out.println("currentFilePath: " + currentFilePath);
//        System.out.println("baseUrl: " + baseUrl);
//        
//        Path relativePath = rootDirectoryPath.relativize(currentFilePath);
//        
//        
//        
//        System.out.println(rootDirectoryPath.relativize(currentFilePath));
//        
//        
//        return null;
//    }
}
