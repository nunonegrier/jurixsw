package br.com.jurix.filemanager.filesystem.exceptions;

public class FileEmptyException extends RuntimeException{

    public FileEmptyException() {
        super("Arquivo vazio.");
    }
}
