package propensi.GCG.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.GCG.model.MasterParameterModel;
import propensi.GCG.repository.MasterParameterDB;

import java.util.List;

@Service
public class MasterParameterServiceImpl implements MasterParameterService{
    @Autowired
    MasterParameterDB masterParameterDB;

    @Override
    public void deleteParameter(MasterParameterModel masterParameterModel) {
        changeNoUrut(masterParameterModel);
        switch (masterParameterModel.getTipe()) {
            case "subfaktor_uji":
                masterParameterDB.delete(masterParameterModel);
            case "subperspektif":
                List<MasterParameterModel> listSubFaktor = getMasterParameterByParentParameter(masterParameterModel);
                if (listSubFaktor.size() != 0) {
                    for (MasterParameterModel subFaktor : listSubFaktor) {
                        masterParameterDB.delete(subFaktor);
                    }
                }
                masterParameterDB.delete(masterParameterModel);
                break;
            case "perspektif":
                List<MasterParameterModel> listSubperspektif = getMasterParameterByParentParameter(masterParameterModel);
                for (MasterParameterModel subperspektif : listSubperspektif) {
                    deleteParameter(subperspektif);
                }
                masterParameterDB.delete(masterParameterModel);
                break;
            case "indikator":
                List<MasterParameterModel> listPerspektif = getMasterParameterByParentParameter(masterParameterModel);
                for (MasterParameterModel perspektif : listPerspektif) {
                    deleteParameter(perspektif);
                }
                masterParameterDB.delete(masterParameterModel);
                break;
            case "aspek":
                List<MasterParameterModel> listIndikator = getMasterParameterByParentParameter(masterParameterModel);
                for (MasterParameterModel indikator : listIndikator) {
                    deleteParameter(indikator);
                }
                masterParameterDB.delete(masterParameterModel);
                break;
        }
    }

    @Override
    public List<MasterParameterModel> getAllMasterParameter() {
        return masterParameterDB.findAll();
    }

    @Override
    public MasterParameterModel getMasterParameterById(long id) {
        return masterParameterDB.findById(id);
    }

    @Override
    public List<MasterParameterModel> getMasterParameterByParentParameter(MasterParameterModel masterParameterModel) {
        return masterParameterDB.findByParentParameter(masterParameterModel);
    }

    @Override
    public List<MasterParameterModel> getMasterParameterByTipe(String tipe) {
        return masterParameterDB.findAllByTipe(tipe);
    }

    @Override
    public List<MasterParameterModel> getMasterParamaterByAspekandTipe(String aspek, String tipe) {
        return masterParameterDB.findByAspekAndTipe(aspek, tipe);
    }

    @Override
    public List<MasterParameterModel> getMasterParamaterByIndikatorandTipe(String indikator, String tipe) {
        return masterParameterDB.findByIndikatorAndTipe(indikator, tipe);
    }

    @Override
    public List<MasterParameterModel> getMasterParamaterByPerspektifandTipe(String perspektif, String tipe) {
        return masterParameterDB.findByPerspektifAndTipe(perspektif, tipe);
    }

    @Override
    public List<MasterParameterModel> getMasterParameterByAspek(String aspek) {
        return masterParameterDB.findByAspek(aspek);
    }

    @Override
    public List<MasterParameterModel> getMasterParameterByIndikator(String indikator) {
        return masterParameterDB.findByIndikator(indikator);
    }

    @Override
    public List<MasterParameterModel> getMasterParameterByPerspektif(String perspektif) {
        return masterParameterDB.findByPerspektif(perspektif);
    }

    @Override
    public List<MasterParameterModel> getMasterParameterBySubperspektif(String subperspektif) {
        return masterParameterDB.findBySubperspektif(subperspektif);
    }

    private void changeNoUrut(MasterParameterModel masterParameterModel){
        List<MasterParameterModel> listParameter = getMasterParameterByParentParameter(masterParameterModel.getParentParameter());
        int index = masterParameterModel.getNoUrut();
        for(int i = index; i < listParameter.size(); i++){
            listParameter.get(i).setNoUrut(listParameter.get(i).getNoUrut() - 1);
        }
    }
}
