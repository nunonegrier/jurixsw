package br.com.jurix.testUtils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class FileTestUtils {

    public static File  createFileTest(Path testRootPath, String fileName) throws IOException {

        Path notExistsFilePath = testRootPath.resolve(fileName);
        File f = new File(notExistsFilePath.toAbsolutePath().toString());
        f.createNewFile();
        return f;
    }

    public static MockMultipartFile createMockFile(String fileName, String contentString){

        final byte[] content = contentString.getBytes();
        return new MockMultipartFile(FilenameUtils.removeExtension(fileName) , fileName, "text/plain", content);
    }
}
