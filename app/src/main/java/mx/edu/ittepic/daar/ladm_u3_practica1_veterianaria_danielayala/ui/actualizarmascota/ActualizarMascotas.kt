package mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.ui.actualizarmascota

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.R
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.clases.Mascota
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.clases.Propietario
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.databinding.ActivityActualizarMainBinding
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.databinding.ActivityActualizarMascotasBinding

class ActualizarMascotas : AppCompatActivity() {

    lateinit var binding: ActivityActualizarMascotasBinding
    var baseRemota = FirebaseFirestore.getInstance()
    var coleccion1 = "propietario"
    var id_mascota = ""
    var curp = ""
    var datalista = ArrayList<String>()
    var listaId = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActualizarMascotasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id_mascota = this.intent.extras!!.getString("mascotaActualizar")!!
        curp = this.intent.extras!!.getString("mascotaCurp")!!

        val spinner: Spinner = binding.SpRaza
        ArrayAdapter.createFromResource(
            this,
            R.array.razas,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        var id = mostrarFiltro(curp)

        /*baseRemota
            .collection(coleccion1)
            .document(id)
            .get()
            .addOnSuccessListener {
                binding.txtcurp.setText(it.getString("curp").toString())
                binding.txtnombrePropietario.setText(it.getString("nombre").toString())
                binding.txttelefono.setText(it.getString("telefono").toString())
                binding.txtedadPropietario.setText(it.getLong("edad").toString())
            }
            .addOnFailureListener {
                mensaje("ERROR: ${it.message!!}")
            }*/

        baseRemota
            .collection("mascota")
            .document(id_mascota)
            .get() //Obtiene 1 documento
            .addOnSuccessListener {
                binding.txtcurpMascota.setText(it.getString("curp"))
                binding.txtnombreMascota.setText(it.getString("nombre"))
            }
            .addOnFailureListener {
                mensaje("ERROR: ${it.message!!}")
            }

        binding.actualizar.setOnClickListener {
            var propietario = Propietario(this)

            try {
                propietario.curp = binding.txtcurp.text.toString()
                propietario.nombre = binding.txtnombrePropietario.text.toString()
                propietario.telefono = binding.txttelefono.text.toString()
                propietario.edad = binding.txtedadPropietario.text.toString().toInt()
            } catch (e:Exception) {
                mensaje("HAY CAMPOS VACIOS")
                return@setOnClickListener
            }

            try {
                if (!(propietario.curp == "" || propietario.nombre == "" || propietario.telefono == "" || propietario.edad.toString() == "")) {
                    propietario.actualizar(id_mascota)
                    limpiarCampos()
                    finish()
                }
            }  catch(e:Exception) {
                mensaje("HAY CAMPOS VACIOS")
            }
        }
    }

    private fun mensaje2(titulo : String,error : String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(error)
            .setNeutralButton("ACEPTAR") {d,i -> }
            .show()
    }

    private fun mensaje(error : String) {
        AlertDialog.Builder(this)
            .setMessage(error)
            .show()
    }

    fun limpiarCampos() {
        binding.txtcurp.setText("")
        binding.txtnombrePropietario.setText("")
        binding.txttelefono.setText("")
        binding.txtedadPropietario.setText("")
    }

    fun mostrarFiltro(busqueda:String) : String {
        var cadena = ""
            baseRemota.collection(coleccion1)
                .whereEqualTo("curp", busqueda)
                .addSnapshotListener { query, error ->
                    if (error != null) {
                        //SI HUBO UNA EXCEPCIÓN
                        mensaje(error.message!!)
                        return@addSnapshotListener
                    }
                    datalista.clear()
                    listaId.clear()
                    for (documento in query!!) {
                        cadena = documento.id.toString()
                    }
                    mensaje(cadena)
                }
        return  cadena
    }
}