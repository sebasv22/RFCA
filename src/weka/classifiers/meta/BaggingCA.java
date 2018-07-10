/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 *    BaggingCA.java
 *    University of Cauca
 *
 */
package weka.classifiers.meta;

import weka.classifiers.trees.rfca.CAs;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.classifiers.RandomizableParallelIteratedSingleClassifierEnhancer;
import weka.classifiers.evaluation.Evaluation;
import weka.core.AdditionalMeasureProducer;
import weka.core.Aggregateable;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.Randomizable;
import weka.core.RevisionUtils;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;
import weka.core.TechnicalInformationHandler;
import weka.core.Utils;
import weka.core.WeightedInstancesHandler;
import weka.core.PartitionGenerator;
import weka.core.SelectedTag;
import weka.core.Tag;

/**
 * <!-- globalinfo-start --> Class for baggingCA a classifier to reduce variance. Can do classification and
 * regression depending on the base learner, This classifier is constructed using 
 * the Strength value chosen. <br/>
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
<!-- technical-plaintext-start -->
Class for baggingCA a classifier to reduce variance. Can do classification and
 * regression depending on the base learner, This classifier is constructed using 
 * the Strength value chose
<!-- technical-plaintext-end -->
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
 * Valid options are: <p/>
 * 
 * <pre> -strength
 *  Set the strength value of the binary covering array to use. (default 5)</pre>
 *
 * <pre> -P
 *  Size of each bag, as a percentage of the
 *  training set size. (default 100)</pre>
 *
 * <pre> -O
 *  Calculate the out of bag error.</pre>
 *
 * <pre> -print
 *  Print the individual classifiers in the output</pre>
 *
 * <pre> -store-out-of-bag-predictions
 *  Whether to store out of bag predictions in internal evaluation object.</pre>
 *
 * <pre> -output-out-of-bag-complexity-statistics
 *  Whether to output complexity-based statistics when out-of-bag evaluation is performed.</pre>
 *
 * <pre> -represent-copies-using-weights
 *  Represent copies of instances using weights rather than explicitly.</pre>
 *
 * <pre> -S &lt;num&gt;
 *  Random number seed.
 *  (default 1)</pre>
 *
 * <pre> -num-slots &lt;num&gt;
 *  Number of execution slots.
 *  (default 1 - i.e. no parallelism)</pre>
 *
 * <pre> -I &lt;num&gt;
 *  Number of iterations.
 *  (default 10)</pre>
 *
 * <pre> -D
 *  If set, classifier is run in debug mode and
 *  may output additional info to the console</pre>
 *
 * <pre> -W
 *  Full name of base classifier.
 *  (default: weka.classifiers.trees.REPTree)</pre>
 *
 * <pre>
 * Options specific to classifier weka.classifiers.trees.REPTree:
 * </pre>
 *
 * <pre> -M &lt;minimum number of instances&gt;
 *  Set minimum number of instances per leaf (default 2).</pre>
 *
 * <pre> -V &lt;minimum variance for split&gt;
 *  Set minimum numeric class variance proportion
 *  of train variance for split (default 1e-3).</pre>
 *
 * <pre> -N &lt;number of folds&gt;
 *  Number of folds for reduced error pruning (default 3).</pre>
 *
 * <pre> -S &lt;seed&gt;
 *  Seed for random data shuffling (default 1).</pre>
 *
 * <pre> -P
 *  No pruning.</pre>
 *
 * <pre> -L
 *  Maximum tree depth (default -1, no maximum)</pre>
 *
 * <pre> -I
 *  Initial class value count (default 0)</pre>
 *
 * <pre> -R
 *  Spread initial count over all class values (i.e. don't use 1 per value)</pre>
 *
 * <!-- options-end -->
 *
 * Options after -- are passed to the designated classifier.<p>
 *
 * @author Sebastian Vivas 
 * @author Carlos Cobos  
 * @author Martha Mendoza
 * @version $Revision: 2222 $
 */
public class BaggingCA
        extends RandomizableParallelIteratedSingleClassifierEnhancer
        implements WeightedInstancesHandler, AdditionalMeasureProducer,
        TechnicalInformationHandler, PartitionGenerator, Aggregateable<BaggingCA> {

    /**
     * for serialization
     */
    static final long serialVersionUID = -115879962237199703L;
    
        /**
     * Strength value (accumulated or not) to chose.
     */
    public static final int STRENGTH_2 = 2;
    public static final int STRENGTH_3 = 3;
    public static final int STRENGTH_4 = 4;
    public static final int STRENGTH_5 = 5;
    public static final int STRENGTH_6 = 6;
    public static final int STRENGTH_7 = 7;
     
    public static final Tag[] TAGS_STRENGTH = {
        new Tag(STRENGTH_2, "Strength 2"),
        new Tag(STRENGTH_3, "Strength 3"),
        new Tag(STRENGTH_4, "Strength 4"),
        new Tag(STRENGTH_5, "Strength 5"),
        new Tag(STRENGTH_6, "Strength 6"),
        new Tag(STRENGTH_7, "Strength 7"),
         
    };

    
    /**
     * Strength value of the coverign array(CA) to be used
     *
     * @param m_strengthValue the Strength value to use
     *
     */
  
    protected int m_strengthValue = STRENGTH_5;
    

    /**
     * The size of each bag sample, as a percentage of the training size
     */
    protected int m_BagSizePercent = 100;

    /**
     * Whether to calculate the out of bag error
     */
    protected boolean m_CalcOutOfBag = false;

    /**
     * Whether to represent copies of instances using weights rather than
     * explicitly
     */
    protected boolean m_RepresentUsingWeights = false;

    /**
     * The evaluation object holding the out of bag error, etc.
     */
    protected Evaluation m_OutOfBagEvaluationObject = null;

    /**
     * Whether to store the out of bag predictions in the evaluation object.
     */
    private boolean m_StoreOutOfBagPredictions = false;

    /**
     * Whether to output complexity-based statistics when OOB-evaluation is
     * performed.
     */
    private boolean m_OutputOutOfBagComplexityStatistics;

    /**
     * Whether class is numeric.
     */
    private boolean m_Numeric = false;

    /**
     * Whether to print individual ensemble members in output.
     */
    private boolean m_printClassifiers;

    /**
     * Random number generator
     */
    protected Random m_random;

    /**
     * Used to indicate whether an instance is in a bag or not
     */
    protected boolean[][] m_inBag;

    /**
     * Reference to the training data
     */
    protected Instances m_data;


    
        /**
     * Coverign Array(Ca) - Instance of the CA class representing Covering Arrays
     *
     * @param Ca 
     *
     */
    private CAs Ca;

    /**
     * Set the strength value of the binary covering array to use
     *
     * @param StrengthSel the Strength value to use
     *
     */
    public void setStrengthValue(SelectedTag StrengthSel) {
        if (StrengthSel.getTags() == TAGS_STRENGTH) {
            m_strengthValue = StrengthSel.getSelectedTag().getID();
        }

    }

    public void setStrengthVal(int strengthSel) {
        m_strengthValue = strengthSel;

    }

    @Override
    public void setNumIterations(int numIterations) {
        m_NumIterations = numIterations;

    }

    /**
     * Gets the number of bagging iterations
     *
     * @return the maximum number of bagging iterations
     */
    @Override
    public int getNumIterations() {

        return m_NumIterations;
    }

    /**
     * Returns the tip text for this property
     *
     * @return tip text for this property suitable for displaying in the
     * explorer/experimenter gui
     */
    public String NumIterationsTipText() {
        return "Corresponds to number of iterations BaggingCA";
    }
    
    
    /**
     * Get the strength value of the binary covering array to use
     *
     * @return the Strength value corresponding to the strength value selected in the parameter m_strengthValue 
     */
    public SelectedTag getStrengthValue() {
        return new SelectedTag(m_strengthValue, TAGS_STRENGTH);
    }
    
      /**
     * Returns the tip text for this property
     *
     * @return tip text for this property suitable for displaying in the
     * explorer/experimenter gui
     */
    
    public String strengthValueTipText() {
        return "Strength value of the binary covering array to use";
    }

    /**
     * Constructor.
     */
    public BaggingCA() {
       // System.out.println("");
        m_Classifier = new weka.classifiers.trees.REPTree();
       // System.out.println("");
    }

    /**
     * Returns a string describing classifier
     *
     * @return a description suitable for displaying in the
     * explorer/experimenter gui
     */
    public String globalInfo() {

        return "Class for bagging CA(Covering Array) a classifier to reduce variance. Can do classification "
                + "and regression depending on the base learner. \n\n"
                + "For more information, see\n\n"
                + getTechnicalInformation().toString();
    }

    /**
     * Returns an instance of a TechnicalInformation object, containing detailed
     * information about the technical background of this class, e.g., paper
     * reference or book this class is based on.
     *
     * @return the technical information about this class
     */
    @Override
    public TechnicalInformation getTechnicalInformation() {
    TechnicalInformation result;

    result = new TechnicalInformation(Type.ARTICLE);
    result.setValue(Field.AUTHOR, "Sebastian Vivas, Carlos Cobos and Martha Mendoza");
    result.setValue(Field.MONTH, "September");
    result.setValue(Field.YEAR, "2018");
    result.setValue(Field.TITLE, "Covering arrays to support the process of feature selection in the Random Forest classifier");
    result.setValue(Field.ORGANIZATION, "LOD");
    result.setValue(Field.JOURNAL, "The Fourth International Conference on Machine Learning, Optimization, and Data Science");

    return result;
  }
    
    

    /**
     * String describing default classifier.
     *
     * @return the default classifier classname
     */
    @Override
    protected String defaultClassifierString() {

        return "weka.classifiers.trees.REPTree";
    }

    /**
     * Returns an enumeration describing the available options.
     *
     * @return an enumeration of all the available options.
     */
    @Override
    public Enumeration<Option> listOptions() {

        Vector<Option> newVector = new Vector<Option>(4);
        
              newVector.addElement(new Option(
                "\t Set the strength value of the binary covering array to use. (default 5)",
                "strength", 1, "-strength"));

        newVector.addElement(new Option(
                "\tSize of each bag, as a percentage of the\n"
                + "\ttraining set size. (default 100)",
                "P", 1, "-P"));
        newVector.addElement(new Option(
                "\tCalculate the out of bag error.",
                "O", 0, "-O"));
        newVector.addElement(new Option(
                "\tWhether to store out of bag predictions in internal evaluation object.",
                "store-out-of-bag-predictions", 0, "-store-out-of-bag-predictions"));
        newVector.addElement(new Option(
                "\tWhether to output complexity-based statistics when out-of-bag evaluation is performed.",
                "output-out-of-bag-complexity-statistics", 0, "-output-out-of-bag-complexity-statistics"));
        newVector.addElement(new Option(
                "\tRepresent copies of instances using weights rather than explicitly.",
                "represent-copies-using-weights", 0, "-represent-copies-using-weights"));
        newVector.addElement(new Option(
                "\tPrint the individual classifiers in the output", "print", 0, "-print"));

        newVector.addAll(Collections.list(super.listOptions()));

        return newVector.elements();
    }

    /**
     * Parses a given list of options.
     * <p/>
     *
     * <!-- options-start -->
     * Valid options are:
     * <p/>
     *
     * <pre> -P
     *  Size of each bag, as a percentage of the
     *  training set size. (default 100)</pre>
     *
     * <pre> -O
     *  Calculate the out of bag error.</pre>
     *
     * <pre> -print
     *  Print the individual classifiers in the output</pre>
     *
     * <pre> -store-out-of-bag-predictions
     *  Whether to store out of bag predictions in internal evaluation object.</pre>
     *
     * <pre> -output-out-of-bag-complexity-statistics
     *  Whether to output complexity-based statistics when out-of-bag evaluation is performed.</pre>
     *
     * <pre> -represent-copies-using-weights
     *  Represent copies of instances using weights rather than explicitly.</pre>
     *
     * <pre> -S &lt;num&gt;
     *  Random number seed.
     *  (default 1)</pre>
     *
     * <pre> -num-slots &lt;num&gt;
     *  Number of execution slots.
     *  (default 1 - i.e. no parallelism)</pre>
     *
     * <pre> -I &lt;num&gt;
     *  Number of iterations.
     *  (default 10)</pre>
     *
     * <pre> -D
     *  If set, classifier is run in debug mode and
     *  may output additional info to the console</pre>
     *
     * <pre> -W
     *  Full name of base classifier.
     *  (default: weka.classifiers.trees.REPTree)</pre>
     *
     * <pre>
     * Options specific to classifier weka.classifiers.trees.REPTree:
     * </pre>
     *
     * <pre> -M &lt;minimum number of instances&gt;
     *  Set minimum number of instances per leaf (default 2).</pre>
     *
     * <pre> -V &lt;minimum variance for split&gt;
     *  Set minimum numeric class variance proportion
     *  of train variance for split (default 1e-3).</pre>
     *
     * <pre> -N &lt;number of folds&gt;
     *  Number of folds for reduced error pruning (default 3).</pre>
     *
     * <pre> -S &lt;seed&gt;
     *  Seed for random data shuffling (default 1).</pre>
     *
     * <pre> -P
     *  No pruning.</pre>
     *
     * <pre> -L
     *  Maximum tree depth (default -1, no maximum)</pre>
     *
     * <pre> -I
     *  Initial class value count (default 0)</pre>
     *
     * <pre> -R
     *  Spread initial count over all class values (i.e. don't use 1 per value)</pre>
     *
     * <!-- options-end -->
     *
     * Options after -- are passed to the designated classifier.<p>
     *
     * @param options the list of options as an array of strings
     * @throws Exception if an option is not supported
     */
    @Override
    public void setOptions(String[] options) throws Exception {
 

        String strength = Utils.getOption("strength", options);
        if ( strength.equalsIgnoreCase("2") )  {
             setStrengthValue(new SelectedTag(STRENGTH_2, TAGS_STRENGTH));
        }
        if ( strength.equalsIgnoreCase("3") )  {
             setStrengthValue(new SelectedTag(STRENGTH_3, TAGS_STRENGTH));
        }  
        if ( strength.equalsIgnoreCase("4") )  {
             setStrengthValue(new SelectedTag(STRENGTH_4, TAGS_STRENGTH));
        }  
        if ( strength.equalsIgnoreCase("5") )  {
             setStrengthValue(new SelectedTag(STRENGTH_5, TAGS_STRENGTH));
        }  
        if ( strength.equalsIgnoreCase("6") )  {
             setStrengthValue(new SelectedTag(STRENGTH_6, TAGS_STRENGTH));
        }  
        if ( strength.equalsIgnoreCase("7") )  {
            setStrengthValue(new SelectedTag(STRENGTH_7, TAGS_STRENGTH));
        }  
          

        String bagSize = Utils.getOption('P', options);
        if (bagSize.length() != 0) {
            setBagSizePercent(Integer.parseInt(bagSize));
        } else {
            setBagSizePercent(100);
        }

        setCalcOutOfBag(Utils.getFlag('O', options));

        setStoreOutOfBagPredictions(Utils.getFlag("store-out-of-bag-predictions", options));

        setOutputOutOfBagComplexityStatistics(Utils.getFlag("output-out-of-bag-complexity-statistics", options));

        setRepresentCopiesUsingWeights(Utils.getFlag("represent-copies-using-weights", options));

        setPrintClassifiers(Utils.getFlag("print", options));

        super.setOptions(options);

        Utils.checkForRemainingOptions(options);
    }

    /**
     * Gets the current settings of the Classifier.
     *
     * @return an array of strings suitable for passing to setOptions
     */
    @Override
    public String[] getOptions() {

        Vector<String> options = new Vector<String>();

        switch (m_strengthValue) {

            case STRENGTH_2:
                options.add("-strength");
                options.add("" + getStrengthValue());
                break;
            case STRENGTH_3:
                options.add("-strength");
                options.add("" + getStrengthValue());
                break;
            case STRENGTH_4:
                options.add("-strength");
                options.add("" + getStrengthValue());
                break;
            case STRENGTH_5:
                options.add("-strength");
                options.add("" + getStrengthValue());
                break;
            case STRENGTH_6:
                options.add("-strength");
                options.add("" + getStrengthValue());
                break;
                case STRENGTH_7:
                options.add("-strength");
                options.add("" + getStrengthValue());
                break;
        }

        options.add("-P");
        options.add("" + getBagSizePercent());

        if (getCalcOutOfBag()) {
            options.add("-O");
        }

        if (getStoreOutOfBagPredictions()) {
            options.add("-store-out-of-bag-predictions");
        }

        if (getOutputOutOfBagComplexityStatistics()) {
            options.add("-output-out-of-bag-complexity-statistics");
        }

        if (getRepresentCopiesUsingWeights()) {
            options.add("-represent-copies-using-weights");
        }

        if (getPrintClassifiers()) {
            options.add("-print");
        }

        Collections.addAll(options, super.getOptions());

        return options.toArray(new String[0]);
    }

    /**
     * Returns the tip text for this property
     *
     * @return tip text for this property suitable for displaying in the
     * explorer/experimenter gui
     */
    public String bagSizePercentTipText() {
        return "Size of each bag, as a percentage of the training set size.";
    }

    /**
     * Gets the size of each bag, as a percentage of the training set size.
     *
     * @return the bag size, as a percentage.
     */
    public int getBagSizePercent() {

        return m_BagSizePercent;
    }

    /**
     * Sets the size of each bag, as a percentage of the training set size.
     *
     * @param newBagSizePercent the bag size, as a percentage.
     */
    public void setBagSizePercent(int newBagSizePercent) {

        m_BagSizePercent = newBagSizePercent;
    }

    /**
     * Returns the tip text for this property
     *
     * @return tip text for this property suitable for displaying in the
     * explorer/experimenter gui
     */
    public String representCopiesUsingWeightsTipText() {
        return "Whether to represent copies of instances using weights rather than explicitly.";
    }

    /**
     * Set whether copies of instances are represented using weights rather than
     * explicitly.
     *
     * @param representUsingWeights whether to represent copies using weights
     */
    public void setRepresentCopiesUsingWeights(boolean representUsingWeights) {

        m_RepresentUsingWeights = representUsingWeights;
    }

    /**
     * Get whether copies of instances are represented using weights rather than
     * explicitly.
     *
     * @return whether copies of instances are represented using weights rather
     * than explicitly
     */
    public boolean getRepresentCopiesUsingWeights() {

        return m_RepresentUsingWeights;
    }

    /**
     * Returns the tip text for this property
     *
     * @return tip text for this property suitable for displaying in the
     * explorer/experimenter gui
     */
    public String storeOutOfBagPredictionsTipText() {
        return "Whether to store the out-of-bag predictions.";
    }

    /**
     * Set whether the out of bag predictions are stored.
     *
     * @param storeOutOfBag whether the out of bag predictions are stored
     */
    public void setStoreOutOfBagPredictions(boolean storeOutOfBag) {

        m_StoreOutOfBagPredictions = storeOutOfBag;
    }

    /**
     * Get whether the out of bag predictions are stored.
     *
     * @return whether the out of bag predictions are stored
     */
    public boolean getStoreOutOfBagPredictions() {

        return m_StoreOutOfBagPredictions;
    }

    /**
     * Returns the tip text for this property
     *
     * @return tip text for this property suitable for displaying in the
     * explorer/experimenter gui
     */
    public String calcOutOfBagTipText() {
        return "Whether the out-of-bag error is calculated.";
    }

    /**
     * Set whether the out of bag error is calculated.
     *
     * @param calcOutOfBag whether to calculate the out of bag error
     */
    public void setCalcOutOfBag(boolean calcOutOfBag) {

        m_CalcOutOfBag = calcOutOfBag;
    }

    /**
     * Get whether the out of bag error is calculated.
     *
     * @return whether the out of bag error is calculated
     */
    public boolean getCalcOutOfBag() {

        return m_CalcOutOfBag;
    }

    /**
     * Returns the tip text for this property
     *
     * @return tip text for this property suitable for displaying in the
     * explorer/experimenter gui
     */
    public String outputOutOfBagComplexityStatisticsTipText() {

        return "Whether to output complexity-based statistics when out-of-bag evaluation is performed.";
    }

    /**
     * Gets whether complexity statistics are output when OOB estimation is
     * performed.
     *
     * @return whether statistics are calculated
     */
    public boolean getOutputOutOfBagComplexityStatistics() {

        return m_OutputOutOfBagComplexityStatistics;
    }

    /**
     * Sets whether complexity statistics are output when OOB estimation is
     * performed.
     *
     * @param b whether statistics are calculated
     */
    public void setOutputOutOfBagComplexityStatistics(boolean b) {

        m_OutputOutOfBagComplexityStatistics = b;
    }

    /**
     * Returns the tip text for this property
     *
     * @return tip text for this property suitable for displaying in the
     * explorer/experimenter gui
     */
    public String printClassifiersTipText() {
        return "Print the individual classifiers in the output";
    }

    /**
     * Set whether to print the individual ensemble classifiers in the output
     *
     * @param print true if the individual classifiers are to be printed
     */
    public void setPrintClassifiers(boolean print) {
        m_printClassifiers = print;
    }

    /**
     * Get whether to print the individual ensemble classifiers in the output
     *
     * @return true if the individual classifiers are to be printed
     */
    public boolean getPrintClassifiers() {
        return m_printClassifiers;
    }

    /**
     * Gets the out of bag error that was calculated as the classifier was
     * built. Returns error rate in classification case and mean absolute error
     * in regression case.
     *
     * @return the out of bag error; -1 if out-of-bag-error has not be estimated
     */
    public double measureOutOfBagError() {

        if (m_OutOfBagEvaluationObject == null) {
            return -1;
        }
        if (m_Numeric) {
            return m_OutOfBagEvaluationObject.meanAbsoluteError();
        } else {
            return m_OutOfBagEvaluationObject.errorRate();
        }
    }

    /**
     * Returns an enumeration of the additional measure names.
     *
     * @return an enumeration of the measure names
     */
    @Override
    public Enumeration<String> enumerateMeasures() {

        Vector<String> newVector = new Vector<String>(1);
        newVector.addElement("measureOutOfBagError");
        return newVector.elements();
    }

    /**
     * Returns the value of the named measure.
     *
     * @param additionalMeasureName the name of the measure to query for its
     * value
     * @return the value of the named measure
     * @throws IllegalArgumentException if the named measure is not supported
     */
    @Override
    public double getMeasure(String additionalMeasureName) {

        if (additionalMeasureName.equalsIgnoreCase("measureOutOfBagError")) {
            return measureOutOfBagError();
        } else {
            throw new IllegalArgumentException(additionalMeasureName
                    + " not supported (Bagging)");
        }
    }

    /**
     * Returns a training set for a particular iteration.
     *
     * @param iteration the number of the iteration for the requested training
     * set.
     * @return the training set for the supplied iteration number
     * @throws Exception if something goes wrong when generating a training set.
     */
    @Override
    protected synchronized Instances getTrainingSet(int iteration) throws Exception {
        int bagSize = (int) (m_data.numInstances() * (m_BagSizePercent / 100.0));
        Instances bagData = null;
        Random r = new Random(m_Seed + iteration);

        // create the in-bag dataset
        if (m_CalcOutOfBag) {
            m_inBag[iteration] = new boolean[m_data.numInstances()];
            bagData = m_data.resampleWithWeights(r, m_inBag[iteration], getRepresentCopiesUsingWeights());
        } else {
            if (bagSize < m_data.numInstances()) {
                bagData = m_data.resampleWithWeights(r, false); // Need to turn off representation using weights in this case.
                bagData.randomize(r);
                Instances newBagData = new Instances(bagData, 0, bagSize);
                bagData = newBagData;
            } else {
                bagData = m_data.resampleWithWeights(r, getRepresentCopiesUsingWeights());
            }
        }
       /*  System.out.println("  bagData ");
        for (int i = 0; i < bagData.size(); i++) {
            System.out.println("  Atr 0: "+bagData.get(i).toString(0) +"\t Atr 1: "+bagData.get(i).toString(1) +"\t Atr 2: "+bagData.get(i).toString(2)+"\t Atr 3: "+bagData.get(i).toString(3) +"\t Atr 4: "+bagData.get(i).toString(4) +"\t Atr 5: "+bagData.get(i).toString(5) +"\t Clase: "+bagData.get(i).toString(6)   );
             
        }*/
        
        return bagData;
    }

    /**
     * Returns the out-of-bag evaluation object.
     *
     * @return the out-of-bag evaluation object; null if out-of-bag error hasn't
     * been calculated
     */
    public Evaluation getOutOfBagEvaluationObject() {

        return m_OutOfBagEvaluationObject;
    }

    /**
     * Bagging method.
     *
     * @param data the training data to be used for generating the bagged
     * classifier.
     * @throws Exception if the classifier could not be built successfully
     */
    @Override
    public void buildClassifier(Instances data) throws Exception {
        
    
        
     /* strengthList - Stores the address list of covering arrays to use */
        ArrayList<String> strengthList = new ArrayList<>();
        
        int numAttributes = data.numAttributes() - 1;
        
       /* Obtain Instance of the Coverign Array class */
        Ca = CAs.getInstance();
        
        // Direction of covering arrays files, used in tree construction
        String dirCAs = "CAs\\CA\\";
        String ArchName = "";

       /* Depending on the strength value selected, it is added to strengthList
          which file or files of covering array  to use */
        
        switch (m_strengthValue) {
            
        /* From strength 2 to strength 7 corresponds to covering arrays */
            case STRENGTH_2:
                strengthList.add("strength_2");
                break;
            case STRENGTH_3:
                strengthList.add("strength_3");
                break;
            case STRENGTH_4:
                strengthList.add("strength_4");
                break;
            case STRENGTH_5:
                strengthList.add("strength_5");
                break;
            case STRENGTH_6:
                strengthList.add("strength_6");
                break;
            case STRENGTH_7:
                strengthList.add("strength_7");
                break;
 
 
        }
        /* Obtain the CA disk addresses  corresponding to the number of Dataset attributes */
        ArrayList<String> ArchList = Ca.getArchList(strengthList, numAttributes);
        
        /* sets the CA corresponding to the number of Dataset attributes */
        Ca.setCAmatrix(ArchList, numAttributes);
        
        /* set the number of iterations from the number of rows of the selected CA  */
        setNumIterations(Ca.getNumRows());

 
        getCapabilities().testWithFail(data);

        // Has user asked to represent copies using weights?
        if (getRepresentCopiesUsingWeights() && !(m_Classifier instanceof WeightedInstancesHandler)) {
            throw new IllegalArgumentException("Cannot represent copies using weights when "
                    + "base learner in bagging does not implement "
                    + "WeightedInstancesHandler.");
        }

        // get fresh Instances object
        m_data = new Instances(data);

        super.buildClassifier(m_data);

        if (m_CalcOutOfBag && (m_BagSizePercent != 100)) {
            throw new IllegalArgumentException("Bag size needs to be 100% if "
                    + "out-of-bag error is to be calculated!");
        }

        m_random = new Random(m_Seed);

        m_inBag = null;
        if (m_CalcOutOfBag) {
            m_inBag = new boolean[m_Classifiers.length][];
        }

        for (int j = 0; j < m_Classifiers.length; j++) {
            if (m_Classifier instanceof Randomizable) {
                ((Randomizable) m_Classifiers[j]).setSeed(m_random.nextInt());
            }
        }

        m_Numeric = m_data.classAttribute().isNumeric();

        buildClassifiers();

        // calc OOB error?
        if (getCalcOutOfBag()) {
            m_OutOfBagEvaluationObject = new Evaluation(m_data);

            for (int i = 0; i < m_data.numInstances(); i++) {
                double[] votes;
                if (m_Numeric) {
                    votes = new double[1];
                } else {
                    votes = new double[m_data.numClasses()];
                }

                // determine predictions for instance
                int voteCount = 0;
                for (int j = 0; j < m_Classifiers.length; j++) {
                    if (m_inBag[j][i]) {
                        continue;
                    }

                    if (m_Numeric) {
                        double pred = m_Classifiers[j].classifyInstance(m_data.instance(i));
                        if (!Utils.isMissingValue(pred)) {
                            votes[0] += pred;
                            voteCount++;
                        }
                    } else {
                        voteCount++;
                        double[] newProbs = m_Classifiers[j].distributionForInstance(m_data.instance(i));
                        // sum the probability estimates
                        for (int k = 0; k < newProbs.length; k++) {
                            votes[k] += newProbs[k];
                        }
                    }
                }

                // "vote"
                if (m_Numeric) {
                    if (voteCount > 0) {
                        votes[0] /= voteCount;
                        m_OutOfBagEvaluationObject.evaluationForSingleInstance(votes, m_data.instance(i), getStoreOutOfBagPredictions());
                    }
                } else {
                    double sum = Utils.sum(votes);
                    if (sum > 0) {
                        Utils.normalize(votes, sum);
                        m_OutOfBagEvaluationObject.evaluationForSingleInstance(votes, m_data.instance(i), getStoreOutOfBagPredictions());
                    }
                }
            }
        } else {
            m_OutOfBagEvaluationObject = null;
        }

        // save memory
        m_inBag = null;
        m_data = new Instances(m_data, 0);
        
        /* Resets the auxiliary variable indicating the row to be used in the construction of a tree */
        CAs.getInstance().setCurrentRow(-1);
    }

    /**
     * Calculates the class membership probabilities for the given test
     * instance.
     *
     * @param instance the instance to be classified
     * @return preedicted class probability distribution
     * @throws Exception if distribution can't be computed successfully
     */
    @Override
    public double[] distributionForInstance(Instance instance) throws Exception {

        double[] sums = new double[instance.numClasses()], newProbs;

        double numPreds = 0;
        for (int i = 0; i < m_NumIterations; i++) {
            if (m_Numeric) {
                double pred = m_Classifiers[i].classifyInstance(instance);
                if (!Utils.isMissingValue(pred)) {
                    sums[0] += pred;
                    numPreds++;
                }
            } else {
                newProbs = m_Classifiers[i].distributionForInstance(instance);
                for (int j = 0; j < newProbs.length; j++) {
                    sums[j] += newProbs[j];
                }
            }
        }
        if (m_Numeric) {
            if (numPreds == 0) {
                sums[0] = Utils.missingValue();
            } else {
                sums[0] /= numPreds;
            }
            return sums;
        } else if (Utils.eq(Utils.sum(sums), 0)) {
            return sums;
        } else {
            Utils.normalize(sums);
            return sums;
        }
    }

    /**
     * Returns description of the bagged classifier.
     *
     * @return description of the bagged classifier as a string
     */
    @Override
    public String toString() {

        if (m_Classifiers == null) {
            return "Bagging: No model built yet.";
        }
        StringBuffer text = new StringBuffer();
        text.append("BaggingCA with " + getNumIterations() + " iterations and base learner\n\n" + getClassifierSpec());
        if (getPrintClassifiers()) {
            text.append("All the base classifiers: \n\n");
            for (int i = 0; i < m_Classifiers.length; i++) {
                text.append(m_Classifiers[i].toString() + "\n\n");
            }
        }
        if (m_CalcOutOfBag) {
            text.append(m_OutOfBagEvaluationObject.toSummaryString("\n\n*** Out-of-bag estimates ***\n", getOutputOutOfBagComplexityStatistics()));
        }

        return text.toString();
    }

    /**
     * Builds the classifier to generate a partition.
     */
    @Override
    public void generatePartition(Instances data) throws Exception {

        if (m_Classifier instanceof PartitionGenerator) {
            buildClassifier(data);
        } else {
            throw new Exception("Classifier: " + getClassifierSpec()
                    + " cannot generate a partition");
        }
    }

    /**
     * Computes an array that indicates leaf membership
     */
    @Override
    public double[] getMembershipValues(Instance inst) throws Exception {

        if (m_Classifier instanceof PartitionGenerator) {
            ArrayList<double[]> al = new ArrayList<double[]>();
            int size = 0;
            for (int i = 0; i < m_Classifiers.length; i++) {
                double[] r = ((PartitionGenerator) m_Classifiers[i]).
                        getMembershipValues(inst);
                size += r.length;
                al.add(r);
            }
            double[] values = new double[size];
            int pos = 0;
            for (double[] v : al) {
                System.arraycopy(v, 0, values, pos, v.length);
                pos += v.length;
            }
            return values;
        } else {
            throw new Exception("Classifier: " + getClassifierSpec()
                    + " cannot generate a partition");
        }
    }

    /**
     * Returns the number of elements in the partition.
     */
    @Override
    public int numElements() throws Exception {

        if (m_Classifier instanceof PartitionGenerator) {
            int size = 0;
            for (int i = 0; i < m_Classifiers.length; i++) {
                size += ((PartitionGenerator) m_Classifiers[i]).numElements();
            }
            return size;
        } else {
            throw new Exception("Classifier: " + getClassifierSpec()
                    + " cannot generate a partition");
        }
    }

    /**
     * Returns the revision string.
     *
     * @return	the revision
     */
    @Override
    public String getRevision() {
        return RevisionUtils.extract("$Revision: 2222 $");
    }

    /**
     * Main method for testing this class.
     *
     * @param argv the options
     */
    public static void main(String[] argv) {
        runClassifier(new BaggingCA(), argv);
    }

    protected List<Classifier> m_classifiersCache;

    /**
     * Aggregate an object with this one
     *
     * @param toAggregate the object to aggregate
     * @return the result of aggregation
     * @throws Exception if the supplied object can't be aggregated for some
     * reason
     */
    @Override
    public BaggingCA aggregate(BaggingCA toAggregate) throws Exception {
        if (!m_Classifier.getClass().isAssignableFrom(toAggregate.m_Classifier.getClass())) {
            throw new Exception("Can't aggregate because base classifiers differ");
        }

        if (m_classifiersCache == null) {
            m_classifiersCache = new ArrayList<Classifier>();
            m_classifiersCache.addAll(Arrays.asList(m_Classifiers));
        }
        m_classifiersCache.addAll(Arrays.asList(toAggregate.m_Classifiers));

        return this;
    }

    /**
     * Call to complete the aggregation process. Allows implementers to do any
     * final processing based on how many objects were aggregated.
     *
     * @throws Exception if the aggregation can't be finalized for some reason
     */
    @Override
    public void finalizeAggregation() throws Exception {
        m_Classifiers = m_classifiersCache.toArray(new Classifier[1]);
        m_NumIterations = m_Classifiers.length;

        m_classifiersCache = null;
    }
}
