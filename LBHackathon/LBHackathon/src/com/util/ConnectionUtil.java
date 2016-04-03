package com.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ConnectionUtil {
	private static SessionFactory factory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			System.out.println("Building new SessionFactory !!");
			factory = new Configuration().configure().buildSessionFactory();

			

		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			//System.out.println("Initial SessionFactory creation failed." + ex);
			ex.printStackTrace();
			//throw new ExceptionInInitializerError(ex);
		}
		return factory;
	}

	public static SessionFactory getSessionFactory() {
		return factory;
	}

	public static void shutdown() {
		// Close caches and connection pools
		getSessionFactory().close();
	}

}
