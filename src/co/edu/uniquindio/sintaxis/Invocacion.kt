package co.edu.uniquindio.sintaxis

import co.edu.uniquindio.lexico.Token

class Invocacion (var nombreFuncion: Token, var listaArgumentos:  ArrayList<Expresion>) : Sentencia() {
}