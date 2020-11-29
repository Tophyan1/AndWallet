package hu.bme.aut.andwallet

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import hu.bme.aut.andwallet.data.ApplicationDatabase
import hu.bme.aut.andwallet.data.Transaction
import kotlinx.android.synthetic.main.activity_monthly_summary.*
import kotlin.concurrent.thread

class MonthlySummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_summary)
        loadData()
    }

    private lateinit var transactions: List<Transaction>

    private fun loadData() {
        thread {
            transactions = ApplicationDatabase.getInstance(this).applicationDao().getAll()
            runOnUiThread { makeChart() }
        }
    }

    private fun makeChart() {
        var expense = 0
        var income = 0
        for (transaction in transactions) {
            if (transaction.amount < 0) expense -= transaction.amount
            else income += transaction.amount
        }

        val dataEntries = listOf(
            PieEntry(income.toFloat(), "Income"),
            PieEntry(expense.toFloat(), "Expenses")
        )

        val dataSet = PieDataSet(dataEntries, "")
        dataSet.colors = listOf(Color.GREEN, Color.RED)

        val data = PieData(dataSet)
        chartSummary.data = data
        chartSummary.isRotationEnabled = false
        chartSummary.description.text = "Summary of incomes and expenses"
        chartSummary.invalidate()
    }
}