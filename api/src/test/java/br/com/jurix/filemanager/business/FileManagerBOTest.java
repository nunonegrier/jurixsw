package br.com.jurix.filemanager.business;

import br.com.jurix.filemanager.entity.FileMetadata;
import br.com.jurix.filemanager.enumeration.StateFileMetadataEnum;
import br.com.jurix.filemanager.filesystem.adapters.harddisc.HardDiscFileSystem;
import br.com.jurix.testUtils.FileTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class FileManagerBOTest {

    @InjectMocks
    private FileManagerBO fileManagerBO;

    @Mock
    private HardDiscFileSystem hardDiscFileSystem;

    @Mock
    private FileMetadataBO fileMetadataBO;

    @Mock
    private FolderManagerBO folderManagerBO;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sholdSaveTemporaryFile(){

        String fileName = "testfilesucess.txt";
        String destFolder = "folder1/folder2";

        MockMultipartFile mockMultipartFile = FileTestUtils.createMockFile(fileName, "content in file");

        FileMetadata fileMetadata = new FileMetadata();

        when(fileMetadataBO.instanceTemporaryFileMetadata(mockMultipartFile, destFolder)).thenReturn(fileMetadata);
        when(fileMetadataBO.save(any(FileMetadata.class))).then(returnsFirstArg());
        when(hardDiscFileSystem.storeFile(eq(mockMultipartFile), anyString())).thenReturn("/opt/test/temp/" + fileName);

        FileMetadata fileMetadataTemp = fileManagerBO.saveTempFile(mockMultipartFile, destFolder);

        assertEquals("/opt/test/temp/testfilesucess.txt", fileMetadataTemp.getUri());
    }

    @Test
    public void sholdChangeToDefinitive(){

        Long idFileMetadata = 55L;

        FileMetadata parentFolder = new FileMetadata();
        parentFolder.setUri("/opt/test/definitive/folder1/folder2");

        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setName("initial-file-name.txt");
        fileMetadata.setDestFolder("folder1/folder2");
        fileMetadata.setUri("temp-uri");

        String resultUri = "/opt/test/definitive/folder1/folder2/testfilesucess.txt";

        when(fileMetadataBO.findOne(idFileMetadata)).thenReturn(fileMetadata);

        doAnswer((Answer<Void>) invocation -> {
            Object[] args = invocation.getArguments();
            FileMetadata fileMetadataArg = (FileMetadata) args[0];

            fileMetadataArg.setParentFolder(parentFolder);

            return null;
        }).when(folderManagerBO).createAndSetParentFolder(fileMetadata);

        when(hardDiscFileSystem.moveFile(fileMetadata.getUri(), "definitive" + "/" + fileMetadata.getDestFolder(), fileMetadata.getName())).thenReturn(resultUri);
        when(fileMetadataBO.save(any(FileMetadata.class))).then(returnsFirstArg());

        FileMetadata fileMetadataDefinitive = fileManagerBO.updatefileToDefinitive(idFileMetadata);

        assertEquals(resultUri, fileMetadataDefinitive.getUri());
        assertEquals(parentFolder.getUri(), fileMetadata.getParentFolder().getUri());
        assertEquals(StateFileMetadataEnum.DEFINITIVE, fileMetadataDefinitive.getState());
        assertEquals("testfilesucess.txt", fileMetadataDefinitive.getName());
    }

}
