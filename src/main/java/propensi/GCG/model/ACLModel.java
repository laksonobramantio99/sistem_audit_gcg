package propensi.GCG.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "ACL")

public class ACLModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idRole", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private RoleModel aclRole;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idMenu", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private MenuModel aclMenu;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RoleModel getAclRole() {
        return aclRole;
    }

    public void setAclRole(RoleModel aclRole) {
        this.aclRole = aclRole;
    }

    public MenuModel getAclMenu() {
        return aclMenu;
    }

    public void setAclMenu(MenuModel aclMenu) {
        this.aclMenu = aclMenu;
    }
}
