/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package domain;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

/**
 *
 * @author J. Alfredo Brambila Hdez.
 */
public class ModelSEIMRRS2 {
    //int pobTotal = 7413000;
    //CSVReader infoBD = new CSVReader();
    CSVDataGet infoBD = new CSVDataGet();
    //private EscenarioCuarentena eCuarentena;
    
    /*
     * LOG Consola
     */
    //boolean logCalculo = false;
    
    int pobTotal;
    
    double w = 1.0;
    private int dias = 365;
    double[] A = new double[2];
    double[] gamma = new double[4];
    double[] sigma = new double[4];
    
    String[][] regionesSTR;
    String[][] estratosSTR;
    String[][] segmentosSTR;
        
    private double[] EPFX;
    private double[] EPQX;
    private double[] BETAB;
    private double[] BETAQ;
    private double[] PCFX;
    private double[] PCQX;
    private double[] DELXB;
    private double[] PHIXB;
    private double[] ETAXB;
    
    private double AINF;
    private double AMAX;
    private double AMIN;
    private double DINF;
    private double DMAX;
    private double DMIN;
    private double FAMC;
    private double PAGK;
    private double PAGL;
    private double PAGQ;
    private double DELT;
    private double MIUN;
    private double PRTK;
    private double PRTL;
    private double PRTQ;
    private double KAPP;
    private double PINF;
    private double MIUUB;
    private double PCONB;
    private double PTRA;
    double[] PBIOGEN = new double[20];
    
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
    double[][] fpoblacion;
    
    double beta0;
    double dt;
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
    private MCuarentena cuarentenas;
    
    double gammaX;
    
    int escenario;
    
    int regiones = 0;
    int segmentos = 0;
    //int estratos = 0;
    int iEstados=0;

    double vZeta[][];
    double PHI[][][]; // fracc tiempo
    double phi[][][]; // traf gente entre regiones
    
    double FPRR[][][];
    double FTRR[][][];
  
    ArrayList<Cuarentena> cuarentList;
        
    public ModelSEIMRRS2() {
	cuarentenas = new MCuarentena(infoBD.getRuta());
	//cuarentList = cuarentenas.getCuarentenaList();
	cuarentList = cuarentenas.getCuarentenaListMes();
		
	pobTotal = infoBD.poblacionTotal();
	regiones = infoBD.getNumeroRegiones();
	segmentos = infoBD.getNumeroSegmentos();
	iEstados = infoBD.getNumeroiEstados();
	
	poblacion = new int[regiones][segmentos];
	fpoblacion = new double[regiones][segmentos];
	
	EPFX = new double[iEstados];
	EPQX = new double[iEstados];
	BETAB = new double[iEstados];
	BETAQ = new double[iEstados];
	PCFX = new double[iEstados];
	PCQX = new double[iEstados];
	DELXB = new double[iEstados];
	PHIXB = new double[iEstados];
	ETAXB = new double[iEstados];
	
	infoBD.cargaArraysParametros(EPFX,EPQX,BETAB,BETAQ,PCFX,PCQX,DELXB,PHIXB,ETAXB);
	//infoBD.cargaArraysParametrosPBIOGEN(AINF, AMAX, AMIN, DINF, DMAX, DMIN, FAMC, PAGK, PAGL, PAGQ, DELT, MIUN, PRTK, PRTL, PRTQ, KAPP, PINF, MIUUB, PCONB, PTRA);
	infoBD.cargaArraysParametrosPBIOGEN(PBIOGEN);
	setParamsPBIO_GEN();

	infoBD.getPoblacion(fpoblacion);
	
	regionesSTR = infoBD.getRegionesSTR();
	estratosSTR = infoBD.getEstratosSTR();
	segmentosSTR = infoBD.getSegmentosSTR();
	

        gamma[0] = ((double) 1)/10;//<-
        
        sigma[0] = ((double) 1)/4.1;
          
        PHI = new double[regiones][regiones][segmentos]; // fracc tiempo
        phi = new double[regiones][regiones][segmentos]; // traf gente entre regiones
	
	FPRR = new double[regiones][regiones][segmentos]; // fracc tiempo
	FTRR = new double[regiones][regiones][segmentos]; // fracc tiempo
	infoBD.getFPRR_FTRR(FPRR,FTRR);
        
	vZeta = new double[regiones][segmentos];
	
	//System.out.println("-------------> FPB: " + FPRR[0][0][0] + " " +FTRR[0][0][0]);
        // carga valores de los parámetros por default
        parametrosDefault();
        //condiciones iniciales: numero de poblacion, susceptibles, infectados, etc
        condicionesIniciales();
        // ejecuta simulacion del modelo (ecuaciones)
        simulaModelo();
	//escribeCSV_VVPOP();
	
    }
     
    //condiciones iniciales: numero de poblacion, susceptibles, infectados, etc
    public void condicionesIniciales() {
	
        // vectores de estados
        t = llenaDias(dias+1); // arreglo con los 365 dias del año
        S = zeros(dias+1); // zeros crea arreglo de tamaño n y lo llena con ceros
        E = zeros(dias+1);
        I0 = zeros(dias+1);
        I1 = zeros(dias+1);
        I2 = zeros(dias+1);
        I3 = zeros(dias+1);
        R = zeros(dias+1);
        D = zeros(dias+1);
        NR = zeros(dias+1);

	infoBD.obtenerPobRegionST(poblacion);
	
	// Cargado desde CSV OPTEX
        infoBD.getSU(S);	
	infoBD.getI0(I0);
	infoBD.getI1(I1);
	infoBD.getI2(I2);
	infoBD.getI3(I3);
	infoBD.getEX(E);
	infoBD.getED(D);
	infoBD.getND(NR);

    }
    
    public String rutaBDS() {
	return infoBD.getRuta();
    }
    
    public void setListadoCuarentena(ArrayList<Cuarentena> lista) {
	cuarentList = lista;
    }
    
    public void parametrosDefault() {
	
        // valores para cuarentena (solo se usa uno por el momento)
        A[0] = 0.0; //asintomáticos
        A[1] = 0.0; //sintomáticos
	
        escenario = 1;        

    }
           
    public void simulaModelo() {
        // regiones
	double BETABO0 = (1 - 0) * 0.3271875 + 0 * -2.98 * Math.log(1-0.01);
	double BETABO1 = (1 - 0) * -10 * Math.log(1 - 0.015) + 0 * -2.98 * Math.log(1 - 0.015);
        for(int rg=0; rg<this.regiones; rg++) {
            // segmentos
            for(int ss=0; ss<this.segmentos; ss++) {
                // t (dias)
                for(int n=0; n<this.dias; n++) {
                 		    
		    A[0] = valorAlpha(n, escenario, 0);
		    A[1] = valorAlpha(n, escenario,1);
		    
		    //System.out.println("Escenario: " + this.escenario);
		    if(this.escenario < 2) {
			this.BETAB[0] = (1 - A[0]) * 0.3271875 + A[0] * -2.98 * Math.log(1-0.01);
			this.BETAB[1] = (1 - A[1]) * -10 * Math.log(1 - 0.015) + A[1] * -2.98 * Math.log(1 - 0.015);
		    } else {
			
			this.BETAB[0] = (A[0] * this.BETAQ[0]) + BETABO0 - (A[0] * BETABO0);
			//System.out.println("n\t"+n+"\tA\t"+A[0]+"\tNueva Beta0:\t" + this.BETAB[0]);
			this.BETAB[1] = (A[1] * this.BETAQ[1]) + BETABO1 - (A[1] * BETABO1);
			//System.out.println("n\t"+n+"\tA\t"+A[1]+"\tNueva Beta1:\t" + this.BETAB[1]);
			
		    }
		    //}

                    /*
                     * Ecuaciones Modelo SEIMR-R-S
                     */
                    
                    
		    S[rg][ss][n+1] = (S[rg][ss][n] - S2I(rg,ss,n)) - (this.MIUN *S[rg][ss][n]);                    
                    
		    E[rg][ss][n+1] = (E[rg][ss][n] + S2I(rg,ss,n)) - (this.FHII() * E[rg][ss][n]);
		    
		    I0[rg][ss][n+1] = I0[rg][ss][n] + this.FHII() * E[rg][ss][n] - this.DSAL(0) * I0[rg][ss][n];
                    
		    I1[rg][ss][n+1] = I1[rg][ss][n] + this.DSZE(0) * I0[rg][ss][n] - this.DSAL(1) * I1[rg][ss][n];//*
		    
		    I2[rg][ss][n+1] = I2[rg][ss][n] + this.DSZE(1) * I1[rg][ss][n] - this.DSAL(2) * I2[rg][ss][n];//*
                    
		    I3[rg][ss][n+1] = I3[rg][ss][n] + this.DSZE(2) * I2[rg][ss][n] - this.DSAL(3) * I3[rg][ss][n];//*

		    R[rg][ss][n+1] = R[rg][ss][n] + (this.DSBE(0) * I0[rg][ss][n] + this.DSBE(1) * I1[rg][ss][n] + this.DSBE(2) * I2[rg][ss][n] + this.DSBE(3) * I3[rg][ss][n]) - this.MIUN * R[rg][ss][n];
		    //D[rg][ss][n+1] = D[rg][ss][n] + (this.MISS(1) * I1[rg][ss][n] + this.MISS(2) * I2[rg][ss][n] + this.MISS(3) * I3[rg][ss][n]);
		    D[rg][ss][n+1] = D[rg][ss][n] + (this.MISS(3) * I3[rg][ss][n]);
		    
		    NR[rg][ss][n+1] = NR[rg][ss][n] + this.MIUN * SR(rg,n) + this.MIUN * RR(rg,n);
		    
		    /*System.out.println("----------");
		    System.out.println("S["+n+"]: " + S[rg][ss][n]);
		    System.out.println("E["+n+"]: " + E[rg][ss][n]);
		    System.out.println("I0["+n+"]: " + I0[rg][ss][n]);
		    System.out.println("I1["+n+"]: " + I1[rg][ss][n]);
		    System.out.println("I2["+n+"]: " + I2[rg][ss][n]);
		    System.out.println("I3["+n+"]: " + I3[rg][ss][n]);
		    System.out.println("R["+n+"]: " + R[rg][ss][n]);
		    System.out.println("D["+n+"]: " + D[rg][ss][n]);
		    System.out.println("NR["+n+"]: " + NR[rg][ss][n]);*/
		    
                }
            }
        }
        
	/*System.out.println("Parámetros: ");
	System.out.println("MIUN:\t" + this.MIUN);
	System.out.println("MIUB:\t" + this.MIUUB);
	System.out.println("MISS:\t"+this.MISS(0)+"\t"+this.MISS(1)+"\t"+this.MISS(2)+"\t"+this.MISS(3)+"\t");
	System.out.println("DELTXB:\t"+this.DELXB[0]+"\t"+this.DELXB[1]+"\t"+this.DELXB[2]+"\t"+this.DELXB[3]+"");
	System.out.println("BETAB\t"+this.BETAB[0]+"\t"+this.BETAB[1]+"\t"+this.BETAB[2]+"\t"+this.BETAB[3]+"\t");
	System.out.println("BESS:\t"+this.BESS(0, 0));
	System.out.println("DSAL:\t"+this.DSAL(0)+"\t"+this.DSAL(1)+"\t"+this.DSAL(2)+"\t"+this.DSAL(3)+"\t");
	System.out.println("DSZE:\t"+this.DSZE(0)+"\t"+this.DSZE(1)+"\t"+this.DSZE(2)+"\t"+this.DSZE(3)+"\t");
	System.out.println("DEDE:\t"+this.DEDE(0)+"\t"+this.DEDE(1)+"\t"+this.DEDE(2)+"\t"+this.DEDE(3)+"\t");
	System.out.println("DEGX:\t"+this.DEGX(0)+"\t"+this.DEGX(1)+"\t"+this.DEGX(2)+"\t"+this.DEGX(3)+"\t");
	System.out.println("DERX:\t"+this.DERX(0)+"\t"+this.DERX(1)+"\t"+this.DERX(2)+"\t"+this.DERX(3)+"\t");
	System.out.println("DEI1:\t"+this.DEI1());
	System.out.println("DSBE:\t"+this.DSBE(0)+"\t"+this.DSBE(1)+"\t"+this.DSBE(2)+"\t"+this.DSBE(3)+"\t");
	System.out.println("DSBE1:\t"+this.DSBE1());
	System.out.println("GAMX:\t"+this.GAMX(0)+"\t"+this.GAMX(1)+"\t"+this.GAMX(2)+"\t"+this.GAMX(3)+"\t");
	System.out.println("ETHAXB:\t" + this.ETAXB[0]+"\t" + this.ETAXB[1]+"\t" + this.ETAXB[2]+"\t" + this.ETAXB[3]+"\t");
	System.out.println("PHIXB:\t"+this.PHIXB[0] + "\t"+this.PHIXB[1] + "\t"+this.PHIXB[2] + "\t"+this.PHIXB[3] + "\t");
	System.out.println("FHII:\t"+this.FHII());
	System.out.println("KAPP:\t"+this.KAPP);
	System.out.println("RHOX:\t"+this.RHOX(0)+"\t"+this.RHOX(1)+"\t"+this.RHOX(2)+"\t"+this.RHOX(3)+"\t");
	System.out.println("RODX:\t"+this.RODX(0)+"\t"+this.RODX(1)+"\t"+this.RODX(2)+"\t"+this.RODX(3)+"\t");*/
	
	escribeCSV_VVPOP();
	//System.out.println("");
    }
    
    public double valorAlpha(int n, int escenario, int sintomatico) {
	if(escenario < 2)
	    return 0.0;
	
	double valor = 0.0;
	for(Cuarentena c : cuarentList) {
	    if(c.enRangoM(n, sintomatico, escenario)) {
		valor = c.getPorcentaje();
		break;
	    }
	}
	return valor;
    }
    
    public String[] getRegionesNomb() {
	String[] nombres = new String[this.regionesSTR.length];
	for(int i=0; i<this.regionesSTR.length; i++) {
	    nombres[i] = this.regionesSTR[i][1];
	}
	return nombres;
    }
    
    public String[] getSegmentosNomb() {
	String[] nombres = new String[this.segmentosSTR.length];
	for(int i=0; i<this.segmentosSTR.length; i++) {
	    nombres[i] = this.segmentosSTR[i][1];
	}
	return nombres;
    }
    
    public void escribeResultadosComparativos() {
		
	double[] S_r = {0.99992702,0.999914743,0.999895165,0.999873992,0.999849043,0.999820265,0.999786902,0.999748273,0.999703536,0.99965173,0.999591738,0.999522269,0.999441826,0.999348679,0.999240824};
	double[] E_r = {2.70E-05,1.23E-05,1.96E-05,2.12E-05,2.49E-05,2.88E-05,3.34E-05,3.86E-05,4.47E-05,5.18E-05,6.00E-05,6.95E-05,8.04E-05,9.31E-05,1.08E-04};
	double[] I0_r = {3.04E-05,5.12E-05,5.32E-05,6.21E-05,7.08E-05,8.16E-05,9.40E-05,1.08E-04,1.25E-04,1.45E-04,1.68E-04,1.94E-04,2.25E-04,2.60E-04,3.01E-04};
	double[] I1_r = {1.55E-05,1.85E-05,2.47E-05,3.03E-05,3.67E-05,4.36E-05,5.14E-05,6.03E-05,7.04E-05,8.19E-05,9.52E-05,1.10E-04,1.28E-04,1.49E-04,1.72E-04};
	double[] I2_r = {1.35E-07,7.37E-07,1.38E-06,2.18E-06,3.09E-06,4.13E-06,5.31E-06,6.64E-06,8.15E-06,9.85E-06,1.18E-05,1.40E-05,1.65E-05,1.93E-05,2.26E-05};
	double[] I3_r = {0,6.42E-09,4.09E-08,1.02E-07,1.96E-07,3.23E-07,4.88E-07,6.92E-07,9.39E-07,1.23E-06,1.58E-06,1.98E-06,2.45E-06,2.99E-06,3.61E-06};
	double[] R_r = {0,2.47E-06,5.93E-06,1.01E-05,1.52E-05,2.13E-05,2.85E-05,3.69E-05,4.69E-05,5.84E-05,7.19E-05,8.76E-05,1.06E-04,1.27E-04,1.51E-04};
	double[] D_r = {0,0,3.21E-10,2.36E-09,7.48E-09,1.73E-08,3.34E-08,5.78E-08,9.24E-08,1.39E-07,2.01E-07,2.80E-07,3.79E-07,5.01E-07,6.51E-07};
	

	
	FileWriter archivo = null;
        PrintWriter pw = null;
	try {
	    archivo = new FileWriter("salida.csv");
            pw = new PrintWriter(archivo);
            
            pw.println("DIA,SU_R,SU,E_SU,EX_R,EX,E_EX,I0_R,I0,E_I0,I1_R,I1,E_I1,I2_R,I2,E_I2,I3_R,I3,E_I3,R_RE,RE,E_RE,ED_R,ED,E_ED,ECM");
	    for(int k=0; k<15; k++) {
		pw.print(k + "," + S_r[k] + "," + this.S[0][0][k] + "," +
			(Math.abs(S_r[k]-this.S[0][0][k])/S_r[k])+ "," + E_r[k] + "," + this.E[0][0][k]+ "," +
			(Math.abs(E_r[k]-this.E[0][0][k])/E_r[k]) + "," + I0_r[k] + "," + this.I0[0][0][k]+ "," +
			(Math.abs(I0_r[k]-this.I0[0][0][k])/I0_r[k]) + "," + I1_r[k] + "," + this.I1[0][0][k]+ "," +
			(Math.abs(I1_r[k]-this.I1[0][0][k])/I1_r[k]) + "," + I2_r[k] + "," + this.I2[0][0][k]+ "," +
			(Math.abs(I2_r[k]-this.I2[0][0][k])/I2_r[k]) + "," + I3_r[k] + "," + this.I3[0][0][k]+ "," +
			(I3_r[k] > 0 ? (Math.abs(I3_r[k]-this.I3[0][0][k])/I3_r[k]) : 0) + "," + R_r[k] + "," + this.R[0][0][k]+ "," +
			(R_r[k] > 0 ?(Math.abs(R_r[k]-this.R[0][0][k])/R_r[k]) : 0) + "," + D_r[k] + "," + this.D[0][0][k]+ "," +
			(D_r[k] > 0 ? (Math.abs(D_r[k]-this.D[0][0][k])/D_r[k]) : 0) +","+ 
			((Math.pow(S_r[k]-this.S[0][0][k],2)+Math.pow(E_r[k]-this.E[0][0][k],2)+Math.pow(I0_r[k]-this.I0[0][0][k],2)+Math.pow(I1_r[k]-this.I1[0][0][k],2)+Math.pow(I2_r[k]-this.I2[0][0][k],2)+Math.pow(I3_r[k]-this.I3[0][0][k],2)+Math.pow(R_r[k]-this.R[0][0][k],2)+Math.pow(D_r[k]-this.D[0][0][k],2))/8) +"\n");
		
	    }
	    archivo.close();          
        } catch(Exception e) {
            System.out.println("------------> Error" + e);
	    JOptionPane.showMessageDialog(null, "Error: " + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void escribeResultadosFR() {
	
	
	FileWriter archivo = null;
        PrintWriter pw = null;
	try {
	    archivo = new FileWriter("salidaTotFr.csv");
            pw = new PrintWriter(archivo);
            
            //pw.println("DIA,SU_R,SU,E_SU,EX_R,EX,E_EX,I0_R,I0,E_I0,I1_R,I1,E_I1,I2_R,I2,E_I2,I3_R,I3,E_I3,R_RE,RE,E_RE,ED_R,ED,E_ED,ECM");
	    pw.println("T,SU,EX,I0,I1,I2,I3,RE,ED");
	    for(int k=0; k<365; k++) {
		pw.print(k + "," + this.S[0][0][k] + ","  + this.E[0][0][k] + ","  + this.I0[0][0][k] + ","  + this.I1[0][0][k] + ","  + this.I2[0][0][k] + ","  + this.I3[0][0][k] + ","  + this.R[0][0][k] + ","  + this.D[0][0][k] + "\n" );		
	    }
	    archivo.close();          
        } catch(Exception e) {
            System.out.println("------------> Error" + e);
	    JOptionPane.showMessageDialog(null, "Error: " + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void escribeCSV_VVPOP() {
	FileWriter archivo = null;
        PrintWriter pw = null;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
	LocalDate date = LocalDate.parse("15/03/2020",formatter);
	
	try {
            archivo = new FileWriter("VV_POP.csv");
            pw = new PrintWriter(archivo);
            
            pw.println("FECHA,COD_STA,COD_UBT,COD_SDS,VALOR,COSTO_RED");
	    for(int k=0; k<this.dias; k++) {
		for(int i=0; i<this.regiones; i++) {
		    for(int j=0; j<this.segmentos; j++) {
			pw.print(date.toString()+",ED,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.D[i][j][k]+",0.01\n");
			pw.print(date.toString()+",EX,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.E[i][j][k]+",0.01\n");
			pw.print(date.toString()+",I0,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.I0[i][j][k]+",0.01\n");
			pw.print(date.toString()+",I1,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.I1[i][j][k]+",0.01\n");
			pw.print(date.toString()+",I2,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.I2[i][j][k]+",0.01\n");
			pw.print(date.toString()+",I3,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.I3[i][j][k]+",0.01\n");
			pw.print(date.toString()+",ND,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.NR[i][j][k]+",0.01\n");
			pw.print(date.toString()+",RE,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.R[i][j][k]+",0.01\n");
			pw.print(date.toString()+",SU,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.S[i][j][k]+",0.01\n");
			//System.out.println("i,j,k" + i + " " + j + " " + k + " r,s " + this.regiones + " " + this.segmentos);
		    }
		    //pw.print("\n");
		}
		
		date = date.plusDays(1);
		//pw.print("\n");
	    }
         archivo.close();          
        } catch(Exception e) {
            System.out.println("------------> Error" + e);
	    JOptionPane.showMessageDialog(null, "Error: " + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
		
    }
    
    public void escribeCSV_VVPOP2() {
	FileWriter archivo = null;
        PrintWriter pw = null;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
	LocalDate date = LocalDate.parse("24/03/2020",formatter);
	
	try {
            archivo = new FileWriter("VV_POP.csv");
            pw = new PrintWriter(archivo);
            
            pw.println("FECHA,COD_STA,COD_UBT,COD_SDS,VALOR,COSTO_RED");
	    for(int k=0; k<this.dias; k++) {
		for(int i=0; i<this.regiones; i++) {
		    for(int j=0; j<this.segmentos; j++) {
			pw.print(date.toString()+",SU,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.S[i][j][k]+",0.01\n");
			pw.print(date.toString()+",EX,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.E[i][j][k]+",0.01\n");
			pw.print(date.toString()+",I0,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.I0[i][j][k]+",0.01\n");
			pw.print(date.toString()+",I1,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.I1[i][j][k]+",0.01\n");
			pw.print(date.toString()+",I2,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.I2[i][j][k]+",0.01\n");
			pw.print(date.toString()+",I3,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.I3[i][j][k]+",0.01\n");
			pw.print(date.toString()+",RE,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.R[i][j][k]+",0.01\n");
			pw.print(date.toString()+",ED,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.D[i][j][k]+",0.01\n");
			pw.print(date.toString()+",ND,"+this.regionesSTR[i][0]+","+this.segmentosSTR[j][0]+","+this.NR[i][j][k]+",0.01\n");
			//System.out.println("i,j,k" + i + " " + j + " " + k + " r,s " + this.regiones + " " + this.segmentos);
		    }
		    //pw.print("\n");
		}
		
		date = date.plusDays(1);
		//pw.print("\n");
	    }
         archivo.close();          
        } catch(Exception e) {
            System.out.println("------------> Error" + e);
	    JOptionPane.showMessageDialog(null, "Error: " + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
		
    }
    
    private void setParamsPBIO_GEN() {
	AINF = PBIOGEN[0];
	AMAX = PBIOGEN[1];
	AMIN = PBIOGEN[2];
	DINF = PBIOGEN[3];
	DMAX = PBIOGEN[4];
	DMIN = PBIOGEN[5];
	FAMC = PBIOGEN[6];
	PAGK = PBIOGEN[7];
	PAGL = PBIOGEN[8];
	PAGQ = PBIOGEN[9];
	DELT = PBIOGEN[10];
	MIUN = PBIOGEN[11];
	PRTK = PBIOGEN[12];
	PRTL = PBIOGEN[13];
	PRTQ = PBIOGEN[14];
	KAPP = PBIOGEN[15];
	PINF = PBIOGEN[16];
	MIUUB = PBIOGEN[17];
	PCONB = PBIOGEN[18];
	PTRA = PBIOGEN[19];
    }
    
    public int getIndiceRegionCOD(String cod) {
	int indice = -1;
	for(int i=0; i<this.regionesSTR.length; i++) {
	    if(this.regionesSTR[i][0].equals(cod)) {
		indice = i;
		break;
	    }
	}
	return indice;
    }
    
    public int getIndiceEstratoCOD(String cod) {
	int indice = -1;
	for(int i=0; i<this.estratosSTR.length; i++) {
	    if(this.estratosSTR[i][0].equals(cod)) {
		indice = i;
		break;
	    }
	}
	return indice;
    }
    
    public int getIndiceSegmentoCOD(String cod) {
	int indice = -1;
	for(int i=0; i<this.segmentosSTR.length; i++) {
	    if(this.segmentosSTR[i][0].equals(cod)) {
		indice = i;
		break;
	    }
	}
	return indice;
    }
    
    //parámetros calculados
    public double LAME(int rg, int ss) {
	return this.infoBD.LAME(this.regionesSTR[rg][0], this.segmentosSTR[ss][0]);
    }
    
    public double LAMS(int rg, int ss) {
	return this.infoBD.LAMS(this.regionesSTR[rg][0], this.segmentosSTR[ss][0]);
    }
    
    public double LAMI(int rg, int ss) {
	return this.infoBD.LAMI(this.regionesSTR[rg][0], this.segmentosSTR[ss][0]);
    }
    
    public double LAMR(int rg, int ss) {
	return this.infoBD.LAMR(this.regionesSTR[rg][0], this.segmentosSTR[ss][0]);
    }
    //
    /*public double FPRR(int rgo, int rgd, int ss) {
	System.out.println(rgo + " -- " + rgd + " -- " + ss);
	System.out.println(this.regionesSTR[rgo][0] + " -- " + this.regionesSTR[rgd][0] + " -- " + this.segmentosSTR[rgo][0]);
	return this.infoBD.FPRR(this.regionesSTR[rgo][0], this.regionesSTR[rgd][0], this.segmentosSTR[ss][0]);
    }
    
    public double FTRR(int rgo, int rgd, int ss) {
	return this.infoBD.FTRR(this.regionesSTR[rgo][0], this.regionesSTR[rgd][0], this.segmentosSTR[ss][0]);
    }*/
    
    //PTRRrg,rd,ss
    public double PTRR(int rgo, int rgd, int ss) {
	return FPRR[rgo][rgd][ss] * FTRR[rgo][rgd][ss];
    }
    
    public double FHII() {
	//FHII =  (1/KAPP)
	return 1/KAPP;
    }
    
    public double DEI1() {
	//DEI1 =  (1/DELT)
	return 1/DELT;
    }
    
    public double BETA() {
	return DEI1() * PTRA;
    }
    
    public double GAMX(int st) {
	//GAMXag,st =  (1/PHIXBst)
	return 1/PHIXB[st];
    }
    
    public double RHOX(int st) {
	return 1/ETAXB[st];
    }
    
    public double DEGX(int st) {
	//DEGXag,st =  DELXBst × GAMXag,st
	return DELXB[st] * GAMX(st);
    }
    
    public double RODX(int st) {
	return DELXB[st] * RHOX(st);
    }
    
    public double DERX(int st) {
	//DERXag,st =  RHOXag,st - RODXag,st
	//return (1-RHOX(st)) * RODX(st);
	//return RHOX(st) * RODX(st);
	return RHOX(st) - RODX(st);
    }
    
    public double DEDE(int st) {
	//DEDEag,st =  DEGXag,st × DERXag,st
	return DEGX(st) + DERX(st);
    }
    
    public double BESS(int rg, int ss) {
	//return DEDE(0) * PCONB;
	return this.DEI1() * this.PCONB;
    }
    
    public double DSAL(int st) {
	//DSALst,ss =  ∑agAGS(ss) DEDEag,st
	//return (1 - DELXB[st]) * this.RHOX(st);
	return DEDE(st); //
    }
    
    public double DSZE(int st) {
	return DERX(st);//
    }
    
    public double DSBE(int st) {
	//DSBEst,ss =  ∑agAGS(ss) DEGXag,st
	return DEGX(st); //
    }
    
    public double DSBE1() {
	// DSBE1s1,ss =  ∑stSTST1(s1) DSBEst,ss
	return DSBE(1);
    }
    
    public double MISS(int st) {
	//MISSss =  ∑agAGS(ss) MIUUB
	return MIUUB;
    }
    
    public double PCONB() {
	//PCONB =  (1/KAPP)
	return 1/KAPP;
    }
    
    
    //***
    
    //Parametros calculados
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
	
        return this.BETAB[0]*this.I0[rg][ss][n] + this.BETAB[1]*this.I1[rg][ss][n] + this.BETAB[2]*this.I2[rg][ss][n] + this.BETAB[3]*this.I3[rg][ss][n];
	
	//return this.I0[rg][ss][n] + this.I1[rg][ss][n] + this.I2[rg][ss][n] + this.I3[rg][ss][n];
    }
    
    //*****************************
    public double II(int rg,int n) {
        double valor=0.0;
        LinkedList<Integer> regionesLista;
        regionesLista = ROR(rg);
        //for(int ro=0; ro<regionesLista.size(); ro++) {
        for(int ro : regionesLista) {
            for(int ss=0; ss<this.segmentos; ss++) {
                //valor += SSR(ro)*phi[ro][rg][ss]*PHI[ro][rg][ss]*IS(ro,ss,n);
		//valor += SSR(ro)*this.FPRR[ro][rg][ss]*this.FTRR[ro][rg][ss]*IS(ro,ss,n);
		valor += this.FPRR[ro][rg][ss]*this.FTRR[ro][rg][ss]*IS(ro,ss,n);
            }
        }
	//System.out.println(">> II: " + valor);
        return valor;
    }
           
    public double IE(int rg,int n) {
        double valor=0.0;
        LinkedList<Integer> regionesLista;
        regionesLista = RDE(rg);
        //for(int rd=0; rd<regionesLista.size(); rd++) {
        for(int rd : regionesLista) {
            for(int ss=0; ss<this.segmentos; ss++) {
                //valor += SSR(rd)*phi[rg][rd][ss]*PHI[rg][rd][ss]*IS(rd,ss,n);
		//valor += SSR(rd)*FPRR[rg][rd][ss]*FTRR[rg][rd][ss]*IS(rd,ss,n);
		valor += (FPRR[rg][rd][ss]*FTRR[rg][rd][ss]*IS(rd,ss,n));
            }
        }
	//System.out.println(">> IE: " + valor);
        return valor;
    }
    
    public double IX(int rg,int n) {
        double valor=0.0;
        for(int ss=0; ss<this.segmentos; ss++) {
            valor += IS(rg,ss,n);
        }
	//System.out.println(">> IX: " + valor);
        return valor;
    }
    
    
    
    public double SE(int rg, int rd, int ss, int n) {
        double valor;
        //valor = phi[rg][rd][ss] * PHI[rg][rd][ss] * S[rd][ss][n];
	//System.out.println("-->SE " + FPRR[rg][rd][ss] +" " + FTRR[rg][rd][ss] +" "+ S[rd][ss][n]);
	
	valor = FPRR[rg][rd][ss] * FTRR[rg][rd][ss] * S[rd][ss][n];
	
	
        return valor;
    }
    
    public double IR(int rg,int n) {
	
        return IX(rg,n) + II(rg,n) - IE(rg,n);
    }
    
    public double SN(int rg, int ss, int n) {
        double valor=0.0;
        double sumaSE=0.0;
        LinkedList<Integer> rdLista;
        rdLista = RDE(rg);
        //for(int ird=0; ird<rdLista.size(); ird++) {
        for(int ird : rdLista) {
            for(int iss=0; iss<this.segmentos; iss++) {
                sumaSE += SE(rg,ird,iss,n);
            }
        }
        
        valor = S[rg][ss][n] - sumaSE;
	
        return valor;
    }
    
    public double SIN(int rg, int ss, int n) {
        double valor;
	
        valor = this.BESS(rg, ss) * IR(rg,n) * SN(rg,ss,n);
	
        return valor;
    }
    
    public double SIE(int rg,int ss,int n) {
        double valor=0.0;
        LinkedList<Integer> rdLista;
        rdLista = RDE(rg);
	
        for(int ird : rdLista) {
                valor += this.BESS(rg, ss) * IR(ird,n) * SE(rg,ird,ss,n);
        }
        return valor;
    }
    
    public double S2I(int rg, int ss, int n) {
        double valor=0.0;
	
	valor = SIN(rg,ss,n) + SIE(rg,ss,n);
	
        return valor;
    }
    
    public double RR(int rg, int n) {
        double valor=0.0;
        LinkedList<Integer> regionesLista;
        regionesLista = RDE(rg);
        //for(int irg=0; irg<regionesLista.size(); irg++) {
        for(int irg : regionesLista) {
            for(int ss=0; ss<this.segmentos; ss++) {
                valor += SSR(irg)*R[rg][ss][n];
            }
        }
        return valor;
    }
    
    public double DR(int rg, int n) {
        double valor=0.0;
        LinkedList<Integer> regionesLista;
        regionesLista = RDE(rg);
        //for(int irg=0; irg<regionesLista.size(); irg++) {
        for(int irg : regionesLista) {
            for(int ss=0; ss<this.segmentos; ss++) {
                valor += SSR(irg)*D[rg][ss][n];
            }
        }
        return valor;
    }
    
    public double SR(int rg, int n) {
        double valor=0.0;
        LinkedList<Integer> regionesLista;
        regionesLista = RDE(rg);
        //for(int irg=0; irg<regionesLista.size(); irg++) {
        for(int irg : regionesLista) {
            for(int ss=0; ss<this.segmentos; ss++) {
                //valor += S[irg][ss][n];
		valor += SSR(irg)*S[irg][ss][n];
            }
        }
        return valor;
    }
    
    /*public double SSR(int rg) {
        double npersonas=0;
        for(int ss=0; ss<this.segmentos; ss++) {
            npersonas += this.poblacion[rg][ss];
        }
        return (double)(npersonas/this.pobTotal); //
        //return (double)(npersonas/this.pobTotal); //
    }*/
    
     public double SSR(int rg) {
        double npersonas=0;
        for(int ss=0; ss<this.segmentos; ss++) {
            npersonas += this.fpoblacion[rg][ss];
        }
	 //System.out.println("-------------------------> EPob: " + npersonas);
        return npersonas; 
        //return (double)(npersonas/this.pobTotal); //
    }
    
    
    public LinkedList<Integer> ROR(int rg) {
        LinkedList<Integer> lista = new LinkedList<Integer>();
        for(int i=0; i<this.regiones; i++) {
            if(rg != i) {
                lista.add(i);
            }
        }
        return lista;
    }
    
    public LinkedList<Integer> RDE(int rg) {
        LinkedList<Integer> lista = new LinkedList<Integer>();
        for(int i=0; i<this.regiones; i++) {
            if(rg != i) {
                lista.add(i);
            }
        }
        return lista;
    }
    
    public String[][] getValoresSEIRMRRS(int rgi, int ssi) {
        String[][] valores = new String[this.dias+1][10];
        for(int i=0; i<dias; i++) {
            valores[i][0] = String.valueOf(t[i]);
            //valores[i][1] = String.valueOf(S[rgi][ssi][i]*poblacion[rgi][ssi]);
	    //System.out.println("S: " + S[rgi][ssi][i]+" * "+this.getPobTotalRS(rgi, ssi) + " = " + (S[rgi][ssi][i]*this.getPobTotalRS(rgi, ssi)));
            valores[i][1] = String.valueOf(S[rgi][ssi][i]*this.getPobTotalRS(rgi, ssi));
            valores[i][2] = String.valueOf(E[rgi][ssi][i]*this.getPobTotalRS(rgi, ssi));
            valores[i][3] = String.valueOf(I0[rgi][ssi][i]*this.getPobTotalRS(rgi, ssi));
            valores[i][4] = String.valueOf(I1[rgi][ssi][i]*this.getPobTotalRS(rgi, ssi));
            valores[i][5] = String.valueOf(I2[rgi][ssi][i]*this.getPobTotalRS(rgi, ssi));
            valores[i][6] = String.valueOf(I3[rgi][ssi][i]*this.getPobTotalRS(rgi, ssi));
            valores[i][7] = String.valueOf(R[rgi][ssi][i]*this.getPobTotalRS(rgi, ssi));
            valores[i][8] = String.valueOf(D[rgi][ssi][i]*this.getPobTotalRS(rgi, ssi));
            valores[i][9] = String.valueOf(NR[rgi][ssi][i]*this.getPobTotalRS(rgi, ssi));
        }
        return valores;
    }
    
    public String[][] getValoresSEIRMRRS_Region(int rgi) {
        String[][] valores = new String[this.dias+1][10];
        for(int i=0; i<dias; i++) {
            valores[i][0] = String.valueOf(t[i]);
            //valores[i][1] = String.valueOf(S[rgi][ssi][i]*poblacion[rgi][ssi]);
	    //System.out.println("--- SUS -----");
            valores[i][1] = String.valueOf(sumaElementosEstadoWPob(S,rgi,i));
            valores[i][2] = String.valueOf(sumaElementosEstadoWPob(E,rgi,i));
            valores[i][3] = String.valueOf(sumaElementosEstadoWPob(I0,rgi,i));
            valores[i][4] = String.valueOf(sumaElementosEstadoWPob(I1,rgi,i));
            valores[i][5] = String.valueOf(sumaElementosEstadoWPob(I2,rgi,i));
            valores[i][6] = String.valueOf(sumaElementosEstadoWPob(I3,rgi,i));
            valores[i][7] = String.valueOf(sumaElementosEstadoWPob(R,rgi,i));
            valores[i][8] = String.valueOf(sumaElementosEstadoWPob(D,rgi,i));
            //System.out.println("Sum D: " + sumaElementosEstado(D,rgi,i) + " * " + this.getPobTotalRS_Region(rgi));
            valores[i][9] = String.valueOf(sumaElementosEstadoWPob(NR,rgi,i));
        }
        return valores;
    }
    
    public String[][] getValoresSEIRMRRS_RegionAgregado() {
        String[][] valores = new String[this.dias+1][10];
        for(int i=0; i<dias; i++) {
            valores[i][0] = String.valueOf(t[i]);
            //valores[i][1] = String.valueOf(S[rgi][ssi][i]*poblacion[rgi][ssi]);
	    //System.out.println("--- SUS -----");
            valores[i][1] = String.valueOf(sumaElementosEstadoWPobTodo(S,i));
            valores[i][2] = String.valueOf(sumaElementosEstadoWPobTodo(E,i));
            valores[i][3] = String.valueOf(sumaElementosEstadoWPobTodo(I0,i));
            valores[i][4] = String.valueOf(sumaElementosEstadoWPobTodo(I1,i));
            valores[i][5] = String.valueOf(sumaElementosEstadoWPobTodo(I2,i));
            valores[i][6] = String.valueOf(sumaElementosEstadoWPobTodo(I3,i));
            valores[i][7] = String.valueOf(sumaElementosEstadoWPobTodo(R,i));
            valores[i][8] = String.valueOf(sumaElementosEstadoWPobTodo(D,i));
            //System.out.println("Sum D: " + sumaElementosEstado(D,rgi,i) + " * " + this.getPobTotalRS_Region(rgi));
            valores[i][9] = String.valueOf(sumaElementosEstadoWPobTodo(NR,i));
        }
        return valores;
    }
    
    public double sumaElementosEstado(double m[][][], int rgi, int n) {
        double suma = 0;
        for(int ss=0; ss<this.segmentos; ss++) {
            suma += m[rgi][ss][n];
        }
        return suma;
    }
    
    public double sumaElementosEstadoWPob(double m[][][], int rgi, int n) {
        double suma = 0;
        for(int ss=0; ss<this.segmentos; ss++) {
            suma += m[rgi][ss][n] * this.getPobTotalRS(rgi, ss) ;
        }
        return suma;
    }
    
    /*public void prueba() {
	System.out.println("Prueba...");	
	ArrayList<Cuarentena> cList = new ArrayList<Cuarentena>();
	cList.add(new Cuarentena(1,0,30,0.8,0));
	cList.add(new Cuarentena(1,31,60,0.8,0));
	cList.add(new Cuarentena(1,61,91,0.8,0));
	cList.add(new Cuarentena(1,92,121,0.8,0));
	cList.add(new Cuarentena(1,122,152,0.8,0));
	cList.add(new Cuarentena(1,153,183,0.8,0));
	cList.add(new Cuarentena(1,184,213,0.8,0));
	cList.add(new Cuarentena(1,214,244,0.8,0));
	cList.add(new Cuarentena(1,245,274,0.8,0));
	cList.add(new Cuarentena(1,275,305,0.8,0));
	cList.add(new Cuarentena(1,306,336,0.8,0));
	cList.add(new Cuarentena(1,337,364,0.8,0));

	cList.add(new Cuarentena(1,0,30,0.8,1));
	cList.add(new Cuarentena(1,31,60,0.8,1));
	cList.add(new Cuarentena(1,61,91,0.8,1));
	cList.add(new Cuarentena(1,92,121,0.8,1));
	cList.add(new Cuarentena(1,122,152,0.8,1));
	cList.add(new Cuarentena(1,153,183,0.8,1));
	cList.add(new Cuarentena(1,184,213,0.8,1));
	cList.add(new Cuarentena(1,214,244,0.8,1));
	cList.add(new Cuarentena(1,245,274,0.8,1));
	cList.add(new Cuarentena(1,275,305,0.8,1));
	cList.add(new Cuarentena(1,306,336,0.8,1));
	cList.add(new Cuarentena(1,337,364,0.8,1));
	
	this.maxFallecimientosRegionMes(cList);
    }*/
    
    public void maxFallecimientosRegionMes(ArrayList<Cuarentena> lista) {
	this.escenario=2;
	this.cuarentList = lista;
	this.simulaModelo();
	int mmes = this.cuarentList.size()/2;
	
	double valor=0.0;
	double max=0.0;
	double[] maximosRegion = new double[this.regiones];
	for(int i=0; i<regiones; i++) { maximosRegion[i]=0.0; }
	for(int imes=0; imes<this.cuarentList.size()/2; imes++) {
	    max=0.0;
	    for(int idia=this.cuarentList.get(imes).getDiaInicio(); idia<this.cuarentList.get(imes).getDiaFin(); idia++) {
		for(int reg=0; reg<this.regiones; reg++) {
		    valor = sumaElementosEstadoWPob(this.D,reg,idia);
		    //max = valor > max ? valor : max;
		    maximosRegion[reg] = valor > maximosRegion[reg] ? valor : maximosRegion[reg];
		}
		
	    }
	    
	    for(int i=0; i<regiones; i++) { 
		max += maximosRegion[i]; 
	    }
	    
	    cuarentList.get(imes).setMaxFallecidos(max);
	    cuarentList.get(imes+mmes).setMaxFallecidos(max);
	}
    }
    
        
    public double sumaElementosEstadoWPobTodo(double m[][][], int n) {
        double suma = 0;
	for(int rgi=0; rgi<this.regiones; rgi++) {
	    for(int ss=0; ss<this.segmentos; ss++) {
		suma += m[rgi][ss][n] * this.getPobTotalRS(rgi, ss) ;
	    }
	}
	
        return suma;
    }
       
    
    private double[][][] zeros(int n) {
        double[][][] vector = new double[this.regiones][this.segmentos][n];
        for(int i=0; i<this.regiones; i++) {
            for(int j=0; j<this.segmentos; j++) {
                for(int k=0; k<n; k++) {
                    vector[i][j][k]=0.0;
                }
            }
        }
        return vector;
    }
    
    public double[] llenaDias(int n) {
        double[] vector = new double[n];
        for(int i=0; i<vector.length; i++) {
            vector[i]=i+1;
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
	//return this.pobTotal;
	//System.out.println("POB: " + this.poblacion[rgi][ssi]);
        return this.poblacion[rgi][ssi];
    }
    
    public int getPobTotalRS_Region(int rgi) {
        int valor = 0;
        for(int ss=0; ss<this.segmentos; ss++)
            valor += this.poblacion[rgi][ss];
        return valor;
    }
    
    
    /*
    dt: 0.3
    Beta: 0.3271875
    miu: 0.005208333333333333
    miu^N: 5.0E-5
    psi: 0.0015
    Gamma: 0.1
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
    
    /*public double getMIUN() {
        return this.miuN;
    }*/
    
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
        return this.segmentos;
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
    
    /*public void setMIUN(double miun) {
        this.miuN = miun;
    }*/
    
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
    
    public void setPHIfm(TableModel mtx) {
        int vro = 0;
        int vrd = 0;
        double value = 0.0;
        for(int i=0; i<this.segmentos; i++) {
            vro = Integer.parseInt(mtx.getValueAt(i, 0).toString());
            vrd = Integer.parseInt(mtx.getValueAt(i, 1).toString());
            value = Double.valueOf(mtx.getValueAt(i, 3).toString());
            this.PHI[vro-1][vrd-1][i] = value;
        }
    }
    
    public void setphifm(TableModel mtx) {
        int vro = 0;
        int vrd = 0;
        double value = 0.0;
        for(int i=0; i<this.segmentos; i++) {
            vro = Integer.parseInt(mtx.getValueAt(i, 0).toString());
            vrd = Integer.parseInt(mtx.getValueAt(i, 1).toString());
            value = Double.valueOf(mtx.getValueAt(i, 3).toString());
            this.phi[vro-1][vrd-1][i] = value;
        }
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
        //System.out.println(gSigma[0] + " " +gSigma[1] + " " +gSigma[2] + " " +gSigma[3]);
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

    /**
     * @return the EPFX
     */
    public double[] getEPFX() {
	return EPFX;
    }

    /**
     * @param EPFX the EPFX to set
     */
    public void setEPFX(double[] EPFX) {
	this.EPFX = EPFX;
    }

    /**
     * @return the EPQX
     */
    public double[] getEPQX() {
	return EPQX;
    }

    /**
     * @param EPQX the EPQX to set
     */
    public void setEPQX(double[] EPQX) {
	this.EPQX = EPQX;
    }

    /**
     * @return the BETAB
     */
    public double[] getBETAB() {
	return BETAB;
    }

    /**
     * @param BETAB the BETAB to set
     */
    public void setBETAB(double[] BETAB) {
	this.BETAB = BETAB;
    }

    /**
     * @return the BETAQ
     */
    public double[] getBETAQ() {
	return BETAQ;
    }

    /**
     * @param BETAQ the BETAQ to set
     */
    public void setBETAQ(double[] BETAQ) {
	this.BETAQ = BETAQ;
    }

    /**
     * @return the PCFX
     */
    public double[] getPCFX() {
	return PCFX;
    }

    /**
     * @param PCFX the PCFX to set
     */
    public void setPCFX(double[] PCFX) {
	this.PCFX = PCFX;
    }

    /**
     * @return the PCQX
     */
    public double[] getPCQX() {
	return PCQX;
    }

    /**
     * @param PCQX the PCQX to set
     */
    public void setPCQX(double[] PCQX) {
	this.PCQX = PCQX;
    }

    /**
     * @return the DELXB
     */
    public double[] getDELXB() {
	return DELXB;
    }

    /**
     * @param DELXB the DELXB to set
     */
    public void setDELXB(double[] DELXB) {
	this.DELXB = DELXB;
    }

    /**
     * @return the PHIXB
     */
    public double[] getPHIXB() {
	return PHIXB;
    }

    /**
     * @param PHIXB the PHIXB to set
     */
    public void setPHIXB(double[] PHIXB) {
	this.PHIXB = PHIXB;
    }

    /**
     * @return the ETAXB
     */
    public double[] getETAXB() {
	return ETAXB;
    }

    /**
     * @param ETAXB the ETAXB to set
     */
    public void setETAXB(double[] ETAXB) {
	this.ETAXB = ETAXB;
    }

    /**
     * @return the AINF
     */
    public double getAINF() {
	return AINF;
    }

    /**
     * @param AINF the AINF to set
     */
    public void setAINF(double AINF) {
	this.AINF = AINF;
    }

    /**
     * @return the AMAX
     */
    public double getAMAX() {
	return AMAX;
    }

    /**
     * @param AMAX the AMAX to set
     */
    public void setAMAX(double AMAX) {
	this.AMAX = AMAX;
    }

    /**
     * @return the AMIN
     */
    public double getAMIN() {
	return AMIN;
    }

    /**
     * @param AMIN the AMIN to set
     */
    public void setAMIN(double AMIN) {
	this.AMIN = AMIN;
    }

    /**
     * @return the DINF
     */
    public double getDINF() {
	return DINF;
    }

    /**
     * @param DINF the DINF to set
     */
    public void setDINF(double DINF) {
	this.DINF = DINF;
    }

    /**
     * @return the DMAX
     */
    public double getDMAX() {
	return DMAX;
    }

    /**
     * @param DMAX the DMAX to set
     */
    public void setDMAX(double DMAX) {
	this.DMAX = DMAX;
    }

    /**
     * @return the DMIN
     */
    public double getDMIN() {
	return DMIN;
    }

    /**
     * @param DMIN the DMIN to set
     */
    public void setDMIN(double DMIN) {
	this.DMIN = DMIN;
    }

    /**
     * @return the FAMC
     */
    public double getFAMC() {
	return FAMC;
    }

    /**
     * @param FAMC the FAMC to set
     */
    public void setFAMC(double FAMC) {
	this.FAMC = FAMC;
    }

    /**
     * @return the PAGK
     */
    public double getPAGK() {
	return PAGK;
    }

    /**
     * @param PAGK the PAGK to set
     */
    public void setPAGK(double PAGK) {
	this.PAGK = PAGK;
    }

    /**
     * @return the PAGL
     */
    public double getPAGL() {
	return PAGL;
    }

    /**
     * @param PAGL the PAGL to set
     */
    public void setPAGL(double PAGL) {
	this.PAGL = PAGL;
    }

    /**
     * @return the PAGQ
     */
    public double getPAGQ() {
	return PAGQ;
    }

    /**
     * @param PAGQ the PAGQ to set
     */
    public void setPAGQ(double PAGQ) {
	this.PAGQ = PAGQ;
    }

    /**
     * @return the DELT
     */
    public double getDELT() {
	return DELT;
    }

    /**
     * @param DELT the DELT to set
     */
    public void setDELT(double DELT) {
	this.DELT = DELT;
    }

    /**
     * @return the PRTK
     */
    public double getPRTK() {
	return PRTK;
    }

    /**
     * @param PRTK the PRTK to set
     */
    public void setPRTK(double PRTK) {
	this.PRTK = PRTK;
    }

    /**
     * @return the PRTL
     */
    public double getPRTL() {
	return PRTL;
    }

    /**
     * @param PRTL the PRTL to set
     */
    public void setPRTL(double PRTL) {
	this.PRTL = PRTL;
    }

    /**
     * @return the PRTQ
     */
    public double getPRTQ() {
	return PRTQ;
    }

    /**
     * @param PRTQ the PRTQ to set
     */
    public void setPRTQ(double PRTQ) {
	this.PRTQ = PRTQ;
    }

    /**
     * @return the KAPP
     */
    public double getKAPP() {
	return KAPP;
    }

    /**
     * @param KAPP the KAPP to set
     */
    public void setKAPP(double KAPP) {
	this.KAPP = KAPP;
    }

    /**
     * @return the PINF
     */
    public double getPINF() {
	return PINF;
    }

    /**
     * @param PINF the PINF to set
     */
    public void setPINF(double PINF) {
	this.PINF = PINF;
    }

    /**
     * @return the MIUUB
     */
    public double getMIUUB() {
	return MIUUB;
    }

    /**
     * @param MIUUB the MIUUB to set
     */
    public void setMIUUB(double MIUUB) {
	this.MIUUB = MIUUB;
    }

    /**
     * @return the PCONB
     */
    public double getPCONB() {
	return PCONB;
    }

    /**
     * @param PCONB the PCONB to set
     */
    public void setPCONB(double PCONB) {
	this.PCONB = PCONB;
    }

    /**
     * @return the PTRA
     */
    public double getPTRA() {
	return PTRA;
    }

    /**
     * @param PTRA the PTRA to set
     */
    public void setPTRA(double PTRA) {
	this.PTRA = PTRA;
    }

    /**
     * @return the MIUN
     */
    public double getMIUN() {
	return MIUN;
    }

    /**
     * @param MIUN the MIUN to set
     */
    public void setMIUN(double MIUN) {
	this.MIUN = MIUN;
    }

    public ArrayList<Cuarentena> getCuarentList() {
        return cuarentList;
    }

    /**
     * @return the dias
     */
    public int getDias() {
	return dias;
    }

    /**
     * @param dias the dias to set
     */
    public void setDias(int dias) {
	this.dias = dias;
    }

    
}

