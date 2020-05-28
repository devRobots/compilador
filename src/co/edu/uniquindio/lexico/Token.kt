package co.edu.uniquindio.lexico

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 1.1
 *
 * Token
 */
class Token(var lexema: String, var categoria: Categoria, var fila: Int, var columna: Int) {
    override fun toString(): String {
        return ("[$lexema : $categoria] ($fila:$columna)")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Token

        if (lexema != other.lexema) return false
        if (categoria != other.categoria) return false
        if (fila != other.fila) return false
        if (columna != other.columna) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lexema.hashCode()
        result = 31 * result + categoria.hashCode()
        result = 31 * result + fila
        result = 31 * result + columna
        return result
    }

    //TODO: Mira sammy puedes llenar esto xd
    fun getJavaCode():String{
        if(categoria == Categoria.IDENTIFICADOR){
            return "${lexema.substring(1)}"
        }
        else if (categoria == Categoria.PALABRA_RESERVADA){
            when(lexema){
                "caja" -> return "package"
                "cosa" -> return "class"
                "meter" -> return "import"
                "estrato1" -> return "public"
                "estrato6" -> return "private"
                "pal" -> return "String"
                "dec" -> return "double"
                "ent" -> return "int"
                "bit" -> return "char"
                "bip" -> return "boolean"
                "saltar" -> return "break"
                "devolver" -> return "return"
                "ciclo" -> return "for"
                "wi" -> return "if"
                "wo" -> return "else"
                "durante" -> return "while"
                "control" -> return "switch"
            }
        }
        else if(categoria == Categoria.OPERADOR_RELACIONAL){
            when (lexema){
                ">>" -> return ">"
                "<<" -> return "<"
                "<-" -> return "<="
                ">-" -> return ">="
                "¬-" -> return "!="
            }
        }
        else if(categoria == Categoria.OPERADOR_LOGICO){
            when(lexema){
                "^" -> return "&&"
                "~" -> return "||"
                "¬" -> return "!"
            }
        }
        else if(categoria == Categoria.OPERADOR_ARITMETICO){
            if (lexema == "°"){
                return "*"
            }
        }
        else if(categoria == Categoria.CADENA_CARACTERES){
            return lexema.replace("(", "\"").replace(")", "\"")
        }
        else if (categoria == Categoria.CARACTER){
            return lexema.replace( "\"" , "\'")
        }
        else if (categoria == Categoria.ENTERO || categoria == Categoria.REAL){
            return "${lexema.substring(1)}"
        }

        return lexema
    }
}