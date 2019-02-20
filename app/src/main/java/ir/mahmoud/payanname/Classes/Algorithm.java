package ir.mahmoud.payanname.Classes;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ir.mahmoud.payanname.Model.Model;
import ir.mahmoud.payanname.Service.AlgorithmService;

public class Algorithm {
    private static final Algorithm ourInstance = new Algorithm();

    public static Algorithm getInstance() {
        return ourInstance;
    }

    private Algorithm() {
    }

    public Context context;
    Intent intent;
    AlarmManager alarm;
    PendingIntent pintent;
    private boolean normalStatus = true ;
    boolean isOk;

    public void init(Context context){
        this.context = context;
//        setPermissionToFiles();
        // set cpu core 0 & 1 on    |   set cpu core 0   cpufreq chmod 777
        setFilesWriteable(0);
        setCore(1,true);
        // set cpu frequency at initial frequency
        setFrequency(Constants.getInstance().frequencies[0]);
        // start Algorithm
        startAlgorithm();
    }

    private void startAlgorithm() {
        Calendar cur_cal = Calendar.getInstance();
        intent = new Intent(context, AlgorithmService.class);
        pintent = PendingIntent.getService(context, 0, intent, 0);
        alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cur_cal.getTimeInMillis(), Constants.getInstance().serviceIntervalTime, pintent);
        Toast.makeText(context, "Algotithm Service Started", Toast.LENGTH_SHORT).show();
    }

    private void setFrequency(String frequency) {
        // set scaling_cur_freq
        JavaUtils.getInstance().setScalingMinFreq(frequency);
        JavaUtils.getInstance().setScalingMaxFreq(frequency);
        JavaUtils.getInstance().setCurrentFreq(frequency);
    }

    public void setCore(int coreNumber,boolean status){
        // for power on or off the core
        String path = "/sys/devices/system/cpu/cpu"+String.valueOf(coreNumber)+"/online/";
        try {

            File file = new File(path);
            // If file does not exists, then create it
            if (file.exists()) {
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                if(status){
                    if(!isCoreOnline(coreNumber)){
                        bw.write("1");
                    }
                }
                else{
                    if(isCoreOnline(coreNumber)){
                        bw.write("0");
                    }
                }
                bw.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isCoreOnline(int core){
        String online = "0";
        try {
            RandomAccessFile reader = new RandomAccessFile( "/sys/devices/system/cpu/cpu"+String.valueOf(core)+"/online", "r" );

            boolean done = false;
            while ( ! done ) {
                String line = reader.readLine();
                if ( null == line ) {
                    done = true;
                    break;
                }
                online = line;
            }

        } catch ( IOException ex ) {
            ex.printStackTrace();
        }

        if(online.equals("1")) return true;
        else  return false;
    }

    public void setFilesWriteable(int coreNumber){
        Process process;
        String path = "/sys/devices/system/cpu/cpu"+String.valueOf(coreNumber)+"/cpufreq/";
        try {
            process = Runtime.getRuntime().exec("chmod -R 777 "+path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPermissionToFiles(){
        Process process;

        for (int i=1;i<=3;i++){
            String path = "/sys/devices/system/cpu/cpu"+String.valueOf(i)+"/online/";
            try {
                process = Runtime.getRuntime().exec("chmod 777 "+path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        for (int i=0;i<=3;i++){
            String path = "/sys/devices/system/cpu/cpu"+String.valueOf(i)+"/cpufreq/";
            try {
                process = Runtime.getRuntime().exec("chmod -R 777 "+path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    public void cancelService(Context context) {
        try {
            alarm.cancel(pintent);
            Toast.makeText(context, "Service Stopped", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // todo : in methode zir k comment shode dorost kar mikone

//    public void checkAlgorithm() {
//        int cpuUasge = -1;
//        double temperuture = Constants.getInstance().min_temp;
//
//        temperuture = Double.parseDouble(JavaUtils.getInstance().getCpuTemperature_huawei());
//        cpuUasge = JavaUtils.getInstance().getCpuUsage();
//
//        if(temperuture > Constants.getInstance().max_threshold_temp || !normalStatus){
//            // bayad be paeen tarin sath beresanim
//            setFrequency(Constants.getInstance().frequencies[0]);
//            setCore(1,false);
//            setCore(2,false);
//            setCore(3,false);
//
//            // ta zamani ke damaye cpu zire in damaye  COnstants.tmp_temperature  nayamade dar in halat bemanad
//
//            normalStatus = false;
//            if(temperuture <= Constants.getInstance().tmp_temperature)  normalStatus = true;
//        }
//
//
//        else if(cpuUasge <= Constants.getInstance().cpu_usage_1 ) {
//            // x means frequency     tedade a ham tedade core ha ro mige
//            // x a
//            setFrequency(Constants.getInstance().frequencies[0]);
//            setCore(1,false);
//            setCore(2,false);
//            setCore(3,false);
//        }
//        else if( (Constants.getInstance().cpu_usage_1 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_2) ) {
//            // x aa
//            setFrequency(Constants.getInstance().frequencies[0]);
//            setCore(1,true);
//            setCore(2,false);
//            setCore(3,false);
//        }
//        else if( (Constants.getInstance().cpu_usage_2 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_3) ) {
//            // x aaa
//            setFrequency(Constants.getInstance().frequencies[0]);
//            setCore(1,true);
//            setCore(2,true);
//            setCore(3,false);
//        }
//        else if( (Constants.getInstance().cpu_usage_3 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_4) ) {
//            // x aaaa
//            setFrequency(Constants.getInstance().frequencies[0]);
//            setCore(1,true);
//            setCore(2,true);
//            setCore(3,true);
//        }
//        else if( (Constants.getInstance().cpu_usage_4 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_5) ) {
//            // xx aa
//            setFrequency(Constants.getInstance().frequencies[1]);
//            setCore(1,true);
//            setCore(2,false);
//            setCore(3,false);
//        }
//        else if( (Constants.getInstance().cpu_usage_5 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_6) ) {
//            // xx aaa
//            setFrequency(Constants.getInstance().frequencies[1]);
//            setCore(1,true);
//            setCore(2,true);
//            setCore(3,false);
//        }
//        else if( (Constants.getInstance().cpu_usage_6 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_7) ) {
//            // xx aaaa
//            setFrequency(Constants.getInstance().frequencies[1]);
//            setCore(1,true);
//            setCore(2,true);
//            setCore(3,true);
//        }
//        else if( (Constants.getInstance().cpu_usage_7 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_8) ) {
//            // xxa aa
//            setFrequency(Constants.getInstance().frequencies[2]);
//            setCore(1,true);
//            setCore(2,false);
//            setCore(3,false);
//        }
//        else if( (Constants.getInstance().cpu_usage_8 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_9) ) {
//            // xxx aaa
//            setFrequency(Constants.getInstance().frequencies[2]);
//            setCore(1,true);
//            setCore(2,true);
//            setCore(3,false);
//        }
//        else if( (Constants.getInstance().cpu_usage_9 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_10) ) {
//            // xxx aaaa
//            setFrequency(Constants.getInstance().frequencies[2]);
//            setCore(1,true);
//            setCore(2,true);
//            setCore(3,true);
//        }
//
//    }


    public void checkAlgorithmWithConsidrationHistory() {
        int cpuUasge = -1;
        double temperuture = Constants.getInstance().min_temp;

        temperuture = Double.parseDouble(JavaUtils.getInstance().getCpuTemperature_huawei());
        cpuUasge = JavaUtils.getInstance().getCpuUsage();

        if(temperuture > Constants.getInstance().max_threshold_temp || !normalStatus){
            // bayad be paeen tarin sath beresanim
            setFrequency(Constants.getInstance().frequencies[0]);
            setCore(1,false);
            setCore(2,false);
            setCore(3,false);

            // ta zamani ke damaye cpu zire in damaye  Constants.tmp_temperature  nayamade dar in halat bemanad

            normalStatus = false;
            if(temperuture <= Constants.getInstance().tmp_temperature)  normalStatus = true;
        }


        else if(cpuUasge <= Constants.getInstance().cpu_usage_1 ) {
            // x means frequency     tedade a ham tedade core ha ro mige
            // x a

            // check next status
            if(checkHistory(Constants.getInstance().frequencies[0],0)){
                setFrequency(Constants.getInstance().frequencies[0]);
                setCore(1,false);
                setCore(2,false);
                setCore(3,false);
            }

        }
        else if( (Constants.getInstance().cpu_usage_1 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_2) ) {
            // x aa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[0],1)) {
                setFrequency(Constants.getInstance().frequencies[0]);
                setCore(1,true);
                setCore(2,false);
                setCore(3,false);
            }
        }
        else if( (Constants.getInstance().cpu_usage_2 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_3) ) {
            // x aaa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[0],2)) {
                setFrequency(Constants.getInstance().frequencies[0]);
                setCore(1,true);
                setCore(2,true);
                setCore(3,false);
            }


        }
        else if( (Constants.getInstance().cpu_usage_3 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_4) ) {
            // x aaaa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[0],3)) {
                setFrequency(Constants.getInstance().frequencies[0]);
                setCore(1,true);
                setCore(2,true);
                setCore(3,true);
            }

        }
        else if( (Constants.getInstance().cpu_usage_4 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_5) ) {
            // xx aa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[1],1)) {
                setFrequency(Constants.getInstance().frequencies[1]);
                setCore(1,true);
                setCore(2,false);
                setCore(3,false);
            }

        }
        else if( (Constants.getInstance().cpu_usage_5 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_6) ) {
            // xx aaa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[1],2)) {
                setFrequency(Constants.getInstance().frequencies[1]);
                setCore(1,true);
                setCore(2,true);
                setCore(3,false);
            }

        }
        else if( (Constants.getInstance().cpu_usage_6 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_7) ) {
            // xx aaaa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[1],3)) {
                setFrequency(Constants.getInstance().frequencies[1]);
                setCore(1,true);
                setCore(2,true);
                setCore(3,true);
            }

        }
        else if( (Constants.getInstance().cpu_usage_7 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_8) ) {
            // xxa aa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[2],1)) {
                setFrequency(Constants.getInstance().frequencies[2]);
                setCore(1,true);
                setCore(2,false);
                setCore(3,false);
            }

        }
        else if( (Constants.getInstance().cpu_usage_8 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_9) ) {
            // xxx aaa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[2],2)) {
                setFrequency(Constants.getInstance().frequencies[2]);
                setCore(1,true);
                setCore(2,true);
                setCore(3,false);
            }

        }
        else if( (Constants.getInstance().cpu_usage_9 < cpuUasge)   &&  (cpuUasge<= Constants.getInstance().cpu_usage_10) ) {
            // xxx aaaa

            // check next status
            if (checkHistory(Constants.getInstance().frequencies[2],3)) {
                setFrequency(Constants.getInstance().frequencies[2]);
                setCore(1,true);
                setCore(2,true);
                setCore(3,true);
            }
        }

    }

    public boolean checkHistory(final String newFreq, final int newCores){
        // newCore  =>  0 means core0
        //          =>  1 means core0 & core1
        //          =>  2 means core0 & core1 $ core2
        //          =>  3 means core0 & core1 $ core2  core3
        isOk = false ;
        Realm myRealm = Realm.getDefaultInstance();
        myRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                if(newCores==0){
                    RealmResults result = realm.where(Model.class).equalTo("cpuOneFreq",newFreq).equalTo("cpuTwoFreq",0)
                            .equalTo("cpuThreeFreq",0).equalTo("cpuFourFreq",0).findAll();
                    List<Model> list = realm.copyFromRealm(result);
                    if(checkConditions(list,newCores)) isOk = true;
                }

                else if(newCores==1){
                    RealmResults result = realm.where(Model.class).equalTo("cpuOneFreq",newFreq).equalTo("cpuTwoFreq",newFreq)
                            .equalTo("cpuThreeFreq",0).equalTo("cpuFourFreq",0).findAll();
                    List<Model> list = realm.copyFromRealm(result);
                if(checkConditions(list,newCores)) isOk = true;
                }

                else if(newCores==2){
                    RealmResults result = realm.where(Model.class).equalTo("cpuOneFreq",newFreq).equalTo("cpuTwoFreq",newFreq)
                            .equalTo("cpuThreeFreq",newFreq).equalTo("cpuFourFreq",0).findAll();
                    List<Model> list = realm.copyFromRealm(result);
                if(checkConditions(list,newCores)) isOk = true;
                }

                else if(newCores==3){
                    RealmResults result = realm.where(Model.class).equalTo("cpuOneFreq",newFreq).equalTo("cpuTwoFreq",newFreq)
                            .equalTo("cpuThreeFreq",newFreq).equalTo("cpuFourFreq",newFreq).findAll();
                    List<Model> list = realm.copyFromRealm(result);
                if(checkConditions(list,newCores)) isOk = true;
                }
            }

        });

        return isOk;
    }

    public boolean checkConditions (List<Model> list,int newCores){

        List<Float> floatList = new ArrayList<>();
        for(Model model : list){  floatList.add(model.getCpuTemperature()); }

        if(workTimePass(list.size()) && temperaturePass(newCores, Collections.max(floatList))) return true;
        else return false;
    }

    private boolean workTimePass(int count) {
        if ( (count * Constants.getInstance().serviceIntervalTime) < Constants.getInstance().workTimeAverage ) return false;
        else return false;
    }

    private boolean temperaturePass(int newCores,float maxTemperature){
        // newCore  =>  0 means core0
        //          =>  1 means core0 & core1
        //          =>  2 means core0 & core1 $ core2
        //          =>  3 means core0 & core1 $ core2  core3

        // max damaye log shode ha ba hadde astaneye khodeman moghayese mikonim.
        // TODO : damahaye temp_level_1 .... 4   dar constants eslah shavand.

        switch (newCores){
            case 0:
                if(maxTemperature < Constants.getInstance().temp_level_1) return true;
                break;
            case 1:
                if(maxTemperature < Constants.getInstance().temp_level_2) return true;
                break;
            case 2:
                if(maxTemperature < Constants.getInstance().temp_level_3) return true;
                break;
            case 3:
                if(maxTemperature < Constants.getInstance().temp_level_4) return true;
                break;
        }
        return false;
    }

}

