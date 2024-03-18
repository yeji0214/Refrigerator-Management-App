package com.example.refrigerator_management_app_kotlin

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.refrigerator_management_app_kotlin.databinding.ActivityRefrigeratorBinding
import com.google.firebase.database.*

class RefrigeratorActivity : AppCompatActivity() {
    private companion object {
        const val NOTIFICATION_PERMISSION_CODE = 100
    }

    private var userUid: String? = null

    private lateinit var activityRefrigeratorBinding: ActivityRefrigeratorBinding
    private lateinit var informationDialog: InformationDialog
    private lateinit var databaseReference: DatabaseReference
    private lateinit var refrigeratorRecyclerView: RecyclerView
    private lateinit var refrigeratorAdapter: RefrigeratorAdapter
    private lateinit var refrigeratorViewModel: RefrigeratorViewModel
    private lateinit var addRefrigeratorDialog: AddRefrigeratorDialog
    private lateinit var addButton: ImageView
    private lateinit var logoutButton: ImageView

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // ActionBar를 숨깁니다.
        activityRefrigeratorBinding = ActivityRefrigeratorBinding.inflate(layoutInflater)
        setContentView(activityRefrigeratorBinding.root)

        val intent = intent
        userUid = intent.getStringExtra("userUid")

        databaseReference = FirebaseDatabase.getInstance().reference

        // 초기화
        refrigeratorViewModel = ViewModelProvider(this).get(RefrigeratorViewModel::class.java)
        refrigeratorRecyclerView = activityRefrigeratorBinding.mainRecyclerView
        addButton = activityRefrigeratorBinding.mainAddButton
        logoutButton = activityRefrigeratorBinding.mainLogOutButton

        refrigeratorAdapter = RefrigeratorAdapter(this@RefrigeratorActivity, refrigeratorViewModel,
            userUid.toString()
        )
        refrigeratorRecyclerView.adapter = refrigeratorAdapter
        refrigeratorRecyclerView.layoutManager = GridLayoutManager(this, 2)
        refrigeratorRecyclerView.setHasFixedSize(true)
        //refrigeratorRecyclerView.addItemDecoration(RefrigeratorRecyclerviewDeco(40))

        // ViewModel 설정
        refrigeratorViewModel = ViewModelProvider(this).get(RefrigeratorViewModel::class.java)
        refrigeratorViewModel.setUserId(userUid.toString())

        // RecyclerView 설정
        //refrigeratorRecyclerView.adapter = refrigeratorAdapter
        refrigeratorRecyclerView.layoutManager = GridLayoutManager(this, 2)
        refrigeratorRecyclerView.setHasFixedSize(true)
        //refrigeratorRecyclerView.addItemDecoration(RefrigeratorRecyclerviewDeco(40))

        val userObserver = Observer<ArrayList<String>> { strings ->
            refrigeratorAdapter.notifyDataSetChanged()
        }
        refrigeratorViewModel.refrigeratorsLiveData.observe(this, userObserver)
        registerForContextMenu(refrigeratorRecyclerView)

        // addButton 클릭
        addButton.setOnClickListener {
            addRefrigeratorDialog = AddRefrigeratorDialog(this@RefrigeratorActivity, refrigeratorViewModel, -1, userUid.toString())
            addRefrigeratorDialog.show()
        }

        // logoutButton 클릭
        logoutButton.setOnClickListener {
            informationDialog = InformationDialog(this@RefrigeratorActivity, 2)
            informationDialog.show()
        }

        showRefrigeratorList()
        checkPermission(Manifest.permission.POST_NOTIFICATIONS, NOTIFICATION_PERMISSION_CODE)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit -> {
                val position: Int = refrigeratorViewModel.getPosition()
                addRefrigeratorDialog = AddRefrigeratorDialog(this@RefrigeratorActivity, refrigeratorViewModel, position, userUid.toString())
                addRefrigeratorDialog.show()
                true
            }
            R.id.deleteCategory -> {
                val itemName: String = refrigeratorViewModel.getRefrigeratorName(refrigeratorViewModel.getPosition()).toString()
                databaseReference.child("UserAccount").child(userUid!!).child("냉장고").child(itemName).removeValue()
                refrigeratorViewModel.deleteRefrigerator(refrigeratorViewModel.getPosition())
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        informationDialog = InformationDialog(this@RefrigeratorActivity, 3)
        informationDialog.show()
    }

    private fun showRefrigeratorList() {
        databaseReference.child("UserAccount").child(userUid!!).child("냉장고").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                refrigeratorViewModel.addRefrigerator(snapshot.key!!)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@RefrigeratorActivity, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this@RefrigeratorActivity, arrayOf(permission), requestCode)
        } else {
            Toast.makeText(this@RefrigeratorActivity, "Permission already Granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@RefrigeratorActivity, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@RefrigeratorActivity, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}