/**
 * 
 */
package com.csc.muthur.server.mocks;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author williamdrew
 * 
 */
public class MockModelServiceImpl {

	private static ApplicationContext	appContext;

	/**
	 * 
	 */
	public MockModelServiceImpl() {
		super();
	}

	/**
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		if (appContext == null) {
			appContext =
					new ClassPathXmlApplicationContext(
							"/META-INF/spring/bundle-context.xml");
		}
		return appContext;
	}

}
