package com.example.pdfgenerator

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pdfgenerator.databinding.ActivityMainBinding
import com.itextpdf.text.*
import com.itextpdf.text.html.WebColors
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    val mainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    var pageHeight = 1120
    var pageWidth = 792

    lateinit var bitmap: Bitmap
    lateinit var scaledBitmap: Bitmap

    lateinit var userList: ArrayList<UserData>
    lateinit var file: File

    var PERMISSION_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        userList = arrayListOf()

        file = File(Environment.getExternalStorageDirectory(), "Lorna.pdf")

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.love)
        scaledBitmap = Bitmap.createScaledBitmap(bitmap, 140, 140, false)


        if (checkPermissions()){
            Toast.makeText(this, "Permission Granted...", Toast.LENGTH_SHORT).show()
        }else{
            requestPermission()
        }

        mainBinding.generatePdf.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){
                generatePDF()
            }
        }
        mainBinding.generatePdf2.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){
                generatePDF2()
            }
        }
    }

    private fun generatePDF2() {
        val colorWhite = WebColors.getRGBColor("#ffffff")
        val colorBlue = WebColors.getRGBColor("#056FAA")
        val grayColor = WebColors.getRGBColor("#425066")

        for (i in 1..100){
            userList.add(UserData("ndieted@gmail.com","0797877878", "Houses/Apartments to Rent","Kamamega", "2 bedroom", "Ksh. 20,000"))
        }


        val white = Font(Font.FontFamily.HELVETICA, 15.0f, Font.BOLD, colorWhite)
        val output = FileOutputStream(file)
        val document = Document(PageSize.A4)
        val table = PdfPTable(floatArrayOf(6f, 20f, 20f, 20f, 20f, 20f, 20f))
        table.defaultCell.horizontalAlignment = Element.ALIGN_CENTER
        table.defaultCell.fixedHeight = 50f
        table.totalWidth = PageSize.A4.width
        table.widthPercentage = 100f
        table.defaultCell.verticalAlignment = Element.ALIGN_LEFT

        val noText = Chunk("No.", white)
        val noCell = PdfPCell(Phrase(noText))
        noCell.fixedHeight = 50f
        noCell.horizontalAlignment = Element.ALIGN_CENTER
        noCell.verticalAlignment = Element.ALIGN_CENTER

        val emailText = Chunk("Email", white)
        val emailCell = PdfPCell(Phrase(emailText))
        emailCell.fixedHeight = 50f
        emailCell.horizontalAlignment = Element.ALIGN_CENTER
        emailCell.verticalAlignment = Element.ALIGN_CENTER

        val phoneText = Chunk("Phone", white)
        val phoneCell = PdfPCell(Phrase(phoneText))
        phoneCell.fixedHeight = 50f
        phoneCell.horizontalAlignment = Element.ALIGN_CENTER
        phoneCell.verticalAlignment = Element.ALIGN_CENTER

        val typeText = Chunk("Type", white)
        val typeCell = PdfPCell(Phrase(typeText))
        typeCell.fixedHeight = 50f
        typeCell.horizontalAlignment = Element.ALIGN_CENTER
        typeCell.verticalAlignment = Element.ALIGN_CENTER

        val locationText = Chunk("Location", white)
        val locationCell = PdfPCell(Phrase(locationText))
        locationCell.fixedHeight = 50f
        locationCell.horizontalAlignment = Element.ALIGN_CENTER
        locationCell.verticalAlignment = Element.ALIGN_CENTER

        val bedroomText = Chunk("Bedroom", white)
        val bedroomCell = PdfPCell(Phrase(bedroomText))
        bedroomCell.fixedHeight = 50f
        bedroomCell.horizontalAlignment = Element.ALIGN_CENTER
        bedroomCell.verticalAlignment = Element.ALIGN_CENTER

        val priceText = Chunk("Price", white)
        val priceCell = PdfPCell(Phrase(priceText))
        priceCell.fixedHeight = 50f
        priceCell.horizontalAlignment = Element.ALIGN_CENTER
        priceCell.verticalAlignment = Element.ALIGN_CENTER


        val footerText = Chunk("SN - Copyright @ 2022")
        val footCell = PdfPCell(Phrase(footerText))
        footCell.fixedHeight = 70f
        footCell.horizontalAlignment = Element.ALIGN_CENTER
        footCell.verticalAlignment = Element.ALIGN_CENTER
        footCell.colspan = 4


        table.addCell(noCell)
        table.addCell(emailCell)
        table.addCell(phoneCell)
        table.addCell(typeCell)
        table.addCell(locationCell)
        table.addCell(bedroomCell)
        table.addCell(priceCell)
        table.headerRows = 1

        val cells = table.getRow(0).cells


        for (cell in cells) {
            cell.backgroundColor = grayColor
        }
        for (i in 0 until userList.size) {
            val pay = userList.get(i)
            val id = (i + 1).toString()
            val email: String = pay.userEmail
            val phone: String = pay.phone
            val type: String = pay.type
            val location: String = pay.location
            val bedroom: String = pay.bedroom
            val price: String = pay.price
            table.addCell("$id. ")
            table.addCell(email)
            table.addCell(phone)
            table.addCell(type)
            table.addCell(location)
            table.addCell(bedroom)
            table.addCell(price)
        }

        val footTable = PdfPTable(floatArrayOf(6f, 25f, 20f, 20f))
        footTable.totalWidth = PageSize.A4.width
        footTable.widthPercentage = 100f
        footTable.addCell(footCell)

        PdfWriter.getInstance(document, output)
        document.open()
        val g = Font(Font.FontFamily.HELVETICA, 25.0f, Font.NORMAL, grayColor)
        document.add(Paragraph(" Enquiries\n\n", g))
        document.add(table)
        document.add(footTable)

        document.close()

        finish()

    }

    private fun generatePDF() {
        var pdfDocument = PdfDocument()

        var paint = Paint()
        var title = Paint()

        var myPageInfo: PdfDocument.PageInfo? = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        var canvas: Canvas = myPage.canvas
        canvas.drawBitmap(scaledBitmap, 56F, 40F, paint)

        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        title.textSize = 15F
        title.color = ContextCompat.getColor(this, R.color.purple_200)

        canvas.drawText("Lorna Muturi.", 209F, 100F, title)
        canvas.drawText("Msupa,", 209F, 80F, title)

        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.color = ContextCompat.getColor(this, R.color.purple_200)
        title.textSize = 15F

        title.textAlign = Paint.Align.CENTER
        canvas.drawText("Check your phone for a virus. With Love :)", 396F, 560F, title)

        pdfDocument.finishPage(myPage)


        try {
            pdfDocument.writeTo(FileOutputStream(file))

            Toast.makeText(this, "Hacked :)", Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            e.printStackTrace()

            Toast.makeText(this, "Generation failed", Toast.LENGTH_SHORT).show()
        }
        pdfDocument.close()


    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), PERMISSION_CODE)
    }

    private fun checkPermissions(): Boolean {
        var writeStoragePermission = ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
        var readStoragePermission = ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)

        return writeStoragePermission == PackageManager.PERMISSION_GRANTED && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_CODE){
            if (grantResults.isNotEmpty()){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted...", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Permission Denied...", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}