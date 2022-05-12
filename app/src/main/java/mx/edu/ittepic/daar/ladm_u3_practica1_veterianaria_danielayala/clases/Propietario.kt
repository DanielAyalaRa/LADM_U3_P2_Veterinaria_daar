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