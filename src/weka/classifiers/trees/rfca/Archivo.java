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
public class Archivo {

    public int[][] cargarDatosCAs(String arch) throws FileNotFoundException, IOException {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<Double> DatosLeidos = new ArrayList<>();
        ArrayList<Double> Datos;
        archivo = new File("./" + arch);
        fr = new FileReader(archivo);
        br = new BufferedReader(fr);
        // Open the file
        String linea;
        int numMaq = 0;
        int numtareas = 0;
        while ((linea = br.readLine()) != null) {
            Datos = new ArrayList<>();
            String[] splitStr = linea.split("\\s+");
            numtareas = splitStr.length;
            int contCeros = 0;
            double auxDatoLeido;

            for (int j = 0; j < splitStr.length; j++) {
                auxDatoLeido = Double.parseDouble(splitStr[j]);
                if (auxDatoLeido == 0.0) {
                    contCeros++;
                }

                Datos.add(auxDatoLeido);
            }
            if (contCeros != splitStr.length) {
                numMaq++;
                for (int j = 0; j < Datos.size(); j++) {
                    DatosLeidos.add(Datos.get(j));
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

    public int[][] cargarDatosCAsCopy(String arch) throws FileNotFoundException, IOException {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<Double> DatosLeidos = new ArrayList<>();
        archivo = new File("./" + arch);
        fr = new FileReader(archivo);
        br = new BufferedReader(fr);
        // Open the file
        String linea;
        int numMaq = 0;
        int numtareas = 0;
        while ((linea = br.readLine()) != null) {
            String[] splitStr = linea.split("\\s+");
            numtareas = splitStr.length;

            for (int j = 0; j < splitStr.length; j++) {
                DatosLeidos.add(Double.parseDouble(splitStr[j]));
            }
            numMaq++;
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

    public int[][] cargarDatosCAsnumAtriCopy(String arch, int numAtributes) throws FileNotFoundException, IOException {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<Double> DatosLeidos = new ArrayList<>();
        ArrayList<Double> Datos;
        archivo = new File("./" + arch);
        fr = new FileReader(archivo);
        br = new BufferedReader(fr);
        // Open the file
        String linea;
        int numMaq = 0;
        int numtareas = 0;
        while ((linea = br.readLine()) != null) {
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

    /* cargarDatosCAsnumAtri - CA CON duplicados  */
    public int[][] cargarDatosCAsnumAtri(ArrayList<String> ListArch, int numAtributes) throws FileNotFoundException, IOException {
        ArrayList<Double> DatosLeidos = new ArrayList<>();
        int numFil = 0;
        for (int a = 0; a < ListArch.size(); a++) {

            File archivo = null;
            FileReader fr = null;
            BufferedReader br = null;

            ArrayList<Double> Datos;
            archivo = new File("./" + ListArch.get(a));
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            // Open the file
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

    public int[][] cargarDatosTCAsnumAtri(String arch, String strengthVal, int numAtributes) throws FileNotFoundException, IOException {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<Double> DatosLeidos = new ArrayList<>();
        ArrayList<Double> Datos;
        archivo = new File("./" + arch);
        fr = new FileReader(archivo);
        br = new BufferedReader(fr);
        // Open the file
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
