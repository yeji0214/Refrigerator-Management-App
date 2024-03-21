package com.example.refrigerator_management_app_kotlin

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
//import com.google.mlkit.vision.common.InputImage
//import com.google.mlkit.vision.text.Text
//import com.google.mlkit.vision.text.TextRecognition
//import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.text.SimpleDateFormat
import java.util.*

class AddItemFragment : Fragment() {
//    private val itemFragment = 1
//    private val itemInformationFragment = 2
//    private val addItemFragment = 3
//    private val calendarFragment = 4
//    private val ITEM_IMAGE = 10
//    private val EXPIRATION_DATE_IMAGE = 11
//    private lateinit var itemActivity: ItemActivity
//    private lateinit var addItemDialog: AddItemDialog
//    private lateinit var addItemBtn: ImageView
//    private lateinit var returnBtn: ImageView
//    private lateinit var shootItemImageView: ImageView
//    private lateinit var shootExpirationDateBtn: ImageView
//    private lateinit var viewModel: ItemViewModel
//    private lateinit var allItems: ArrayList<Item>
//    private lateinit var recognizer: TextRecognizer
//    private lateinit var uri: Uri
//    private lateinit var bitmap: Bitmap
//    private lateinit var inputImage: InputImage
//    private var itemBitmap: Bitmap? = null
//    private lateinit var itemInputImage: InputImage
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_add_item, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        /* ---------- 초기화 ---------- */
//        itemActivity = activity as ItemActivity
//        viewModel = (activity as ItemActivity).viewModel
//        allItems = (activity as ItemActivity).allItems
//        addItemBtn = view.findViewById(R.id.addItemBtn)
//        returnBtn = view.findViewById(R.id.returnBtn)
//        shootItemImageView = view.findViewById(R.id.shootItemImageView)
//        shootExpirationDateBtn = view.findViewById(R.id.shootExpirationDateImageView)
//        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
//
//        /* ---------- 리스너 ---------- */
//        addItemBtn.setOnClickListener { addItemBtnClick(it) }
//        returnBtn.setOnClickListener { returnBtnClick(it) }
//        shootItemImageView.setOnClickListener { shootItemImageViewClick(it) }
//        shootExpirationDateBtn.setOnClickListener { shootExpirationDataBtnClick(it) }
//
//        // Uri exposure 무시
//        val builder = StrictMode.VmPolicy.Builder()
//        StrictMode.setVmPolicy(builder.build())
//
//        val r = (Math.random() * 5).toInt()
//        when (r) {
//            0 -> shootItemImageView.setImageResource(R.drawable.food)
//            1 -> shootItemImageView.setImageResource(R.drawable.bakery)
//            2 -> shootItemImageView.setImageResource(R.drawable.fruit)
//            3 -> shootItemImageView.setImageResource(R.drawable.vegetable)
//            4 -> shootItemImageView.setImageResource(R.drawable.groceries)
//            5 -> shootItemImageView.setImageResource(R.drawable.liquor)
//            6 -> shootItemImageView.setImageResource(R.drawable.drink)
//        }
//    }
//
//    // "추가하기" 버튼 클릭 -> 추가하겠냐는 다이얼로그 생성
//    private fun addItemBtnClick(v: View) {
//        itemActivity.currentFragment = itemFragment
//        val newNameEditText = requireView().findViewById<EditText>(R.id.newNameEditText)
//        val newExpirationDateEditText = requireView().findViewById<EditText>(R.id.newExpirationDateEditText)
//        val newMemoEditText = requireView().findViewById<EditText>(R.id.newMemoEditText)
//        val newName = newNameEditText.text.toString()
//        val newExpirationDate = newExpirationDateEditText.text.toString()
//        val newMemo = newMemoEditText.text.toString()
//        addItemDialog = AddItemDialog(requireContext(), viewModel, allItems, newName, newExpirationDate, newMemo)
//        addItemDialog.show()
//    }
//
//    // "유통기한 촬영" 버튼 클릭 -> 카메라로 사진 찍기
//    private fun shootExpirationDataBtnClick(v: View) {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (intent.resolveActivity(requireContext().packageManager) != null) {
//            startActivityForResult(intent, EXPIRATION_DATE_IMAGE)
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == EXPIRATION_DATE_IMAGE && resultCode == RESULT_OK) {
//            Log.e("onActivityResult", "유통기한 촬영")
//            val extras = data?.extras
//            bitmap = extras?.get("data") as Bitmap
//            inputImage = InputImage.fromBitmap(bitmap, 0)
//            itemActivity.currentFragment = addItemFragment
//            textRecognition(recognizer) // 찍은 사진으로 유통기한 인식
//        }
//        if (requestCode == ITEM_IMAGE && resultCode == RESULT_OK) {
//            Log.e("onActivityResult", "이미지 촬영")
//            val extras = data?.extras
//            itemBitmap = extras?.get("data") as Bitmap?
//            itemInputImage = InputImage.fromBitmap(itemBitmap, 0)
//            itemActivity.currentFragment = addItemFragment
//            setItemImage(recognizer)
//        }
//    }
//
//    private fun textRecognition(recognizer: TextRecognizer) {
//        val result = recognizer.process(inputImage)
//            .addOnSuccessListener { text ->
//                val resultText = text.text // 인식한 텍스트
//                val str = getDateString(resultText)
//                Log.e("AddItemFragment", "인식한 텍스트 $resultText")
//                val newExpirationDateEditText = itemActivity.findViewById<EditText>(R.id.newExpirationDateEditText)
//                newExpirationDateEditText.setText(str)
//                shootItemImageView = itemActivity.findViewById<ImageView>(R.id.shootItemImageView)
//                shootItemImageView.setImageBitmap(bitmap)
//            }
//            .addOnFailureListener { }
//    }
//
//    private fun getDateString(value: String): String {
//        var str = value.replace("[^0-9]".toRegex(), "")
//        var index = 0
//        if ((str.indexOf('2')) != -1) {
//            str = str.substring(index, str.length)
//        }
//        if (!str.startsWith("2")) {
//            return "null"
//        }
//        if (str.startsWith("20") && Integer.parseInt(str.substring(2, 4)) < 13) {
//            str = "20$str"
//        }
//        str = if (!str.startsWith("20")) "20$str" else str
//        if (str.substring(0, 8).length != 8) {
//            return "null"
//        }
//        val year = str.substring(0, 4)
//        val month = str.substring(4, 6)
//        val date = str.substring(6, 8)
//        str = "$year-$month-$date"
//        return str
//    }
//
//    // item 촬영 버튼 클릭
//    private fun shootItemImageViewClick(v: View) {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (intent.resolveActivity(requireContext().packageManager) != null) {
//            startActivityForResult(intent, ITEM_IMAGE)
//        }
//    }
//
//    private fun setItemImage(recognizer: TextRecognizer) {
//        val result = recognizer.process(itemInputImage)
//            .addOnSuccessListener { text ->
//                shootItemImageView = itemActivity.findViewById(R.id.shootItemImageView)
//                shootItemImageView.setImageBitmap(itemBitmap)
//            }
//            .addOnFailureListener { }
//    }
//
//    // "돌아가기" 버튼 클릭 -> ItemFragment로 이동
//    private fun returnBtnClick(v: View) {
//        (activity as ItemActivity).changeFragment(itemFragment)
//    }
//
//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            AddItemFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}

