package mobileDevelopment.com.root.campusbuddy;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ContactDetails extends ActionBarActivity {
    String contact,emailid;
    Toolbar toolbar;
    int contact1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        toolbar = (Toolbar) findViewById(R.id.tool_bar1);
        toolbar.setTitle("Details of the Person");
        setSupportActionBar(toolbar);


        Intent m=getIntent();
        Bundle b=m.getExtras();
        contact=b.getString("Clicked Contact number");
        emailid=b.getString("Clicked email-id");

        EditText edit_contact=(EditText)findViewById(R.id.contactno);
        EditText edit_email=(EditText)findViewById(R.id.emailid);
        Button callbutton=(Button)findViewById(R.id.callbutton);
        Button emailbutton=(Button)findViewById(R.id.emailbutton);

//        contact1=Integer.parseInt(contact);


        TelephoneContacts tc=new TelephoneContacts();
        edit_contact.setText(contact+"");
        edit_contact.setTextColor(Color.parseColor("#000000"));
        edit_email.setText(emailid+"");
        edit_email.setTextColor(Color.parseColor("#000000"));

        callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact));
                startActivity(intent);
            }
        });

        emailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setType("plain/text");
//                intent.setData(Uri.parse("www.gmail.com"));
                intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emailid});
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
