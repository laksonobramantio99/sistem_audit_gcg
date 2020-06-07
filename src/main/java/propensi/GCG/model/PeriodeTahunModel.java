package propensi.GCG.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "periodeTahun")
public class PeriodeTahunModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "tahun", nullable = false)
    private int tahun;

    @NotNull
    @Column(name="activeFlag", nullable = false)
    private boolean activeFlag;

    @NotNull
    @Column(name = "createdDate", nullable = false, columnDefinition = "DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;

    @NotNull
    @Column(name="isEnded", nullable = false)
    private boolean isEnded;

    @Column(name="capaian")
    private Double capaian;

    @OneToMany(mappedBy = "periodeLembarKerja", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LembarKerjaModel> periodeLembarKerjaList;

    @OneToMany(mappedBy = "periodeDokumenArsip", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DokumenArsipModel> periodeDokumenArsipList;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idCreator", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserModel creatorPeriode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTahun() {
        return tahun;
    }

    public void setTahun(int tahun) {
        this.tahun = tahun;
    }

    public boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public boolean getIsEnded() {
        return isEnded;
    }
    public void setEnded(boolean ended) {
        isEnded = ended;
    }

    public List<LembarKerjaModel> getPeriodeLembarKerjaList() {
        return periodeLembarKerjaList;
    }

    public void setPeriodeLembarKerjaList(List<LembarKerjaModel> periodeLembarKerjaList) {
        this.periodeLembarKerjaList = periodeLembarKerjaList;
    }

    public List<DokumenArsipModel> getPeriodeDokumenArsipList() {
        return periodeDokumenArsipList;
    }

    public void setPeriodeDokumenArsipList(List<DokumenArsipModel> periodeDokumenArsipList) {
        this.periodeDokumenArsipList = periodeDokumenArsipList;
    }

    public UserModel getCreatorPeriode() {
        return creatorPeriode;
    }

    public void setCreatorPeriode(UserModel creatorPeriode) {
        this.creatorPeriode = creatorPeriode;
    }

    public Double getCapaian() {
        return capaian;
    }

    public void setCapaian(Double capaian) {
        this.capaian = capaian;
    }

}
