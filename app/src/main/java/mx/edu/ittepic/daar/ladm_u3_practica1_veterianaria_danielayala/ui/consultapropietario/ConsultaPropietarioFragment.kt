package mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.ui.consultapropietario

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.R
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.clases.Propietario
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.databinding.ConsultaPropietarioFragmentBinding
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.ui.actualizarpropietario.ActualizarMainActivity

class ConsultaPropietarioFragment : Fragment() {

    private var _binding: ConsultaPropietarioFragmentBinding? = null

    private val binding get() = _binding!!
    var baseRemota = FirebaseFirestore.getInstance()
    var coleccion1 = "propietario"
    var datalista = ArrayList<String>()
    var listaId = ArrayList<String>()

    var listaIDs = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(ConsultaPropietarioViewModel::class.java)

        _binding = ConsultaPropietarioFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val spinner: Spinner = binding.SpConsultasPropietario
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.buscarProPor,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }



        binding.btnBuscar.setOnClickListener {
            var busqueda = binding.txtbuscar.text.toString()

            if (busqueda == "") {
                //mostrarDatosEnListView()
            } else {
                //mostrarPropietariosFiltro(busqueda,binding.SpConsultasPropietario.selectedItem.toString())
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun mostrarTodos() {
        baseRemota
            .collection(coleccion1)
            .addSnapshotListener { query, error ->
                if (error != null) {
                    //SI HUBO UNA EXCEPCIÓN
                    mensaje2(error.message!!)
                    return@addSnapshotListener
                }
                datalista.clear()
                listaId.clear()
                for (documento in query!!) {
                    var cadena =
                        "Nombre: ${documento.getString("nombre")} -- Telefono: ${
                            documento.getString(
                                "telefono"
                            )
                        }-- Edad: ${documento.getLong("edad")}"
                    datalista.add(cadena)

                    listaId.add(documento.id.toString())
                }
            }

        binding.listaConsultaPropietario.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,datalista)
    }

    /*
    fun mostrarPropietariosFiltro(busqueda:String, filtro:String) {
        var listaFiltro = Propietario(requireContext()).mostrarFiltro(busqueda,filtro)
        var listaData = ArrayList<String>()
        var DataPropietario = Propietario(requireContext())

        listaIDs.clear()
        (0..listaFiltro.size-1).forEach {
            val persona = listaFiltro.get(it)
            DataPropietario.curp = persona.curp
            DataPropietario.nombre = persona.nombre
            DataPropietario.telefono = persona.telefono
            DataPropietario.edad = persona.edad

            listaData.add(DataPropietario.contenido())
            listaIDs.add(persona.curp)
        }

        binding.listaConsultaPropietario.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,listaData)
    }*/

    private fun mensaje2(error : String) {
        AlertDialog.Builder(requireContext())
            .setMessage(error)
            .show()
    }

}