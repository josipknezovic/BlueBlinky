package layout;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.mate.blueblinky.BluetoothService;
import com.example.mate.blueblinky.MainActivity;
import com.example.mate.blueblinky.R;

import java.io.IOException;

/**
 * Implementation of App Widget functionality.
 */
public class BlueWidget extends AppWidgetProvider {

    public static final String WIDGET_OPEN_CLICKED = "WidgetOpenButtonClicked";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, BluetoothService.class);
        intent.setAction(WIDGET_OPEN_CLICKED);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pending = PendingIntent.getService(context, 0, intent, 0);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.blue_widget);
        views.setOnClickPendingIntent(R.id.button3, pending);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

    /*@Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context,intent);

        if(intent.getAction().equals(OPEN_CLICKED)) {

        }

    }*/
}

