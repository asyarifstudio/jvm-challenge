package com.asyarifstudio.productservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

import com.asyarifstudio.productservice.dto.ProductResponse;
import com.asyarifstudio.productservice.dto.ReviewResponse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	@Autowired
	private com.fasterxml.jackson.databind.ObjectMapper ObjectMapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WebClient.Builder webClientBuilder;

	@Test
	void shouldGetProductById() throws Exception {
		 // Create a sample response from the review service
		 List<ReviewResponse> reviews = new ArrayList<ReviewResponse>();
		 ReviewResponse review1 = ReviewResponse.builder().id("1").productId("product1").reviewScore(4).build();
		 ReviewResponse review2 = ReviewResponse.builder().id("2").productId("product1").reviewScore(3).build();
		 reviews.add(review1);
		 reviews.add(review2);
		 String reviewStrings = ObjectMapper.writeValueAsString(reviews);
		 ClientResponse reviewServiceResponse = ClientResponse.create(HttpStatus.OK)
				 .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				 .body(reviewStrings)
				 .build();

		// Set up mock response for the review service
		WebClient mockWebClient = Mockito.mock(WebClient.class);
		WebClient.RequestHeadersUriSpec mockUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
		WebClient.ResponseSpec mockResponseSpec = Mockito.mock(WebClient.ResponseSpec.class);
		Mockito.when(webClientBuilder.build()).thenReturn(mockWebClient);
		Mockito.when(mockWebClient.get()).thenReturn(mockUriSpec);
		Mockito.when(mockUriSpec.uri(Mockito.anyString())).thenReturn(mockUriSpec);
		Mockito.when(mockUriSpec.retrieve()).thenReturn(mockResponseSpec);
		Mockito.when(mockResponseSpec.bodyToMono(ReviewResponse[].class)).thenReturn(Mono.just(reviews.toArray(new ReviewResponse[reviews.size()])));

		// Perform the request to the endpoint
        ResultActions action1 = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/product/product1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

		ProductResponse productResponse = ObjectMapper.readValue(action1.andReturn().getResponse().getContentAsString(),ProductResponse.class) ;
		Assertions.assertEquals("product1", productResponse.getId());
		Assertions.assertEquals(2, productResponse.getNumberOfRevierws());
		Assertions.assertEquals(3.5, productResponse.getAverageReviewScore());
	}

}
