package de.abas.restapi.client.model;

import java.util.LinkedList;
import java.util.List;

import com.jayway.jsonpath.JsonPath;

public class CustomerList extends CommonModel {

	private CustomerList(String data) {
		this.document = JsonPath.parse(data);
	}

	public static CustomerList forData(String data) {
		return new CustomerList(data);
	}

	public List<Customer> listCustomers() {

		String basePath = "$.content.data.erpDataObjects";
		Integer length = this.document.read(basePath, List.class).size();
		LinkedList<Customer> list = new LinkedList<Customer>();
		for (int i = 0; i<length; i++) {
			list.add(Customer.forDocumentContext(this.document, basePath + "["+i+"]"));
		}

		return list;
	}
}
