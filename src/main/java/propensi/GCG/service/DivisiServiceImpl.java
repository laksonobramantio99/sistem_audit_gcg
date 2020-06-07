package propensi.GCG.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.GCG.model.DivisiModel;
import propensi.GCG.repository.DivisiDB;

import java.util.List;
import java.util.Optional;

@Service
public class DivisiServiceImpl implements DivisiService {
    @Autowired
    DivisiDB divisiDB;

    @Override
    public List<DivisiModel> getDivisiList() {
        return divisiDB.findAll();
    }

    @Override
    public Optional<DivisiModel> getDivisiByIdDivisi(Long id) {
        return divisiDB.findById(id);
    }

    @Override
    public DivisiModel addDivisi(DivisiModel divisiModel) {
        return divisiDB.save(divisiModel);
    }

    @Override
    public void deleteDivisi(DivisiModel divisiModel) {
        divisiDB.delete(divisiModel);
    }
}
