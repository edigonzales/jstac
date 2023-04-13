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
    // external
    
    private String id;
    
    @JsonProperty("stac_version")
    private String stacVersion;
    
    private String title;
    
    private String description;
    
    private String license;
    
    protected CatalogType type = CatalogType.CATALOG;
    
    private Collection<Link> links = new ArrayList<>();
    
    // internal
        
    @JsonIgnore
    private Path rootPath;
    
    @JsonIgnore
    private Path parentPath;
    
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

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
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
        
    public void setRootPath(Path rootPath) {
        this.rootPath = rootPath;
    }
    
    public Path getRootPath() {
        return rootPath;
    }

    public void setParentPath(Path parentPath) {
        this.parentPath = parentPath;
    }
    
    public Path getParentPath() {
        return parentPath;
    }
    
    public boolean isHasParent() {
        return hasParent;
    }

    public void setHasParent(boolean hasParent) {
        this.hasParent = hasParent;
    }
        
//    public String getFileName() {
//        return fileName;
//    }
//
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }
    
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
    
    public Catalog license(String license) {
        this.license = license;
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
        this.publicationType = publicationType;

        Path currentPath = target.toAbsolutePath();

        // add self link
        // Wenn der Katalog self-contained ist, gibt es keinen self-link.
//        if (!publicationType.equals(PublicationType.SELF_CONTAINED)) {
//            Link selfLink = new Link();
//            selfLink.rel("self").href(selfHref).type(LinkMimeType.APPLICATION_JSON);
//            links.add(selfLink);
//            
//            fileName = selfHref.substring(selfHref.lastIndexOf('/')+1, selfHref.length());
//        }
        
        // Man darf den rootPath nur verändern, wenn es sich um das wirkliche
        // Root-Element handelt.
        if (!hasParent) {
            rootPath = Paths.get(target.toFile().getAbsolutePath(), fileName); // TODO filename ist redundant           
        }
        for (Catalog child : children) {
            child.setRootPath(this.rootPath);
            
            Path childTarget = Paths.get(target.toFile().getAbsolutePath()).resolve(Paths.get(child.getId()));
            if (!childTarget.toFile().exists()) {
                childTarget.toFile().mkdirs();
            }
                        
            Path relativeParentPath = childTarget.relativize(currentPath.resolve(fileName));
            child.setParentPath(relativeParentPath);
            
            child.save(childTarget);
        }
        
        Link rootLink = new Link().rel("root").href(currentPath.relativize(rootPath).toString()).type(LinkMimeType.APPLICATION_JSON);
        links.add(rootLink);
        
        if (this.parentPath != null) {
            Link parentLink = new Link().rel("parent").href(this.parentPath.toString()).type(LinkMimeType.APPLICATION_JSON);
            links.add(parentLink);            
        }
        
        
        File resultFile = Paths.get(target.toFile().getAbsolutePath(), fileName).toFile();

        ObjectMapper objectMapper = JacksonObjectMapperHolder.getInstance().getObjectMapper();
        objectMapper.writeValue(resultFile, this);
    }
    
    public void save(Path target) throws StreamWriteException, DatabindException, IOException {
        this.save(publicationType, target);
    }
}
