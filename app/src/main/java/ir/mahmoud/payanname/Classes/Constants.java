package ir.mahmoud.payanname.Classes;


public class Constants {
    private static final Constants ourInstance = new Constants();

    public static Constants getInstance() {
        return ourInstance;
    }

    private Constants() {
    }

    // temperature
    public double min_temp = 34;
    public double max_temp = 59;
    public double max_threshold_temp = 57;
    public double tmp_temperature = 40;

    public double temp_level_1 = 100;
    public double temp_level_2 = 100;
    public double temp_level_3 = 100;
    public double temp_level_4 = 100;

    // frequencies
    public String[] frequencies = {"104000","1196000","1300000"};
    // cpu usages
    public int cpu_usage_1 = 3;
    public int cpu_usage_2 = 7;
    public int cpu_usage_3 = 11;
    public int cpu_usage_4 = 15;
    public int cpu_usage_5 = 19;
    public int cpu_usage_6 = 23;
    public int cpu_usage_7 = 29;
    public int cpu_usage_8 = 33;
    public int cpu_usage_9 = 50;
    public int cpu_usage_10 = 100;
    ///////////////// work time ( s )
    public long serviceIntervalTime = 3000; // ms
    public long workTimeAverage = 12000; // ms

    /////////////// services Intervals
    public long collectDataServiceIntervalTime = 3000; // ms
    public long algorithmServiceIntervalTime = 3000; // ms

}
