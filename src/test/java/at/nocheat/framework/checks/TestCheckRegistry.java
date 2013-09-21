package at.nocheat.framework.checks;

import static org.junit.Assert.fail;

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
		for (String name : Arrays.asList("Test1", "Test2", "Test3")) {
			reg.registerCheckType(name, null);
		}
	}
	
	@Test
	public void registerParents() throws NotRegisteredException, IllegalOverrideException {
		CheckTypeRegistry reg = new CheckTypeRegistry();
		final String[] names = new String[]{"Test1", "Test2", "Test3", "Test4"};
		CheckType last = null;
		String expectedName = names[0];
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			last = reg.registerCheckType(name, last);
			if (i > 0) expectedName += "." + name;
		}
		if (!last.getFullName().equals(expectedName)) {
			fail("Expect full name to be " + expectedName + " , got instead: " + last.getFullName());
		}
	}
	
	@Test(expected=NotRegisteredException.class)
	public void registerWrongParent() throws NotRegisteredException, IllegalOverrideException {
		CheckType notRegistered = new CheckTypeRegistry().registerCheckType("NotRegistered", null);
		CheckTypeRegistry reg = new CheckTypeRegistry();
		reg.registerCheckType("Test", notRegistered);
	}
	
	// TODO: More on getCheckType, getCheckTypeExact, isValid
	
}
