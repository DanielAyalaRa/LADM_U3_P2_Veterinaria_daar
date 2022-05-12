package mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.clases


import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore


class Propietario (este:Context) {
    var baseRemota = FirebaseFirestore.getInstance()
    var coleccion = "propietario"
    var este = este
    var curp = ""
    var nombre = ""
    var telefono = ""
    var edad = 0

    fun insertar() {
        val datos = hashMapOf( // le pasamos los valores primarios que van a tener
            "curp" to curp,
            "nombre" to nombre,
            "telefono" to telefono,
            "edad" to edad
        )
        baseRemota.collection(coleccion) // es como una tabla
            .add(datos)
            .addOnSuccessListener {
                // SI SE PUDO
                alerta("EXITO! SI SE INSERTO")
            }
            .addOnFailureListener {
                // NO SE PUDO
                mensaje(it.message!!)}
    }

    fun eliminar(idSeleccionar : String) {
        baseRemota
            .collection(coleccion)
            .document(idSeleccionar)
            .delete()
            .addOnSuccessListener {
                alerta("SE ELIMINO CON EXITO")
            }
            .addOnFailureListener {
                mensaje("ERROR: ${it.message!!}")
            }
    }

    fun actualizar(idSeleccionar : String) {
        baseRemota
            .collection(coleccion)
            .document(idSeleccionar)
            .update("nombre", nombre,
                "telefono", telefono,
                "edad", edad)
            .addOnSuccessListener {
                alerta("SE ACTUALIZO CON EXITO")
            }
            .addOnFailureListener {
                mensaje("ERROR: ${it.message!!}")
            }
    }

    fun mostrarPropietario(idSeleccionar:String) : Propietario {
        var propietario = Propietario(este)
        baseRemota
            .collection(coleccion)
            .document(idSeleccionar)
            .get()
            .addOnSuccessListener {
                propietario.curp = it.getString("curp").toString()
                propietario.nombre = it.getString("nombre").toString()
                propietario.telefono = it.getString("telefono").toString()
                propietario.edad = it.getLong("edad").toString().toInt()
            }
            .addOnFailureListener {
                mensaje("ERROR: ${it.message!!}")
            }
        return propietario
    }

    /*fun buscarPropietario(nombreApeBuscar:String) : ArrayList<Propietario> {
        var baseDatos = DataBase(este, "propietario1", null, 1)
        err = ""
        var arreglo = ArrayList<Propietario>()
        var buscar = nombreApeBuscar

        try {
            var tabla = baseDatos.readableDatabase
            var SQL_SELECT = "SELECT * FROM PROPIETARIO WHERE NOMBRE LIKE '${buscar}%' OR NOMBRE LIKE '%${buscar}%'"

            var cursor = tabla.rawQuery(SQL_SELECT, null)
            if (cursor.moveToFirst()) {
                do {
                    val propietario = Propietario(este)
                    propietario.curp = cursor.getString(0)
                    propietario.nombre = cursor.getString(1)
                    propietario.telefono = cursor.getString(2)
                    propietario.edad = cursor.getString(3).toInt()
                    arreglo.add(propietario)
                } while (cursor.moveToNext())
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
        } finally {
            baseDatos.close()
        }
        return arreglo
    }

    fun mostrarFiltro(busqueda:String,filtro:String) : ArrayList<Propietario> {
        var baseDatos = DataBase(este, "propietario1", null, 1)
        err = ""
        var arreglo = ArrayList<Propietario>()
        var bus = busqueda
        var fil = filtro
        var SQL_SELECT = ""

        try {
            when (fil) {
                "CURP" -> {
                    SQL_SELECT = "SELECT * FROM PROPIETARIO WHERE CURP LIKE '${bus}%'"
                }
                "NOMBRE" -> {
                    SQL_SELECT = "SELECT * FROM PROPIETARIO WHERE NOMBRE LIKE '${bus}%' OR NOMBRE LIKE '%${bus}%'"
                }
                "TELEFONO" -> {
                    SQL_SELECT = "SELECT * FROM PROPIETARIO WHERE TELEFONO LIKE '${bus}%'"
                }
                "EDAD" -> {
                    SQL_SELECT = "SELECT * FROM PROPIETARIO WHERE EDAD LIKE '${bus}%'"
                }
            }
            var tabla = baseDatos.readableDatabase

            var cursor = tabla.rawQuery(SQL_SELECT, null)
            if (cursor.moveToFirst()) {
                do {
                    val propietario = Propietario(este)
                    propietario.curp = cursor.getString(0)
                    propietario.nombre = cursor.getString(1)
                    propietario.telefono = cursor.getString(2)
                    propietario.edad = cursor.getString(3).toInt()
                    arreglo.add(propietario)
                } while (cursor.moveToNext())
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
        } finally {
            baseDatos.close()
        }
        return arreglo
    }*/

    fun contenido() : String {
        return "CURP: ${curp}\nNOMBRE: ${nombre}\nTELEFONO: ${telefono}\nEDAD: ${edad}"
    }

    fun nombrePropietario() : String {
        return nombre+", "+telefono
    }

    private fun alerta(cadena :String) {
        Toast.makeText(este,cadena, Toast.LENGTH_LONG)
            .show()
    }

    private fun mensaje(error : String) {
        AlertDialog.Builder(este)
            .setMessage(error)
            .show()
    }
}