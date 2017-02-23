package de.abas.restapi.client.model;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class Customer extends CommonModel {

	public final static String HEAD_FIELDS = "nummer,such,name,saldo";

	private String basePath = "$.content.data.";

	private Customer(String data) {
		this.document = JsonPath.parse(data);
	}

	private Customer(DocumentContext document, String basePath) {
		this.document = document;
		this.basePath = basePath;
	}

	public static Customer forData(String data) {
		return new Customer(data);
	}

	static Customer forDocumentContext(DocumentContext document, String basePath) {
		return new Customer(document, basePath);
	}

	public String getNummer() {
		return this.document.read(basePath + "head.fields.nummer.value", String.class);
	}

	public String getSwd() {
		return this.document.read(basePath + "head.fields.such.value", String.class);
	}

	public String getName() {
		return this.document.read(basePath + "head.fields.name.value", String.class);
	}

	public Double getSaldo() {
		return this.document.read(basePath + "head.fields.saldo.value", Double.class);
	}

	@Override
	public String toString() {
		return getNummer() + " (" + getSwd() + ") " + getName() + ": " + getSaldo();
	}

}
