package propensi.GCG.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import propensi.GCG.dao.AOI;
import propensi.GCG.model.LembarKerjaModel;
import propensi.GCG.model.PeriodeTahunModel;
import propensi.GCG.model.SubmisiModel;
import propensi.GCG.myapachepoi.ExcelGenerator;
import propensi.GCG.service.LembarKerjaService;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
public class HttpMappingRestController {

    private propensi.GCG.dao.AOI AOI;
    private ExcelGenerator excel;

    @Autowired
    public HttpMappingRestController(AOI AOI, ExcelGenerator excel) {
        this.AOI = AOI;
        this.excel = excel;
    }

    @Autowired
    public LembarKerjaService lembarKerjaService;


    @GetMapping("/export/{tahun}")
    public ResponseEntity<InputStreamResource> excelAOIReport(@PathVariable(value = "tahun") Integer tahun) throws Exception {
//        List<SubmisiModel> aoiAll = AOI.findAll();
        List<LembarKerjaModel> listLK = lembarKerjaService.getSubperspektifAktif(tahun);

        ByteArrayInputStream in = excel.exportExcel(listLK, tahun);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=AOI_" + tahun + ".xlsx");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));

    }


}
