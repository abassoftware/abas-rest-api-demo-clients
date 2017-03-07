package de.abas.restapi.client;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.jboss.resteasy.client.jaxrs.ProxyBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import de.abas.restapi.client.filters.EdpLoggingFilter;
import de.abas.restapi.client.filters.LanguageFilter;
import de.abas.restapi.client.model.Customer;
import de.abas.restapi.client.model.CustomerList;
import de.abas.restapi.client.model.ProductPart;
import de.abas.restapi.client.model.ProductPartList;
import de.abas.restapi.client.rest.Customers;
import de.abas.restapi.client.rest.ProductParts;

public class RestEasyClient {
	private static final boolean EDP_LOGGING_ON = false;
	private static ResteasyClient client;
	
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();

		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(ConnectionData.BASIC_AUTH_USERNAME, ConnectionData.BASIC_AUTH_PASSWORD);
		client = ClientCreator.createClientWithBasicAuthentication(ConnectionData.HOST, ConnectionData.PORT, credentials);
		ResteasyWebTarget target = client.target("http://" + ConnectionData.HOST + ":" + ConnectionData.PORT + ConnectionData.BASE);
		
		addFilterForEdpLogging();
		addFilterForLanguage();
		
		final Customers customers = ProxyBuilder.builder(Customers.class, target).build();
		final ProductParts parts = ProxyBuilder.builder(ProductParts.class, target).build();

		oneCustomer(customers);
		System.out.println("------------------------------------------------------------");
		multipleCustomers(customers);
		System.out.println("############################################################");
		onePart(parts);
		System.out.println("------------------------------------------------------------");
		multipleParts(parts);

		long end = System.currentTimeMillis();
		System.out.println("Run time in millis: " + (end - start));
	}

	private static void addFilterForLanguage() {
		LanguageFilter languageFilter = new LanguageFilter();
		client.register(languageFilter);
		languageFilter.setValue(I18N.LANG);
	}

	private static void addFilterForEdpLogging() {
		if (EDP_LOGGING_ON) {
			EdpLoggingFilter filter = new EdpLoggingFilter();
			client.register(filter);
			filter.setValue("INFO");
		}
	}

	private static void oneCustomer(final Customers customers) {
		String data = customers.withId("70001", Customer.headerFields(), I18N.LANG);
		Customer customer = Customer.forData(data);
		System.out.println(customer);
	}

	private static void multipleCustomers(final Customers customers) {
		String criteria = I18N.LANG.equalsIgnoreCase("DE") ? "such=S!S" : "swd=S!S";
		String list = customers.customerList(criteria, Customer.headerFields(), I18N.LANG);
		// System.out.println(">>> " + list);
		CustomerList customerList = CustomerList.forData(list);
		for (Customer customer : customerList.listCustomers()) {
			System.out.println(customer);
		}
	}

	private static void onePart(final ProductParts parts) {
		String data = parts.withId("10000", ProductPart.headerFields(), I18N.LANG);
		ProductPart part = ProductPart.forData(data);
		System.out.println(part);
	}
	
	private static void multipleParts(final ProductParts parts) {
		String criteria = I18N.LANG.equalsIgnoreCase("DE") ? "such=TI!TI" : "swd=TI!TI";
		String list = parts.partList(criteria, ProductPart.headerFields(), I18N.LANG);
		// System.out.println(">>> " + list);
		ProductPartList partList = ProductPartList.forData(list);
		for (ProductPart part : partList.listParts()) {
			System.out.println(part);
		}
	}
	
}
