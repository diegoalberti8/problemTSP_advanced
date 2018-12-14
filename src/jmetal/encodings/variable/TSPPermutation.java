//  TSPPermutation.java
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

package jmetal.encodings.variable;
import jmetal.problems.ProblemTSP;
import jmetal.core.Variable;
import jmetal.util.PseudoRandom;

/**
 * Class implementing a permutation of integer decision encodings.variable
 */
public class TSPPermutation extends Variable {

  /**
   * Stores a permutation of <code>int</code> values
   */
  public int [] vector_;

  /**
   * Stores the length of the permutation
   */
  public int size_;

  /**
   * Constructor
   */
  public TSPPermutation() {
    size_   = 0;
    vector_ = null;

  } //Permutation

  public TSPPermutation(TSPPermutation permutation) {
    size_   = permutation.size_;
    vector_ = permutation.vector_;

  } //Permutation
  
  /**
   * Constructor
   * @param size Length of the permutation
   * This constructor has been contributed by Madan Sathe
   */
  public TSPPermutation(ProblemTSP problem) {
    size_   = problem.getLength(0);
    vector_ = new int[size_];

    java.util.ArrayList<Integer> randomSequence = new java.util.ArrayList<Integer>(size_);

    for(int i = 0; i < size_; i++) {
      randomSequence.add(i);
    }
    
    java.util.ArrayList<Integer> valoresPosibles;
    int ciudad_origen;
    int ciudad_destino;
    int valor_final;
    double random;   
    java.util.Collections.shuffle(randomSequence);
    for(int j = 1; j < randomSequence.size(); j++) {
        
        ciudad_origen = randomSequence.get(j-1);
        ciudad_destino = randomSequence.get(j);
        
        valoresPosibles = problem.mediosPosibles(ciudad_origen,ciudad_destino);
        
        valor_final = -1;
        if (valoresPosibles.size() == 1) {
            valor_final = valoresPosibles.get(0);
        }
        
        if (valoresPosibles.size() == 2) {
            random = PseudoRandom.randDouble();
            if (random < 0.5) { 
                valor_final = valoresPosibles.get(0);
            } else {
                valor_final = valoresPosibles.get(1);
            }
        }
        
        if (valoresPosibles.size() == 3) {
            random = PseudoRandom.randDouble();
            if (random < 0.33) { 
                valor_final = valoresPosibles.get(0);
            } else {
                if (random < 0.66) {
                    valor_final = valoresPosibles.get(1);
                } else {
                    valor_final = valoresPosibles.get(2);
                }    
            }
        }
        vector_[j] = valor_final;
    }    
    
    ciudad_origen = randomSequence.get(randomSequence.size() - 1);
    ciudad_destino = randomSequence.get(0);

    valoresPosibles = problem.mediosPosibles(ciudad_origen,ciudad_destino);

    valor_final = -1;
    if (valoresPosibles.size() == 1) {
        valor_final = valoresPosibles.get(0);
    }

    if (valoresPosibles.size() == 2) {
        random = PseudoRandom.randDouble();
        if (random < 0.5) { 
            valor_final = valoresPosibles.get(0);
        } else {
            valor_final = valoresPosibles.get(1);
        }
    }

    if (valoresPosibles.size() == 3) {
        random = PseudoRandom.randDouble();
        if (random < 0.33) { 
            valor_final = valoresPosibles.get(0);
        } else {
            if (random < 0.66) {
                valor_final = valoresPosibles.get(1);
            } else {
                valor_final = valoresPosibles.get(2);
            }    
        }
    }
    vector_[0] = valor_final;
        
    //debug
    /*int i = 0;
    while (i < vector_.length) {
      System.out.print(vector_[i] + ", ");
      i += 1;
    }
    System.out.println();*/
    //debug
        
  } // Constructor


  /**
   * Copy Constructor
   * @param permutation The permutation to copy
   */
  /*public TSPPermutation(TSPPermutation permutation) {
    size_   = permutation.size_;
    vector_ = new int[size_];

    System.arraycopy(permutation.vector_, 0, vector_, 0, size_);
  } //Permutation*/


  /**
   * Create an exact copy of the <code>Permutation</code> object.
   * @return An exact copy of the object.
   */
  public Variable deepCopy() {
    return new TSPPermutation(this);
  } //deepCopy

  /**
   * Returns the length of the permutation.
   * @return The length
   */
  public int getLength(){
    return size_;
  } //getNumberOfBits

  /**
   * Returns a string representing the object
   * @return The string
   */
  public String toString(){
    String string ;

    string = "" ;
    for (int i = 0; i < size_ ; i ++)
      string += vector_[i] + " " ;

    return string ;
  } // toString
} // Permutation
