package propensi.GCG.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.GCG.model.*;
import propensi.GCG.repository.LembarKerjaDB;
import propensi.GCG.repository.SubmisiDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LembarKerjaServiceImpl implements LembarKerjaService {
    @Autowired
    LembarKerjaDB lembarKerjaDB;

    @Autowired
    SubmisiDB submisiDB;

    @Autowired
    MasterParameterService masterParameterService;

    @Autowired
    PeriodeTahunService periodeTahunService;

    @Override
    public List<LembarKerjaModel> getListPerspektifByAspek(PeriodeTahunModel periode, String aspek) {
        return lembarKerjaDB.findByAspekAndActiveFlagAndTipeAndPeriodeLembarKerja(aspek, true, "perspektif", periode);
    }

    @Override
    public List<LembarKerjaModel> getListAspek(PeriodeTahunModel periode) {
        return lembarKerjaDB.findByActiveFlagAndTipeAndPeriodeLembarKerja(true, "aspek", periode);
    }
//
    @Override
    public Optional<LembarKerjaModel> getLembarKerjaByIdFixed(Long id) {
       return lembarKerjaDB.findById(id);
    }
//
//    @Override
//    public List<LembarKerjaModel> getPerspektifByAspek(String aspek) {
//        return lembarKerjaDB.findPerspektifByAspekAndActiveFlag(aspek, true);
//    }

    @Override
    public LembarKerjaModel getLembarKerja(PeriodeTahunModel periode, int no_aspek){
        return lembarKerjaDB.findByActiveFlagAndTipeAndPeriodeLembarKerjaAndNoUrut(true, "aspek", periode, no_aspek);
    }

    @Override
    public List<LembarKerjaModel> getListIndikatorByAspek(PeriodeTahunModel periode, String aspek){
        return lembarKerjaDB.findByAspekAndActiveFlagAndTipeAndPeriodeLembarKerja(aspek, true, "indikator", periode);
    }

    @Override
    public List<LembarKerjaModel> getListPerspektifByIndikator(PeriodeTahunModel periode, String indikator){
        return lembarKerjaDB.findByIndikatorAndPeriodeLembarKerjaAndTipe(indikator, periode, "perspektif");
    }

    @Override
    public List<LembarKerjaModel> getListSubperspektifByPerspektif(PeriodeTahunModel periode, String perspektif){
        return lembarKerjaDB.findByPerspektifAndPeriodeLembarKerjaAndTipe(perspektif, periode, "subperspektif");
    }

    @Override
    public List<LembarKerjaModel> getListSubfaktorUjiBySubperspektif(PeriodeTahunModel periode, String subperspektif){
        return lembarKerjaDB.findBySubperspektifAndPeriodeLembarKerjaAndTipe(subperspektif, periode, "subfaktor_uji");
    }

    @Override
    public List<LembarKerjaModel> getListIndikatorBerdasarkanDelegasiDanAspek(UserModel user, LembarKerjaModel aspek, PeriodeTahunModel periode) {
        List<LembarKerjaModel>  listIndikator = new ArrayList<>();
        DivisiModel divisi = user.getUserDivisi();

        List<DelegasiModel> listDelegasiDivsi = divisi.getDelegasiDivisiList();
        for (DelegasiModel delegasi : listDelegasiDivsi){
            if (delegasi.getDelegasiLembarKerja().getPeriodeLembarKerja() == periode
            && delegasi.isActiveFlag()){
                LembarKerjaModel perspektif = delegasi.getDelegasiLembarKerja();
                LembarKerjaModel indikator = perspektif.getLembarKerjaParent();
                if(perspektif.getAspek().equals(aspek.getAspek())){
                    if (!listIndikator.contains(indikator)){
                        listIndikator.add(indikator);
                    }
                }
            }
        }

        return listIndikator;
    }

    @Override
    public List<Double> getPersentaseSubperspektifTanpaDokumen(LembarKerjaModel aspek) {
        Double jumlahPerspektifDilengkapi = Double.valueOf(0);
        List<LembarKerjaModel> listSubperspektifByAspek =
                lembarKerjaDB.findByAspekAndActiveFlagAndTipeAndPeriodeLembarKerja(aspek.getAspek(), true, "subperspektif", aspek.getPeriodeLembarKerja());
        for (LembarKerjaModel subperspektif : listSubperspektifByAspek){
            List<SubmisiModel> listSubmisi = subperspektif.getSubmisiLembarKerjaList();
            if (!listSubmisi.isEmpty()){
                jumlahPerspektifDilengkapi++;
            }
        }
        System.out.println("dokumen lengkap : " + jumlahPerspektifDilengkapi + " semua : " +  listSubperspektifByAspek.size());
        List<Double> persentase = new ArrayList<>();
        persentase.add(jumlahPerspektifDilengkapi);
        persentase.add((double) listSubperspektifByAspek.size());
        if (jumlahPerspektifDilengkapi == (double) 0){
            persentase.add((double) 0);
        }
        else {
            persentase.add(jumlahPerspektifDilengkapi/listSubperspektifByAspek.size());
        }
        return persentase;
    }

    @Override
    public void addLembarKerja(PeriodeTahunModel periodeTahunModel) {
        List<MasterParameterModel> masterParameterModel = masterParameterService.getAllMasterParameter();
        List<Long> idParent = new ArrayList<Long>();
        for(MasterParameterModel parameter : masterParameterModel){
            try {
                idParent.add(parameter.getParentParameter().getIdFixed());
            }
            catch (NullPointerException e){
                idParent.add(null);
            }
        }

        for(MasterParameterModel parameter : masterParameterModel){
            LembarKerjaModel lembarKerjaBaru = new LembarKerjaModel();
            lembarKerjaBaru.setIdFixed(parameter.getIdFixed());
            lembarKerjaBaru.setTipe(parameter.getTipe());
            lembarKerjaBaru.setNoUrut(parameter.getNoUrut());
            lembarKerjaBaru.setAspek(parameter.getAspek());
            lembarKerjaBaru.setIndikator(parameter.getIndikator());
            lembarKerjaBaru.setPerspektif(parameter.getPerspektif());
            lembarKerjaBaru.setSubperspektif(parameter.getSubperspektif());
            lembarKerjaBaru.setSubfaktorUji(parameter.getSubfaktorUji());
            lembarKerjaBaru.setBobot(parameter.getBobot());
            lembarKerjaBaru.setActiveFlag(parameter.getActiveFlag());
            lembarKerjaBaru.setPeriodeLembarKerja(periodeTahunModel);
            lembarKerjaDB.save(lembarKerjaBaru);
        }
        List<LembarKerjaModel> lembarKerja = lembarKerjaDB.findByPeriodeLembarKerja(periodeTahunModel);
        for (int i = 0; i < lembarKerja.size(); i++){
            try{
                lembarKerja.get(i).setLembarKerjaParent(lembarKerjaDB.findByIdFixedAndPeriodeLembarKerja(idParent.get(i), periodeTahunModel));
            }
            catch (NullPointerException e){
                lembarKerja.get(i).setLembarKerjaParent(null);
            }
            lembarKerjaDB.save(lembarKerja.get(i));
        }
    }

    @Override
    public LembarKerjaModel getSubperspektifById(long idSubperspektif) {
        return lembarKerjaDB.findById(idSubperspektif).get();
    }

    @Override
    public LembarKerjaModel createNilai(LembarKerjaModel subperspektifInput) {
        LembarKerjaModel existingLembarKerjaModel = lembarKerjaDB.findById(subperspektifInput.getId()).get();
        existingLembarKerjaModel.setNilai(subperspektifInput.getNilai());
        lembarKerjaDB.save(existingLembarKerjaModel);
        return existingLembarKerjaModel;
    }



    @Override
    public LembarKerjaModel perbaharuiNilai(LembarKerjaModel parameterYangDikalkulasi) {

        List<LembarKerjaModel> listchild = getListChildLembarKerjaByParent(parameterYangDikalkulasi);
        Double totalNilaiChildParameter = Double.valueOf(0);
        Double jumlahChild = Double.valueOf(listchild.size());

        System.out.println("paramaeter yang d kalkulasi : " + parameterYangDikalkulasi.getId() + " " + parameterYangDikalkulasi.getTipe());
        if(parameterYangDikalkulasi.getTipe().equals("perspektif") || parameterYangDikalkulasi.getTipe().equals("subperspektif")){
            for (LembarKerjaModel child : listchild){
                if (child.getNilai() != null){
                    totalNilaiChildParameter+=child.getNilai();
                }
            }

            Double nilai = totalNilaiChildParameter/jumlahChild;

            parameterYangDikalkulasi.setNilai(nilai);
            parameterYangDikalkulasi.setTertimbang(nilai*parameterYangDikalkulasi.getBobot());

            lembarKerjaDB.save(parameterYangDikalkulasi);
            return parameterYangDikalkulasi;
        }

        else{
            for (LembarKerjaModel child : listchild){
                if (child.getTertimbang() != null){
                    totalNilaiChildParameter+=child.getTertimbang();
                }
            }

            Double nilai = totalNilaiChildParameter/parameterYangDikalkulasi.getBobot();

            parameterYangDikalkulasi.setNilai(nilai);
            parameterYangDikalkulasi.setTertimbang(totalNilaiChildParameter);

            lembarKerjaDB.save(parameterYangDikalkulasi);
            System.out.println("periode : " + parameterYangDikalkulasi.getPeriodeLembarKerja());

            PeriodeTahunModel periode = periodeTahunService.kalkulasiTotalCapaian(parameterYangDikalkulasi.getPeriodeLembarKerja());
            return parameterYangDikalkulasi;
        }

    }

    @Override
    public List<LembarKerjaModel> getListChildLembarKerjaByParent(LembarKerjaModel parent) {
        return parent.getLembarKerjaParentList();
    }

    @Override
    public List<LembarKerjaModel> getListPerspektif(PeriodeTahunModel periode) {
        return lembarKerjaDB.findByPeriodeLembarKerjaAndTipeAndActiveFlag(periode, "perspektif", true);
    }

    @Override
    public LembarKerjaModel setNilaiNull(LembarKerjaModel subperspektif) {
        subperspektif.setNilai(null);
        subperspektif.setTertimbang(null);
        lembarKerjaDB.save(subperspektif);
        return subperspektif;
    }

    @Override
    public List<LembarKerjaModel> getSubperspektifAktif(Integer tahun) {
        PeriodeTahunModel currentPeriode = periodeTahunService.getPeriodeByTahun(tahun);
        return lembarKerjaDB.findByPeriodeLembarKerjaAndTipeOrTipe(currentPeriode, "aspek", "subperspektif");
    }

}
