package hu.bme.aut.andwallet

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.andwallet.adapter.TransactionAdapter
import hu.bme.aut.andwallet.data.ApplicationDatabase
import hu.bme.aut.andwallet.data.Transaction
import hu.bme.aut.andwallet.data.Wallet
import hu.bme.aut.andwallet.fragments.TransactionDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(),
                     TransactionAdapter.TransactionClickListener,
                     TransactionDialogFragment.TransactionDialogListener {
    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: TransactionAdapter
    private lateinit var database: ApplicationDatabase
    private var editIndex = -1
    companion object {
        const val KEY_EDIT = "KEY_EDIT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fab.setOnClickListener {
            TransactionDialogFragment().show(
                supportFragmentManager,
                TransactionDialogFragment.TAG
            )
        }
        database = ApplicationDatabase.getInstance(this)
        thread {
            val transactions = database.applicationDao().getAll()
            val piggyBank = database.applicationDao().getPiggyBank()
            Wallet.init(transactions, piggyBank)
            runOnUiThread {
                updateBalanceTextView()
            }
        }
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        updateBalanceTextView()
    }

    private fun updateBalanceTextView() {
        balanceTextView.text = getString(R.string.balance_amount, Wallet.balance)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_saving ->  {
                startActivity(Intent(this, PiggyBankActivity::class.java))
                true
            }
            R.id.action_see_monthly_summary -> {
                startActivity(Intent(this, MonthlySummaryActivity::class.java))
                true
            }
            R.id.action_remove_all -> {
                AlertDialog.Builder(this)
                    .setMessage(R.string.are_you_sure_want_to_remove_all_items)
                    .setPositiveButton(R.string.ok) { _, _ -> removeAllItems() }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun removeAllItems() {
        thread {
            database.applicationDao().deleteAllItems()
            Wallet.init(piggyBank = database.applicationDao().getPiggyBank())
            runOnUiThread {
                adapter.removeAllItems()
                updateBalanceTextView()
            }
        }
    }

    private fun initRecyclerView() {
        recyclerView = MainRecyclerView
        adapter  = TransactionAdapter(this)
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.applicationDao().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onItemRemoved(position: Int) {
        val item = adapter.get(position)
        thread {
            database.applicationDao().deleteItem(item)
            Wallet.remove(item.amount)
            updateBalanceTextView()
        }
    }

    override fun showEditDialog(item: Transaction, position: Int) {
        editIndex = position
        val editDialog = TransactionDialogFragment()

        val  bundle = Bundle()
        bundle.putSerializable(KEY_EDIT, item)
        editDialog.arguments = bundle
        editDialog.show(supportFragmentManager, "TransactionDialogFragment_edit")
    }

    override fun onTransactionCreated(newItem: Transaction) {
        thread {
            val newId = database.applicationDao().insert(newItem)
            val newTransaction = newItem.copy(
                id = newId
            )
            runOnUiThread {
                adapter.addItem(newTransaction)
                Wallet.add(newTransaction.amount)
                updateBalanceTextView()
            }
        }
    }

    override fun onTransactionUpdated(item: Transaction) {
        thread {
            database.applicationDao().update(item)
            Wallet.init(transactions = adapter.getAll())
            runOnUiThread {
                adapter.updateItem(item, editIndex)
                updateBalanceTextView()
            }
        }
    }
}