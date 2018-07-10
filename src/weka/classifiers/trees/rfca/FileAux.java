 
package weka.classifiers.trees.rfca;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/**
 * @author SEBASTIAN VIVAS
 * @author CARLOS COBOS 
 * @author MARTHA MENDOZA 
 */

 /**
 <!-- globalinfo-start -->
 * Class that allows locating the CA(.ca) or TCA(.caGold) file according to the number 
 * of attributes of the dataset,the methods allow to return the binary matrix 
 * that represents the CA or TCA (depending on the case).
 * <br/>
 * <br/>
 * TCA(Tower Covering Array)<br/>
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
 * @author Sebastian Vivas 
 * @author Carlos Cobos  
 * @author Martha Mendoza
 * @version $Revision: 1 $
 */


public class FileAux {

    /**
     * Gets the Binary matrix representing the CA according to the selected
     * strength values and number of attributes.
     *
     * @param ListArch list of selected strengths
     * @param numAtributes number of Dataset attributes
     * @return Binary matrix representing the CA according to the selected strength
     * values and number of attributes.
     * @throws java.io.FileNotFoundException
     * @throws Exception if generation fails
     */
    public int[][] loadDataCAsnumAtri(ArrayList<String> ListArch, int numAtributes) throws FileNotFoundException, IOException {
        ArrayList<Double> DatosLeidos = new ArrayList<>();
        int numFil = 0;
        for (int a = 0; a < ListArch.size(); a++) {

            File archivo = null;
            FileReader fr = null;
            BufferedReader br = null;

            ArrayList<Double> Datos;

            /*INICIO Para construir de acuerdo a las pruebas */
            // archivo = new File("./" + ListArch.get(a));
            /*FIN  Para construir de acuerdo a las pruebas */
            /*INICIO Para construir de acuerdo a la ubicacion de los paquetes*/
            /* The default folder name for Weka bits and bobs */
            String WEKAFILES_DIR_NAME = "wekafiles";
            String PACKAGE_NAME = "RFCA";
            String WEKA_HOME_DIR = System.getProperty("user.home") + File.separator + WEKAFILES_DIR_NAME
                    + File.separator + "packages" + File.separator + PACKAGE_NAME;

            archivo = new File(WEKA_HOME_DIR + File.separator + ListArch.get(a));
            /* FIN Para construir de acuerdo a la ubicacion de los paquetes*/

            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            // Lectura del fichero
            String linea;

            while ((linea = br.readLine()) != null) {

                Datos = new ArrayList<>();
                String[] splitStr = linea.split("\\s+");
                int contCeros = 0;
                double auxDatoLeido;
                for (int j = 0; j < numAtributes; j++) {
                    auxDatoLeido = Double.parseDouble(splitStr[j]);
                    if (auxDatoLeido == 0.0) {
                        contCeros++;
                    }

                    Datos.add(auxDatoLeido);
                }
                if (contCeros != numAtributes) {
                    numFil++;
                    for (int j = 0; j < Datos.size(); j++) {
                        DatosLeidos.add(Datos.get(j));
                    }

                }

            }

        }
        int aux = 0;
        int DatosArch[][] = new int[numFil][numAtributes];
        for (int i = 0; i < numFil; i++) {
            for (int j = 0; j < numAtributes; j++) {
                Double dato = DatosLeidos.get(aux);
                DatosArch[i][j] = dato.intValue();
                aux++;
            }
        }
        return DatosArch;

    }

    /**
     * Gets the Binary matrix representing the TCA according to the selected
     * strength values and number of attributes.
     *
     * @param arch direction corresponding to the TCA files folder
     * @param strengthVal selected strength value
     * @param numAtributes number of Dataset attributes
     * @return Binary matrix representing the TCA according to the selected
     * strength values and number of attributes.
     * @throws java.io.FileNotFoundException
     * @throws Exception if generation fails
     */
    public int[][] loadDataTCAsnumAtri(String arch, String strengthVal, int numAtributes) throws FileNotFoundException, IOException {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<Double> DatosLeidos = new ArrayList<>();
        ArrayList<Double> Datos;
        
        /*INICIO Para construir de acuerdo a las pruebas */
        //  archivo = new File("./" + arch);
        /*FIN  Para construir de acuerdo a las pruebas */
        /*INICIO Para construir de acuerdo a la ubicacion de los paquetes*/
        /* The default folder name for Weka bits and bobs */
        String WEKAFILES_DIR_NAME = "wekafiles";
        String PACKAGE_NAME = "RFCA";
        String WEKA_HOME_DIR = System.getProperty("user.home") + File.separator + WEKAFILES_DIR_NAME
                + File.separator + "packages" + File.separator + PACKAGE_NAME;

        archivo = new File(WEKA_HOME_DIR + File.separator + arch);
        /* FIN Para construir de acuerdo a la ubicacion de los paquetes*/
        
        fr = new FileReader(archivo);
        br = new BufferedReader(fr);
        // Lectura del fichero
        String linea;
        int numMaq = 0;
        int numtareas = 0;
        int band = 0;
        while ((linea = br.readLine()) != null && band == 0) {
            linea = linea.trim();
            if (linea.contains("0") || linea.contains("1")) {
                Datos = new ArrayList<>();
                String[] splitStr = linea.split("\\s+");
                numtareas = numAtributes;
                int contCeros = 0;
                double auxDatoLeido;
                for (int j = 0; j < numAtributes; j++) {
                    auxDatoLeido = Double.parseDouble(splitStr[j]);
                    if (auxDatoLeido == 0.0) {
                        contCeros++;
                    }

                    Datos.add(auxDatoLeido);
                }
                if (contCeros != numAtributes) {
                    numMaq++;
                    for (int j = 0; j < Datos.size(); j++) {
                        DatosLeidos.add(Datos.get(j));
                    }

                }

            } else {
                if (linea.contains(strengthVal)) {
                    band = 1;
                }

            }
        }

        int aux = 0;
        int DatosArch[][] = new int[numMaq][numtareas];
        for (int i = 0; i < numMaq; i++) {
            for (int j = 0; j < numtareas; j++) {
                Double dato = DatosLeidos.get(aux);
                DatosArch[i][j] = dato.intValue();
                aux++;
            }
        }
        return DatosArch;
    }

}
