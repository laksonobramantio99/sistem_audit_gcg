package propensi.GCG.model.wrapper;

import propensi.GCG.model.LembarKerjaModel;

import java.util.List;

public class DaftarLembarKerja {
    private List<LembarKerjaModel> daftarLembarKerja;

    public void addLembarKerja(LembarKerjaModel lembarKerjaModel) {
        this.daftarLembarKerja.add(lembarKerjaModel);
    }

    public List<LembarKerjaModel> getDaftarLembarKerja() {
        return daftarLembarKerja;
    }

    public void setDaftarLembarKerja(List<LembarKerjaModel> daftarLembarKerja) {
        this.daftarLembarKerja = daftarLembarKerja;
    }


}
