package mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.clases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore

class Mascota (este:Context) {
    var baseRemota = FirebaseFirestore.getInstance()
    var coleccion = "mascota"
    var este = este
    var nombre = ""
    var raza = ""
    var curp = ""

    fun insertar() {
        val datos = hashMapOf( // le pasamos los valores primarios que van a tener
            "nombre" to nombre,
            "raza" to raza,
            "curp" to curp

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
                "raza", raza,
                "curp", curp)
            .addOnSuccessListener {
                alerta("SE ACTUALIZO CON EXITO")
            }
            .addOnFailureListener {
                mensaje("ERROR: ${it.message!!}")
            }
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

    fun nombreMascota() : String {
        return nombre+", "+raza+", "+curp
    }
}