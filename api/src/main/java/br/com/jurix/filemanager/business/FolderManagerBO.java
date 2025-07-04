package br.com.jurix.filemanager.business;

import br.com.jurix.filemanager.entity.FileMetadata;
import br.com.jurix.filemanager.enumeration.StateFileMetadataEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class FolderManagerBO {

    @Autowired
    private FileMetadataBO fileMetadataBO;

    public void createAndSetParentFolder(FileMetadata fileMetadata){

        if(Objects.nonNull(fileMetadata.getDestFolder())) {
            List<FileMetadata> foldersToCreate = getFoldersToCreate(fileMetadata);

            FileMetadata parentFolder = null;
            for (FileMetadata folderFileMetadata : foldersToCreate) {

                FileMetadata folder = createFolderIfExists(folderFileMetadata);
                folder.setParentFolder(parentFolder);

                parentFolder = folder;
            }

            fileMetadata.setParentFolder(parentFolder);
        }
    }

    private FileMetadata createFolderIfExists(FileMetadata folderFileMetadata){

        FileMetadata foldersExistem = fileMetadataBO.findOne(folderFileMetadata.getName(), folderFileMetadata.getDestFolder());

        if (Objects.isNull(foldersExistem)) {
            return fileMetadataBO.save(folderFileMetadata);
        }

        return foldersExistem;
    }

    private List<FileMetadata> getFoldersToCreate(FileMetadata fileMetadata){


        System.out.println("------ Preparando para criar pastas " + fileMetadata.getDestFolder());

        List<FileMetadata> foldersToCreate = new ArrayList<>();
        List<String> foldersNameToCreate = Arrays.asList(fileMetadata.getDestFolder().split("/"));
        List<String> destFolders = new ArrayList<>();

        for(String folderToCreate : foldersNameToCreate){

            System.out.println("-----------Criando pasta " + folderToCreate);

            FileMetadata folderFileMetadata = new FileMetadata(folderToCreate);

            setDestFolder(folderFileMetadata, destFolders);
            setUri(folderFileMetadata, fileMetadata);

            folderFileMetadata.setCreateDate(fileMetadata.getCreateDate());
            folderFileMetadata.setState(StateFileMetadataEnum.DEFINITIVE);

            foldersToCreate.add(folderFileMetadata);
            destFolders.add(folderToCreate);
        }

        return foldersToCreate;
    }

    private void setUri(FileMetadata folderFileMetadata, FileMetadata fileMetadata) {

        String uriFile = FilenameUtils.normalize(FilenameUtils.getFullPathNoEndSeparator(fileMetadata.getUri()));
        uriFile = uriFile.replace(FilenameUtils.normalize(fileMetadata.getDestFolder()) , "");

        if(Objects.isNull(folderFileMetadata.getDestFolder())) {
            folderFileMetadata.setUri(uriFile + folderFileMetadata.getName());
        }else{
            folderFileMetadata.setUri(FilenameUtils.normalize(uriFile + folderFileMetadata.getDestFolder() + "/" + folderFileMetadata.getName()));
        }
    }

    private void setDestFolder(FileMetadata fileMetadata, List<String> destFolders){
        if(CollectionUtils.isNotEmpty(destFolders)){
            fileMetadata.setDestFolder(String.join("/", destFolders));
        }
    }

}
