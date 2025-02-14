package com.pascal.ecommercecompose.ui.screen.report

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.pascal.ecommercecompose.R
import com.pascal.ecommercecompose.data.local.entity.ProductEntity
import com.pascal.ecommercecompose.data.local.repository.LocalRepository
import com.pascal.ecommercecompose.data.prefs.PreferencesLogin
import com.pascal.ecommercecompose.data.repository.firebase.FirebaseRepository
import com.pascal.ecommercecompose.domain.base.Resource
import com.pascal.ecommercecompose.utils.calculateTotalPrice
import com.pascal.ecommercecompose.utils.getCurrentFormattedDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.io.FileOutputStream

class ReportViewModel(
    private val database: LocalRepository,
    private val firebaseAuthRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUIState())
    val uiState get() = _uiState.asStateFlow()

    suspend fun loadReport(product: List<ProductEntity>?) {
        _uiState.update { it.copy(isLoading = true) }

        when (val result = firebaseAuthRepository.addTransaction(product)) {
            is Resource.Success -> {
                Log.d("tag report", result.data)
            }

            is Resource.Error -> {
               Log.e("tag report", result.exception.message.toString())
            }
        }
    }

    suspend fun deleteCart() {
        try {
            database.deleteCart()
        } catch (e: Exception) {
            Log.e("tag report", e.message.toString())
        }
    }

    fun generatePdfAndOpen(context: Context, product: List<ProductEntity>?) {
        _uiState.update { it.copy(isLoading = true) }

        try {
            val pref = PreferencesLogin.getLoginResponse(context)
            val pdfFile = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                "Report_${getCurrentFormattedDate()}.pdf"
            )

            val pdfWriter = PdfWriter(FileOutputStream(pdfFile))
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)

            val drawable = ContextCompat.getDrawable(context, R.drawable.logo)
            val bitmap = (drawable as BitmapDrawable).bitmap
            val logoImage = Image(ImageDataFactory.create(bitmapToByteArray(bitmap)))
                .setWidth(250f)
                .setHeight(100f)

            val tableHeader = Table(floatArrayOf(1f, 3f))
            tableHeader.setWidth(UnitValue.createPercentValue(100f))
            tableHeader.addCell(Cell().add(logoImage).setBorder(Border.NO_BORDER))
            tableHeader.addCell(
                Cell().add(Paragraph("Transaction Report").setBold().setFontSize(18f))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBorder(Border.NO_BORDER)
            )

            document.add(tableHeader)
            document.add(Paragraph("\n"))

            document.add(Paragraph("General Information").setBold().setFontSize(14f))
            document.add(Paragraph("Name: ${pref?.name ?: "No Name"}"))
            document.add(Paragraph("Email: ${pref?.email ?: "No Email"}"))

            document.add(Paragraph("\nTransaction").setBold().setFontSize(14f))

            product?.forEach {
                document.add(Paragraph("Product Name: ${it.name}"))
                document.add(Paragraph("Product Qty: ${it.qty}"))
            }

            document.add(Paragraph("\nTotal Amount").setBold().setFontSize(14f))
            document.add(Paragraph(calculateTotalPrice(product ?: emptyList())))


            document.close()

            openPdf(context, pdfFile)
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = true,
                    message = e.message.toString()
                )
            }
            e.printStackTrace()
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = java.io.ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun openPdf(context: Context, file: File) {
        try {
            _uiState.update { it.copy(isLoading = false) }

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/pdf")
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY

            context.startActivity(Intent.createChooser(intent, "Buka PDF dengan:"))
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = true,
                    message = e.message.toString()
                )
            }
            e.printStackTrace()
        }
    }

    fun setError(bool: Boolean) {
        _uiState.update { it.copy(isError = bool) }
    }

    override fun onCleared() {
        super.onCleared()
        _uiState.update { it.copy(isReport = false) }
    }
}