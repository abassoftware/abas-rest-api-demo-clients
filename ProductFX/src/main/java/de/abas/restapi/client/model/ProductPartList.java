package de.abas.restapi.client.model;

import java.util.LinkedList;
import java.util.List;

import com.jayway.jsonpath.JsonPath;

public class ProductPartList extends CommonModel {

	private ProductPartList(String data) {
		this.document = JsonPath.parse(data);
	}

	public static ProductPartList forData(String data) {
		return new ProductPartList(data);
	}

	public List<ProductPart> listParts() {

		String basePath = "$.content.data.erpDataObjects";
		Integer length = this.document.read(basePath, List.class).size();
		LinkedList<ProductPart> list = new LinkedList<ProductPart>();
		for (int i = 0; i < length; i++) {
			list.add(ProductPart.forDocumentContext(this.document, basePath + "[" + i + "]"));
		}

		return list;
	}
}
