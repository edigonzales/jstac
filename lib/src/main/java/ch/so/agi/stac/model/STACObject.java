package ch.so.agi.stac.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class STACObject {
    private List<Link> links = new ArrayList<>();
    
    public void addLink(Link link) {
        link.setOwner(this);
        links.add(link);
    }
    
    public void addLinks(List<Link> links) {
        for (Link link : links) {
            this.addLink(link);
        }
    }
    
    public void removeLinks(RelType rel) {
        links.removeIf(l -> l.getRel().equals(rel));
    }
    
    // remove_hierarchical_links: link.isHierachical?
    
    // get_single_link
    @JsonIgnore
    public Link getSingleLink(RelType rel) {
        return links.stream()
        .filter(l -> l.getRel().equals(rel))
        .findFirst()
        .orElse(null);
    }
    
    @JsonIgnore
    public Link getSingleLink() {
        return links.stream()
        .findFirst()
        .orElse(null);
    }
    
    // get_links
    @JsonIgnore
    public List<Link> getLinks(RelType rel) {
        return links.stream()
        .filter(l -> l.getRel().equals(rel))
        .collect(Collectors.toList());
    }
    
    public List<Link> getLinks() {
        return links;
    }
    
    // clear_links
    
    // get_root_link
    @JsonIgnore
    public Link getRootLink() {
        return getSingleLink(RelType.ROOT);
    }
    
    // property: self_href
    
    // get_self_href
    @JsonIgnore
    public String getSelfHref() {
        Link selfLink = getSingleLink(RelType.SELF);
        if (selfLink != null && selfLink.hasTargetRef()) {
            return selfLink.getTargetStr();
        } else {
            return null;
        }
    }
    
    // set_self_href
    
    // get_root
    @JsonIgnore
    public Collection getRoot() {
        Link rootLink = getRootLink();
        
        if (rootLink != null) {
            if (!rootLink.isResolved()) {
                System.out.println("resolve_stac_object TODO");
                // TODO "resolve_stac_object" dünkt mich noch tricky, da auch von file gelesen werden können muss. 
            }
            return (Collection) rootLink.getTarget();
        } else {
            return null;
        }
        
    }

    // set_root
    public void setRoot(Collection root) {
        int rootLinkIdx = -1; 
        for (int i=0; i< links.size(); i++) {
            if (links.get(i).getRel().equals(RelType.ROOT)) {
                rootLinkIdx = i;
                break;
            }
        }

        if (root == null) {
            removeLinks(RelType.ROOT);
        } else {
            Link rootLink = Link.root(root);
            if (rootLinkIdx > -1) {
                links.set(rootLinkIdx, rootLink);
                rootLink.setOwner(this);
            } else {
                addLink(rootLink);
            }
        }
    }
    
    // get_parent
    
    // set_parent
    public void setParent(Collection parent) {
        removeLinks(RelType.PARENT);
        if (parent != null) {
            this.addLink(Link.parent(parent));
        }
    }
    
    // get_stac_object
    
    // save_object
    
    // resolve_links
    
}
