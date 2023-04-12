package ch.so.agi.stac.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Catalog {   
    private String id;
    
    public String getId() {
        return id;
    }

    @JsonProperty("stac_version")
    private String stacVersion;
    
    protected CatalogType type = CatalogType.CATALOG;

    @JsonProperty("title")
    private String title;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("links")
    private Collection<Link> links = new ArrayList<>();
    
    private Collection<Catalog> children = new ArrayList<>();
    
    private String selfHref;
    
    protected String fileName = "catalog.json";
    
    private Path filePath;
    
    private boolean hasParent = false;
    
    private PublicationType publicationType = PublicationType.SELF_CONTAINED;
    
    public String getType() {
        return type.toString();
    }

    public void setSelfHref(String selfHref) {
        this.selfHref = selfHref;
    }
    
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
        children.add(child);
    }
    
    public void save(PublicationType publicationType, File file) throws StreamWriteException, DatabindException, IOException {
        this.publicationType = publicationType;
        
        // add self link
        // Wenn der Katalog self-contained ist, gibt es keinen self-link.
        if (!publicationType.equals(PublicationType.SELF_CONTAINED)) {
            Link selfLink = new Link();
            selfLink.rel("self").href(selfHref).type(LinkMimeType.APPLICATION_JSON);
            links.add(selfLink);
            
            fileName = selfHref.substring(selfHref.lastIndexOf('/')+1, selfHref.length());
        }
               
        // add root link
        // TODO: das wird komplizierter wenn vererbt wird. Weniger wegen dem Vererben als eher wegen 
        // der Kinder.
        // Die Collection zeigt immer auch auf das root-Element.
        // Zu welchem Zeitpunkt muss es den Root-Link geben? Wenn erste beim saven des katalogs, alls Kinder
        // upgedatet werden, ists hier ok.
        // Muss ich nicht wissen, ob ich Eltern habe? Erst wenn es keine Eltern mehr gibt,
        // bin ich das wirklich root-Element? Weil Catalogs können wiederum Catalogs als Kinder
        // haben. Also ist es nicht nur an der Klasse anzuhängen.
        
        // Vielleicht doch am einfachsten "parent", "root" und ggf. andere als eigenständige
        // Variablen führen und mein adden des Childs dem Child adden.
        // "root" immer das new File()-Verzeichnis als Default? Wenn es ein File ist (und kein String),
        // wird der Pfadt (../../etc pp) richtig gehandelt. resp. das Wissen liegt vorg.
        // Ah: Der PublicationType eines Kindes hat nach dem Adden zum Parent keine Bedeutung mehr, sonst
        // geht es ja nicht, da Widerspruch möglich.
        
        Link rootLink = new Link();
        rootLink.rel("root").type(LinkMimeType.APPLICATION_JSON);
        if (!publicationType.equals(PublicationType.SELF_CONTAINED)) {
            rootLink.href(selfHref);
        } else {
            rootLink.href(Paths.get(file.getAbsolutePath(), fileName).toFile().getAbsolutePath());
        }
        links.add(rootLink);
        
        for (Catalog child : children) {
            File childFile = Paths.get("/Users/stefan/tmp/gaga").toFile();
            child.save(childFile);
        }
        
        
        File resultFile = Paths.get(file.getAbsolutePath(), fileName).toFile();

        ObjectMapper objectMapper = new ObjectMapper(); // TODO: Singleton o.ä.?
        objectMapper.writeValue(resultFile, this);
    }
    
    public void save(File file) throws StreamWriteException, DatabindException, IOException {
        this.save(publicationType, file);
    }
}
