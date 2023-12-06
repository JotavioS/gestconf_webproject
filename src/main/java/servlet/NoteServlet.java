package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import data.Note;
import data.Singleton;

@WebServlet("/notes/*")
public class NoteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Singleton singleton = Singleton.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=" + "UTF-8");
        // Obtém o caminho da URL após "/notes/"
        String pathInfo = request.getPathInfo();
        
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                // Extrai o ID da parte final do caminho
                long id = Long.parseLong(pathInfo.substring(1));

                // Procura a nota com o ID fornecido
                Note foundNote = findNoteById(id);

                if (foundNote != null) {
                    // Se encontrou a nota, retorna apenas ela
                    PrintWriter writer = response.getWriter();
                    writer.println(new Gson().toJson(foundNote));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().println("Nota não encontrada!");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Formato de ID inválido");
            }
        } else {
            // Se nenhum "id" foi fornecido, retorna a lista completa de notas
            PrintWriter writer = response.getWriter();
            writer.println(new Gson().toJson(singleton.getNotes()));
        }
    }

    // Método auxiliar para encontrar uma nota pelo ID
    private Note findNoteById(long id) {
        List<Note> notes = singleton.getNotes();
        for (Note note : notes) {
            if (note.getId() == id) {
                return note;
            }
        }
        return null;
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=" + "UTF-8");
        try {
            // Lê o JSON do corpo da solicitação
            StringBuilder requestBody = new StringBuilder();
            request.getReader().lines().forEach(requestBody::append);

            // Converte o JSON para um objeto Note usando Gson
            Gson gson = new Gson();
            Note newNote = gson.fromJson(requestBody.toString(), Note.class);

            // Adiciona a nova nota à lista
            singleton.addNote(newNote);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().println(new Gson().toJson(newNote));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Error parsing JSON: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=" + "UTF-8");
        // Obtém o caminho da URL após "/notes/"
        String pathInfo = request.getPathInfo();
        
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                // Extrai o ID da parte final do caminho
                long id = Long.parseLong(pathInfo.substring(1));

                // Lê o JSON do corpo da solicitação
                StringBuilder requestBody = new StringBuilder();
                request.getReader().lines().forEach(requestBody::append);

                // Converte o JSON para um objeto Note usando Gson
                Gson gson = new Gson();
                Note updatedNote = gson.fromJson(requestBody.toString(), Note.class);

                // Atualiza a nota existente com base no ID
                List<Note> notes = singleton.getNotes();
                for (Note note : notes) {
                    if (note.getId() == id) {
                        note.setName(updatedNote.getName());
                        note.setDescription(updatedNote.getDescription());
                        response.getWriter().println(new Gson().toJson(note));
                        return;
                    }
                }

                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("Nota não encontrada!");
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Formato de ID inválido");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("ID não fornecido na URL");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Obtém o caminho da URL após "/notes/"
        String pathInfo = request.getPathInfo();
        
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                // Extrai o ID da parte final do caminho
                long id = Long.parseLong(pathInfo.substring(1));

                // Exclui a nota existente com base no ID
                singleton.deleteNoteById(id);
                
                response.getWriter().println("Nota excluída com sucesso!");
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Formato de ID inválido");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("ID não fornecido na URL");
        }
    }
}
