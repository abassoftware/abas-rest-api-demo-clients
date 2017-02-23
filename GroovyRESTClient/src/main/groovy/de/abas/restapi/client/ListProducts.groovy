package de.abas.restapi.client

import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient;

public class ListProducts {

    public static void main(String[] args) {
		RESTClient restClient = Utils.createClient()

		HttpResponseDecorator response = restClient.get(
			path:"obj/data/2:1",
			query:[
				criteria:"such=M!N",
				headFields:"nummer,such,sucherw",
				limit:1000])
		response.data.content.data.erpDataObjects.each { product ->
			println "${product.head.fields.nummer.value} ${product.head.fields.such.value} ${product.head.fields.sucherw.value}"
			println "\tRows: ${product.meta.rowCount} "
			println "\tLocation: ${product.meta.link.href} "
		}

	}
}
