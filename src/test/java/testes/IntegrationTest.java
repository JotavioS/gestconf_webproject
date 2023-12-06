package testes;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import data.Singleton;
import io.restassured.RestAssured;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationTest {
	
	private static final Optional<String> PORT = Optional.ofNullable(System.getenv("PORT"));
    private static final Optional<String> HOSTNAME = Optional.ofNullable(System.getenv("HOSTNAME"));

    @BeforeAll
    public static void setup() {
        // Configura a base URI para os testes com RestAssured
        RestAssured.baseURI = "http://" + HOSTNAME.orElse("localhost") + ":" + PORT.orElse("8080");
    }
    
    @Test
    @Order(1)
    public void testGetAllNotes() {
        given()
                .when()
                .get("/notes")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    @Order(2)
    public void testCreateNote() {
        // Substitua o conteúdo abaixo pelos dados reais da nota que você deseja criar
        String requestBody = "{ \"name\": \"Test Note\", \"description\": \"Test Description\" }";

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/notes")
                .then()
                .statusCode(201)
                .body("name", equalTo("Test Note"))
                .body("description", equalTo("Test Description"));
    }

    @Test
    @Order(3)
    public void testGetNoteById() {
        given()
                .when()
                .get("/notes/{id}", 3)
                .then()
                .statusCode(200)
                .body("id", equalTo(3));
    }
    
    

    @Test
    @Order(4)
    public void testUpdateNote() {
        // Substitua o conteúdo abaixo pelos dados reais da nota que você deseja
        // atualizar
        String requestBody = "{ \"name\": \"Updated Note\", \"description\": \"Updated Description\" }";

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/notes/{id}", 3)
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Note"))
                .body("description", equalTo("Updated Description"));
    }

    @Test
    @Order(5)
    public void testDeleteNote() {
        given()
                .when()
                .delete("/notes/{id}", 3)
                .then()
                .statusCode(200);
    }
    
    @Test
    @Order(6)
    public void testGetNoteByIdNotFind() {
        given()
                .when()
                .get("/notes/{id}", 4)
                .then()
                .statusCode(404);
    }
    
    @Test
    @Order(7)
    public void testGetNoteByIdFail() {
        given()
                .when()
                .get("/notes/{id}", "%sdft")
                .then()
                .statusCode(400);
    }
    
    @Test
    @Order(8)
    public void testUpdateNoteNotFind() {
        given()
                .when()
                .put("/notes/{id}", 4)
                .then()
                .statusCode(404);
    }
    
    @Test
    @Order(9)
    public void testUpdateNoteNoID() {
        given()
                .when()
                .put("/notes/")
                .then()
                .statusCode(400);
    }
    
    @Test
    @Order(10)
    public void testUpdateNoteFail() {
        given()
                .when()
                .put("/notes/{id}", "%sdft")
                .then()
                .statusCode(400);
    }
}
