package com.filter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class AnalogButterworth extends AnalogPrototype {
    /**
     * Instantiates a new analog Butterworth filter design with the indicated number of poles.
     *
     * @param order   int specifying the number of poles of the filter.
     */
    public AnalogButterworth( int order ) {
            
            super();
            
            int nRealPoles        = order - 2*(order/2);
            int nComplexPolePairs = order/2;
            int nPoles            = nRealPoles + 2*nComplexPolePairs;
            
    if ( nRealPoles == 1 ) {
      double[] td = {1.0, 1.0};
      addSection( new Rational( new Polynomial(1.0), new Polynomial(td) ) );
    }
    
    double dAngle = Math.PI/nPoles;

    for ( int i = 0;  i < nComplexPolePairs;  i++ ) {
        double   angle = -Math.PI/2  +  dAngle/2 *( 1 + nRealPoles )  +  i*dAngle;
        double[] td    = {1.0, -2*Math.sin(angle), 1.0 };
        addSection( new Rational( new Polynomial(1.0), new Polynomial(td) ) );
    }
    
    }

}
