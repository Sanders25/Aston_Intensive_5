package com.example.aston_intensive_5

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.aston_intensive_5.data.Contact
import com.example.aston_intensive_5.databinding.ContactsListItemBinding


class ContactsListItem @JvmOverloads constructor(
    context:Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var _binding: ContactsListItemBinding? = null
    private val binding get() = _binding!!
    private var touchType = -1

    var onContactClick: () -> Unit = {}

    private lateinit var contact: Contact

    init {
        _binding = ContactsListItemBinding.inflate(LayoutInflater.from(context), this)
    }

    fun populate(_contact: Contact) {
        contact = _contact
        binding.name.text = _contact.name
        binding.number.text = _contact.number
        binding.initials.text = _contact.initials
        val background = ContextCompat.getDrawable(context, R.drawable.rounded_textview)
        binding.initials.background = background
        background?.setTint(_contact.backgroundColor)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent?): Boolean {
        val value = super.onTouchEvent(e)

        when(e?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchType = 1
                return true
            }
            MotionEvent.ACTION_UP -> {
                when (touchType) {
                    1 -> onContactClick()
                }
            }
        }
        return value
    }
}