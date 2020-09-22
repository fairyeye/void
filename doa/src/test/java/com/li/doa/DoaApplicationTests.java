package com.li.doa;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class DoaApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void createMockObject(){

//		ArrayList mockArrayList = mock(ArrayList.class);
//		Assert.assertTrue(mockArrayList instanceof List);

		List mockList = mock(List.class);
		Assert.assertTrue(mockList instanceof List);

		when(mockList.add("one")).thenReturn(true);
		when(mockList.size()).thenReturn(1);

		// 调用 mockList.add("one") 返回结果为 true
		Assert.assertTrue(mockList.add("one")); // -> true

		// 调用 mockList.add("two") 返回为默认结果 false
		Assert.assertTrue(mockList.add("two")); // -> false

		// 调用 mockList.size() 返回结果为 1
		Assert.assertEquals(mockList.size(), 1); // -> true

	}
}
