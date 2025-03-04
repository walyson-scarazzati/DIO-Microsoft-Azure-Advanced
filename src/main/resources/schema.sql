-- Criar o banco de dados (caso ainda não exista)
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'ToDoDB')
BEGIN
    CREATE DATABASE ToDoDB;
END
GO

-- Usar o banco de dados
USE ToDoDB;
GO

-- Criar a tabela ToDoItem
CREATE TABLE ToDoItem (
    Id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    [order] INT NULL,
    title NVARCHAR(255) NOT NULL,
    url NVARCHAR(500) NOT NULL,
    completed BIT DEFAULT 0
);
GO

-- Criar índice para otimizar buscas por título
CREATE INDEX IDX_ToDoItem_Title ON ToDoItem(title);
GO
