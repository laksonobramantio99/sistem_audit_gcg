package propensi.GCG.service;

import propensi.GCG.model.DivisiModel;

import java.util.List;
import java.util.Optional;

public interface DivisiService {
    List<DivisiModel> getDivisiList();
    Optional<DivisiModel> getDivisiByIdDivisi(Long id);
    DivisiModel addDivisi(DivisiModel divisiModel);
    void deleteDivisi(DivisiModel divisiModel);
}
