package propensi.GCG.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dokumen")
public class DokumenModel implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @JsonView(LihatDokumenModel.FileInfo.class)
    private String id;

    @JsonView(LihatDokumenModel.FileInfo.class)
    private String fileName;

    private String fileType;

    @Lob
    private byte[] data;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idSubmisi", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private SubmisiModel submisiDokumen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idArsip", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private DokumenArsipModel arsipDokumen;

    public DokumenModel() {

    }

    public DokumenModel(String fileName, String fileType, byte[] data, SubmisiModel submisiModel, DokumenArsipModel dokumenArsipModel) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.submisiDokumen = submisiModel;
        this.arsipDokumen = dokumenArsipModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public SubmisiModel getSubmisiDokumen() {
        return submisiDokumen;
    }

    public void setSubmisiDokumen(SubmisiModel submisiDokumen) {
        this.submisiDokumen = submisiDokumen;
    }

    public DokumenArsipModel getArsipDokumen() {
        return arsipDokumen;
    }

    public void setArsipDokumen(DokumenArsipModel arsipDokumen) {
        arsipDokumen = arsipDokumen;
    }
}
