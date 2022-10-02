package com.example.aston_intensive_5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.aston_intensive_5.data.Contact
import com.example.aston_intensive_5.databinding.ContactDescriptionBinding

private const val STATE_NAME = "STATE_NAME"
private const val STATE_INITIALS = "STATE_INITIALS"
private const val STATE_NUMBER = "STATE_NUMBER"
private const val STATE_COLOR = "STATE_COLOR"

const val RETURNED_NAME = "RETURNED_NAME"
const val RETURNED_INITIALS = "RETURNED_INITIALS"
const val RETURNED_NUMBER = "RETURNED_NUMBER"
const val RETURNED_COLOR = "RETURNED_COLOR"

class ContactDescription : Fragment(R.layout.contact_description) {
    private var _binding: ContactDescriptionBinding? = null
    private val binding get() = _binding!!

    private lateinit var name: String
    private lateinit var initials: String
    private lateinit var number: String
    private var color: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(TO_DESC) { _, bundle ->
            name = bundle.getString(NAME).toString()
            initials = bundle.getString(INITIALS).toString()
            number = bundle.getString(NUMBER).toString()
            color = bundle.getInt(COLOR)
            populateDescription()
            parentFragmentManager.clearFragmentResultListener(TO_DESC)
        }
    }

    private fun populateDescription() {
        val btnBackground = ContextCompat.getDrawable(requireContext(), R.drawable.btn_shape)
        btnBackground?.setTint(color)
        binding.buttonSave.background = btnBackground
        binding.name.setText(name)
        binding.avatar.text = initials
        binding.number.setText(number)
        val avatarBackground =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_textview)
        avatarBackground?.setTint(color)
        binding.avatar.background = avatarBackground
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactDescriptionBinding.inflate(inflater, container, false)
        val view = binding.root
        if (savedInstanceState != null) {
            name = savedInstanceState.getString(STATE_NAME).toString()
            initials = savedInstanceState.getString(STATE_INITIALS).toString()
            number = savedInstanceState.getString(STATE_NUMBER).toString()
            color = savedInstanceState.getInt(STATE_COLOR)
            populateDescription()
        }

        binding.buttonSave.setOnClickListener {
            saveChanges()
        }
        return view
    }

    private fun saveChanges() {
        val bundle = Bundle()

        val newContact = Contact(
            name = binding.name.text.toString(),
            number = binding.number.text.toString()
        )

        bundle.putString(RETURNED_NAME, newContact.name)
        bundle.putString(RETURNED_INITIALS, newContact.initials)
        bundle.putString(RETURNED_NUMBER, newContact.number)
        bundle.putInt(RETURNED_COLOR, newContact.backgroundColor)

        parentFragmentManager.setFragmentResult(FROM_DESC, bundle)
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (this::name.isInitialized) {
            outState.putString(STATE_NAME, name)
            outState.putString(STATE_INITIALS, initials)
            outState.putString(STATE_NUMBER, number)
            outState.putInt(STATE_COLOR, color)
        }
    }
}