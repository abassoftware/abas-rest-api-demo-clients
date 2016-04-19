package de.abas.mw.client;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.jboss.resteasy.client.jaxrs.ProxyBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import de.abas.mw.client.model.Customer;
import de.abas.mw.client.model.CustomerList;

public class RestEasyClient {

	public static void main(String[] args) throws Exception {

		String host = "localhost";
		int port = 8080;
		final ResteasyClient client = ClientCreator.createClientWithBasicAuthentication(host, port, new UsernamePasswordCredentials("", "sy"));
		final ResteasyWebTarget target = client.target("http://" + host + ":" + port + "/mw/r/roh@vm-crest");

		final Customers customers = ProxyBuilder.builder(Customers.class, target).build();

		oneCustomer(customers);
		System.out.println("------------------------------");
		multipleCustomers(customers);
	}

	private static void oneCustomer(final Customers customers) {
		Customer customer = Customer.forData(customers.withId("70001", Customer.HEAD_FIELDS));
		System.out.println(customer);
	}

	private static void multipleCustomers(final Customers customers) {
		CustomerList customerList = CustomerList.forData(customers.customerList("such=S!S", Customer.HEAD_FIELDS));
		for (Customer customer : customerList.listCustomers()) {
			System.out.println(customer);
		}
	}

}
