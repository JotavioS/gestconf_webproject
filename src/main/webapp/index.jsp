<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Notas</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 100px 0;
            padding: 0;
            background-color: #f2f2f2;
        }

        h1,
        h2,
        h3 {
            margin: 10px 0;
            color: #333;
        }
        
        li {
        	display: flex;
    		align-content: center;
    		justify-content: space-between;
    		align-items: center;
    		padding: 5px 0;
        }

        input{
            border: 1px solid #d0d0d0;
            border-radius: 5px;
            padding: 10px;
        }

        #header {
            background-color: #4CAF50;
            padding: 10px;
            text-align: center;
            color: white;
        }

        #addNoteButton {
            background-color: #4CAF50;
            color: white;
            padding: 10px;
            border: none;
            cursor: pointer;
        }

        #noteList {
            list-style-type: none;
            padding: 0;
        }

        .main {
            margin: auto;
            width: calc(100% - 430px);
            max-width: 1000px;
            padding: 20px 30px;
            background-color: #fff;
            min-height: 700px;
            border: 1px solid #d0d0d0;
            box-shadow: 0px 8px 6px -6px #999999;
            border-radius: 10px;
            font-weight: 600;
        }

        .noteItem {
            background-color: white;
            padding: 10px;
            margin: 10px;
            border-radius: 5px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .noteItem button {
            background-color: #333;
            color: white;
            padding: 5px 10px;
            border: none;
            cursor: pointer;
            margin-right: 5px;
        }

        .button1 {
            background-color: #fff;
            border: 1px solid #d0d0d0;
            box-shadow: 2px 1px 4px 0px #04950c3d;
            padding: 10px;
            border-radius: 5px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
        }

        .button1:hover {
            color: #5bc05f;
            box-shadow: 2px 1px 4px 0px #04950c3d;
        }

        .button2 {
            background-color: #fff;
            border: 1px solid #d0d0d0;
            box-shadow: 2px 1px 4px 0px #04950c3d;
            padding: 10px;
            border-radius: 5px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
        }

        .button2:hover {
            color: #ff9b00;
            box-shadow: 2px 1px 4px 0px #ff9b0047;
        }

        .button3 {
            background-color: #fff;
            border: 1px solid #d0d0d0;
            box-shadow: 2px 1px 4px 0px #04950c3d;
            padding: 10px;
            border-radius: 5px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
        }

        .button3:hover {
            color: #ff0000;
            box-shadow: 2px 1px 4px 0px #ff000047;
        }

        .col{
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .cb{
            display: flex;
            justify-content: center;
            gap: 10px;
        }

        .floatR {
            float: right;
        }

        #overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.7);
            z-index: 1;
        }

        #modal,
        #deleteConfirmation {
            display: none;
            position: fixed;
            top: 30%;
            left: 50%;
            min-width: 600px;
            transform: translate(-50%, -50%);
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            z-index: 2;
        }

        #deleteConfirmation{
            text-align: center;
        }

        #popup {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: #333;
            color: white;
            padding: 15px;
            border-radius: 5px;
            z-index: 3;
        }
    </style>
</head>

<body>
    <!-- Overlay para o modal -->
    <div id="overlay"></div>

    <div class="main">
        <button class="button1 floatR" onclick="addNote()">Nova Nota</button>
        <!-- Lista de Notas -->
        <h2>Notas:</h2>
        <ul id="noteList"></ul>

        <!-- Modal para adição e edição de notas -->
        <div id="modal">
            <h3 id="modalTitle"></h3>
            <form class="col" onsubmit="saveNote(); return false;">
                <input type="hidden" id="noteId">
                Nome: <input type="text" id="noteName" required><br>
                Descrição: <input type="text" id="noteDescription" required><br>
                <div class="cb">
                    <button class="button1" type="submit">Salvar</button>
                    <button class="button3" type="button" onclick="closeOverlay()">Cancelar</button>
                </div>
            </form>
        </div>

        <!-- Modal de confirmação para exclusão -->
        <div id="deleteConfirmation">
            <div class="col">
                <h3>Você tem certeza que deseja excluir esta nota?</h3>
                <div class="cb">
                    <button class="button1" onclick="confirmDeleteNote()">Sim</button>
                    <button class="button3" onclick="closeDeleteConfirmation()">Não</button>
                </div>
                <input type="hidden" id="deleteNoteId">
            </div>
        </div>

        <!-- Popup para mensagens temporárias -->
        <div id="popup"></div>
    </div>
    <script type="text/javascript">
        function showAllNotes() {
            $.ajax({
                type: 'GET',
                url: '/api/notes',
                dataType: 'json',
                success: function (data) {
                    $('#noteList').empty();
                    $.each(data, function (index, note) {
                        // Adiciona um botão de edição para cada nota
                        $('#noteList').append('<li>' + note.name +
                            ' <div class="cb"><button class="button2" onclick="editNoteConfirmation(' + note.id + ')">Editar</button>' +
                            ' <button class="button3" onclick="showDeleteConfirmation(' + note.id + ')">Excluir</button></div></li>');
                    });
                },
                error: function () {
                    showPopup('Erro ao buscar notas.', 3000);
                }
            });
        }

        function showDeleteConfirmation(id) {
            // Esconde o overlay
            $('#overlay').show();
            // Exibe o modal de confirmação
            $('#deleteConfirmation').fadeIn();
            // Define o ID da nota a ser excluída
            $('#deleteNoteId').val(id);
        }

        function closeDeleteConfirmation() {
            // Exibe novamente o overlay
            $('#overlay').hide();
            // Esconde o modal de confirmação
            $('#deleteConfirmation').fadeOut();
        }

        function confirmDeleteNote() {
            var id = $('#deleteNoteId').val();
            deleteNote(id);
            closeDeleteConfirmation();
        }

        function addNote() {
            $('#modalTitle').text('Adicionar Nova Nota');
            $('#noteName').val('');
            $('#noteDescription').val('');
            openModal();
        }

        function editNoteConfirmation(id) {
            $.ajax({
                type: 'GET',
                url: '/api/notes/' + id,
                dataType: 'json',
                success: function (note) {
                    $('#modalTitle').text('Editar Nota');
                    $('#noteId').val(note.id);
                    $('#noteName').val(note.name);
                    $('#noteDescription').val(note.description);
                    openModal();
                },
                error: function () {
                    alert('Erro ao buscar nota para edição.');
                }
            });
        }

        function openModal() {
            $('#overlay').show();
            $('#modal').show();
        }

        function closeOverlay() {
            $('#overlay').hide();
            $('#modal').hide();
        }

        function showPopup(message, duration) {
            $('#popup').text(message).fadeIn();
            setTimeout(function () {
                $('#popup').fadeOut();
            }, duration);
        }

        function saveNote() {
            var id = $('#noteId').val();
            var noteName = $('#noteName').val();
            var noteDescription = $('#noteDescription').val();

            var method = id ? 'PUT' : 'POST';
            var url = id ? '/api/notes/' + id : '/api/notes';

            $.ajax({
                type: method,
                url: url,
                contentType: 'application/json;charset=UTF-8',
                data: JSON.stringify({ "name": noteName, "description": noteDescription }),
                success: function () {
                    showPopup('Nota salva com sucesso!', 1000);
                    closeOverlay();
                    showAllNotes();
                },
                error: function () {
                    showPopup('Erro ao salvar nota.', 1000);
                }
            });
        }

        function deleteNote(id) {
            $.ajax({
                type: 'DELETE',
                url: '/api/notes/' + id,
                success: function () {
                    showPopup('Nota excluída com sucesso!', 1000);
                    showAllNotes();
                },
                error: function () {
                    showPopup('Erro ao excluir nota.', 1000);
                }
            });
        }

        // Chama showAllNotes quando a página carrega
        $(document).ready(function () {
            showAllNotes();
        });
    </script>
</body>

</html>