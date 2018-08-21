package org.apache.camel.artemis.multicast;

import javax.jms.ConnectionFactory;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class TopicCamelComponent {

	public static void main(final String[] args) {
		try {

			final CamelContext context = new DefaultCamelContext();

			final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					"tcp://localhost:61616");

			context.addComponent("artemis-jms",
					JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

			context.addRoutes(new RouteBuilder() {
				public void configure() {
					from("artemis-jms:topic:exampleTopic").routeId("a").to(
							"direct:a");

					from("artemis-jms:topic:exampleTopic").routeId("b").to(
							"direct:b");

					from("direct:a")
							.transform(simple("Subscriber-1 output: ${body}"))
							.delay(1000).to("stream:out");
					from("direct:b")
							.transform(simple("Subscriber-2 output: ${body}"))
							.delay(2000).to("stream:out");

				}
			});

			final ProducerTemplate template = context.createProducerTemplate();
			context.start();
			for (int i = 0; i < 10; i++) {
				System.out.println("Sending \"" + "Test Message: " + i + "\"");
				template.sendBody("artemis-jms:topic:exampleTopic",
						"Test Message: " + i);
			}

			Thread.sleep(10000);
			context.stop();
		}

		catch (final Exception e) {

			System.out.println(e.toString());

			e.printStackTrace();

		}

	}
}
