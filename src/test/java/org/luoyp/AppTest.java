package org.luoyp;

import junit.framework.Assert;
import org.junit.Test;

public class AppTest
{
	@Test
	public void aTest()
	{
		App app = new App();
		assert "Hello World!".equals(app.a());
	}
}
