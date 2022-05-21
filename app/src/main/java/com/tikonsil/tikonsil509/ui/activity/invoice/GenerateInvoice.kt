package com.tikonsil.tikonsil509.ui.activity.invoice

import android.Manifest
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.text.TextPaint
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import com.tikonsil.tikonsil509.presentation.saveusersroom.UsersRoomViewModel
import com.tikonsil.tikonsil509.ui.activity.home.HomeActivity
import com.tikonsil.tikonsil509.utils.Constant
import com.tikonsil.tikonsil509.utils.Constant.Companion.DESCRIPTION
import com.tikonsil.tikonsil509.utils.Constant.Companion.SLOGAN
import java.io.File
import java.io.FileOutputStream


/** * Created by ISMOY BELIZAIRE on 08/05/2022. */
abstract class GenerateInvoice<VB:ViewBinding>:AppCompatActivity() {
 protected lateinit var binding: VB
 protected lateinit var mAuthProvider: AuthProvider
 protected val viewmodel by lazy { ViewModelProvider(this)[UsersRoomViewModel::class.java] }
 private val REQUEST_CODE:Int by lazy { 200 }
 protected var firstnamevinvoice:TextView?=null
 protected var lastnamevinvoice:TextView?=null
 protected var emailvinvoice:TextView?=null
 protected var rolevinvoice:TextView?=null
 protected var tiporecargavinvoice:TextView?=null
 protected var paisvinvoice:TextView?=null
 protected var subtotalvinvoice:TextView?=null
 protected var datevinvoice:TextView?=null
 protected var phonevinvoice:TextView?=null
 protected var descriptionvinvoice:TextView?=null
 protected var slogan:TextView?=null

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  binding = getActivityBinding()
  setContentView(binding.root)
  mAuthProvider = AuthProvider()
  firstnamevinvoice =binding.root.findViewById(R.id.nameuser)
  lastnamevinvoice = binding.root.findViewById(R.id.lastnameuser)
  emailvinvoice=binding.root.findViewById(R.id.emailuser)
  rolevinvoice = binding.root.findViewById(R.id.roleuser)
  tiporecargavinvoice = binding.root.findViewById(R.id.recargatypeuser)
  paisvinvoice = binding.root.findViewById(R.id.paisuser)
  subtotalvinvoice = binding.root.findViewById(R.id.subtotaluser)
  datevinvoice = binding.root.findViewById(R.id.dateuser)
  phonevinvoice = binding.root.findViewById(R.id.phoneuser)
  descriptionvinvoice = binding.root.findViewById(R.id.descripcionuser)
  slogan = binding.root.findViewById(R.id.slogan)
 }

 protected abstract fun getActivityBinding(): VB
/*
 fun generalpdf() {
  val pdfdocument = PdfDocument()
  val paint = Paint()
  val titulo = TextPaint()
  val subtitulo = TextPaint()
  val description = TextPaint()
  val pageinfo = PdfDocument.PageInfo.Builder(500,1024,1).create()
  val pagina1 = pdfdocument.startPage(pageinfo)
  val canvas = pagina1.canvas
  val bitmap = BitmapFactory.decodeResource(resources, R.drawable.logo)
  val bitmapscale = Bitmap.createScaledBitmap(bitmap,50,50,false)
  canvas.drawBitmap(bitmapscale,368f,20f,paint)
  titulo.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
  titulo.textSize = 20f
  canvas.drawText(Constant.TITLE,10f,150f,titulo)
  subtitulo.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
  subtitulo.textSize = 20f
  canvas.drawText(Constant.SUBTITLE,10f,150f,subtitulo)
  description.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
  description.textSize = 14f
  val arrdescription = DESCRIPTION.split("\n")
  var y = 200f
  for (item in arrdescription){
   canvas.drawText(DESCRIPTION,10f,y,description)
   y +=15
  }
  pdfdocument.finishPage(pagina1)

  try {
   pdfdocument.writeTo(FileOutputStream(getFilePath()))
   Toast.makeText(this, "Boleta Creado correctamente", Toast.LENGTH_SHORT).show()
  }catch (e:Exception){
  }
  pdfdocument.close()

 }

 private fun checkPermission():Boolean{
  val permission1 = ContextCompat.checkSelfPermission(applicationContext,
   Manifest.permission.WRITE_EXTERNAL_STORAGE
  )
  val permission2 = ContextCompat.checkSelfPermission(applicationContext,
   Manifest.permission.READ_EXTERNAL_STORAGE
  )
  return permission1 == PackageManager.PERMISSION_GRANTED && permission2== PackageManager.PERMISSION_GRANTED

 }

 private fun requestPermissions(){
  ActivityCompat.requestPermissions(this, arrayOf(
   Manifest.permission.WRITE_EXTERNAL_STORAGE,
   Manifest.permission.READ_EXTERNAL_STORAGE
  ),REQUEST_CODE)
 }

 override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
  super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  if (requestCode==REQUEST_CODE){
   if (grantResults.isNotEmpty()){
    val writeStorage = grantResults[0]== PackageManager.PERMISSION_GRANTED
    val readStorage = grantResults[0]== PackageManager.PERMISSION_GRANTED
    if (writeStorage && readStorage){
     Toast.makeText(this, "Permisos conseguido", Toast.LENGTH_SHORT).show()
    }else{
     Toast.makeText(this, "Permisos rechazados", Toast.LENGTH_SHORT).show()
    }

   }
  }
 }

  private fun getFilePath(): String? {
  val contextWrapper = ContextWrapper(applicationContext)
  val documentDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
  val file = File(documentDirectory, "BoletaTikonsil" + ".pdf")
  return file.path
 }*/
 fun showDataIntent(){
  slogan?.text = SLOGAN
  viewmodel.readAllData.observe(this, Observer {
   it.forEach { data->
    firstnamevinvoice?.text=data.firstname
    lastnamevinvoice?.text=data.lastname
    emailvinvoice?.text =data.email
    when(data.role){
     1->{
      rolevinvoice?.text= this.getString(R.string.agent)
     }else->{
     rolevinvoice?.text= this.getString(R.string.master)
     }
    }
    tiporecargavinvoice?.text =data.typerecharge
    paisvinvoice?.text =data.countryselected
    subtotalvinvoice?.text=data.subtotal
    datevinvoice?.text =data.date
    phonevinvoice?.text =data.phone
    descriptionvinvoice?.text =data.description
   }
  })

 }
}