package exam.app.firebase

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log

/**
 * Created by Mikkel on 14/05/2017.
 */
class FBJobService : JobService() {

    val TAG = "FBJobService"

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Performing long running task in scheduled job")
        // TODO(developer): add long running task here.
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        return false;
    }
}