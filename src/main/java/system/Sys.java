package system;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;

public class Sys {

    public static String toJson(Object x) {
        return BsonData.ToJson(BsonData.ToValue(x), true);
    }

    public static String toFlatJson(Object x) {
        return BsonData.ToFlatJson(BsonData.ToValue(x), true);
    }

    public static Object fromJson(String json) {
        return BsonData.FromValue(BsonData.FromJson(json));
    }

    protected static String getTypeNameString(Object x) {
        if (x == null) return "null";
        String result = "";
        x = Dynamic.strip(x);
        if (x instanceof SimpleList) return SimpleList.class.getName();
        if (x instanceof SimpleMap) return SimpleMap.class.getName();
        return x.getClass().getName();
    }

    public static void echo(Object x, String title) {
        if (title != null) System.out.printf("%s: ", title);
        String result = "";
        if (x == null) result = "null";
        else result = x.toString();
        if (x != null) {
            if (x instanceof Dynamic)
                result = "<Dynamic:" + getTypeNameString(x) /*Dynamic.strip(x).getClass().getName()*/ + "> " + result;
            else
                result = "<" + getTypeNameString(x) /*x.getClass().getName()*/ + "> " + result;
        }
        System.out.println(result);
    }

    public static void echo(Object x) {
        echo(x, null);
    }

    public static void echoJson(Object x, String title) {
        if (title != null) System.out.printf("%s: ", title);
        String result = toJson(x);
        if (x != null) {
            if (x instanceof Dynamic)
                result = "<Dynamic:" + getTypeNameString(x) /*Dynamic.strip(x).getClass().getName()*/ + "> " + result;
            else
                result = "<" + getTypeNameString(x) /*x.getClass().getName()*/ + "> " + result;
        }
        System.out.println(result);
    }

    public static void echoJson(Object x) {
        echoJson(x, null);
    }

    public static SimpleList newList(Object[] args) {
        SimpleList result = new SimpleList();
        for (int i = 0; i < args.length; i++) {
            result.add(args[i]);
        }
        return result;
    }

    public static SimpleMap newMap(Object[] args) {
        SimpleMap result = new SimpleMap();
        for (int i = 0; i < args.length; i += 2) {
            result.put((String) args[i], args[i + 1]);
        }
        return result;
    }

    public static String readAsText(String path) throws Exception {
        if (path.startsWith(":/")) {
            return ResourceUtil.GetString(path.substring(2));
        } else if (path.startsWith("http:") || path.startsWith("https:")) {
            try (InputStream in = new URL(path).openStream()) {
                return IOUtils.toString(in);
            }
        } else {
            return FileUtils.readFileToString(new File(path));
        }
    }

    public static Object readAsJson(String path) throws Exception {
        return fromJson(readAsText(path));
    }

    public static void writeStringToFile(String path, String data) throws Exception {
        MiscUtil.WriteStringToFile(path, data);
    }

    public static String readStringFromFile(String path, String fallback) throws Exception {
        return MiscUtil.ReadStringFromFile(path, fallback);
    }

    public static int asInt(Object x) {
        if (x == null) throw new NullPointerException();
        if (x instanceof Integer) return ((Integer) x).intValue();
        if (x instanceof Long) return ((Long) x).intValue();
        if (x instanceof Double) return ((Double) x).intValue();
        if (x instanceof BigDecimal) return ((BigDecimal) x).intValue();
        return Integer.valueOf(x.toString());
    }

    public static long asLong(Object x) {
        if (x == null) throw new NullPointerException();
        if (x instanceof Integer) return ((Integer) x).longValue();
        if (x instanceof Long) return ((Long) x).longValue();
        if (x instanceof Double) return ((Double) x).longValue();
        if (x instanceof BigDecimal) return ((BigDecimal) x).longValue();
        return Long.valueOf(x.toString());
    }

    public static double asDouble(Object x) {
        if (x == null) throw new NullPointerException();
        if (x instanceof Integer) return ((Integer) x).doubleValue();
        if (x instanceof Long) return ((Long) x).doubleValue();
        if (x instanceof Double) return ((Double) x).doubleValue();
        if (x instanceof BigDecimal) return ((BigDecimal) x).doubleValue();
        return Double.valueOf(x.toString());
    }

    public static String asString(Object x) {
        if (x == null) throw new NullPointerException();
        return x.toString();
    }

}
