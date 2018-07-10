package Experimentation;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import weka.classifiers.meta.BaggingCA;
import weka.classifiers.trees.*;

/**
 *
 * @author SEBASTIAN VIVAS
 * @author CARLOS COBOS 
 * @author MARTHA MENDOZA 
 */
public class InterpreterContextBAGCA {

 

    public BaggingCA getRFCA() {
        BaggingCA baggCA = new RFCA();
        return baggCA;
    }

    
}
