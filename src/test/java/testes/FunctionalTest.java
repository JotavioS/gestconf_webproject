package testes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SeleniumTestIT {
	private static final Optional<String> PORT = Optional.ofNullable(System.getenv("PORT"));
    private static final Optional<String> HOSTNAME = Optional.ofNullable(System.getenv("HOSTNAME"));
    private static WebDriver driver;

    @BeforeAll
    public static void setup() {
        // Configurar o WebDriver, por exemplo, para o Chrome
        System.setProperty("webdriver.gecko.driver", "C:\\geckodriver.exe");
        driver = new FirefoxDriver();
        driver.get("http://" + HOSTNAME.orElse("localhost") + ":" + PORT.orElse("8080"));
    }

    @AfterAll
    public static void finish() {
        driver.quit();
    }

    @Test
    @Order(1)
    public void testAddNote() {
        // Clicar no botão Nova Nota
        WebElement addButton = driver.findElement(By.id("addNoteButton"));
        addButton.click();

        // Preencher o formulário de adição de nota
        WebElement noteNameInput = driver.findElement(By.id("noteName"));
        noteNameInput.sendKeys("Nova Nota de Teste");

        WebElement noteDescriptionInput = driver.findElement(By.id("noteDescription"));
        noteDescriptionInput.sendKeys("Descrição da Nova Nota de Teste");

        // Clicar no botão Salvar
        WebElement saveButton = driver.findElement(By.cssSelector("#modal .button1"));
        saveButton.click();

        // Verificar se a nota foi adicionada com sucesso
        WebElement noteList = driver.findElement(By.id("noteList"));
        WebElement lastNote = noteList.findElements(By.tagName("span")).get(0);
        assertEquals("Nova Nota de Teste", lastNote.getText());
    }

    @Test
    @Order(2)
    public void testDeleteNote() {
        // Adicionar uma nota para deletar
        testAddNote();

        // Clicar no botão Excluir
        WebElement deleteButton = driver.findElement(By.cssSelector("#noteList li .button3"));
        deleteButton.click();

        // Confirmar a exclusão
        WebElement confirmButton = driver.findElement(By.cssSelector("#deleteConfirmation .button1"));
        confirmButton.click();

        // Verificar se a nota foi excluída
        WebElement noteList = driver.findElement(By.id("noteList"));
        assertEquals(1, noteList.findElements(By.tagName("li")).size());
    }

}
