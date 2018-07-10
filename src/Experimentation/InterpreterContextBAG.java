package Experimentation;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.BaggingCA;
import weka.classifiers.trees.*;

/**
 *
 * @author SEBASTIAN VIVAS
 * @author CARLOS COBOS 
 * @author MARTHA MENDOZA 
 */
public class InterpreterContextBAG {

    public Bagging getRF() {
        Bagging baggCA = new RandomForest();
        return baggCA;
    }

    public Bagging getRF_SQRT() {
        Bagging baggCA = new RandomForest_SQRT();
        return baggCA;
    }

}
