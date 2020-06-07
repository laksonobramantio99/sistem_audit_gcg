package propensi.GCG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import propensi.GCG.model.LembarKerjaModel;
import propensi.GCG.model.MasterParameterModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface MasterParameterDB extends JpaRepository<MasterParameterModel, Long> {
    MasterParameterModel findById(long id);
    List<MasterParameterModel> findByParentParameter(MasterParameterModel masterParameterModel);
    List<MasterParameterModel> findByAspek(String aspek);
    List<MasterParameterModel> findByIndikator(String indikator);
    List<MasterParameterModel> findByPerspektif(String perspektif);
    List<MasterParameterModel> findBySubperspektif(String subperspektif);
    List<MasterParameterModel> findAllByTipe(String tipe);
    List<MasterParameterModel> findByAspekAndTipe(String aspek, String tipe);
    List<MasterParameterModel> findByIndikatorAndTipe(String indikator, String tipe);
    List<MasterParameterModel> findByPerspektifAndTipe(String perspektif, String subperspektif);
}
