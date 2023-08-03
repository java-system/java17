package system;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VMTest {

	@Test
	void test() {
		//fail("Not yet implemented");
		VM vm = new VM();
		Object result = vm.eval("$0 + $1", 11, 22);
		vm.print(result);
		result = vm.eval("[1, 2, null]");
		assertTrue(result instanceof java.util.List);
		result = vm.eval("({a: 1, b: 2, c: null})");
		assertTrue(result instanceof java.util.Map);
	}

}
