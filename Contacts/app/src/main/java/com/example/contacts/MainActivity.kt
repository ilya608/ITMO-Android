package com.example.contacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.jar.Manifest


//data class User(val firstName: String, val lastName: String)

//val usersList = (0..30).map {
//    User("First name #$it", "Last name #$it")
//}

data class Contact(val name: String, val phoneNumber: String)

class ContactViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
    fun bind(contact: Contact) {
        with(root) {
            name.text = contact.name
            phone.text = contact.phoneNumber
        }
    }
}

fun Context.fetchAllContacts(): List<Contact> {
    contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        null
    )
        .use { cursor ->
            if (cursor == null) return emptyList()
            val builder = ArrayList<Contact>()
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                        ?: "N/A"
                val phoneNumber =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        ?: "N/A"

                builder.add(Contact(name, phoneNumber))
            }
            return builder
        }
}

class ContactAdapter(
    private val users: List<Contact>,
    private val onClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        var holder = ContactViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        )
        holder.root.setOnClickListener {
            onClick(users[holder.adapterPosition])
        }
        return holder
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) =
        holder.bind(users[position])
}


class MainActivity : AppCompatActivity() {

    val myRequestId = 39
    val contactList = ArrayList<Contact>()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            myRequestId -> {
                // grantResults пуст, если пользователь отменил диалог
                // (но не согласился или отказался)
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    processReceivedPermission()
                } else {
                    Toast.makeText(applicationContext, "Необходимо разрешение для просмотра контактов", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }


    private fun checkPermission() {

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.READ_CONTACTS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity, // Контекст
                arrayOf(android.Manifest.permission.READ_CONTACTS), // Что спрашиваем
                myRequestId
            ) // Пользовательская константа для уникальности запроса
        } else {
            processReceivedPermission()
            return

        }
    }

    private fun processReceivedPermission() {
        contactList.clear()
        contactList.addAll(fetchAllContacts())
        myRecyclerView.adapter?.notifyDataSetChanged()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewManager = LinearLayoutManager(this)
        myRecyclerView.layoutManager = viewManager
        checkPermission()
//        checkPermission()
        val contactAdapter = ContactAdapter(contactList) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${it.phoneNumber}")
            startActivity(intent)
        }
        myRecyclerView.adapter = contactAdapter
//        myRecyclerView.apply {
//            layoutManager = viewManager
//            adapter = contactAdapter
//        }


//        val viewManager = LinearLayoutManager(this)
//        myRecyclerView.apply {
//            layoutManager = viewManager
//            adapter = UserAdapter(usersList) {
//                Toast.makeText(
//                    this@MainActivity,
//                    "Clicked on user $it!",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }


    }
}
