//package in.co.mdg.campusBuddy.contacts;
//
//import android.content.ActivityNotFoundException;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.net.ConnectivityManager;
//import android.os.Bundle;
//import android.speech.RecognizerIntent;
//import android.support.design.widget.TabLayout;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.AutoCompleteTextView;
//import android.widget.CheckedTextView;
//import android.widget.FrameLayout;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//import in.co.mdg.campusBuddy.NetworkCheck;
//import in.co.mdg.campusBuddy.R;
//import in.co.mdg.campusBuddy.contacts.ContactsRecyclerAdapter.ClickListener;
//import in.co.mdg.campusBuddy.contacts.data_models.ContactSearchModel;
//import in.co.mdg.campusBuddy.contacts.data_models.CustomStringArrayGsonAdapter;
//import in.co.mdg.campusBuddy.contacts.data_models.Group;
//import io.realm.Realm;
//
///**
// * Created by rc on 29/6/15.
// */
//
//public class ContactsMainActivity extends AppCompatActivity implements ClickListener {
//
//    public static Boolean loadImages = true;
//    private AutoCompleteTextView searchBox;
//    private static final int REQ_CODE_SPEECH_INPUT = 100;
//    private static int SPEECHORCLEAR = 1;
//    private Realm realm;
//    private SearchSuggestionAdapter searchAdapter;
//    private FrameLayout dimLayout;
//    private LinearLayout searchBar;
//    private ImageView backButton;
//    private ProgressBar progressBar;
//    private DeptListFragment deptList;//, AToZ;
//    private View overlay;
//    private ImageButton closeBtn;
//    private CheckedTextView checkedTextView;
//    private TextView dataPackTV;
//    private ConnectivityManager connMgr;
////    private String contactsUrl = "https://www.sdsmdg.ml/cb/contacts.json";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_telephone_contacts);
//        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        connMgr = (ConnectivityManager)
//                this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        overlay = findViewById(R.id.settings);
//        closeBtn = (ImageButton) overlay.findViewById(R.id.close_btn);
//        checkedTextView = (CheckedTextView) overlay.findViewById(R.id.enable_images);
//        dataPackTV = (TextView) overlay.findViewById(R.id.data_pack_notif_tv);
//        dimLayout = (FrameLayout) findViewById(R.id.dim_layout);
//        final ImageView speechButton = (ImageView) findViewById(R.id.speechbutton);
//        backButton = (ImageView) findViewById(R.id.backbutton);
//        searchBox = (AutoCompleteTextView) findViewById(R.id.searchbox);
//        searchBar = (LinearLayout) findViewById(R.id.searchbar);
//        progressBar = (ProgressBar) findViewById(R.id.progressbar);
//        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
//        deptList = DeptListFragment.newInstance(1);
////        AToZ = DeptListFragment.newInstance(2);
//        setUpViewPager(viewPager);
//        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
//        tabLayout.setTabTextColors(Color.parseColor("#A1F5F5F5"), Color.parseColor("#FFF5F5F5"));
//
//        realm = Realm.getDefaultInstance();
//        checkDbExists();
//
//        closeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                overlay.setVisibility(View.GONE);
//            }
//        });
//        checkedTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((CheckedTextView) view).toggle();
//                loadImages = ((CheckedTextView) view).isChecked();
//                if (loadImages) {
//                    deptList.adapter.notifyDataSetChanged();
////                    AToZ.adapter.notifyDataSetChanged();
//                }
//            }
//        });
//
//        searchAdapter = new SearchSuggestionAdapter(this, R.layout.search_suggestion_listitem);
//        searchBox.setThreshold(1);
//        searchBox.setDropDownAnchor(R.id.searchbar);
//        searchBox.setAdapter(searchAdapter);
//        searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                final ContactSearchModel contact = searchAdapter.getItem(position);
//                realm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        ContactSearchModel historySearch = realm.where(ContactSearchModel.class).equalTo("name", contact.getName()).findFirst();
//                        if (contact.isDept()) {
//                            showDepartmentContacts(contact.getName());
//                        } else {
//                            if (historySearch == null) {
//                                contact.setDateAdded(new Date());
//                                contact.setHistorySearch(true);
//                                realm.copyToRealm(contact);
//                            } else {
//                                historySearch.setDateAdded(new Date());
//                            }
////                            if (contact.getDept().equals("Administration")) {
////                                Contact contact1 = realm.where(Contact.class).equalTo("designation", contact.getName()).findFirst();
////                                if (contact1 != null)
////                                    contact.setName(contact1.getName());
////                            }
//                            showContact(contact.getName(), contact.getDept());
//                        }
//                    }
//                });
//                searchBox.setText("");
//                backButton.performClick();
//            }
//        });
//        dimLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                backButton.performClick();
//            }
//        });
//        searchBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchBox.showDropDown();
//            }
//        });
//
////        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
////            @Override
////            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
////                if(actionId == EditorInfo.IME_NULL)
////                {
////                    search(v.getText().toString());
////                }
////                return true;
////            }
////        });
//        searchBox.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() > 0) {
//                    SPEECHORCLEAR = 2;
//                    speechButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_clear_black_24dp));
//                } else {
//                    SPEECHORCLEAR = 1;
//                    speechButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_mic_black_24dp));
//                }
//            }
//        });
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchBox.clearFocus();
//                dimLayout.setVisibility(View.GONE);
//                searchBar.setVisibility(View.GONE);
//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
//            }
//        });
//
//        speechButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (SPEECHORCLEAR) {
//                    case 1:
//                        promptSpeechInput();
//                        break;
//                    case 2:
//                        searchBox.setText("");
//                        SPEECHORCLEAR = 1;
//                        speechButton.setImageDrawable(
//                                ContextCompat.getDrawable(
//                                        speechButton.getContext()
//                                        , R.drawable.ic_mic_black_24dp));
//                        break;
//                }
//            }
//        });
//    }
//
//    private void setUpViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFrag(deptList, "Contact Groups");
////        adapter.addFrag(AToZ, "A TO Z LIST");
//        viewPager.setAdapter(adapter);
//    }
//
//    private void showContact(String name, String dept) {
//        searchBox.setText("");
//        Intent in = new Intent(this, ShowContact.class);
//        in.putExtra("name", name);
//        in.putExtra("dept", dept);
//        startActivity(in);
//    }
//
//    private void showDepartmentContacts(String deptName) {
//        Intent in = new Intent(this, ShowDepartmentContacts.class);
//        in.putExtra("dept_name", deptName);
//        startActivity(in);
//    }
//
//    private void showGroupDepartments(String groupName) {
//        Intent in = new Intent(this, ShowGroupDepartments.class);
//        in.putExtra("group_name", groupName);
//        startActivity(in);
//    }
//
//    private void checkDbExists() {
////        int status = NetworkCheck.chkStatus(connMgr);
//        if (realm.where(Group.class).count() == 0) {
//
//            try {
//                InputStream stream;
//                final StringBuilder buf = new StringBuilder();
//                stream = getAssets().open("contacts.min.json");
//                BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
//                String str;
//                while ((str = in.readLine()) != null) {
//                    buf.append(str);
//                }
//                in.close();
//                final Gson gson = new CustomStringArrayGsonAdapter().getGson();
//                realm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        List<Group> groups = gson.fromJson(String.valueOf(buf), new TypeToken<List<Group>>() {
//                        }.getType());
//                        realm.copyToRealm(groups);
////                            realm.createOrUpdateAllFromJson(Group.class, stream);
//                    }
//                });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
////        else {
////
////            if (status == 1 || status == 2)
////                new JSONTask().execute();
////        }
////        if (status == 2) {//mobile data warning
////            overlay.setVisibility(View.VISIBLE);
////            loadImages = false;
////            checkedTextView.setChecked(false);
////            dataPackTV.setVisibility(View.VISIBLE);
////        }
//    }
//
////    private void updateContacts(final String finalJson) {
////        if (isJSONArrayValid(finalJson)) {
////            try {
////                realm.executeTransaction(new Realm.Transaction() {
////                    @Override
////                    public void execute(Realm realm) {
////                        realm.createOrUpdateAllFromJson(Department.class, finalJson);
////                        //Do write something
////                    }
////                });
////            } catch (Exception e) {
////                e.printStackTrace();
////                return;
////            }
////            deptList.adapter.notifyDataSetChanged();
////            AToZ.adapter.notifyDataSetChanged();
////            progressBar.setVisibility(View.GONE);
////            Toast.makeText(ContactsMainActivity.this, "Contacts Updated", Toast.LENGTH_LONG).show();
////        }
////    }
//
////
////    public boolean isJSONArrayValid(String json) {
////        try {
////            new JSONArray(json);
////        } catch (JSONException ex1) {
////            return false;
////        }
////        return true;
////    }
//
////    public class JSONTask extends AsyncTask<Void, String, Boolean> {
////        String finalJson = "";
////
////        @Override
////        protected void onPreExecute() {
////            progressBar.setVisibility(View.VISIBLE);
////        }
////
////        @Override
////        protected Boolean doInBackground(Void... voids) {
////            if (NetworkCheck.isNetConnected()) {
////                HttpURLConnection connection = null;
////                BufferedReader reader = null;
////                try {
////                    URL url = new URL(contactsUrl);
////                    connection = (HttpURLConnection) url.openConnection();
////                    connection.connect();
////                    InputStream stream = connection.getInputStream();
////                    reader = new BufferedReader(new InputStreamReader(stream));
////                    String line;
////                    StringBuilder buffer = new StringBuilder();
////                    while ((line = reader.readLine()) != null) {
////                        buffer.append(line);
////                    }
////                    finalJson = buffer.toString();
////
////                } catch (IOException e) {
////                    e.printStackTrace();
////                    return false;
////                } finally {
////                    if (connection != null) {
////                        connection.disconnect();
////                    }
////                    try {
////                        if (reader != null) {
////                            reader.close();
////                        }
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }
////                return true;
////            } else {
////                return false;
////            }
////        }
////
////        @Override
////        protected void onPostExecute(Boolean result) {
////            final InputStream stream;
////            try {
////                stream = getAssets().open("contacts.json");
////                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
////                String line;
////                StringBuilder buffer = new StringBuilder();
////                while ((line = reader.readLine()) != null) {
////                    buffer.append(line);
////                }
////                String initialJson = buffer.toString();
////                if (result && finalJson.length() > 0 && !initialJson.contentEquals(finalJson)) {
////                    updateContacts(finalJson);
////                } else {
////                    progressBar.setVisibility(View.GONE);
////                }
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////
////        }
////    }
//
//    @Override
//    public void onBackPressed() {
//        if (searchBar.getVisibility() == View.VISIBLE)
//            backButton.performClick();
//        else {
//            finish();
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        realm.close();
//        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_up);
//        super.onDestroy();
//    }
//
//    private void promptSpeechInput() {
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
//                getString(R.string.speech_prompt));
//        try {
//            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
//        } catch (ActivityNotFoundException a) {
//            Toast.makeText(getApplicationContext(),
//                    getString(R.string.speech_not_supported),
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQ_CODE_SPEECH_INPUT:
//                if (resultCode == RESULT_OK && null != data) {
//                    ArrayList<String> result = data
//                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    searchBox.append(result.get(0));
//                    searchBox.requestFocus();
//                }
//                break;
//        }
//    }
//
//    @Override
//    public void itemClicked(int type, String contactName, String deptName, String groupName) {
//        switch (type) {
//            case 1:
//                showGroupDepartments(deptName);
//                break;
//            case 2:
//            case 3:
//                showContact(contactName, deptName);
//                break;
//            case 4:
//                showDepartmentContacts(deptName);
//
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.search) {
//            searchBar.setVisibility(View.VISIBLE);
//            dimLayout.setVisibility(View.VISIBLE);
//            if (searchBox.getText().toString().equals("")) {
//                searchAdapter.getFilter().filter(null);
//            }
//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
//            searchBox.requestFocus();
//            searchBox.showDropDown();
//            return true;
//        } else if (id == R.id.settings) {
//            int status = NetworkCheck.chkStatus(connMgr);
//            overlay.setVisibility(View.VISIBLE);
//            if (status == 2) {
//                dataPackTV.setVisibility(View.VISIBLE);
//            } else {
//                dataPackTV.setVisibility(View.GONE);
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_telephone_contacts, menu);
//        return true;
//    }
//
//
//}
//
