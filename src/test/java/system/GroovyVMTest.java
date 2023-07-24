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

    int intVar = vm.evalAsInt("123.45");
    vm.echo(intVar);
    assertEquals(123, intVar);

    intVar = vm.evalAsInt("123.45D");
    vm.echo(intVar);
    assertEquals(123, intVar);

    intVar = vm.evalAsInt("12345L");
    vm.echo(intVar);
    assertEquals(12345, intVar);

    intVar = vm.evalAsInt("'12345'");
    vm.echo(intVar);
    assertEquals(12345, intVar);

    String strVar = vm.evalAsString("123.45");
    vm.echo(strVar);
    assertEquals("123.45", strVar);

    var list = new SimpleList() {
      {
        add(11);
        add("abc");
        add(null);
      }
    };
    int listLen = vm.evalAsInt("_0.size()", list);
    assertEquals(3, listLen);
    for (int i=0; i<vm.evalAsInt("_0.size()", list); i++) {
      vm.echo(vm.eval("_0[_1]", list, i));
    }
  }

}
