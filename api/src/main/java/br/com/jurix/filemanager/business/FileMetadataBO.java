package br.com.jurix.filemanager.business;

import br.com.jurix.datetime.business.JurixDateTime;
import br.com.jurix.filemanager.entity.FileMetadata;
import br.com.jurix.filemanager.enumeration.StateFileMetadataEnum;
import br.com.jurix.filemanager.exception.InvalidFileMetadataException;
import br.com.jurix.filemanager.repository.FileMetadataRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class FileMetadataBO {

    @Autowired
    private JurixDateTime jurixDateTime;

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    public FileMetadata instanceTemporaryFileMetadata(MultipartFile file, String destFolder) {

        FileMetadata fileMetadata = new FileMetadata(file, destFolder);
        fileMetadata.setCreateDate(jurixDateTime.getCurrentDateTime());
        fileMetadata.setState(StateFileMetadataEnum.TEMPORARY);

        return fileMetadata;
    }

    public FileMetadata save(FileMetadata fileMetadata) {
        return fileMetadataRepository.save(fileMetadata);
    }


    public FileMetadata findOne(Long id) {
        FileMetadata fileMetadata = fileMetadataRepository.findOne(id);
        validateFileMetadataExists(fileMetadata);
        return fileMetadata;
    }

    public FileMetadata findOne(String name, String destFolder) {

        if (Objects.isNull(destFolder)) {
            return fileMetadataRepository.findByNameAndFolderDestNull(name);
        }
        return fileMetadataRepository.findByNameAndFolderDest(name, destFolder);
    }

    public FileMetadata getFileMetadata(String metadataUri) {

        String name = FilenameUtils.getName(metadataUri);
        String destFolder = FilenameUtils.getPathNoEndSeparator(metadataUri);
        destFolder = destFolder.isEmpty() ? null : destFolder;


        return findOne(name, destFolder);
    }


    public FileMetadata getFileMetadataWithChildres(String metadataUri) {

        FileMetadata fileMetadata = getFileMetadata(metadataUri);
        if (Objects.nonNull(fileMetadata)) {
            fileMetadata.setChildrens(fileMetadataRepository.findChildrens(fileMetadata.getId()));
        }
        return fileMetadata;

    }

    private static void validateFileMetadataExists(FileMetadata fileMetadata) {
        if (Objects.isNull(fileMetadata)) {
            throw new InvalidFileMetadataException();
        }
    }

}
