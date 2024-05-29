package Reports;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

import java.io.IOException;
import java.util.Map;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.Cell;

public class Report {

    String file_name_report;
    int type_of_report;
    String file_name_sheet;
    public Report(int type_of_report, String file_name_sheet){
        this.file_name_report = "Reports.xls";
        this.type_of_report = type_of_report;
        this.file_name_sheet = file_name_sheet;
    }

    public void createReport(Map<String, String> data){
        WritableWorkbook workbook = null;
        Workbook existingWorkbook = null;

        Path currentPath = Paths.get(System.getProperty("user.dir"));
        File folder = new File(currentPath.toFile(), "Reports_data");

        if(!folder.exists()){
            folder.mkdir();
            System.out.println("Carpeta no creada o no encontrada, carpeta creada");
        }

        try {
            File file = new File(folder, this.file_name_report);

            if (file.exists()) {
                existingWorkbook = Workbook.getWorkbook(file);
                workbook = Workbook.createWorkbook(file, existingWorkbook);
            } else {
                workbook = Workbook.createWorkbook(file);
            }


            // Check if the sheet exists
            WritableSheet sheet = null;
            String[] sheetNames = workbook.getSheetNames();
            for (String name : sheetNames) {
                if (name.equals(this.file_name_sheet)) {
                    sheet = workbook.getSheet(name);
                    break;
                }
            }

            // If the sheet doesn't exist, create a new one
            if (sheet == null) {
                sheet = workbook.createSheet(this.file_name_sheet, workbook.getNumberOfSheets());
            }

            // Find the next empty row or the last row with content
            int lastRow = sheet.getRows();

            sheet.setColumnView(0, 50);
            sheet.setColumnView(1, 50);

            addToSheet(sheet, lastRow, data);

            workbook.write();
            workbook.close();
        } catch (IOException | WriteException | BiffException e){
            System.out.println("No se pudo crear el reporte");
            e.printStackTrace();
        }

    }

    private static void addToSheet(WritableSheet sheet, int startRow, Map<String, String> data) throws WriteException {
        int row = startRow;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            sheet.addCell(new Label(0, row, entry.getKey()));
            sheet.addCell(new Label(1, row, entry.getValue()));
            row++;
        }

        // Optionally, add an empty row at the end
        sheet.addCell(new Label(0, row, ""));
        sheet.addCell(new Label(1, row, ""));
    }
}


/*

is it possible to add the content like this

Content: 1
Content: 2
Content: 3
Content: 4

but keeping the functionality to still append more data and not override it
 */