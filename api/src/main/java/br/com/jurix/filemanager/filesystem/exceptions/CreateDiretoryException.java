package br.com.jurix.filemanager.filesystem.exceptions;

public class CreateDiretoryException extends RuntimeException{

    public CreateDiretoryException(String fileSystemName) {
        super("Erro ao criar diretório com " + fileSystemName);
    }
}
