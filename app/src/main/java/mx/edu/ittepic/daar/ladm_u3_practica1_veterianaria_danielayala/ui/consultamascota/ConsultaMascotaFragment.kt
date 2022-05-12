package mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.ui.consultamascota

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
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.clases.Mascota
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.clases.Propietario
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.databinding.ConsultaMascotaFragmentBinding
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.databinding.ConsultaPropietarioFragmentBinding
import mx.edu.ittepic.daar.ladm_u3_practica1_veterianaria_danielayala.ui.consultapropietario.ConsultaPropietarioViewModel

class ConsultaMascotaFragment : Fragment() {

    private var _binding: ConsultaMascotaFragmentBinding? = null

    private val binding get() = _binding!!
    var baseRemota = FirebaseFirestore.getInstance()
    var coleccion2 = "mascota"
    var datalista = ArrayList<String>()
    var listaId = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(ConsultaMascotaViewModel::class.java)

        _binding = ConsultaMascotaFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val spinner: Spinner = binding.SpConsultasMascotas
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.buscarMascota,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        baseRemota
            .collection(coleccion2)
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
                        "Nombre: ${documento.getString("nombre")} -- Raza: ${
                            documento.getString(
                                "raza"
                            )
                        }\nPropietario: ${documento.getString("curp")}"
                    datalista.add(cadena)

                    listaId.add(documento.id.toString())
                }
            }

        binding.lista.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,datalista)

        binding.btnBuscar.setOnClickListener {
            var busqueda = binding.buscarMascota.text.toString()

            mostrarFiltro(busqueda,binding.SpConsultasMascotas.selectedItem.toString())
        }

        return  root
    }

    fun mostrarFiltro(busqueda:String,filtro:String) {
            baseRemota.collection(coleccion2)
                .whereEqualTo("${filtro}", busqueda)
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
                            "Nombre: ${documento.getString("nombre")} -- Raza: ${
                                documento.getString(
                                    "raza"
                                )
                            }\nPropietario: ${documento.getString("curp")}"
                        datalista.add(cadena)

                        listaId.add(documento.id.toString())
                    }

                    binding.lista.adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        datalista
                    )
                }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun mensaje2(error : String) {
        AlertDialog.Builder(requireContext())
            .setMessage(error)
            .show()
    }

}