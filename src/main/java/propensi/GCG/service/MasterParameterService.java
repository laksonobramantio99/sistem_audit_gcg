package propensi.GCG.service;

import propensi.GCG.model.MasterParameterModel;

import java.util.List;

public interface MasterParameterService {
    void deleteParameter(MasterParameterModel masterParameterModel);
    MasterParameterModel getMasterParameterById(long id);
    List<MasterParameterModel> getAllMasterParameter();
    List<MasterParameterModel> getMasterParameterByParentParameter(MasterParameterModel masterParameterModel);
    List<MasterParameterModel> getMasterParameterByAspek(String aspek);
    List<MasterParameterModel> getMasterParameterByIndikator(String indikator);
    List<MasterParameterModel> getMasterParameterByPerspektif(String perspektif);
    List<MasterParameterModel> getMasterParameterBySubperspektif(String subperspektif);
    List<MasterParameterModel> getMasterParameterByTipe(String tipe);
    List<MasterParameterModel> getMasterParamaterByAspekandTipe(String aspek, String tipe);
    List<MasterParameterModel> getMasterParamaterByIndikatorandTipe(String indikator, String tipe);
    List<MasterParameterModel> getMasterParamaterByPerspektifandTipe(String perspektif, String tipe);
}
