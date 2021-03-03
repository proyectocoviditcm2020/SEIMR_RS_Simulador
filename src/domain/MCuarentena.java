/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package domain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import support.ConexionDB;

/**
 *
 * @author J. Alfredo Brambila Hdez.
 */
public class MCuarentena {
    ArrayList<Cuarentena> cuarentenaList = new ArrayList<Cuarentena>();
    int nEscenarios;
    private String ruta = "";
    public MCuarentena(String ruta) {
	this.ruta = ruta;
    }
    
    public int getNEscenarios() {
	int resultado = 0;
	try {
	    ConexionDB conDB = new ConexionDB();
	    Connection conn = conDB.getConexion(ruta);
	    Statement stmt = conn.createStatement();
	    ResultSet resultSet = stmt.executeQuery("SELECT COUNT(DISTINCT COD_EQUA) FROM MAE_QUA");
	    
	    if(resultSet.next()) {
		resultado = resultSet.getInt(1);
	    }
	    conn.close();
	} catch(Exception e) {
	    System.out.println("getNEscenarios Error:  " + e);
	}
	return resultado;
    }
    
    public String[] getEscenariosSTR() {
	int resultado = getNEscenarios();
	String[] escenarios = new String[resultado];
	try {
	    ConexionDB conDB = new ConexionDB();
	    Connection conn = conDB.getConexion(ruta);
	    Statement stmt = conn.createStatement();
	    ResultSet resultSet = stmt.executeQuery("SELECT DISTINCT COD_EQUA FROM MAE_QUA");
	    
	    int i=0;
	    while(resultSet.next()) {
		escenarios[i++] = resultSet.getString(1);
	    }
	    conn.close();
	} catch(Exception e) {
	    System.out.println("getEscenariosSTR Error:  " + e);
	}
	return escenarios;
    }
    
    public double getPorcentajeCuarentena(int dia, int sintomatico, int escenario) {
	if(escenario < 2)
	    return 0.0;
	double valor=0.0;
	Properties props = new Properties();
	props.put("columnTypes", "Integer, String, Double");
	try {
	    ConexionDB conDB = new ConexionDB();
	    Connection conn = conDB.getConexion(ruta,props);
	    Statement stmt = conn.createStatement();

	    // FECHA	COD_STA	ALFA     PCON_ALFA
	    ResultSet resultSet = stmt.executeQuery("SELECT ALFA FROM PCON_ALFA WHERE  FECHA = "+dia+" AND sintomaticos = " + sintomatico);
	    
	    int i=0;
	    if(resultSet.next()) {
		valor = resultSet.getDouble(1);
	    }
	    
	    conn.close();
	} catch(Exception e) {
	    System.out.println("getPorcentajeCuarentena Error:  " + e);
	}
	return valor;
    }
    
    /*public ArrayList<Double> obtenerMaximoDefuncionesXMes(ArrayList<Cuarentena> lista) {
	ArrayList<Double> defuncionesList = new ArrayList<Double>();
	
	ModelSEIMRRS2 modelo = new ModelSEIMRRS2();
	modelo.condicionesIniciales();
	modelo.setListadoCuarentena(lista);
	modelo.simulaModelo();
	modelo.fallecimientoMaximosRegionDia();
	//int dias = modelo.getDias();
	//double[][][] fallecidos = modelo.getD();
	//rg,ss,ss
	
	return defuncionesList;
    }*/

    public ArrayList<Cuarentena> getCuarentenaList() {
	//System.out.println("------------------ " + getFechaInicioEpidemia());
	ArrayList<Cuarentena> cList = new ArrayList<Cuarentena>();
	Properties props = new Properties();
	//props.put("columnTypes", "Integer,Integer,Integer,Integer,Double,Integer");
	props.put("columnTypes", "Integer, String, Double");
	try {
	    ConexionDB conDB = new ConexionDB();
	    Connection conn = conDB.getConexion(ruta,props);
	    Statement stmt = conn.createStatement();

	    //ResultSet resultSet = stmt.executeQuery("SELECT dinicio, dfin, fraccion, escenario, sintomaticos FROM CUARENTENA");
	    ResultSet resultSet = stmt.executeQuery("SELECT FECHA, COD_STA, ALFA FROM PCON_ALFA");
	    	
	    //Cuarentena(int inicio, int sintomatico, double porcentaje)
	    //Cuarentena(int inicio, int fin, double porcentaje, int escenario, int sintomatico)
	    int inicio=0;
	    int estado = 0;
	    double alpa = 0.0;
	    while(resultSet.next()) {
		inicio = resultSet.getInt(1);
		estado = Integer.parseInt(resultSet.getString(2).charAt(1)+"");
		alpa = resultSet.getDouble(3);
		//cList.add(new Cuarentena(resultSet.getInt(1),resultSet.getInt(2),resultSet.getDouble(3),resultSet.getInt(4),resultSet.getInt(5)));
		cList.add(new Cuarentena(inicio,estado,alpa));
	    }
	    
	    conn.close();
	} catch(Exception e) {
	    System.out.println("getPorcentajeCuarentena LST Error:  " + e);
	}
	return cList;
    }
    
    public String getFechaInicioEpidemia() {
	//FECHA_INICIO
	String f_inicio="";
	
	try { 
	    
	    Properties props = new Properties();
	    props.put("columnTypes", "String");
	    ConexionDB conDB = new ConexionDB();
	    Connection conn = conDB.getConexion(ruta,props);
	    Statement stmt = conn.createStatement();

	    ResultSet resultSet = stmt.executeQuery("SELECT FECHA_INICIO FROM FECHA_INICIO");
	    
	    
	    if(resultSet.next()) {
		f_inicio = resultSet.getString(1);
	    }
	    
	    
	    conn.close();
	} catch(Exception e) {
	    System.out.println("getPorcentajeCuarentena LST Error:  " + e);
	}
	return f_inicio;
    }
    
    /// Mes ...
    public ArrayList<Cuarentena> getCuarentenaListMes() {
	
	String inicioCuarentena = getFechaInicioEpidemia();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");    
	LocalDate  fInicioCuarentena = LocalDate.parse(inicioCuarentena,formatter);
	
	
	ArrayList<Cuarentena> cList = new ArrayList<Cuarentena>();
	Properties props = new Properties();
	//props.put("columnTypes", "Integer,Integer,Integer,Integer,Double,Integer");
	props.put("columnTypes", "String, Integer, String, String, Double");
	LocalDate  dateInicio;
	LocalDate  dateFin;
	try {
	    ConexionDB conDB = new ConexionDB();
	    Connection conn = conDB.getConexion(ruta,props);
	    Statement stmt = conn.createStatement();

	    //ResultSet resultSet = stmt.executeQuery("SELECT dinicio, dfin, fraccion, escenario, sintomaticos FROM CUARENTENA");
	    // COD_STA	MES	D_INICIO	D_FIN	ALPHA
	    //ResultSet resultSet = stmt.executeQuery("SELECT FECHA, COD_STA, ALFA FROM PCON_ALFA");
	    ResultSet resultSet = stmt.executeQuery("SELECT COD_STA, MES, D_INICIO, D_FIN, ALPHA FROM PCON_ALFA_MES");
	    	
	    //Cuarentena(int inicio, int sintomatico, double porcentaje)
	    //Cuarentena(int inicio, int fin, double porcentaje, int escenario, int sintomatico)
	    int estado = 0;
	    int mes=0;
	    String f_inicio="";
	    String f_fin="";
	    double alpa = 0.0;
	    int fInicialInt=0;
	    int fFinalInt=0;
	    while(resultSet.next()) {
		estado = Integer.parseInt(resultSet.getString(1).charAt(1)+"");
		mes = resultSet.getInt(2);
		
		f_inicio = resultSet.getString(3);
		dateInicio = LocalDate.parse(f_inicio,formatter);
		fInicialInt = (int)DAYS.between(fInicioCuarentena,dateInicio);

		f_fin = resultSet.getString(4);
		dateFin = LocalDate.parse(f_fin,formatter);
		fFinalInt = (int)DAYS.between(fInicioCuarentena,dateFin);
			
		alpa = resultSet.getDouble(5);
		//cList.add(new Cuarentena(resultSet.getInt(1),resultSet.getInt(2),resultSet.getDouble(3),resultSet.getInt(4),resultSet.getInt(5)));
		//Cuarentena(int mes, int inicio, int fin, double porcentaje, int sintomatico)
		cList.add(new Cuarentena(mes,fInicialInt,fFinalInt,alpa,estado));
	    }
	    
	    conn.close();
	} catch(Exception e) {
	    System.out.println("getPorcentajeCuarentena LST Error:  " + e);
	}
	return cList;
    }
}

