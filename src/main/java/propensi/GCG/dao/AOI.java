package propensi.GCG.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.GCG.model.LembarKerjaModel;
import propensi.GCG.model.SubmisiModel;

@Repository
public interface AOI extends JpaRepository<LembarKerjaModel, Long> {
}
