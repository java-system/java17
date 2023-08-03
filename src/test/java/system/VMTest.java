package system;

import org.junit.jupiter.api.Test;

class VMTest {

	@Test
	void test() {
		//fail("Not yet implemented");
		VM vm = new VM();
		Object result = vm.js("$0 + $1", 11, 22);
		vm.print(result);
	}

}
