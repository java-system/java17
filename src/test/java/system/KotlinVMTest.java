package system;

import org.junit.jupiter.api.Test;

class KotlinVMTest {

    @Test
    void test() throws Exception {
        var vm = new system.KotlinVM();
        //ScriptEngineManager manager = new ScriptEngineManager();
        //ScriptEngine engine = manager.getEngineByExtension("main.kts");
        //vm.echo(engine);
        vm.setVariable("_0", "xyz");
        vm.setVariable("result", null);
        var result = vm.eval("""
@file:Repository("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")

import kotlinx.html.*
import kotlinx.html.stream.*
import kotlinx.html.attributes.*

val addressee = "World"

print(
    createHTML().html {
        body {
            h1 { +"Hello, $addressee!" }
        }
    }
)
println(_0)
val array = vm.newList(1, "a", null)
vm.echo(array, "array")
val obj = vm.newMap("a", "11", "b", array)
vm.echo(obj, "obj")
vm.echo(obj.get("b"));
"abc"
""", "xyz-2");
        vm.echo(result);
    }

}