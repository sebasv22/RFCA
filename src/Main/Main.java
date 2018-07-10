/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

 
import Experimentation.Results_RFCA;
import Experimentation.ResultsStrategy;

import java.io.File;

import java.util.ArrayList;

/**
 *
 * @author SEBASTIAN VIVAS
 * @author CARLOS COBOS 
 * @author MARTHA MENDOZA 
 */
public class Main {

    public static void main(String[] args) throws Exception {

        File archivo = new File("./");

        ArrayList<String> DatasetsList = new ArrayList<>();

        /* Datasets */
        
      /*  DatasetsList.add("banknote");
        DatasetsList.add("blood");
        DatasetsList.add("car");
        DatasetsList.add("chart");
        DatasetsList.add("climate");
        DatasetsList.add("contraceptive");
        DatasetsList.add("dermatology");
        DatasetsList.add("diabetes");
        DatasetsList.add("ecoli");
        DatasetsList.add("fertility");
        DatasetsList.add("glass");
        DatasetsList.add("haberman");
        DatasetsList.add("hayes");
        DatasetsList.add("indian");
        DatasetsList.add("ionosphere");*/
        DatasetsList.add("iris");
        /* DatasetsList.add("knowledge");
        DatasetsList.add("leaf"); 
        DatasetsList.add("libras");
        DatasetsList.add("planning");
        DatasetsList.add("qsarbiodegradation");
        DatasetsList.add("seeds");
        DatasetsList.add("segment");
        DatasetsList.add("sonar");
        DatasetsList.add("soybean");
        DatasetsList.add("spectf");
        DatasetsList.add("vowel");
        DatasetsList.add("wdbc");
        DatasetsList.add("wine");
        DatasetsList.add("wine_red");
        DatasetsList.add("yeast"); 
        DatasetsList.add("zoo"); */
         
        String fileName = "";

 
        ResultsStrategy res_rfca;
         
        res_rfca = new Results_RFCA();
        
        String path = "";
        for (int i = 0; i < DatasetsList.size(); i++) {

            fileName = DatasetsList.get(i);
            /* Address of the datasets files(.arff) */
            String dirArff = archivo.getCanonicalPath() + "\\DataSetsExp\\" + fileName + ".arff";

            long initialTime = System.currentTimeMillis();

            path = res_rfca.processAccuracy(fileName, dirArff, DatasetsList.get(i));

            System.out.println("\nDataset " + fileName + " was processed in: "
                    + +(System.currentTimeMillis() - initialTime) / 1000
                    + " sec");

        }
        
        System.out.println("\nAccording to the location of the project, the results file is available at the following address: " + path);
        

    }

}
