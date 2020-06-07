package propensi.GCG.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "lembarKerja")
public class LembarKerjaModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "idFixed", nullable = false)
    private Long idFixed;

    @NotNull
    @Size(max = 30)
    @Column(name = "tipe" , nullable = false)
    private String tipe;

    @NotNull
    @Column(name = "noUrut", nullable = false)
    private Integer noUrut;

    @Column(name = "aspek")
    private String aspek;

    @Column(name = "indikator")
    private String indikator;

    @Column(name = "perspektif")
    private String perspektif;

    @Column(name = "subperspektif")
    private String subperspektif;

    @Column(name = "subfaktorUji")
    private String subfaktorUji;

    @NotNull
    @Column(name = "bobot")
    private Double bobot;

    @Column(name = "nilai")
    @Value("${lembarkerja.nilai:#{null}}")
    private Double nilai;

    @NotNull
    @Column(name = "activeFlag")
    private Boolean activeFlag;

    @Column(name = "tertimbang")
    @Value("${lembarkerja.tertimbang:#{null}}")
    private Double tertimbang;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idPeriode", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private PeriodeTahunModel periodeLembarKerja;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idParent", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private LembarKerjaModel lembarKerjaParent;

    @OneToMany(mappedBy = "submisiLembarKerja", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SubmisiModel> submisiLembarKerjaList;

    @OneToMany(mappedBy = "lembarKerjaParent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LembarKerjaModel> lembarKerjaParentList;

    @OneToMany(mappedBy = "delegasiLembarKerja", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DelegasiModel> delegasiLembarKerjaList;

    // setter getter -

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdFixed() {
        return idFixed;
    }

    public void setIdFixed(Long idFixed) {
        this.idFixed = idFixed;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public Integer getNoUrut() {
        return noUrut;
    }

    public void setNoUrut(Integer noUrut) {
        this.noUrut = noUrut;
    }

    public String getAspek() {
        return aspek;
    }

    public void setAspek(String aspek) {
        this.aspek = aspek;
    }

    public String getIndikator() {
        return indikator;
    }

    public void setIndikator(String indikator) {
        this.indikator = indikator;
    }

    public String getPerspektif() {
        return perspektif;
    }

    public void setPerspektif(String perspektif) {
        this.perspektif = perspektif;
    }

    public String getSubperspektif() {
        return subperspektif;
    }

    public void setSubperspektif(String subperspektif) {
        this.subperspektif = subperspektif;
    }

    public String getSubfaktorUji() {
        return subfaktorUji;
    }

    public void setSubfaktorUji(String subfaktorUji) {
        this.subfaktorUji = subfaktorUji;
    }

    public Double getBobot() {
        return bobot;
    }

    public void setBobot(Double bobot) {
        this.bobot = bobot;
    }

    public Double getNilai() {
        return nilai;
    }

    public void setNilai(Double nilai) {
        this.nilai = nilai;
    }

    public Boolean getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public PeriodeTahunModel getPeriodeLembarKerja() {
        return periodeLembarKerja;
    }

    public void setPeriodeLembarKerja(PeriodeTahunModel periodeLembarKerja) {
        this.periodeLembarKerja = periodeLembarKerja;
    }

    public LembarKerjaModel getLembarKerjaParent() {
        return lembarKerjaParent;
    }

    public void setLembarKerjaParent(LembarKerjaModel lembarKerjaParent) {
        this.lembarKerjaParent = lembarKerjaParent;
    }

    public List<SubmisiModel> getSubmisiLembarKerjaList() {
        return submisiLembarKerjaList;
    }

    public void setSubmisiLembarKerjaList(List<SubmisiModel> submisiLembarKerjaList) {
        this.submisiLembarKerjaList = submisiLembarKerjaList;
    }

    public List<LembarKerjaModel> getLembarKerjaParentList() {
        return lembarKerjaParentList;
    }

    public void setLembarKerjaParentList(List<LembarKerjaModel> lembarKerjaParentList) {
        this.lembarKerjaParentList = lembarKerjaParentList;
    }

    public List<DelegasiModel> getDelegasiLembarKerjaList() {
        return delegasiLembarKerjaList;
    }

    public void setDelegasiLembarKerjaList(List<DelegasiModel> delegasiLembarKerjaList) {
        this.delegasiLembarKerjaList = delegasiLembarKerjaList;
    }

    public Double getTertimbang() {
        return tertimbang;
    }

    public void setTertimbang(Double tertimbang) {
        this.tertimbang = tertimbang;
    }
}