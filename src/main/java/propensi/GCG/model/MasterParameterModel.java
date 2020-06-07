package propensi.GCG.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "masterParameter")
public class MasterParameterModel implements Serializable {

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

    @NotNull
    @Column(name = "activeFlag")
    private Boolean activeFlag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idParent", referencedColumnName = "idFixed")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private MasterParameterModel parentParameter;

    @OneToMany(mappedBy = "parentParameter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MasterParameterModel> parentParameterList;

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

    public Boolean getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public MasterParameterModel getParentParameter() {
        return parentParameter;
    }

    public void setParentParameter(MasterParameterModel parentParameter) {
        this.parentParameter = parentParameter;
    }

    public List<MasterParameterModel> getParentParameterList() {
        return parentParameterList;
    }

    public void setParentParameterList(List<MasterParameterModel> parentParameterList) {
        this.parentParameterList = parentParameterList;
    }
}