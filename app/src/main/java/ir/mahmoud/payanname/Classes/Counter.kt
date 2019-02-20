package ir.mahmoud.payanname.Classes

import android.os.Handler
import android.widget.Toast
import ir.mahmoud.payanname.Enum.Language
import java.util.ArrayList

class Counter(var count:Int = 8 , var language:Language = Language.C) {

    var threadList: MutableList<Thread> = ArrayList()
    var integerList: MutableList<Int> = ArrayList()
    var kotlinResult: String ="Empty"
    var isStopped:Boolean = false

    companion object
    {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

    fun start(){
        when(language){
            Language.Kotlin -> kotlin_start()
            Language.C -> c_start()
            Language.Java -> java_start()
        }
    }

    fun stop(){
        when(language){
            Language.Kotlin -> kotlin_end()
            Language.C -> c_end()
            Language.Java -> java_end()
        }
    }

    fun getData():String{
        when(language){
            Language.Kotlin -> return kotlinResult
            Language.C -> return intFromJNI()
            Language.Java -> return  Java.getInstance().getResult()
        }
    }


    /////////////////////  Kotlin  //////////////////////////////////

    fun kotlin_start(){
        isStopped = false
        initial()
        execute()
    }

    fun kotlin_end(){
        try {
            isStopped = true
            kotlinResult = ""


            for(i in 0 until count){
                threadList[i].interrupt()
            }

            for (i in 0 until count){
                kotlinResult += (i+1).toString()+"=> "+integerList[i]+"\n"
            }

        }
        catch (e:Exception){

        }
    }

    fun initial() {

        integerList.clear()
        threadList.clear()

        for (i in 0 until count)
            integerList.add(0)

        for (i in 0 until count){
            threadList.add(createMainThread(i))
        }
    }

    fun createMainThread(value: Int): Thread {

        return Thread(Runnable {
            while (true) {
                if (isStopped)
                    break
                else
                    integerList[value] = integerList[value] + 1
            }
        })
    }

    fun execute(){
        for(i in 0 until count){
            threadList[i].start()
        }
        //Toast.makeText(this,"started", Toast.LENGTH_SHORT).show()
    }

    fun kotlin_getData(){
        //sample_text.text = kotlinResult
    }


    /////////////////////  C    //////////////////////////////////////

    fun c_start(){
        createThread(count)
        //Toast.makeText(this,"started", Toast.LENGTH_SHORT).show()
        // end thread at 20 seconds

//            Handler().postDelayed({
//                stopThread()
//                Toast.makeText(this,"ended",Toast.LENGTH_SHORT).show()
//            },20000)
    }

    fun c_end(){
        stopThread()
        //Toast.makeText(this,"ended", Toast.LENGTH_SHORT).show()
    }

    fun c_getData(){
        // sample_text.text = intFromJNI()
    }

    /////////////////////  JAVA    //////////////////////////////////////


    fun java_start(){
        Java.getInstance().isStopped = false
        Java.getInstance().initialList(count)
        //Toast.makeText(this,"started", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            java_end()
        },20000)
    }

    fun java_end(){
        Java.getInstance().stop()
        //Toast.makeText(this,"ended", Toast.LENGTH_SHORT).show()
    }

    fun java_getData(){
        //sample_text.text = Java.getInstance().getResult()
    }

    ///////////////////////////////////////////////////////////////////////////////////
    external fun stringFromJNI(): String
    external fun intFromJNI(): String
    external fun createThread(threadCount:Int)
    external fun stopThread()



}