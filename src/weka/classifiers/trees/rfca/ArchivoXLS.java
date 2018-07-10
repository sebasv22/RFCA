
package weka.classifiers.trees.rfca;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author SEBASTIAN VIVAS
 * @author CARLOS COBOS 
 * @author MARTHA MENDOZA 
 */

@SuppressWarnings("deprecation")
public class ArchivoXLS {

    private int PosFila;

    public ArchivoXLS() {
        PosFila = -1;
    }

    public int getPosFila() {
        this.PosFila = this.PosFila + 1;
        return PosFila;
    }

    public void setPosFila(int PosFila) {
        this.PosFila = PosFila;
    }

    public static void readXLSFile(String ruta) throws IOException {
        InputStream ExcelFileToRead = new FileInputStream(ruta);
        HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);

        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row;
        HSSFCell cell;

        Iterator rows = sheet.rowIterator();

        while (rows.hasNext()) {
            row = (HSSFRow) rows.next();
            Iterator cells = row.cellIterator();

            while (cells.hasNext()) {
                cell = (HSSFCell) cells.next();

                if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                    System.out.print(cell.getStringCellValue() + " ");
                } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                    System.out.print(cell.getNumericCellValue() + " ");
                } else {
                    //U Can Handel Boolean, Formula, Errors
                }
            }
            System.out.println();
        }

    }

    public static void writeXLSFile() throws IOException {

        String excelFileName = "C:/Test.xls";//name of excel file
        //  String excelFileName = ruta;
        String sheetName = "Sheet1";//name of sheet

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);

        //iterating r number of rows
        for (int r = 0; r < 5; r++) {
            HSSFRow row = sheet.createRow(r);

            //iterating c number of columns
            for (int c = 0; c < 5; c++) {
                HSSFCell cell = row.createCell(c);

                cell.setCellValue("Cell " + r + " " + c);
            }
        }

        FileOutputStream fileOut = new FileOutputStream(excelFileName);

        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    public static void readXLSXFiles(String ruta) throws IOException {
        //InputStream ExcelFileToRead = new FileInputStream("C:/Test.xlsx");
        InputStream ExcelFileToRead = new FileInputStream(ruta);
        XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);

        XSSFWorkbook test = new XSSFWorkbook();

        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;

        Iterator rows = sheet.rowIterator();

        while (rows.hasNext()) {
            row = (XSSFRow) rows.next();
            Iterator cells = row.cellIterator();
            while (cells.hasNext()) {
                cell = (XSSFCell) cells.next();

                if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                    System.out.print(cell.getStringCellValue() + " ");
                } else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                    System.out.print(cell.getNumericCellValue() + " ");
                } else {
                    //U Can Handel Boolean, Formula, Errors
                }
            }
            System.out.println();
        }

    }

    public String[][] LeerXLSX(String ruta) throws IOException {
        //InputStream ExcelFileToRead = new FileInputStream("C:/Test.xlsx");
        InputStream ExcelFileToRead = new FileInputStream(ruta);
        XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);

        XSSFWorkbook test = new XSSFWorkbook();

        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;

        Iterator rows = sheet.rowIterator();
        int auxFilas = 1;
        int numFilas = 0;
        int numColumn = 0;
        ArrayList<String> DatosLeidos = new ArrayList<>();
        while (rows.hasNext()) {
            numFilas = numFilas + 1;
            row = (XSSFRow) rows.next();
            Iterator cells = row.cellIterator();
            while (cells.hasNext()) {
                cell = (XSSFCell) cells.next();
                if (auxFilas > 1) {
                    if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        DatosLeidos.add(cell.getStringCellValue());
                    }
                    if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                        DatosLeidos.add("" + cell.getNumericCellValue());
                    }
                }
                if (auxFilas == 1) {
                    numColumn = numColumn + 1;
                }
            }
            auxFilas = auxFilas + 1;
        }
        int aux = 0;
        numFilas = numFilas - 1;
        String DatosArch[][] = new String[numFilas][numColumn];
        for (int i = 0; i < numFilas; i++) {
            for (int j = 0; j < numColumn; j++) {
                DatosArch[i][j] = DatosLeidos.get(aux);
                aux++;
            }
        }

        return DatosArch;

    }

    public static void writeXLSXFile() throws IOException {

        String excelFileName = "C:/Test.xlsx";//name of excel file
        //String excelFileName = "Datos_PlanHorarios\\Salida\\HorarioPrescolar.xlsx";
        String sheetName = "Sheet1";//name of sheet

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(sheetName);

        //iterating r number of rows
        for (int r = 0; r < 5; r++) {
            XSSFRow row = sheet.createRow(r);

            //iterating c number of columns
            for (int c = 0; c < 5; c++) {
                XSSFCell cell = row.createCell(c);

                cell.setCellValue("Cell " + r + " " + c);
            }
        }

        FileOutputStream fileOut = new FileOutputStream(excelFileName);

        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();

    } 
    
    public void escribiResultados(ArrayList<Object> ListaNumArboles, ArrayList<Object> ListaResAccuracy) throws FileNotFoundException, IOException {

        String excelFileName = "ResultsXLS\\ResultadosDiabetes.xlsx";
        String sheetName = "Resultados";//name of sheet

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(sheetName);

        XSSFRow row = sheet.createRow(getPosFila());
        for (int i = 0; i < ListaNumArboles.size(); i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue("" + ListaNumArboles.get(i));
        }

        row = sheet.createRow(getPosFila());
        for (int i = 0; i < ListaResAccuracy.size(); i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellType(CellType.NUMERIC);

            String auxResAccuracy = "" + ListaResAccuracy.get(i);
            auxResAccuracy = auxResAccuracy.replace(".", ",");
            cell.setCellValue("" + auxResAccuracy);
        }

        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        File archivo = new File("./");
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();

        System.out.println("Conforme a la ubicacion del proyecto el archivo esta disponible en la siguiente direccion: " + archivo.getAbsolutePath() + excelFileName);

    }

    public void escribirResultados(String nombreArch, ArrayList<Object> ListaNumArboles, ArrayList<ArrayList<Object>> ListResRForest, ArrayList<ArrayList<Object>> ListResCAs) throws FileNotFoundException, IOException {
        nombreArch = "Resultados_" + nombreArch + ".xlsx";

        //       String excelFileName = "ResultsXLS\\ResultadosDiabetes.xlsx";
        String excelFileName = "ResultsXLS\\" + nombreArch;
        String sheetName = "Resultados";//name of sheet

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(sheetName);

        XSSFRow row = sheet.createRow(getPosFila());
        for (int i = 0; i < ListaNumArboles.size(); i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue("" + ListaNumArboles.get(i));
        }

        row = sheet.createRow(getPosFila());
        for (int i = 0; i < ListResRForest.size(); i++) {
            row = sheet.createRow(getPosFila());

            for (int j = 0; j < ListResRForest.get(i).size(); j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellType(CellType.NUMERIC);

                String auxResultado = "" + ListResRForest.get(i).get(j);
                auxResultado = auxResultado.replace(".", ",");
                cell.setCellValue("" + auxResultado);
            }
        }

        row = sheet.createRow(getPosFila());
        for (int i = 0; i < ListResCAs.size(); i++) {
            row = sheet.createRow(getPosFila());

            for (int j = 0; j < ListResCAs.get(i).size(); j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellType(CellType.NUMERIC);

                String auxResAccuracy = "" + ListResCAs.get(i).get(j);
                auxResAccuracy = auxResAccuracy.replace(".", ",");
                cell.setCellValue("" + auxResAccuracy);
            }
        }

        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        File archivo = new File("./");
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();

        System.out.println("Conforme a la ubicacion del proyecto el archivo esta disponible en la siguiente direccion: " + archivo.getAbsolutePath() + excelFileName);

    }

    public void escribirResultadosDatasets(String nombreArch, ArrayList<Object> ListaAlgoritmos, ArrayList<ArrayList<Object>> ListResultadosRF) throws FileNotFoundException, IOException {
        nombreArch = "Resultados_" + nombreArch + ".xlsx";

        //       String excelFileName = "ResultsXLS\\ResultadosDiabetes.xlsx";
        String excelFileName = "ResultsXLS\\" + nombreArch;
        String sheetName = "Resultados";//name of sheet

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(sheetName);

        XSSFRow row = sheet.createRow(getPosFila());
        for (int i = 0; i < ListaAlgoritmos.size(); i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue("" + ListaAlgoritmos.get(i));
        }

        row = sheet.createRow(getPosFila());
        for (int i = 0; i < ListResultadosRF.size(); i++) {
            row = sheet.createRow(getPosFila());

            for (int j = 0; j < ListResultadosRF.get(i).size(); j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellType(CellType.NUMERIC);

                String auxResultado = "" + ListResultadosRF.get(i).get(j);
                auxResultado = auxResultado.replace(".", ",");
                cell.setCellValue("" + auxResultado);
            }
        }

        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        File archivo = new File("./");
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();

        System.out.println("Conforme a la ubicacion del proyecto el archivo esta disponible en la siguiente direccion: " + archivo.getAbsolutePath() + excelFileName);

    }

    public void escribirResultadosTCA(String nombreArch, ArrayList<Object> ListaNumArboles, ArrayList<ArrayList<Object>> ListResRForest, ArrayList<ArrayList<Object>> ListResCAs) throws FileNotFoundException, IOException {
        nombreArch = "Resultados_" + nombreArch + "TCA" + ".xlsx";

        //       String excelFileName = "ResultsXLS\\ResultadosDiabetes.xlsx";
        String excelFileName = "ResultsXLS\\" + nombreArch;
        String sheetName = "Resultados";//name of sheet

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(sheetName);

        XSSFRow row = sheet.createRow(getPosFila());
        for (int i = 0; i < ListaNumArboles.size(); i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue("" + ListaNumArboles.get(i));
        }

        row = sheet.createRow(getPosFila());
        for (int i = 0; i < ListResRForest.size(); i++) {
            row = sheet.createRow(getPosFila());

            for (int j = 0; j < ListResRForest.get(i).size(); j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellType(CellType.NUMERIC);

                String auxResultado = "" + ListResRForest.get(i).get(j);
                auxResultado = auxResultado.replace(".", ",");
                cell.setCellValue("" + auxResultado);
            }
        }

        row = sheet.createRow(getPosFila());
        for (int i = 0; i < ListResCAs.size(); i++) {
            row = sheet.createRow(getPosFila());

            for (int j = 0; j < ListResCAs.get(i).size(); j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellType(CellType.NUMERIC);

                String auxResAccuracy = "" + ListResCAs.get(i).get(j);
                auxResAccuracy = auxResAccuracy.replace(".", ",");
                cell.setCellValue("" + auxResAccuracy);
            }
        }

        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        File archivo = new File("./");
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();

        System.out.println("Conforme a la ubicacion del proyecto el archivo esta disponible en la siguiente direccion: " + archivo.getAbsolutePath() + excelFileName);

    }

    public void escribirResultadosRunRFCA(String nombreArch, ArrayList<ArrayList<Object>> ListResCAs, ArrayList<ArrayList<Object>> ListRF) throws FileNotFoundException, IOException {
        nombreArch = "Resultados_" + nombreArch + ".xlsx";

        String excelFileName = "ResultsXLS\\" + nombreArch;
        String sheetName = "Resultados_CA";

        XSSFWorkbook wb = new XSSFWorkbook();

        XSSFSheet sheet = wb.createSheet(sheetName);
        XSSFRow row;
        /* Escribir resulados CA*/
        for (int i = 0; i < ListResCAs.size(); i++) {
            row = sheet.createRow(getPosFila());

            for (int j = 0; j < ListResCAs.get(i).size(); j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellType(CellType.NUMERIC);

                String auxResultado = "" + ListResCAs.get(i).get(j);
                auxResultado = auxResultado.replace(".", ",");
                cell.setCellValue("" + auxResultado);
            }
        }
        /* Escribir resulados  Random Forest Referencia */
        setPosFila(-1);
        sheetName = "Resultados_RFR";
        XSSFSheet sheetRFR = wb.createSheet(sheetName);
        XSSFRow rowRFR;
        /* Escribir resulados CA*/
        for (int i = 0; i < ListRF.size(); i++) {
            rowRFR = sheetRFR.createRow(getPosFila());

            for (int j = 0; j < ListRF.get(i).size(); j++) {
                XSSFCell cell = rowRFR.createCell(j);
                cell.setCellType(CellType.NUMERIC);

                String auxResultado = "" + ListRF.get(i).get(j);
                auxResultado = auxResultado.replace(".", ",");
                cell.setCellValue("" + auxResultado);
            }
        }

        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        File archivo = new File("./");
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();

        System.out.println("Conforme a la ubicacion del proyecto el archivo esta disponible en la siguiente direccion: " + archivo.getAbsolutePath() + excelFileName);

    }
    
       public void escribirResultadosAccuracyTCA(String nombreArch, ArrayList<ArrayList<Object>> ListaAlg, ArrayList<ArrayList<Object>> ListResultadosRFCA ) throws FileNotFoundException, IOException {
        nombreArch = "Resultados_" + nombreArch + ".xlsx";
        /* Escribir resulados RFTCA*/

        String excelFileName = "ResultsXLS\\" + nombreArch;
        String sheetName = "Resultados_RFTCA";

        XSSFWorkbook wb = new XSSFWorkbook();

        XSSFSheet sheet = wb.createSheet(sheetName);
        XSSFRow row;
        /*Escritura de encabezado Random Forest con Torres de CA  */
        row = sheet.createRow(getPosFila());

        for (int j = 0; j < ListaAlg.get(0).size(); j++) {
            XSSFCell cell = row.createCell(j);
            cell.setCellType(CellType.NUMERIC);

            String auxResultado = "" + ListaAlg.get(0).get(j);
            auxResultado = auxResultado.replace(".", ",");
            cell.setCellValue("" + auxResultado);
        }
        /*Escritura de datos de Random Forest con Torres de CA  */
        for (int i = 0; i < ListResultadosRFCA.size(); i++) {
            row = sheet.createRow(getPosFila());

            for (int j = 0; j < ListResultadosRFCA.get(i).size(); j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellType(CellType.NUMERIC);

                String auxResultado = "" + ListResultadosRFCA.get(i).get(j);
                auxResultado = auxResultado.replace(".", ",");
                cell.setCellValue("" + auxResultado);
            }
        }
        
          

        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        File archivo = new File("./");
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();

        System.out.println("Conforme a la ubicacion del proyecto, el archivo esta disponible en la siguiente direccion: " + archivo.getAbsolutePath() + excelFileName);

    }

    public void escribirResultadosAccuracy100Trees(String nombreArch, ArrayList<ArrayList<Object>> ListaAlg, ArrayList<ArrayList<Object>> ListResultadosRFCA, ArrayList<ArrayList<Object>> ListResultadosRFCA_Acum) throws FileNotFoundException, IOException {
        nombreArch = "Resultados_" + nombreArch + ".xlsx";
        /* Escribir resulados RFCA*/

        String excelFileName = "ResultsXLS\\" + nombreArch;
        String sheetName = "Resultados_RFCA";

        XSSFWorkbook wb = new XSSFWorkbook();

        XSSFSheet sheet = wb.createSheet(sheetName);
        XSSFRow row;
        /*Escritura de encabezado Random Forest CA Acumulado */
        row = sheet.createRow(getPosFila());

        for (int j = 0; j < ListaAlg.get(0).size(); j++) {
            XSSFCell cell = row.createCell(j);
            cell.setCellType(CellType.NUMERIC);

            String auxResultado = "" + ListaAlg.get(0).get(j);
            auxResultado = auxResultado.replace(".", ",");
            cell.setCellValue("" + auxResultado);
        }
        /*Escritura de resultados Random Forest CA Acumulado */
        for (int i = 0; i < ListResultadosRFCA.size(); i++) {
            row = sheet.createRow(getPosFila());

            for (int j = 0; j < ListResultadosRFCA.get(i).size(); j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellType(CellType.NUMERIC);

                String auxResultado = "" + ListResultadosRFCA.get(i).get(j);
                auxResultado = auxResultado.replace(".", ",");
                cell.setCellValue("" + auxResultado);
            }
        }
        /* Escribir resulados  Random Forest CA Acumulado */
        setPosFila(-1);
        sheetName = "Resultados_RFCA_Acum";
        XSSFSheet sheetRFR = wb.createSheet(sheetName);
        XSSFRow rowRFR;

        /*Escritura de encabezado Random Forest CA Acumulado */
        rowRFR = sheetRFR.createRow(getPosFila());

        for (int j = 0; j < ListaAlg.get(1).size(); j++) {
            XSSFCell cell = rowRFR.createCell(j);
            cell.setCellType(CellType.NUMERIC);

            String auxResultado = "" + ListaAlg.get(1).get(j);
            auxResultado = auxResultado.replace(".", ",");
            cell.setCellValue("" + auxResultado);
        }

        /*Escritura de resultados Random Forest CA Acumulado */
        for (int i = 0; i < ListResultadosRFCA_Acum.size(); i++) {
            rowRFR = sheetRFR.createRow(getPosFila());

            for (int j = 0; j < ListResultadosRFCA_Acum.get(i).size(); j++) {
                XSSFCell cell = rowRFR.createCell(j);
                cell.setCellType(CellType.NUMERIC);

                String auxResultado = "" + ListResultadosRFCA_Acum.get(i).get(j);
                auxResultado = auxResultado.replace(".", ",");
                cell.setCellValue("" + auxResultado);
            }
        }

        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        File archivo = new File("./");
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();

        System.out.println("Conforme a la ubicacion del proyecto, el archivo esta disponible en la siguiente direccion: " + archivo.getAbsolutePath() + excelFileName);

    }
    
    public String writeResults(String nombreArch, ArrayList<ArrayList<Object>> ListaAlg, ArrayList<ArrayList<Object>> ListResultadosRFCA) throws FileNotFoundException, IOException {
        //nombreArch = "Resultados_" + nombreArch + ".xlsx";
         nombreArch =   nombreArch + ".xlsx";
        /* Escribir resulados RFCA*/

        String excelFileName = "ResultsXLS\\" + nombreArch;
        String sheetName = "Results_RFCA";

        XSSFWorkbook wb = new XSSFWorkbook();

        XSSFSheet sheet = wb.createSheet(sheetName);
        XSSFRow row;
        /*Escritura de encabezado Random Forest CA */
        row = sheet.createRow(getPosFila());

        for (int j = 0; j < ListaAlg.get(0).size(); j++) {
            XSSFCell cell = row.createCell(j);
            cell.setCellType(CellType.NUMERIC);

            String auxResultado = "" + ListaAlg.get(0).get(j);
            auxResultado = auxResultado.replace(".", ",");
            cell.setCellValue("" + auxResultado);
        }
        /*Escritura de resultados Random Forest CA  */
        for (int i = 0; i < ListResultadosRFCA.size(); i++) {
            row = sheet.createRow(getPosFila());

            for (int j = 0; j < ListResultadosRFCA.get(i).size(); j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellType(CellType.NUMERIC);

                String auxResultado = "" + ListResultadosRFCA.get(i).get(j);
                auxResultado = auxResultado.replace(".", ",");
                cell.setCellValue("" + auxResultado);
            }
        }
 

        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        File file = new File("./");
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        String path =file.getAbsolutePath() + excelFileName;
        return path;
    }
}
