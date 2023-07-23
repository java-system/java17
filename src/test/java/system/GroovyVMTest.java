package system;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroovyVMTest {

  @Test
  void test() {
    GroovyVM vm = new GroovyVM();
    vm.echo(123);
    Dynamic result = Dynamic.wrap(vm.eval("[a: 777]"));
    assertEquals("{a=777}", result.toString());
    result = Dynamic.wrap(vm.eval("_0 + _1", 11, 22));
    assertEquals("33", result.toString());
  }

}
