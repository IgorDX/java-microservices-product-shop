package com.microservices.product_service;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.containers.MongoDBContainer;
import io.restassured.RestAssured;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");
	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup(){
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}
	static {
		mongoDBContainer.start();
	}


	@Test
	void shouldCreateProduct() {
		String requestBody = """
		{
    "name": "iphone 15s",
    "description" : "new same iphone",
    "price" : 1000
}
		""";
		RestAssured.given()
		.contentType("application/json")
		.body(requestBody)
		.when()
		.post("/api/product")
		.then()
		.statusCode(201);
	}

}
