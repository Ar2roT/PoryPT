/*:-----------------------------------------------------------------------------
 *:                       INSTITUTO TECNOLOGICO DE LA LAGUNA
 *:                     INGENIERIA EN SISTEMAS COMPUTACIONALES
 *:                         LENGUAJES Y AUTOMATAS II           
 *: 
 *:                  SEMESTRE: ___________    HORA: ___________ HRS
 *:                                   
 *:               
 *:         Clase con la funcionalidad del Analizador Sintactico
 *                 
 *:                           
 *: Archivo       : SintacticoSemantico.java
 *: Autor         : Fernando Gil  ( Estructura general de la clase  )
 *:                 Grupo de Lenguajes y Automatas II ( Procedures  )
 *: Fecha         : 03/SEP/2014
 *: Compilador    : Java JDK 7
 *: Descripción   : Esta clase implementa un parser descendente del tipo 
 *:                 Predictivo Recursivo. Se forma por un metodo por cada simbolo
 *:                 No-Terminal de la gramatica mas el metodo emparejar ().
 *:                 El analisis empieza invocando al metodo del simbolo inicial.
 *: Ult.Modif.    :
 *:  Fecha      Modificó            Modificacion
 *:=============================================================================
 *: 22/Feb/2015 FGil                -Se mejoro errorEmparejar () para mostrar el
 *:                                 numero de linea en el codigo fuente donde 
 *:                                 ocurrio el error.
 *: 08/Sep/2015 FGil                -Se dejo lista para iniciar un nuevo analizador
 *:                                 sintactico.
 *: 20/FEB/2023 F.Gil, Oswi         -Se implementaron los procedures del parser
 *:                                  predictivo recursivo de leng BasicTec.
 *:-----------------------------------------------------------------------------
 */
package compilador;

import javax.swing.JOptionPane;

public class SintacticoSemantico {

    private Compilador cmp;
    private boolean    analizarSemantica = false;
    private String     preAnalisis;
    
    //--------------------------------------------------------------------------
    // Constructor de la clase, recibe la referencia de la clase principal del 
    // compilador.
    //

    public SintacticoSemantico(Compilador c) {
        cmp = c;
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // Metodo que inicia la ejecucion del analisis sintactico predictivo.
    // analizarSemantica : true = realiza el analisis semantico a la par del sintactico
    //                     false= realiza solo el analisis sintactico sin comprobacion semantica

    public void analizar(boolean analizarSemantica) {
        this.analizarSemantica = analizarSemantica;
        preAnalisis = cmp.be.preAnalisis.complex;

        // * * *   INVOCAR AQUI EL PROCEDURE DEL SIMBOLO INICIAL   * * *
        
    }

    //--------------------------------------------------------------------------

    private void emparejar(String t) {
        if (cmp.be.preAnalisis.complex.equals(t)) {
            cmp.be.siguiente();
            preAnalisis = cmp.be.preAnalisis.complex;            
        } else {
            errorEmparejar( t, cmp.be.preAnalisis.lexema, cmp.be.preAnalisis.numLinea );
        }
    }
    
    //--------------------------------------------------------------------------
    // Metodo para devolver un error al emparejar
    //--------------------------------------------------------------------------
 
    private void errorEmparejar(String _token, String _lexema, int numLinea ) {
        String msjError = "";

        if (_token.equals("id")) {
            msjError += "Se esperaba un identificador";
        } else if (_token.equals("num")) {
            msjError += "Se esperaba una constante entera";
        } else if (_token.equals("num.num")) {
            msjError += "Se esperaba una constante real";
        } else if (_token.equals("literal")) {
            msjError += "Se esperaba una literal";
        } else if (_token.equals("oparit")) {
            msjError += "Se esperaba un operador aritmetico";
        } else if (_token.equals("oprel")) {
            msjError += "Se esperaba un operador relacional";
        } else if (_token.equals("opasig")) {
            msjError += "Se esperaba operador de asignacion";
        } else {
            msjError += "Se esperaba " + _token;
        }
        msjError += " se encontró " + ( _lexema.equals ( "$" )? "fin de archivo" : _lexema ) + 
                    ". Linea " + numLinea;        // FGil: Se agregó el numero de linea

        cmp.me.error(Compilador.ERR_SINTACTICO, msjError);
    }

    // Fin de ErrorEmparejar
    //--------------------------------------------------------------------------
    // Metodo para mostrar un error sintactico

    private void error(String _descripError) {
        cmp.me.error(cmp.ERR_SINTACTICO, _descripError);
    }

    // Fin de error
    //--------------------------------------------------------------------------
    //  *  *   *   *    PEGAR AQUI EL CODIGO DE LOS PROCEDURES  *  *  *  *
    //--------------------------------------------------------------------------

    //Comienzo de declaracion de procedures
    
    /*------------------------------------------- 
    PROGRAMA
    Autor: Arturo Tejeda García
    Conujunto de primeros = {INSTRUCCION, empty}
    */
    private void PROGRAMA() {
        INSTRUCCION();
        PROGRAMA();
    }
    /*------------------------------------------- 
    INSTRUCCION
    Autor: Arturo Tejeda García
    Conujunto de primeros = {INSTRUCCION, empty}
    */
    private void INSTRUCCION() {
        if ( preAnalisis.equals( "FUNCION" ) ){
            //INSTRUCCION -> FUNCION
            FUNCION();
        }
        else if ( preAnalisis.equals( "PRPOSICION" ) ){
            //INSTRUCCION -> PRPOSICION
            PROPOSICION();
        }
    }
     /*------------------------------------------- 
    INSTRUCCION
    Autor: Luis Ernesto Molina Arias
    Conujunto de primeros = {def}
    */
    private void FUNCION() {
        if ( preAnalisis.equals( "def" ) ){
            //FUNCION -> def id ( ARGUMENTOS ) : TIPO_RETORNO  PROPOSICIONES_OPTATIVAS  return RESULTADO ::
            emparejar( "def" );
            emparejar( "id" );
            emparejar( "(" );
            ARGUMENTOS();
            emparejar( ":" );
            TIPO_RETORNO();
            PROPOSICIONES_OPTATIVAS();
            emparejar( "return" );
            RESULTADO();
            emparejar( "::" );
        }
        else {
            
        }
    }
     /*------------------------------------------- 
    ARGUMENTOS
    Autor: Luis Ernesto Molina Arias
    Conujunto de primeros = {TIPO_DATO, empty}
    */
    private void ARGUMENTOS() {
        if ( preAnalisis.equals( "int" ) || preAnalisis.equals( "float" ) || preAnalisis.equals( "string" )){
            //ARGUMENTOS -> TIPO_DATO id  ARGUMENTOS’  | ϵ
            TIPO_DATO();
            emparejar( "id" );
            ARGUMENTOS_P();
        }
        else {
            
        }
    }
     /*------------------------------------------- 
    ARGUMENTOS_P
    Autor: Luis Ernesto Molina Arias
    Conujunto de primeros = {,, empty}
    */
    private void ARGUMENTOS_P() {
        if ( preAnalisis.equals( "," ) || preAnalisis.equals( "float" ) || preAnalisis.equals( "string" )){
            //ARGUMENTOS’ -> , TIPO_DATO id  ARGUMENTOS’  |  ϵ
            emparejar( "," );
            TIPO_DATO();
            emparejar( "id" );
            ARGUMENTOS_P();
        }
        else {
            
        }
    }
    /*------------------------------------------- 
    DECLARACION_VARS
    Autor: Luis Ernesto Molina Arias
    Conujunto de primeros = {TIPO_DATO}
    */
    private void DECLARACION_VARS() {
        if ( preAnalisis.equals( "," ) || preAnalisis.equals( "float" ) || preAnalisis.equals( "string" )){
            //ARGUMENTOS’ -> , TIPO_DATO id  ARGUMENTOS’  |  ϵ
            emparejar( "," );
            TIPO_DATO();
            emparejar( "id" );
            ARGUMENTOS_P();
        }
        else {
            
        }
    }
    /*------------------------------------------- 
    TIPO_DATO
    Autor: Efrain Montalvo Sáncez
    Conujunto de primeros = {int, float, string}
    */
    private void TIPO_DATO() {
        if( preAnalisis.equals( "int" ) ){
            //TIPO_DATO -> int
            emparejar( "int" );            
        }
        else if ( preAnalisis.equals( "float" ) ){
            //TIPO_DATO -> float
            emparejar( "float" );
        }
        else if ( preAnalisis.equals( "string" ) ){
            //TIPO_DATO -> string
            emparejar( "string" );
        }
    }
    
    /*-----------------------------------------------------
    PORPOSICION
    Autor: Luis Ernesto Molina Arias
    Conjunto de primeros = {DECLARACION_VARS, id, if, while, print}
    */
    private void PROPOSICION() {
        if ( preAnalisis.equals( "int" ) || preAnalisis.equals( "float" )|| preAnalisis.equals( "string" )){
            DECLARACION_VARS()
        }
        else if ( preAnalisis.equals( "id" ) ){
            //PREPOSICION -> id PREPOSICON'
            emparejar( "id" );
            PREPOSICION_P();
        }
        else if ( preAnalisis.equals( "if" ) ){
            //PREPOSICION -> if
            emparejar( "if" );
            CONDICION();
            if ( preAnalisis.equals( ":" ) ){
                
            }
        }
    }
    /*-----------------------------------------------------
    EXPRESION'
    Autor: Arturo Tejeda García
    Conjunto de primeros = {opsuma, empty}
    */
    private void EXPRESION_P() {
        if ( preAnalisis.equals( "opsuma" ) || preAnalisis.equals( "float" )|| preAnalisis.equals( "string" )){
            emparejar( "opsuma" );
            TERMINO();
            EXPRESION_P();
        }
        else{
            //ERROR
        }
    }
    /*-----------------------------------------------------
    TERMINO
    Autor: Arturo Tejeda García
    Conjunto de primeros = {FACTOR}
    */
    private void TERMINO() {
        if ( preAnalisis.equals( "id" ) || preAnalisis.equals( "num" )|| preAnalisis.equals( "numnum" ) || preAnalisis.equals( "(" )){
            FACTOR();
            TERMINO_P();
        }
        else{
            //ERROR
        }
    }
    /*-----------------------------------------------------
    TERMINO_P
    Autor: Arturo Tejeda García
    Conjunto de primeros = {opmult, empty}
    */
    private void TERMINO_P() {
        if ( preAnalisis.equals( "opmult" ) || preAnalisis.equals( "float" )|| preAnalisis.equals( "string" )){
            emparejar( "opmult" );
            FACTOR();
            TERMINO_P();
        }
        else {
            //ERROR
        }
    }
    /*-----------------------------------------------------
    FACTOR
    Autor: Arturo Tejeda García
    Conjunto de primeros = {id, num, num.num}
    */
    private void FACTOR() {
        if ( preAnalisis.equals( "id" ) ){
            emparejar( "id" );
            FACTOR_P();
        }
        else if ( preAnalisis.equals( "num" ) ){
            emparejar("num");
        }
        else if ( preAnalisis.equals( "numnum" ) ){
            emparejar("numnum");
        }
        else if ( preAnalisis.equals( "(" ) ){
            EXPRESION();
            emparejar(")");
        }
        else {
            //ERROR
        }
    }
    /*-----------------------------------------------------
    FACTOR_P
    Autor: Arturo Tejeda García
    Conjunto de primeros = {(, empty}
    */
    private void FACTOR_P() {
        if ( preAnalisis.equals( "(" ) || preAnalisis.equals( "float" )|| preAnalisis.equals( "string" )){
            LISTA_EXPRESIONES();
            emparejar( ")" );
        }
        else {
            //ERROR
        }
    }
    private void PREPOSICION_P() {
        
    }
}
//------------------------------------------------------------------------------
//::