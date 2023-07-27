package system;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;

public class GroovyVM {

    protected groovy.lang.Binding binding = null;
    protected groovy.lang.GroovyShell shell = null;

    public java.util.Map<String, Long> imported = new java.util.LinkedHashMap<String, Long>();

    public GroovyVM() {
        this.binding = new Binding();
        this.binding.setProperty("vm", this);
        this.shell = new GroovyShell(Thread.currentThread().getContextClassLoader(), this.binding);
    }

    public void setVariable(String name, Object x) {
        this.binding.setVariable(name, x);
    }

    public Object getVariable(String name) {
        return this.binding.getVariable(name);
    }

    public boolean hasVariable(String name) {
        return this.binding.hasVariable(name);
    }

    private Object run(String script, Object[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            this.setVariable("_" + i, args[i]);
        }
        try {
            return this.shell.evaluate(script);
        } finally {
            for (int i = 0; i < args.length; i++) {
                this.setVariable("_" + i, null);
            }
        }
    }

    public Object eval(String script, Object... args) throws Exception {
        return run(script, args);
    }

    public int evalAsInt(String script, Object... args) throws Exception {
        return Sys.asInt(run(script, args));
    }

    public long evalAsLong(String script, Object... args) throws Exception {
        return Sys.asLong(run(script, args));
    }

    public double evalAsDouble(String script, Object... args) throws Exception {
        return Sys.asDouble(run(script, args));
    }

    public String evalAsString(String script, Object... args) throws Exception {
        return Sys.asString(run(script, args));
    }

    public Object __eval__(String script, Object... args) {
        try {
            return run(script, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void echo(Object x, String title) {
        Sys.echo(x, title);
    }

    public void echo(Object x) {
        Sys.echo(x);
    }

    public void echoJson(Object x, String title) {
        Sys.echoJson(x, title);
    }

    public void echoJson(Object x) {
        Sys.echoJson(x);
    }

    public String toJson(Object x) {
        return Sys.toJson(x, true);
    }

    public String toFlatJson(Object x) {
        return Sys.toFlatJson(x, true);
    }

    public Object fromJson(String json) {
        return Sys.fromJson(json);
    }

    public java.util.List<Object> newList(Object... args) {
        return Sys.newList(args);
    }

    public java.util.Map<String, Object> newMap(Object... args) {
        return Sys.newMap(args);
    }

    public String readAsText(String path) throws Exception {
        return Sys.readAsText(path);
    }

    public Object readAsJson(String path) throws Exception {
        return Sys.readAsJson(path);
    }

    public Object load(String path) throws Exception {
        return eval(readAsText(path));
    }

    public void require(String path) throws Exception {
        if (path.startsWith(":/")) {
        } else if (path.startsWith("http:") || path.startsWith("https:")) {
        } else {
            path = new File(path).getAbsolutePath();
        }
        if (this.imported.containsKey(path)) {
            long count = this.imported.get(path);
            this.imported.put(path, count + 1);
            return;
        }
        eval(readAsText(path));
        this.imported.put(path, 1L);
    }

    public void writeStringToFile(String path, String data) throws Exception {
        Sys.writeStringToFile(path, data);
    }

    public String readStringFromFile(String path, String fallback) throws Exception {
        return Sys.readStringFromFile(path, fallback);
    }

}
