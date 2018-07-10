package weka.classifiers.trees.rfca;

import java.io.Serializable;
/**
 * @author SEBASTIAN VIVAS
 * @author CARLOS COBOS 
 * @author MARTHA MENDOZA 
 */

 /**
 <!-- globalinfo-start -->
 * The FileCA object represents a. ca or. caGold file, it allows 
 * to store the file name, the number of columns of the Covering Array 
 * or Tower Covering Array, this allows to locate the CA or TCA appropriate 
 * to the number of attributes, the selected CA o TCA will correspond 
 * to one whose number of attributes is less than or equal to the number
 * of columns of the Covering Array or Tower Covering Array .
 * <br/>
 * <br/>
 * For more information, see<br/>
 * <br/>
 * Sebastian Vivas, Carlos Cobos and Martha Mendoza
 * Covering arrays to support the process of feature selection 
 * in the Random Forest classifier. LOD 2018 - The Fourth International 
 * Conference on Machine Learning, Optimization, and Data Science. 
 * <p/>
 <!-- globalinfo-end -->
 *
 <!-- technical-bibtex-start -->
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
 <!-- technical-bibtex-end -->
 *
 <!-- options-start -->
 * Attributes are: <p/>
 * 
 * <pre> numAttributes
 *  number of attributes(number of columns) of a FileCA object, 
 *  the FileCA object represents a. ca or .caGold fie. </pre>
 * 
 * <pre> fileName
 * file name of a FileCA object, the FileCA object represents
 * a .ca or .caGold file. </pre> 
 * 
 <!-- options-end -->
 *
 * Options after -- are passed to the designated classifier.<p>
 *
 * @author Carlos Cobos
 * @author Sebastian Vivas (jusvivas@unicauca.edu.co)
 * @version $Revision: 1 $
 */
public class FileCA implements Comparable<FileCA>, Serializable {

    private int numAttributes;
    private String fileName;

    public FileCA() {
    }

    public FileCA(int numAttributes, String fileName) {
        this.numAttributes = numAttributes;
        this.fileName = fileName;
    }

    /**
     * Get the number of attributes of a FileCA object, the FileCA object
     * represents  a .ca or .caGold file.
     *
     * @return number of attributes of the FileCA object
     * @throws Exception if generation fails
     */
    public int getNumAttributes() {
        return numAttributes;
    }

    /**
     * Set the number of attributes of a FileCA object, the FileCA object
     * represents a .ca or .caGold file.
     *
     * @param numAttributes number of attributes to set
     * @throws Exception if generation fails
     */
    public void setNumAttributes(int numAttributes) {
        this.numAttributes = numAttributes;
    }

    /**
     * Get the file name of a FileCA object, the FileCA object represents
     * a .ca or .caGold file.
     *
     * @return file name of the FileCA object
     * @throws Exception if generation fails
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set the file name of a FileCA object, the FileCA object represents a .ca
     * .caGold file.
     *
     * @param fileName file name of the FileCA object to set
     * @throws Exception if generation fails
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Compares the number of attributes of a FileCA object with another FileCA
     * object
     *
     * @param OtherArch FileCA object to compare
     * @return 0 - if the number of attributes of both FileCA objects to be
     * compared is the same 1 - if the number of attributes of the FileCA
     * OtherArch object is smaller -1 - if the number of attributes of the
     * FileCA OtherArch object is larger
     * @throws Exception if generation fails
     */
    @Override
    public int compareTo(FileCA OtherArch) {
        double numAttributesOtherArch = OtherArch.getNumAttributes();

        if (numAttributes == numAttributesOtherArch) {
            return 0;
        } else if (numAttributes > numAttributesOtherArch) {
            return 1;
        } else {
            return -1;
        }
    }

}
