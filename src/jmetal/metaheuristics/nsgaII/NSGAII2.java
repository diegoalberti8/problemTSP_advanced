//  NSGAII_MOTSP_main.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2012 Antonio J. Nebro, Juan J. Durillo
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

package jmetal.metaheuristics.nsgaII;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.ProblemTSP;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/** 
 * Class to configure and execute the NSGA-II algorithm. The experiments.settings are aimed
 * at solving the ProblemTSP problem.
 */

public class NSGAII2 {
  
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object

  /**
   * @param args Command line arguments.
   * @throws JMException 
   * @throws IOException 
   * @throws SecurityException 
   * Usage: 
      - jmetal.metaheuristics.nsgaII.NSGAII1
   */
  public static void main(String [] args) throws JMException, SecurityException, IOException, ClassNotFoundException {
      
    ProblemTSP   problem   ; // The problem to solve
    Algorithm algorithm ; // The algorithm to use
    Operator  crossover ; // Crossover operator
    Operator  mutation  ; // Mutation operator
    Operator  selection ; // Selection operator
    
    HashMap  parameters ; // Operator parameters
    
    QualityIndicator indicators ; // Object to get quality indicators

    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("NSGAII2_ProblemTSP_main.log"); 
    logger_.addHandler(fileHandler_) ;
    
    String ruta_distancias = "distancias.txt";
    problem = new ProblemTSP("TSPPermutation", ruta_distancias);
    
    indicators = new QualityIndicator(problem, "FUN_NSGAII2"); //indicators = null ;

    algorithm = new NSGAII_ProblemTSP((Problem)problem);

    // Algorithm parameters
    algorithm.setInputParameter("populationSize",100);
    algorithm.setInputParameter("maxEvaluations",25000);
    
    /* Crossver operator */
    parameters = new HashMap() ;
    parameters.put("probability", 0.3) ;
    crossover = CrossoverFactory.getCrossoverOperator("PMXCrossoverTSP", parameters); //Partially Mapped Crossover
        
    /* Mutation operator */
    parameters = new HashMap() ;
    parameters.put("probability1", 0.8) ; 
    
    mutation = MutationFactory.getMutationOperator("SwapMutationTSP", parameters);                    
  
    /* Selection Operator */
    parameters = null;
    selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters) ;                            

    // Add the operators to the algorithm
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("mutation",mutation);
    algorithm.addOperator("selection",selection);
    
    // Add the indicator object to the algorithm
    algorithm.setInputParameter("indicators", indicators) ;
    
    // Execute the Algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    
    // Result messages 
    logger_.info("Total execution time: "+estimatedTime + "ms");
    population.printVariablesToFile("VAR_NSGAII2");
    logger_.info("Variables values have been writen to file VAR_NSGAII2");
    population.printObjectivesToFile("FUN_NSGAII2");
    logger_.info("Objectives values have been writen to file FUN_NSGAII2");
    
    if (indicators != null) {
      
      logger_.info("QUALITY INDICATORS") ;
      logger_.info("Hypervolume: " + indicators.getHypervolume(population)) ;
      logger_.info("GD         : " + indicators.getGD(population)) ;
      logger_.info("IGD        : " + indicators.getIGD(population)) ;
      logger_.info("Spread     : " + indicators.getSpread(population)) ;
      logger_.info("Epsilon    : " + indicators.getEpsilon(population)) ;  
      
      int evaluations = ((Integer)algorithm.getOutputParameter("evaluations")).intValue();
      logger_.info("Speed      : " + evaluations + " evaluations") ;  
      
    } // if
  } //main
} // NSGAII1
