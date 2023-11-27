package data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Singleton {
    private static Singleton INSTANCE;
    private List<Note> notes;
    private AtomicLong idCounter;

    private Singleton() {
        this.notes = new ArrayList<>();
        this.idCounter = new AtomicLong(1); // Inicia o contador de IDs
    }

    public static Singleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Singleton();
        }
        return INSTANCE;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void addNote(Note note) {
        long id = idCounter.getAndIncrement(); // Atribui um ID único
        note.setId(id);
        this.notes.add(note);
    }

    public void deleteNoteById(long id) {
        this.notes.removeIf(note -> note.getId() == id);
    }
}
