package joshvdh.com.codechallengeapril.audiofft

//******************************************************************************
// SCICHART® Copyright SciChart Ltd. 2011-2017. All rights reserved.
//
// Web: http://www.scichart.com
// Support: support@scichart.com
// Sales:   sales@scichart.com
//
// Radix2FFT.java is part of the SCICHART® Showcases. Permission is hereby granted
// to modify, create derivative works, distribute and publish any part of this source
// code whether for commercial, private or personal use.
//
// The SCICHART® examples are distributed in the hope that they will be useful, but
// without any warranty. It is provided "AS IS" without warranty of any kind, either
// expressed or implied.
//******************************************************************************

/**
 * FFT implementation based on code from
 * http://stackoverflow.com/documentation/algorithm/8683/fast-fourier-transform/27088/radix-2-fft
 */
public class Radix2FFT {

    private class Complex {
        var re = 0.0
        var im = 0.0
    }

    private var n = 0
    private var m = 0
    private var mm1 = 0

    private var fftSize = 0

    private var x = arrayOf<Complex>()
    private var dft = arrayOf<Complex>()
    private var TwoPi_N = 0.0

    private var WN = Complex()      // Wn is the exponential weighting function in the form a + jb
    private var TEMP = Complex()    // TEMP is used to save computation in the butterfly calc

    constructor(n: Int) {
        this.n = n
        this.m = logBaseX(n.toDouble(), 2.0).toInt()

        if (Math.pow(2.0, m.toDouble()) != n.toDouble())
            throw UnsupportedOperationException("n should be with power of 2");

        this.fftSize = n / 2
        this.TwoPi_N = Math.PI * 2 / n    // constant to save computational time.  = 2*PI / N
        this.mm1 = m - 1

        this.x = Array<Complex>(n) { i -> Complex() }
        this.dft = Array<Complex>(n) { i -> Complex() }
    }

    fun run(input: ShortArray, output: MutableList<Double>) {
        if (input.size != n) throw UnsupportedOperationException()

        // init input values
        for (i in 0 until n) {
            val complex = x[i]
            complex.re = input[i].toDouble()
            complex.im = 0.0
        }

        // perform fft
        rad2FFT(x, dft)

        // set output
        output.clear()
        output.addAll(
            Array(fftSize) {
                i ->
                calculateOutputValue(dft[i])
            }
        )
    }
//    public void run(ShortValues input, DoubleValues output)
//    {
//        if (input.size() != n) throw new UnsupportedOperationException ();
//
//        // init input values
//        final short [] itemsArray = input . getItemsArray ();
//        for (int i = 0; i < n; i++) {
//            final Complex complex = x[i];
//            complex.re = itemsArray[i];
//            complex.im = 0;
//        }
//
//        // perform fft
//        rad2FFT(x, dft);
//
//        // set output
//        output.setSize(fftSize);
//        final double [] outputItems = output . getItemsArray ();
//        for (int i = 0; i < fftSize; i++) {
//            outputItems[i] = calculateOutputValue(dft[i]);
//        }
//    }

    private fun calculateOutputValue(complex: Complex): Double {
        val magnitude = Math.sqrt(complex.re * complex.re + complex.im * complex.im)

        // convert to magnitude to dB
        return 20 * Math.log10(magnitude / n)
    }

//    private double calculateOutputValue(Complex complex)
//    {
//        final double magnitude = Math.sqrt(complex.re * complex.re + complex.im * complex.im);
//
//        // convert to magnitude to dB
//        return 20 * Math.log10(magnitude / n);
//    }

    private fun rad2FFT(x: Array<Complex>, DFT: Array<Complex>) {
        var BSep: Int                  // BSep is memory spacing between butterflies
        var BWidth: Int                // BWidth is memory spacing of opposite ends of the butterfly
        var P: Int                     // P is number of similar Wn's to be used in that stage
        var iaddr: Int                 // bitmask for bit reversal
        var ii: Int                    // Integer bitfield for bit reversal (Decimation in Time)

        var DFTindex = 0          // Pointer to first elements in DFT array

        // Decimation In Time - x[n] sample sorting
        var i = 0
        while (i < n) {
            val pX = x[i]        // Calculate current x[n] from index i.
            ii = 0                         // Reset new address for DFT[n]
            iaddr = i                      // Copy i for manipulations
            for (l in 0 until m)
            // Bit reverse i and store in ii...
            {
                if (iaddr and 0x01 != 0)
                // Detemine least significant bit
                    ii += 1 shl mm1 - l // Increment ii by 2^(M-1-l) if lsb was 1
                iaddr =
                    iaddr shr 1                // right shift iaddr to test next bit. Use logical operations for speed increase
                if (iaddr == 0)
                    break
            }

            val dft = DFT[ii]    // Calculate current DFT[n] from bit reversed index ii
            dft.re = pX.re                 // Update the complex array with address sorted time domain signal x[n]
            dft.im = pX.im                 // NB: Imaginary is always zero
            i++
            DFTindex++
        }

        // FFT Computation by butterfly calculation
        for (stage in 1..m)
        // Loop for M stages, where 2^M = N
        {
            BSep = Math.pow(2.0, stage.toDouble()).toInt()  // Separation between butterflies = 2^stage
            P = n / BSep                       // Similar Wn's in this stage = N/Bsep
            BWidth = BSep / 2                  // Butterfly width (spacing between opposite points) = Separation / 2.

            for (j in 0 until BWidth)
            // Loop for j calculations per butterfly
            {
                if (j != 0)
                // Save on calculation if R = 0, as WN^0 = (1 + j0)
                {
                    WN.re = Math.cos(TwoPi_N * P.toDouble() * j.toDouble())     // Calculate Wn (Real and Imaginary)
                    WN.im = -Math.sin(TwoPi_N * P.toDouble() * j.toDouble())
                }

                // HiIndex is the index of the DFT array for the top value of each butterfly calc
                var HiIndex = j
                while (HiIndex < n)
                // Loop for HiIndex Step BSep butterflies per stage
                {
                    val pHi = DFT[HiIndex]                  // Point to higher value
                    val pLo = DFT[HiIndex + BWidth]         // Point to lower value

                    if (j != 0)
                    // If exponential power is not zero...
                    {
                        // Perform complex multiplication of LoValue with Wn
                        TEMP.re = pLo.re * WN.re - pLo.im * WN.im
                        TEMP.im = pLo.re * WN.im + pLo.im * WN.re

                        // Find new LoValue (complex subtraction)
                        pLo.re = pHi.re - TEMP.re
                        pLo.im = pHi.im - TEMP.im

                        // Find new HiValue (complex addition)
                        pHi.re = pHi.re + TEMP.re
                        pHi.im = pHi.im + TEMP.im
                    } else {
                        TEMP.re = pLo.re
                        TEMP.im = pLo.im

                        // Find new LoValue (complex subtraction)
                        pLo.re = pHi.re - TEMP.re
                        pLo.im = pHi.im - TEMP.im

                        // Find new HiValue (complex addition)
                        pHi.re = pHi.re + TEMP.re
                        pHi.im = pHi.im + TEMP.im
                    }
                    HiIndex += BSep
                }
            }
        }
    }


    companion object {
        fun logBaseX(value: Double, base: Double): Double {
            return Math.log(value) / Math.log(base)
        }
    }
}