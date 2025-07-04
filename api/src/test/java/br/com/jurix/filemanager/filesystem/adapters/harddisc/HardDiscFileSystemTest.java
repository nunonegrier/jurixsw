package br.com.jurix.filemanager.filesystem.adapters.harddisc;


import br.com.jurix.filemanager.filesystem.exceptions.CreateDiretoryException;
import br.com.jurix.filemanager.filesystem.exceptions.FileEmptyException;
import br.com.jurix.filemanager.filesystem.exceptions.OriginFileNotExistsException;
import br.com.jurix.filemanager.filesystem.exceptions.SavingFileException;
import br.com.jurix.testUtils.FileTestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HardDiscFileSystemTest {

    @InjectMocks
    private HardDiscFileSystem hardDiscFileSystem;

    @Mock
    private HardDiscProperties hardDiscProperties;

    private Path testRootPath;


    @Before
    public void init() throws IOException {
        MockitoAnnotations.initMocks(this);
        initRootFolderTest();
    }

    @Test
    public void storeFileSucess(){

        setMockProperties();

        String fileName = "testfilesucess.txt";
        MockMultipartFile mockMultipartFile = FileTestUtils.createMockFile(fileName, "content in file");

        String folderDestname = "testsucess/test1";

        String fullFilePath = hardDiscFileSystem.storeFile(mockMultipartFile, folderDestname);

        Path filePath = Paths.get(fullFilePath);

        assertTrue(filePath.toFile().exists());
    }

    @Test
    public void storeFileSucessWithAlreadyExistsDestFolder() throws IOException {

        setMockProperties();

        String fileName = "testwithdestfolder.txt";
        MockMultipartFile mockMultipartFile = FileTestUtils.createMockFile(fileName, "content in file");
        String folderDestname = "testsucess/test1";

        Files.createDirectories(testRootPath.resolve(folderDestname));

        String fullFilePath = hardDiscFileSystem.storeFile(mockMultipartFile, folderDestname);

        Path filePath = Paths.get(fullFilePath);

        assertTrue(filePath.toFile().exists());
    }

    @Test(expected = FileEmptyException.class)
    public void sholdThrowErrorWhenFileIsEmpty() throws IOException {

        setMockProperties();

        String fileName = "testwithdestfolder.txt";
        MockMultipartFile mockMultipartFile = FileTestUtils.createMockFile(fileName, "");
        String folderDestname = "testsucess/test1";

        Files.createDirectories(testRootPath.resolve(folderDestname));

        hardDiscFileSystem.storeFile(mockMultipartFile, folderDestname);
    }

    @Test(expected = SavingFileException.class)
    public void sholdThrowGenericErrorWhenIOExceptionIsThrowedToSaveFile() throws IOException {

        setMockProperties();

        MultipartFile mockMultipartFile = mock(MultipartFile.class);

        String folderDestname = "testsucess/test1";

        doThrow(IOException.class).when(mockMultipartFile).isEmpty();

        hardDiscFileSystem.storeFile(mockMultipartFile, folderDestname);
    }

    @Test(expected = CreateDiretoryException.class)
    public void sholdThrowGenericErrorWhenIOExceptionIsThrowedToCreateFolders() throws IOException {


        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        String folderDestname = "foldertest";

        File folderTest = testRootPath.resolve("folderDestname").toFile();

        testRootPath = mock(Path.class);

        when(hardDiscProperties.getRootPath()).thenReturn(testRootPath);
        when(testRootPath.resolve(folderDestname)).thenReturn(testRootPath);
        when(testRootPath.toFile()).thenReturn(folderTest);
        doThrow(IOException.class).when(testRootPath).getFileSystem();
        doThrow(IOException.class).when(testRootPath).toAbsolutePath();

        hardDiscFileSystem.storeFile(mockMultipartFile, folderDestname);
    }

    @Test
    public void sholdMoveFile() throws IOException {

        setMockProperties();

        String fileNameOrigin = "file-test-mov1.txt";
        File fileOrigin = FileTestUtils.createFileTest(testRootPath, fileNameOrigin);

        String fullFilePath = hardDiscFileSystem.moveFile(fileOrigin.getAbsolutePath(), "definitive", fileNameOrigin);

        Path filePath = Paths.get(fullFilePath);


        assertTrue(filePath.toFile().exists());
        assertFalse(fileOrigin.exists());
    }

    @Test(expected = OriginFileNotExistsException.class)
    public void sholdThrowErrorWhenOriginFileNotExists() throws IOException {

        setMockProperties();

        String fileNameOrigin = "file-ssssssssssss.txt";

        hardDiscFileSystem.moveFile("sssssss", "definitive", fileNameOrigin);
    }

    @Test(expected = SavingFileException.class)
    public void sholdThrowGenericErrorWhenIOExceptionIsThrowedToMoveFile() throws IOException {

        setMockProperties();

        String fileNameOrigin = "file-test-mov1.txt";
        File fileOrigin = FileTestUtils.createFileTest(testRootPath, fileNameOrigin);

        hardDiscFileSystem.moveFile(fileOrigin.getAbsolutePath(), "definitive", "invalid / name");
    }

    private void initRootFolderTest() throws IOException {

        String testRoot = "target/hardisctest/harddiscfilesystem";
        testRootPath = Paths.get(testRoot);

        FileUtils.deleteDirectory(new File(testRoot));
        Files.createDirectories(testRootPath);
    }


    private void setMockProperties(){
        when(hardDiscProperties.getRootPath()).thenReturn(testRootPath);
    }


    

}
