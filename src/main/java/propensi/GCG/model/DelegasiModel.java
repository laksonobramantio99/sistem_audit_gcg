package propensi.GCG.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "delegasi")
public class DelegasiModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "activeFlag", nullable = false)
    private boolean activeFlag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idDivisi", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private DivisiModel delegasiDivisi;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idLembarKerja", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private LembarKerjaModel delegasiLembarKerja;

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

    public DivisiModel getDelegasiDivisi() {
        return delegasiDivisi;
    }

    public void setDelegasiDivisi(DivisiModel delegasiDivisi) {
        this.delegasiDivisi = delegasiDivisi;
    }

    public LembarKerjaModel getDelegasiLembarKerja() {
        return delegasiLembarKerja;
    }

    public void setDelegasiLembarKerja(LembarKerjaModel delegasiLembarKerja) {
        this.delegasiLembarKerja = delegasiLembarKerja;
    }
}