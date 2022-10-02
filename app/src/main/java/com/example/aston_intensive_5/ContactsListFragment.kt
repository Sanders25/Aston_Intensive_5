package com.example.aston_intensive_5

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import com.example.aston_intensive_5.data.Contact
import com.example.aston_intensive_5.data.ContactsRepo
import com.example.aston_intensive_5.databinding.ContactsListBinding
import java.io.Serializable

const val TO_DESC = "TO_DESC"
const val FROM_DESC = "FROM_DESC"
const val NAME = "NAME"
const val INITIALS = "INITIALS"
const val NUMBER = "NUMBER"
const val COLOR = "COLOR"
const val CONTACTS_LIST_STATE = "CONTACTS_LIST_STATE"
const val LAST_EDITED_INDEX_STATE = "LAST_EDITED_INDEX_STATE"
const val DESCRIPTION_STATE = "DESCRIPTION_STATE"
const val FRAGMENT_TAG = "NEW_FRAGMENT"


class ContactsListFragment : Fragment(R.layout.contacts_list) {

    private var _binding: ContactsListBinding? = null
    private val binding get() = _binding!!

    private var contacts = ContactsRepo().getContacts().toMutableList()

    private var lastEdited: Contact? = null
    private var lastEditedIndex: Int? = null

    private var descriptionFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            contacts =
                savedInstanceState.getSerializable(CONTACTS_LIST_STATE) as MutableList<Contact>
            lastEditedIndex = savedInstanceState.getInt(LAST_EDITED_INDEX_STATE)
        }

        setFragmentResultListener(FROM_DESC) { _, bundle ->
            lastEditedIndex?.let {
                updateContacts(bundle, it)
            }
        }
    }

    private fun updateContacts(bundle: Bundle, index: Int) {
        val layout = binding.root

        contacts.removeAt(index)
        contacts.add(
            index, Contact(
                name = bundle.getString(RETURNED_NAME).toString(),
                number = bundle.getString(RETURNED_NUMBER).toString()
            )
        )
        layout.removeAllViewsInLayout()
        loadContacts()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && isTablet(requireContext())) {
            descriptionFragment =
                parentFragmentManager.getFragment(savedInstanceState, DESCRIPTION_STATE)
            openDescription(contacts[lastEditedIndex!!], lastEditedIndex!!)
        }
            loadContacts()
    }

    private fun loadContacts() {
        contacts.forEachIndexed { index, it ->
            val item = ContactsListItem(requireContext())
            item.populate(it)
            item.onContactClick = {
                openDescription(it, index)
            }
            binding.contactsContainer.addView(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openDescription(contact: Contact, index: Int) {
        val bundle = Bundle()
        lastEdited = contact
        lastEditedIndex = index

        bundle.putString(NAME, contact.name)
        bundle.putString(INITIALS, contact.initials)
        bundle.putString(NUMBER, contact.number)
        bundle.putInt(COLOR, contact.backgroundColor)

        setFragmentResult(TO_DESC, bundle)

        parentFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            if (isTablet(requireContext())) {
                replace<ContactDescription>(R.id.details_fragment_container)
                addToBackStack(FRAGMENT_TAG)
            } else {
                replace<ContactDescription>(R.id.master_fragment_container)
                addToBackStack(null)
            }
        }
        descriptionFragment = parentFragmentManager.findFragmentByTag(FRAGMENT_TAG)
    }

    private fun isTablet(context: Context): Boolean {
        return ((context.resources.configuration.screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        lastEditedIndex?.let {
            outState.putInt(LAST_EDITED_INDEX_STATE, it)
        }
        outState.putSerializable(CONTACTS_LIST_STATE, contacts as Serializable)
        descriptionFragment?.let {
            parentFragmentManager.putFragment(outState, DESCRIPTION_STATE, it)
        }
    }
}