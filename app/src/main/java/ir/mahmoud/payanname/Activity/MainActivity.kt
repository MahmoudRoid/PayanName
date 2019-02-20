package ir.mahmoud.payanname.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import ir.mahmoud.payanname.Classes.JavaUtils
import ir.mahmoud.payanname.Classes.NewAlgorithm
import ir.mahmoud.payanname.Enum.Language
import ir.mahmoud.payanname.R
import ir.mahmoud.payanname.Service.AlgorithmService
import ir.mahmoud.payanname.Service.CollectDataService
import ir.mahmoud.payanname.Service.CounterService
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var root: Process
    var count = 8
    val language: Language = Language.C
    var  test = NewAlgorithm()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toobar) as Toolbar
        setSupportActionBar(toolbar)

        try {
            root = Runtime.getRuntime().exec("su")
        }
        catch (e: IOException) {
            e.printStackTrace()
        }

        toolbar.setOnClickListener {
            // temperature
            toolbar.title = JavaUtils.getInstance().getCpuTemperature_Xperia()
        }

        /////// collect data
        start_btn_collect_data.setOnClickListener {
            val intent = Intent(this, CollectDataService::class.java)
            intent.action = CollectDataService.ACTION_START_FOREGROUND_SERVICE
            startService(intent)
        }
        stop_btn_collect_data.setOnClickListener {
            val intent = Intent(this, CollectDataService::class.java)
            intent.action = CollectDataService.ACTION_STOP_FOREGROUND_SERVICE
            startService(intent)
        }
        ////// algorithm
        start_btn_algorithm.setOnClickListener {
            val intent = Intent(this, AlgorithmService::class.java)
            intent.action = AlgorithmService.ACTION_START_FOREGROUND_SERVICE
            startService(intent)
        }
        stop_btn_algorithm.setOnClickListener {
            val intent = Intent(this, AlgorithmService::class.java)
            intent.action = AlgorithmService.ACTION_STOP_FOREGROUND_SERVICE
            startService(intent)
        }
        ////// counter
        counter_edt.setText(count.toString())
        counter_edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isNotEmpty())
                    count = s.toString().toInt()
                else
                    count = 8
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        start_btn_counter.setOnClickListener {
            val intent = Intent(this, CounterService::class.java)
            intent.action = CounterService.ACTION_START_FOREGROUND_SERVICE
            intent.putExtra("threadCount",counter_edt.text.toString().toInt())
            intent.putExtra("Language",language.toString())
            startService(intent)
        }
        stop_btn_counter.setOnClickListener {
            val intent = Intent(this, CounterService::class.java)
            intent.action = CounterService.ACTION_STOP_FOREGROUND_SERVICE
            startService(intent)
        }
        get_data_btn_counter.setOnClickListener {
            result_txt.text = CounterService.result
        }

        //////  test cores
        btn_core_0.setOnClickListener { test.start(0,counter_edt.text.toString()) }
        btn_core_1.setOnClickListener { test.start(1,counter_edt.text.toString()) }
        btn_core_2.setOnClickListener { test.start(2,counter_edt.text.toString()) }
        btn_core_3.setOnClickListener {
//            test.start(3,counter_edt.text.toString())
            test.getCpuUtilizationWithCpuFreq(this)
        }


    }
}
