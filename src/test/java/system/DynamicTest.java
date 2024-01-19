package system;

import org.bson.BsonDocument;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.Date;

class DynamicTest {

    @Test
    void test() throws Exception {
        Sys.echo("ハロー©");
        Dynamic list = Dynamic.newList(new Object[]{11, "abc", null, 12L, new Date()});
        Sys.echo(list);
        Sys.echo(list.getAt(0));
        Sys.echo(list.getAt(0).asInt());
        Sys.echo(list.getAt(0).asLong());
        Sys.echo(list.getAt(0).asDouble());
        Sys.echoJson(list.getAt(3).asLong());
        Sys.echo(list.getAt(2) == null);
        for (int i=0; i<list.size(); i++) {
            Sys.echoJson(list.getAt(i), "" + i);
        }
        var listBson = Dynamic.toBsonValue(list);
        Sys.echo(listBson, "listBson");
        var list2 = Dynamic.fromBsonValue(listBson);
        Sys.echo(list2, "list2");
        Dynamic map = Dynamic.newMap(new Object[]{
                list.getAt(1), "aaa",
                "xyz", 12.3, "dt",
                new Date(), "bytes",
                new byte[] {1, 2, 3}});
        Sys.echo(map);
        Dynamic keys = map.keys();
        Sys.echo(keys);
        for (int i=0; i<keys.size(); i++) {
            Sys.echo(keys.getAt(i));
            Sys.echo(map.get(keys.getAt(i).asString()));
        }
        var mapBson = (BsonDocument)Dynamic.toBsonValue(map);
        Sys.echo(mapBson, "mapBson");
        var mapBytes = BsonData.EncodeToBytes(mapBson);
        Files.write(new File("C:/ProgramData/tmp.bson").toPath(), mapBytes);
        byte[] mapBytes2 = Files.readAllBytes(new File("C:/ProgramData/tmp.bson").toPath());
        var mapBson2 = BsonData.DecodeFromBytes(mapBytes2);
        Sys.echo(mapBson2, "mapBson2");
        var mapJson = Sys.toJson(map, true);
        Sys.echo(mapJson, "mapJson");
        var map2 = Sys.fromJson(mapJson);
        Sys.echo(map2, "map2");
    }

}
