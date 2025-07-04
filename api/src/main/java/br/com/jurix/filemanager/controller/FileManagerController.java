package br.com.jurix.filemanager.controller;


import br.com.jurix.filemanager.business.FileManagerBO;
import br.com.jurix.filemanager.business.FileMetadataBO;
import br.com.jurix.filemanager.entity.FileMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/arquivos")
public class FileManagerController {

    @Autowired
    private FileManagerBO fileManagerBO;

    @Autowired
    private FileMetadataBO fileMetadataBO;


    @PostMapping(path = "/uploadTemp", consumes = "multipart/form-data")
    public FileMetadata uploadTempFile(@RequestParam("file") MultipartFile file, @RequestParam("repository") String repository) {
        return fileManagerBO.saveTempFile(file, repository);
    }

    @PostMapping(path = "/uploadDefinitive", consumes = "multipart/form-data")
    public FileMetadata uploadDefinitive(@RequestParam("file") MultipartFile file, @RequestParam("repository") String repository, @RequestParam("description") String description) {
        FileMetadata fileMetadata = fileManagerBO.saveTempFile(file, repository, description);
        return fileManagerBO.updatefileToDefinitive(fileMetadata.getId());
    }


    @PutMapping(path = "/setDefinitive/{id}")
    public FileMetadata setDefinitiveFile(@PathVariable("id") Long id, @RequestBody FileMetadata fileMetadata) {
        return fileManagerBO.updatefileToDefinitive(id, fileMetadata);
    }

    @PutMapping(path = "/setRemoved/{id}")
    public void setRemoved(@PathVariable("id") Long id) {
        fileManagerBO.setRemoved(id);
    }

    @GetMapping(path = "/getMetadata")
    public FileMetadata getMetadata(@RequestParam("path") String path) {
        return fileMetadataBO.getFileMetadataWithChildres(path);
    }

    @GetMapping(path = "/download")
    public void downloadDefinitiveFile(@RequestParam("uri") String metadataUri, HttpServletResponse httpServletResponse) {
        fileManagerBO.downloadDefinitiveFile(metadataUri, httpServletResponse);
    }


}
