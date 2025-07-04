package br.com.jurix.filemanager.business;


import br.com.jurix.filemanager.entity.FileMetadata;
import org.apache.commons.io.FilenameUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class FolderManagerBOTest {


    @InjectMocks
    private FolderManagerBO folderManagerBO;

    @Mock
    private FileMetadataBO fileMetadataBO;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sholdCreateAndSetThreeLevelParentFolder(){

        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setDestFolder("folder1/folder2/folder3");
        fileMetadata.setUri("/opt/definitive/folder1/folder2/folder3/file1.txt");

        when(fileMetadataBO.save(any(FileMetadata.class))).then(returnsFirstArg());

        folderManagerBO.createAndSetParentFolder(fileMetadata);


        ArgumentCaptor<FileMetadata> fileMetadataCaptor = ArgumentCaptor.forClass(FileMetadata.class);
        verify(fileMetadataBO, times(3)).save(fileMetadataCaptor.capture());

        List<FileMetadata> foldersSaved = fileMetadataCaptor.getAllValues();

        assertEquals("folder1", foldersSaved.get(0).getName());
        assertNull(foldersSaved.get(0).getDestFolder());
        assertNull(foldersSaved.get(0).getParentFolder());
        assertEquals("/opt/definitive/folder1", foldersSaved.get(0).getUri());

        assertEquals("folder2", foldersSaved.get(1).getName());
        assertEquals("folder1", foldersSaved.get(1).getDestFolder());
        assertEquals(foldersSaved.get(0).getName(), foldersSaved.get(1).getParentFolder().getName());
        assertEquals("/opt/definitive/folder1/folder2", foldersSaved.get(1).getUri());

        assertEquals("folder3", foldersSaved.get(2).getName());
        assertEquals("folder1/folder2", foldersSaved.get(2).getDestFolder());
        assertEquals(foldersSaved.get(1).getName(), foldersSaved.get(2).getParentFolder().getName());
        assertEquals("/opt/definitive/folder1/folder2/folder3", foldersSaved.get(2).getUri());

        assertEquals(fileMetadata.getParentFolder().getName(), "folder3");

    }

    @Test
    public void sholdCreateAndSetOneLevelParentFolder(){

        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setDestFolder("folder1");
        fileMetadata.setUri("/opt/definitive/folder1/file1.txt");

        when(fileMetadataBO.save(any(FileMetadata.class))).then(returnsFirstArg());

        folderManagerBO.createAndSetParentFolder(fileMetadata);

        ArgumentCaptor<FileMetadata> fileMetadataCaptor = ArgumentCaptor.forClass(FileMetadata.class);
        verify(fileMetadataBO, times(1)).save(fileMetadataCaptor.capture());

        List<FileMetadata> foldersSaved = fileMetadataCaptor.getAllValues();

        assertEquals("folder1", foldersSaved.get(0).getName());
        assertNull(foldersSaved.get(0).getDestFolder());
        assertNull(foldersSaved.get(0).getParentFolder());
        assertEquals("/opt/definitive/folder1", foldersSaved.get(0).getUri());

        assertEquals(fileMetadata.getParentFolder().getName(), "folder1");
    }

    @Test
    public void sholdCreateAndSetParentFolderAlreadyExistem(){

        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setDestFolder("folder1");
        fileMetadata.setUri("/opt/definitive/folder1/file1.txt");

        FileMetadata folderFileMetadata = new FileMetadata();
        folderFileMetadata.setName("folder1");

        when(fileMetadataBO.findOne("folder1", null)).thenReturn(folderFileMetadata);

        folderManagerBO.createAndSetParentFolder(fileMetadata);

        verify(fileMetadataBO, never()).save(any(FileMetadata.class));

        assertEquals(fileMetadata.getParentFolder().getName(), "folder1");
    }


    @Test
    public void sholdDoNothingWhenFileIsInTheRoot(){

        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setDestFolder(null);
        fileMetadata.setUri("/opt/definitive/file1.txt");

        FileMetadata folderFileMetadata = new FileMetadata();

        folderManagerBO.createAndSetParentFolder(fileMetadata);

        verify(fileMetadataBO, never()).save(any(FileMetadata.class));
        verify(fileMetadataBO, never()).findOne(anyString(), anyString());

        assertNull(fileMetadata.getParentFolder());
    }

    @Test
    public void windowsFolderTest(){


        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setDestFolder("cliente/2");
        fileMetadata.setUri("C:\\jurix\\jurix-arquivos\\definitive/cliente\\2\\pdf-3.pdf");

        System.out.println(FilenameUtils.normalize(fileMetadata.getUri()));


    }


}

