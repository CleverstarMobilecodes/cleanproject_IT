package com.mobiledi.earnit.service;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.mobiledi.earnit.utils.Utils;

/**
 * Created by praks on 18/08/17.
 */


public class FCMJobService extends JobService {

    private static final String TAG = "FCMJobService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Utils.logDebug(TAG, "Performing long running task in scheduled job");
        // TODO(developer): add long running task here.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}