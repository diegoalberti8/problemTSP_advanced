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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import jmetal.util.PseudoRandom;

/** 
 * Class to configure and execute the NSGA-II algorithm. The experiments.settings are aimed
 * at solving the ProblemTSP problem.
 */

public class Generator_ProblemTSP {

  /**
   * @param args Command line arguments.
   * @throws JMException 
   * @throws IOException 
   * @throws SecurityException 
   * Usage: 
   *      - jmetal.metaheuristics.nsgaII.NSGAII_ProblemTSP_main
   */
  public static void main(String [] args) throws JMException, SecurityException, IOException, ClassNotFoundException {
    
    String path = "distancias.txt";
    
    try {
      /* Open the file */
      FileOutputStream fos   = new FileOutputStream(path)     ;
      OutputStreamWriter osw = new OutputStreamWriter(fos)    ;
      BufferedWriter bw      = new BufferedWriter(osw)        ;
      
      int cant_ciudades = PseudoRandom.randInt(70, 70);
      bw.write(Integer.toString(cant_ciudades));
      bw.newLine();
      
      String nombre = "Ciudad";
      for (int i = 0; i < cant_ciudades; i++) {
          
        bw.write(nombre + i + " ");
        for (int j = 0; j < cant_ciudades; j++) {
            if (j == i) {
                bw.write(0 + " ");
            } else {
                bw.write(PseudoRandom.randInt(20, 20000) + " ");
            }
        }
        bw.newLine();
      }

      /* Close the file */
      bw.close();
    }catch (IOException e) {
      Configuration.logger_.severe("Error acceding to the file");
      e.printStackTrace();
    }
  } //main
} // Generator_ProblemTSP
