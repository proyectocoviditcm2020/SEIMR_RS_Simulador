/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package heuristics;

import java.util.LinkedList;

/**
 *
 * @author J. Alfredo Brambila Hdez.
 */
public class ModelSEIMRRS2_C {
    // int pobTotal = 7413000;
    int pobTotal;

    double w = 1.0;
    int dias = 365;
    double[] A = new double[2];
    // double[] delta = new double[4];
    double[] gamma = new double[4];
    double[] sigma = new double[4];

    double[] t;
    double[][][] S;
    double[][][] E;
    double[][][] I0;
    double[][][] I1;
    double[][][] I2;
    double[][][] I3;
    double[][][] R;
    double[][][] D;
    double[][][] NR;

    int[][] poblacion;

    double beta0;
    double dt;
    // double beta;
    double miu;
    double miuN;
    double psi;
    double gamma0;
    double sigma0;
    double zeta;

    double valorZR1;
    double valorZR2;
    double valorZR3;

    private double[] gZeta = new double[4];
    private double[] gSigma = new double[4];
    private double miuSigma;

    double gammaX;

    int escenario;

    int regiones = 3;
    int estratos = 6;

    double vZeta[][];
    double PHI[][][]; // fracc tiempo
    double phi[][][]; // traf gente entre regiones

    public ModelSEIMRRS2_C() {
        /*delta[0] = ((double) 3)/10;
        delta[1] = ((double) 4)/5;
        delta[2] = ((double) 5)/7;
        delta[3] = ((double) 1)/2;*/

        gamma[0] = ((double) 1)/10;//<-
        //gamma[1] = ((double) 1)/8;
        //gamma[2] = ((double) 1)/8;
        //gamma[3] = ((double) 1)/10;

        sigma[0] = ((double) 1)/4.1;
        //sigma[1] = ((double) 1)/5;
        //sigma[2] = ((double) 1)/6;
        //sigma[3] = ((double) 1)/10;

        poblacion = new int[regiones][estratos];
        
        PHI = new double[regiones][regiones][estratos]; // fracc tiempo
        phi = new double[regiones][regiones][estratos]; // traf gente entre regiones
        vZeta = new double[regiones][estratos];

        // carga valores de los parámetros por default
        parametrosDefault();
        //condiciones iniciales: numero de poblacion, susceptibles, infectados, etc
        condicionesIniciales();
        // ejecuta simulacion del modelo (ecuaciones)
        simulaModelo();
    }

    // condiciones iniciales: numero de poblacion, susceptibles, infectados, etc
    public void condicionesIniciales() {

        // vectores de estados
        t = llenaDias(dias + 1); // arreglo con los 365 dias del año
        S = zeros(dias + 1); // zeros crea arreglo de tamaño n y lo llena con ceros
        E = zeros(dias + 1);
        I0 = zeros(dias + 1);
        I1 = zeros(dias + 1);
        I2 = zeros(dias + 1);
        I3 = zeros(dias + 1);
        R = zeros(dias + 1);
        D = zeros(dias + 1);
        NR = zeros(dias + 1);

        // pobTotal = 758659;
        pobTotal = 790521;

        // Tampico [region][estrato]
        poblacion[0][0] = 55642; // 1
        poblacion[0][1] = 52369; // 2
        poblacion[0][2] = 19639; // 3
        poblacion[0][3] = 88373; // 4
        poblacion[0][4] = 81827; // 5
        poblacion[0][5] = 29458; // 6

        // Madero [region][estrato]
        poblacion[1][0] = 36833; // 1
        poblacion[1][1] = 32500; // 2
        poblacion[1][2] = 10833; // 3
        poblacion[1][3] = 62832; // 4
        poblacion[1][4] = 54166; // 5
        poblacion[1][5] = 19500; // 6

        // Altamira [region][estrato]
        poblacion[2][0] = 69379; // 1
        poblacion[2][1] = 50481; // 2
        poblacion[2][2] = 9579; // 3
        poblacion[2][3] = 62771; // 4
        poblacion[2][4] = 45673; // 5
        poblacion[2][5] = 8666; // 6

        // valores iniciales susceptibles
        S[0][0][0] = ((double) poblacion[0][0]) / poblacion[0][0];
        S[0][1][0] = ((double) poblacion[0][1]) / poblacion[0][1];
        S[0][2][0] = ((double) poblacion[0][2]) / poblacion[0][2];
        S[0][3][0] = ((double) poblacion[0][3]) / poblacion[0][3];
        S[0][4][0] = ((double) poblacion[0][4]) / poblacion[0][4];
        S[0][5][0] = ((double) poblacion[0][5]) / poblacion[0][5];

        S[1][0][0] = ((double) poblacion[1][0]) / poblacion[1][0];
        S[1][1][0] = ((double) poblacion[1][1]) / poblacion[1][1];
        S[1][2][0] = ((double) poblacion[1][2]) / poblacion[1][2];
        S[1][3][0] = ((double) poblacion[1][3]) / poblacion[1][3];
        S[1][4][0] = ((double) poblacion[1][4]) / poblacion[1][4];
        S[1][5][0] = ((double) poblacion[1][5]) / poblacion[1][5];

        S[2][0][0] = ((double) poblacion[2][0]) / poblacion[2][0];
        S[2][1][0] = ((double) poblacion[2][1]) / poblacion[2][1];
        S[2][2][0] = ((double) poblacion[2][2]) / poblacion[2][2];
        S[2][3][0] = ((double) poblacion[2][3]) / poblacion[2][3];
        S[2][4][0] = ((double) poblacion[2][4]) / poblacion[2][4];
        S[2][5][0] = ((double) poblacion[2][5]) / poblacion[2][5];

        // valores iniciales Asintomáticos
        /*
         * I0[0][0][0] = ((double)1)/poblacion[0][0]; I0[0][1][0] =
         * ((double)1)/poblacion[0][1]; I0[0][2][0] = ((double)1)/poblacion[0][2];
         * I0[0][3][0] = ((double)1)/poblacion[0][3]; I0[0][4][0] =
         * ((double)1)/poblacion[0][4];
         */
        I0[0][5][0] = ((double) 1) / poblacion[0][5];

        /*
         * I0[1][0][0] = ((double)1)/poblacion[1][0]; I0[1][1][0] =
         * ((double)1)/poblacion[1][1]; I0[1][2][0] = ((double)1)/poblacion[1][2];
         * I0[1][3][0] = ((double)1)/poblacion[1][3]; I0[1][4][0] =
         * ((double)1)/poblacion[1][4];
         */
        I0[1][5][0] = ((double) 1) / poblacion[1][5];

        /*
         * I0[2][0][0] = ((double)1)/poblacion[2][0]; I0[2][1][0] =
         * ((double)1)/poblacion[2][1]; I0[2][2][0] = ((double)1)/poblacion[2][2];
         * I0[2][3][0] = ((double)1)/poblacion[2][3]; I0[2][4][0] =
         * ((double)1)/poblacion[2][4];
         */
        I0[2][5][0] = ((double) 1) / poblacion[2][5];

        // valores iniciales Moderados
        /*
         * I1[0][0][0] = ((double)3)/poblacion[0][0]; I1[0][1][0] =
         * ((double)10)/poblacion[0][1]; I1[0][2][0] = ((double)5)/poblacion[0][2];
         * I1[0][3][0] = ((double)2)/poblacion[0][3]; I1[0][4][0] =
         * ((double)15)/poblacion[0][4]; I1[0][5][0] = ((double)7)/poblacion[0][5];
         * 
         * I1[1][0][0] = ((double)5)/poblacion[1][0]; I1[1][1][0] =
         * ((double)15)/poblacion[1][1]; I1[1][2][0] = ((double)5)/poblacion[1][2];
         * I1[1][3][0] = ((double)3)/poblacion[1][3]; I1[1][4][0] =
         * ((double)20)/poblacion[1][4]; I1[1][5][0] = ((double)7)/poblacion[1][5];
         * 
         * I1[2][0][0] = ((double)1)/poblacion[2][0]; I1[2][1][0] =
         * ((double)2)/poblacion[2][1]; I1[2][2][0] = ((double)2)/poblacion[2][2];
         * I1[2][3][0] = ((double)2)/poblacion[2][3]; I1[2][4][0] =
         * ((double)4)/poblacion[2][4]; I1[2][5][0] = ((double)1)/poblacion[2][5];
         * 
         * // valores iniciales Severos I2[0][0][0] = ((double)1)/poblacion[0][0];
         * I2[0][1][0] = ((double)1)/poblacion[0][1]; I2[0][2][0] =
         * ((double)1)/poblacion[0][2]; I2[0][3][0] = ((double)1)/poblacion[0][3];
         * I2[0][4][0] = ((double)1)/poblacion[0][4]; I2[0][5][0] =
         * ((double)1)/poblacion[0][5];
         * 
         * I2[1][0][0] = ((double)1)/poblacion[1][0]; I2[1][1][0] =
         * ((double)1)/poblacion[1][1]; I2[1][2][0] = ((double)1)/poblacion[1][2];
         * I2[1][3][0] = ((double)1)/poblacion[1][3]; I2[1][4][0] =
         * ((double)1)/poblacion[1][4]; I2[1][5][0] = ((double)1)/poblacion[1][5];
         * 
         * I2[2][0][0] = ((double)1)/poblacion[2][0]; I2[2][1][0] =
         * ((double)1)/poblacion[2][1]; I2[2][2][0] = ((double)1)/poblacion[2][2];
         * I2[2][3][0] = ((double)1)/poblacion[2][3]; I2[2][4][0] =
         * ((double)1)/poblacion[2][4]; I2[2][5][0] = ((double)1)/poblacion[2][5];
         */
        // valores iniciales Graves se queda en ceros

        int valorE = 10;

        // valores iniciales Expuestos
        E[0][0][0] = ((double) valorE * 0.17) / poblacion[0][0];
        E[0][1][0] = ((double) valorE * 0.16) / poblacion[0][1];
        E[0][2][0] = ((double) valorE * 0.06) / poblacion[0][2];
        E[0][3][0] = ((double) valorE * 0.27) / poblacion[0][3];
        E[0][4][0] = ((double) valorE * 0.25) / poblacion[0][4];
        E[0][5][0] = ((double) valorE * 0.09) / poblacion[0][5];

        E[1][0][0] = ((double) valorE * 0.17) / poblacion[1][0];
        E[1][1][0] = ((double) valorE * 0.15) / poblacion[1][1];
        E[1][2][0] = ((double) valorE * 0.05) / poblacion[1][2];
        E[1][3][0] = ((double) valorE * 0.29) / poblacion[1][3];
        E[1][4][0] = ((double) valorE * 0.25) / poblacion[1][4];
        E[1][5][0] = ((double) valorE * 0.09) / poblacion[1][5];

        E[2][0][0] = ((double) valorE * 0.28) / poblacion[2][0];
        E[2][1][0] = ((double) valorE * 0.20) / poblacion[2][1];
        E[2][2][0] = ((double) valorE * 0.04) / poblacion[2][2];
        E[2][3][0] = ((double) valorE * 0.25) / poblacion[2][3];
        E[2][4][0] = ((double) valorE * 0.19) / poblacion[2][4];
        E[2][5][0] = ((double) valorE * 0.04) / poblacion[2][5];

        vZeta[0][0] = valorZR1;
        vZeta[0][1] = valorZR1;
        vZeta[0][2] = valorZR1;
        vZeta[0][3] = valorZR1;
        vZeta[0][4] = valorZR1;
        vZeta[0][5] = valorZR1;

        vZeta[1][0] = valorZR2;
        vZeta[1][1] = valorZR2;
        vZeta[1][2] = valorZR2;
        vZeta[1][3] = valorZR2;
        vZeta[1][4] = valorZR2;
        vZeta[1][5] = valorZR2;

        vZeta[2][0] = valorZR3;
        vZeta[2][1] = valorZR3;
        vZeta[2][2] = valorZR3;
        vZeta[2][3] = valorZR3;
        vZeta[2][4] = valorZR3;
        vZeta[2][5] = valorZR3;

        // int valorD=1;
        /*
         * POBRES JOVENES POBRES ADULTOS POBRES ADULTOS MAYORES NO POBRES JOVENES NO
         * POBRES ADULTOS NO POBRES ADULTOS MAYORES
         * 
         */
        // valores iniciales Expuestos
        /*
         * D[0][0][0] = ((double)1)/poblacion[0][0]; D[0][1][0] =
         * ((double)1)/poblacion[0][1]; D[0][2][0] = ((double)1)/poblacion[0][2];
         * D[0][3][0] = ((double)1)/poblacion[0][3]; D[0][4][0] =
         * ((double)1)/poblacion[0][4]; D[0][5][0] = ((double)1)/poblacion[0][5];
         * 
         * D[1][0][0] = ((double)1)/poblacion[1][0]; D[1][1][0] =
         * ((double)1)/poblacion[1][1]; D[1][2][0] = ((double)1)/poblacion[1][2];
         * D[1][3][0] = ((double)1)/poblacion[1][3]; D[1][4][0] =
         * ((double)1)/poblacion[1][4]; D[1][5][0] = ((double)1)/poblacion[1][5];
         * 
         * D[2][0][0] = ((double)1)/poblacion[2][0]; D[2][1][0] =
         * ((double)1)/poblacion[2][1]; D[2][2][0] = ((double)1)/poblacion[2][2];
         * D[2][3][0] = ((double)1)/poblacion[2][3]; D[2][4][0] =
         * ((double)1)/poblacion[2][4]; D[2][5][0] = ((double)1)/poblacion[2][5];
         */

    }

    public void parametrosDefault() {

        // valores para cuarentena (solo se usa uno por el momento)
        A[0] = 0.0; // asintomáticos
        // A[1] = 0.3; //sintomáticos

        beta0 = (1 - A[0]) * 0.3271875 + A[0] * -2.98 * Math.log(1 - 0.01);
        sigma0 = sigma[0];
        zeta = 0.75; // z por region //
        escenario = 1;
        dt = 1;
        miu = 0.001;
        miuN = 0.00005;
        psi = 1;
        gamma0 = gamma[0];
        gammaX = 0.8;

        // anteriores
        // valorZR1=0.79;
        // valorZR2=1.16;
        // valorZR3=1.47;

        valorZR1 = 0.545;
        valorZR2 = 0.744;
        valorZR3 = 1.085;

        gZeta[0] = 0.051820;
        gZeta[1] = 0.051820;
        gZeta[2] = 0.051820;
        gZeta[3] = 0.051820;

        gSigma[0] = 0.024390;
        gSigma[1] = 0.024390;
        gSigma[2] = 0.024390;
        gSigma[3] = 0.024390;

        miuSigma = 0.001;

        // PHI Fracción de tiempo que la gente permanece al moverse a una region
        // Tampico 0, Madero 1, Altamira 2
        // [Region Origen][Region Destino][Estrato]
        // Tampico a Madero
        /*
         * PHI[0][1][0] = 0.1; PHI[0][1][1] = 0.1; PHI[0][1][2] = 0.1; PHI[0][1][3] =
         * 0.1; PHI[0][1][4] = 0.1; PHI[0][1][5] = 0.1; // de Tampico a Altamira
         * PHI[0][2][0] = 0.1; PHI[0][2][1] = 0.1; PHI[0][2][2] = 0.1; PHI[0][2][3] =
         * 0.1; PHI[0][2][4] = 0.1; PHI[0][2][5] = 0.1;
         * 
         * // de Madero a Tampico PHI[1][0][0] = 0.1; PHI[1][0][1] = 0.1; PHI[1][0][2] =
         * 0.1; PHI[1][0][3] = 0.1; PHI[1][0][4] = 0.1; PHI[1][0][5] = 0.1; // de Madero
         * a Altamira PHI[1][2][0] = 0.1; PHI[1][2][1] = 0.1; PHI[1][2][2] = 0.1;
         * PHI[1][2][3] = 0.1; PHI[1][2][4] = 0.1; PHI[1][2][5] = 0.1;
         * 
         * // de Altamira a Tampico PHI[2][0][0] = 0.1; PHI[2][0][1] = 0.1; PHI[2][0][2]
         * = 0.1; PHI[2][0][3] = 0.1; PHI[2][0][4] = 0.1; PHI[2][0][5] = 0.1; // de
         * Altamira a Madero PHI[2][1][0] = 0.1; PHI[2][1][1] = 0.1; PHI[2][1][2] = 0.1;
         * PHI[2][1][3] = 0.1; PHI[2][1][4] = 0.1; PHI[2][1][5] = 0.1;
         */
        // phi fraccion de gente q se mueve
        // RO RD SS
        // Tampico 0, Madero 1, Altamira 2
        // [Region Origen][Region Destino][Estrato]
        // de Tampico a Madero

        // LLenar PHI(Fraccion de tiempo) y phi(Porción de genten que se mueve entre
        // regiones)
        for (int i = 1; i < this.regiones; i++) {
            for (int j = 0; j < this.estratos; j++) {
                PHI[0][i][j] = 0.0; // 0.5
                phi[0][i][j] = 0.0; // 0.2
            }
        }
        for (int i = 0; i < this.regiones; i++) {
            for (int j = 0; j < this.estratos; j++) {
                if (i != 1) {
                    PHI[1][i][j] = 0.0;
                    phi[1][i][j] = 0.0;
                }
            }
        }
        for (int i = 0; i < this.regiones; i++) {
            for (int j = 0; j < this.estratos; j++) {
                if (i != 2) {
                    PHI[2][i][j] = 0.0;
                    phi[2][i][j] = 0.0;
                }
            }
        }
        /*
         * phi[0][1][0] = 0.2; phi[0][1][1] = 0.2; phi[0][1][2] = 0.2; phi[0][1][3] =
         * 0.2; phi[0][1][4] = 0.2; phi[0][1][5] = 0.2; // de Tampico a Altamira
         * phi[0][2][0] = 0.2; phi[0][2][1] = 0.2; phi[0][2][2] = 0.2; phi[0][2][3] =
         * 0.2; phi[0][2][4] = 0.2; phi[0][2][5] = 0.2;
         * 
         * // de Madero a Tampico phi[1][0][0] = 0.2; phi[1][0][1] = 0.5; phi[1][0][2] =
         * 0.2; phi[1][0][3] = 0.2; phi[1][0][4] = 0.2; phi[1][0][5] = 0.2; // de Madero
         * a Altamira phi[1][2][0] = 0.2; phi[1][2][1] = 0.2; phi[1][2][2] = 0.2;
         * phi[1][2][3] = 0.2; phi[1][2][4] = 0.2; phi[1][2][5] = 0.2;
         * 
         * // de Altamira a Tampico phi[2][0][0] = 0.01; phi[2][0][1] = 0.1;
         * phi[2][0][2] = 0.04; phi[2][0][3] = 0.01; phi[2][0][4] = 0.1; phi[2][0][5] =
         * 0.04; // de Altamira a Madero phi[2][1][0] = 0.05; phi[2][1][1] = 0.2;
         * phi[2][1][2] = 0.1; phi[2][1][3] = 0.05; phi[2][1][4] = 0.2; phi[2][1][5] =
         * 0.1;
         */
        /*
         * System.out.println("dt: " + dt); System.out.println("Beta: " + beta0);
         * System.out.println("miu: " + miu); System.out.println("miu^N: " + miuN);
         * System.out.println("psi: " + psi); System.out.println("Gamma: " + gamma0);
         * System.out.println("Escenario: " + escenario);
         */
    }

    public void simulaModelo() {
        // regiones
        for (int rg = 0; rg < this.regiones; rg++) {
            // estratos
            for (int ss = 0; ss < this.estratos; ss++) {
                // t (dias)
                for (int n = 0; n < this.dias; n++) {

                    this.beta0 = (1 - A[0]) * 0.3271875 + A[0] * -2.98 * Math.log(1 - 0.01);// revisar
                    /*
                     * Ecuaciones Modelo SEIMR-R-S
                     */

                    // S
                    S[rg][ss][n + 1] = S[rg][ss][n] - S2I(rg, ss, n) - miuN * S[rg][ss][n];

                    // E
                    E[rg][ss][n + 1] = E[rg][ss][n] + S2I(rg, ss, n) - psi * E[rg][ss][n];

                    // IO
                    // I0[rg][ss][n+1] = I0[rg][ss][n] + S2I(rg,ss,n) -
                    // gamma0*vZeta[rg][ss]*I0[rg][ss][n];
                    I0[rg][ss][n + 1] = I0[rg][ss][n] + S2I(rg, ss, n) - gZeta[0] * I0[rg][ss][n];

                    // I1
                    // I1[rg][ss][n+1] = I1[rg][ss][n] + gamma0*sigma0*I0[rg][ss][n] -
                    // gammaX*sigma0*I1[rg][ss][n];//*
                    I1[rg][ss][n + 1] = I1[rg][ss][n] + gSigma[0] * I0[rg][ss][n] - gSigma[1] * I1[rg][ss][n];// *

                    // I2
                    // I2[rg][ss][n+1] = I2[rg][ss][n] + gamma0*sigma0*I1[rg][ss][n] -
                    // gammaX*sigma0*I2[rg][ss][n];//*
                    I2[rg][ss][n + 1] = I2[rg][ss][n] + gSigma[1] * I1[rg][ss][n] - gSigma[2] * I2[rg][ss][n];// *

                    // I3
                    // I3[rg][ss][n+1] = I3[rg][ss][n] + gamma0*I2[rg][ss][n] -
                    // gamma0*zeta*I3[rg][ss][n];
                    // I3[rg][ss][n+1] = I3[rg][ss][n] + gamma0*I2[rg][ss][n] -
                    // gamma0*vZeta[rg][ss]*I3[rg][ss][n];
                    I3[rg][ss][n + 1] = I3[rg][ss][n] + gSigma[2] * I2[rg][ss][n] - gZeta[3] * I3[rg][ss][n];

                    // R
                    // R[rg][ss][n+1] = R[rg][ss][n] +
                    // (gamma0*I0[rg][ss][n]+gamma0*I1[rg][ss][n]+gamma0*I2[rg][ss][n]+gamma0*I3[rg][ss][n])
                    // - miuN*R[rg][ss][n]; // estados inf intermedios
                    // R[rg][ss][n+1] = R[rg][ss][n] +
                    // (gSigma[0]*I0[rg][ss][n]+gSigma[1]*I1[rg][ss][n]+gSigma[2]*I2[rg][ss][n]+gSigma[3]*I3[rg][ss][n])
                    // - miuN*R[rg][ss][n]; // estados inf intermedios
                    R[rg][ss][n + 1] = R[rg][ss][n] + (gSigma[0] * I1[rg][ss][n] + gSigma[1] * I2[rg][ss][n])
                            - miuN * R[rg][ss][n]; // estados inf intermedios

                    // D
                    D[rg][ss][n + 1] = D[rg][ss][n] + miuSigma * I3[rg][ss][n];
                    // D[rg][ss][n+1] = D[rg][ss][n] +
                    // (miuSigma*I1[rg][ss][n]+miuSigma*I2[rg][ss][n]);

                    // NR
                    NR[rg][ss][n + 1] = NR[rg][ss][n] + miuN * SR(rg, n) + miuN * RR(rg, n);
                }
            }
        }

    }

    // Parametros calculados
    public double deltaZeta() {
        double valor = 0.0;
        return valor;
    }

    public double deltaSigma() {
        double valor = 0.0;
        return valor;
    }

    public double miuSigma() {
        double valor = 0.0;
        return valor;
    }

    // movilidad
    public double IS(int rg, int ss, int n) {
        return this.I0[rg][ss][n] + this.I1[rg][ss][n] + this.I2[rg][ss][n] + this.I3[rg][ss][n];
    }

    public double II(int rg, int n) {
        double valor = 0.0;
        LinkedList<Integer> regionesLista;
        regionesLista = ROR(rg);
        // for(int ro=0; ro<regionesLista.size(); ro++) {
        for (int ro : regionesLista) {
            for (int ss = 0; ss < this.estratos; ss++) {
                valor += SSR(ro) * phi[ro][rg][ss] * PHI[ro][rg][ss] * IS(ro, ss, n);
            }
        }
        return valor;
    }

    public double IE(int rg, int n) {
        double valor = 0.0;
        LinkedList<Integer> regionesLista;
        regionesLista = RDE(rg);
        // for(int rd=0; rd<regionesLista.size(); rd++) {
        for (int rd : regionesLista) {
            for (int ss = 0; ss < this.estratos; ss++) {
                valor += SSR(rd) * phi[rg][rd][ss] * PHI[rg][rd][ss] * IS(rd, ss, n);
            }
        }
        return valor;
    }

    public double IX(int rg, int n) {
        double valor = 0.0;
        for (int ss = 0; ss < this.estratos; ss++) {
            valor += IS(rg, ss, n);
        }
        return valor;
    }

    public double IR(int rg, int n) {
        return IX(rg, n) + II(rg, n) - IE(rg, n);
    }

    public double SE(int rg, int rd, int ss, int n) {
        double valor;
        valor = phi[rg][rd][ss] * PHI[rg][rd][ss] * S[rd][ss][n];
        return valor;
    }

    public double SN(int rg, int ss, int n) {
        double valor = 0.0;
        double sumaSE = 0.0;
        LinkedList<Integer> rdLista;
        rdLista = RDE(rg);
        // for(int ird=0; ird<rdLista.size(); ird++) {
        for (int ird : rdLista) {
            for (int iss = 0; iss < this.estratos; iss++) {
                sumaSE += SE(rg, ird, iss, n);
            }
        }
        // for(int iss =0; iss<this.estratos; iss++)
        valor = S[rg][ss][n] - sumaSE;

        return valor;
    }

    public double SIN(int rg, int ss, int n) {
        double valor;
        valor = IR(rg, n) * SN(rg, ss, n);
        return valor;
    }

    public double SIE(int rg, int ss, int n) {
        double valor = 0.0;
        LinkedList<Integer> rdLista;
        rdLista = RDE(rg);
        // for(int ird=0; ird<rdLista.size(); ird++) {
        for (int ird : rdLista) {
            // for(int iss=0; iss<this.estratos; iss++) {
            valor += IR(ird, n) * SE(rg, ird, ss, n);
            // }
        }
        return valor;
    }

    public double S2I(int rg, int ss, int n) {
        double valor = 0.0;
        valor = this.beta0 * SIN(rg, ss, n) + this.beta0 * SIE(rg, ss, n);
        return valor;
    }

    public double RR(int rg, int n) {
        double valor = 0.0;
        LinkedList<Integer> regionesLista;
        regionesLista = RDE(rg);
        // for(int irg=0; irg<regionesLista.size(); irg++) {
        for (int irg : regionesLista) {
            for (int ss = 0; ss < this.estratos; ss++) {
                valor += SSR(irg) * R[rg][ss][n];
            }
        }
        return valor;
    }

    public double DR(int rg, int n) {
        double valor = 0.0;
        LinkedList<Integer> regionesLista;
        regionesLista = RDE(rg);
        // for(int irg=0; irg<regionesLista.size(); irg++) {
        for (int irg : regionesLista) {
            for (int ss = 0; ss < this.estratos; ss++) {
                valor += SSR(irg) * D[rg][ss][n];
            }
        }
        return valor;
    }

    public double SR(int rg, int n) {
        double valor = 0.0;
        LinkedList<Integer> regionesLista;
        regionesLista = RDE(rg);
        // for(int irg=0; irg<regionesLista.size(); irg++) {
        for (int irg : regionesLista) {
            for (int ss = 0; ss < this.estratos; ss++) {
                valor += S[irg][ss][n];
            }
        }
        return valor;
    }

    public double SSR(int rg) {
        double npersonas = 0;
        for (int ss = 0; ss < this.estratos; ss++) {
            npersonas += this.poblacion[rg][ss];
        }
        return (double) (npersonas / this.pobTotal); //
        // return (double)(npersonas/this.pobTotal); //
    }

    public LinkedList<Integer> ROR(int rg) {
        LinkedList<Integer> lista = new LinkedList<Integer>();
        for (int i = 0; i < this.regiones; i++) {
            if (rg != i) {
                lista.add(i);
            }
        }
        return lista;
    }

    public LinkedList<Integer> RDE(int rg) {
        LinkedList<Integer> lista = new LinkedList<Integer>();
        for (int i = 0; i < this.regiones; i++) {
            if (rg != i) {
                lista.add(i);
            }
        }
        return lista;
    }

    public String[][] getValoresSEIRMRRS(int rgi, int ssi) {
        String[][] valores = new String[this.dias + 1][10];
        for (int i = 0; i < dias; i++) {
            valores[i][0] = String.valueOf(t[i]);
            // valores[i][1] = String.valueOf(S[rgi][ssi][i]*poblacion[rgi][ssi]);
            valores[i][1] = String.valueOf(S[rgi][ssi][i] * this.getPobTotalRS(rgi, ssi));
            valores[i][2] = String.valueOf(E[rgi][ssi][i] * this.getPobTotalRS(rgi, ssi));
            valores[i][3] = String.valueOf(I0[rgi][ssi][i] * this.getPobTotalRS(rgi, ssi));
            valores[i][4] = String.valueOf(I1[rgi][ssi][i] * this.getPobTotalRS(rgi, ssi));
            valores[i][5] = String.valueOf(I2[rgi][ssi][i] * this.getPobTotalRS(rgi, ssi));
            valores[i][6] = String.valueOf(I3[rgi][ssi][i] * this.getPobTotalRS(rgi, ssi));
            valores[i][7] = String.valueOf(R[rgi][ssi][i] * this.getPobTotalRS(rgi, ssi));
            valores[i][8] = String.valueOf(D[rgi][ssi][i] * this.getPobTotalRS(rgi, ssi));
            valores[i][9] = String.valueOf(NR[rgi][ssi][i] * this.getPobTotalRS(rgi, ssi));
        }
        return valores;
    }

    public String[][] getValoresSEIRMRRS_Region(int rgi) {
        String[][] valores = new String[this.dias + 1][10];
        for (int i = 0; i < dias; i++) {
            valores[i][0] = String.valueOf(t[i]);
            // valores[i][1] = String.valueOf(S[rgi][ssi][i]*poblacion[rgi][ssi]);
            valores[i][1] = String.valueOf(sumaElementosEstadoWPob(S, rgi, i));
            valores[i][2] = String.valueOf(sumaElementosEstadoWPob(E, rgi, i));
            valores[i][3] = String.valueOf(sumaElementosEstadoWPob(I0, rgi, i));
            valores[i][4] = String.valueOf(sumaElementosEstadoWPob(I1, rgi, i));
            valores[i][5] = String.valueOf(sumaElementosEstadoWPob(I2, rgi, i));
            valores[i][6] = String.valueOf(sumaElementosEstadoWPob(I3, rgi, i));
            valores[i][7] = String.valueOf(sumaElementosEstadoWPob(R, rgi, i));
            valores[i][8] = String.valueOf(sumaElementosEstadoWPob(D, rgi, i));
            // System.out.println("Sum D: " + sumaElementosEstado(D,rgi,i) + " * " +
            // this.getPobTotalRS_Region(rgi));
            valores[i][9] = String.valueOf(sumaElementosEstadoWPob(NR, rgi, i));
        }
        return valores;
    }

    public double sumaElementosEstado(double m[][][], int rgi, int n) {
        double suma = 0;
        for (int ss = 0; ss < this.estratos; ss++) {
            suma += m[rgi][ss][n];
        }
        return suma;
    }

    public double sumaElementosEstadoWPob(double m[][][], int rgi, int n) {
        double suma = 0;
        for (int ss = 0; ss < this.estratos; ss++) {
            suma += m[rgi][ss][n] * this.getPobTotalRS(rgi, ss);
        }
        return suma;
    }

    private double[][][] zeros(int n) {
        double[][][] vector = new double[this.regiones][this.estratos][n];
        for (int i = 0; i < this.regiones; i++) {
            for (int j = 0; j < this.estratos; j++) {
                for (int k = 0; k < n; k++) {
                    vector[i][j][k] = 0.0;
                }
            }
        }
        return vector;
    }

    public double[] llenaDias(int n) {
        double[] vector = new double[n];
        for (int i = 0; i < vector.length; i++) {
            vector[i] = i + 1;
        }
        return vector;
    }

    /**
     * @return the t
     */
    public double[] getT() {
        return t;
    }

    /**
     * @return the S
     */
    public double[][][] getS() {
        return S;
    }

    /**
     * @return the E
     */
    public double[][][] getE() {
        return E;
    }

    /**
     * @return the I0
     */
    public double[][][] getI0() {
        return I0;
    }

    /**
     * @return the I1
     */
    public double[][][] getI1() {
        return I1;
    }

    /**
     * @return the I2
     */
    public double[][][] getI2() {
        return I2;
    }

    /**
     * @return the I3
     */
    public double[][][] getI3() {
        return I3;
    }

    /**
     * @return the R
     */
    public double[][][] getR() {
        return R;
    }

    /**
     * @return the D
     */
    public double[][][] getD() {
        return D;
    }

    /**
     * @return the NR
     */
    public double[][][] getNR() {
        return NR;
    }

    /**
     * @return the Poblacion
     */
    public int getPobTotal() {
        return this.pobTotal;
    }

    public int getPobTotalRS(int rgi, int ssi) {
        return this.poblacion[rgi][ssi];
    }

    public int getPobTotalRS_Region(int rgi) {
        int valor = 0;
        for (int ss = 0; ss < this.estratos; ss++)
            valor += this.poblacion[rgi][ss];
        return valor;
    }

    /*
     * dt: 0.3 Beta: 0.3271875 miu: 0.005208333333333333 miu^N: 5.0E-5 psi: 0.0015
     * Gamma: 0.1
     */
    public double getDT() {
        return this.dt;
    }

    public double getBeta() {
        return this.beta0;
    }

    public double getMIU() {
        return this.miu;
    }

    public double getMIUN() {
        return this.miuN;
    }

    public double getPSI() {
        return this.psi;
    }

    public double getGamma() {
        return this.gamma0;
    }

    public double getSigma() {
        return this.sigma0;
    }

    public int getNRegiones() {
        return this.regiones;
    }

    public int getNEstratos() {
        return this.estratos;
    }

    public double getZeta() {
        return this.zeta;
    }

    public void setGamma(double gamma) {
        this.gamma0 = gamma;
    }

    public void setBeta(double beta) {
        this.beta0 = beta;
    }

    public void setMIU(double miu) {
        this.miu = miu;
    }

    public void setMIUN(double miun) {
        this.miuN = miun;
    }

    public void setPSI(double psi) {
        this.psi = psi;
    }

    public void setZeta(double zeta) {
        this.zeta = zeta;
    }

    public void setZetaR1(double zetaR1) {
        this.valorZR1 = zetaR1;
    }

    public double getZetaR1() {
        return this.valorZR1;
    }

    public void setZetaR2(double zetaR2) {
        this.valorZR2 = zetaR2;
    }

    public double getZetaR2() {
        return this.valorZR2;
    }

    public void setZetaR3(double zetaR3) {
        this.valorZR3 = zetaR3;
    }

    public double getZetaR3() {
        return this.valorZR3;
    }

    public void setSigma(double sigma) {
        this.sigma0 = sigma;
    }

    public double[][][] getPHI() {
        return this.PHI;
    }

    public double[][][] getphi() {
        return this.phi;
    }

    public int getNDias() {
        return this.dias;
    }

    public void setPHI(double[][][] vPHI) {
        this.PHI = vPHI;
    }

    public void setphi(double[][][] vphi) {
        this.phi = vphi;
    }

    public int getEscenario() {
        return this.escenario;
    }

    public void setEscenario(int escenario) {
        this.escenario = escenario;
    }

    /**
     * @return the gZeta
     */
    public double[] getgZeta() {
        return gZeta;
    }

    /**
     * @param gZeta the gZeta to set
     */
    public void setgZeta(double[] gZeta) {
        this.gZeta = gZeta;
    }

    /**
     * @return the gSigma
     */
    public double[] getgSigma() {
        return gSigma;
    }

    /**
     * @param gSigma the gSigma to set
     */
    public void setgSigma(double[] gSigma) {
        this.gSigma = gSigma;
    }

    /**
     * @return the miuSigma
     */
    public double getMiuSigma() {
        return miuSigma;
    }

    /**
     * @param miuSigma the miuSigma to set
     */
    public void setMiuSigma(double miuSigma) {
        this.miuSigma = miuSigma;
    }

    public void setQuarentinePercentage(double value) {
        this.A[0] = value;
    }
}