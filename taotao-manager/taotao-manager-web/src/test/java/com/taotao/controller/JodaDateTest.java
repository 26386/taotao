package com.taotao.controller;

import org.joda.time.DateTime;
import org.junit.Test;

public class JodaDateTest {
	
	@Test
	public void testName() throws Exception {
		System.out.println(new DateTime().toString());
		System.out.println(new DateTime().toString("/yyyy/MM/dd"));
	}
}
