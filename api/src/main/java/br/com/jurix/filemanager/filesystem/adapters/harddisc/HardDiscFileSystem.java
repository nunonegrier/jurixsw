package br.com.jurix.filemanager.filesystem.adapters.harddisc;

import br.com.jurix.filemanager.filesystem.exceptions.CreateDiretoryException;
import br.com.jurix.filemanager.filesystem.exceptions.FileEmptyException;
import br.com.jurix.filemanager.filesystem.exceptions.OriginFileNotExistsException;
import br.com.jurix.filemanager.filesystem.exceptions.SavingFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
@Component
public class HardDiscFileSystem {

    @Autowired
    private HardDiscProperties hardDiscProperties;

    public String storeFile(MultipartFile file, String destPath) {

        Path fileDestPath = hardDiscProperties.getRootPath().resolve(destPath);
        initPath(fileDestPath);

        try {

            validateEmptyFile(file);

            Path filePath = HadDiscFileNameResolver.getPathNewFile(fileDestPath, file.getOriginalFilename());

            String absolutePathStr = filePath.toAbsolutePath().toString();

            Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE_NEW);

            log.info("Saving the form file to:" + absolutePathStr);

            return absolutePathStr;
        } catch (IOException e) {

            log.error("Error to save file.", e);

            throw new SavingFileException(getClass().getName());
        }
    }

    public String moveFile(String originFullPath, String destRelativePath, String originalFilename) {

        Path originPath = Paths.get(originFullPath);

        if (!originPath.toFile().exists()) {
            throw new OriginFileNotExistsException();
        }

        Path fileDestPath = hardDiscProperties.getRootPath().resolve(destRelativePath);
        initPath(fileDestPath);

        try {

            Path filePath = HadDiscFileNameResolver.getPathNewFile(fileDestPath, originalFilename);

            String absolutePathStr = filePath.toAbsolutePath().toString();

            Files.move(originPath, filePath);

            return absolutePathStr;

        } catch (IOException e) {

            log.error("Error to save file.", e);

            throw new SavingFileException(getClass().getName());
        }
    }

    public InputStream getFileAsInputStream(String path) {

        try {

            Path fileDestPath = Paths.get(path);
            byte[] data = Files.readAllBytes(fileDestPath);
            return new ByteArrayInputStream(data);

        } catch (IOException e) {
            throw new RuntimeException("Falha ao ler arquivo", e);
        }
    }


    private void validateEmptyFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileEmptyException();
        }
    }

    private void initPath(Path path) {

        if (!path.toFile().exists()) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new CreateDiretoryException(getClass().getName());
            }
        }

    }


}
