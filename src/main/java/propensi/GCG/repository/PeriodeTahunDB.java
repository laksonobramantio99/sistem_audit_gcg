package propensi.GCG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import propensi.GCG.model.LembarKerjaModel;
import propensi.GCG.model.PeriodeTahunModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodeTahunDB extends JpaRepository<PeriodeTahunModel, Long> {
    PeriodeTahunModel findByTahunAndActiveFlag(int tahun, boolean active_flag);
    List<PeriodeTahunModel> findByActiveFlag(boolean active_flag);
    List<PeriodeTahunModel> findByActiveFlagAndIsEnded(boolean active_flag, boolean is_ended);
    Optional<PeriodeTahunModel> findById(Long id);
}
