package de.abas.mw.client

import groovy.json.JsonOutput
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient

class CreateOpportunity {
	public static void main(String[] args) {
		RESTClient restClient = Utils.createClient()

		def editorLocation = createEditor(restClient)
		try {
			fillFields(restClient, editorLocation)
			def links = commitEditor(restClient, editorLocation)
			def latestVersionLocation = links.find { it.rel == "latest-version" }.href
			closeWorkingSet(restClient, links.find { it.rel == "urn:abas:rel:workingSet:CLOSE" }.href)

			println ""
			println "Location: ${latestVersionLocation}"
		} catch (Exception e) {
			cancelWorkingSetOfEditor(restClient, editorLocation)
			throw e
		}
	}

	private static String createEditor(RESTClient restClient) {
		HttpResponseDecorator response = restClient.post(
				path:"obj/data/3:30/commands/NEW",
				query:[
					filterHeadFields:"kunde,reempf,warenempf,betreuer,tterm,sumnetto,betreff,gltvon,gltbis,audatum,wahrsch",
					filterTableFields:"artikel,tename,mge,he,preis,pwert"],
				body:null
				)
		return response.headers."Location"
	}

	private static void fillFields(RESTClient restClient, String editorLocation) {
		HttpResponseDecorator response = restClient.post(
				path:editorLocation,
				query:[:],
				contentType:"application/json",
				body:[ actions: [
						[ _type: "SetFieldValue", fieldName: "kunde", value: "70001"],
						[ _type: "SetFieldValue", fieldName: "tterm", value: "+14"],
						[ _type: "InsertRow", rowSpec: "0" ],
						[ _type: "SetFieldValue",rowSpec: ".", fieldName: "artex", value: "30010"],
						[ _type: "SetFieldValue",rowSpec: ".", fieldName: "mgex", value: "2"],
					]
				]
				)
		println "positions:"
		response.data.content.data.table.each { row ->
			println "${row.fields.mge.value} ${row.fields.he.text} ${row.fields.tename.value}\tprice: ${row.fields.pwert.value}"
		}
	}

	private static List commitEditor(RESTClient restClient, String editorLocation) {
		HttpResponseDecorator response = restClient.post(
				path:editorLocation,
				query:[:],
				contentType:"application/json",
				body:[ actions: [
						[ _type: "Commit"]
					]
				]
				)
		return response.data.content.data.links
	}

	private static void closeWorkingSet(RESTClient restClient, String href) {
		restClient.post(path: href)
	}

	private static void cancelWorkingSetOfEditor(RESTClient restClient,String editorLocation) {
		def editorLinks = restClient.get(path:editorLocation).data.content.data.links
		def workingSetLocation = editorLinks.find {it.rel == "up"}.href
		def workingSetLinks = restClient.get(path:workingSetLocation ).data.content.data.links
		def cancelLocation = workingSetLinks.find { it.rel == "urn:abas:rel:workingSet:CANCEL" }.href
		restClient.post(path:cancelLocation)
	}

}
