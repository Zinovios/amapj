package fr.amapj.view.engine.widgets;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestCurrencyTextFieldConverter
{

	@Test
	public void test1()
	{
		CurrencyTextFieldConverter c = new CurrencyTextFieldConverter();
		
		
		assertIntEqual(0, c.convertToCurrency(""));
		assertIntEqual(0, c.convertToCurrency("0"));
		assertIntEqual(12300, c.convertToCurrency("123"));
		assertIntEqual(12300, c.convertToCurrency("123."));
		assertIntEqual(12310, c.convertToCurrency("123.1"));
		assertIntEqual(12312, c.convertToCurrency("123.12"));
		assertNull(c.convertToCurrency("a123"));
		assertNull(c.convertToCurrency("-123"));
		assertNull(c.convertToCurrency("-123.1"));
		assertNull(c.convertToCurrency("-123.12"));
		assertNull(c.convertToCurrency("123.123"));
	}
	
	
	
	@Test
	public void test2()
	{
		CurrencyTextFieldConverter c = new CurrencyTextFieldConverter();
		
		
		assertEquals("0.00", c.convertToString(new Integer(0)));
		assertEquals("123.00", c.convertToString(new Integer(12300)));
		assertEquals("123.12", c.convertToString(new Integer(12312)));
		assertEquals("123.02", c.convertToString(new Integer(12302)));
		
	}
	
	
	
	
	
	private void assertIntEqual(int expected, Integer actual)
	{
		if (actual==null) 
		{
			fail("expected ="+expected+" . actual="+actual);
		}
		
		assertEquals(expected, actual.intValue());
	}

}
