package com.marcus.springboot;

import com.marcus.springboot.entity.Book;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class},webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SpringbootApplicationTests {

	private static final String API_ROOT = "http://localhost:8081/api/books";

	@Test
	public void contextLoads() {
	}

	private Book createRamdonBook(){
		Book book = new Book();
		book.setTitle("Norway Tour Guide");
		book.setAuthor("Yamaroshi");
		return book;
	}

	private String createBookAsUri(Book book) {
		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(book)
				.post(API_ROOT);
		return API_ROOT+"/"+response.jsonPath().get("id");
	}

	@Test
	public void whenGetAllBooks_thenOK(){
		Book book = createRamdonBook();
		String bookUri = createBookAsUri(book);
		Response response = RestAssured.get(API_ROOT+"/title/"+book.getTitle());
		assertEquals(HttpStatus.OK.value(),response.getStatusCode());
		assertTrue(response.as(List.class).size()>0);
	}

	@Test
	public void whenGetCreatedBookById_thenOK(){
		Book book = createRamdonBook();
		String location = createBookAsUri(book);
		Response response = RestAssured.get(location);
		assertEquals(HttpStatus.OK.value(),response.getStatusCode());
		assertEquals(book.getTitle(),response.jsonPath().get("title"));
	}

	@Test
	public void whenGetNotExistBookById_thenNotFound(){
		Response response = RestAssured.get(API_ROOT+"/"+"SDFDSFS");
		assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatusCode());
	}

	@Test
	public void whenCreateNewBook_thenCreate(){
		Book book = createRamdonBook();
		String location = createBookAsUri(book);
		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(book)
				.post(API_ROOT);
		assertEquals(HttpStatus.CREATED.value(),response.getStatusCode());
	}

	@Test
	public void whenInvalidBook_thenError(){
		Book book = createRamdonBook();
		book.setAuthor(null);
		String location = createBookAsUri(book);
		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(book)
				.post(API_ROOT);
		assertEquals(HttpStatus.CREATED.value(),response.getStatusCode());
	}

	@Test
	public void whenUpdateCreatedBook_thenUpdated(){
		Book book = createRamdonBook();
		String location = createBookAsUri(book);
		book.setId(Long.parseLong((location.split("/api/books/")[1])));
		book.setAuthor("newAuthor");
		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(book)
				.put(location);
		assertEquals(HttpStatus.OK.value(),response.getStatusCode());
		assertEquals("new Author",response.jsonPath().get("author"));
	}

	@Test
	public void whenDeleteCreatedBook_thenOk(){
		Book book = createRamdonBook();
		String location = createBookAsUri(book);
		Response response = RestAssured.delete(location);
		assertEquals(HttpStatus.OK.value(),response.getStatusCode());
		response = RestAssured.get(location);
		assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatusCode());
	}
}
