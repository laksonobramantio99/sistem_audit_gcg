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
@Table(name = "user")
public class UserModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nama", nullable = false)
    private String nama;

    @NotNull
    @Size(max = 100)
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull
    @Lob
    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @NotNull
    @Column(name="activeFlag", nullable = false)
    private boolean activeFlag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idRole", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private RoleModel userRole;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idDivisi", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private DivisiModel userDivisi;

    @OneToMany(mappedBy = "uploaderSubmisi", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SubmisiModel> uploaderSubmisiList;

    @OneToMany(mappedBy = "evaluatorSubmisi", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SubmisiModel> evaluatorSubmisiList;

    @OneToMany(mappedBy = "ownerDokumenArsip", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DokumenArsipModel> ownerDokumenArsipList;

    @OneToMany(mappedBy = "creatorPeriode", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PeriodeTahunModel> creatorPeriodeList;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public RoleModel getUserRole() {
        return userRole;
    }

    public void setUserRole(RoleModel userRole) {
        this.userRole = userRole;
    }

    public DivisiModel getUserDivisi() {
        return userDivisi;
    }

    public void setUserDivisi(DivisiModel userDivisi) {
        this.userDivisi = userDivisi;
    }

    public List<SubmisiModel> getUploaderSubmisiList() {
        return uploaderSubmisiList;
    }

    public void setUploaderSubmisiList(List<SubmisiModel> uploaderSubmisiList) {
        this.uploaderSubmisiList = uploaderSubmisiList;
    }

    public List<SubmisiModel> getEvaluatorSubmisiList() {
        return evaluatorSubmisiList;
    }

    public void setEvaluatorSubmisiList(List<SubmisiModel> evaluatorSubmisiList) {
        this.evaluatorSubmisiList = evaluatorSubmisiList;
    }

    public List<DokumenArsipModel> getOwnerDokumenArsipList() {
        return ownerDokumenArsipList;
    }

    public void setOwnerDokumenArsipList(List<DokumenArsipModel> ownerDokumenArsipList) {
        this.ownerDokumenArsipList = ownerDokumenArsipList;
    }

    public List<PeriodeTahunModel> getCreatorPeriodeList() {
        return creatorPeriodeList;
    }

    public void setCreatorPeriodeList(List<PeriodeTahunModel> creatorPeriodeList) {
        this.creatorPeriodeList = creatorPeriodeList;
    }
}