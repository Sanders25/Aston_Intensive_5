package com.example.aston_intensive_5

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<ContactsListFragment>(R.id.master_fragment_container)
            }
        setContentView(R.layout.activity_main)
    }
}