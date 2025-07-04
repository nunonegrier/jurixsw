package br.com.jurix.filemanager.filesystem.adapters.harddisc;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;
import java.text.Normalizer;

public class HadDiscFileNameResolver {

    private HadDiscFileNameResolver(){

    }

    public static Path getPathNewFile(Path fileDestPath, String originalFileName){
        return getPathNewFile(fileDestPath, originalFileName, 0);
    }

    private static Path getPathNewFile(Path fileDestPath, String originalFileName, Integer counter){

        Path filePath = fileDestPath.resolve(getFormattedFileName(originalFileName, counter));

        if(filePath.toFile().exists()){
            return getPathNewFile(fileDestPath, originalFileName, ++counter);
        }

        return filePath;
    }

    private static String getFormattedFileName(String fileName, Integer sameNameFilesCounter) {
        String fileIndex = indexOfFileStringBuilder(sameNameFilesCounter);
        return Normalizer
                .normalize(getFilenameWithIndex(fileName, fileIndex), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll(" ", "_");
    }

    private static String getFilenameWithIndex(String fileName, String fileIndex) {
        return FilenameUtils.removeExtension(fileName) + fileIndex + "." +FilenameUtils.getExtension(fileName);
    }


    private static String indexOfFileStringBuilder(Integer sameNameFilesCounter) {
        return (sameNameFilesCounter > 0) ? "(" + Integer.toString(sameNameFilesCounter) + ")" : "";
    }
}
