package ch.so.agi.stac.jackson;

import java.io.IOException;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class IntervalDeserializer extends StdDeserializer<Interval> {

    public IntervalDeserializer() { 
        this(null); 
    } 

    public IntervalDeserializer(Class<?> vc) { 
        super(vc); 
    }

    @Override
    public Interval deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);
        ArrayNode intervalArrayNode = (ArrayNode) node.get("interval").get(0);
        
        Interval interval = new Interval();
        if (!intervalArrayNode.get(0).isNull()) {
            interval.setStartInterval(ZonedDateTime.parse(intervalArrayNode.get(0).asText()));
        }
        
        if (!intervalArrayNode.get(1).isNull()) {
            interval.setEndInterval(ZonedDateTime.parse(intervalArrayNode.get(1).asText()));
        }  
        
        return interval;
    }
}
