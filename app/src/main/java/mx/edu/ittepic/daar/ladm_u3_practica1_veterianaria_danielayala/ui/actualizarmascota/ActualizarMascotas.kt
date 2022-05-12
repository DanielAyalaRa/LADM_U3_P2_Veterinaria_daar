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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActualizarMascotasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id_mascota = this.intent.extras!!.getString("mascotaActualizar")!!

        val spinner: Spinner = binding.SpRaza
        ArrayAdapter.createFromResource(
            this,
            R.array.razas,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        baseRemota
            .collection("mascota")
            .document(id_mascota)
            .get()
            .addOnSuccessListener {
                binding.txtcurpMascota.setText(it.getString("curp"))
                binding.txtnombreMascota.setText(it.getString("nombre"))
                mostrarPropietario(it.getString("curp").toString())
            }
            .addOnFailureListener {
                mensaje("ERROR: ${it.message!!}")
            }

        binding.actualizar.setOnClickListener {
            var mascota = Mascota(this)

            try {
                mascota.nombre = binding.txtnombreMascota.text.toString()
                mascota.raza = binding.SpRaza.selectedItem.toString()
                mascota.curp = binding.txtcurp.text.toString()
            } catch (e:Exception) {
                mensaje("HAY CAMPOS VACIOS")
                return@setOnClickListener
            }

            try {
                if (!(mascota.curp == "" || mascota.nombre == "")) {
                    mascota.actualizar(id_mascota)
                    limpiarCampos()
                    finish()
                }
            }  catch(e:Exception) {
                mensaje("HAY CAMPOS VACIOS")
            }
        }
    }

    private fun mostrarPropietario(curp : String) {
        baseRemota
            .collection(coleccion1)
            .whereEqualTo("curp", curp)
            .addSnapshotListener { query, error ->
                if (error != null) {
                    mensaje(error.message!!)
                    return@addSnapshotListener
                }
                for (documento in query!!) {
                    binding.txtcurp.setText(documento.getString("curp").toString())
                    binding.txtnombrePropietario.setText(documento.getString("nombre").toString())
                    binding.txttelefono.setText(documento.getString("telefono").toString())
                    binding.txtedadPropietario.setText(documento.getLong("edad").toString())
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
}