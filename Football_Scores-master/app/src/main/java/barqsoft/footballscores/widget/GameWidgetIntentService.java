package barqsoft.footballscores.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by seanholcomb on 9/4/15.
 */
//modified from Sunshines's TodayWidgetIntentService
public class GameWidgetIntentService extends IntentService {

    private static final String[] DATABASE_COLUMNS = {
            DatabaseContract.scores_table.LEAGUE_COL,
            DatabaseContract.scores_table.DATE_COL,
            DatabaseContract.scores_table.TIME_COL,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.MATCH_ID ,
            DatabaseContract.scores_table.MATCH_DAY
    };


    private static final int INDEX_LEAGUE = 0;
    private static final int INDEX_DATE = 1;
    private static final int INDEX_MATCHTIME = 2;
    private static final int INDEX_HOME = 3;
    private static final int INDEX_AWAY = 4;
    private static final int INDEX_HOME_GOALS = 5;
    private static final int INDEX_AWAY_GOALS = 6;
    private static final int INDEX_MATCH_ID = 7;
    private static final int INDEX_MATCHDAY = 8;

    public GameWidgetIntentService() {
        super("GameWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                GameWidgetProvider.class));

        // Get today's data from the ContentProvider
        Date todayDate = new Date(System.currentTimeMillis());
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = (mformat.format(todayDate));

        Uri TodayGameUri = DatabaseContract.scores_table.buildScoreWithDate();
        Cursor data = getContentResolver().query(TodayGameUri, DATABASE_COLUMNS, null,
                new String[]{dateString}, DatabaseContract.scores_table.DATE_COL + " ASC");
        if (data == null) {
            return;
        }

        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        // Extract the weather data from the Cursor
        int gameId = data.getInt(INDEX_MATCH_ID);
        String homeName = data.getString(INDEX_HOME);
        String awayName = data.getString(INDEX_AWAY);
        int homeCrest = Utilies.getTeamCrestByTeamName(homeName, getApplicationContext());
        int awayCrest = Utilies.getTeamCrestByTeamName(awayName, getApplicationContext());
        String score = Utilies.getScores(data.getInt(INDEX_HOME_GOALS), data.getInt(INDEX_AWAY_GOALS));
        String date = data.getString(INDEX_MATCHTIME);
        data.close();

        // Perform this loop procedure for each Today widget
        for (int appWidgetId : appWidgetIds) {

            int layoutId = R.layout.widget_game;

            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            views.setTextViewText(R.id.home_name, homeName);
            views.setTextViewText(R.id.away_name, awayName);
            views.setTextViewText(R.id.score_textview, score);
            views.setTextViewText(R.id.data_textview, date);
            views.setImageViewResource(R.id.home_crest, homeCrest);
            views.setImageViewResource(R.id.away_crest, awayCrest);

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            //launchIntent.putExtra("EXTRA_APPWIDGET_ID",appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
/*
    //may use for collection widget
    private int getWidgetHeight(AppWidgetManager appWidgetManager, int appWidgetId) {
        // Prior to Jelly Bean, widgets were always their default size
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return getResources().getDimensionPixelSize(R.dimen.widget_game_default_height);
        }
        // For Jelly Bean and higher devices, widgets can be resized - the current size can be
        // retrieved from the newly added App Widget Options
        return getWidgetHeightFromOptions(appWidgetManager, appWidgetId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private int getWidgetHeightFromOptions(AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)) {
            int minHeightDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
            // The width returned is in dp, but we'll convert it to pixels to match the other widths
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minHeightDp,
                    displayMetrics);
        }
        return  getResources().getDimensionPixelSize(R.dimen.widget_game_default_height);
    }
    */
}
