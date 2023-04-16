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
    
    public void removeLinks(RelType rel) {
        links.removeIf(l -> l.getRel().equals(rel));
    }
    
    public List<Link> getLinks(RelType rel) {
        return links.stream()
        .filter(l -> l.getRel().equals(rel))
        .collect(Collectors.toList());
    }
    
    public List<Link> getLinks() {
        return links;
    }
}
