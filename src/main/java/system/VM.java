package system;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.shell.Global;

import java.io.File;
//import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class VM {
	public Context context;
	public Global global;

	public VM() {
		this.context = Context.enter();
		// load、importPackage、print を使うために必要
		this.global = new Global(context);
		// スコープを初期化
		ImporterTopLevel.init(context, global, true);
		this.eval("globalThis.console = { log: globalThis.echo }");
		this.setGlobal("$vm", this);
		this.eval("globalThis.echo = function(x, title) { $vm.echo(x, title===undefined?null:title); }");
		this.eval("globalThis.load = function(path) { return $vm.loadFile(path); }");
		this.eval("globalThis.vm = { echo: globalThis.echo, load: globalThis.load }");
	}

	public Object setGlobal(String name, Object x) {
		ScriptableObject.putProperty(global, name, toNative(x));
		return ScriptableObject.getProperty(global, name);
	}

	public Object runWithPath(String path, String script, Object[] args) {
		Scriptable scope = context.initStandardObjects(global);
		for (int i = 0; i < args.length; i++) {
			ScriptableObject.putProperty(scope, "$" + i, toNative(args[i]));
		}
		return context.evaluateString(scope, script, path, 1, null);
	}

	public Object run(String script, Object[] args) {
		return runWithPath("<eval>", script, args);
	}

	public Object eval(String script, Object... args) {
		return run(script, args);
	}

	public Object evalToJson(String script, Object... args) {
		return toJson(run(script, args));
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

	public void echo(Object x, String title) {
		if (title != null) {
			System.out.print(title);
			System.out.print(": ");
		}
		// run("print(JSON.stringify($0, null, 2))", x);
		if (x instanceof String) {
			System.out.println(x);
		} else {
			String json = (String) eval("JSON.stringify($0, null, 2)", x);
			System.out.println(json);
		}
	}

	public void echo(Object x) {
		echo(x, null);
	}

	public Object load(String x) {
		return this.eval("load($0)", x);
	}

	public Object loadToJson(String x) {
		return this.evalToJson("load($0)", x);
	}

	public Object toJson(Object x) {
		if (x == null)
			return null;
		String className = x.getClass().getName();
		switch (className) {
		case "org.mozilla.javascript.NativeArray": {
			org.mozilla.javascript.NativeArray ary = (org.mozilla.javascript.NativeArray) x;
			JSONArray result = new JSONArray();
			for (int i = 0; i < ary.size(); i++) {
				result.put(i, toJson(ary.get(i)));
			}
			return result;
		}
		case "org.mozilla.javascript.NativeObject": {
			org.mozilla.javascript.NativeObject obj = (org.mozilla.javascript.NativeObject) x;
			JSONObject result = new JSONObject();
			Object[] keys = obj.keySet().toArray();
			for (int i = 0; i < keys.length; i++) {
				result.put((String) keys[i], toJson(obj.get(keys[i])));
			}
			return result;
		}
		default: {
			return x;
		}
		}
	}

	public Object toNative(Object x) {
		if (x == null)
			return null;
		String className = x.getClass().getName();
		switch (className) {

		case "org.json.JSONArray": {
			org.json.JSONArray ary = (org.json.JSONArray) x;
			NativeArray result = new NativeArray(ary.length());
			for (int i = 0; i < ary.length(); i++) {
				NativeArray.putProperty(result, i, toNative(ary.get(i)));
			}
			return result;
		}
		case "org.json.JSONObject": {
			org.json.JSONObject obj = (org.json.JSONObject) x;
			NativeObject result = new NativeObject();
			Object[] keys = obj.keySet().toArray();
			for (int i = 0; i < keys.length; i++) {
				NativeObject.putProperty(result, (String) keys[i], toNative(obj.get((String) keys[i])));
			}
			return result;
		}
		default: {
			return x;
		}
		}
	}

	public String getSourceCode(String path) throws Exception {
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

	public Object loadFile(String path) throws Exception {
		return this.runWithPath(path, this.getSourceCode(path), new Object[] {});
	}

}
