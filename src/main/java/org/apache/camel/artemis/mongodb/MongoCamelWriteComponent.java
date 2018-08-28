package org.apache.camel.artemis.mongodb;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MongoCamelWriteComponent {

	private static ApplicationContext context;

	public static void main(final String[] args) {
		try {
			context = new ClassPathXmlApplicationContext(
					"application-context.xml");
			final CamelContext camelContext = context.getBean("camel-client",
					CamelContext.class);

			camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() {
					from("jms:queue:exampleQueue")
							.delay(1000)
							//.to("file://C://Users//Sharmila//Desktop//test")
							.to("stream:out")
							.to("mongodb:myDb?database=camelartemis&collection=Employee&operation=insert");
				}
			});

			ProducerTemplate template = context.getBean("camelTemplate",
					ProducerTemplate.class);
			camelContext.start();
			for (int i = 0; i < 10; i++) {
				System.out.println("Sending \"" + "Test Message: " + i + "\"");
				template.sendBody("jms:queue:exampleQueue", "{\"name\":\"Test"
						+ i + "\"}");
			}

			Thread.sleep(10000);
			camelContext.stop();
		}

		catch (final Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

	}
}
