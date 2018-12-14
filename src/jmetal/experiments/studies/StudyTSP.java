//  Study.java
//
//  Authors:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jmetal.experiments.studies;

import jmetal.core.Algorithm;
import jmetal.experiments.Experiment;
import jmetal.experiments.Settings;
import jmetal.experiments.settings.NSGAIIPermutation_SettingsTSP;
import jmetal.experiments.settings.SPEA2Permutation_SettingsTSP;
import jmetal.experiments.util.Friedman;
import jmetal.util.JMException;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class implementing an example of experiment using NSGA-II as base algorithm.
 * The experiment consisting in studying the effect of the crossover probability
 * in NSGA-II.
 */
public class StudyTSP extends Experiment {
  /**
   * Configures the algorithms in each independent run
   * @param problemName The problem to solve
   * @param problemIndex
   * @param algorithm Array containing the algorithms to run
   * @throws ClassNotFoundException 
   */
  public synchronized void algorithmSettings(String problemName, int problemIndex, Algorithm[] algorithm) throws ClassNotFoundException {  	
    
    try {
      int numberOfAlgorithms = algorithmNameList_.length;

      HashMap[] parameters = new HashMap[numberOfAlgorithms];

      for (int i = 0; i < numberOfAlgorithms; i++) {
        parameters[i] = new HashMap();
      } // for

      if (!paretoFrontFile_[problemIndex].equals("")) {
        for (int i = 0; i < numberOfAlgorithms; i++)
          parameters[i].put("paretoFrontFile_", paretoFrontFile_[problemIndex]);
      } // if

      parameters[0].put("crossoverProbability_", 0.3);
      parameters[0].put("mutationProbability_", 0.2);
      
      parameters[1].put("crossoverProbability_", 0.3);
      parameters[1].put("mutationProbability_", 0.8);
      
      parameters[2].put("crossoverProbability_", 0.8);
      parameters[2].put("mutationProbability_", 0.2);
      
      parameters[3].put("crossoverProbability_", 0.8);
      parameters[3].put("mutationProbability_", 0.8);
      
      parameters[4].put("crossoverProbability_", 0.3);
      parameters[4].put("mutationProbability_", 0.2);
      
      parameters[5].put("crossoverProbability_", 0.3);
      parameters[5].put("mutationProbability_", 0.8);
      
      parameters[5].put("crossoverProbability_", 0.8);
      parameters[5].put("mutationProbability_", 0.2);
      
      parameters[5].put("crossoverProbability_", 0.8);
      parameters[5].put("mutationProbability_", 0.8);
      
      if ((!paretoFrontFile_[problemIndex].equals("")) || 
      		(paretoFrontFile_[problemIndex] == null)) {
        for (int i = 0; i < numberOfAlgorithms; i++)
          parameters[i].put("paretoFrontFile_",  paretoFrontFile_[problemIndex]);
      } // if
 
      for (int i = 0; i < numberOfAlgorithms; i++)
        if (i < 4) { 
            algorithm[i] = new NSGAIIPermutation_SettingsTSP(problemName).configure(parameters[i]);
        } else {
            algorithm[i] = new SPEA2Permutation_SettingsTSP(problemName).configure(parameters[i]);
        }
            
    } catch (IllegalArgumentException ex) {
      Logger.getLogger(StudyTSP.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      Logger.getLogger(StudyTSP.class.getName()).log(Level.SEVERE, null, ex);
    } catch (JMException ex) {
      Logger.getLogger(StudyTSP.class.getName()).log(Level.SEVERE, null, ex);
    }
  } // algorithmSettings
  
  public static void main(String[] args) throws JMException, IOException {
    StudyTSP exp = new StudyTSP() ;
    
    exp.experimentName_  = "StudyTSP" ;
    exp.algorithmNameList_   = new String[] {"NSGAII1", "NSGAII2", "NSGAII3", "NSGAII4", "SPEA21", "SPEA22", "SPEA23", "SPEA24"} ; 
    exp.problemList_     = new String[] {"ProblemTSP"} ;
    exp.paretoFrontFile_ = new String[] {"ProblemTSP.pf"} ;
    exp.indicatorList_   = new String[] {"HV", "SPREAD", "IGD", "EPSILON"} ;
    
    int numberOfAlgorithms = exp.algorithmNameList_.length ;

    exp.experimentBaseDirectory_ = "test" + exp.experimentName_;
    
    exp.paretoFrontDirectory_ = "test\\ParetoFronts";
    
    exp.algorithmSettings_ = new Settings[numberOfAlgorithms] ;
    
    exp.independentRuns_ = 15 ;

    exp.initExperiment();  //setea información para los threads

    // Run the experiments
    int numberOfThreads ;
    exp.runExperiment(numberOfThreads = 1) ;  //exp.runExperiment(numberOfThreads = 6) ;

    exp.generateQualityIndicators() ;
    
    // Generate latex tables (comment this sentence is not desired)
    exp.generateLatexTables() ;
    
    // Configure the R scripts to be generated
    int rows  ;
    int columns  ;
    String prefix ;
    String [] problems ;

    rows = 2 ;
    columns = 3 ;
    prefix = new String("Problems");
    problems = new String[]{"ProblemTSP"} ;

    boolean notch ;
    exp.generateRBoxplotScripts(rows, columns, problems, prefix, notch = true, exp) ;
    exp.generateRWilcoxonScripts(problems, prefix, exp) ;

    // Applying Friedman test
    Friedman test = new Friedman(exp);
    test.executeTest("EPSILON");
    test.executeTest("HV");
    test.executeTest("IGD");
    test.executeTest("SPREAD");
  } // main
} // Study


