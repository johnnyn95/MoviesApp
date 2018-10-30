package com.example.jonathannguyen.moviesapp.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.jonathannguyen.moviesapp.R;
import com.example.jonathannguyen.moviesapp.api.model.Movies;
import com.example.jonathannguyen.moviesapp.ui.MovieDetails.MovieDetails;

public class NotificationUtils {
    private static final int ADD_TO_FAVOURITES_PENDING_INTENT_ID = 3418;
    private static final int TURN_OFF_NOTIFICATIONS_PENDING_INTENT_ID = 3419;
    private static final int TRENDING_MOVIE_NOTIFICATION_ID = 1138;
    private static final int TRENDING_MOVIE_PENDING_INTENT_ID = 3417;
    private static final String TRENDING_MOVIE_NOTIFICATION_CHANNEL_ID = "update_notification_channel";

    public static void notifyUser(Context context,Movies movie) {
        final NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    TRENDING_MOVIE_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,TRENDING_MOVIE_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_whatshot_black_24dp)
                .setContentTitle(context.getResources().getString(R.string.notification_title) + " " + movie.getTitle())
                .setContentText(movie.getOverview())
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .addAction(addToFavourites(context))
                .addAction(turnOffNotificationsAction(context))
                .setContentIntent(contentIntent(context,movie))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(TRENDING_MOVIE_NOTIFICATION_ID, notificationBuilder.build());
    }

    private static PendingIntent contentIntent(Context context,Movies movie) {
        Intent startActivityIntent = new Intent(context, MovieDetails.class);
        startActivityIntent.putExtra(context.getResources().getString(R.string.EXTRA_MOVIE_ID),movie.getId());
        return PendingIntent.getActivity(
                context,
                TRENDING_MOVIE_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static NotificationCompat.Action turnOffNotificationsAction(Context context){
        Intent turnOffNotificationsIntent = new Intent(context,GetTrendingMoviesIntentService.class);
        turnOffNotificationsIntent.setAction(GetTrendingMoviesTask.ACTION_TURN_OFF_NOTIFICATIONS_TRENDING_MOVIES);
        PendingIntent pendingIntent = PendingIntent.getService(context,
                TURN_OFF_NOTIFICATIONS_PENDING_INTENT_ID,
                turnOffNotificationsIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
         NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_clear_black_24dp,context.getResources().getString(R.string.notification_turn_off),pendingIntent);
         return action;
    }

    private static NotificationCompat.Action addToFavourites(Context context){
        Intent addMovieToFavouritesIntent = new Intent(context,GetTrendingMoviesIntentService.class);
        addMovieToFavouritesIntent.setAction(GetTrendingMoviesTask.ACTION_ADD_TO_FAVOURITES_TRENDING_MOVIES);
        PendingIntent pendingIntent = PendingIntent.getService(context,
                ADD_TO_FAVOURITES_PENDING_INTENT_ID,
                addMovieToFavouritesIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.favourite,context.getResources().getString(R.string.notification_add_to_favourites),pendingIntent);
        return action;
    }
}
