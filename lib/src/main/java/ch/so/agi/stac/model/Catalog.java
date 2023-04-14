package ch.so.agi.stac.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    
    private List<Link> links = new ArrayList<>();
    
    // api
        
    @JsonIgnore
    private Path rootFilePath;
    
    @JsonIgnore
    private Path parentFilePath;
    
    @JsonIgnore
    private String selfHref; // Eher sowas wie rootSelfHref aka baseUrl.
    
    private List<Catalog> children = new ArrayList<>();
        
    @JsonIgnore
    protected String fileName = "catalog.json";
  
//    private PublicationType publicationType = PublicationType.SELF_CONTAINED;
    
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

    public void setType(String type) {
        this.type = CatalogType.fromString(type);
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
        
    public void setRootFilePath(Path rootFilePath) {
        this.rootFilePath = rootFilePath;
    }
    
    public Path getRootFilePath() {
        return rootFilePath;
    }

//    public void setParentPath(Path parentPath) {
//        this.parentFilePath = parentPath;
//    }
//    
//    public Path getParentPath() {
//        return parentFilePath;
//    }
    
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
    public Catalog links(List<Link> links) {
        this.links = links;
        return this;
    }
    
    public<T extends Catalog> void addChild(Catalog child) {
        child.setHasParent(true);
        children.add(child);
    }
    
    public void save(PublicationType publicationType, Path target) throws IOException {
//        this.publicationType = publicationType; // TODO Brauche ich das noch als globale Variable?

        File resultFile = Paths.get(target.toFile().getAbsolutePath(), fileName).toFile();
        Path resultFilePath = resultFile.toPath();

        // Man darf den rootPath nur verändern, wenn es sich um das wirkliche
        // Root-Element handelt.
        if (!hasParent) {
            rootFilePath = Paths.get(target.toFile().getAbsolutePath(), fileName);           
        }
        
        Path rootDirectoryPath = rootFilePath.getParent();
        
        Path currentDirectoryPath = target.toAbsolutePath();
        Path currentFilePath = currentDirectoryPath.resolve(fileName).toAbsolutePath();
                
        // self relation
        // Wenn der Katalog self-contained ist, gibt es keinen self-link.
        // self containing: keine self relation
        // absolut: alles Elemente haben eine self relation
        // relativ: nur das root Element hat eine absolute self relation
        if (!publicationType.equals(PublicationType.SELF_CONTAINED)) {
            if ((!hasParent && publicationType.equals(PublicationType.RELATIVE_PUBLISHED) || publicationType.equals(PublicationType.ABSOLUTE_PUBLISHED))) {
                String currentHref = selfHref + rootDirectoryPath.relativize(currentFilePath).toString();

                Link selfLink = new Link();
                selfLink.rel("self").href(currentHref).type(LinkMimeType.APPLICATION_JSON); 
                links.add(selfLink);
            } 
        }
                
        for (Catalog child : children) {
            child.setRootFilePath(rootFilePath); // Bleibt für jedes Kind und Grosskind identisch.
            child.setSelfHref(selfHref); // Entspricht eher einer Base Url.
            
            Path childTarget = Paths.get(target.toFile().getAbsolutePath()).resolve(Paths.get(child.getId()));
                        
            if (!childTarget.toFile().exists()) {
                childTarget.toFile().mkdirs();
            }
            
            // parent relation
            // absolut: Entspricht der self relation des Elternteils. Also von currentHref.
            // self und relation: Lokales (relatives) Verzeichnis des Elternteils als Vergleich Kind-Verzeichnis vs Elternteil-Verzeichnis.
            String parentHref;
            if (publicationType.equals(PublicationType.ABSOLUTE_PUBLISHED)) {
                parentHref = selfHref + rootDirectoryPath.relativize(currentFilePath).toString();
                
            } else {
                Path parentFilePath = childTarget.relativize(currentFilePath);
                parentHref = parentFilePath.toString();
            }
            
            Link parentLink = new Link().rel("parent").href(parentHref).type(LinkMimeType.APPLICATION_JSON);
            child.getLinks().add(parentLink);

           
            child.save(publicationType,  childTarget);
            
            // child relation
            String childHref;
            if (publicationType.equals(PublicationType.ABSOLUTE_PUBLISHED)) {
                childHref = selfHref + rootDirectoryPath.relativize(childTarget.resolve(child.getFileName())).toString();
            } else {
                Path relativeChildPath = currentDirectoryPath.relativize(childTarget).resolve(child.getFileName());            
                childHref = relativeChildPath.toString();
            }
            Link childLink = new Link();
            childLink.rel("child").href(childHref).type(LinkMimeType.APPLICATION_JSON).title(child.getTitle());
            links.add(childLink);
        }
        
        // root relation
        // Nur absolut hat absolute href. 
        String rootLinkHref;
        if (publicationType.equals(PublicationType.ABSOLUTE_PUBLISHED)) {
            rootLinkHref = selfHref + rootFilePath.toFile().getName();
        } else {
            rootLinkHref = currentDirectoryPath.relativize(rootFilePath).toString();
        } 
        
        Link rootLink = new Link().rel("root").href(rootLinkHref).type(LinkMimeType.APPLICATION_JSON);
        links.add(rootLink);

        ObjectMapper objectMapper = JacksonObjectMapperHolder.getInstance().getObjectMapper();
        objectMapper.writeValue(resultFile, this);            
    } 
}
