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
@Table(name = "Menu")
public class MenuModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "nama", nullable = false)
    @Size(max = 40)
    private String nama;

    @Column(name = "link")
    private String link;

    @NotNull
    @Column(name = "isParent", nullable = false)
    private Boolean isParent;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "idParent", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private MenuModel parentMenu;

    @OneToMany(mappedBy = "parentMenu", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MenuModel> menuList;

    @OneToMany(mappedBy = "aclMenu", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ACLModel> aclMenuList;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public MenuModel getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(MenuModel parentMenu) {
        this.parentMenu = parentMenu;
    }

    public List<MenuModel> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuModel> menuList) {
        this.menuList = menuList;
    }

    public List<ACLModel> getAclMenuList() {
        return aclMenuList;
    }

    public void setAclMenuList(List<ACLModel> aclMenuList) {
        this.aclMenuList = aclMenuList;
    }
}
