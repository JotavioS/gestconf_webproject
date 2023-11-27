package testes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import data.Note;
import data.Singleton;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SingletonTest {

private Singleton singleton;
	
	@BeforeEach
    public void setUp() {
        singleton = Singleton.getInstance();
    }
	
	@Test
	@Order(1)
    public void testAddNote() {
		Note note = new Note("Nota de teste", "Essa é uma nota de teste");
        singleton.addNote(note);
        List<Note> notes = singleton.getNotes();
        assertEquals(1, note.getId());
        assertEquals(1, notes.size());
        assertEquals(note, notes.get(0));
    }
	
	@Test
	@Order(2)
    public void testInstance() {
        List<Note> notes = singleton.getNotes();
        assertEquals(1, notes.size());
        Note oldNote = notes.get(0);
        assertEquals(1, oldNote.getId());
        assertEquals("Nota de teste", oldNote.getName());
        assertEquals("Essa é uma nota de teste", oldNote.getDescription());
        singleton.deleteNoteById(1);
        assertEquals(0, notes.size());
    }
	
	@Test
	@Order(3)
    public void testDeleteNotes() {
        Note note1 = new Note("Primeira nota de teste", "Essa é a primeira nota de teste");
        Note note2 = new Note("Segunda nota de teste", "Essa é a segunda nota de teste");

        singleton.addNote(note1);
        singleton.addNote(note2);

        List<Note> notesBeforeDeletion = singleton.getNotes();
        assertEquals(2, notesBeforeDeletion.size());

        singleton.deleteNoteById(note1.getId());
        List<Note> notesAfterDeletion = singleton.getNotes();
        assertEquals(1, notesAfterDeletion.size());
        assertEquals(note2, notesAfterDeletion.get(0));
        
        singleton.deleteNoteById(note2.getId());
        assertEquals(0, notesAfterDeletion.size());
    }

}
