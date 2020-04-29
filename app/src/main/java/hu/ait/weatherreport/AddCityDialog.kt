package hu.ait.weatherreport

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import hu.ait.weatherreport.data.City
import kotlinx.android.synthetic.main.city_dialog.view.*


class AddCityDialog : DialogFragment() {

    interface CityHandler{
        fun cityCreated(city: City)
    }

    lateinit var cityHandler: CityHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is CityHandler){
            cityHandler = context
        } else {
            throw RuntimeException(
                "The Activity is not implementing the CityHandler interface.")
        }
    }

    lateinit var etCityText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle("City name")
        val dialogView = requireActivity().layoutInflater.inflate(
            R.layout.city_dialog, null
        )

        etCityText = dialogView.etCityText
        dialogBuilder.setView(dialogView)
        dialogBuilder.setPositiveButton("Ok") {
                _, _ ->
        }

        dialogBuilder.setNegativeButton("Cancel") {
                _, _ ->
        }

        return dialogBuilder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etCityText.text.isNotEmpty()) {
                handleTodoCreate()
                dialog!!.dismiss()
            } else {
                etCityText.error = "This field can not be empty"
            }
        }
    }

    private fun handleTodoCreate() {
        cityHandler.cityCreated(
            City(
                null,
                etCityText.text.toString()
            )
        )
    }

}