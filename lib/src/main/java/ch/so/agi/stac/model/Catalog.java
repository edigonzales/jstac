package ch.so.agi.stac.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private PublicationType publicationType;    
    
    // TODO eine der beiden ist überflüssig hier.
    
    @JsonIgnore
    private Path outputDirectoryPath; // z.B. "/opt/stac/"
    
    @JsonIgnore
    private Path rootFilePath; // z.B. "/opt/stac/catalog.json"
        
    @JsonIgnore
    private String rootSelfHref; // Kann auch als Basis-Url des Rootelementes betrachtet werden.
    
    private Map<String,Catalog> children = new HashMap<>();
            
    @JsonIgnore
    protected String fileName = "catalog.json";
      
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
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getRootSelfHref() {
        return rootSelfHref;
    }

    public void setRootSelfHref(String rootSelfHref) {
        this.rootSelfHref = rootSelfHref;
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
    
    public Catalog publicationType(PublicationType publicationType) {
        this.publicationType = publicationType;
        return this;
    }
    
    public Catalog selfHref(String selfHref) {
        this.rootSelfHref = selfHref;
        return this;
    }
    
    public Catalog outputDirectory(Path outputDirectory) {
        this.outputDirectoryPath = outputDirectory;
        return this;
    } 
        
    // TODO nötig?
    public Catalog links(List<Link> links) {
        this.links = links;
        return this;
    }
    
    public<T extends Catalog> void addChild(Catalog child) {
        child.setHasParent(true);
        children.put(child.getId(), child);
    }
    
        
    private Link getSelfLink(String selfHref, PublicationType publicationType, Path rootDirectoryPath, Path currentFilePath) {
        if (!publicationType.equals(PublicationType.SELF_CONTAINED)) {
            if ((!hasParent && publicationType.equals(PublicationType.RELATIVE_PUBLISHED) || publicationType.equals(PublicationType.ABSOLUTE_PUBLISHED))) {
                String currentHref = selfHref + rootDirectoryPath.relativize(currentFilePath).toString();

                Link selfLink = new Link();
                selfLink.rel("self").href(currentHref).type(LinkMimeType.APPLICATION_JSON); 
                return selfLink;
            } 
        }
        return null;
    }
    
    private Link getParentLink(String selfHref, PublicationType publicationType, Path rootDirectoryPath, Path currentFilePath, Path childTarget) {
        String parentHref;
        if (publicationType.equals(PublicationType.ABSOLUTE_PUBLISHED)) {
            parentHref = selfHref + rootDirectoryPath.relativize(currentFilePath).toString();
            
        } else {
            Path parentFilePath = childTarget.relativize(currentFilePath);
            parentHref = parentFilePath.toString();
        }
        
        Link parentLink = new Link().rel("parent").href(parentHref).type(LinkMimeType.APPLICATION_JSON);
        return parentLink;
    }
    
    private Link getChildLink(Catalog child, String selfHref, PublicationType publicationType, Path rootDirectoryPath, Path currentDirectoryPath, Path childTarget) {
        String childHref;
        if (publicationType.equals(PublicationType.ABSOLUTE_PUBLISHED)) {
            childHref = selfHref + rootDirectoryPath.relativize(childTarget.resolve(child.getFileName())).toString();
        } else {
            Path relativeChildPath = currentDirectoryPath.relativize(childTarget).resolve(child.getFileName());            
            childHref = relativeChildPath.toString();
        }
        Link childLink = new Link();
        childLink.rel("child").href(childHref).type(LinkMimeType.APPLICATION_JSON).title(child.getTitle());
        
        return childLink;
    }
    
    private Link getRootLink(String selfHref, PublicationType publicationType, Path currentDirectoryPath) {
        String rootLinkHref;
        if (publicationType.equals(PublicationType.ABSOLUTE_PUBLISHED)) {
            rootLinkHref = selfHref + rootFilePath.toFile().getName();
        } else {
            rootLinkHref = currentDirectoryPath.relativize(rootFilePath).toString();
        } 
        
        Link rootLink = new Link().rel("root").href(rootLinkHref).type(LinkMimeType.APPLICATION_JSON);
        return rootLink;
    }

    // TODO publicationtype und target braucht es nicht mehr. Die werden zu Beginn
    // gesetzt.
    public void save(PublicationType publicationType, Path target) throws IOException {
        File resultFile = Paths.get(target.toFile().getAbsolutePath(), fileName).toFile();

        // Der rootFilePath (z.B. /opt/stac/catalog.json) als Root-Catalog/-Collection
        // wird nur einmal gesetzt. Nämlich wenn es sich um den Root-Catalog/-Collection
        // handelt. Er ist für jedes weitere Element identisch.
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
        
        Link selfLink = getSelfLink(rootSelfHref, publicationType, rootDirectoryPath, currentFilePath);
        if (selfLink != null) {
            links.add(selfLink);
        }
                
        for (Map.Entry<String, Catalog> entry : children.entrySet()) {
            Catalog child = entry.getValue();

            // Der Kind-Katalog/-Collection muss mit Informationen angereichtert werden,
            // die man beim Elternteil (also hier) kennt. Z.B. die Basis-Url oder
            // der Speicherpfad des Root-Elements (der benötigt wird für die Hrefs zu 
            // eruieren). Vor allem auch der Parent-Link ist hier bekannt, da das
            // Kind sonst nicht verküpft ist. Es ist im Code unidirektional: Elternteil kennt
            // Kind. Kind kennt Elternteil nicht.
            
            child.setRootFilePath(rootFilePath); 
            child.setRootSelfHref(rootSelfHref); 
            
            // TODO rename to childOutputDirectoryPath
            Path childTarget = Paths.get(target.toFile().getAbsolutePath()).resolve(Paths.get(child.getId()));
                        
            if (!childTarget.toFile().exists()) {
                childTarget.toFile().mkdirs();
            }
            
            // parent relation
            // absolut: Entspricht der self relation des Elternteils. Also von currentHref.
            // self und relation: Lokales (relatives) Verzeichnis des Elternteils als Vergleich Kind-Verzeichnis vs Elternteil-Verzeichnis.
            Link parentLink = getParentLink(rootSelfHref, publicationType, rootDirectoryPath, currentFilePath, childTarget);
            child.getLinks().add(parentLink);

            child.save(publicationType,  childTarget);
            
            // child relation
            Link childLink = getChildLink(child, rootSelfHref, publicationType, rootDirectoryPath, currentDirectoryPath, childTarget);
            links.add(childLink);
        }
                
        // root relation
        // Nur absolut hat absolute href. 
        Link rootLink = getRootLink(rootSelfHref, publicationType, currentDirectoryPath);
        links.add(rootLink);

        ObjectMapper objectMapper = JacksonObjectMapperHolder.getInstance().getObjectMapper();
        objectMapper.writeValue(resultFile, this);            
    }     
    
    // TODO Wahrscheinlich kann man das noch zusammenlegen mit save. Da Pub-Typ und Speicherort vorgängig bekannt
    // sind. Das Entfernen der Kinder ist sicher unterschiedlich. 
    
    // Leichte (?) Redundanz beim Speicherort in den Variablen.
    
    public void flushChildren() throws IOException {
        if (!hasParent) {
            rootFilePath = Paths.get(outputDirectoryPath.toFile().getAbsolutePath(), fileName);           
        }

        Path rootDirectoryPath = rootFilePath.getParent();
        
        Path currentDirectoryPath = outputDirectoryPath.toAbsolutePath();
        Path currentFilePath = currentDirectoryPath.resolve(fileName).toAbsolutePath();

        List<String> flushedCatalogs = new ArrayList<>();
        for (Map.Entry<String, Catalog> entry : children.entrySet()) {
            String key = entry.getKey();
            flushedCatalogs.add(key);
            
            Catalog child = entry.getValue();
            
            child.getLinks().removeAll(child.getLinks());
            
            child.setRootFilePath(rootFilePath); 
            child.setRootSelfHref(rootSelfHref);
            
            Path childTarget = Paths.get(outputDirectoryPath.toFile().getAbsolutePath()).resolve(Paths.get(child.getId()));
                        
            if (!childTarget.toFile().exists()) {
                childTarget.toFile().mkdirs();
            }
            
            // parent relation
            // absolut: Entspricht der self relation des Elternteils. Also von currentHref.
            // self und relation: Lokales (relatives) Verzeichnis des Elternteils als Vergleich Kind-Verzeichnis vs Elternteil-Verzeichnis.
            Link parentLink = getParentLink(rootSelfHref, publicationType, rootDirectoryPath, currentFilePath, childTarget);
            child.getLinks().add(parentLink);

            child.save(publicationType,  childTarget);
            
            // child relation
            Link childLink = getChildLink(child, rootSelfHref, publicationType, rootDirectoryPath, currentDirectoryPath, childTarget);
            links.add(childLink);            
        }
        
        for (String id : flushedCatalogs) {
          System.out.println(children.size());
            children.remove(id);
          System.out.println(children.size());
        }
    }

}
