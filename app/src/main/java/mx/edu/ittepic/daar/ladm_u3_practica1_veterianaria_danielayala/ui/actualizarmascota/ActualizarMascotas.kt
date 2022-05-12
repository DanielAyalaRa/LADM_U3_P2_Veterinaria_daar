package mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.ui.actualizarmascota

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.R
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.clases.Mascota
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.clases.Propietario
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.databinding.ActivityActualizarMainBinding
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.databinding.ActivityActualizarMascotasBinding

class ActualizarMascotas : AppCompatActivity() {

    lateinit var binding: ActivityActualizarMascotasBinding
    var id_mascota = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActualizarMascotasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id_mascota = this.intent.extras!!.getString("mascotaActualizar")!!

        /*var mascota = Mascota(this).mostrarMascota(id_mascota)
        var propietario = Propietario(this).mostrarPropietario(mascota.curp)

        val spinner: Spinner = binding.SpRaza
        ArrayAdapter.createFromResource(
            this,
            R.array.razas,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        binding.txtcurp.setText(propietario.curp)
        binding.txtnombrePropietario.setText(propietario.nombre)
        binding.txttelefono.setText(propietario.telefono)
        binding.txtedadPropietario.setText(propietario.edad.toString())
        binding.txtid.setText(mascota.id_mascota)
        binding.txtnombreMascota.setText(mascota.nombre)
        binding.txtcurpMascota.setText(mascota.curp)

        binding.actualizar.setOnClickListener {
            var mascota = Mascota(this)

            try {
                mascota.id_mascota = binding.txtid.text.toString()
                mascota.nombre = binding.txtnombreMascota.text.toString()
                mascota.raza = binding.SpRaza.selectedItem.toString()
                mascota.curp = binding.txtcurpMascota.text.toString()
            } catch (e:Exception) {
                AlertDialog.Builder(this)
                    .setTitle("ATENCIÓN")
                    .setMessage("HAY CAMPOS VACIOS")
                    .show()
                return@setOnClickListener
            }

            var respuesta = mascota.actualizar()

            if (respuesta) {
                Toast.makeText(this, "SE ACTUALIZO CON EXITO", Toast.LENGTH_LONG)
                    .show()
                limpiarCampos()
                // quitar teclado virtual
                val view = this.currentFocus
                view?.let { v ->
                    val imm =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(v.windowToken, 0)
                }
                finish()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("ERROR")
                    .setMessage("NO SE PUDO ACTUALIZAR")
                    .show()
                finish()
            }
        }*/
    }

    fun limpiarCampos() {
        binding.txtcurp.setText("")
        binding.txtnombrePropietario.setText("")
        binding.txttelefono.setText("")
        binding.txtedadPropietario.setText("")
    }
}