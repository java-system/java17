package system;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class KotlinVM {
    protected ScriptEngine engine = null;
    public KotlinVM() {
        ScriptEngineManager manager = new ScriptEngineManager();
        this.engine = manager.getEngineByExtension("main.kts");
        engine.put("vm", this);
    }
    public void setVariable(String name, Object x) {
        this.engine.put(name, x);
    }

    public Object getVariable(String name) {
        return this.engine.get(name);
    }

    private Object run(String script, Object[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            this.setVariable("_" + i, args[i]);
        }
        return this.engine.eval(script);
    }

    public Object eval(String script, Object... args) throws Exception {
        return run(script, args);
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
        return Sys.toJson(x);
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

}
