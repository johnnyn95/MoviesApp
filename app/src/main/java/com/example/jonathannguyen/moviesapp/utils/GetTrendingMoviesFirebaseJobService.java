package com.example.jonathannguyen.moviesapp.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class GetTrendingMoviesFirebaseJobService extends JobService {
    AsyncTask backgroundTask;
    @Override
    public boolean onStartJob(final JobParameters job) {
        backgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = GetTrendingMoviesFirebaseJobService.this;
                GetTrendingMoviesTask.executeTask(getApplication(),GetTrendingMoviesTask.ACTION_GET_TRENDING_MOVIES);
                Log.d("joblog",String.valueOf(System.currentTimeMillis()));
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job,false);
            }
        };
        backgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(backgroundTask != null) backgroundTask.cancel(true);
        return true;
    }
}
