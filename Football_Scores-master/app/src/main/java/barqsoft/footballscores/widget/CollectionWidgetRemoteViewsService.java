package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by seanholcomb on 9/4/15.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CollectionWidgetRemoteViewsService extends RemoteViewsService {
    private static final String SCORES_BY_DATE =
            DatabaseContract.scores_table.DATE_COL + " LIKE ";
    private final int DAYS_AWAY=2;
    private final int TOTAL_DAYS=DAYS_AWAY*2+1;
    private final int MILIS_IN_DAY=86400000;

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

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            //number of days past and future to be shown in app

            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();
                // Get today's data from the ContentProvider
                Date todayDate;
                String questionString="";
                String dateString = "";
                String[] dateStrings;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                for (int i=-DAYS_AWAY;i<=DAYS_AWAY; i++) {
                    todayDate = new Date(System.currentTimeMillis()+((i)*MILIS_IN_DAY));
                    dateString = dateString + "," + (format.format(todayDate));
                    questionString= questionString+"?";
                }
                dateString = dateString.substring(1);
                dateStrings=dateString.split(",");
                String selection= SCORES_BY_DATE+ "?";//questionString;

                //dateString=dateStrings[0].substring(0,-1)+"%";


                Uri TodayGameUri = DatabaseContract.scores_table.buildScoreWithDate();
                Log.e("EEEEEEEEE", dateString);
                data = getContentResolver().query(
                        TodayGameUri,
                        DATABASE_COLUMNS,
                        selection,
                        new String[] {dateStrings[0]},
                        DatabaseContract.scores_table.DATE_COL + " ASC");
                if (data!=null) {
                    String ee = data.getCount()+" ";
                    Log.e("EEEEEEEEE", ee);
                }
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.scores_list_item);
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    Log.e("EEEEEEEEE", "g");
                    return views;
                }

                Log.e("EEEEEEEEE", "h");
                int gameId = data.getInt(INDEX_MATCH_ID);
                String homeName = data.getString(INDEX_HOME);
                String awayName = data.getString(INDEX_AWAY);
                int homeCrest = Utilies.getTeamCrestByTeamName(homeName, getApplicationContext());
                int awayCrest = Utilies.getTeamCrestByTeamName(awayName, getApplicationContext());
                String score = Utilies.getScores(data.getInt(INDEX_HOME_GOALS), data.getInt(INDEX_AWAY_GOALS));
                String date = data.getString(INDEX_MATCHTIME);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.scores_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(INDEX_MATCH_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}