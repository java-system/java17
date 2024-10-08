package system;

////import com.oracle.truffle.regex.tregex.util.json.JsonNull;

import org.bson.*;
import org.bson.conversions.Bson;
import org.bson.types.*;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.io.BasicOutputBuffer;
import org.bson.json.JsonWriterSettings;
////import org.json.JSONArray;
////import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class BsonData {
    public static byte[] EncodeToBytes(BsonDocument doc) {
        BasicOutputBuffer outputBuffer = new BasicOutputBuffer();
        BsonBinaryWriter writer1 = new BsonBinaryWriter(outputBuffer);
        byte[] bsonBytes2 = null;
        try {
            new BsonDocumentCodec().encode(writer1, doc, org.bson.codecs.EncoderContext.builder().build());
            writer1.flush();
            outputBuffer.flush();
            bsonBytes2 = outputBuffer.toByteArray();
            outputBuffer.close();
        } catch (Exception e) {
            throw new BsonSerializationException("Error converting BsonDocument to byte array: " + e.getMessage());
        }
        return bsonBytes2;
    }

    public static BsonDocument DecodeFromBytes(byte[] bytes) {
        RawBsonDocument rawDoc = new RawBsonDocument(bytes);
        return DecodeFromBytes_Helper(rawDoc).asDocument();
    }

    private static BsonValue DecodeFromBytes_Helper(BsonValue x) {
        if (x.isDocument()) {
            BsonDocument result = new BsonDocument();
            Set<String> keys = x.asDocument().keySet();
            for (String key : keys) {
                result.put(key, DecodeFromBytes_Helper(x.asDocument().get(key)));
            }
            return result;
        } else if (x.isArray()) {
            BsonArray result = new BsonArray();
            for (int i = 0; i < x.asArray().size(); i++) {
                result.set(i, DecodeFromBytes_Helper(x.asArray().get(i)));
            }
            return result;
        }
        return x;
    }

    public static String ToJson(BsonValue x, boolean indent) {
        BsonDocument doc = null;
        doc = new BsonDocument();
        doc.put("$json", x);
        return doc.toJson(JsonWriterSettings.builder().indent(indent).build())
                .replaceAll("\r\n", "\n");
    }

    public static String ToFlatJson(BsonValue x, boolean indent) {
        BsonDocument doc = null;
        if (x instanceof BsonDocument) {
            doc = (BsonDocument) x;
        } else {
            doc = new BsonDocument();
            doc.put("$json", x);
        }
        return doc.toJson(JsonWriterSettings.builder().indent(indent).build())
                .replaceAll("\r\n", "\n");
    }

    public static BsonValue FromJson(String json) {
        BsonDocument doc = BsonDocument.parse(json);
        if (doc.containsKey("$json")) {
            return doc.get("$json");
        }
        return doc;
    }

    public static BsonArray NewOArray() {
        return new BsonArray();
    }

    public static BsonDocument NewObject() {
        return new BsonDocument();
    }

    public static BsonValue ToValue(Object x) {
        if (x instanceof BsonValue) return (BsonValue) x;
        if (x == null) return new BsonNull();
        if (x instanceof Dynamic) x = Dynamic.strip(x);
        if (x instanceof Boolean) return new BsonBoolean((boolean) x);
        if (x instanceof Integer) return new BsonInt32((int) x);
        if (x instanceof Long) return new BsonInt64((long) x);
        if (x instanceof Double) return new BsonDouble((double) x);
        if (x instanceof BigDecimal) return new BsonDecimal128(new Decimal128((BigDecimal) x));
        if (x instanceof String) return new BsonString((String) x);
        if (x instanceof Date) return new BsonDateTime(((Date) x).getTime());
        if (x instanceof byte[]) return new BsonBinary((byte[]) x);
        if (x instanceof java.util.List<?>) {
            java.util.List<?> array = (java.util.List<?>) x;
            BsonArray result = new BsonArray();
            for (int i = 0; i < array.size(); i++) {
                result.add(ToValue(array.get(i)));
            }
            return result;
        }
        if (x instanceof java.util.Map<?, ?>) {
            java.util.Map<?, ?> object = (java.util.Map<?, ?>) x;
            BsonDocument result = new BsonDocument();
            Object[] keys = object.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                result.put((String) keys[i], ToValue(object.get((String) keys[i])));
            }
            return result;
        }
        throw new RuntimeException(x.getClass().getName());
    }

    public static Object FromValue(BsonValue x) {
        if (x == null) return null;
        if (x instanceof BsonNull) return null;
        if (x instanceof BsonBoolean) return x.asBoolean().getValue();
        if (x instanceof BsonInt32) return x.asInt32().intValue();
        if (x instanceof BsonInt64) return x.asInt64().longValue();
        if (x instanceof BsonDouble) return x.asDouble().doubleValue();
        if (x instanceof BsonDecimal128) return x.asDecimal128().decimal128Value().bigDecimalValue();
        if (x instanceof BsonString) return x.asString().getValue();
        if (x instanceof BsonDateTime) return new Date(x.asDateTime().getValue());
        if (x instanceof BsonBinary) return x.asBinary().getData();
        if (x instanceof BsonArray) {
            BsonArray array = x.asArray();
            SimpleList result = new SimpleList();
            for (int i = 0; i < array.size(); i++) {
                result.add(FromValue(array.get(i)));
            }
            return result;
        }
        if (x instanceof BsonDocument) {
            BsonDocument doc = x.asDocument();
            SimpleMap result = new SimpleMap();
            Object[] keys = doc.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                result.put((String) keys[i], FromValue(doc.get((String) keys[i])));
            }
            return result;
        }
        return x;
    }

    public static void PutBsonToFileEnd(String filePath, BsonDocument doc) throws Exception {
        byte[] bytes = EncodeToBytes(doc);
        MiscUtil.PutBytesToFileEnd(filePath, bytes);
    }

    public static BsonDocument GetBsonFromFileEnd(String filePath) throws Exception {
        byte[] bytes = MiscUtil.GetBytesFromFileEnd(filePath);
        return DecodeFromBytes(bytes);
    }

}
