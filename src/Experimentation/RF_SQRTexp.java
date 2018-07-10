package Experimentation;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.BaggingCA;

/**
 *
 * @author SEBASTIAN VIVAS
 * @author CARLOS COBOS 
 * @author MARTHA MENDOZA 
 */
public class RF_SQRTexp implements ExpressionRF {

    public RF_SQRTexp() {
    }

     
    @Override
    public Bagging interpret(InterpreterContextBAG ic) {
           return ic.getRF_SQRT();
    }

 

}
