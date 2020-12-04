package hu.bme.aut.andwallet

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
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
            PieEntry(income.toFloat(), getString(R.string.income)),
            PieEntry(expense.toFloat(), getString(R.string.expenses))
        )

        val dataSet = PieDataSet(dataEntries, "")
        dataSet.colors = listOf(Color.GREEN, Color.RED)

        val data = PieData(dataSet)
        chartSummary.data = data
        chartSummary.isRotationEnabled = false
        chartSummary.description.text = getString(R.string.summary_of_incomes_and_expenses)
        chartSummary.description.textColor = getColor(R.color.light_grey)
        chartSummary.setEntryLabelColor(Color.BLACK)
        chartSummary.setEntryLabelTextSize(16f)
        val holeColor = TypedValue()
        theme.resolveAttribute(R.attr.backgroundColor, holeColor, true)
        chartSummary.setHoleColor(holeColor.data)

        chartSummary.invalidate()
    }
}