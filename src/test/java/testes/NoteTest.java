package testes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import data.Note;

class NoteTest {

	@Test
    public void testNoteCreation() {
        Note note = new Note("Nota de teste", "Essa é uma nota de teste");
        assertEquals(0, note.getId());
        assertEquals("Nota de teste", note.getName());
        assertEquals("Essa é uma nota de teste", note.getDescription());
    }
	
	@Test
    public void testNoteUpdate() {
        Note note = new Note("Nota de teste", "Essa é uma nota de teste");
        note.setId(1);
        note.setName("Nota de teste modificada");
        note.setDescription("Essa é uma nota de teste modificada");
        assertEquals(1, note.getId());
        assertEquals("Nota de teste modificada", note.getName());
        assertEquals("Essa é uma nota de teste modificada", note.getDescription());
    }

}
