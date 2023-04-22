package ch.so.agi.stac.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class CollectionMixin {
    @JsonProperty("stac_version")
    private String stacVersion;
    @JsonProperty
    private String id;
    @JsonProperty
    private String description;
    @JsonProperty
    private String title;
    
}
