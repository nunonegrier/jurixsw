package br.com.jurix.filemanager.business;


import br.com.jurix.filemanager.entity.FileMetadata;
import br.com.jurix.filemanager.enumeration.StateFileMetadataEnum;
import br.com.jurix.filemanager.filesystem.adapters.harddisc.HardDiscFileSystem;
import br.com.jurix.filemanager.helper.ResponseFileHelper;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Service
@Transactional
public class FileManagerBO {

    private static final String TEMP_DIR = "temp";
    private static final String DEFINITIVE_DIR = "definitive";

    @Autowired
    private HardDiscFileSystem hardDiscFileSystem;

    @Autowired
    private FileMetadataBO fileMetadataBO;

    @Autowired
    private FolderManagerBO folderManagerBO;

    @Autowired
    private EntityManager entityManager;

    public FileMetadata saveTempFile(MultipartFile file, String destFolder) {

        FileMetadata fileMetadata = fileMetadataBO.instanceTemporaryFileMetadata(file, destFolder);
        String uri = hardDiscFileSystem.storeFile(file, TEMP_DIR);
        fileMetadata.setUri(uri);

        return fileMetadataBO.save(fileMetadata);
    }

    @Transactional
    public FileMetadata saveTempFile(MultipartFile file, String destFolder, String description) {

        FileMetadata fileMetadata = fileMetadataBO.instanceTemporaryFileMetadata(file, destFolder);
        String uri = hardDiscFileSystem.storeFile(file, TEMP_DIR);
        fileMetadata.setUri(uri);
        fileMetadata.setDescription(description);

        return fileMetadataBO.save(fileMetadata);
    }


    @Transactional
    public FileMetadata updatefileToDefinitive(Long idFileMetadata) {
        FileMetadata fileMetadata = fileMetadataBO.findOne(idFileMetadata);

        String uri = hardDiscFileSystem.moveFile(fileMetadata.getUri(), DEFINITIVE_DIR + "/" + fileMetadata.getDestFolder(), fileMetadata.getName());

        fileMetadata.setName(FilenameUtils.getName(uri));
        fileMetadata.setUri(uri);
        fileMetadata.setState(StateFileMetadataEnum.DEFINITIVE);

        folderManagerBO.createAndSetParentFolder(fileMetadata);

        return fileMetadataBO.save(fileMetadata);
    }

    @Transactional
    public FileMetadata updatefileToDefinitive(Long idFileMetadata, FileMetadata fileMetadataEdit) {

        lockTable();

        FileMetadata fileMetadata = fileMetadataBO.findOne(idFileMetadata);

        fileMetadata.setDescription(fileMetadataEdit.getDescription());
        fileMetadata.setDestFolder(fileMetadataEdit.getDestFolder());

        String uri = hardDiscFileSystem.moveFile(fileMetadata.getUri(), DEFINITIVE_DIR + "/" + fileMetadata.getDestFolder(), fileMetadata.getName());

        fileMetadata.setName(FilenameUtils.getName(uri));
        fileMetadata.setUri(uri);
        fileMetadata.setState(StateFileMetadataEnum.DEFINITIVE);

        folderManagerBO.createAndSetParentFolder(fileMetadata);

        return fileMetadataBO.save(fileMetadata);
    }

    private void lockTable(){
        String statement = "LOCK TABLE " + getFullTableNameFromClass(FileMetadata.class) + " IN ACCESS EXCLUSIVE MODE";
        entityManager.createNativeQuery(statement).executeUpdate();
    }

    private static String getFullTableNameFromClass(Class entityClass) {
        Table table = (Table) entityClass.getAnnotation(Table.class);//NOSONAR
        return table.schema() + "." + table.name();
    }


    public void downloadDefinitiveFile(String metadataUri, HttpServletResponse httpServletResponse) {

        FileMetadata fileMetadata = fileMetadataBO.getFileMetadata(metadataUri);
        InputStream file = hardDiscFileSystem.getFileAsInputStream(fileMetadata.getUri());

        ResponseFileHelper.prepareDownloadResponse(fileMetadata, file, httpServletResponse);
    }

    public void setRemoved(Long idFileMetadata) {
        FileMetadata fileMetadata = fileMetadataBO.findOne(idFileMetadata);
        fileMetadata.setState(StateFileMetadataEnum.REMOVED);
        fileMetadataBO.save(fileMetadata);
    }
}
