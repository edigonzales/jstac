package ch.so.agi.stac.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import ch.so.agi.stac.model.Bbox;

public class BboxDeserializer extends StdDeserializer<Bbox> {

    public BboxDeserializer() { 
        this(null); 
    } 

    public BboxDeserializer(Class<?> vc) { 
        super(vc); 
    }

    @Override
    public Bbox deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);
        ArrayNode bboxArrayNode = (ArrayNode) node.get("spatial").get("bbox");
        
        double west = ((ArrayNode) bboxArrayNode.get(0)).get(0).asDouble();
        double south = ((ArrayNode) bboxArrayNode.get(0)).get(1).asDouble();
        double east = ((ArrayNode) bboxArrayNode.get(0)).get(2).asDouble();
        double north = ((ArrayNode) bboxArrayNode.get(0)).get(3).asDouble();
        
        return new Bbox().west(west).south(south).east(east).north(north);
    }

}
