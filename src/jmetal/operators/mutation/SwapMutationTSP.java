//  SwapMutation.java
//
//  Author:
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

package jmetal.operators.mutation;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.TSPPermutationSolutionType;
import jmetal.encodings.variable.TSPPermutation;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import jmetal.problems.ProblemTSP;

/**
 * This class implements a swap mutation. The solution type of the solution
 * must be Permutation.
 */
public class SwapMutationTSP extends Mutation{
  /**
   * Valid solution types to apply this operator 
   */
  private static final List VALID_TYPES = Arrays.asList(TSPPermutationSolutionType.class) ;
  
  private Double mutationProbability_ = null;
  
  /** 
   * Constructor
   */
  public SwapMutationTSP(HashMap<String, Object> parameters) {    
  	super(parameters) ;
  	
  	if (parameters.get("probability") != null) {
            mutationProbability_ = (Double) parameters.get("probability");
        }
        //if (parameters.get("probability2") != null) {
        //    changeTransportProbability_ = (Double) parameters.get("probability2");
        //}    
  } // Constructor

  /**
   * Constructor
   */
  //public SwapMutation(Properties properties) {
  //  this();
  //} // Constructor

  /**
   * Performs the operation
   * @param probability Mutation probability
   * @param solution The solution to mutate
   * @throws JMException 
   */
  public void doMutation(double probability, Solution solution) throws JMException {   
    int permutation[] ;
    int permutationLength ;
    
    if (solution.getType().getClass() == TSPPermutationSolutionType.class) {

        permutationLength = ((TSPPermutation)solution.getDecisionVariables()[0]).getLength() ;
        //permutation = ((TSPPermutation)solution.getDecisionVariables()[0]).vector_ ;
        
        permutation = new int[permutationLength];
        for (int i = 0; i < permutationLength; i++) {
            permutation[i] = ((TSPPermutation)solution.getDecisionVariables()[0]).vector_[i];
        }
        
        int pos1 ;
        int pos2 ;

        pos1 = PseudoRandom.randInt(0,permutationLength-1) ;
        pos2 = PseudoRandom.randInt(0,permutationLength-1) ;

        while (pos1 == pos2) {
            if (pos1 == (permutationLength - 1)) 
                pos2 = PseudoRandom.randInt(0, permutationLength- 2);
            else 
                pos2 = PseudoRandom.randInt(pos1, permutationLength- 1);
        } // while
            
        if (PseudoRandom.randDouble() < probability) {
        
            int temp = permutation[pos1];
            permutation[pos1] = permutation[pos2];
            permutation[pos2] = temp;
        
            //Problema TSP
            int ciudad_origen;
            int ciudad_destino;
            int valor_final;
            int valor_original;
            double random;
            java.util.ArrayList<Integer> valoresPosibles;
            ProblemTSP problem = (ProblemTSP) solution.getProblem();

            for (int i = 1; i < permutationLength; i++) {
                
                valor_original = permutation[i];
                ciudad_origen = problem.getCity(permutation[(i-1)%permutationLength]);
                ciudad_destino = problem.getCity(permutation[i]);
        
                valoresPosibles = problem.mediosPosibles(ciudad_origen,ciudad_destino);

                valor_final = -1;
                if (valoresPosibles.size() == 1) {
                    valor_final = valoresPosibles.get(0);
                }

                if (valoresPosibles.size() == 2) {
                    if (valor_original == valoresPosibles.get(0)) {
                        valor_final = valoresPosibles.get(1);
                    } else if (valor_original == valoresPosibles.get(1)) {
                        valor_final = valoresPosibles.get(0);
                    } else {
                        random = PseudoRandom.randDouble();
                        if (random < 0.5) { 
                            valor_final = valoresPosibles.get(0);
                        } else {
                            valor_final = valoresPosibles.get(1);
                        }
                    }
                }

                if (valoresPosibles.size() == 3) {
                    int t1 = 0; //traslado 1
                    int t2 = 0; //traslado 2

                    if (valoresPosibles.get(0) == valor_original) {
                        t1 = 1;
                        t2 = 2;
                    }
                    if (valoresPosibles.get(1) == valor_original) {
                        t1 = 0;
                        t2 = 2;
                    }
                    if (valoresPosibles.get(2) == valor_original) {
                        t1 = 0;
                        t2 = 1;
                    }

                    random = PseudoRandom.randDouble();
                    if (random < 0.5) { 
                        valor_final = valoresPosibles.get(t1);
                    } else {
                        valor_final = valoresPosibles.get(t2);
                    }
                }
                permutation[i] = valor_final;
            }
            
            valor_original = permutation[0];
            ciudad_origen = problem.getCity(permutation[(permutationLength-1)%permutationLength]);
            ciudad_destino = problem.getCity(permutation[0]);

            valoresPosibles = problem.mediosPosibles(ciudad_origen,ciudad_destino);

            valor_final = -1;
            if (valoresPosibles.size() == 1) {
                valor_final = valoresPosibles.get(0);
            }

            if (valoresPosibles.size() == 2) {
                if (valor_original == valoresPosibles.get(0)) {
                    valor_final = valoresPosibles.get(1);
                } else if (valor_original == valoresPosibles.get(1)) {
                    valor_final = valoresPosibles.get(0);
                } else {
                    random = PseudoRandom.randDouble();
                    if (random < 0.5) { 
                        valor_final = valoresPosibles.get(0);
                    } else {
                        valor_final = valoresPosibles.get(1);
                    }
                }
            }

            if (valoresPosibles.size() == 3) {
                int t1 = 0; //traslado 1
                int t2 = 0; //traslado 2

                if (valoresPosibles.get(0) == valor_original) {
                    t1 = 1;
                    t2 = 2;
                }
                if (valoresPosibles.get(1) == valor_original) {
                    t1 = 0;
                    t2 = 2;
                }
                if (valoresPosibles.get(2) == valor_original) {
                    t1 = 0;
                    t2 = 1;
                }

                random = PseudoRandom.randDouble();
                if (random < 0.5) { 
                    valor_final = valoresPosibles.get(t1);
                } else {
                    valor_final = valoresPosibles.get(t2);
                }
            }
            permutation[0] = valor_final;
            
            ((TSPPermutation)solution.getDecisionVariables()[0]).vector_ = permutation;
            solution.unMarked(); //desmarco la solución para volver a evaluarla
        } // if
    } else  {
      Configuration.logger_.severe("SwapMutationTSP.doMutation: invalid type. " +
          ""+ solution.getDecisionVariables()[0].getVariableType());

      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".doMutation()") ;
    }
  } // doMutation

  /**
   * Executes the operation
   * @param object An object containing the solution to mutate
   * @return an object containing the mutated solution
   * @throws JMException 
   */
  public Object execute(Object object) throws JMException {
    Solution solution = (Solution)object;
    
    if (!VALID_TYPES.contains(solution.getType().getClass())) {
            Configuration.logger_.severe("SwapMutation.execute: the solution " +
                            "is not of the right type. The type should be 'Binary', " +
                            "'BinaryReal' or 'Int', but " + solution.getType() + " is obtained");

            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
    } // if 
    
    this.doMutation(mutationProbability_, solution);
    return solution;
  } // execute  
} // SwapMutationTSP
