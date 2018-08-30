package org.apache.camel.artemis.mongodb;

import java.util.List;

import javax.jms.ConnectionFactory;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class MongoCamelWriteComponentCDI {

	public static void main(final String[] args) {
		try {
	        try {

	            final CamelContext context = new DefaultCamelContext();

	            final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

	            context.addComponent("test-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

	            System.out.println(context.getName());
	            final List<Route> routes = context.getRoutes();
	            routes.forEach(r -> System.out.println(r));

	            System.out.println(context.getRoutes());

	            context.addRoutes(new RouteBuilder() {
	                @Override
	                public void configure() {
	                    from("test-jms:queue:exampleQueue").delay(1000).to("file://C://Users//Sharmila//Desktop//test").to("stream:out");
	                }
	            });

	            final ProducerTemplate template = context.createProducerTemplate();
	            context.start();
	            for (int i = 0; i < 10; i++) {
	            	System.out.println("Sending \""+"Test Message: " + i+"\"");
	                template.sendBody("test-jms:queue:exampleQueue", "Test Message: " + i);
	            }
	            
	            Thread.sleep(10000);
	            context.stop();
	        }

	        catch (final Exception e) {

	            System.out.println(e.toString());

	            e.printStackTrace();

	        }

	    }

		catch (final Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

	}
}
