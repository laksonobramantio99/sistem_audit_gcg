package propensi.GCG.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "dokumenArsip")
public class DokumenArsipModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "nama", nullable = false)
    private String nama;

    @NotNull
    @Column(name = "uploadDate", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime uploadDate;

    @NotNull
    @Column(name = "dokumenRef", nullable = false)
    private String dokumenRef;

    @NotNull
    @Column(name = "tahun", nullable = false)
    private int tahun;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idPeriode", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private PeriodeTahunModel periodeDokumenArsip;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idOwner", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserModel ownerDokumenArsip;

    @OneToMany(mappedBy = "arsipDokumen", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DokumenModel> arsipDokumenList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getDokumenRef() {
        return dokumenRef;
    }

    public void setDokumenRef(String dokumenRef) {
        this.dokumenRef = dokumenRef;
    }

    public int getTahun() { return tahun; }

    public void setTahun(int tahun) { this.tahun = tahun; }

    public PeriodeTahunModel getPeriodeDokumenArsip() {
        return periodeDokumenArsip;
    }

    public void setPeriodeDokumenArsip(PeriodeTahunModel periodeDokumenArsip) {
        this.periodeDokumenArsip = periodeDokumenArsip;
    }

    public UserModel getOwnerDokumenArsip() {
        return ownerDokumenArsip;
    }

    public void setOwnerDokumenArsip(UserModel ownerDokumenArsip) {
        this.ownerDokumenArsip = ownerDokumenArsip;
    }

    public List<DokumenModel> getArsipDokumenList() {
        return arsipDokumenList;
    }

    public void setArsipDokumenList(List<DokumenModel> arsipDokumenList) {
        this.arsipDokumenList = arsipDokumenList;
    }
}