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
 *     RFCA.java
 *    Copyright (C) 2001-2012 University of Waikato, Hamilton, New Zealand
 *
 */

package weka.classifiers.trees;

import weka.classifiers.trees.rfca.CAs;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.meta.Bagging;
import weka.core.Capabilities;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.RevisionUtils;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;
import weka.core.Utils;
import weka.core.WekaException;
import weka.gui.ProgrammaticProperty;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import weka.classifiers.meta.BaggingCA;
import static weka.classifiers.meta.BaggingCA.STRENGTH_2;
import static weka.classifiers.meta.BaggingCA.STRENGTH_3;
import static weka.classifiers.meta.BaggingCA.STRENGTH_4;
import static weka.classifiers.meta.BaggingCA.STRENGTH_5;
import static weka.classifiers.meta.BaggingCA.STRENGTH_6;
import static weka.classifiers.meta.BaggingCA.STRENGTH_7;
import static weka.classifiers.meta.BaggingCA.TAGS_STRENGTH;
import weka.core.SelectedTag;
/**
 * <!-- globalinfo-start --> Class for constructing a forest of random trees using Covering Binary Arrays  a Covering
 * Array is represented by a CA object (files .ca) (see class CAs) ,in which, according to the strength selected in the
 * binary matrix corresponding to said strength, it also contains information about the number of rows and columns of the   
 * loaded CA .Each row of the CA(binary matrix) represents a tree in the Random Forest. In which the attributes that will 
 * be used for the construction of that tree will be determined by positions of the row of the CA whose 1. the positions
 * 0 in the row indicate that this attribute will not be used in the construction of the tree.
 * The number of rows of the covering array represented by the binary matrix indicates the number of trees to be constructed 
 * in the random forest. <br>
 * <br>
 * For more information see: <br>
 * <br>
 * Sebastian Vivas, Carlos Cobos and Martha Mendoza
 * Covering arrays to support the process of feature selection 
 * in the Random Forest classifier. LOD 2018 - The Fourth International 
 * Conference on Machine Learning, Optimization, and Data Science.  <br>
 * <br>
 * <!-- globalinfo-end -->
 * 
 * <!-- technical-bibtex-start --> BibTeX:
 * 
 * <pre>
 * &#64;article{Sebastian Vivas, Carlos Cobos and Martha Mendoza.
 * Covering arrays to support the process of feature selection 
 * in the Random Forest classifier. LOD 2018 - The Fourth International 
 * Conference on Machine Learning, Optimization, and Data Science. 
 * September 13-16, 2018. Volterra, Tuscany, Italy. To appear 
 * in Lecture Notes in Computer Science (LNCS), Springer.
 * }
 * </pre>
 * 
 * <br>
 * <br>
 * <!-- technical-bibtex-end -->
 * 
 * <!-- options-start --> Valid options are:
 * <p>
 * 
 * <pre>
 * -strength
 *  Set the strength value of the binary covering array to use. (default 5)
 * </pre>
 * 
 * <pre>
 * -P
 *  Size of each bag, as a percentage of the
 *  training set size. (default 100)
 * </pre>
 * 
 * <pre>
 * -O
 *  Calculate the out of bag error.
 * </pre>
 * 
 * <pre>
 * -store-out-of-bag-predictions
 *  Whether to store out of bag predictions in internal evaluation object.
 * </pre>
 * 
 * <pre>
 * -output-out-of-bag-complexity-statistics
 *  Whether to output complexity-based statistics when out-of-bag evaluation is performed.
 * </pre>
 * 
 * <pre>
 * -print
 *  Print the individual classifiers in the output
 * </pre>
 * 
 * <pre>
 * -attribute-importance
 *  Compute and output attribute importance (mean impurity decrease method)
 * </pre>
 * 
 * <pre>
 * -I &lt;num&gt;
 *  Number of iterations (disabled, this value is automatically set, taking
 *  as value the number of rows of the covering array loaded according to 
 *  the strength value indicated in the strength parameter).
 *  (current value 100)
 * </pre>
 * 
 * <pre>
 * -num-slots &lt;num&gt;
 *  Number of execution slots.
 *  (default 1 - i.e. no parallelism)
 *  (use 0 to auto-detect number of cores)
 * </pre>
 * 
 * <pre>
 * -K &lt;number of attributes&gt;
 *  Number of attributes to randomly investigate. (default 0)
 *  (&lt;1 = int(log_2(#predictors)+1)).
 * </pre>
 * 
 * <pre>
 * -M &lt;minimum number of instances&gt;
 *  Set minimum number of instances per leaf.
 *  (default 1)
 * </pre>
 * 
 * <pre>
 * -V &lt;minimum variance for split&gt;
 *  Set minimum numeric class variance proportion
 *  of train variance for split (default 1e-3).
 * </pre>
 * 
 * <pre>
 * -S &lt;num&gt;
 *  Seed for random number generator.
 *  (default 1)
 * </pre>
 * 
 * <pre>
 * -depth &lt;num&gt;
 *  The maximum depth of the tree, 0 for unlimited.
 *  (default 0)
 * </pre>
 * 
 * <pre>
 * -N &lt;num&gt;
 *  Number of folds for backfitting (default 0, no backfitting).
 * </pre>
 * 
 * <pre>
 * -U
 *  Allow unclassified instances.
 * </pre>
 * 
 * <pre>
 * -B
 *  Break ties randomly when several attributes look equally good.
 * </pre>
 * 
 * <pre>
 * -output-debug-info
 *  If set, classifier is run in debug mode and
 *  may output additional info to the console
 * </pre>
 * 
 * <pre>
 * -do-not-check-capabilities
 *  If set, classifier capabilities are not checked before classifier is built
 *  (use with caution).
 * </pre>
 * 
 * <pre>
 * -num-decimal-places
 *  The number of decimal places for the output of numbers in the model (default 2).
 * </pre>
 * 
 * <pre>
 * -batch-size
 *  The desired batch size for batch prediction  (default 100).
 * </pre>
 * 
 * <!-- options-end -->
 * 
 * @author Sebastian Vivas 
 * @author Carlos Cobos  
 * @author Martha Mendoza
 * @version $Revision: 1 $
 */
public class RFCA extends BaggingCA {

  /** for serialization */
  static final long serialVersionUID = 1116839470751428698L;

  /** True to compute attribute importance */
  protected boolean m_computeAttributeImportance;

  /**
   * The default number of iterations to perform.
   */
  @Override
  protected int defaultNumberOfIterations() {
    return 100;
  }

  /**
   * Constructor that sets base classifier for bagging to RandomTre and default
   * number of iterations to 100.
   */
  public RFCA() {
         
        RandomTree_RFCA rTreeCA = new RandomTree_RFCA();
        rTreeCA.setDoNotCheckCapabilities(true);
        super.setClassifier(rTreeCA);
        super.setRepresentCopiesUsingWeights(true);

 
  }
  
    public RFCA(int minNum) {
      
        RandomTree_RFCA rTreeCA = new RandomTree_RFCA();
        rTreeCA.setDoNotCheckCapabilities(true);
        rTreeCA.setMinNum(minNum);
        super.setClassifier(rTreeCA);
        super.setRepresentCopiesUsingWeights(true);
  
  }

  /**
   * Returns default capabilities of the base classifier.
   *
   * @return the capabilities of the base classifier
   */
  public Capabilities getCapabilities() {

    // Cannot use the main RandomTree_RFCA object because capabilities checking has
    // been turned off
    // for that object.
return (new RandomTree_RFCA()).getCapabilities();
  }

  /**
   * String describing default classifier.
   *
   * @return the default classifier classname
   */
  @Override
  protected String defaultClassifierString() {

    return "weka.classifiers.trees.RandomTree_RFCA";
  }

  /**
   * String describing default classifier options.
   *
   * @return the default classifier options
   */
  @Override
  protected String[] defaultClassifierOptions() {

    String[] args = { "-do-not-check-capabilities" };
    return args;
  }

  /**
   * Returns a string describing classifier
   * 
   * @return a description suitable for displaying in the explorer/experimenter
   *         gui
   */
    
  public String globalInfo() {

    return "Class for constructing a forest of random trees using Covering Binary Arrays .\n, "
            + "A Covering Array is represented by a CA object (files .ca) (see class CAs)"
            + "The construction of the tree is carried out according to the selected strength value (from 2 to 7),"
            + "the number of trees to be built is determined by the number of rows of the CA, each row of the CA "
            + "represents a tree of the Random Forest, In which the attributes that will It will be used for the "
            + "construction of that tree will be determined by positions of the row of the CA, where for "
            + "the selection of attributes 1 indicates the presence and 0 the absence of an attribute.\n" +
"\n"      + "For more information see: \n\n" + getTechnicalInformation().toString();
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
   * This method only accepts RandomTree_RFCA arguments.
   *
   * @param newClassifier the RandomTree_RFCA to use.
     * @throws (ERROR) argument is not a RandomTree_RFCA
   */
  @Override
  @ProgrammaticProperty
  public void setClassifier(Classifier newClassifier) {
    if (!(newClassifier instanceof RandomTree_RFCA)) {
      throw new IllegalArgumentException(
        "RFCA: Argument of setClassifier() must be a RandomTree_RFCA.");
    }
    super.setClassifier(newClassifier);
  }

  /**
   * This method only accepts true as its argument
   *
   * @param representUsingWeights must be set to true.
   * @exception if argument is not true
   */
  @Override
  @ProgrammaticProperty
  public void setRepresentCopiesUsingWeights(boolean representUsingWeights) {
    if (!representUsingWeights) {
      throw new IllegalArgumentException(
        "RFCA: Argument of setRepresentCopiesUsingWeights() must be true.");
    }
    super.setRepresentCopiesUsingWeights(representUsingWeights);
  }

  /**
   * Returns the tip text for this property
   *
   * @return tip text for this property suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String numFeaturesTipText() {
    return ((RandomTree_RFCA) getClassifier()).KValueTipText();
  }

  /**
   * Get the number of features used in random selection.
   *
   * @return Value of numFeatures.
   */
  public int getNumFeatures() {

    return ((RandomTree_RFCA) getClassifier()).getKValue();
  }

  /**
   * Set the number of features to use in random selection.
   *
   * @param newNumFeatures Value to assign to numFeatures.
   */
  public void setNumFeatures(int newNumFeatures) {

    ((RandomTree_RFCA) getClassifier()).setKValue(newNumFeatures);
  }

  /**
   * Returns the tip text for this property
   *
   * @return tip text for this property suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String computeAttributeImportanceTipText() {
    return "Compute attribute importance via mean impurity decrease";
  }

  /**
   * Set whether to compute and output attribute importance scores
   *
   * @param computeAttributeImportance true to compute attribute importance
   *          scores
   */
  public void setComputeAttributeImportance(boolean computeAttributeImportance) {
    m_computeAttributeImportance = computeAttributeImportance;
    ((RandomTree_RFCA)m_Classifier).setComputeImpurityDecreases(computeAttributeImportance);
  }

  /**
   * Get whether to compute and output attribute importance scores
   *
   * @return true if computing attribute importance scores
   */
  public boolean getComputeAttributeImportance() {
    return m_computeAttributeImportance;
  }

  /**
   * Returns the tip text for this property
   *
   * @return tip text for this property suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String maxDepthTipText() {
    return ((RandomTree_RFCA) getClassifier()).maxDepthTipText();
  }

  /**
   * Get the maximum depth of trh tree, 0 for unlimited.
   *
   * @return the maximum depth.
   */
  public int getMaxDepth() {
    return ((RandomTree_RFCA) getClassifier()).getMaxDepth();
  }

  /**
   * Set the maximum depth of the tree, 0 for unlimited.
   *
   * @param value the maximum depth.
   */
  public void setMaxDepth(int value) {
    ((RandomTree_RFCA) getClassifier()).setMaxDepth(value);
  }

  /**
   * Returns the tip text for this property
   *
   * @return tip text for this property suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String breakTiesRandomlyTipText() {
    return ((RandomTree_RFCA) getClassifier()).breakTiesRandomlyTipText();
  }

  /**
   * Get whether to break ties randomly.
   *
   * @return true if ties are to be broken randomly.
   */
  public boolean getBreakTiesRandomly() {

    return ((RandomTree_RFCA) getClassifier()).getBreakTiesRandomly();
  }

  /**
   * Set whether to break ties randomly.
   *
   * @param newBreakTiesRandomly true if ties are to be broken randomly
   */
  public void setBreakTiesRandomly(boolean newBreakTiesRandomly) {

    ((RandomTree_RFCA) getClassifier()).setBreakTiesRandomly(newBreakTiesRandomly);
  }

  /**
   * Set debugging mode.
   *
   * @param debug true if debug output should be printed
   */
  public void setDebug(boolean debug) {

    super.setDebug(debug);
    ((RandomTree_RFCA) getClassifier()).setDebug(debug);
  }

  /**
   * Set the number of decimal places.
   */
  public void setNumDecimalPlaces(int num) {

    super.setNumDecimalPlaces(num);
    ((RandomTree_RFCA) getClassifier()).setNumDecimalPlaces(num);
  }

  /**
   * Set the preferred batch size for batch prediction.
   *
   * @param size the batch size to use
   */
  @Override
  public void setBatchSize(String size) {

    super.setBatchSize(size);
    ((RandomTree_RFCA) getClassifier()).setBatchSize(size);
  }

  /**
   * Sets the seed for the random number generator.
   *
   * @param s the seed to be used
   */
  public void setSeed(int s) {

    super.setSeed(s);
    ((RandomTree_RFCA) getClassifier()).setSeed(s);
  }

  /**
   * Returns description of the bagged classifier.
   *
   * @return description of the bagged classifier as a string
   */
  @Override
  public String toString() {

    if (m_Classifiers == null) {
      return "RFCA: No model built yet.";
    }
    StringBuilder buffer = new StringBuilder("RFCA\n\n");
    buffer.append(super.toString());

    if (getComputeAttributeImportance()) {
      try {
        double[] nodeCounts = new double[m_data.numAttributes()];
        double[] impurityScores =
          computeAverageImpurityDecreasePerAttribute(nodeCounts);
        int[] sortedIndices = Utils.sort(impurityScores);
        buffer
          .append("\n\nAttribute importance based on average impurity decrease "
            + "(and number of nodes using that attribute)\n\n");
        for (int i = sortedIndices.length - 1; i >= 0; i--) {
          int index = sortedIndices[i];
          if (index != m_data.classIndex()) {
            buffer
              .append(
                Utils.doubleToString(impurityScores[index], 10,
                  getNumDecimalPlaces())).append(" (")
              .append(Utils.doubleToString(nodeCounts[index], 6, 0))
              .append(")  ").append(m_data.attribute(index).name())
              .append("\n");
          }
        }
      } catch (WekaException ex) {
        // ignore
      }
    }

    return buffer.toString();
  }

  /**
   * Computes the average impurity decrease per attribute over the trees
   *
   * @param nodeCounts an optional array that, if non-null, will hold the count
   *          of the number of nodes at which each attribute was used for
   *          splitting
   * @return the average impurity decrease per attribute over the trees
   */
  public double[] computeAverageImpurityDecreasePerAttribute(
    double[] nodeCounts) throws WekaException {

    if (m_Classifiers == null) {
      throw new WekaException("Classifier has not been built yet!");
    }

    if (!getComputeAttributeImportance()) {
      throw new WekaException("Stats for attribute importance have not "
        + "been collected!");
    }

    double[] impurityDecreases = new double[m_data.numAttributes()];
    if (nodeCounts == null) {
      nodeCounts = new double[m_data.numAttributes()];
    }
    for (Classifier c : m_Classifiers) {
      double[][] forClassifier = ((RandomTree_RFCA) c).getImpurityDecreases();
      for (int i = 0; i < m_data.numAttributes(); i++) {
        impurityDecreases[i] += forClassifier[i][0];
        nodeCounts[i] += forClassifier[i][1];
      }
    }
    for (int i = 0; i < m_data.numAttributes(); i++) {
      if (nodeCounts[i] > 0) {
        impurityDecreases[i] /= nodeCounts[i];
      }
    }

    return impurityDecreases;
  }

  /**
   * Returns an enumeration describing the available options.
   * 
   * @return an enumeration of all the available options
   */
  @Override
  public Enumeration<Option> listOptions() {

    Vector<Option> newVector = new Vector<Option>();

               newVector.addElement(new Option(
                "\t Set the strength value of the binary covering array to use. (default 5)",
                "strength", 1, "-strength"));

    newVector.addElement(new Option(
      "\tSize of each bag, as a percentage of the\n"
        + "\ttraining set size. (default 100)", "P", 1, "-P"));

    newVector.addElement(new Option("\tCalculate the out of bag error.", "O",
      0, "-O"));

    newVector
      .addElement(new Option(
        "\tWhether to store out of bag predictions in internal evaluation object.",
        "store-out-of-bag-predictions", 0, "-store-out-of-bag-predictions"));

    newVector
      .addElement(new Option(
        "\tWhether to output complexity-based statistics when out-of-bag evaluation is performed.",
        "output-out-of-bag-complexity-statistics", 0,
        "-output-out-of-bag-complexity-statistics"));

    newVector
      .addElement(new Option(
        "\tPrint the individual classifiers in the output", "print", 0,
        "-print"));

    newVector.addElement(new Option(
      "\tCompute and output attribute importance (mean impurity decrease "
        + "method)", "attribute-importance", 0, "-attribute-importance"));

    newVector.addElement(new Option("\tNumber of iterations.\n"
      + "\t(current value " + getNumIterations() + ")", "I", 1, "-I <num>"));

    newVector.addElement(new Option("\tNumber of execution slots.\n"
      + "\t(default 1 - i.e. no parallelism)\n"
      + "\t(use 0 to auto-detect number of cores)", "num-slots", 1,
      "-num-slots <num>"));

    // Add base classifier options
    List<Option> list =
      Collections.list(((OptionHandler) getClassifier()).listOptions());
    newVector.addAll(list);

    return newVector.elements();
  }

  /**
   * Gets the current settings of the forest.
   * 
   * @return an array of strings suitable for passing to setOptions()
   */
  @Override
  public String[] getOptions() {
    Vector<String> result = new Vector<String>();
    
         switch (m_strengthValue) {

            case STRENGTH_2:
                result.add("-strength");
                result.add("" + getStrengthValue());
                break;
            case STRENGTH_3:
                result.add("-strength");
                result.add("" + getStrengthValue());
                break;
            case STRENGTH_4:
                result.add("-strength");
                result.add("" + getStrengthValue());
                break;
            case STRENGTH_5:
                result.add("-strength");
                result.add("" + getStrengthValue());
                break;
            case STRENGTH_6:
                result.add("-strength");
                result.add("" + getStrengthValue());
                break;
            case STRENGTH_7:
                result.add("-strength");
                result.add("" + getStrengthValue());
                break;
        }
         
    result.add("-P");
    result.add("" + getBagSizePercent());

    if (getCalcOutOfBag()) {
      result.add("-O");
    }

    if (getStoreOutOfBagPredictions()) {
      result.add("-store-out-of-bag-predictions");
    }

    if (getOutputOutOfBagComplexityStatistics()) {
      result.add("-output-out-of-bag-complexity-statistics");
    }

    if (getPrintClassifiers()) {
      result.add("-print");
    }

    if (getComputeAttributeImportance()) {
      result.add("-attribute-importance");
    }

    result.add("-I");
    result.add("" + getNumIterations());

    result.add("-num-slots");
    result.add("" + getNumExecutionSlots());

    if (getDoNotCheckCapabilities()) {
      result.add("-do-not-check-capabilities");
    }

    // Add base classifier options
    Vector<String> classifierOptions = new Vector<String>();
    Collections.addAll(classifierOptions,
      ((OptionHandler) getClassifier()).getOptions());
    Option.deleteFlagString(classifierOptions, "-do-not-check-capabilities");
    result.addAll(classifierOptions);

    return result.toArray(new String[result.size()]);
  }

  /**
   * Parses a given list of options.
   * <p/>
   * 
   * <!-- options-start --> Valid options are:
   * <p>
   * 
   * <pre>
   * -P
   *  Size of each bag, as a percentage of the
   *  training set size. (default 100)
   * </pre>
   * 
   * <pre>
   * -O
   *  Calculate the out of bag error.
   * </pre>
   * 
   * <pre>
   * -store-out-of-bag-predictions
   *  Whether to store out of bag predictions in internal evaluation object.
   * </pre>
   * 
   * <pre>
   * -output-out-of-bag-complexity-statistics
   *  Whether to output complexity-based statistics when out-of-bag evaluation is performed.
   * </pre>
   * 
   * <pre>
   * -print
   *  Print the individual classifiers in the output
   * </pre>
   * 
   * <pre>
   * -attribute-importance
   *  Compute and output attribute importance (mean impurity decrease method)
   * </pre>
   * 
   * <pre>
   * -I &lt;num&gt;
   *  Number of iterations.
   *  (current value 100)
   * </pre>
   * 
   * <pre>
   * -num-slots &lt;num&gt;
   *  Number of execution slots.
   *  (default 1 - i.e. no parallelism)
   *  (use 0 to auto-detect number of cores)
   * </pre>
   * 
   * <pre>
   * -K &lt;number of attributes&gt;
   *  Number of attributes to randomly investigate. (default 0)
   *  (&lt;1 = int(log_2(#predictors)+1)).
   * </pre>
   * 
   * <pre>
   * -M &lt;minimum number of instances&gt;
   *  Set minimum number of instances per leaf.
   *  (default 1)
   * </pre>
   * 
   * <pre>
   * -V &lt;minimum variance for split&gt;
   *  Set minimum numeric class variance proportion
   *  of train variance for split (default 1e-3).
   * </pre>
   * 
   * <pre>
   * -S &lt;num&gt;
   *  Seed for random number generator.
   *  (default 1)
   * </pre>
   * 
   * <pre>
   * -depth &lt;num&gt;
   *  The maximum depth of the tree, 0 for unlimited.
   *  (default 0)
   * </pre>
   * 
   * <pre>
   * -N &lt;num&gt;
   *  Number of folds for backfitting (default 0, no backfitting).
   * </pre>
   * 
   * <pre>
   * -U
   *  Allow unclassified instances.
   * </pre>
   * 
   * <pre>
   * -B
   *  Break ties randomly when several attributes look equally good.
   * </pre>
   * 
   * <pre>
   * -output-debug-info
   *  If set, classifier is run in debug mode and
   *  may output additional info to the console
   * </pre>
   * 
   * <pre>
   * -do-not-check-capabilities
   *  If set, classifier capabilities are not checked before classifier is built
   *  (use with caution).
   * </pre>
   * 
   * <pre>
   * -num-decimal-places
   *  The number of decimal places for the output of numbers in the model (default 2).
   * </pre>
   * 
   * <pre>
   * -batch-size
   *  The desired batch size for batch prediction  (default 100).
   * </pre>
   * 
   * <!-- options-end -->
   * 
   * @param options the list of options as an array of strings
   * @throws Exception if an option is not supported
   */
  @Override
  public void setOptions(String[] options) throws Exception {
      
      String strength = Utils.getOption("strength", options);
        if ( strength.equalsIgnoreCase("2") )  {
             setStrengthValue(new SelectedTag(STRENGTH_2, TAGS_STRENGTH ));
        }
        if ( strength.equalsIgnoreCase("3") )  {
             setStrengthValue(new SelectedTag(STRENGTH_3, TAGS_STRENGTH ));
        }  
        if ( strength.equalsIgnoreCase("4") )  {
             setStrengthValue(new SelectedTag(STRENGTH_4, TAGS_STRENGTH ));
        }  
        if ( strength.equalsIgnoreCase("5") )  {
             setStrengthValue(new SelectedTag(STRENGTH_5, TAGS_STRENGTH ));
        }  
        if ( strength.equalsIgnoreCase("6") )  {
             setStrengthValue(new SelectedTag(STRENGTH_6, TAGS_STRENGTH ));
        }  
        if ( strength.equalsIgnoreCase("7") )  {
            setStrengthValue(new SelectedTag(STRENGTH_7, TAGS_STRENGTH ));
        }  
        
    String bagSize = Utils.getOption('P', options);
    if (bagSize.length() != 0) {
      setBagSizePercent(Integer.parseInt(bagSize));
    } else {
      setBagSizePercent(100);
    }

    setCalcOutOfBag(Utils.getFlag('O', options));

    setStoreOutOfBagPredictions(Utils.getFlag("store-out-of-bag-predictions",
      options));

    setOutputOutOfBagComplexityStatistics(Utils.getFlag(
      "output-out-of-bag-complexity-statistics", options));

    setPrintClassifiers(Utils.getFlag("print", options));

    setComputeAttributeImportance(Utils
      .getFlag("attribute-importance", options));

    String iterations = Utils.getOption('I', options);
    if (iterations.length() != 0) {
      setNumIterations(Integer.parseInt(iterations));
    } else {
      setNumIterations(defaultNumberOfIterations());
    }

    String numSlots = Utils.getOption("num-slots", options);
    if (numSlots.length() != 0) {
      setNumExecutionSlots(Integer.parseInt(numSlots));
    } else {
      setNumExecutionSlots(1);
    }

    RandomTree_RFCA classifier =
      ((RandomTree_RFCA) AbstractClassifier.forName(defaultClassifierString(),
        options));
    classifier.setComputeImpurityDecreases(m_computeAttributeImportance);
    setDoNotCheckCapabilities(classifier.getDoNotCheckCapabilities());
    setSeed(classifier.getSeed());
    setDebug(classifier.getDebug());
    setNumDecimalPlaces(classifier.getNumDecimalPlaces());
    setBatchSize(classifier.getBatchSize());
    classifier.setDoNotCheckCapabilities(true);

    // Set base classifier and options
    setClassifier(classifier);

    Utils.checkForRemainingOptions(options);
  }

  /**
   * Returns the revision string.
   * 
   * @return the revision
   */
  @Override
  public String getRevision() {
    return RevisionUtils.extract("$Revision: 13295 $");
  }

  /**
   * Main method for this class.
   * 
   * @param argv the options
   */
  public static void main(String[] argv) {
    runClassifier(new RFCA(), argv);
  }
}
