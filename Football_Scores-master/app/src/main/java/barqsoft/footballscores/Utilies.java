package barqsoft.footballscores;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies
{
    public static final int SERIE_A = 357;
    public static final int PREMIER_LEGAUE = 354;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int PRIMERA_DIVISION = 358;
    public static final int BUNDESLIGA = 351;
    public static String getLeague(int league_num, Context c)
    {
        Resources resources = c.getResources();
        switch (league_num)
        {
            case SERIE_A : return resources.getString(R.string.seriaa);
            case PREMIER_LEGAUE : return resources.getString(R.string.premierleague);
            case CHAMPIONS_LEAGUE : return resources.getString(R.string.champions_league);
            case PRIMERA_DIVISION : return resources.getString(R.string.primeradivison);
            case BUNDESLIGA : return resources.getString(R.string.bundesliga);
            default: return resources.getString(R.string.no_league);
        }
    }
    public static String getMatchDay(int match_day,int league_num, Context c)
    {
        Resources resources = c.getResources();
        if(league_num == CHAMPIONS_LEAGUE)
        {
            if (match_day <= 6)
            {
                return resources.getString(R.string.gs_matchday6);
            }
            else if(match_day == 7 || match_day == 8)
            {
                return resources.getString(R.string.first_knockout_round);
            }
            else if(match_day == 9 || match_day == 10)
            {
                return resources.getString(R.string.quarter_final);
            }
            else if(match_day == 11 || match_day == 12)
            {
                return resources.getString(R.string.semi_final);
            }
            else
            {
                return resources.getString(R.string.final_text);
            }
        }
        else
        {
            return resources.getString(R.string.matchdday_colo) + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals,int awaygoals)
    {
        if(home_goals < 0 || awaygoals < 0)
        {
            return " - ";
        }
        else
        {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (String teamname, Context c)
    {
        Resources resources = c.getResources();
        if (teamname==null){
            return R.drawable.no_icon;
        }else if (teamname.equals(resources.getString(R.string.arsenal))){
            return R.drawable.arsenal;
        }else if (teamname.equals(resources.getString(R.string.manchester))){
            return R.drawable.manchester_united;
        }else if (teamname.equals(resources.getString(R.string.swansea))){
            return R.drawable.swansea_city_afc;
        }else if (teamname.equals(resources.getString(R.string.leicester))){
            return R.drawable.leicester_city_fc_hd_logo;
        }else if (teamname.equals(resources.getString(R.string.everton))){
            return R.drawable.everton_fc_logo1;
        }else if (teamname.equals(resources.getString(R.string.west_ham))){
            return R.drawable.west_ham;
        }else if (teamname.equals(resources.getString(R.string.tottenham))){
            return R.drawable.tottenham_hotspur;
        }else if (teamname.equals(resources.getString(R.string.west_brom))){
            return R.drawable.west_bromwich_albion_hd_logo;
        }else if (teamname.equals(resources.getString(R.string.sunderland))){
            return R.drawable.sunderland;
        }else if (teamname.equals(resources.getString(R.string.stroke))){
            return R.drawable.stoke_city;
        }else {
            return R.drawable.no_icon;
        }
    }
}
