package at.nocheat.framework.checks;

import java.util.Arrays;

import org.junit.Test;

import at.nocheat.framework.exceptions.IllegalOverrideException;
import at.nocheat.framework.exceptions.NotRegisteredException;

public class TestCheckRegistry {
	
	@Test(expected=IllegalOverrideException.class)
	public void registerCamelTwice() throws NotRegisteredException, IllegalOverrideException {
		CheckTypeRegistry reg = new CheckTypeRegistry();
		reg.registerCheckType("Test", null);
		reg.registerCheckType("Test", null);
	}
	
	@Test(expected=IllegalOverrideException.class)
	public void registerLowerTwice() throws NotRegisteredException, IllegalOverrideException {
		CheckTypeRegistry reg = new CheckTypeRegistry();
		reg.registerCheckType("test", null);
		reg.registerCheckType("test", null);
	}
	
	@Test(expected=IllegalOverrideException.class)
	public void registerVaryingCaseTwice() throws NotRegisteredException, IllegalOverrideException {
		CheckTypeRegistry reg = new CheckTypeRegistry();
		reg.registerCheckType("test", null);
		reg.registerCheckType("Test", null);
	}
	
	@Test
	public void registerRootTypes() throws NotRegisteredException, IllegalOverrideException {
		CheckTypeRegistry reg = new CheckTypeRegistry();
		for (String name : Arrays.asList("Test", "Test2", "Test3")) {
			reg.registerCheckType(name, null);
		}
	}
	
}
