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
    public double max_temp = 95;
    public double max_threshold_temp = 93;
    public double tmp_temperature = 88;

    public double temp_level_1 = 100;
    public double temp_level_2 = 100;
    public double temp_level_3 = 100;
    public double temp_level_4 = 100;

    // frequencies
    public String[] frequencies = {"","","","","",""};
    public String freq_A = "1026000";
    public String freq_B = "1134000";
    public String freq_C = "1242000";
    public String freq_D = "1350000";
    public String freq_E = "1458000";
    public String freq_F = "1512000";
    // cpu usages

    public int cpu_usage_1 = 50;
    public int cpu_usage_2 = 55;
    public int cpu_usage_3 = 60;
    public int cpu_usage_4 = 65;
    public int cpu_usage_5 = 70;
    public int cpu_usage_6 = 75;
    public int cpu_usage_7 = 80;
    public int cpu_usage_8 = 85;
    public int cpu_usage_9 = 90;
    public int cpu_usage_10 = 94;
    public int cpu_usage_11 = 96;
    public int cpu_usage_12 = 98;

    ///////////////// work time ( s )
    public long serviceIntervalTime = 3000; // ms
    public long workTimeAverage = 12000; // ms

    /////////////// services Intervals
    public long collectDataServiceIntervalTime = 3000; // ms
    public long algorithmServiceIntervalTime = 3000; // ms

}
