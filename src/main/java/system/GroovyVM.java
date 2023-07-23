package system;

//import groovy.lang.Binding;
//import groovy.lang.GroovyShell;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class GroovyVM {

    protected Class<?> bindingClass = null;
    protected Object binding = null;
    protected Class<?> shellClass = null;
    protected Object shell = null;

    public java.util.Map<String, Long> imported = new java.util.LinkedHashMap<String, Long>();

    public GroovyVM() {
        try {
            this.bindingClass = Class.forName("groovy.lang.Binding");
            this.binding = bindingClass.newInstance();
            Method setPropertyMethod = bindingClass.getMethod("setProperty", String.class, Object.class);
            setPropertyMethod.invoke(this.binding, "vm", this);
            this.shellClass = Class.forName("groovy.lang.GroovyShell");
            Constructor<?> shellCons = this.shellClass.getConstructor(ClassLoader.class, bindingClass);
            this.shell = shellCons.newInstance(Thread.currentThread().getContextClassLoader(), this.binding);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setVariable(String name, Object x) {
        //this.binding.setVariable(name, x);
        try {
            Method setVariableMethod = this.bindingClass.getMethod("setVariable", String.class, Object.class);
            setVariableMethod.invoke(this.binding, name, x);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getVariable(String name) {
        //return this.binding.getVariable(name);
        try {
            Method getVariableMethod = this.bindingClass.getMethod("getVariable", String.class);
            return getVariableMethod.invoke(this.binding, name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasVariable(String name) {
        //return this.binding.hasVariable(name);
        try {
            Method hasVariableMethod = this.bindingClass.getMethod("hasVariable", String.class);
            return (boolean)hasVariableMethod.invoke(this.binding, name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object run(String script, Object[] args) {
        for (int i = 0; i < args.length; i++) {
            this.setVariable("_" + i, args[i]);
        }
        try {
            //return this.shell.evaluate(script);
            try {
                Method evaluateMethod = this.shellClass.getMethod("evaluate", String.class);
                return evaluateMethod.invoke(this.shell, script);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } finally {
            for (int i = 0; i < args.length; i++) {
                this.setVariable("_" + i, null);
            }
        }
    }

    public Object eval(String script, Object... args) {
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

    public String toFlatJson(Object x) {
        return Sys.toFlatJson(x);
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

    public String readAsText(String path) {
        try {
            return Sys.readAsText(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object readAsJson(String path) {
        try {
            return Sys.readAsJson(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object load(String path) {
        try {
            return eval(readAsText(path));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void require(String path) {
        try {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeStringToFile(String path, String data) {
        try {
            Sys.writeStringToFile(path, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String readStringFromFile(String path, String fallback) {
        try {
            return Sys.readStringFromFile(path, fallback);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
