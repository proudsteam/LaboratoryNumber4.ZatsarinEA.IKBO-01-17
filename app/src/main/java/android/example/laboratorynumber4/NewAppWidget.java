package android.example.laboratorynumber4;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    CalendarView calendarView;
    final static String NAME_OF_SHARED = "daysBetween";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        String daysBetween = context.getSharedPreferences(NAME_OF_SHARED, Context.MODE_PRIVATE).getString("days" + appWidgetId, "");
        if (widgetText == null) return;

        Log.d("Zheka", "textForWidget " + daysBetween);
        Intent intent = new Intent(context,MainActivity.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,1,intent,0);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);


        // Instruct the widget manager to update the widget
        views.setTextViewText(R.id.appwidget_text, daysBetween);



        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("Zheka","onUpdate");

        SharedPreferences mSettings =context.getSharedPreferences(
                NAME_OF_SHARED, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate = mdformat.format(calendar.getTime());
       // Log.d("Zheka", "current time= " + strDate + "s1= " + strDate.substring(0,2) + "s2= " + strDate.substring(3,5));
        if (strDate.substring(0,2).equals("00"))
        {
            int timeNeeded = Integer.parseInt(strDate.substring(3,5));
            if ((timeNeeded <=30) && (timeNeeded >=0))
            {
                for (int appWidgetId : appWidgetIds) {
                if ((!mSettings.getString("days" + appWidgetId, "").equals("")))
                {
                    int dayBetween = Integer.parseInt(mSettings.getString("days" + appWidgetId, ""));
                    if (dayBetween > 0) {
                        dayBetween--;
                        editor.putString("days" + appWidgetId,"" + dayBetween);
                        editor.apply();
                    }
                    else
                    {
                        editor.putString("days" + appWidgetId,"");
                        editor.apply();
                    }
                }
                    updateAppWidget(context, appWidgetManager, appWidgetId);
                }

            }
            else
            {
                for (int appWidgetId : appWidgetIds) {




                    updateAppWidget(context, appWidgetManager, appWidgetId);
                }
            }
        }
        else {

            for (int appWidgetId : appWidgetIds) {


                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        // Удаляем Preferences
        SharedPreferences.Editor editor = context.getSharedPreferences(
                NAME_OF_SHARED, Context.MODE_PRIVATE).edit();
        for (int widgetID : appWidgetIds) {
            editor.remove("days" + widgetID);
        }
        editor.apply();
    }

}

