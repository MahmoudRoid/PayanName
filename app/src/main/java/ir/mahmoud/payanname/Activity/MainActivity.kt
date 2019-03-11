package ir.mahmoud.payanname.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import ir.mahmoud.payanname.Classes.JavaUtils
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
            try {
                val intent = Intent(this, CollectDataService::class.java)
                intent.action = CollectDataService.ACTION_STOP_FOREGROUND_SERVICE
                startService(intent)
            } catch (e: Exception) {
            }
        }
        ////// algorithm
        start_btn_algorithm.setOnClickListener {
                        val intent = Intent(this, AlgorithmService::class.java)
                        intent.action = AlgorithmService.ACTION_START_FOREGROUND_SERVICE
                        startService(intent)
        }
        stop_btn_algorithm.setOnClickListener {
              try {
                   val intent = Intent(this, AlgorithmService::class.java)
                   intent.action = AlgorithmService.ACTION_STOP_FOREGROUND_SERVICE
                   startService(intent)
               } catch (e: Exception) {
               }
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

                try {// stop after 5 min
                    Handler().postDelayed( {
                        val intent = Intent(this, CounterService::class.java)
                        intent.action = CounterService.ACTION_STOP_FOREGROUND_SERVICE
                        startService(intent)
                        Toast.makeText(this,"ended",Toast.LENGTH_SHORT).show()
                    }, 1000 * 60 * 5)
                } catch (e: Exception) {
                }

        }
        stop_btn_counter.setOnClickListener {
            try {
                val intent = Intent(this, CounterService::class.java)
                intent.action = CounterService.ACTION_STOP_FOREGROUND_SERVICE
                startService(intent)
            } catch (e: Exception) {
            }
        }
        get_data_btn_counter.setOnClickListener {


            var liissst: MutableList<String> = CounterService.result.split("\n").map { it -> it.trim() }.toMutableList()
            var sum = 0f
            for(i in liissst){
                if (i != "")
                    sum += i.toFloat()
            }

            sum /= counter_edt.text.toString().toInt()

            result_txt.text = CounterService.result + "sum is $sum"

        }

        //////  test cores
        btn_core_0.setOnClickListener {
            setCurrentFreq(counter_edt.text.toString(),0)
        }
        btn_core_1.setOnClickListener {
            setCurrentFreq(counter_edt.text.toString(),1)
        }
        btn_core_2.setOnClickListener {
            setCurrentFreq(counter_edt.text.toString(),2)
        }
        btn_core_3.setOnClickListener {
            setCurrentFreq(counter_edt.text.toString(),3)
        }

    }

    fun setCore(coreNumber: Int, value: Boolean) {
        val fileName = "/sys/devices/system/cpu/cpu$coreNumber/online"
        val governor = "/sys/devices/system/cpu/cpu$coreNumber/cpufreq/scaling_governor"
        val one = """"1""""
        val zero = """"0""""

        if (value){
            // set core on
            Runtime.getRuntime().exec(arrayOf("su", "-c", "echo $one> $fileName "))
            // set userspace
            Runtime.getRuntime().exec(arrayOf("su", "-c", "echo userspace> $governor "))
        }
        else{
            Runtime.getRuntime().exec(arrayOf("su", "-c", "echo ondemand> $governor "))
            Runtime.getRuntime().exec(arrayOf("su", "-c", "echo $zero> $fileName "))
        }
    }

    fun setCurrentFreq(frequency: String, core:Int) {
        // all of the cores
        val fileName = "/sys/devices/system/cpu/cpu$core/cpufreq/scaling_setspeed"
        Runtime.getRuntime().exec(arrayOf("su", "-c", "echo $frequency > $fileName"))
    }




}
