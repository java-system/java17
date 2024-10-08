package system;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonNull;
import org.bson.BsonValue;

import java.util.List;
import java.util.Map;

public class Dynamic {
    protected Object value = null;

    protected Dynamic(Object x) {
        this.value = x;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    public String type() {
        return Sys.getTypeNameString(this.value);
        //return this.value.getClass().getName();
    }

    /*
    public Object value() {
        return this.value;
    }
    */

    public static Dynamic wrap(Object x) {
        if (x == null) return null;
        if (x instanceof Dynamic) return (Dynamic) x;
        return new Dynamic(x);
    }

    public static Object strip(Object x) {
        if (x == null) {
            return null;
        } else if (!(x instanceof Dynamic)) {
            return x;
        } else {
            //var vo = (Dynamic) x;
            return ((Dynamic) x).value;
        }
    }

    public static Dynamic newList(Object[] args) {
        SimpleList result = new SimpleList();
        for (int i = 0; i < args.length; i++) {
            result.add(strip(args[i]));
        }
        return new Dynamic(result);
    }

    public int size() {
        java.util.List<Object> list = (java.util.List<Object>) this.value;
        return list.size();
    }

    public java.util.List<Object> asList() {
        try {
            java.util.List<Object> list = (java.util.List<Object>) this.value;
            return list;
        } catch(Exception ex) {
            return null;
        }
    }

    public Dynamic getAt(int index, Object fallback) {
        java.util.List<Object> list = (java.util.List<Object>) this.value;
        Object result = list.get(index);
        if (result == null) {
            //list.set(index, strip(fallback));
            return wrap(fallback);
        }
        return new Dynamic(result);
    }

    public Dynamic getAt(int index) {
        return getAt(index, null);
    }

    /*
    public Dynamic getAt(Dynamic index) {
        return getAt(index.asInt());
    }
    */

    public void add(Object x) {
        java.util.List<Object> list = (java.util.List<Object>) this.value;
        list.add(strip(x));
    }

    public void putAt(int index, Object x) {
        java.util.List<Object> list = (java.util.List<Object>) this.value;
        list.set(index, strip(x));
    }

    public Dynamic remove(int index) {
        java.util.List<Object> list = (java.util.List<Object>) this.value;
        list.remove(index);
        return this;
    }

    public static Dynamic newMap(Object[] args) {
        SimpleMap result = new SimpleMap();
        for (int i = 0; i < args.length; i += 2) {
            result.put((String) strip(args[i]), strip(args[i + 1]));
        }
        return new Dynamic(result);
    }

    public Dynamic keys() {
        java.util.Map<String, Object> map = (java.util.Map<String, Object>) this.value;
        Object[] keys = map.keySet().toArray();
        Dynamic result = newList(new Object[]{});
        for (int i = 0; i < keys.length; i++) {
            result.add(keys[i]);
        }
        return result;
    }

    public java.util.Map<String, Object> asMap() {
        try {
            java.util.Map<String, Object> map = (java.util.Map<String, Object>) this.value;
            return map;
        } catch(Exception ex) {
            return null;
        }
    }

    public Dynamic get(String key, Object fallback) {
        java.util.Map<String, Object> map = (java.util.Map<String, Object>) this.value;
        if (!map.containsKey(key)) {
            /*
            try {
                map.put(key, strip(fallback));
            } catch (Exception e) {
            }
            */
            return wrap(fallback);
        }
        Object result = map.get(key);
        if (result == null) return null;
        return new Dynamic(result);
    }

    public Dynamic get(String key) {
        java.util.Map<String, Object> map = (java.util.Map<String, Object>) this.value;
        if (!map.containsKey(key)) {
            return null;
        }
        Object result = map.get(key);
        if (result == null) return null;
        return new Dynamic(result);
    }

    public void put(String key, Object x) {
        java.util.Map<String, Object> map = (java.util.Map<String, Object>) this.value;
        map.put(key, strip(x));
    }

    public Dynamic remove(String key) {
        java.util.Map<String, Object> map = (java.util.Map<String, Object>) this.value;
        map.remove(key);
        return this;
    }

    /*
    public void put(Dynamic key, Object x) {
        put(key.asString(), x);
    }
    */

    public int asInt() {
        //if (this.value instanceof Integer) return (int) this.value;
        //return Integer.valueOf(this.value.toString());
        return Sys.asInt(this.value);
    }

    public long asLong() {
        //if (this.value instanceof Long) return (long) this.value;
        //return Long.valueOf(this.value.toString());
        return Sys.asLong(this.value);
    }

    public double asDouble() {
        //if (this.value instanceof Double) return (double) this.value;
        //return Double.valueOf(this.value.toString());
        return Sys.asDouble(this.value);
    }

    public String asString() {
        //return this.value.toString();
        return Sys.asString(this.value);
    }

    /*
    public BsonValue toBsonValue() {
        return toBsonValue(this);
    }
    */

    public /*static*/ BsonValue toBsonValue(/*Dynamic x*/) {
        if (this.value instanceof java.util.List<?>) {
            List<Object> list = (List<Object>) /*x*/this.value;
            BsonArray result = new BsonArray();
            for (int i=0; i<list.size(); i++) {
                if (list.get(i) == null)
                    result.add(new BsonNull());
                else
                    result.add(new Dynamic(list.get(i)).toBsonValue());
            }
            return result;
        }
        if (this.value instanceof java.util.Map<?, ?>) {
            Map<String, Object> map = (Map<String, Object>) this.value;
            BsonDocument result = new BsonDocument();
            Object[] keys = map.keySet().toArray();
            for (int i=0; i<keys.length; i++) {
                if (map.get(keys[i]) == null)
                    result.put((String)keys[i], new BsonNull());
                else
                    result.put((String)keys[i], new Dynamic(map.get(keys[i])).toBsonValue());
            }
            return result;
        }
        return BsonData.ToValue(this.value);
    }

    public static Dynamic fromBsonValue(BsonValue x) {
        if (x instanceof BsonArray) {
            BsonArray array = (BsonArray) x;
            Dynamic result = newList(new Object[]{});
            for (int i=0; i<array.size(); i++) {
                result.add(fromBsonValue(array.get(i)));
            }
            return result;
        }
        if (x instanceof BsonDocument) {
            BsonDocument doc = (BsonDocument) x;
            Dynamic result = newMap(new Object[]{});
            Object[] keys = doc.keySet().toArray();
            for (int i=0; i<keys.length; i++) {
                result.put((String)keys[i], fromBsonValue(doc.get((String)keys[i])));
            }
            return result;
        }
        Object val = BsonData.FromValue(x);
        if (val == null) return null;
        return new Dynamic(val);
    }

    public String toJson(boolean indent) {
        return Sys.toJson(this.toBsonValue(), indent);
    }
    public static Dynamic fromJson(String json) {
        return new Dynamic(Sys.fromJson(json));
    }

}
