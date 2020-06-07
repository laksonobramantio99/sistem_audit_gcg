package propensi.GCG.repository;

import com.sun.xml.fastinfoset.algorithm.BooleanEncodingAlgorithm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import propensi.GCG.model.LembarKerjaModel;
import propensi.GCG.model.PeriodeTahunModel;
import propensi.GCG.model.SubmisiModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface LembarKerjaDB extends JpaRepository<LembarKerjaModel, Long> {
    List<LembarKerjaModel> findByAspekAndActiveFlagAndTipe(String aspek, boolean activeFlag, String tipe);

    List<LembarKerjaModel> findByAspekAndActiveFlagAndTipeAndPeriodeLembarKerja(String aspek, boolean activeFlag, String tipe, PeriodeTahunModel periode);

    List<LembarKerjaModel> findByActiveFlagAndTipeAndPeriodeLembarKerja(boolean activeFlag, String tipe, PeriodeTahunModel periode);

    List<LembarKerjaModel> findByIndikatorAndPeriodeLembarKerjaAndTipe(String indikator, PeriodeTahunModel periode, String tipe);

    List<LembarKerjaModel> findByPerspektifAndPeriodeLembarKerjaAndTipe(String perspektif, PeriodeTahunModel periode, String tipe);

    List<LembarKerjaModel> findBySubperspektifAndPeriodeLembarKerjaAndTipe(String subperspektif, PeriodeTahunModel periode, String tipe);

    List<LembarKerjaModel> findBySubfaktorUjiAndPeriodeLembarKerjaAndTipe(String subfaktorUji, PeriodeTahunModel periode, String tipe);

    List<LembarKerjaModel> findByLembarKerjaParentAndTipeAndPeriodeLembarKerja(LembarKerjaModel lembarKerjaModel, PeriodeTahunModel periode, String tipe );

    LembarKerjaModel findByActiveFlagAndTipeAndPeriodeLembarKerjaAndNoUrut(boolean activeFlag, String tipe, PeriodeTahunModel periode, int no_urut);

    List<LembarKerjaModel> findByPeriodeLembarKerja(PeriodeTahunModel periodeTahunModel);

    LembarKerjaModel findByIdFixed(Long idfixed);
    List<LembarKerjaModel> findByLembarKerjaParentAndActiveFlag(LembarKerjaModel parent, boolean activeFlag);

    List<LembarKerjaModel> findByPeriodeLembarKerjaAndTipeAndActiveFlag(PeriodeTahunModel periode, String tipe, boolean activeFlag);
    LembarKerjaModel findByIdFixedAndPeriodeLembarKerja(Long idfixed, PeriodeTahunModel periodeTahunModel);

    List<LembarKerjaModel> findByPeriodeLembarKerjaAndTipeOrTipe(PeriodeTahunModel periodeTahunModel, String aspek, String subperspektif);


}
