package com.menemi.filter;


import android.support.v7.app.AppCompatActivity;

/**
 * Created by tester03 on 22.06.2016.
 */
public class FilterActivity extends AppCompatActivity
{

    /*final  String[] hereTo = {"Make new friends", "Chat", "Date"};
    Context ctx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_layout);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        final Spinner spinner = (Spinner)findViewById(R.id.spinner);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Filter");
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        final LinearLayout withMaleButton = (LinearLayout)findViewById(R.id.check_male_button);
        final CheckBox male_checkbox = (CheckBox)findViewById(R.id.checkbox_male);
        final LinearLayout withFemaleButton = (LinearLayout)findViewById(R.id.check_female_button);
        final CheckBox female_checkbox = (CheckBox)findViewById(R.id.checkbox_female);
        final LinearLayout hereToSpinner = (LinearLayout) findViewById(R.id.hereToSpinner);
        assert withMaleButton != null;
        withMaleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (male_checkbox.isChecked()){

                    male_checkbox.setChecked(false);
                    Toast.makeText(getApplicationContext(),"Unchecked male",Toast.LENGTH_SHORT).show();
                } else {
                    male_checkbox.setChecked(true);
                    Toast.makeText(getApplicationContext(),"Checked male",Toast.LENGTH_SHORT).show();
                }
            }
        });

        assert withFemaleButton != null;
        withFemaleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (female_checkbox.isChecked()){

                    female_checkbox.setChecked(false);
                    Toast.makeText(getApplicationContext(),"Unchecked female",Toast.LENGTH_SHORT).show();
                } else {
                    female_checkbox.setChecked(true);
                    Toast.makeText(getApplicationContext(),"Checked female",Toast.LENGTH_SHORT).show();
                }
            }
        });
        assert hereToSpinner != null;
        hereToSpinner.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //final String[] items = {"1","2","3"};

// dialog list icons: some examples here
                final int[] icons = {
                        R.drawable.chat_normal,
                        R.drawable.people,
                        android.R.drawable.ic_menu_delete
                };

                final ListAdapter adapter = new ArrayAdapter<String>(
                        getApplicationContext(), R.layout.list_item, hereTo) {


                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        if (convertView == null) {
                            convertView = inflater.inflate(R.layout.list_item, null);
                        }
                            ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
                        TextView title = (TextView) convertView.findViewById(R.id.title);
                        title.setText(hereTo[position]);
                        icon.setImageResource(icons[position]);
                        title.setTextColor(Color.BLACK);
                        return convertView;
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(FilterActivity.this);
                builder.setTitle("I'm here to");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(getApplicationContext(),adapter.getItem(0) + "",Toast.LENGTH_SHORT).show();

                    }
                });
                builder.create();
                if (! ((Activity) FilterActivity.this).isFinishing()) {
                    builder.show();
            }

            }});
        RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<Integer>(this);
        // Set the range
        //rangeSeekBar.setRangeValues(18, 32);
        rangeSeekBar.setNotifyWhileDragging(true);
       // rangeSeekBar.setSelectedMinValue(18);
       // rangeSeekBar.setSelectedMaxValue(32);
        // Seek bar for which we will set text color in code
        RangeSeekBar rangeSeekBarTextColorWithCode = (RangeSeekBar) findViewById(R.id.rangeSeekBarTextColorWithCode);
        rangeSeekBarTextColorWithCode.setTextAboveThumbsColorResource(android.R.color.holo_red_dark);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_in_filter_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            onBackPressed();
        }
        else {

            if (id == R.id.acceptedFilterSettings)
            {
                Toast.makeText(getApplicationContext(),"Filter settings accepted",Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }*/





    /*public void Dialog()
    {
        AlertDialog.Builder b = new AlertDialog.Builder(FilterActivity.this);
        b.setTitle("I'am here to");
        b.setItems(hereTo, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        b.create();
        b.show();
    }*/

}
