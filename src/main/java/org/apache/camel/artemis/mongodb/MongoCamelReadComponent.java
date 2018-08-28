package org.apache.camel.artemis.mongodb;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MongoCamelReadComponent {

	private static ApplicationContext context;

	public static void main(final String[] args) {
		try {
			context = new ClassPathXmlApplicationContext(
					"application-context.xml");
			final CamelContext camelContext = context.getBean("camel-client",
					CamelContext.class);
			// To work with this collection should be capped use this command
			// db.runCommand({ convertToCapped: 'Employee', size: 8192 } )
			camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() {

					from(
							"mongodb:myDb?database=camelartemis&collection=Employee")
							.convertBodyTo(String.class).delay(1000)
							.to("stream:out");
				}
			});

			camelContext.start();

			Thread.sleep(12000);
			camelContext.stop();
		}

		catch (final Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

	}
}
