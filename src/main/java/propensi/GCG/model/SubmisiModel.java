package propensi.GCG.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "submisi")

public class SubmisiModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "activeFlag")
    private boolean activeFlag;

//    @NotNull
//    @Column(name = "dokumenRef", nullable = false)
//    private String dokumenRef;

    @Column(name = "nilai")
    private Double nilai;

    @Column(name = "komentar")
    private String komentar;

    @Column(name = "kelebihan")
    private String kelebihan;

    @Column(name = "kekurangan")
    private String kekurangan;

    @Column(name = "hambatan")
    private String hambatan;

    @Column(name = "referensi")
    private String referensi;

    @Column(name = "rekomendasi")
    private String rekomendasi;

    @NotNull
    @Column(name = "uploadDate", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime uploadDate;

    @Column(name = "evaluateDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime evaluateDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUploader", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserModel uploaderSubmisi;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idEvaluator", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserModel evaluatorSubmisi;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idLembarKerja", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private LembarKerjaModel submisiLembarKerja;

    @OneToMany(mappedBy = "submisiDokumen", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DokumenModel> dokumenList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

//    public String getDokumenRef() {
//        return dokumenRef;
//    }
//
//    public void setDokumenRef(String dokumenRef) {
//        this.dokumenRef = dokumenRef;
//    }

    public Double getNilai() {
        return nilai;
    }

    public void setNilai(Double nilai) {
        this.nilai = nilai;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getKelebihan() {
        return kelebihan;
    }

    public void setKelebihan(String kelebihan) {
        this.kelebihan = kelebihan;
    }

    public String getKekurangan() {
        return kekurangan;
    }

    public void setKekurangan(String kekurangan) {
        this.kekurangan = kekurangan;
    }

    public String getHambatan() {
        return hambatan;
    }

    public void setHambatan(String hambatan) {
        this.hambatan = hambatan;
    }

    public String getReferensi() {
        return referensi;
    }

    public void setReferensi(String referensi) {
        this.referensi = referensi;
    }

    public String getRekomendasi() {
        return rekomendasi;
    }

    public void setRekomendasi(String rekomendasi) {
        this.rekomendasi = rekomendasi;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public LocalDateTime getEvaluateDate() {
        return evaluateDate;
    }

    public void setEvaluateDate(LocalDateTime evaluateDate) {
        this.evaluateDate = evaluateDate;
    }

    public UserModel getUploaderSubmisi() {
        return uploaderSubmisi;
    }

    public void setUploaderSubmisi(UserModel uploaderSubmisi) {
        this.uploaderSubmisi = uploaderSubmisi;
    }

    public UserModel getEvaluatorSubmisi() {
        return evaluatorSubmisi;
    }

    public void setEvaluatorSubmisi(UserModel evaluatorSubmisi) {
        this.evaluatorSubmisi = evaluatorSubmisi;
    }

    public LembarKerjaModel getSubmisiLembarKerja() {
        return submisiLembarKerja;
    }

    public void setSubmisiLembarKerja(LembarKerjaModel submisiLembarKerja) {
        this.submisiLembarKerja = submisiLembarKerja;
    }

    public List<DokumenModel> getDokumenList() {
        return dokumenList;
    }

    public void setDokumenList(List<DokumenModel> dokumenList) {
        this.dokumenList = dokumenList;
    }
}