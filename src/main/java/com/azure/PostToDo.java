package com.azure;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class PostToDo {
    @FunctionName("PostToDo")
    public HttpResponseMessage run(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<String> request,
        final ExecutionContext context) {

        context.getLogger().info("Processing a new ToDoItem request.");

        ObjectMapper objectMapper = new ObjectMapper();
        ToDoItem toDoItem;

        try {
            toDoItem = objectMapper.readValue(request.getBody(), ToDoItem.class);
        } catch (IOException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Invalid request body").build();
        }

        toDoItem.setId(UUID.randomUUID());
        toDoItem.setUrl("https://example.com?id=" + toDoItem.getId());
        if (toDoItem.getCompleted() == null) {
            toDoItem.setCompleted(false);
        }

        // Conectar ao banco e inserir dados
        String sqlConnectionString = System.getenv("SqlConnectionString"); // Pegando a string de conexão do ambiente

        try (Connection connection = DriverManager.getConnection(sqlConnectionString);
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO dbo.ToDo (Id, [order], title, url, completed) VALUES (?, ?, ?, ?, ?)")) {

            stmt.setObject(1, toDoItem.getId());
            stmt.setObject(2, toDoItem.getOrder());
            stmt.setString(3, toDoItem.getTitle());
            stmt.setString(4, toDoItem.getUrl());
            stmt.setBoolean(5, toDoItem.getCompleted());

            stmt.executeUpdate();
        } catch (SQLException e) {
            context.getLogger().severe("SQL Error: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error").build();
        }

        return request.createResponseBuilder(HttpStatus.CREATED).body(toDoItem).build();
    }
}
