package de.abas.restapi.client.model;

import java.util.Arrays;
import java.util.List;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import de.abas.restapi.client.I18N;

public class Customer extends CommonModel {

	public final static String[] HEAD_FIELDS_DE = new String[] { "nummer", "such", "namebspr", "saldo" };
	public final static String[] HEAD_FIELDS_EN = new String[] { "idno", "swd", "descrOperLang", "bal" };

	private static final List<String> ENUMS = Arrays.asList("");
	
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

	public String get(String fieldName) {
		String fieldValue = "";

		try {
			if (ENUMS.contains(fieldName)) {
				fieldValue = this.document.read(basePath + "head.fields." + fieldName + ".text", String.class);
			} else {
				fieldValue = this.document.read(basePath + "head.fields." + fieldName + ".value", String.class);
			}
		} catch (Exception e) {
			System.out.println("Could not find field: " + fieldName);
		}

		return fieldValue;
	}

	public static String headerFields() {
		StringBuffer headerFields = new StringBuffer();
		String[] fields = HEAD_FIELDS_EN;
		if (I18N.LANG.equalsIgnoreCase("DE")) {
			fields = HEAD_FIELDS_DE;
		}

		boolean first = true;
		for (int i = 0; i < fields.length; i++) {
			if (first) {
				headerFields.append(fields[i]);
				first = false;
			}
			headerFields.append(',');
			headerFields.append(fields[i]);
		}

		return headerFields.toString();
	}

	@Override
	public String toString() {
		StringBuffer values = new StringBuffer();
		if (I18N.LANG.equalsIgnoreCase("DE")) {
			for (int i = 0; i < HEAD_FIELDS_DE.length; i++) {
				values.append(get(HEAD_FIELDS_DE[i]));
				values.append(" - ");
			}
		} else {
			for (int i = 0; i < HEAD_FIELDS_EN.length; i++) {
				values.append(get(HEAD_FIELDS_EN[i]));
				values.append(" - ");
			}

		}

		return values.toString();
	}

}
