package br.com.jurix.filemanager.filesystem.exceptions;

public class SavingFileException extends RuntimeException {

    public SavingFileException(String fileSystemName) {
        super("Erro ao salvar arquivo com " + fileSystemName);
    }
}
