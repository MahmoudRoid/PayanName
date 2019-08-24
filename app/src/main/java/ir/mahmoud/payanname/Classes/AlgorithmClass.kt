package ir.mahmoud.payanname.Classes

import android.content.Context
import android.util.Log
import android.widget.Toast
import io.realm.Realm
import ir.mahmoud.payanname.Model.Model
import java.io.*
import java.util.*

class AlgorithmClass {

    var normalStatus = true
    var process :Process? = null


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
            process = Runtime.getRuntime().exec(arrayOf("su", "-c", "echo $frequency > $fileName"))
            process!!.inputStream
        
    }

    fun setCurrentFreq(frequency: String) {
        // all of the cores
        for (i in 0..3){
            val fileName = "/sys/devices/system/cpu/cpu$i/cpufreq/scaling_setspeed"
            Runtime.getRuntime().exec(arrayOf("su", "-c", "echo $frequency > $fileName"))
        }
        // todo set for min and max freq
    }

    fun setCurrentFreqUsingFS(first:String,second:String,third:String,fourth:String){

        when(first.toUpperCase()){
            "A" -> setCurrentFreq(0 , Constants.freq_A)
            "B" -> setCurrentFreq(0 , Constants.freq_B)
            "C" -> setCurrentFreq(0 , Constants.freq_C)
            "D" -> setCurrentFreq(0 , Constants.freq_D)
            "E" -> setCurrentFreq(0 , Constants.freq_E)
            "F" -> setCurrentFreq(0 , Constants.freq_F)
        }

        when(second.toUpperCase()){
            "A" -> setCurrentFreq(1 , Constants.freq_A)
            "B" -> setCurrentFreq(1 , Constants.freq_B)
            "C" -> setCurrentFreq(1 , Constants.freq_C)
            "D" -> setCurrentFreq(1 , Constants.freq_D)
            "E" -> setCurrentFreq(1 , Constants.freq_E)
            "F" -> setCurrentFreq(1 , Constants.freq_F)
        }

        when(third.toUpperCase()){
            "A" -> setCurrentFreq(2 , Constants.freq_A)
            "B" -> setCurrentFreq(2 , Constants.freq_B)
            "C" -> setCurrentFreq(2 , Constants.freq_C)
            "D" -> setCurrentFreq(2 , Constants.freq_D)
            "E" -> setCurrentFreq(2 , Constants.freq_E)
            "F" -> setCurrentFreq(2 , Constants.freq_F)
        }

        when(fourth.toUpperCase()){
            "A" -> setCurrentFreq(3 , Constants.freq_A)
            "B" -> setCurrentFreq(3 , Constants.freq_B)
            "C" -> setCurrentFreq(3 , Constants.freq_C)
            "D" -> setCurrentFreq(3 , Constants.freq_D)
            "E" -> setCurrentFreq(3 , Constants.freq_E)
            "F" -> setCurrentFreq(3 , Constants.freq_F)
        }
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
        var temperuture = Constants.min_temp

        temperuture = java.lang.Double.parseDouble(JavaUtils.getInstance().cpuTemperature_Xperia)
        cpuUasge = JavaUtils.getInstance().cpuUsage

        if (temperuture > Constants.max_threshold_temp || !normalStatus) {
            // bayad be paeen tarin sath beresanim
            // FFDD
            setCurrentFreqUsingFS("F","F","D","D")

            // ta zamani ke damaye cpu zire in damaye  Constants.tmp_temperature  nayamade dar in halat bemanad
            normalStatus = false
            if (temperuture <= Constants.tmp_temperature) normalStatus = true
        }

        else{

            // u >= 98
            if (cpuUasge >= Constants.cpu_usage_12){
                if (checkHistory(Constants.frequencies[0], 0)) {
                    setCurrentFreqUsingFS("F","F","F","F")
                }
            }
            // 96 <= u < 98
            else if ( (cpuUasge >= Constants.cpu_usage_11) && (cpuUasge < Constants.cpu_usage_12)){
                if (checkHistory(Constants.frequencies[0], 0)) {
                    setCurrentFreqUsingFS("F","F","F","E")
                }
            }
            // 94 <= u < 96
            else if ( (cpuUasge >= Constants.cpu_usage_10) && (cpuUasge < Constants.cpu_usage_11)){
                if (checkHistory(Constants.frequencies[0], 0)) {
                    setCurrentFreqUsingFS("F","F","F","D")
                }
            }
            // 90 <= u < 94
            else  if ( (cpuUasge >= Constants.cpu_usage_9) && (cpuUasge < Constants.cpu_usage_10)){
                if (checkHistory(Constants.frequencies[0], 0)) {
                    setCurrentFreqUsingFS("F","F","F","C")
                }
            }
            // 85 <= u < 90
            else  if ( (cpuUasge >= Constants.cpu_usage_8) && (cpuUasge < Constants.cpu_usage_9)){
                if (checkHistory(Constants.frequencies[0], 0)) {
                    setCurrentFreqUsingFS("F","F","F","B")
                }
            }
            // 80 <= u < 85
            else  if ( (cpuUasge >= Constants.cpu_usage_7) && (cpuUasge < Constants.cpu_usage_8)){
                if (checkHistory(Constants.frequencies[0], 0)) {
                    setCurrentFreqUsingFS("F","F","F","A")
                }
            }
            // 75 <= u < 80
            else  if ( (cpuUasge >= Constants.cpu_usage_6) && (cpuUasge < Constants.cpu_usage_7)){
                if (checkHistory(Constants.frequencies[0], 0)) {
                    setCurrentFreqUsingFS("F","F","E","E")
                }
            }
            // 70 <= u < 75
            else if ( (cpuUasge >= Constants.cpu_usage_5) && (cpuUasge < Constants.cpu_usage_6)){
                if (checkHistory(Constants.frequencies[0], 0)) {
                    setCurrentFreqUsingFS("F","F","E","B")
                }
            }
            // 65 <= u < 70
            else  if ( (cpuUasge >= Constants.cpu_usage_4) && (cpuUasge < Constants.cpu_usage_5)){
                if (checkHistory(Constants.frequencies[0], 0)) {
                    setCurrentFreqUsingFS("F","F","D","C")
                }
            }
            // 60 <= u < 65
            else if ( (cpuUasge >= Constants.cpu_usage_3) && (cpuUasge < Constants.cpu_usage_4)){
                if (checkHistory(Constants.frequencies[0], 0)) {
                    setCurrentFreqUsingFS("F","F","C","A")
                }
            }
            // 55 <= u < 60
            else if ( (cpuUasge >= Constants.cpu_usage_2) && (cpuUasge < Constants.cpu_usage_3)){
                if (checkHistory(Constants.frequencies[0], 0)) {
                    setCurrentFreqUsingFS("F","F","B","B")
                }
            }
            // 50 <= u < 55
            else  if ( (cpuUasge >= Constants.cpu_usage_1) && (cpuUasge < Constants.cpu_usage_2)){
                if (checkHistory(Constants.frequencies[0], 0)) {
                    setCurrentFreqUsingFS("F","F","B","A")
                }
            }
            // u < 50
            else   if (cpuUasge < Constants.cpu_usage_1){
                if (checkHistory(Constants.frequencies[0], 0)) {
                    setCurrentFreqUsingFS("F","F","A","A")
                }
            }

        }

    }

    private fun checkHistory(newFreq: String, newCores: Int): Boolean {
        // newCore  =>  0 means core0
        //          =>  1 means core0 & core1
        //          =>  2 means core0 & core1 & core2
        //          =>  3 means core0 & core1 & core2 & core3
        var isOk = false
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

    private fun checkConditions(list: List<Model>, newCores: Int): Boolean {

        val floatList = ArrayList<Float>()
        for (model in list) {
            floatList.add(model.cpuTemperature)
        }

        return workTimePass(list.size) && temperaturePass(newCores, Collections.max(floatList))
    }

    private fun workTimePass(count: Int): Boolean {
        return  (count * Constants.serviceIntervalTime >= Constants.workTimeAverage)
    }

    private fun temperaturePass(newCores: Int, maxTemperature: Float): Boolean {
        // newCore  =>  0 means core0
        //          =>  1 means core0 & core1
        //          =>  2 means core0 & core1 $ core2
        //          =>  3 means core0 & core1 $ core2  core3

        when (newCores) {
            0 -> if (maxTemperature < Constants.temp_level_1) return true
            1 -> if (maxTemperature < Constants.temp_level_2) return true
            2 -> if (maxTemperature < Constants.temp_level_3) return true
            3 -> if (maxTemperature < Constants.temp_level_4) return true
        }
        return false
    }

}