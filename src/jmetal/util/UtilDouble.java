/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmetal.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 *
 * @author Diego
 */
public class UtilDouble {
    
    public static double transDouble(double entrada) {
        double salida = -1;
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("####.##",simbolos);
        String num_str = formateador.format(entrada);
        salida = Double.parseDouble(num_str);
        return salida;
    }
}
