package propensi.GCG.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.GCG.model.LembarKerjaModel;
import propensi.GCG.model.SubmisiModel;
import propensi.GCG.model.UserModel;
import propensi.GCG.repository.SubmisiDB;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubmisiServiceImpl implements SubmisiService {


    @Autowired
    SubmisiDB submisiDB;
    @Autowired
    UserService userService;

    @Override
    public SubmisiModel getAktifSubmisiBySubperspektif(LembarKerjaModel subperspektif) {
        List<SubmisiModel> listSubmisi = submisiDB.findSubmisiModelBySubmisiLembarKerjaAndActiveFlag(subperspektif, true);
        System.out.println("listSubmisi : " + listSubmisi.size());
        SubmisiModel submisi = listSubmisi.get(listSubmisi.size()-1);
        return submisi;
    }


   @Override
   public SubmisiModel createNilai(SubmisiModel submisiSebelumDinilai, LembarKerjaModel subperspektifInput) {
       UserModel user = userService.getCurrentUser();
       LocalDateTime currentDate = LocalDateTime.now();
       submisiSebelumDinilai.setNilai(subperspektifInput.getNilai());
       submisiSebelumDinilai.setEvaluatorSubmisi(user);
       submisiSebelumDinilai.setEvaluateDate(currentDate);
       submisiDB.save(submisiSebelumDinilai);
       return submisiSebelumDinilai;
   }

    @Override
    public SubmisiModel simpanFeedback(SubmisiModel submisi) {
        SubmisiModel submisiDinilai = submisiDB.findById(submisi.getId()).get();

        submisiDinilai.setKelebihan(submisi.getKelebihan());
        submisiDinilai.setKekurangan(submisi.getKekurangan());
        submisiDinilai.setReferensi(submisi.getReferensi());
        submisiDinilai.setHambatan(submisi.getHambatan());
        submisiDinilai.setRekomendasi(submisi.getRekomendasi());

        UserModel penilai = userService.getCurrentUser();
        submisiDinilai.setEvaluatorSubmisi(penilai);

        submisiDB.save(submisiDinilai);

        return submisiDinilai;
    }

    @Override
    public SubmisiModel addSubmisi(SubmisiModel submisiModel) {
        return submisiDB.save(submisiModel);
    }

    @Override
    public SubmisiModel editSubmisi(SubmisiModel submisiModel){
        return submisiDB.save(submisiModel);
    }

    @Override
    public SubmisiModel findById(long id) {
        Optional<SubmisiModel> sub = submisiDB.findById(id);
        if (sub.isPresent()) {
            return sub.get();
        }
        return null;
    }

    @Override
    public SubmisiModel getAktifSubmisi(LembarKerjaModel subperspektif) {
        List<SubmisiModel> listSubmisi = submisiDB.findSubmisiModelBySubmisiLembarKerjaAndActiveFlag(subperspektif, true);
        System.out.println("listSubmisi : " + listSubmisi.size());
        if (listSubmisi.isEmpty()) {
            return null;
        }
        SubmisiModel submisi = listSubmisi.get(listSubmisi.size()-1);
        return submisi;
    }

//    @Override
//    public SubmisiModel addSubmisi(SubmisiModel submisiModel) {
//
//    }
//
//    @Override
//    public SubmisiModel findById(long id) {
//
//    }

}
