package weka.classifiers.trees.rfca;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SEBASTIAN VIVAS
 * @author CARLOS COBOS 
 * @author MARTHA MENDOZA 
 */

/**
 * <!-- globalinfo-start -->
 * Class that represents a CA object (files .ca) ,in which, according to the
 * strength selected in the binary matrix corresponding to said strength, it
 * also contains information about the number of rows and columns of the loaded
 * CA. Each row of the CA(binary matrix) represents a tree in the Random Forest.
 * In which the attributes that will be used for the construction of that tree
 * will be determined by positions of the row of the CA whose 1. the positions 0
 * in the row indicate that this attribute will not be used in the construction
 * of the tree. The number of rows of the covering array represented by the
 * binary matrix indicates the number of trees to be constructed in the random
 * forest.
 * <br/>
 * <br/>
 * CA(Covering Array)<br/>
 * <br/>
 * <br/>
 * For more information, see<br/>
 * <br/>
 * Sebastian Vivas, Carlos Cobos and Martha Mendoza
 * Covering arrays to support the process of feature selection 
 * in the Random Forest classifier. LOD 2018 - The Fourth International 
 * Conference on Machine Learning, Optimization, and Data Science. 
 * <p/>
 * <!-- globalinfo-end -->
 *
 * <!-- technical-bibtex-start -->
 * BibTeX:
 * <pre>
 * &#64;article{Sebastian Vivas, Carlos Cobos and Martha Mendoza.
 * Covering arrays to support the process of feature selection 
 * in the Random Forest classifier. LOD 2018 - The Fourth International 
 * Conference on Machine Learning, Optimization, and Data Science. 
 * September 13-16, 2018. Volterra, Tuscany, Italy. To appear 
 * in Lecture Notes in Computer Science (LNCS), Springer.
 * }
 * </pre>
 * <p/>
 * <!-- technical-bibtex-end -->
 *
 * <!-- options-start -->
 * Attributes are:
 * <p/>
 *
 * <pre> MatrixCA
 *  CA binary matrix from the selected strength</pre>
 *
 * <pre> currentRow
 *  the position of the current row of the covering array
 *  represented by the binary matrix.
 *  The construction of each tree in the Random Forest algorithm is
 *  carried out progressively, so this attribute allows to know which
 *  row of the CA to use for the construction of the tree </pre>
 *
 * <pre> numRows
 *  number of rows of the selected CA</pre>
 *
 * <pre> numColumn
 *  number of columns of the selected CA.</pre>
 *
 * <!-- options-end -->
 *
 * Options after -- are passed to the designated classifier.<p>
 *
 * @author Sebastian Vivas 
 * @author Carlos Cobos  
 * @author Martha Mendoza
 * @version $Revision: 1 $
 */
public class CAs implements Serializable {

    private int[][] MatrixCA;
    private int currentRow;
    private int numRows;
    private int numColumn;
    private ArrayList<FileCA> ListFileCA;
    private static CAs instance;

    /* Singleton pattern implementation */
    private CAs() {

        this.MatrixCA = new int[2][2];
        this.currentRow = -1;
        this.numRows = MatrixCA.length;
        this.numColumn = MatrixCA[0].length;

    }

    /**
     * Obtain a CA instance
     *
     * @return CA instance
     * @throws Exception if generation fails
     */
    public static CAs getInstance() {
        synchronized (CAs.class) {
            if (instance == null) {
                instance = new CAs();
            }
        }
        return instance;
    }

    /**
     * Gets the file .ca according to the number of attributes of the dataset
     * and the corresponding direction to the type of strength.
     *
     * @param dirCAs direction corresponding to the type of strength.
     * @param numAttrib number of Dataset attributes
     * @return direction of the file .ca
     * @throws Exception if generation fails
     */
    public String getFile(String dirCAs, int numAttrib) {
        String fileName = "";
        ArrayList<FileCA> ListFiles = new ArrayList<>();

        /*INICIO Para construir de acuerdo a las pruebas */
        //File folder = new File("./" + dirCAs);
        /*FIN  Para construir de acuerdo a las pruebas */

        /*INICIO Para construir de acuerdo a la ubicacion de los paquetes*/

        /* The default folder name for Weka bits and bobs */
         String WEKAFILES_DIR_NAME = "wekafiles";
         String PACKAGE_NAME = "RFCA";
         String WEKA_HOME_DIR = System.getProperty("user.home") + File.separator + WEKAFILES_DIR_NAME
         + File.separator + "packages" + File.separator + PACKAGE_NAME;
         File folder = new File(WEKA_HOME_DIR + File.separator + dirCAs);

        /* FIN Para construir de acuerdo a la ubicacion de los paquetes*/
        
        /*     System.out.println(" folder.getPath(): " + folder.getPath());
         System.out.println(" folder.getAbsolutePath(): " + folder.getAbsolutePath());
         try {
         System.out.println(" folder.getCanonicalPath(): " + folder.getCanonicalPath());
         } catch (IOException ex) {
         Logger.getLogger(CAs.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        File[] listOfFiles = folder.listFiles();
        FileCA fileCa;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String file = listOfFiles[i].getName();
                String numAttributes = file.substring(file.indexOf("k") + 1, file.indexOf("v"));
                int auxNumAttributes = Integer.parseInt(numAttributes);
                fileCa = new FileCA();
                fileCa.setFileName(file);
                fileCa.setNumAttributes(auxNumAttributes);
                ListFiles.add(fileCa);
            }
        }
        Collections.sort(ListFiles);

        for (int i = 0; i < ListFiles.size(); i++) {
            if (numAttrib <= ListFiles.get(i).getNumAttributes()) {
                fileName = ListFiles.get(i).getFileName();
                break;
            }
        }

        fileName = dirCAs + fileName;

        return fileName;

    }

    /**
     * Reads the. ca files from the CA folder according to the selected strength
     * type and stores FileCA objects in a list (ListFiles), in the objects
     * FileCA the CA name and CA attribute number are stored.
     *
     * @param strengthSelected selected strength value
     * @return list of FileCA objects, in the objects FileCA the CA name and CA
     * attribute number are stored.
     * @throws Exception if generation fails
     */
    public ArrayList<FileCA> getListFiles(String strengthSelected) {
        ArrayList<FileCA> ListFiles = new ArrayList<>();
        String dirCAs = "CAs\\CA\\";

        String dirStrengthDir = "";

        switch (strengthSelected) {
            case "strength_2":
                dirStrengthDir = "t2";
                break;
            case "strength_3":
                dirStrengthDir = "t3";
                break;
            case "strength_4":
                dirStrengthDir = "t4";
                break;
            case "strength_5":
                dirStrengthDir = "t5";
                break;
            case "strength_6":
                dirStrengthDir = "t6";
                break;
            case "strength_7":
                dirStrengthDir = "t7";
                break;

        }

        dirCAs = dirCAs + dirStrengthDir + "\\";

        /*INICIO Para construir de acuerdo a las preubas a realizar*/
        File folder = new File("./" + dirCAs);

        /*FIN Para construir de acuerdo a la ubicacion de los paquetes*/
        /*INICIO Para construir de acuerdo a la ubicacion de los paquetes*/
        /* The default folder name for Weka bits and bobs */
        /*String WEKAFILES_DIR_NAME = "wekafiles";
         String PACKAGE_NAME = "RFCA_RFTCA";
         String WEKA_HOME_DIR = System.getProperty("user.home") + File.separator + WEKAFILES_DIR_NAME
         + File.separator + "packages" + File.separator + PACKAGE_NAME;

         File folder = new File(WEKA_HOME_DIR + File.separator + dirCAs);**/

        /* FIN Para construir de acuerdo a la ubicacion de los paquetes*/
        File[] listOfFiles = folder.listFiles();
        FileCA fileCa;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String fileName = listOfFiles[i].getName();
                String numAttributes = fileName.substring(fileName.indexOf("k") + 1, fileName.indexOf("v"));
                int auxNumAttributes = Integer.parseInt(numAttributes);
                fileCa = new FileCA();
                fileCa.setFileName(fileName);
                fileCa.setNumAttributes(auxNumAttributes);
                ListFiles.add(fileCa);
            }
        }
        return ListFiles;

    }

    /**
     * Get the number of iterations from a determinated CA
     *
     * @param m_strengthValue selected strength value
     * @param numAttributes number of Dataset attributes
     * @return number of iterations from a determinated CA
     * @throws Exception if generation fails
     */
    public int getNumIterations(int m_strengthValue, int numAttributes) {

        /* strengthList - Stores the address list of covering arrays to use */
        ArrayList<String> strengthList = new ArrayList<>();

        /* Depending on the strength value selected, it is added to strengthList
         which file or files of covering array  to use */
        switch (m_strengthValue) {

            /* From strength 2 to strength 8 corresponds to covering arrays without accumulating */
            case 2:
                strengthList.add("strength_2");
                break;
            case 3:
                strengthList.add("strength_3");
                break;
            case 4:
                strengthList.add("strength_4");
                break;
            case 5:
                strengthList.add("strength_5");
                break;
            case 6:
                strengthList.add("strength_6");
                break;
            case 7:
                strengthList.add("strength_7");
                break;

        }
        ArrayList<String> ArchList = getArchList(strengthList, numAttributes);
        setCAmatrix(ArchList, numAttributes);

        int numIterations = getNumRows();

        return numIterations;

    }

    /**
     * Obtain the CA directions corresponding to the number of Dataset
     * attributes
     *
     * @param strengthList List of selected strengths
     * @param numAttributes number of Dataset attributes
     * @return the disk addresses of the CAs corresponding to the strengths
     * selected in the strengthList parameter
     * @throws Exception if generation fails
     */
    public ArrayList<String> getArchList(ArrayList<String> strengthList, int numAttributes) {
        ArrayList<String> fileList = new ArrayList<>();
        for (int i = 0; i < strengthList.size(); i++) {
            /* dirCAs - Direction from the Covering Arrays*/
            String dirCAs = "CAs\\CA\\";
            /* valueT - Strength Value corresponding to the strength selected in strengthlist */
            String valueT = "";

            switch (strengthList.get(i)) {
                case "strength_2":
                    valueT = "t2";
                    break;
                case "strength_3":
                    valueT = "t3";
                    break;
                case "strength_4":
                    valueT = "t4";
                    break;
                case "strength_5":
                    valueT = "t5";
                    break;
                case "strength_6":
                    valueT = "t6";
                    break;
                case "strength_7":
                    valueT = "t7";
                    break;

            }
            dirCAs = dirCAs + valueT + "\\";
            /* Obtains the file name according to the number of attributes
             of the dataset and the corresponding address with the type of strength. */
            String fileName = getFile(dirCAs, numAttributes);
            fileList.add(fileName);
        }

        return fileList;

    }

    /**
     * Sets the CA corresponding to the number of Dataset attributes
     *
     * @param strengthList list of selected strengths
     * @param numAtributes number of Dataset attributes
     * @throws Exception if generation fails
     */
    public void setCAmatrix(ArrayList<String> strengthList, int numAtributes) {
        FileAux fileAux = new FileAux();
        int[][] Data = null;

        try {

            Data = fileAux.loadDataCAsnumAtri(strengthList, numAtributes);
            this.MatrixCA = Data;
            this.numRows = MatrixCA.length;
            this.numColumn = MatrixCA[0].length;
        } catch (IOException ex) {
            Logger.getLogger(CAs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * From the current row of the CA you get the positions in which an
     * attribute is selected, that is, where the value of 1
     *
     * @return List of positions of the selected attributes
     * @throws Exception if generation fails
     */
    public ArrayList<Integer> SelectColumns() {
        ArrayList<Integer> columnsSelected = new ArrayList<>();
        int rowCurrent = getCurrentRow();
        for (int j = 0; j < numColumn; j++) {
            if (MatrixCA[rowCurrent][j] != 0) {
                columnsSelected.add(j);

            }

        }
        return columnsSelected;
    }

    /**
     * Get the position of the current row
     *
     * @return current row position
     * @throws Exception if generation fails
     */
    public int getCurrentRow() {
        this.currentRow = this.currentRow + 1;
        double mod = this.currentRow % this.numRows;
        Double modulo = mod;
        int RowCurrent = modulo.intValue();

        return RowCurrent;
    }

    /**
     * Resets the auxiliary variable indicating the row to be used in the
     * construction of a tree
     *
     * @param currentRow row position to set
     * @throws Exception if generation fails
     */
    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    /**
     * Gets the number of rows of the selected CA
     *
     * @return number of rows of the selected CA
     * @throws Exception if generation fails
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Set the number of rows of the CA
     *
     * @param numRows number of rows to set
     * @throws Exception if generation fails
     */
    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    /**
     * Get the number of columns of the CA
     *
     * @return number of columns of the CA
     * @throws Exception if generation fails
     */
    public int getNumColumn() {
        return numColumn;
    }

    /**
     * Set the number of columns of the CA
     *
     * @param numColumn number of columns to set
     * @throws Exception if generation fails
     */
    public void setNumColumn(int numColumn) {
        this.numColumn = numColumn;
    }

    public ArrayList<FileCA> getListFileCA() {
        return ListFileCA;
    }

    public void setListFileCA(ArrayList<FileCA> ListFileCA) {
        this.ListFileCA = ListFileCA;
    }

    /**
     * Get the Ca binary matrix from the selected strength
     *
     * @return Ca matrix
     * @throws Exception if generation fails
     */
    public int[][] getMatrixCA() {
        return MatrixCA;
    }

    /**
     * Set the CA matrix
     *
     * @param MatrixCA matrix to set
     * @throws Exception if generation fails
     */
    public void setMatrixCA(int[][] MatrixCA) {
        this.MatrixCA = MatrixCA;
    }

}
