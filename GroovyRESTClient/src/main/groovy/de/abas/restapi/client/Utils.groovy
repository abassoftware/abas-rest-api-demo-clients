package de.abas.restapi.client

import groovy.json.JsonOutput
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient

class Utils {

	public static RESTClient createClient() {
		RESTClient restClient = new RESTClient(System.getProperty("baseUrl", "http://localhost:8080/mw/r/roh@vm-crest/"))
		restClient.auth.basic("", System.getProperty("password", "sy"))
		restClient.encoder.'application/abas.objects+json' = restClient.encoder.'application/json'
		restClient.parser.'application/abas.objects+json' = restClient.parser.'application/json'
		restClient.headers['Accept-Language'] = 'de'
		if (Boolean.getBoolean("edpLog")) {
			restClient.headers['X-abas-mw-edp-log-level'] = "INFO"
		}
		restClient.headers['Accept'] = 'application/abas.objects+json'
		restClient.handler.failure =  { HttpResponseDecorator response,data ->
			System.err.println JsonOutput.prettyPrint(JsonOutput.toJson(data));
			restClient.defaultFailureHandler(response, data)
		}
		return restClient
	}

	public static String formatJson(jsonData) {
		return JsonOutput.prettyPrint(JsonOutput.toJson(jsonData));
	}
}
