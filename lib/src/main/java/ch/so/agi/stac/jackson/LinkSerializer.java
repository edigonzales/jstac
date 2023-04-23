package ch.so.agi.stac.jackson;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import ch.so.agi.stac.model.Bbox;
import ch.so.agi.stac.model.Link;

public class LinkSerializer extends StdSerializer<Link> {

    public LinkSerializer() {
        this(null);
    }

    protected LinkSerializer(Class<Link> t) {
        super(t);
    }

    @Override
    public void serialize(Link value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String rel = value.getRel().toString();
        String type = value.getMediaType().toString();
        String href = value.getHref();
        String title = value.getTitle();
                
        gen.writeStartObject();
        gen.writeStringField("rel", rel);
        gen.writeStringField("type", type);
        gen.writeStringField("href", href);
        gen.writeStringField("title", title);
        gen.writeEndObject();
    }
}
