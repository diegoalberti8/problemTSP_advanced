//  MOTSP.java
//
//  Author:
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

package jmetal.problems;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.TSPPermutationSolutionType;
import jmetal.encodings.variable.TSPPermutation;
import java.io.*;
import java.lang.Math;
import jmetal.util.PseudoRandom;
import jmetal.util.UtilDouble;


/**
 * Class representing a multi-objective TSP (Traveling Salesman Problem) problem.
 * This class is tested with two objectives and the KROA150 and KROB150 
 * instances of TSPLIB
 */
public class ProblemTSP extends Problem {
  
  public int         numberOfCities_;
  public String []   cityNames_;
  public double [][] costMatrix_; 
  public double [][] timeMatrix_;
  public double costPlane_;
  public double costTrain_;
  public double costBus_;
  public double timePlane_;
  public double timeTrain_;
  public double timeBus_;
  
 /* Creates a new ProblemTSP problem instance. It accepts data files from TSPLIB */
  public ProblemTSP(String solutionType, String file) throws IOException {
      
    numberOfVariables_  = 1;
    numberOfObjectives_ = 2;
    numberOfConstraints_= 0;
    problemName_        = "ProblemTSP";

    length_       = new int[numberOfVariables_];

    costPlane_ = 0.12;
    costTrain_ = 0.09;
    costBus_ = 0.03;
    timePlane_ = 0.0013;  //0.0021
    timeTrain_ = 0.021;  //0.011
    timeBus_ = 0.032;
    
    readProblemFile(file);
    
    System.out.println(" ");
    System.out.println("INSTANCIA DEL PROBLEMA: \n");
    
    System.out.println("numberOfCities_ = " + numberOfCities_);
    
    System.out.println("Nombres de ciudades: \n");
    showArray(cityNames_);
    System.out.println("\nMatriz de costos: \n");
    showMatrix(costMatrix_);
    System.out.println("\nMatriz de tiempos: \n");
    showMatrix(timeMatrix_);
    
    length_      [0] = numberOfCities_ ;
    if (solutionType.compareTo("TSPPermutation") == 0)
    	solutionType_ = new TSPPermutationSolutionType(this) ;
    else {
    	System.out.println("Error: solution type " + solutionType + " invalid") ;
    	System.exit(-1) ;
    }
  } // ProblemTSP
    
 /* Evaluates a solution @param solution The solution to evaluate */      
  public void evaluate(Solution solution) {
    double cost = 0.0;
    double time = 0.0;
    
    /*//debug
    int q = 0;
    while (q < ((TSPPermutation)solution.getDecisionVariables()[0]).vector_.length) {
      System.out.print(((TSPPermutation)solution.getDecisionVariables()[0]).vector_[q] + ", ");
      q += 1;
    }
    System.out.println();
    //debug*/
    
    if (!solution.isMarked()) {
        int x ; 
        int y ;
        for (int i = 1; i < numberOfCities_; i++) {  

          x = ((TSPPermutation)solution.getDecisionVariables()[0]).vector_[i-1];
          y = ((TSPPermutation)solution.getDecisionVariables()[0]).vector_[i];

          //System.out.print("costMatrix_[" + x % numberOfCities_ + "][" + y + "] = " + costMatrix_[x % numberOfCities_][y]);
          //System.out.println("     timeMatrix_[" + x % numberOfCities_ + "][" + y + "] = " + timeMatrix_[x % numberOfCities_][y]);
          
          cost += costMatrix_[x % numberOfCities_][y];
          time += timeMatrix_[x % numberOfCities_][y];
        } // for

        int lastCity = ((TSPPermutation)solution.getDecisionVariables()[0]).vector_[numberOfCities_ - 1];
        int firstCity = ((TSPPermutation)solution.getDecisionVariables()[0]).vector_[0];

        //System.out.print("costMatrix_[" + lastCity % numberOfCities_ + "][" + firstCity + "] = " + costMatrix_[lastCity % numberOfCities_][firstCity]);
        //System.out.println("     timeMatrix_[" + lastCity % numberOfCities_ + "][" + firstCity + "] = " + timeMatrix_[lastCity % numberOfCities_][firstCity]);
        
        cost += costMatrix_[lastCity % numberOfCities_][firstCity];
        time += timeMatrix_[lastCity % numberOfCities_][firstCity];
        
        double aux_cost = UtilDouble.transDouble(cost);
        double aux_time = UtilDouble.transDouble(time);

        solution.setObjective(0, aux_cost);
        solution.setObjective(1, aux_time);

        solution.marked(); //marco la solución para no volver a calcularle el fitness
        //System.out.println("-----------------------");
    }   
  } // evaluate

  public void readProblemFile(String file) throws IOException {
      
    Reader inputFile = new BufferedReader( new InputStreamReader( new FileInputStream(file)));
    StreamTokenizer token = new StreamTokenizer(inputFile);
    String msgError = "";
    
    numberOfCities_ = 0;
    try {
      
      token.nextToken();    //leo cantidad de ciudades
      numberOfCities_ = (int)token.nval;
      
      if (numberOfCities_ > 0) {
        
        cityNames_  = new String [numberOfCities_];                      //inicializo arreglo de ciudades
        costMatrix_ = new double[numberOfCities_][numberOfCities_ * 3]; //inicializo matriz de costos
        timeMatrix_ = new double[numberOfCities_][numberOfCities_ * 3]; //inicializo matriz de tiempos
        String city_name;
        boolean espacio;
        int ciudad = 0;
        token.nextToken();
        boolean error = false;
        while (ciudad < numberOfCities_ && !error) {
          
          //Obtengo nombre de la ciudad  
          city_name = "";
          espacio = false;
          while (token.ttype == token.TT_WORD) { //itero tomando palabras si la ciudad tiene mas de una palabra
            if (!espacio) {
              city_name += token.sval;
              espacio = true;
            } else {
              city_name += " " + token.sval;
            }
            token.nextToken();
          }
          cityNames_[ciudad] = city_name;  //agrego el nombre de la ciudad
          
          costMatrix_[ciudad][ciudad] = 0;
          timeMatrix_[ciudad][ciudad] = 0;
          costMatrix_[ciudad][ciudad + numberOfCities_] = 0;
          timeMatrix_[ciudad][ciudad + numberOfCities_] = 0;
          costMatrix_[ciudad][ciudad + numberOfCities_ * 2] = 0;
          timeMatrix_[ciudad][ciudad + numberOfCities_ * 2] = 0;
          
          int j = ciudad + 1;
          for (int i = 1; i <= j; i++) {
            token.nextToken();
          }
          
          double cost_plane;
          double cost_train;
          double cost_bus;
          double time_plane;
          double time_train;
          double time_bus;
          
          while (j < numberOfCities_ && !error) {
            
            cost_plane = costPlane_;
            time_plane = timePlane_;
            if (PseudoRandom.randDouble() < 0.1) {
              cost_plane = 0;
              time_plane = 0;
            }
            cost_train = costTrain_;
            time_train = timeTrain_;
            if (PseudoRandom.randDouble() < 0.1) {
              cost_train = 0;
              time_train = 0;
            }
            cost_bus = costBus_;
            time_bus = timeBus_;
            if (PseudoRandom.randDouble() < 0.1) {
              cost_bus = 0;
              time_bus = 0;
            }
            
            if (cost_bus == 0 && cost_train == 0 && cost_plane == 0) {
                cost_plane = costPlane_;
            }
                
            if (token.nval > 0) {
              
              costMatrix_[ciudad][j] = UtilDouble.transDouble(token.nval * costPlane_);             
              timeMatrix_[ciudad][j] = UtilDouble.transDouble(token.nval * timePlane_);
              costMatrix_[ciudad][j + numberOfCities_] = UtilDouble.transDouble(token.nval * costTrain_);
              timeMatrix_[ciudad][j + numberOfCities_] = UtilDouble.transDouble(token.nval * timeTrain_);
              costMatrix_[ciudad][j + numberOfCities_ * 2] = UtilDouble.transDouble(token.nval * costBus_);
              timeMatrix_[ciudad][j + numberOfCities_ * 2] = UtilDouble.transDouble(token.nval * timeBus_);
              
              costMatrix_[j][ciudad] = UtilDouble.transDouble(token.nval * costPlane_);
              timeMatrix_[j][ciudad] = UtilDouble.transDouble(token.nval * timePlane_);
              costMatrix_[j][ciudad + numberOfCities_] = UtilDouble.transDouble(token.nval * costTrain_);
              timeMatrix_[j][ciudad + numberOfCities_] = UtilDouble.transDouble(token.nval * timeTrain_);
              costMatrix_[j][ciudad + numberOfCities_ * 2] = UtilDouble.transDouble(token.nval * costBus_);
              timeMatrix_[j][ciudad + numberOfCities_ * 2] = UtilDouble.transDouble(token.nval * timeBus_);
              
              token.nextToken();
              j++;
            } else {
              error = true;
              msgError = "TSP.readProblemFile(): values must be positive";
            }
          }
          ciudad += 1;
        }
        
        if (error) {
          System.err.println (msgError);
          System.exit(1);
        }
      } else {
        System.err.println ("TSP.readProblemFile(): numberOfCities_ cannot be zero");
        System.exit(1);
      }
    } // try
    catch (Exception e) { 
      System.err.println ("TSP.readProblemFile(): error when reading data file "+e);
      System.exit(1);
    } // catch
  } // readProblemCost
  
  public void showArray(String [] arreglo) throws IOException {
    
    int i = 0;
    while (i < arreglo.length) {
      System.out.println(i + " - " + arreglo[i]);
      i += 1;
    }  
    System.out.println("arreglo.length = " + arreglo.length);
  } //showArray
  
  public void showMatrix(double [] [] matriz) throws IOException {
    
    int i;
    int j;
    
    i = 0;
    while (i < matriz.length) {
      j = 0;
      while (j < matriz[i].length) {
        System.out.print(" " + matriz[i][j]);
        j += 1;
      }
      System.out.println();
      i += 1;
    } 
    
    System.out.println("matriz.length = " + matriz.length);
    System.out.println("matriz[0].length = " + matriz[0].length);
    System.out.println();
  } //showArray
  
  public java.util.ArrayList<Integer> mediosPosibles(int ciudad_origen, int ciudad_destino) {
      
      java.util.ArrayList<Integer> valoresPosibles = new java.util.ArrayList<Integer>(3);
      
      if (costMatrix_[ciudad_origen][ciudad_destino] > 0) {
          valoresPosibles.add(ciudad_destino);
      }
      if (costMatrix_[ciudad_origen][ciudad_destino + numberOfCities_] > 0) {
          valoresPosibles.add(ciudad_destino + numberOfCities_);
      }
      if (costMatrix_[ciudad_origen][ciudad_destino + numberOfCities_ * 2] > 0) {
          valoresPosibles.add(ciudad_destino + numberOfCities_ * 2);
      }
      
      return valoresPosibles;
  } // mediosPosibles
  
  public int getCity(int valor) {
    return (valor % numberOfCities_);
  }
} // ProblemTSP
