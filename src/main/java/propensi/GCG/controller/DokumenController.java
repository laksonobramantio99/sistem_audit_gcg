package propensi.GCG.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import propensi.GCG.model.*;
import propensi.GCG.payload.UploadFileResponse;
import propensi.GCG.service.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DokumenController {
    private static final Logger logger = LoggerFactory.getLogger(DokumenController.class);

    @Autowired
    private DokumenService dokumenService;

    @Autowired
    private SubmisiService submisiService;

    @Autowired
    DokumenArsipService dokumenArsipService;

    @Autowired
    private PeriodeTahunService periodeTahunService;

    @Autowired
    private LembarKerjaService lembarKerjaService;




    @PostMapping("/uploadFile/{idSubmisi}")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @PathVariable (value = "idSubmisi") long idSubmisi) {
        SubmisiModel submisi = submisiService.findById(idSubmisi);

        DokumenModel dokumenModel = dokumenService.storeFile(file,submisi, null);
//        dokumenModel.setSubmisiDokumen(submisiService.findById(idSubmisi));

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(dokumenModel.getId())
                .toUriString();
        return new UploadFileResponse(dokumenModel.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }

//    @PostMapping("/uploadMultipleFiles")
//    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
//        return Arrays.asList(files)
//                .stream()
//                .map(file -> uploadFile(file))
//                .collect(Collectors.toList());
//    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        // Load file from database
        DokumenModel dokumenModel = dokumenService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dokumenModel.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dokumenModel.getFileName() + "\"")
                .body(new ByteArrayResource(dokumenModel.getData()));
    }

    @GetMapping("/downloadArsip/{idArsip}")
    public ResponseEntity<Resource> downloadArsip(@PathVariable Long idArsip) {
        // Load file from database
        DokumenArsipModel dokumenArsip = dokumenArsipService.getById(idArsip);
        DokumenModel dokumenModel = dokumenService.getFileByArsipDokumen(dokumenArsip);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dokumenModel.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dokumenModel.getFileName() + "\"")
                .body(new ByteArrayResource(dokumenModel.getData()));
    }



}
