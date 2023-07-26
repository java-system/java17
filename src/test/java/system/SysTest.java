package system;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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

    }

}
