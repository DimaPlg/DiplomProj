package com.example.fitnesappmember.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.fitnesappmember.R
import com.example.fitnesappmember.databinding.FragmentAddMemberBinding
import com.example.fitnesappmember.global.DB
import com.example.fitnesappmember.global.MyFunction
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import java.util.*

class FragmentAddMember : Fragment() {

    var db: DB?=null
    var oneMonth:String? = ""
    var twoMonths:String? = ""
    var treeMonths:String? = ""
    var sixMonths:String? = ""
    var oneYear:String? = ""
    private var gender = "Male"
    private lateinit var binding: FragmentAddMemberBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddMemberBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = activity?.let { DB(it) }

        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener{view1, year, monthOfYear, dayOfMonth ->


            cal.set(Calendar.YEAR,year)
            cal.set(Calendar.MONTH,monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH,dayOfMonth)

            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
            binding.edtJoining.setText(sdf.format(cal.time))
        }

        binding.spMemberShip.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                val value = binding.spMemberShip.selectedItem.toString().trim()

                if(value =="Select"){
                    binding.edtExpire.setText("")
                    calculateTotal(binding.spMemberShip,binding.edtDiscount,binding.edtAmount)
                }else {
                    if (binding.edtJoining.text.toString().trim().isEmpty()) {
                        if(value =="1 Month"){
                            calculateExpireDate(1,binding.edtExpire)
                            calculateTotal(binding.spMemberShip,binding.edtDiscount,binding.edtAmount)
                        }else if(value =="2 Months"){
                            calculateExpireDate(2,binding.edtExpire)
                            calculateTotal(binding.spMemberShip,binding.edtDiscount,binding.edtAmount)
                        }else if(value =="3 Months"){
                            calculateExpireDate(3,binding.edtExpire)
                            calculateTotal(binding.spMemberShip,binding.edtDiscount,binding.edtAmount)
                        }else if(value =="6 Months"){
                            calculateExpireDate(6,binding.edtExpire)
                            calculateTotal(binding.spMemberShip,binding.edtDiscount,binding.edtAmount)
                        }else if(value =="1 Year"){
                            calculateExpireDate(12,binding.edtExpire)
                            calculateTotal(binding.spMemberShip,binding.edtDiscount,binding.edtAmount)
                        }

                    } else {
                        showToast("Select Joining date first")
                        binding.spMemberShip.setSelection(0)

                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        binding.edtDiscount.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if(p0!=null){
                    calculateTotal(binding.spMemberShip,binding.edtDiscount,binding.edtAmount)
                }
            }

        })

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when(id){
                R.id.rdMale ->{
                    gender = "Male"
                }
                R.id.rdFemale ->{
                    gender = "Female"
                }
            }
        }

        binding.btnAddMemberSave.setOnClickListener {
            if (validate()){

            }

        }

        binding.imgPicDate.setOnClickListener{
            activity?.let{ it1 -> DatePickerDialog(it1,dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()}
        }

        binding.imgTakeImage.setOnClickListener{}

        getFee()
    }


    private  fun getFee(){
        try {
            val sqlQuery = "SELECT * FROM FEE WHERE ID ='1'"
            db?.fireQuery(sqlQuery)?.use {
                oneMonth = MyFunction.getValue(it,"ONE_MONTH")
                twoMonths = MyFunction.getValue(it, "TWO_MONTH")
                treeMonths = MyFunction.getValue(it, "THREE_MONTH")
                sixMonths = MyFunction.getValue(it, "SIX_MONTH")
                oneYear = MyFunction.getValue(it, "ONE_YEAR")
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun calculateTotal(spMember:Spinner,edtDis:EditText,edtAmt:EditText){
        val month = spMember.selectedItem.toString().trim()
        var discount = edtDis.text.toString().trim()
        if(edtDis.text.toString().toString().isEmpty()){
            discount = "0"
        }
        if (month =="Select"){
            edtAmt.setText("")
        }else if(month == "1 Month"){
            if(discount.trim().isEmpty()){
                discount = "0"
            }

            if(oneMonth!!.trim().isNotEmpty()){
                val discountAmount = ((oneMonth!!.toDouble() * discount.toDouble())/100)
                val total = oneMonth!!.toDouble() - discountAmount
                edtAmt.setText(total.toString())
            }
        }else if(month == "2 Month"){
            if(discount.trim().isEmpty()){
                discount = "0"
            }

            if(twoMonths!!.trim().isNotEmpty()){
                val discountAmount = ((twoMonths!!.toDouble() * discount.toDouble())/100)
                val total = twoMonths!!.toDouble() - discountAmount
                edtAmt.setText(total.toString())
            }
        }else if(month == "3 Month"){
            if(discount.trim().isEmpty()){
                discount = "0"
            }

            if(treeMonths!!.trim().isNotEmpty()){
                val discountAmount = ((treeMonths!!.toDouble() * discount.toDouble())/100)
                val total = treeMonths!!.toDouble() - discountAmount
                edtAmt.setText(total.toString())
            }
        }else if(month == "6 Month"){
            if(discount.trim().isEmpty()){
                discount = "0"
            }

            if(sixMonths!!.trim().isNotEmpty()){
                val discountAmount = ((sixMonths!!.toDouble() * discount.toDouble())/100)
                val total = sixMonths!!.toDouble() - discountAmount
                edtAmt.setText(total.toString())
            }
        }else if(month == "1 Year"){
            if(discount.trim().isEmpty()){
                discount = "0"
            }

            if(oneYear!!.trim().isNotEmpty()){
                val discountAmount = ((oneYear!!.toDouble() * discount.toDouble())/100)
                val total = oneYear!!.toDouble() - discountAmount
                edtAmt.setText(total.toString())
            }
        }
    }
    @SuppressLint("SimpleDateFormat")
    private fun calculateExpireDate(month:Int, edtExpiry:EditText){
        val dtStart = binding.edtJoining.text.toString().trim()
        if (dtStart.isEmpty()){
            val format = SimpleDateFormat("dd/MM/yyyy")
            val date = format.parse(dtStart)
            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.MONTH,month)

            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
            edtExpiry.setText(sdf.format(cal.time))
        }
    }
    private fun showToast(value: String){
        Toast.makeText(activity,value, Toast.LENGTH_LONG).show()
    }

    private  fun  validate():Boolean{
        if (binding.edtFirstName.text.toString().trim().isEmpty()){
            showToast("Enter first name")
            return false
        }else if (binding.edtLastName.text.toString().trim().isEmpty()){
            showToast("Enter last name")
            return false
        }else if (binding.edtAge.text.toString().trim().isEmpty()){
            showToast("Enter age ")
            return false
        }else if (binding.edtMobile.text.toString().trim().isEmpty()){
            showToast("Enter mobile number")
            return false
        }
        return  true
    }

    private fun saveDate(){
        try {
            val sqlQuery = "INSERT OR REPLACE INTO MEMBER(ID,FIRST_NAME,LAST_NAME,GENDER,AGE,"+
                    "WEIGHT,MOBILE,ADDRESS,DATE_OF_JOINING,MEMBERSHIP,EXPIRE_ON,DISCOUNT,TOTAL,STATUS)VALUES"+
                    "('"+getIncrementedId()+"',"DatabaseUtils.sqlEscapeString(binding.edtFirstName.text.toString().trim())+")"

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun getIncrementedId():String{
        var incrementId = ""
        try {
            val sqlQuery = "SELECT MAX(ID)+1 AS ID FROM MEMBER"
            db?.fireQuery(sqlQuery)?.use{
                if (it.count>0) {
                    incrementId = MyFunction.getValue(it, "ID")
                }
            }

        }catch (e:Exception){
            e.printStackTrace()
        }

        return incrementId
    }

   /* private fun getImage(){
        val items:Array<CharSequence>
        try{

            items = arrayOf("Take Photo", "Choose  Image", "Cancel")
            val builder = android.app.AlertDialog.Builder(activity)
            builder.setTitle("Select Image")
            builder.setItems(items) { dialogInterface, i ->
                if(items[i] == "Take Photo"){
                    RuntimePermission
                }

            })

        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    */
}