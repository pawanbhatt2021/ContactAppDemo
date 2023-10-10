package com.contactapp.contactappdemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.contactapp.contactappdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private var contactList:MutableLiveData<MutableList<String>> = MutableLiveData()

    val registerActivityForResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                contactList.postValue(getContacts())
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contactList.postValue(getContacts())

        contactList.observe(this){
            it?.let {
                binding.rvGetContacts.adapter=ContactAdapter(it.toList())
        }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("Recycle", "Range")
    private fun getContacts(): MutableList<String> {
        val list = mutableListOf<String>()
        sdkAboveOreo {
            isPermissionGranted(this, android.Manifest.permission.READ_CONTACTS) {
                if (it) {
                    val contentResolver = applicationContext.contentResolver
                    val cursor = contentResolver.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                    )
                    if (cursor?.moveToFirst() == true) {
                        do {
                            val name =
                                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                            val number =
                                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                            list.add(name)
                            list.add(number)
                        } while (cursor.moveToNext())
                    }
                } else {
                    registerActivityForResult.launch(android.Manifest.permission.READ_CONTACTS)
                }
            }
        }
        return list
    }

    private inline fun sdkAboveOreo(call: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            call.invoke()
        }
    }

    private inline fun isPermissionGranted(
        context: Context,
        permission: String,
        call: (Boolean) -> Unit
    ) {
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            call.invoke(true)
        } else {
            call(false)
        }
    }
}