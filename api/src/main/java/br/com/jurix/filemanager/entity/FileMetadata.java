package br.com.jurix.filemanager.entity;

import br.com.jurix.filemanager.enumeration.StateFileMetadataEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TB_FILE_METADATA", schema = "JURIX")
@SequenceGenerator(name = "SEQ_FILE_METADATA", sequenceName = "JURIX.SEQ_FILE_METADATA")
@EqualsAndHashCode
@ToString
@Getter
@Setter
@NoArgsConstructor
public class FileMetadata {

    @Id
    @Column(name = "FM_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_FILE_METADATA")
    private Long id;

    @Column(name = "FM_NAME", length = 256, nullable = false)
    private String name;

    @Column(name = "FM_EXTENSION", length = 16)
    private String extension;

    @Column(name = "FM_DESCRIPTION", length = 512)
    private String description;

    @JsonIgnore
    @Column(name = "FM_URI", length = 512, nullable = false)
    private String uri;

    @Column(name = "FM_DEST_FOLDER", length = 256)
    private String destFolder;

    @Column(name = "FM_STATE", nullable = false)
    @Enumerated(EnumType.STRING)
    private StateFileMetadataEnum state;

    @Column(name = "FM_SIZE")
    private Long size;

    @Column(name = "FM_IS_FOLDER", nullable = false)
    private Boolean isFolder;

    @Column(name = "FM_CREATED_AT", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FM_PARENT_ID", referencedColumnName = "FM_ID")
    private FileMetadata parentFolder;

    @Transient
    private List<FileMetadata> childrens;


    public FileMetadata(MultipartFile file, String destFolder) {
        this.name = file.getOriginalFilename();
        this.size = file.getSize();
        this.extension = FilenameUtils.getExtension(file.getOriginalFilename());
        this.isFolder = false;
        this.destFolder = destFolder;
    }

    public FileMetadata(String folderName){
        this.name = folderName;
        this.isFolder = true;
    }
}
