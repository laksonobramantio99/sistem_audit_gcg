package propensi.GCG.service;

import propensi.GCG.model.LembarKerjaModel;
import propensi.GCG.model.SubmisiModel;

public interface SubmisiService {
    SubmisiModel getAktifSubmisiBySubperspektif(LembarKerjaModel subperspektif);
    SubmisiModel createNilai(SubmisiModel submisiSebelumDinilai, LembarKerjaModel subperspektifDinilai);
    SubmisiModel simpanFeedback(SubmisiModel submisi);
    SubmisiModel addSubmisi(SubmisiModel submisiModel);
    SubmisiModel editSubmisi(SubmisiModel submisiModel);
    SubmisiModel findById(long id);
    SubmisiModel getAktifSubmisi(LembarKerjaModel subperspektif);


}
