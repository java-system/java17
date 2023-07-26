package system;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SysTest {

    @Test
    void test() throws Exception {

        assertNotNull(Sys.getConversionMethod(12345, "intValue", int.class));

        assertThrows(java.lang.NullPointerException.class, () -> Sys.asInt(null));
        assertThrows(java.lang.NullPointerException.class, () -> Sys.asLong(null));
        assertThrows(java.lang.NullPointerException.class, () -> Sys.asDouble(null));

        assertEquals(123, Sys.asInt(123.45));
        assertEquals(123, Sys.asLong(123.45));
        assertEquals(123.45, Sys.asDouble(123.45));

        Object it = null;

        it = new BigDecimal(678.9);
        assertEquals(678, Sys.asInt(it));
        assertEquals(678, Sys.asLong(it));
        assertEquals(678.9, Sys.asDouble(it));

        final String s = "123.45";
        assertEquals(123, Sys.asInt(s));
        assertEquals(123, Sys.asLong(s));
        assertEquals(123.45, Sys.asDouble(s));

        Date now = new Date();
        Sys.echo(now, "now");
        String nowStr = Sys.asString(now);
        Sys.echo(nowStr, "nowStr");
        Date now2 = Sys.asDate(nowStr);
        Sys.echo(now2, "now2");
        //assertEquals(now.toString(), now2.toString());
        //assertEquals(Sys.asString(now), Sys.asString(now2));

        long nowLong = Sys.asLong(now);
        Date now3 = Sys.asDate(nowLong);
        //assertEquals(now.toString(), now3.toString());
        //assertEquals(Sys.asString(now), Sys.asString(now3));

        BigDecimal dec = null;
        dec = Sys.asDecimal(123);
        assertEquals("{\"$json\": {\"$numberDecimal\": \"123\"}}", Sys.toJson(dec, false));
        dec = Sys.asDecimal(123L);
        assertEquals("{\"$json\": {\"$numberDecimal\": \"123\"}}", Sys.toJson(dec, false));
        dec = Sys.asDecimal(123.45f);
        assertEquals("{\"$json\": {\"$numberDecimal\": \"123.45\"}}", Sys.toJson(dec, false));

    }

}
