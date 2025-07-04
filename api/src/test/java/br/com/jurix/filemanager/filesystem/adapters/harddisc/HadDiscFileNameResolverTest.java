package br.com.jurix.filemanager.filesystem.adapters.harddisc;

import br.com.jurix.testUtils.FileTestUtils;
import br.com.jurix.testUtils.PrivateConstructor;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class HadDiscFileNameResolverTest {


    private Path testRootPath;

    @Before
    public void prepareTest() throws IOException {
        initRootFolderTest();
    }

    @Test
    public void testConstructor() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        PrivateConstructor.assertPrivateConstructor(HadDiscFileNameResolver.class);
    }

    @Test
    public void getPathNewFileNotExistsFileTest() {

        String notExistsFileName = "not-exists-file.txt";
        Path resultPath = HadDiscFileNameResolver.getPathNewFile(testRootPath, notExistsFileName);
        assertEquals(testRootPath.resolve("not-exists-file.txt"), resultPath);
    }

    @Test
    public void getPathNewFileWithtExistsFileTest() throws IOException {

        String notExistsFileName = "already-exists-file.txt";
        FileTestUtils.createFileTest(testRootPath, notExistsFileName);

        Path resultPath = HadDiscFileNameResolver.getPathNewFile(testRootPath, notExistsFileName);
        assertEquals(testRootPath.resolve("already-exists-file(1).txt"), resultPath);
    }

    public void initRootFolderTest() throws IOException {

        String testRoot = "target/hardisctest/filenameresolver";
        testRootPath = Paths.get(testRoot);

        FileUtils.deleteDirectory(new File(testRoot));
        Files.createDirectories(testRootPath);
    }


}
