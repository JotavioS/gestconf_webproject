package testes;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.restassured.RestAssured;
import server.TomcatServer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.Optional;

import org.apache.catalina.LifecycleException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NoteApiTest {
	
	private static final Optional<String> PORT = Optional.ofNullable(System.getenv("PORT"));
    private static final Optional<String> HOSTNAME = Optional.ofNullable(System.getenv("HOSTNAME"));
    private static TomcatServer tomcat;

    @BeforeAll
    public static void setup() throws LifecycleException, InterruptedException {
    	// Inicializa o Tomcat antes dos testes
        tomcat = TomcatServer.getInstance();
        tomcat.start();
        boolean status = false; 
        while(!status) {
        	status = tomcat.isReady();
        }
        
        // Configura a base URI para os testes com RestAssured
        RestAssured.baseURI = "http://" + HOSTNAME.orElse("localhost") + ":" + PORT.orElse("8080");
    }
    
    @AfterAll
    public static void finish() throws LifecycleException {
        // Para o Tomcat após os testes
        tomcat.stop();
    }

    @Test
    @Order(1)
    public void testGetAllNotes() {
        given()
                .when()
                .get("/api/notes")
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
                .post("/api/notes")
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
                .get("/api/notes/{id}", 1)
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
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
                .put("/api/notes/{id}", 1)
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
                .delete("/api/notes/{id}", 1)
                .then()
                .statusCode(200);
    }
}
