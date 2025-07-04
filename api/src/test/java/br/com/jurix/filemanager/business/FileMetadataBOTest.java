package br.com.jurix.filemanager.business;

import br.com.jurix.datetime.business.JurixDateTime;
import br.com.jurix.filemanager.entity.FileMetadata;
import br.com.jurix.filemanager.enumeration.StateFileMetadataEnum;
import br.com.jurix.filemanager.exception.InvalidFileMetadataException;
import br.com.jurix.filemanager.repository.FileMetadataRepository;
import br.com.jurix.testUtils.FileTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



public class FileMetadataBOTest {

    @InjectMocks
    private FileMetadataBO fileMetadataBO;

    @Mock
    private JurixDateTime jurixDateTime;

    @Mock
    private FileMetadataRepository fileMetadataRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldInstanceANewTemporaryFile(){

        String fileName = "testfilesucess.txt";
        String destFolder = "folder1/folder2";

        MockMultipartFile mockMultipartFile = FileTestUtils.createMockFile(fileName, "content in file");

        Date createAt = new Date();

        when(jurixDateTime.getCurrentDateTime()).thenReturn(createAt);

        FileMetadata fileMetadata = fileMetadataBO.instanceTemporaryFileMetadata(mockMultipartFile, destFolder);

        assertEquals(createAt, fileMetadata.getCreateDate());
        assertEquals(StateFileMetadataEnum.TEMPORARY, fileMetadata.getState());
    }

    @Test
    public void shouldSaveFileMetadata(){

        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setId(77L);

        fileMetadataBO.save(fileMetadata);

        verify(fileMetadataRepository, times(1)).save(fileMetadata);
    }

    @Test
    public void shouldReturnFileMetadataWhenFinOne(){

        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setId(77L);

        when(fileMetadataRepository.findOne(fileMetadata.getId())).thenReturn(fileMetadata);

        FileMetadata fileMetadataFinded = fileMetadataBO.findOne(fileMetadata.getId());

        assertEquals(fileMetadata, fileMetadataFinded);
    }

    @Test
    public void shouldReturnFileMetadataWhenFinOneWithNameAndDestFolder(){

        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setId(77L);

        String path = "destFolder/name";

        when(fileMetadataRepository.findByNameAndFolderDest("name", "destFolder")).thenReturn(fileMetadata);

        FileMetadata fileMetadataFinded = fileMetadataBO.getFileMetadata(path);

        assertEquals(fileMetadata, fileMetadataFinded);
    }

    @Test(expected = InvalidFileMetadataException.class)
    public void sholdThrowErrorWhenFileMetadataNotExists(){

        Long id = 77L;

        when(fileMetadataRepository.findOne(id)).thenReturn(null);

        fileMetadataBO.findOne(id);
    }
}
