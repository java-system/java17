package system;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Sys {

    public static String toJson(Object x, boolean indent) {
        return BsonData.ToJson(BsonData.ToValue(x), indent);
    }

    public static String toFlatJson(Object x, boolean indent) {
        return BsonData.ToFlatJson(BsonData.ToValue(x), indent);
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
        String result = toJson(x, true);
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

    protected static Method getConversionMethod(Object x, String name, Class<?> returnClass) {
        //Sys.echo(x, "x");
        Class<?> xClass = x.getClass();
        //Sys.echo(xClass, "xClass");
        //Sys.echo(returnClass, "returnClass");
        try {
            Method m = xClass.getMethod(name);
            //Sys.echo(m.getReturnType(), "m.getReturnType()");
            if (m.getReturnType() != returnClass) return null;
            return m;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static int asInt(Object x) {
        x = Dynamic.strip(x);
        if (x == null) throw new NullPointerException();
        if (x instanceof Date) return Sys.asInt(((Date) x).getTime());
        Method m = getConversionMethod(x, "intValue", int.class);
        if (m != null) {
            try {
                return (int) m.invoke(x);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return new BigDecimal(x.toString()).intValue();
    }

    public static long asLong(Object x) {
        x = Dynamic.strip(x);
        if (x == null) throw new NullPointerException();
        if (x instanceof Date) return ((Date) x).getTime();
        Method m = getConversionMethod(x, "longValue", long.class);
        if (m != null) {
            try {
                return (long) m.invoke(x);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return new BigDecimal(x.toString()).longValue();
    }

    public static double asDouble(Object x) {
        x = Dynamic.strip(x);
        if (x == null) throw new NullPointerException();
        if (x instanceof Date) return Sys.asDouble(((Date) x).getTime());
        Method m = getConversionMethod(x, "doubleValue", double.class);
        if (m != null) {
            try {
                return (double) m.invoke(x);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return new BigDecimal(x.toString()).doubleValue();
    }

    public static BigDecimal asDecimal(Object x) {
        x = Dynamic.strip(x);
        if (x == null) throw new NullPointerException();
        if (x instanceof BigDecimal) return (BigDecimal) x;
        if (x instanceof Integer) return new BigDecimal((Integer) x);
        if (x instanceof Long) return new BigDecimal((Long) x);
        if (x instanceof Double) return new BigDecimal((Double) x);
        return new BigDecimal(x.toString());
    }

    public static String asString(Object x) {
        x = Dynamic.strip(x);
        if (x == null) throw new NullPointerException();
        if (x instanceof Date) {
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
            df.setTimeZone(tz);
            String result = df.format(new Date());
            return result;
        }
        return x.toString();
    }

    public static Date asDate(Object x) {
        x = Dynamic.strip(x);
        if (x == null) throw new NullPointerException();
        if (x instanceof Date) return (Date) x;
        if ((x instanceof Integer) || (x instanceof Long) || (x instanceof Double)) {
            long time = Sys.asLong(x);
            Date result = new Date();
            result.setTime(time);
            return result;
        }
        Date result = org.joda.time.DateTime.parse(x.toString()).toDate();
        return result;
    }

    public static byte[] asBytes(Object x) {
        x = Dynamic.strip(x);
        if (x == null) throw new NullPointerException();
        if (x instanceof byte[]) return (byte[]) x;
        throw new IllegalArgumentException();
    }

}
