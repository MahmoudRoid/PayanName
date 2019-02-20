package ir.mahmoud.payanname.Classes;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

/**
 * Created by mahmoud on 3/2/18.
 */

public class JavaUtils {
    private static final JavaUtils ourInstance = new JavaUtils();

    public static JavaUtils getInstance() {
        return ourInstance;
    }

    private JavaUtils() {
    }


    private String path = "/sys/devices/system/cpu/cpu0/cpufreq/";


    public  String getMaxCpuFreq(){
        String maxFreq ="";
        try {

            RandomAccessFile reader = new RandomAccessFile( "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq", "r" );

            boolean done = false;
            while ( ! done ) {
                String line = reader.readLine();
                if ( null == line ) {
                    done = true;
                    break;
                }
                maxFreq = line;
            }

        } catch ( IOException ex ) {
            ex.printStackTrace();
        }
        return maxFreq;
    }
    public  String getMinCpuFreq(){
        String maxFreq ="";
        try {

            RandomAccessFile reader = new RandomAccessFile( "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq", "r" );

            boolean done = false;
            while ( ! done ) {
                String line = reader.readLine();
                if ( null == line ) {
                    done = true;
                    break;
                }
                maxFreq = line;
            }

        } catch ( IOException ex ) {
            ex.printStackTrace();
        }
        return maxFreq;
    }
    public  String getCurrentCpuFreq(int core){
        String maxFreq = "0";
        try {
            RandomAccessFile reader = new RandomAccessFile( "/sys/devices/system/cpu/cpu"+String.valueOf(core)+"/cpufreq/cpuinfo_cur_freq", "r" );

            boolean done = false;
            while ( ! done ) {
                String line = reader.readLine();
                if ( null == line ) {
                    done = true;
                    break;
                }
                maxFreq =line;
            }

        } catch ( IOException ex ) {
            ex.printStackTrace();
        }
        return maxFreq;
    }
    public  String getScalingCpuFreq(int core){
        String maxFreq = "0";
        try {
            RandomAccessFile reader = new RandomAccessFile( "/sys/devices/system/cpu/cpu"+String.valueOf(core)+"/cpufreq/scaling_cur_freq", "r" );

            boolean done = false;
            while ( ! done ) {
                String line = reader.readLine();
                if ( null == line ) {
                    done = true;
                    break;
                }
                maxFreq = line;
            }

        } catch ( IOException ex ) {
            ex.printStackTrace();
        }
        return maxFreq;
    }
    public  String getCpuTemperature_huawei(){
        //az 3 ravesh bedast miad

        // cat sys/class/thermal/thermal_zone0/temp
        // cat sys/devices/virtual/thermal/thermal_zone0/temp
        // cat sys/class/hwmon/hwmonX/temp1_input

        String maxTemp = "";
        try {
            RandomAccessFile reader = new RandomAccessFile( "/sys/class/thermal/thermal_zone1/temp", "r" );

            boolean done = false;
            while ( ! done ) {
                String line = reader.readLine();
                if ( null == line ) {
                    done = true;
                    break;
                }
                maxTemp =line;
            }

        } catch ( IOException ex ) {
            ex.printStackTrace();
        }
        return String.valueOf(Double.parseDouble(maxTemp) / 1000);
    }
    public  String getCpuTemperature_Xperia(){
        //az 3 ravesh bedast miad

        // cat sys/class/thermal/thermal_zone0/temp
        // cat sys/devices/virtual/thermal/thermal_zone0/temp
        // cat sys/class/hwmon/hwmonX/temp1_input

        double maxTemp = 0.0;
        RandomAccessFile reader;
        //////// core 1
        try {
            reader = new RandomAccessFile( "/sys/class/thermal/thermal_zone7/temp", "r" );
            boolean done = false;
            while ( ! done ) {
                String line = reader.readLine();
                if ( null == line ) {
                    done = true;
                    break;
                }
                maxTemp += Double.parseDouble(line);
            }

        } catch ( IOException ex ) {
            ex.printStackTrace();
        }
        //////// core 2
        try {
            reader = new RandomAccessFile( "/sys/class/thermal/thermal_zone8/temp", "r" );
            boolean done = false;
            while ( ! done ) {
                String line = reader.readLine();
                if ( null == line ) {
                    done = true;
                    break;
                }
                maxTemp += Double.parseDouble(line);
            }

        } catch ( IOException ex ) {
            ex.printStackTrace();
        }
        //////// core 3
        try {
            reader = new RandomAccessFile( "/sys/class/thermal/thermal_zone9/temp", "r" );
            boolean done = false;
            while ( ! done ) {
                String line = reader.readLine();
                if ( null == line ) {
                    done = true;
                    break;
                }
                maxTemp += Double.parseDouble(line);
            }

        } catch ( IOException ex ) {
            ex.printStackTrace();
        }
        //////// core 4
        try {
            reader = new RandomAccessFile( "/sys/class/thermal/thermal_zone10/temp", "r" );
            boolean done = false;
            while ( ! done ) {
                String line = reader.readLine();
                if ( null == line ) {
                    done = true;
                    break;
                }
                maxTemp += Double.parseDouble(line);
            }

        } catch ( IOException ex ) {
            ex.printStackTrace();
        }

        return String.valueOf(maxTemp / 4);
    }
    public  String[] getCpuLoad(){
        String loadavgs = "";
        try {
            RandomAccessFile reader = new RandomAccessFile( "/proc/loadavg", "r" );

            boolean done = false;
            while ( ! done ) {
                String line = reader.readLine();
                if ( null == line ) {
                    done = true;
                    break;
                }
                loadavgs = line;
            }

        } catch ( IOException ex ) {
            ex.printStackTrace();
        }

        return loadavgs.split(" ");
    }
    public  String getCpuUtilization(int core){
        String maxFreq = "0";
        try {
            RandomAccessFile reader = new RandomAccessFile( "/sys/devices/system/cpu/cpu"+String.valueOf(core)+"/cpufreq/cpu_utilization", "r" );

            boolean done = false;
            while ( ! done ) {
                String line = reader.readLine();
                if ( null == line ) {
                    done = true;
                    break;
                }
                maxFreq = line;
            }

        } catch ( IOException ex ) {
            ex.printStackTrace();
        }
        return maxFreq;
    }
    ////////////////////////////////////  set ///////////////////////////////
    public  boolean  setCurrentFreq(String valueFreq){
        String path = "/sys/devices/system/cpu/cpu0/cpufreq/";
        boolean ok= false;
        try {
            File file = new File(path+"scaling_setspeed");
            // If file does not exists, then create it
            if (file.exists()) {
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(valueFreq);
                bw.close();
                ok = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            ok = false;
        }
        return ok;
    }
    public  boolean  setScalingMinFreq(String valueFreq){
        String path = "/sys/devices/system/cpu/cpu0/cpufreq/";
        boolean ok= false;
        try {
            File file = new File(path+"scaling_min_freq");
            // If file does not exists, then create it
            if (file.exists()) {
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(valueFreq);
                bw.close();
                ok = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            ok = false;
        }
        return ok;
    }
    public  boolean  setScalingMaxFreq(String valueFreq){
        String path = "/sys/devices/system/cpu/cpu0/cpufreq/";
        boolean ok= false;
        try {
            File file = new File(path+"scaling_max_freq");
            // If file does not exists, then create it
            if (file.exists()) {
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(valueFreq);
                bw.close();
                ok = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            ok = false;
        }
        return ok;
    }
    /////////////////////////////////////////////////////////////////////////
    public int getCpuUsage() {
        String tempString = executeTop();

        if(!tempString.equals("")){
            tempString = tempString.replaceAll(",", "");
            tempString = tempString.replaceAll("User", "");
            tempString = tempString.replaceAll("System", "");
            tempString = tempString.replaceAll("IOW", "");
            tempString = tempString.replaceAll("IRQ", "");
            tempString = tempString.replaceAll("%", "");
            for (int i = 0; i < 10; i++) {
                tempString = tempString.replaceAll("  ", " ");
            }
            tempString = tempString.trim();
            String[] myString = tempString.split(" ");
            int[] cpuUsageAsInt = new int[myString.length];
            for (int i = 0; i < myString.length; i++) {
                myString[i] = myString[i].trim();
                cpuUsageAsInt[i] = Integer.parseInt(myString[i]);
            }
            return (cpuUsageAsInt[0]+cpuUsageAsInt[1]+cpuUsageAsInt[2]+cpuUsageAsInt[3]);
        }
        else return -1;

    }
    private String executeTop() {
        Process p = null;
        BufferedReader in = null;
        String returnString = "";
        try {
            p = Runtime.getRuntime().exec("top -m 1000 -n 1 -d 1");
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while (returnString == null || returnString.contentEquals("")) {
                returnString = in.readLine();
            }
        }
        catch (IOException e) {
            Log.e("executeTop", "error in getting first line of top");
            e.printStackTrace();
        }
        finally {
            try {
                in.close();
                p.destroy();

            } catch (IOException e) {
                Log.e("executeTop",
                        "error in closing and destroying top process");
                e.printStackTrace();
                return returnString;
            }
        }
        return returnString;
    }

    public  boolean isRooted() {

        // check if /system/app/Superuser.apk is present
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e1) {
            // ignore
        }
        return false;
    }



}
