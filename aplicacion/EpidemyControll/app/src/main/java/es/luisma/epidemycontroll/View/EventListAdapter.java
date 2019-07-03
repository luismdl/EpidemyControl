package es.luisma.epidemycontroll.View;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import es.luisma.epidemycontroll.R;

public class EventListAdapter extends ArrayAdapter {

    //to reference the Activity
    private final Activity context;


    //to store the list of countries
    private final String[] nameArray;

    //to store the list of countries
    private final String[] infoArray;


    public EventListAdapter(Activity context, String[] nameArrayParam, String[] infoArrayParam){

        super(context, R.layout.eventlist_line , nameArrayParam);
        this.context=context;
        this.nameArray = nameArrayParam;
        this.infoArray = infoArrayParam;


    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.eventlist_line, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.eventTxt1);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.eventTxt2);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameArray[position]);
        infoTextField.setText(infoArray[position]);

        return rowView;

    };

}
