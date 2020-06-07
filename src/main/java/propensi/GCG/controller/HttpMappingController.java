package propensi.GCG.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import propensi.GCG.myapachepoi.ExcelGenerator;

@Controller
public class HttpMappingController {

    @Autowired
    private ExcelGenerator excel;

    @GetMapping("/import")
    public String creteGetImport() {

        return "import";
    }

//    @PostMapping("/import")
//    public String cretePostImport(@RequestParam(name = "file") MultipartFile file) throws Exception {
//
//        excel.importExcel(file);
//
//        return "redirect:/import";
//    }

}