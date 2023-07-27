package system;

import org.junit.jupiter.api.Test;

class DynamicTest {

    @Test
    void test() throws Exception {
        Dynamic list = Dynamic.newList(new Object[]{11, "abc", null, 12L});
        Sys.echo(list);
        Sys.echo(list.getAt(0));
        Sys.echo(list.getAt(0).asInt());
        Sys.echo(list.getAt(0).asLong());
        Sys.echo(list.getAt(0).asDouble());
        Sys.echoJson(list.getAt(3).asLong());
        Sys.echo(list.getAt(2) == null);
        for (int i=0; i<list.size(); i++) {
            Sys.echo(list.getAt(i), "" + i);
        }
        Dynamic map = Dynamic.newMap(new Object[]{list.getAt(1), "aaa", "xyz", 12.3});
        Sys.echo(map);
        Dynamic keys = map.keys();
        Sys.echo(keys);
        for (int i=0; i<keys.size(); i++) {
            Sys.echo(keys.getAt(i));
            Sys.echo(map.get(keys.getAt(i).asString()));
        }
    }

}
