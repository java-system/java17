package system;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VMTest {

	@Test
	void test() throws Exception {
		//fail("Not yet implemented");
		VM vm = new VM();
		Object result = vm.eval("$0 + $1", 11, 22);
		vm.echo(result);
		result = vm.eval("[1, 2, null]");
		assertTrue(result instanceof java.util.List);
		for (int i=0; i<vm.evalAsInt("$0.length", result); i++) {
			vm.eval("vm.echo($0[$1])", result, i);
		}
		result = vm.eval("({a: 1, b: 2, c: null})");
		assertTrue(result instanceof java.util.Map);
	}

}
