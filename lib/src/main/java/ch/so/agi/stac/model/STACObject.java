package ch.so.agi.stac.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class STACObject {
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
    
    // property: self_href
    
    // get_self_href
    
    // set_self_href
    
    // get_root
    
    // set_root
    
    // get_parent
    
    // set_parent
    
    // get_stac_object
    
    // save_object
    
    // resolve_links
    
}
