package propensi.GCG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.GCG.model.LembarKerjaModel;
import propensi.GCG.model.SubmisiModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmisiDB extends JpaRepository<SubmisiModel, Long> {
    List<SubmisiModel> findSubmisiModelBySubmisiLembarKerja(LembarKerjaModel subperspektif);
    List<SubmisiModel> findSubmisiModelBySubmisiLembarKerjaAndActiveFlag(LembarKerjaModel subperspektif, boolean activeFlag);
    Optional<SubmisiModel> findById (long id);
}
