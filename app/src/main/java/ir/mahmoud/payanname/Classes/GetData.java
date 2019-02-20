package ir.mahmoud.payanname.Classes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import io.realm.Realm;
import ir.mahmoud.payanname.Model.Model;


public class GetData {

    String Cpu1Freq="",Cpu2Freq="0",Cpu3Freq="",Cpu4Freq="0",CpuLoad="",CpuUsage="",CpuTemperature="",text = "";

    private static final GetData ourInstance = new GetData();

    public static GetData getInstance() {
        return ourInstance;
    }

    private GetData() {}

    public void saveData() {
        getSystemFilesData();
        //save to file
        writeToFile(text);
        //save to DB
        saveToDB();
    }

    public void getSystemFilesData(){
        // cpu_freq1="x"|cpu_freq2="x"|cpu_load="x"|cpu_temp="x"

        this.Cpu1Freq = JavaUtils.getInstance().getScalingCpuFreq(0);
        this.Cpu2Freq = JavaUtils.getInstance().getScalingCpuFreq(1);
        this.Cpu3Freq = JavaUtils.getInstance().getScalingCpuFreq(2);
        this.Cpu4Freq = JavaUtils.getInstance().getScalingCpuFreq(3);
        this.CpuLoad = JavaUtils.getInstance().getCpuLoad()[0];
        this.CpuUsage = String.valueOf(JavaUtils.getInstance().getCpuUsage());
//        this.CpuTemperature = JavaUtils.getInstance().getCpuTemperature_Sony();
        this.CpuTemperature = JavaUtils.getInstance().getCpuTemperature_Xperia();

        text = "cpu_freq1="+Cpu1Freq
                +"|"+"cpu_freq2="+Cpu2Freq
                +"|"+"cpu_freq3="+Cpu3Freq
                +"|"+"cpu_freq4="+Cpu4Freq
                +"|"+"cpu_load="+CpuLoad
                +"|"+"cpu_usage="+CpuUsage
                +"|"+"cpu_temp="+CpuTemperature+"\n";

    }

    public void writeToFile(String text){
        File file = new File(MyApplication.DataFile);
        try {
            FileWriter writer = new FileWriter(file,true);
            writer.append(text + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToDB() {
        try {

            Realm myRealm = Realm.getDefaultInstance();

            myRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    //save
                    // Create an object
                    Model model = realm.createObject(Model.class);
                    // Set its fields
                    model.setCpuOneFreq(Integer.parseInt(Cpu1Freq));
                    model.setCpuTwoFreq(Integer.parseInt(Cpu2Freq));
                    model.setCpuThreeFreq(Integer.parseInt(Cpu3Freq));
                    model.setCpuFourFreq(Integer.parseInt(Cpu4Freq));
                    model.setCpuLoad(Float.parseFloat(CpuLoad));
                    model.setCpuUsage(Float.parseFloat(CpuUsage));
                    model.setCpuTemperature(Float.parseFloat(CpuTemperature));
                }
            });
        }
        catch (OutOfMemoryError error){
            error.printStackTrace();
        }
        catch (NullPointerException n){
            n.printStackTrace();}
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        catch (Exception ee) {
            ee.printStackTrace();
        }

    }
}
