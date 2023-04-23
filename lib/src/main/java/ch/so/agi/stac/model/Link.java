package ch.so.agi.stac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonInclude(Include.NON_NULL)
public class Link {
    private RelType rel;
    private String href;
    private Object target;
    private MediaType mediaType;
    private String title;
    
    private STACObject owner;
    
    private String targetHref;
    private STACObject targetObject;
    
    @Override
    public String toString() {
        return "Link [rel=" + rel + ", href=" + href + ", target=" + target + ", mediaType=" + mediaType + ", title="
                + title + ", owner=" + owner + ", targetHref=" + targetHref + ", targetObject=" + targetObject + "]";
    }

    public Link(LinkBuilder builder) {
        this.rel = builder.rel;
        this.target = builder.target;
        this.mediaType = builder.mediaType;
        this.title = builder.title;
        
        if (this.target instanceof STACObject) {
            this.targetObject = (STACObject) target;
        } else  {
            this.targetHref = (String) target;
        }
    }

    public RelType getRel() {
        return rel;
    }

    public void setRel(RelType rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
    
    public MediaType getMediaType() {
        return mediaType;
    }

    public String getTitle() {
        return title;
    }

    public void setOwner(STACObject owner) {
        this.owner = owner;
    }
    
    public boolean isResolved() {
        return targetObject != null;
    }
    
    public STACObject getTarget() {
        return (STACObject) target;
    }
    
    public String getTargetStr() {
        if (targetHref != null) {
            return targetHref;
        } else if (targetObject != null) {
            return targetObject.getSelfHref();
        } else {
            return null;
        }
    }
    
    public boolean hasTargetRef() {
        return targetHref != null;
    }
        
    public static Link root(Collection root) {
        return new Link.LinkBuilder().rel(RelType.ROOT).mediaType(MediaType.APPLICATION_JSON).target(root).build();
    }

    public static Link collection(Collection collection) {
        return new Link.LinkBuilder().rel(RelType.COLLECTION).mediaType(MediaType.APPLICATION_JSON).target(collection).build();
    }
    
    public static Link parent(Collection parent) {
        return new Link.LinkBuilder().rel(RelType.PARENT).mediaType(MediaType.APPLICATION_JSON).target(parent).build();
    }

    public static Link item(Item item) {
        return new Link.LinkBuilder().rel(RelType.ITEM).mediaType(MediaType.APPLICATION_JSON).target(item).build();
    }

    public static Link child(Collection child) {
        return new Link.LinkBuilder().rel(RelType.COLLECTION).mediaType(MediaType.APPLICATION_JSON).target(child).build();
    }

    public static Link self_href(String href) {
        return new Link.LinkBuilder().rel(RelType.SELF).mediaType(MediaType.APPLICATION_JSON).target(href).build();
    }
    
    public static class LinkBuilder {
        private RelType rel;
        private Object target;
        private MediaType mediaType;
        private String title;
        
        public LinkBuilder rel(RelType rel) {
            this.rel = rel;
            return this;
        }

        public LinkBuilder target(Object target) {
            this.target = target;
            return this;
        }

        public LinkBuilder mediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public LinkBuilder title(String title) {
            this.title = title;
            return this;
        }

        public Link build() {
            return new Link(this);
        }
    } 
    
    public enum MediaType {
        APPLICATION_JSON("application/json");
        
        private String value;

        MediaType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static MediaType fromString(String text) {
            for (MediaType l : MediaType.values()) {
                if (l.value.equalsIgnoreCase(text)) {
                    return l;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

}
