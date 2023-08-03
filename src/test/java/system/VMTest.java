package system;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VMTest {

	@Test
	void test() throws Exception {
		//fail("Not yet implemented");
		VM vm = new VM();
		Object result = vm.eval("_0 + _1", 11, 22);
		vm.echo(result);
		result = vm.eval("[1, 2, null]");
		assertTrue(result instanceof java.util.List);
		for (int i=0; i<vm.evalAsInt("_0.length", result); i++) {
			vm.eval("vm.echo(_0[_1])", result, i);
		}
		Dynamic dyn = Dynamic.wrap(result);
		for (int i=0; i<dyn.size(); i++) {
			vm.echo(dyn.getAt(i));
		}
		result = vm.eval("({a: 1, b: 2, c: null})");
		assertTrue(result instanceof java.util.Map);
		vm.eval("echo(`${_0}&${_1}`)", 111, 222);
	}

}
