package propensi.GCG.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "role")
public class RoleModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "nama", nullable = false)
    private String nama;

    @OneToMany(mappedBy = "aclRole", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ACLModel> aclRoleList;

    @OneToMany(mappedBy = "userRole", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserModel> userRoleList;

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

    public List<ACLModel> getAclRoleList() {
        return aclRoleList;
    }

    public void setAclRoleList(List<ACLModel> aclRoleList) {
        this.aclRoleList = aclRoleList;
    }

    public List<UserModel> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List<UserModel> userRoleList) {
        this.userRoleList = userRoleList;
    }
}
