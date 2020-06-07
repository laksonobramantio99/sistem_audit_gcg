package propensi.GCG.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "divisi")
public class DivisiModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "nama", nullable = false)
    private String nama;

    @OneToMany(mappedBy = "userDivisi", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserModel> userDivisiList;

    @OneToMany(mappedBy = "delegasiDivisi", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DelegasiModel> delegasiDivisiList;

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

    public List<UserModel> getUserDivisiList() {
        return userDivisiList;
    }

    public void setUserDivisiList(List<UserModel> userDivisiList) {
        this.userDivisiList = userDivisiList;
    }

    public List<DelegasiModel> getDelegasiDivisiList() {
        return delegasiDivisiList;
    }

    public void setDelegasiDivisiList(List<DelegasiModel> delegasiDivisiList) {
        this.delegasiDivisiList = delegasiDivisiList;
    }
}