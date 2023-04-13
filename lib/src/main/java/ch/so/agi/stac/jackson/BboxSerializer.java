package ch.so.agi.stac.jackson;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import ch.so.agi.stac.model.Bbox;

public class BboxSerializer extends StdSerializer<Bbox> {

    public BboxSerializer() {
        this(null);
    }

    protected BboxSerializer(Class<Bbox> t) {
        super(t);
    }

    @Override
    public void serialize(Bbox value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        List<Double> bboxList = List.of(value.getWest(), value.getSouth(), value.getEast(), value.getNorth());
        Double[] bboxDouble = bboxList.toArray(new Double[bboxList.size()]);
        double[] bbox = Stream.of(bboxDouble).mapToDouble(Double::doubleValue).toArray();
        
        gen.writeStartObject();
        gen.writeFieldName("spatial");
            gen.writeStartObject();
            gen.writeArrayFieldStart("bbox");
            gen.writeArray(bbox, 0, 4);
            gen.writeEndArray();
            gen.writeEndObject();
        gen.writeEndObject();
    }
}
