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
public class InterpreterClient {

    
    public InterpreterContextBAGCA icBagca;
    public InterpreterContextBAG icBag;

    public InterpreterClient(InterpreterContextBAGCA i) {
        this.icBagca = i;
    }

    public InterpreterClient(InterpreterContextBAG ibag) {
        this.icBag = ibag;
    }
    
   

    public BaggingCA interpret(String str) {
        Expression exp = null;
     
        if (str.equalsIgnoreCase("RFCA")) {
            exp = new RFCAexp();
        }
 
        return exp.interpret(icBagca);
    }

    public Bagging interpretRF(String str) {
        ExpressionRF exprf = null;
        if (str.equalsIgnoreCase("RF")) {
            exprf = new RFexp();
        }
        if (str.equalsIgnoreCase("RF_SQRT")) {
            exprf = new RF_SQRTexp();
        }

        return exprf.interpret(icBag);
    }
    
       
}
