package com.asyarifstudio.reviewservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import com.asyarifstudio.reviewservice.dto.ReviewRequest;
import com.asyarifstudio.reviewservice.dto.ReviewResponse;
import com.asyarifstudio.reviewservice.model.Review;
import com.asyarifstudio.reviewservice.repository.ReviewRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ReviewServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@Container
	static GenericContainer<?> redisContainer = new GenericContainer<>("redis:6.2").withExposedPorts(6379);

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
		dynamicPropertyRegistry.add("spring.redis.host", redisContainer::getHost);
		dynamicPropertyRegistry.add("spring.redis.port", redisContainer::getFirstMappedPort);
	}

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private com.fasterxml.jackson.databind.ObjectMapper ObjectMapper;

	@Autowired
	private ReviewRepository reviewRepository;

	@BeforeAll
	public static void setUp() {
		redisContainer.start();
		mongoDBContainer.start();
	}

	@BeforeEach
	public void BeforeEach(){
		reviewRepository.deleteAll();
	}

	@Test
	void shouldCreateReview() throws JsonProcessingException, Exception {

		// create simple review request
		ReviewRequest reviewRequest = ReviewRequest.builder()
				.productId("testId")
				.reviewScore(3)
				.build();
		// perform the request
		mockMvc.perform(MockMvcRequestBuilders
				.post("/api/review")
				.contentType(MediaType.APPLICATION_JSON)
				.content(ObjectMapper.writeValueAsString(reviewRequest))).andExpect(status().isCreated());

		// verify new product is created
		List<Review> reviews = reviewRepository.findAll();
		Assertions.assertEquals(1, reviews.size());

		Review review = reviews.get(0);
		Assertions.assertEquals("testId", review.getProductId());
		Assertions.assertEquals(3, review.getReviewScore());
	}

	@Test
	void shouldUpdateReview() throws JsonProcessingException, Exception {
		// create simple review
		Review review = Review.builder().productId("testId").reviewScore(4).build();
		Review savedReview = reviewRepository.save(review);

		// perform update
		ReviewRequest reviewRequest = ReviewRequest.builder()
				.productId("testId")
				.reviewScore(2)
				.build();
		String reviewId = savedReview.getId();
		// perform the request
		mockMvc.perform(MockMvcRequestBuilders
				.put("/api/review/"+reviewId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(ObjectMapper.writeValueAsString(reviewRequest))).andExpect(status().isOk());

		// verify product is updated
		List<Review> reviews = reviewRepository.findAll();
		Assertions.assertEquals(1, reviews.size());
		Review updatedReview = reviews.get(0);
		Assertions.assertEquals("testId", updatedReview.getProductId());
		Assertions.assertEquals(2, updatedReview.getReviewScore());	

	}

	@Test
	void shouldDeleteReview() throws Exception{
		// create simple review
		Review review = Review.builder().productId("testId").reviewScore(4).build();
		Review savedReview = reviewRepository.save(review);
		
		//perform delete
		String reviewId = savedReview.getId();
		// perform the request
		mockMvc.perform(MockMvcRequestBuilders
				.delete("/api/review/"+reviewId)
				.contentType(MediaType.APPLICATION_JSON)
				).andExpect(status().isOk());

		// verify product is dleted
		List<Review> reviews = reviewRepository.findAll();
		Assertions.assertEquals(0, reviews.size());
	}

	@Test()
	void shouldGetReviewByProductId() throws Exception{
		// create simple review
		Review review1 = Review.builder().productId("testId1").reviewScore(4).build();
		Review review2 = Review.builder().productId("testId1").reviewScore(3).build();
		Review review3 = Review.builder().productId("testId2").reviewScore(3).build();
		Review review4 = Review.builder().productId("testId2").reviewScore(5).build();
		reviewRepository.saveAll(Arrays.asList(review1,review2,review3,review4));

		// perform the request
		ResultActions action1 = mockMvc.perform(MockMvcRequestBuilders
				.get("/api/review/testId1")
				.contentType(MediaType.APPLICATION_JSON)
				).andExpect(status().isOk());

		TypeReference<List<ReviewResponse>> typeReference = new TypeReference<List<ReviewResponse>>() {};
				
		//verify product1
		List<ReviewResponse> product1Reviews = ObjectMapper.readValue(action1.andReturn().getResponse().getContentAsString(),typeReference) ;
		Assertions.assertEquals(2, product1Reviews.size());
		Assertions.assertEquals("testId1", product1Reviews.get(0).getProductId());
		Assertions.assertEquals("testId1", product1Reviews.get(1).getProductId());

		// perform the request
		ResultActions action2 = mockMvc.perform(MockMvcRequestBuilders
				.get("/api/review/testId2")
				.contentType(MediaType.APPLICATION_JSON)
				).andExpect(status().isOk());

		//verify product2
		List<ReviewResponse> product2Reviews = ObjectMapper.readValue(action2.andReturn().getResponse().getContentAsString(),typeReference) ;
		Assertions.assertEquals(2, product2Reviews.size());
		Assertions.assertEquals("testId2", product2Reviews.get(0).getProductId());
		Assertions.assertEquals("testId2", product2Reviews.get(1).getProductId());
	}

}
