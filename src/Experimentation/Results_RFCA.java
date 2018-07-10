/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Experimentation;

import weka.classifiers.trees.rfca.ArchivoXLS;
import weka.core.Instances;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.BaggingCA;
import weka.classifiers.trees.rfca.CAs;
import weka.core.Utils;

/**
 *
 * @author SEBASTIAN VIVAS
 * @author CARLOS COBOS 
 * @author MARTHA MENDOZA 
 */
public class Results_RFCA implements ResultsStrategy { /*, Runnable {*/


    private ArrayList<ArrayList<Object>> ListResultsRFCA;
    private ArrayList<ArrayList<Object>> ListResults_Acum;
    private String nameFile;
    private String dirArff;
    private String nameDataset;
    private String nameArchSalida;
    private int seedMin;
    private int seedMax;

    public Results_RFCA() {

        this.ListResultsRFCA = new ArrayList<>();
        this.ListResults_Acum = new ArrayList<>();
        this.nameFile = "";
        this.dirArff = "";
        this.nameDataset = "";
        this.nameArchSalida = "";
        this.seedMin = 0;
        this.seedMax = 0;
    }

    public Results_RFCA(String nombreArchivo, String dirArff, String nombreDataset, String nombreArchSalida, int seedMin, int seedMax) {

        this.ListResultsRFCA = new ArrayList<>();
        this.ListResults_Acum = new ArrayList<>();
        this.nameFile = nombreArchivo;
        this.dirArff = dirArff;
        this.nameDataset = nombreDataset;
        this.nameArchSalida = nombreArchSalida;
        this.seedMin = seedMin;
        this.seedMax = seedMax;
    }

    public Results_RFCA(ArrayList<ArrayList<Object>> ListResultadosRFCA, ArrayList<ArrayList<Object>> ListResultadosRFCA_Acum, String nombreArchivo, String dirArff, String nombreDataset) {

        this.ListResultsRFCA = ListResultadosRFCA;
        this.ListResults_Acum = ListResultadosRFCA_Acum;
        this.nameFile = nombreArchivo;
        this.dirArff = dirArff;
        this.nameDataset = nombreDataset;
    }

    public double formatFourDecimals(double number) {
         
        double result = 0;
        DecimalFormat df = new DecimalFormat("#.####");
        String auxRes = df.format(number);
        auxRes = auxRes.replace(",", ".");
        result = Double.valueOf(auxRes);
        return result;
    }

    public double calcMean(double acum) {
        double mean = 0;

        mean = acum / 30;
        mean = formatFourDecimals(mean);

        return mean;
    }

    public String CreateResultsFile(ArrayList<String> ListAlgRFCA, ArrayList<String> ListAlgRF) throws IOException {

        ArchivoXLS fileXLS = new ArchivoXLS();

        ArrayList<String> LstAlg = new ArrayList<>();
        ArrayList<ArrayList<Object>> ListAlg = new ArrayList<>();
        ArrayList<Object> headlineXSL = new ArrayList<>();

        for (int i = 0; i < ListAlgRFCA.size(); i++) {
            LstAlg.add(ListAlgRFCA.get(i));
        }
        for (int i = 0; i < ListAlgRF.size(); i++) {
            LstAlg.add(ListAlgRF.get(i));
        }

        headlineXSL.add("Dataset");
        headlineXSL.add("Type Strength");
        headlineXSL.add("Number Trees");
        for (int i = 0; i < LstAlg.size(); i++) {
            headlineXSL.add(LstAlg.get(i) + "_" + "ACCURACY");
            headlineXSL.add(LstAlg.get(i) + "_" + "PCTINCORRECT");
            headlineXSL.add(LstAlg.get(i) + "_" + "PRECISION");
            headlineXSL.add(LstAlg.get(i) + "_" + "RECALL");
            headlineXSL.add(LstAlg.get(i) + "_" + "FMEASURE");
            headlineXSL.add(LstAlg.get(i) + "_" + "AUC(ROC)");
            headlineXSL.add(LstAlg.get(i) + "_" + "TIME_MIL_SEG_BUID");
            headlineXSL.add(LstAlg.get(i) + "_" + "TIME_SEG_BUID");
            headlineXSL.add(LstAlg.get(i) + "_" + "TIME_MIL_SEG_EVAL");
            headlineXSL.add(LstAlg.get(i) + "_" + "TIME_SEG_EVAL");
        }

        
        headlineXSL.add("Seed");
         

        ListAlg.add(headlineXSL);

        String path = fileXLS.writeResults("Results_RFCA", ListAlg, ListResultsRFCA);
        
        return path;
    }

     

    public String typeStrength(int numType) {
        String type = "";

        switch (numType) {
            case 2:
                type = "Strength_2";
                break;
            case 3:
                type = "Strength_3";
                break;
            case 4:
                type = "Strength_4";
                break;
            case 5:
                type = "Strength_5";
                break;
            case 6:
                type = "Strength_6";
                break;
            case 7:
                type = "Strength_7";
                break;
      
            default:
                type = "default";
                break;
        }

        return type;

    }

    /*@Override
     public void run() {
     processAccuracyThread();
     }*/
    @Override
    public String processAccuracy(String fileName, String dirarff, String datasetName) {

        BufferedReader reader = null;
        String path="";
        try {

            System.out.println(" Dataset Name: " + datasetName);
            ArchivoXLS archivoXLS = new ArchivoXLS();
            reader = new BufferedReader(
                    new FileReader(dirarff));
            Instances data = new Instances(reader);
            reader.close();
            data.setClassIndex(data.numAttributes() - 1);
            System.out.println("Number of Attributes: " + (data.numAttributes() - 1));

            int KValue = (int) Utils.log2(data.numAttributes() - 1) + 1;

            System.out.println("m_KValue with log2: " + KValue);
            int mKvalue_raiz = (int) Math.sqrt((data.numAttributes() - 1));

            System.out.println("m_KValue with sqrt: " + mKvalue_raiz);

            /* List of algorithms to be evaluated */
            ArrayList<String> ListaAlgRFCA = new ArrayList<>();
            /* RFCA */
            
            ListaAlgRFCA.add("RFCA");
             
            /* RF */
            ArrayList<String> ListaAlgRF = new ArrayList<>();
            ListaAlgRF.add("RF");
            ListaAlgRF.add("RF_SQRT");

            Classifier clsRFCA;
            Evaluation eval;
            Random rand; // using seed = 1
            int folds = 10;
            String nameDataset = datasetName;

            ArrayList<Integer> ListArchRFCA = new ArrayList<>();
            /* Strength Values to be evaluated(strength 2 to strength 7)*/
            int STRENGTH_2 = 2;
            int STRENGTH_3 = 3;
            int STRENGTH_4 = 4;
            int STRENGTH_5 = 5;
            int STRENGTH_6 = 6;
            int STRENGTH_7 = 7;

            ListArchRFCA.add(STRENGTH_2);
            ListArchRFCA.add(STRENGTH_3);
            ListArchRFCA.add(STRENGTH_4);
            ListArchRFCA.add(STRENGTH_5);
            ListArchRFCA.add(STRENGTH_6);
            ListArchRFCA.add(STRENGTH_7);

            ArrayList<Object> ListResCAs;

            int numArbolesRFR = 0;

            /* Initialization of variables to take the execution time */
            double timeMilSegBuild;
            double timeSegBuild;
            double timeMilSegEval;
            double timeSegEval;

            long initialTime;
            long finalTime;

            List<Double> listMeasures = new ArrayList<>();
            List<Double> listAcumMeasures = new ArrayList<>();

            for (int i = 0; i < ListArchRFCA.size(); i++) {

                System.out.println("\n type: " + typeStrength(ListArchRFCA.get(i)));

                InterpreterClient ecBagCa;
                ecBagCa = new InterpreterClient(new InterpreterContextBAGCA());
                InterpreterClient ecBag;
                ecBag = new InterpreterClient(new InterpreterContextBAG());

                for (int d = 1; d <= 30; d++) {
                    System.out.print(" " + d);
                    ListResCAs = new ArrayList<>();
                    ListResCAs.add(nameDataset);

                    String typeStrength = typeStrength(ListArchRFCA.get(i));
                    ListResCAs.add(typeStrength);

                    for (int j = 0; j < ListaAlgRFCA.size(); j++) {
                       
                        BaggingCA baggCA = ecBagCa.interpret(ListaAlgRFCA.get(j));
                        rand = new Random(1);
                        baggCA.setStrengthVal(ListArchRFCA.get(i));
                        baggCA.setSeed(d);

                        initialTime = System.currentTimeMillis();
                        baggCA.buildClassifier(data);
                        finalTime = (System.currentTimeMillis() - initialTime);

                        timeMilSegBuild = finalTime;
                        timeSegBuild = timeMilSegBuild / 1000;

                        initialTime = System.currentTimeMillis();

                        clsRFCA = baggCA;
                        eval = new Evaluation(data);
                        eval.crossValidateModel(clsRFCA, data, folds, rand);

                        finalTime = (System.currentTimeMillis() - initialTime);

                        timeMilSegEval = finalTime;
                        timeSegEval = timeMilSegEval / 1000;

                        listMeasures.add(formatFourDecimals(eval.pctCorrect()));
                        listMeasures.add(formatFourDecimals(eval.pctIncorrect()));
                        listMeasures.add(formatFourDecimals(eval.weightedPrecision()));
                        listMeasures.add(formatFourDecimals(eval.weightedRecall()));
                        listMeasures.add(formatFourDecimals(eval.weightedFMeasure()));
                        listMeasures.add(formatFourDecimals(eval.weightedAreaUnderROC()));
                        listMeasures.add(timeMilSegBuild);
                        listMeasures.add(timeSegBuild);
                        listMeasures.add(timeMilSegEval);
                        listMeasures.add(timeSegEval);
                        
                         
                    }
                    
                       numArbolesRFR = CAs.getInstance().getMatrixCA().length;

                    for (int j = 0; j < ListaAlgRF.size(); j++) {
 
                        Bagging bagg = ecBag.interpretRF(ListaAlgRF.get(j));
                        rand = new Random(1);
                        
                        bagg.setNumIterations(numArbolesRFR);
                        bagg.setSeed(d);

                        initialTime = System.currentTimeMillis();
                        bagg.buildClassifier(data);
                        finalTime = (System.currentTimeMillis() - initialTime);

                        timeMilSegBuild = finalTime;
                        timeSegBuild = timeMilSegBuild / 1000;

                        initialTime = System.currentTimeMillis();

                        clsRFCA = bagg;
                        eval = new Evaluation(data);
                        eval.crossValidateModel(clsRFCA, data, folds, rand);

                        finalTime = (System.currentTimeMillis() - initialTime);

                        timeMilSegEval = finalTime;
                        timeSegEval = timeMilSegEval / 1000;

                        listMeasures.add(formatFourDecimals(eval.pctCorrect()));
                        listMeasures.add(formatFourDecimals(eval.pctIncorrect()));
                        listMeasures.add(formatFourDecimals(eval.weightedPrecision()));
                        listMeasures.add(formatFourDecimals(eval.weightedRecall()));
                        listMeasures.add(formatFourDecimals(eval.weightedFMeasure()));
                        listMeasures.add(formatFourDecimals(eval.weightedAreaUnderROC()));
                        listMeasures.add(timeMilSegBuild);
                        listMeasures.add(timeSegBuild);
                        listMeasures.add(timeMilSegEval);
                        listMeasures.add(timeSegEval);

                    }
                    /* If this is the first iteration, the value is added to the cumulative list of measurements */
                    if (d == 1) {

                        for (int j = 0; j < listMeasures.size(); j++) {
                            listAcumMeasures.add(listMeasures.get(j));
                        }

                    } /*  If it is an iteration other than the first iteration it accumulates 
                     the value in the cumulative list of measures*/ else {
                        for (int j = 0; j < listAcumMeasures.size(); j++) {
                            double auxMeasure = listAcumMeasures.get(j) + listMeasures.get(j);
                            listAcumMeasures.set(j, auxMeasure);
                        }
                    }
                    /* The tasks are added to the list for the purpose of subsequently
                     write them in xsl format*/
                    ListResCAs.add(numArbolesRFR);
                    for (int j = 0; j < listMeasures.size(); j++) {
                        ListResCAs.add(listMeasures.get(j));
                    }
                    /* Resetting the measurement list */
                    listMeasures.clear();
                    /* Seed Writing */
                    ListResCAs.add(d);
                    ListResultsRFCA.add(ListResCAs);

                }

                ListResCAs = new ArrayList<>();
                ListResCAs.add(nameDataset);

                String typeStrength = typeStrength(ListArchRFCA.get(i));
                ListResCAs.add(typeStrength + "_Mean");

                // numArbolesRFR = CAs.getInstance().getMatrizCA().length;
                ListResCAs.add(numArbolesRFR);
                for (int j = 0; j < listAcumMeasures.size(); j++) {
                    double acumMeasure = listAcumMeasures.get(j);
                    ListResCAs.add(calcMean(acumMeasure));
                }
                /* Resetting Cumulative Measurements List */
                listAcumMeasures.clear();
                /* Mean Seed Writing */
                ListResCAs.add("Mean Seed");
                ListResultsRFCA.add(ListResCAs);
            }


            /* Create File with Results*/
            path = CreateResultsFile(ListaAlgRFCA, ListaAlgRF);

        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } catch (Exception ex) {
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(Results_RFCA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         
        return path;
    }

    
    /* Getters and Setters */
    public ArrayList<ArrayList<Object>> getListResultsRFCA() {
        return ListResultsRFCA;
    }

    public void setListResultsRFCA(ArrayList<ArrayList<Object>> ListResultsRFCA) {
        this.ListResultsRFCA = ListResultsRFCA;
    }

    public ArrayList<ArrayList<Object>> getListResults_Acum() {
        return ListResults_Acum;
    }

    public void setListResults_Acum(ArrayList<ArrayList<Object>> ListResults_Acum) {
        this.ListResults_Acum = ListResults_Acum;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getNameDataset() {
        return nameDataset;
    }

    public void setNameDataset(String nameDataset) {
        this.nameDataset = nameDataset;
    }

    public String getNameArchSalida() {
        return nameArchSalida;
    }

    public void setNameArchSalida(String nameArchSalida) {
        this.nameArchSalida = nameArchSalida;
    }
    
    public String getDirArff() {
        return dirArff;
    }

    public void setDirArff(String dirArff) {
        this.dirArff = dirArff;
    }

    public int getSeedMin() {
        return seedMin;
    }

    public void setSeedMin(int seedMin) {
        this.seedMin = seedMin;
    }

    public int getSeedMax() {
        return seedMax;
    }

    public void setSeedMax(int seedMax) {
        this.seedMax = seedMax;
    }
    
}
