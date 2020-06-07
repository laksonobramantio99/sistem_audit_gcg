package propensi.GCG.myapachepoi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import propensi.GCG.dao.AOI;
import propensi.GCG.model.LembarKerjaModel;
import propensi.GCG.model.SubmisiModel;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.SpreadsheetVersion;
//import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import propensi.GCG.service.SubmisiService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Iterator;
import java.util.List;
@Component
public class ExcelGenerator {

    @Autowired
    private AOI aoi;

    @Autowired
    private SubmisiService submisiService;

    /* export */
    public ByteArrayInputStream exportExcel(List<LembarKerjaModel> listLK, Integer tahun) throws Exception{
        String[] columns = {"Id", "Aspek", "Subperspektif", "Hambatan", "Kekurangan", "Kelebihan", "Referensi", "Rekomendasi",
        "Tertimbang", "Nilai"};
        System.out.println("sebelum try");
//        try(
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
//        )
        {

            CreationHelper creationHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Data AOI");
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            //Row ofor Header
            Row headerRow = sheet.createRow(0);

            //Header
            for(int i=0;i<columns.length;i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIdx = 1;
            for(LembarKerjaModel lk : listLK) {
                Row row = sheet.createRow(rowIdx);

                if (lk.getTipe().equals("subperspektif")) {
                    SubmisiModel submisi = submisiService.getAktifSubmisi(lk);
                    if (submisi == null) {
                        continue;
                    }
                    row.createCell(0).setCellValue(lk.getId());
                    row.createCell(1).setCellValue(lk.getAspek());
                    row.createCell(2).setCellValue(lk.getSubperspektif());
                    if (submisi.getHambatan() == null){
                        row.createCell(3).setCellValue("");
                    }
                    else{
                        row.createCell(3).setCellValue(submisi.getHambatan().replaceAll("\\<.*?>",""));
                    }

                    if (submisi.getKekurangan() == null){
                        row.createCell(4).setCellValue("");
                    }
                    else{
                        row.createCell(4).setCellValue(submisi.getKekurangan().replaceAll("\\<.*?>",""));
                    }

                    if (submisi.getKelebihan() == null){
                        row.createCell(5).setCellValue("");
                    }
                    else{
                        row.createCell(5).setCellValue(submisi.getKelebihan().replaceAll("\\<.*?>",""));
                    }

                    if (submisi.getReferensi() == null){
                        row.createCell(6).setCellValue("");
                    }
                    else{
                        row.createCell(6).setCellValue(submisi.getReferensi().replaceAll("\\<.*?>",""));
                    }

                    if (submisi.getRekomendasi() == null){
                        row.createCell(7).setCellValue("");
                    }
                    else{
                        row.createCell(7).setCellValue(submisi.getRekomendasi().replaceAll("\\<.*?>",""));
                    }
                    if (submisi.getNilai() != null) {
                        row.createCell(9).setCellValue(submisi.getNilai());
                    }



                }
                else { // aspek
                    row.createCell(0).setCellValue(lk.getId());
                    row.createCell(1).setCellValue(lk.getAspek());
                    if (lk.getTertimbang() != null ) {
                        row.createCell(8).setCellValue(lk.getTertimbang());
                    }
                    if (lk.getNilai() != null ) {
                        row.createCell(9).setCellValue(lk.getNilai());
                    }
                }

                rowIdx++;
            }

            workbook.write(out);
            workbook.close();

            return new ByteArrayInputStream(out.toByteArray());
//        }catch(Exception e) {
//            System.out.println(e);
//        }
//        return null;
    }

    /* Import */
//    public void importExcel(MultipartFile file) throws Exception{
//
//        Workbook workbook = new XSSFWorkbook(file.getInputStream());
//        Sheet sheet = workbook.getSheetAt(0);
//
//        for(int i=0;i<(CoutRowExcel(sheet.rowIterator()));i++) {
//            if(i == 0) {
//                continue;
//            }
//
//            Row row = sheet.getRow(i);
//
//            String hambatan = row.getCell(1).getStringCellValue();
//            String kekurangan = row.getCell(2).getStringCellValue();
//            String kelebihan = row.getCell(3).getStringCellValue();
//            String referensi = row.getCell(4).getStringCellValue();
//            String rekomendasi = row.getCell(5).getStringCellValue();
//
//            aoi.save(new SubmisiModel(0, hambatan,kekurangan, kelebihan, referensi, rekomendasi ));
//        }
//
//    }

    /* Cout Row of Excel Table */
//    public static int CoutRowExcel(Iterator<Row> iterator) {
//        int size = 0;
//        while(iterator.hasNext()) {
//            Row row = iterator.next();
//            size++;
//        }
//        return size;
//    }
}}
