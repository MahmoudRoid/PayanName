package ir.mahmoud.payanname.Model;

import io.realm.RealmObject;

/**
 * Created by soheilsystem on 3/6/2018.
 */

public class Model extends RealmObject {

    private int cpuOneFreq,cpuTwoFreq,cpuThreeFreq,cpuFourFreq;
    private float cpuLoad,cpuUsage,cpuTemperature;

    public Model(){}


    public int getCpuOneFreq() {
        return cpuOneFreq;
    }

    public void setCpuOneFreq(int cpuOneFreq) {
        this.cpuOneFreq = cpuOneFreq;
    }

    public int getCpuTwoFreq() {
        return cpuTwoFreq;
    }

    public void setCpuTwoFreq(int cpuTwoFreq) {
        this.cpuTwoFreq = cpuTwoFreq;
    }

    public int getCpuThreeFreq() {
        return cpuThreeFreq;
    }

    public void setCpuThreeFreq(int cpuThreeFreq) {
        this.cpuThreeFreq = cpuThreeFreq;
    }

    public int getCpuFourFreq() {
        return cpuFourFreq;
    }

    public void setCpuFourFreq(int cpuFourFreq) {
        this.cpuFourFreq = cpuFourFreq;
    }

    public float getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(float cpuLoad) {
        this.cpuLoad = cpuLoad;
    }

    public float getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(float cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public float getCpuTemperature() {
        return cpuTemperature;
    }

    public void setCpuTemperature(float cpuTemperature) {
        this.cpuTemperature = cpuTemperature;
    }
}
