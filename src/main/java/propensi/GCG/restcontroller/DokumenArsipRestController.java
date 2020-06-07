package propensi.GCG.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import propensi.GCG.exception.FileStorageException;
import propensi.GCG.model.*;
import propensi.GCG.payload.UploadFileResponse;
import propensi.GCG.repository.DokumenDB;
import propensi.GCG.repository.PeriodeTahunDB;
import propensi.GCG.service.DokumenArsipService;
import propensi.GCG.service.DokumenService;
import propensi.GCG.service.PeriodeTahunService;
import propensi.GCG.service.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/arsip")
public class DokumenArsipRestController {
    @Autowired
    UserService userService;

    @Autowired
    PeriodeTahunService periodeTahunService;

    @Autowired
    DokumenService dokumenService;

    @Autowired
    DokumenArsipService dokumenArsipService;

    @Autowired
    DokumenDB dokumenDB;

    @Autowired
    PeriodeTahunDB periodeTahunDB;

    @PostMapping(value = "/tambah/{idPeriode}")
    public Map<String, Object> addArsip(@ModelAttribute DokumenArsipModel dokumenArsipModel,
                                        @PathVariable(value = "idPeriode") long id){
        LocalDateTime date = LocalDateTime.now();
        UserModel user = userService.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        PeriodeTahunModel periode = periodeTahunService.getPeriodeById(id);
        dokumenArsipModel.setDokumenRef("aoi");
        dokumenArsipModel.setPeriodeDokumenArsip(periode);
        dokumenArsipModel.setOwnerDokumenArsip(user);
        dokumenArsipModel.setNama("nama");
        dokumenArsipModel.setUploadDate(date);
        dokumenArsipModel.setTahun(periode.getTahun());
        DokumenArsipModel dokumenBaru = dokumenArsipService.addArsip(dokumenArsipModel);
        Map<String, Object> respon = new HashMap<String, Object>();
        respon.put("idArsip", dokumenBaru.getId());
        periode.setEnded(true);
        periodeTahunDB.save(periode);
        return respon;
    }

    @PostMapping("/upload/{idArsip}")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @PathVariable (value = "idArsip") long idArsip) {
        DokumenArsipModel arsip = dokumenArsipService.getById(idArsip);
        DokumenModel dokumen = dokumenDB.findByArsipDokumen(arsip);
        if(dokumen != null){
            String nama = StringUtils.cleanPath(file.getOriginalFilename());

            try{
            dokumen.setFileName(nama);
            dokumen.setFileType(file.getContentType());
            dokumen.setData(file.getBytes());
            dokumen.setSubmisiDokumen(null);
            dokumen.setArsipDokumen(arsip);
            dokumenDB.save(dokumen);
            }
            catch (IOException ex){
                throw new FileStorageException("Could not store file " + nama + ". Please try again!", ex);
            }
            String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(dokumen.getId())
                    .toUriString();
            return new UploadFileResponse(dokumen.getFileName(), uri,
                    file.getContentType(), file.getSize());
        }
        DokumenModel dokumenModel = dokumenService.storeFile(file,null, arsip);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(dokumenModel.getId())
                .toUriString();
        return new UploadFileResponse(dokumenModel.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping(value = "/tambah-arsip/{idPeriode}")
    public Map<String, Object> addDokumenArsip(@ModelAttribute DokumenArsipModel dokumenArsipModel,
                                        @PathVariable(value = "idPeriode") long idPeriode, @RequestParam String namaDokumen, @RequestParam int tahun){
        LocalDateTime date = LocalDateTime.now();
        UserModel user = userService.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        PeriodeTahunModel periode = periodeTahunService.getPeriodeById(idPeriode);
        dokumenArsipModel.setNama(namaDokumen);
        dokumenArsipModel.setTahun(tahun);
        dokumenArsipModel.setDokumenRef("arsip");
        dokumenArsipModel.setPeriodeDokumenArsip(periode);
        dokumenArsipModel.setOwnerDokumenArsip(user);
        dokumenArsipModel.setUploadDate(date);
        DokumenArsipModel dokumenBaru = dokumenArsipService.addArsip(dokumenArsipModel);
        Map<String, Object> respon = new HashMap<String, Object>();
        respon.put("idArsip", dokumenBaru.getId());
        return respon;
    }

    @PostMapping("/upload-arsip/{idArsip}")
    public UploadFileResponse uploadArsipFile(@RequestParam("file") MultipartFile file, @PathVariable (value = "idArsip") long idArsip) {
        DokumenArsipModel arsip = dokumenArsipService.getById(idArsip);
        DokumenModel dokumen = dokumenService.storeArsipFile(file,null, arsip);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(dokumen.getId())
                .toUriString();
        return new UploadFileResponse(dokumen.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @GetMapping(value = "/isAvailable/{namaDokumen}")
    private Map<String, Object> isAvailable(@PathVariable String namaDokumen) {
        DokumenArsipModel dokumenArsip = dokumenArsipService.findDokumenArsipByNamaDokumen(namaDokumen);
        boolean isAvailable = false;
        if (dokumenArsip != null) {
            isAvailable = true;
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("namaDokumen", namaDokumen);
        response.put("isAvailable", isAvailable);

        return response;
    }
}
