package propensi.GCG.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import propensi.GCG.exception.FileStorageException;
import propensi.GCG.exception.MyFileNotFoundException;
import propensi.GCG.model.DokumenArsipModel;
import propensi.GCG.model.DokumenModel;
import propensi.GCG.model.SubmisiModel;
import propensi.GCG.repository.DokumenDB;

import java.io.IOException;

@Service
public class DokumenService {
    @Autowired
    private DokumenDB dokumenDB;

    public DokumenModel storeFile(MultipartFile file, SubmisiModel submisiModel, DokumenArsipModel dokumenArsipModel) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            DokumenModel dokumenModel = new DokumenModel(fileName, file.getContentType(), file.getBytes(), submisiModel, dokumenArsipModel);

            return dokumenDB.save(dokumenModel);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public DokumenModel getFile(String fileId) {
        return dokumenDB.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }

    public DokumenModel getFileByArsipDokumen(DokumenArsipModel dokumenArsip) {
        return dokumenDB.findByArsipDokumen(dokumenArsip);
    }

//    public DokumenModel getFileName(String name){
//        return dokumenDB.findByName(name).get();
//    }

    public DokumenModel storeArsipFile(MultipartFile file, SubmisiModel submisiModel, DokumenArsipModel dokumenArsipModel) {
        // Normalize file name
        String namaFile = file.getOriginalFilename();
        String slicing[] = namaFile.split("\\.");
        String fileName = dokumenArsipModel.getNama() + '.' + slicing[slicing.length-1];

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            DokumenModel dokumenModel = new DokumenModel(fileName, file.getContentType(), file.getBytes(), submisiModel, dokumenArsipModel);

            return dokumenDB.save(dokumenModel);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public void deleteDokumen(DokumenArsipModel dokumenArsip) {
        DokumenModel dokumen = dokumenDB.findByArsipDokumen(dokumenArsip);
        dokumenDB.delete(dokumen);
    }
}
