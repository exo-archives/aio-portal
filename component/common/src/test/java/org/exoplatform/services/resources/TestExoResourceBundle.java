package org.exoplatform.services.resources;

import junit.framework.TestCase;

public class TestExoResourceBundle extends TestCase {

	public void testConstructor() {
		ExoResourceBundle bundle; 
		bundle = new ExoResourceBundle("key1=value");
		assertEquals(1, bundle.getContents().length);
		assertEquals("value", bundle.getString("key1"));
		bundle = new ExoResourceBundle("key1=value\nkey2=value");
		assertEquals(2, bundle.getContents().length);
		assertEquals("value", bundle.getString("key1"));
		assertEquals("value", bundle.getString("key2"));
		bundle = new ExoResourceBundle("key1=value\r\nkey2=value");
		assertEquals(2, bundle.getContents().length);
		assertEquals("value", bundle.getString("key1"));
		assertEquals("value", bundle.getString("key2"));
		bundle = new ExoResourceBundle("#comment\r\nkey2=value");
		assertEquals(1, bundle.getContents().length);
		assertEquals("value", bundle.getString("key2"));
		bundle = new ExoResourceBundle("  #comment\r\nkey2=value");
		assertEquals(1, bundle.getContents().length);
		assertEquals("value", bundle.getString("key2"));
		bundle = new ExoResourceBundle("  bad entry\r\nkey2=value");
		assertEquals(1, bundle.getContents().length);
		assertEquals("value", bundle.getString("key2"));
		bundle = new ExoResourceBundle("#key1 =value\r\nkey2=value");
		assertEquals(1, bundle.getContents().length);
		assertEquals("value", bundle.getString("key2"));
		bundle = new ExoResourceBundle(" key1 =value\r\n key2 =value");
		assertEquals(2, bundle.getContents().length);
		assertEquals("value", bundle.getString(" key1 "));
		assertEquals("value", bundle.getString(" key2 "));
	}
	
	public void testUnicode2Char() {
		for (int i = 0; i < (1 << 16); i++) {
			String value = Integer.toHexString(i);
			while (value.length() < 4) {
				value = "0" + value;
			}
			assertEquals((char) i, ExoResourceBundle.unicode2Char("\\u" + value));			
		}
	}
	
	public void testConvert() {
		assertEquals("Normal Value", ExoResourceBundle.convert("Normal Value"));
		assertEquals("\u00E9\u00E7\u00E0\u00F9\u0194\u0BF5", ExoResourceBundle.convert("\\u00E9\\u00E7\\u00E0\\u00F9\\u0194\\u0BF5"));
		assertEquals("before \u00E9\u00E7\u00E0\u00F9\u0194\u0BF5", ExoResourceBundle.convert("before \\u00E9\\u00E7\\u00E0\\u00F9\\u0194\\u0BF5"));
		assertEquals("\u00E9\u00E7\u00E0\u00F9\u0194\u0BF5 after", ExoResourceBundle.convert("\\u00E9\\u00E7\\u00E0\\u00F9\\u0194\\u0BF5 after"));
		assertEquals("before \u00E9\u00E7\u00E0 between \u00F9\u0194\u0BF5 after", ExoResourceBundle.convert("before \\u00E9\\u00E7\\u00E0 between \\u00F9\\u0194\\u0BF5 after"));
	}
}
