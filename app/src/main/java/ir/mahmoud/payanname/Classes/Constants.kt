package ir.mahmoud.payanname.Classes


class Constants {

    companion object {

        // temperature
        val min_temp = 34.0
        val max_temp = 95.0
        val max_threshold_temp = 93.0
        val tmp_temperature = 88.0

        val temp_level_1 = 100.0
        val temp_level_2 = 100.0
        val temp_level_3 = 100.0
        val temp_level_4 = 100.0

        // frequencies
        val frequencies = arrayOf("", "", "", "", "", "")
        val freq_A = "1026000"
        val freq_B = "1134000"
        val freq_C = "1242000"
        val freq_D = "1350000"
        val freq_E = "1458000"
        val freq_F = "1512000"
        // cpu usages

        val cpu_usage_1 = 50
        val cpu_usage_2 = 55
        val cpu_usage_3 = 60
        val cpu_usage_4 = 65
        val cpu_usage_5 = 70
        val cpu_usage_6 = 75
        val cpu_usage_7 = 80
        val cpu_usage_8 = 85
        val cpu_usage_9 = 90
        val cpu_usage_10 = 94
        val cpu_usage_11 = 96
        val cpu_usage_12 = 98

        ///////////////// work time ( s )
        val serviceIntervalTime: Long = 3000 // ms
        val workTimeAverage: Long = 12000 // ms

        /////////////// services Intervals
        val collectDataServiceIntervalTime: Long = 3000 // ms
        val algorithmServiceIntervalTime: Long = 5000 // ms

    }

}
