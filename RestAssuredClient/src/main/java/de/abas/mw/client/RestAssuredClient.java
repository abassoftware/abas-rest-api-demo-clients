/**
 * creation date: Nov 03, 2015
 * first author: marco
 * maintained by: marco
 * <p>
 * (C) Copyright abas Software AG, Karlsruhe, Germany; 2015
 */
package de.abas.mw.client;

import static com.jayway.restassured.RestAssured.with;

import java.util.List;
import java.util.Map;

import com.jayway.restassured.response.Response;

public class RestAssuredClient {

    public static void main(String[] args) {

        final Response response = with().
                auth().basic("", "verkauf").
                parameter("criteria", "such=S!S").
                parameter("limit", "3").
                get("http://localhost:8080/mw/r/test/obj/data/0:1");
        final List<Map<String, Object>> customerList = response.then().extract().path("content.data.erpDataObjects");
        for (final Map<String, Object> customer : customerList) {
            System.out.println("--------------------------------------");
            System.out.println(customer);
        }
        //customer.entrySet().stream().filter(e -> "head".equals(e.getKey())).map(e -> e.getValue()).forEach(fields -> System.out.println(fields));
        System.out.println("\n");
        final List<List> fieldsListList = response.then().extract().path("content.data.erpDataObjects.head.fields");
        fieldsListList.forEach(fieldsList -> {
            System.out.println("--------------------------------------");
            fieldsList.forEach(field -> {
                System.out.println(field);
            });
        });

    }
}
