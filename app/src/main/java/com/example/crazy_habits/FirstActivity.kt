package com.example.crazy_habits

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FirstActivity : AppCompatActivity()    {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_first)

        val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val name : MutableList<String> = mutableListOf()
        val desc : MutableList<String> = mutableListOf()
        val type : MutableList<String> = mutableListOf()
        val priority : MutableList<String> = mutableListOf()
        val period : MutableList<String> = mutableListOf()
        var itemCount : Int = 0

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                fun getParameterHab(par : String) : String {
                    if (data?.getStringExtra(par).toString().isEmpty()) {
                        return "не указано"
                    }else return data?.getStringExtra(par).toString()
                }
                name.add(getParameterHab("name"))
                desc.add(getParameterHab("desc"))
                type.add(getParameterHab("type"))
                priority.add(getParameterHab("priority"))
                period.add(getParameterHab("period"))
                itemCount++
                recyclerView.adapter  = Adapter(itemCount, name, desc, type, priority, period)
            }
        }

        fun openActivityForResult() {
            val intent = Intent(this, SecondActivity::class.java)
            resultLauncher.launch(intent)
        }

        val fab: View = findViewById(R.id.FAB)
        fab.setOnClickListener { view ->
            openActivityForResult()
        }

    }


     override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
//        val counter1: TextView = findViewById(R.id.counter1)
//        counter1.text = (counter1.text.toString().toInt()+1).toString()
//        outState.putString(act1_data, counter1.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(TAG, "onRestoreInstanceState")
//        val counter1:TextView = findViewById(R.id.counter1)
//        counter1.text = savedInstanceState.getString(act1_data)
    }



    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
    }


    companion object {
        private const val TAG = "FirstActivity"
        private const val act1_data = "act1_data"

    }


    class Adapter (private val itemCount: Int,
                   private val name : MutableList<String>,
                   private val desc : MutableList<String>,
                   private val type : MutableList<String>,
                   private val priority : MutableList<String>,
                   private val period : MutableList<String>,
                   ): RecyclerView.Adapter<Adapter.ViewHolder>() {
        override fun getItemCount(): Int {
            return itemCount
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.ViewHolder {
            val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_view, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: Adapter.ViewHolder, position: Int) {
            holder?.nameHabit?.text = name[position]
            holder?.desc?.text      = desc[position]
            holder?.type?.text      = type[position]
            holder?.priority?.text  = priority[position]
            holder?.period?.text    = period[position]
        }

        class ViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!){
            var nameHabit : TextView? = null
            var desc : TextView? = null
            var type : TextView? = null
            var priority : TextView? = null
            var period : TextView? = null
            init{
                nameHabit = itemView?.findViewById(R.id.nameHabit)
                desc      = itemView?.findViewById(R.id.description)
                type      = itemView?.findViewById(R.id.type)
                priority  = itemView?.findViewById(R.id.priority)
                period    = itemView?.findViewById(R.id.period)
            }
        }
    }
}


