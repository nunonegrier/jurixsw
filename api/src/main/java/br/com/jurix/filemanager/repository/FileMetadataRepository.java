package br.com.jurix.filemanager.repository;

import br.com.jurix.filemanager.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {

    @Query("SELECT fm FROM FileMetadata fm WHERE fm.name=:name AND fm.destFolder=:destFolder")
    FileMetadata findByNameAndFolderDest(@Param("name")String name, @Param("destFolder")String destFolder);

    @Query("SELECT fm FROM FileMetadata fm WHERE fm.name=:name AND fm.destFolder IS NULL")
    FileMetadata findByNameAndFolderDestNull(@Param("name")String name);

    @Query("SELECT fm FROM FileMetadata fm WHERE fm.parentFolder.id=:id AND fm.state <> 'REMOVED'")
    List<FileMetadata> findChildrens(@Param("id")Long id);
}
