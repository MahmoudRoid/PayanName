package ir.mahmoud.payanname.Classes

import android.content.Context
import android.util.Log
import android.widget.Toast
import io.realm.Realm
import ir.mahmoud.payanname.Model.Model
import java.io.*
import java.util.*

class NewAlgorithm {

    var normalStatus = true
    var isOk: Boolean = false

   init{
        // set governor for core 0
        setGovernor_Core_0()
    }

    //////////////////////////////////////////////

    fun setGovernor_Core_0(){
        val governor = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor"
        Runtime.getRuntime().exec(arrayOf("su", "-c", "echo userspace> $governor "))
    }
    fun setPermissions(coreNumber: Int) {
        var process: Process
        val path_1 = "/sys/devices/system/cpu/cpu$coreNumber/cpufreq/scaling_min_freq"
        val path_2 = "/sys/devices/system/cpu/cpu$coreNumber/cpufreq/scaling_max_freq"
        val path_3 = "/sys/devices/system/cpu/cpu$coreNumber/cpufreq/scaling_setspeed"
        try {
            process = Runtime.getRuntime().exec(arrayOf("su", "-c", "chmod 777 $path_1 $path_2 $path_3"))
            val s = Scanner(process.errorStream).useDelimiter("\\A")
            Log.e("stderr", if (s.hasNext()) s.next() else "")
            process.waitFor()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    fun setCurrentFreq(coreNumber: Int = 0 , frequency: String) {
        val fileName = "/sys/devices/system/cpu/cpu$coreNumber/cpufreq/scaling_setspeed"
        Runtime.getRuntime().exec(arrayOf("su", "-c", "echo $frequency > $fileName"))
        // todo set for min and max freq
    }
    fun setCurrentFreq(frequency: String) {
        // all of the cores
        for (i in 0..3){
            val fileName = "/sys/devices/system/cpu/cpu$i/cpufreq/scaling_setspeed"
            Runtime.getRuntime().exec(arrayOf("su", "-c", "echo $frequency > $fileName"))
        }
        // todo set for min and max freq
    }
    private fun setCore(coreNumber: Int, value: Boolean) {
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
            Runtime.getRuntime().exec(arrayOf("su", "-c", "echo $zero> $fileName "))
        }
    }
    fun isCoreOnline(core: Int): Boolean {
        var online = "0"
        try {
            val reader = RandomAccessFile("/sys/devices/system/cpu/cpu$core/online", "r")

            var done = false
            while (!done) {
                val line = reader.readLine()
                if (null == line) {
                    done = true
                    break
                }
                online = line
            }

        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return online == "1"
    }

    /////////////////////////////////////////////////////////////


    fun checkAlgorithmWithConsidrationHistory() {
        var cpuUasge = -1
        var temperuture = Constants.getInstance().min_temp

        temperuture = java.lang.Double.parseDouble(JavaUtils.getInstance().cpuTemperature_Xperia)
        cpuUasge = JavaUtils.getInstance().cpuUsage

        if (temperuture > Constants.getInstance().max_threshold_temp || !normalStatus) {
            // bayad be paeen tarin sath beresanim
            setCurrentFreq(Constants.getInstance().frequencies[0])
            setCore(1, false)
            setCore(2, false)
            setCore(3, false)

            // ta zamani ke damaye cpu zire in damaye  Constants.tmp_temperature  nayamade dar in halat bemanad

            normalStatus = false
            if (temperuture <= Constants.getInstance().tmp_temperature) normalStatus = true
        } else if (cpuUasge <= Constants.getInstance().cpu_usage_1) {
            // x means frequency     tedade a ham tedade core ha ro mige
            // x a

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[0], 0)) {
                setCurrentFreq(Constants.getInstance().frequencies[0])
                setCore(1, false)
                setCore(2, false)
                setCore(3, false)
            }

        } else if (Constants.getInstance().cpu_usage_1 < cpuUasge && cpuUasge <= Constants.getInstance().cpu_usage_2) {
            // x aa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[0], 1)) {
                setCurrentFreq(Constants.getInstance().frequencies[0])
                setCore(1, true)
                setCore(2, false)
                setCore(3, false)
            }
        } else if (Constants.getInstance().cpu_usage_2 < cpuUasge && cpuUasge <= Constants.getInstance().cpu_usage_3) {
            // x aaa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[0], 2)) {
                setCurrentFreq(Constants.getInstance().frequencies[0])
                setCore(1, true)
                setCore(2, true)
                setCore(3, false)
            }


        } else if (Constants.getInstance().cpu_usage_3 < cpuUasge && cpuUasge <= Constants.getInstance().cpu_usage_4) {
            // x aaaa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[0], 3)) {
                setCurrentFreq(Constants.getInstance().frequencies[0])
                setCore(1, true)
                setCore(2, true)
                setCore(3, true)
            }

        } else if (Constants.getInstance().cpu_usage_4 < cpuUasge && cpuUasge <= Constants.getInstance().cpu_usage_5) {
            // xx aa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[1], 1)) {
                setCurrentFreq(Constants.getInstance().frequencies[1])
                setCore(1, true)
                setCore(2, false)
                setCore(3, false)
            }

        } else if (Constants.getInstance().cpu_usage_5 < cpuUasge && cpuUasge <= Constants.getInstance().cpu_usage_6) {
            // xx aaa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[1], 2)) {
                setCurrentFreq(Constants.getInstance().frequencies[1])
                setCore(1, true)
                setCore(2, true)
                setCore(3, false)
            }

        } else if (Constants.getInstance().cpu_usage_6 < cpuUasge && cpuUasge <= Constants.getInstance().cpu_usage_7) {
            // xx aaaa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[1], 3)) {
                setCurrentFreq(Constants.getInstance().frequencies[1])
                setCore(1, true)
                setCore(2, true)
                setCore(3, true)
            }

        } else if (Constants.getInstance().cpu_usage_7 < cpuUasge && cpuUasge <= Constants.getInstance().cpu_usage_8) {
            // xxa aa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[2], 1)) {
                setCurrentFreq(Constants.getInstance().frequencies[2])
                setCore(1, true)
                setCore(2, false)
                setCore(3, false)
            }

        } else if (Constants.getInstance().cpu_usage_8 < cpuUasge && cpuUasge <= Constants.getInstance().cpu_usage_9) {
            // xxx aaa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[2], 2)) {
                setCurrentFreq(Constants.getInstance().frequencies[2])
                setCore(1, true)
                setCore(2, true)
                setCore(3, false)
            }

        } else if (Constants.getInstance().cpu_usage_9 < cpuUasge && cpuUasge <= Constants.getInstance().cpu_usage_10) {
            // xxx aaaa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[2], 3)) {
                setCurrentFreq(Constants.getInstance().frequencies[2])
                setCore(1, true)
                setCore(2, true)
                setCore(3, true)
            }
        }

    }

    private fun checkHistory(newFreq: String, newCores: Int): Boolean {
        // newCore  =>  0 means core0
        //          =>  1 means core0 & core1
        //          =>  2 means core0 & core1 & core2
        //          =>  3 means core0 & core1 & core2 & core3
        isOk = false
        val myRealm = Realm.getDefaultInstance()
        myRealm.executeTransactionAsync { realm ->
            if (newCores == 0) {
                val result = realm.where(Model::class.java).equalTo("cpuOneFreq", newFreq.toInt()).equalTo("cpuTwoFreq",0.toInt() )
                        .equalTo("cpuThreeFreq", 0.toInt()).equalTo("cpuFourFreq", 0.toInt()).findAll()
                val list = realm.copyFromRealm(result)
                if (checkConditions(list, newCores)) isOk = true
            } else if (newCores == 1) {
                val result = realm.where(Model::class.java).equalTo("cpuOneFreq", newFreq.toInt()).equalTo("cpuTwoFreq", newFreq.toInt()).equalTo("cpuThreeFreq", 0.toInt()).equalTo("cpuFourFreq", 0.toInt()).findAll()

                val list = realm.copyFromRealm(result)
                if (checkConditions(list, newCores)) isOk = true
            } else if (newCores == 2) {
                val result = realm.where(Model::class.java).equalTo("cpuOneFreq", newFreq.toInt()).equalTo("cpuTwoFreq", newFreq.toInt())
                        .equalTo("cpuThreeFreq", newFreq.toInt()).equalTo("cpuFourFreq", 0.toInt()).findAll()
                val list = realm.copyFromRealm(result)
                if (checkConditions(list, newCores)) isOk = true
            } else if (newCores == 3) {
                val result = realm.where(Model::class.java).equalTo("cpuOneFreq", newFreq.toInt()).equalTo("cpuTwoFreq", newFreq.toInt())
                        .equalTo("cpuThreeFreq", newFreq).equalTo("cpuFourFreq", newFreq).findAll()
                val list = realm.copyFromRealm(result)
                if (checkConditions(list, newCores)) isOk = true
            }
        }

        return isOk
    }

    fun checkConditions(list: List<Model>, newCores: Int): Boolean {

        val floatList = ArrayList<Float>()
        for (model in list) {
            floatList.add(model.cpuTemperature)
        }

        return workTimePass(list.size) && temperaturePass(newCores, Collections.max(floatList))
    }

    private fun workTimePass(count: Int): Boolean {
        // todo : inja return ha dorost shavad
        return if (count * Constants.getInstance().serviceIntervalTime < Constants.getInstance().workTimeAverage)
            false
        else
            false
    }

    private fun temperaturePass(newCores: Int, maxTemperature: Float): Boolean {
        // newCore  =>  0 means core0
        //          =>  1 means core0 & core1
        //          =>  2 means core0 & core1 $ core2
        //          =>  3 means core0 & core1 $ core2  core3

        // max damaye log shode ha ba hadde astaneye khodeman moghayese mikonim.
        // TODO : damahaye temp_level_1 .... 4   dar constants eslah shavand.

        when (newCores) {
            0 -> if (maxTemperature < Constants.getInstance().temp_level_1) return true
            1 -> if (maxTemperature < Constants.getInstance().temp_level_2) return true
            2 -> if (maxTemperature < Constants.getInstance().temp_level_3) return true
            3 -> if (maxTemperature < Constants.getInstance().temp_level_4) return true
        }
        return false
    }

    fun xxx(context: Context) {

//        var list : MutableList<Model>
        val myRealm = Realm.getDefaultInstance()
        myRealm.executeTransaction { realm ->
            val result = realm.where(Model::class.java).findAll()
            val list = realm.copyFromRealm(result)
            Toast.makeText(context , list[0].cpuTemperature.toString() , Toast.LENGTH_SHORT).show()
        }



    }


}