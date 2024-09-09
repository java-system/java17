package demo;

import system.Dynamic;
import system.Sys;

public class Main {
    public static void main(String[] args) {
        System.out.println("This is demo.Main");
        var o = Sys.fromJson("{\"a\": [1,2,3]}");
        Sys.echo(o);
        var d = Dynamic.fromJson("{\"b\": [1,2.0,3]}");
        Sys.echo(d);
        Sys.echo(d.get("b"));
        Sys.echo(d.get("b").getAt(1));
        d.get("b").asList().remove(0);
        Sys.echo(d);
        //d.asList().remove(0);
        Sys.echo(d.asMap().size(), "d.asMap().size()");
        Sys.echo(d.toJson(true));
    }
}
