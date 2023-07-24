package system;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroovyVMTest {

  @Test
  void test() throws Exception {
    GroovyVM vm = new GroovyVM();
    vm.echo(123);
    Dynamic result1 = Dynamic.wrap(vm.eval("[a: 777]"));
    assertEquals("{a=777}", result1.toString());
    Dynamic result2 = Dynamic.wrap(vm.eval("_0 + _1", 11, 22));
    assertEquals("33", result2.toString());
    vm.echo(vm.toFlatJson(result1));
    assertEquals("{\n" +
            "  \"a\": 777\n" +
            "}", vm.toFlatJson(result1));
    vm.echo(vm.toFlatJson(result2));
    assertEquals("{\n" +
            "  \"$json\": 33\n" +
            "}", vm.toFlatJson(result2));
  }

}
