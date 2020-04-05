package co.edu.uniquindio.lexico

import kotlin.collections.ArrayList

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 1.1
 *
 * Analizador Lexico
 */
class AnalizadorLexico(private val codigoFuente: String) {
    /**
     * Lista de tokens y errores generados por el analizador lexico
     */
    val listaTokens: ArrayList<Token> = ArrayList()
    val listaErrores: ArrayList<String> = ArrayList()

    /**
     * Elementos necesarios del analizador lexico
     */
    private var caracterActual: Char
    private var posicionActual = 0
    private var filaActual = 0
    private var columnaActual = 0
    private val finCodigo = 0.toChar()

    init {
        caracterActual = codigoFuente[posicionActual]
    }

    /**
     * Funcion principal
     *
     * Analiza el codigo fuente
     *
     * @param codigoFuente El codigo que se analizara
     *
     * @return tokens La lista de tokens
     * @return errores La rutina de errores
     */
    fun analizar() {
        while (caracterActual != finCodigo) {
            if (caracterActual == ' ' || caracterActual == '\t' || caracterActual == '\n') {
                siguienteCaracter()
                continue
            }
            if (esEntero()) continue
            if (esReal()) continue
            if (esBooleano()) continue

            if (esIdentificador()) continue
            if (esPalabraReservada()) continue

            if (esCadena()) continue
            if (esCaracter()) continue

            if (esOperadorAsignacion()) continue
            if (esOperadorAritmetico()) continue
            if (esOperadorIncreDecre()) continue

            if (esOperadorRelacional()) continue
            if (esOperadorLogico()) continue

            if (esParentesisLlave()) continue
            if (esTerminal()) continue
            if (esSeparador()) continue

            if (esComentario()) continue
            if (esPunto()) continue

            if (caracterActual != finCodigo) {
                listaTokens.add(Token("" + caracterActual, Categoria.DESCONOCIDO, filaActual, columnaActual))
                siguienteCaracter()
            }
        }
    }


    /**
     * Metodo que avanza al siguiente caracter
     *
     * Configura las filas y columnas
     * Verifica que no se desborde
     */
    private fun siguienteCaracter() {
        posicionActual++
        if (posicionActual < codigoFuente.length) {
            if (caracterActual == '\n') {
                filaActual++
                columnaActual = 0
            } else {
                columnaActual++
            }
            caracterActual = codigoFuente[posicionActual]
        } else {
            caracterActual = finCodigo
        }
    }

    /**
     * Realiza backtracking para volver hasta una posicion anterior
     *
     * @param posicionInicial La posicion inicial
     * @param fila La fila inicial
     * @param columna La columna inicial
     */
    private fun backtracking(posicionInicial:Int, fila:Int, columna:Int) {
        posicionActual = posicionInicial
        filaActual = fila
        columnaActual = columna

        caracterActual = codigoFuente[posicionActual]
    }

    /**
     * Verifica si la palabra actual es un numero entero
     *
     * @return esEntero retorna true si es entero
     */
    private fun esEntero(): Boolean {
        val posicionInicial = posicionActual
        val fila = filaActual
        val columna = columnaActual

        var palabra = ""
        var centinela = false

        if (caracterActual == '#') {
            palabra += caracterActual
            siguienteCaracter()

            if (Character.isDigit(caracterActual)) {
                palabra += caracterActual
                siguienteCaracter()

                centinela = true

                while (Character.isDigit(caracterActual)) {
                    palabra += caracterActual
                    siguienteCaracter()
                }

                listaTokens.add(Token(palabra, Categoria.ENTERO, fila, columna))
            } else {
                backtracking(posicionInicial, fila, columna)
            }
        }

        return centinela
    }

    /**
     * Verifica si la palabra actual es un identificador
     *
     * @return esIdentificador retorna true si es identificador
     */
    private fun esIdentificador(): Boolean {
        val posicionInicial = posicionActual
        val fila = filaActual
        val columna = columnaActual

        var palabra = ""
        var centinela = false

        if (caracterActual == '@') {
            palabra += caracterActual
            siguienteCaracter()

            if (Character.isLetter(caracterActual)) {
                palabra += caracterActual
                siguienteCaracter()

                while (Character.isLetter(caracterActual) || caracterActual == '_' || Character.isDigit(caracterActual)) {
                    palabra += caracterActual
                    siguienteCaracter()
                }

                if (!palabra.endsWith("_")) {
                    listaTokens.add(Token(palabra, Categoria.IDENTIFICADOR, fila, columna))
                } else {
                    listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))//TODO: debe retornar a la posicion inicial
                }
            } else {
                listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))//TODO: debe retornar a la posicion inicial
            }
            return true
        }
        return false
    }

    /**
     * Verifica si la palabra actual es un numero real
     *
     * @return esReal retorna true si es real
     */
    private fun esReal(): Boolean {
        val posicionInicial = posicionActual
        val fila = filaActual
        val columna = columnaActual

        var palabra = ""
        var centinela = false

        if (caracterActual == '*') {
            palabra += caracterActual
            siguienteCaracter()

            if (Character.isDigit(caracterActual)) {
                palabra += caracterActual
                siguienteCaracter()
                while (Character.isDigit(caracterActual)) { // Transicion
                    palabra += caracterActual
                    siguienteCaracter()
                }
                if (caracterActual == '.') {
                    palabra += caracterActual
                    siguienteCaracter()
                    
                    if (Character.isDigit(caracterActual)) {
                        palabra += caracterActual
                        siguienteCaracter()

                        while (Character.isDigit(caracterActual)) { // Transicion
                            palabra += caracterActual
                            siguienteCaracter()
                        }
                        listaTokens.add(Token(palabra, Categoria.REAL, fila, columna))
                        siguienteCaracter()
                    } else {
                        listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
                        //TODO: debe retornar a la posicion inicial
                    }
                } else {
                    listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
                    //TODO: debe retornar a la posicion inicial
                }
            } else {
                listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
                //TODO: debe retornar a la posicion inicial
            }
            return true
        }
        return false
    }


    /**
     * Verifica si la palabra actual es un operador aritmetico
     *
     * @return esOperadorAritmetico retorna true si es un operador aritmetico
     */
    private fun esOperadorAritmetico(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        return if (evaluarOperadorAritmetico(caracterActual)) {
            if (caracterActual != '-') {
                listaTokens.add(Token(caracterActual.toString() + "", Categoria.OPERADOR_ARTIMETICO, fila, columna))
                siguienteCaracter()
                true
            } else {
                siguienteCaracter()
                if (caracterActual != '>') {
                    listaTokens.add(Token("-", Categoria.OPERADOR_ARTIMETICO, fila, columna))
                    siguienteCaracter()
                    true
                } else {
                    listaTokens.add(Token("->", Categoria.OPERADOR_RELACIONAL, fila, columna))
                    siguienteCaracter()
                    true
                }
            }
        } else false
    }

    /**
     * Verifica si el caracter corresponde a un operador aritmetico
     *
     * @param caracter El caracter a verificar
     *
     * @return boolean retorna true si es un operador aritmetico valido
     */
    private fun evaluarOperadorAritmetico(caracter: Char): Boolean {
        return caracter == '+' || caracter == '-' || caracter == 'x' || caracter == '%' || caracter == '/'
    }
    /**
     * Verifica si la palabra actual es un operador de asignacion
     *
     * @return esOperadorAsignacion retorna true si es un operador de asignacion
     */
    private fun esOperadorAsignacion(): Boolean {
        //TODO: Faltan operadores de asignacion
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual.toString() + "" == "=") {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.OPERADOR_ASIGNACION, fila, columna))
            siguienteCaracter()
            return true
        }
        return false
    }

    /**
     * Verifica si la palabra actual es operador de incremento o decremento
     *
     * @return esOperadorIncreDecre retorna true si es operador de incremento o decremento
     */
    private fun esOperadorIncreDecre(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual == '^') {
            var pal = caracterActual.toString() + ""
            siguienteCaracter()

            when (caracterActual) {
                '+' -> {
                    pal += caracterActual
                    listaTokens.add(Token(pal, Categoria.OPERADOR_INCREMENTO, fila, columna))
                    siguienteCaracter()
                }
                '-' -> {
                    pal += caracterActual
                    listaTokens.add(Token(pal, Categoria.OPERADOR_DECREMENTO, fila, columna))
                    siguienteCaracter()
                }
                else -> {
                    listaTokens.add(Token("^", Categoria.OPERADOR_LOGICO, fila, columna))
                }
            }
            return true
        }
        return false
    }

    /**
     * Verifica si la palabra actual es un operador logico
     *
     * @return esOperadorLogico retorna true si es un operador logico
     */
    private fun esOperadorLogico(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual == '^' || caracterActual == '~' || caracterActual == '¬') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.OPERADOR_LOGICO, fila, columna))
            siguienteCaracter()
            return true
        }
        return false
    }

    /**
     * Verifica si la palabra actual es parentesis o llave
     *
     * @return esParentesisLlaves retorna true si es parentesis o llave
     */
    private fun esParentesisLlave(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual == '{' || caracterActual == '}') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.PARENTESIS, fila, columna))
            siguienteCaracter()
            return true
        } else if (caracterActual == '¿' || caracterActual == '?') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.LLAVES, fila, columna))
            siguienteCaracter()
            return true
        }
        return false
    }

    /**
     * Verifica si la palabra actual es terminal
     *
     * @return esTerminal retorna true si es terminal
     */
    private fun esTerminal(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual == '!') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.FIN_SENTENCIA, fila, columna))
            siguienteCaracter()
            return true
        }
        return false
    }

    /**
     * Verifica si la palabra actual es un separador
     *
     * @return esSeparador retorna true si es separador
     */
    private fun esSeparador(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual == '°') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.SEPARADOR, fila, columna))
            siguienteCaracter()
            return true
        }
        return false
    }

    /**
     * Verifica si la palabra actual es un operador relacional
     *
     * @return esOperadorRelacional retorna true si es operador relacional
     */
    private fun esOperadorRelacional(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        var pal = ""
        if (caracterActual == '<') {
            pal += caracterActual
            siguienteCaracter()
            if (caracterActual == '-' || caracterActual == '<') {
                pal += caracterActual
                siguienteCaracter()
                if (caracterActual == '>' && codigoFuente[posicionActual-1] == '-') {
                    pal += caracterActual
                    siguienteCaracter()
                }
                listaTokens.add(Token(pal, Categoria.OPERADOR_RELACIONAL, fila, columna))
            } else {
                listaTokens.add(Token(pal, Categoria.DESCONOCIDO, fila, columna))//TODO: debe retornar a la posicion inicial
            }
            return true
        }
        if (caracterActual == '>') {
            pal += caracterActual
            siguienteCaracter()
            if (caracterActual == '-' || caracterActual == '>') {
                pal += caracterActual
                siguienteCaracter()
                listaTokens.add(Token(pal, Categoria.OPERADOR_RELACIONAL, fila, columna))
            } else {
                listaTokens.add(Token(pal, Categoria.DESCONOCIDO, fila, columna))//TODO: debe retornar a la posicion inicial
            }
            return true
        }
        if (caracterActual == '¬') {
            pal += caracterActual
            siguienteCaracter()
            if (caracterActual == '-') {
                pal += caracterActual
                siguienteCaracter()
                listaTokens.add(Token(pal, Categoria.OPERADOR_RELACIONAL, fila, columna))
                return true
            } else {
                posicionActual -= 2
                siguienteCaracter()
                return false
            }
        }
        return false
    }

    /**
     * Verifica si la palabra actual es un Booleano
     *
     * Palabras Booleanas:
     * .true , .false
     *
     * @return esBooleano retorna true si es una palabra Booleana
     */
    private fun esBooleano(): Boolean { // palabra reservada
        if (caracterActual == '.') {
            var palabra = ""
            val fila = filaActual
            val columna = columnaActual
            palabra += caracterActual
            siguienteCaracter()
            if (caracterActual == 't') {
                palabra += caracterActual
                siguienteCaracter()
                if (caracterActual == 'r') {
                    palabra += caracterActual
                    siguienteCaracter()
                    if (caracterActual == 'u') {
                        palabra += caracterActual
                        siguienteCaracter()
                        if (caracterActual == 'e'){
                            palabra += caracterActual
                            siguienteCaracter()
                            listaTokens.add(Token(palabra, Categoria.BOOLEANO, fila, columna))
                            return true
                        }
                    }
                }
            }
            if (caracterActual == 'f') {
                palabra += caracterActual
                siguienteCaracter()
                if (caracterActual == 'a') {
                    palabra += caracterActual
                    siguienteCaracter()
                    if (caracterActual == 'l') {
                        palabra += caracterActual
                        siguienteCaracter()
                        if (caracterActual == 's'){
                            palabra += caracterActual
                            siguienteCaracter()
                            if (caracterActual == 'e'){
                                palabra += caracterActual
                                siguienteCaracter()
                                listaTokens.add(Token(palabra, Categoria.BOOLEANO, fila, columna))
                                return true
                            }
                        }
                    }
                }
            }

            if (palabra.isNotEmpty()) {
                while (Character.isLetter(caracterActual)) {
                    palabra += caracterActual
                    siguienteCaracter()
                }

                listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
                //TODO: debe retornar a la posicion inicial
                return true
            }
        }
        return false
    }


    /**
     * Verifica si la palabra actual es una cadena de caracteres
     *
     * @return esCadena retorna true si es cadena
     */
    private fun esCadena(): Boolean { // TODO: corregir, validar si encuentra caracter especial


        if (caracterActual == '(') {
            var palabra = ""
            val fila = filaActual
            val columna = columnaActual
            // Transici�n
            palabra += caracterActual
            siguienteCaracter()
            while (caracterActual != ')' && caracterActual !='\n' && caracterActual != finCodigo) {
                palabra += caracterActual
                siguienteCaracter()
            }
            palabra += caracterActual
            siguienteCaracter()
            if (palabra.endsWith(")")) {
                listaTokens.add(Token(palabra, Categoria.CADENA_CARACTERES, fila, columna))
            }else{
                listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
            }
            return true
        }
        return false
    }

    /**
     * Verifica si la palabra actual es un caracter
     *
     * @return esCaracter retorna true si es un caracter
     */
    private fun esCaracter(): Boolean {
        //TODO: validar si tiene caracter especial
        if (posicionActual+2 <= codigoFuente.length && caracterActual == '"') {
            if (codigoFuente[posicionActual+2] == '"') {
                var palabra = ""
                val fila = filaActual
                val columna = columnaActual
                // obtiene el caracter
                palabra += caracterActual
                siguienteCaracter()
                palabra += caracterActual
                siguienteCaracter()
                palabra += caracterActual
                siguienteCaracter()
                listaTokens.add(Token(palabra, Categoria.CARACTER, fila, columna))
                return true
            }
        }
        return false
    }

    /**
     * Verifica si la palabra actual es un comentario
     *
     * @return esComentario retorna true si es comentario
     */
    private fun esComentario(): Boolean {
        if (caracterActual == ':') {
            var palabra = ""
            var terminal = '\n'
            var category = Categoria.COMENTARIO_LINEA
            val fila = filaActual
            val columna = columnaActual
            palabra += caracterActual
            siguienteCaracter()
            // Verifica si es un comentario de bloque o de linea
            if (caracterActual == '/'){
                category = Categoria.COMENTARIO_BLOQUE
                palabra += caracterActual
                siguienteCaracter()
                while (caracterActual != finCodigo && !esTerminalBloque()) {
                    palabra += caracterActual
                    siguienteCaracter()
                }
                palabra += caracterActual
                siguienteCaracter()
            } else{
                while (caracterActual != finCodigo && caracterActual != terminal) {
                    palabra += caracterActual
                    siguienteCaracter()
                }
            }
            palabra += caracterActual
            siguienteCaracter()
            listaTokens.add(Token(palabra, category, fila, columna))
            return true
        }
        return false
    }

    /**
     * Metodo para validar si es un terminal de Comentario de bloque
     */
    private fun esTerminalBloque(): Boolean{
        if (posicionActual+1 <= codigoFuente.length && caracterActual == '/' && codigoFuente[posicionActual+1] == ':'){
            return true;
        }
        return false;
    }

    /**
     * Verifica si hay dos punto o un punto
     *
     * @return esPunto retorna true si es dos punto o un punto
    */
    private fun esPunto(): Boolean{
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual == '|') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.DOS_PUNTOS, fila, columna))
            siguienteCaracter()
            return true
        }else if (caracterActual == ';'){
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.PUNTO, fila, columna))
            siguienteCaracter()
            return true
        }
        return false
    }

    /**
     * Verifica si la palabra actual es una palabra reservada
     *
     * Palabras Reservadas:
     * cosa, caja, choose, ciclo, ent, estrato1, estrato6, dec, devolver, durante, meter, saltar, pal, bip, wi, wo, bit
     *
     * @return esPalabraReservada retorna true si es palabra reservada
     */
    private fun esPalabraReservada(): Boolean { // palabra reservada
        var palabra = ""
        val fila = filaActual
        val columna = columnaActual

        if (caracterActual == 'c') {
            palabra += caracterActual
            siguienteCaracter()
            if (caracterActual == 'o') {
                palabra += caracterActual
                siguienteCaracter()
                if (caracterActual == 's') {
                    palabra += caracterActual
                    siguienteCaracter()
                    if (caracterActual == 'a') {
                        palabra += caracterActual
                        siguienteCaracter()
                        listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                        return true
                    }
                }
            } else if (caracterActual == 'a') {
                palabra += caracterActual
                siguienteCaracter()
                if (caracterActual == 'j') {
                    palabra += caracterActual
                    siguienteCaracter()
                    if (caracterActual == 'a') {
                        palabra += caracterActual
                        siguienteCaracter()
                        listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                        return true
                    }
                }
            }else if (caracterActual == 'h'){
                palabra += caracterActual
                siguienteCaracter()
                if (caracterActual == 'o') {
                    palabra += caracterActual
                    siguienteCaracter()
                    if (caracterActual == 'o') {
                        palabra += caracterActual
                        siguienteCaracter()
                        if (caracterActual == 's') {
                            palabra += caracterActual
                            siguienteCaracter()
                            if (caracterActual == 'e') {
                                palabra += caracterActual
                                siguienteCaracter()
                                listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                                return true
                            }
                        }
                    }
                }
            }else if(caracterActual == 'i'){
                palabra += caracterActual
                siguienteCaracter()
                if (caracterActual == 'c') {
                    palabra += caracterActual
                    siguienteCaracter()
                    if (caracterActual == 'l') {
                        palabra += caracterActual
                        siguienteCaracter()
                        if (caracterActual == 'o') {
                            palabra += caracterActual
                            siguienteCaracter()
                            listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                            return true
                        }
                    }
                }
            }
        }
        if (caracterActual == 'e') {
            // Transici�n
            palabra += caracterActual
            siguienteCaracter()
            if (caracterActual == 'n') {
                palabra += caracterActual
                siguienteCaracter()
                if (caracterActual == 't') {
                    palabra += caracterActual
                    siguienteCaracter()
                    listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                    return true
                }
            } else if (caracterActual == 's') {
                palabra += caracterActual
                siguienteCaracter()
                if (caracterActual == 't') {
                    palabra += caracterActual
                    siguienteCaracter()
                    if (caracterActual == 'r') {
                        palabra += caracterActual
                        siguienteCaracter()
                        if (caracterActual == 'a') {
                            palabra += caracterActual
                            siguienteCaracter()
                            if (caracterActual == 't') {
                                palabra += caracterActual
                                siguienteCaracter()
                                if (caracterActual == 'o') {
                                    palabra += caracterActual
                                    siguienteCaracter()
                                    if (caracterActual == '1') {
                                        palabra += caracterActual
                                        siguienteCaracter()
                                        listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                                        return true
                                    } else if (caracterActual == '6') {
                                        palabra += caracterActual
                                        siguienteCaracter()
                                        listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                                        return true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // Palabra Reservada
        if (caracterActual == 'd') {
            // Transici�n
            palabra += caracterActual
            siguienteCaracter()
            if (caracterActual == 'e') {
                palabra += caracterActual
                siguienteCaracter()
                if (caracterActual == 'c') {
                    palabra += caracterActual
                    siguienteCaracter()
                    listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                    return true
                } else if (caracterActual == 'v') {
                    palabra += caracterActual
                    siguienteCaracter()
                    if (caracterActual == 'o') {
                        palabra += caracterActual
                        siguienteCaracter()
                        if (caracterActual == 'l') {
                            palabra += caracterActual
                            siguienteCaracter()
                            if (caracterActual == 'v') {
                                palabra += caracterActual
                                siguienteCaracter()
                                if (caracterActual == 'e') {
                                    palabra += caracterActual
                                    siguienteCaracter()
                                    if (caracterActual == 'r') {
                                        palabra += caracterActual
                                        siguienteCaracter()
                                        listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                                        return true
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (caracterActual == 'u') {
                palabra += caracterActual
                siguienteCaracter()
                if (caracterActual == 'r') {
                    palabra += caracterActual
                    siguienteCaracter()
                    if (caracterActual == 'a') {
                        palabra += caracterActual
                        siguienteCaracter()
                        if (caracterActual == 'n') {
                            palabra += caracterActual
                            siguienteCaracter()
                            if (caracterActual == 't') {
                                palabra += caracterActual
                                siguienteCaracter()
                                if (caracterActual == 'e') {
                                    palabra += caracterActual
                                    siguienteCaracter()
                                    listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                                    return true
                                }
                            }
                        }
                    }
                }
            }
        }
        if (caracterActual == 'm') {
            palabra += caracterActual
            siguienteCaracter()
            if (caracterActual == 'e') {
                palabra += caracterActual
                siguienteCaracter()
                if (caracterActual == 't') {
                    palabra += caracterActual
                    siguienteCaracter()
                    if (caracterActual == 'e') {
                        palabra += caracterActual
                        siguienteCaracter()
                        if (caracterActual == 'r') {
                            palabra += caracterActual
                            siguienteCaracter()
                            listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                            return true
                        }
                    }
                }
            }
        }
        if (caracterActual == 's') {
            // Transici�n
            palabra += caracterActual
            siguienteCaracter()
            if (caracterActual == 'a') {
                palabra += caracterActual
                siguienteCaracter()
                if (caracterActual == 'l') {
                    palabra += caracterActual
                    siguienteCaracter()
                    if (caracterActual == 't') {
                        palabra += caracterActual
                        siguienteCaracter()
                        if (caracterActual == 'a') {
                            palabra += caracterActual
                            siguienteCaracter()
                            if (caracterActual == 'r') {
                                palabra += caracterActual
                                siguienteCaracter()
                                listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                                return true
                            }
                        }
                    }
                }
            }
        }

        if (caracterActual == 'p') {
            palabra += caracterActual
            siguienteCaracter()
            if (caracterActual == 'a') {
                palabra += caracterActual
                siguienteCaracter()
                if (caracterActual == 'l') {
                    palabra += caracterActual
                    siguienteCaracter()
                    listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                    return true
                }
            }
        }

        if (caracterActual == 'w'){
            palabra += caracterActual
            siguienteCaracter()
            if (caracterActual == 'i'){
                palabra += caracterActual
                siguienteCaracter()
                listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                return true
            }else if(caracterActual == 'o'){
                palabra += caracterActual
                siguienteCaracter()
                listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                return true
            }
        }

        if (caracterActual == 'b'){
            palabra += caracterActual
            siguienteCaracter()
            if (caracterActual == 'i'){
                palabra += caracterActual
<<<<<<< HEAD
                obtenerSgteCaracter()
                if (caracterActual == 'p' || caracterActual == 't') {
=======
                siguienteCaracter()
                if (caracterActual == 'p'){
>>>>>>> master
                    palabra += caracterActual
                    siguienteCaracter()
                    listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                    return true
                }
            }
        }

        if (palabra.isNotEmpty()) {
            while (Character.isLetter(caracterActual)) {
                palabra += caracterActual
                siguienteCaracter()
            }

            listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
            return true
        }
        return false
    }
}