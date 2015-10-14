package com.cmg.android.bbcaccent;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.fragment.FeedbackFragment;
import com.cmg.android.bbcaccent.fragment.info.AboutFragment;
import com.cmg.android.bbcaccent.fragment.info.LicenceFragment;
import com.cmg.android.bbcaccent.fragment.tab.Preferences;
import com.cmg.android.bbcaccent.fragment.info.HelpFragment;
import com.cmg.android.bbcaccent.adapter.ListMenuAdapter;
import com.cmg.android.bbcaccent.auth.AccountManager;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.data.sqlite.WordDBAdapter;
import com.cmg.android.bbcaccent.fragment.FreeStyleFragment;
import com.cmg.android.bbcaccent.service.SyncDataService;
import com.cmg.android.bbcaccent.utils.AnalyticHelper;
import com.cmg.android.bbcaccent.utils.AppLog;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.google.gson.Gson;
import com.luhonghai.litedb.exception.LiteDatabaseException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends BaseActivity implements SearchView.OnQueryTextListener,
        SearchView.OnSuggestionListener {

    enum FragmentState {
        NULL(null),
        FREE_STYLE(FreeStyleFragment.class, ListMenuAdapter.MenuItem.FREESTYLE),
        LESSON(FreeStyleFragment.class, ListMenuAdapter.MenuItem.LESSON),
        HELP(HelpFragment.class, ListMenuAdapter.MenuItem.HELP),
        SETTINGS(Preferences.class, ListMenuAdapter.MenuItem.SETTING),
        ABOUT(AboutFragment.class, ListMenuAdapter.MenuItem.ABOUT),
        LICENCE(LicenceFragment.class, ListMenuAdapter.MenuItem.LICENCE),
        FEEDBACK(FeedbackFragment.class, ListMenuAdapter.MenuItem.FEEDBACK)
        ;
        Class<?> clazz;
        ListMenuAdapter.MenuItem menuItem;

        FragmentState(Class<?> clazz) {
            this.clazz = clazz;
        }

        FragmentState(Class<?> clazz, ListMenuAdapter.MenuItem menuItem) {
            this.clazz = clazz;
            this.menuItem = menuItem;
        }

        static FragmentState fromMenuItem(ListMenuAdapter.MenuItem menuItem) {
            for (FragmentState state : values()) {
                if (state.menuItem == menuItem) return state;
            }
            return NULL;
        }

        static FragmentState fromFragmentClassName(String className) {
            for (FragmentState state : values()) {
                if (state.clazz != null && state.clazz.getName().equals(className)) return state;
            }
            return NULL;
        }

        @Override
        public String toString() {
            return clazz == null ? "null" : clazz.getName();
        }
    }

    private FragmentState currentFragmentState = FragmentState.NULL;

    private DrawerLayout drawerLayout;

    private boolean isDrawerOpened;

    private MaterialMenuView materialMenu;

    private ListView listMenu;

    private AccountManager accountManager;

    private SearchView searchView;

    private WordDBAdapter dbAdapter;

    private CursorAdapter adapter;

    private ImageView imgAvatar;

    private TextView txtUserName;

    private TextView txtUserEmail;

    public void syncService(){
        Gson gson = new Gson();
        UserProfile profile = Preferences.getCurrentProfile(this);
        String jsonProfile = gson.toJson(profile);
        Intent mIntent = new Intent(this, SyncDataService.class);
        mIntent.putExtra("jsonProfile", jsonProfile);
        startService(mIntent);
    }
    /**
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserEmail = (TextView) findViewById(R.id.txtUserEmail);

        initListMenu();
        initCustomActionBar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                materialMenu.setTransformationOffset(
                        MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                        isDrawerOpened ? 2 - slideOffset : slideOffset
                );
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isDrawerOpened = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawerOpened = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_IDLE) {
                    if (isDrawerOpened) materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
                    else materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
                }
            }
        });
        accountManager = new AccountManager(this);
        checkProfile();
        syncService();
        switchFragment(ListMenuAdapter.MenuItem.FREESTYLE, null);
    }

    private void initListMenu() {
        listMenu = (ListView) findViewById(R.id.listMenu);
        ListMenuAdapter adapter = new ListMenuAdapter(this);
        listMenu.setAdapter(adapter);
        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListMenuAdapter.MenuItem menuItem = ListMenuAdapter.MenuItem.values()[position];
                switch (menuItem) {
                    case LOGOUT:
                        SweetAlertDialog d = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
                        d.setTitleText(getString(R.string.logout_account_message_title));
                        d.setContentText(getString(R.string.logout_account_message_content));
                        d.setConfirmText(getString(R.string.logout));
                        d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                UserProfile profile = Preferences.getCurrentProfile(MainActivity.this);
                                if (profile != null) {
                                    AnalyticHelper.sendUserLogout(MainActivity.this, profile.getUsername());
                                }
                                accountManager.logout();
                                MainActivity.this.finish();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                        d.setCancelText(getString(R.string.dialog_no));
                        d.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
                        d.show();
                        break;
                    default:
                        switchFragment(menuItem, null);
                }
            }
        });
        adapter.notifyDataSetChanged();
    }



    private void initCustomActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.main_action_bar);
        materialMenu = (MaterialMenuView) actionBar.getCustomView().findViewById(R.id.action_bar_menu);
        materialMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDrawerOpened) {
                    materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        isDrawerOpened = drawerLayout.isDrawerOpen(Gravity.LEFT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        if (null != searchView) {
            searchView.setFocusable(true);
            searchView.performClick();
            searchView.requestFocus();
            searchView.setIconified(true);
            dbAdapter = new WordDBAdapter();
            searchView.setQueryHint(getString(R.string.tint_search_word));
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            //  searchView.setIconifiedByDefault(false);
            searchView.setOnQueryTextListener(this);
            searchView.setOnSuggestionListener(this);
            try {
                adapter = new SimpleCursorAdapter(this, R.layout.search_word_item,
                        dbAdapter.getAll(),
                        new String[]{"word", "pronunciation"},
                        new int[]{R.id.txtWord, R.id.txtPhoneme},
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                searchView.setSuggestionsAdapter(adapter);
            } catch (LiteDatabaseException e) {
                SimpleAppLog.error("Could not open word database", e);
            }

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (isDrawerOpened) {
                    materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.menu_feedback:
                switchFragment(FragmentState.FEEDBACK, null);
                break;
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(final String s) {
        if (s.length() > 0) {
            hideSoftKeyboard();
            View v = getCurrentFocus();
            if (v != null)
                v.clearFocus();
            MainBroadcaster.getInstance().getSender().sendSearchWord(s);
        }
        return false;
    }

    private Runnable updateQueryRunnable = new Runnable() {
        @Override
        public void run() {
            updateQuery();
        }
    };

    private void updateQuery() {
        final Cursor c;
        try {
            c = dbAdapter.search(searchText);
            if (c.getCount() > 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchView.getSuggestionsAdapter().changeCursor(c);
                        //searchView.getSuggestionsAdapter().notifyDataSetChanged();
                    }
                });
            }
        } catch (LiteDatabaseException e) {
            SimpleAppLog.error("Could not open word database",e);
        }

    }

    private Handler updateQueryHandler = new Handler();

    private String searchText;

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.length() > 0) {
            searchText = s;
            updateQueryHandler.removeCallbacks(updateQueryRunnable);
            updateQueryHandler.postDelayed(updateQueryRunnable, 200);
            //updateQuery();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateQueryHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchSetting();
    }

    private void checkProfile() {
        UserProfile profile = Preferences.getCurrentProfile(this);
        if (profile == null) {
            AppLog.logString("No profile found");
            //openSettings();
            //switchFragment(HelpActivity.class);
        } else if (profile.getHelpStatus() == UserProfile.HELP_INIT) {
            AppLog.logString("Profile is not setup: " + profile.getUsername());
            //openSettings();
            //profile.setHelpStatus(UserProfile.HELP_SKIP);
            SweetAlertDialog dialogHelp = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
            dialogHelp.setCustomImage(android.R.drawable.ic_menu_search);
            dialogHelp.setTitleText(getString(R.string.help_search_icon_title));
            dialogHelp.setContentText(getString(R.string.help_search_icon));
            dialogHelp.setConfirmText(getString(R.string.dialog_ok));
            dialogHelp.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            dialogHelp.show();
            Preferences.setHelpStatusProfile(this, profile.getUsername(), UserProfile.HELP_SKIP);
            switchFragment(FragmentState.HELP, null);
        } else if (profile.getHelpStatus() == UserProfile.HELP_SKIP) {
            AppLog.logString("Display help dialog");
            showHelpDialog();
        } else {
            SimpleAppLog.info("Help status: " + profile.getHelpStatus());
            //showHelpDialog();
        }
    }

    private void fetchSetting() {
        try {
            UserProfile profile = Preferences.getCurrentProfile(this);
            if (profile != null) {
                txtUserName.setText(profile.getName());
                txtUserEmail.setText(profile.getUsername());
                if (!ImageLoader.getInstance().isInited()) {
                    ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
                }
                ImageLoader.getInstance().displayImage(profile.getProfileImage(), imgAvatar);
            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not fetch setting", e);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        accountManager.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        accountManager.stop();
    }


    public void hideSoftKeyboard() {
        if (this.getCurrentFocus() == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) this
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                .getWindowToken(), 0);
    }

    @Override
    public boolean onSuggestionSelect(int i) {
        selectSuggestionWord(i);
        return true;
    }

    private void selectSuggestionWord(int index) {
        try {
            AppLog.logString("Select suggestion: " + index);
            Cursor cursor = (Cursor) adapter.getItem(index);
            String s = dbAdapter.toObject(cursor).getWord();
            searchView.setQuery(s, true);
        } catch (Exception e) {
            SimpleAppLog.error("Could not select suggestion word", e);
        }
    }

    @Override
    public boolean onSuggestionClick(int i) {
        selectSuggestionWord(i);
        return true;
    }

    private void showHelpDialog() {
        final Dialog dialog = new Dialog(this, R.style.Theme_WhiteDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.help_dialog);

        dialog.findViewById(R.id.btnNever).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfile userProfile = Preferences.getCurrentProfile(MainActivity.this);
                if (userProfile != null) {
                    Preferences.setHelpStatusProfile(MainActivity.this, userProfile.getUsername(), UserProfile.HELP_NEVER);
                }
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btnSkip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(FragmentState.HELP, null);
                dialog.dismiss();
            }
        });

        dialog.setTitle(null);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void switchFragment(ListMenuAdapter.MenuItem menuItem, Bundle args) {
        FragmentState state = FragmentState.fromMenuItem(menuItem);
        if (state != null) {
            switchFragment(state, args);
        } else {
            SimpleAppLog.error("Could not found fragment state of menu item " + menuItem.toString());
        }
    }

    private void switchFragment(FragmentState state, Bundle args) {
        if (state.clazz == null) {
            SimpleAppLog.error("Could not found fragment state");
        } else {
            switchFragment(state.clazz, args);
        }
    }

    private void switchFragment(String className, Bundle args) {
        try {
            Class<?> clazz = Class.forName(className);
            switchFragment(clazz, args);
        } catch (ClassNotFoundException e) {
            SimpleAppLog.error("Could not found fragment class " + className,e);
        }
    }

    private void switchFragment(Class<?> clazz, Bundle args) {
        try {
            SimpleAppLog.debug("Switch to fragment class " + clazz.getName());
            FragmentState state = FragmentState.fromFragmentClassName(clazz.getName());
            if (state != FragmentState.NULL) {
                if (isDrawerOpened) {
                    //materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                if (state != currentFragmentState) {
                    switch (state) {
                        case FREE_STYLE:
                        case LESSON:
                            if (searchView != null) searchView.setVisibility(View.VISIBLE);
                            break;
                        default:
                            if (searchView != null) {
                                if (!searchView.isIconified())
                                    searchView.setIconified(true);
                                searchView.setVisibility(View.INVISIBLE);
                            }
                            break;
                    }
                    if (currentFragmentState != FragmentState.NULL) {
                        if (PreferenceFragment.class.isAssignableFrom(currentFragmentState.clazz)) {
                            android.app.Fragment fragment = getFragmentManager().findFragmentByTag(currentFragmentState.toString());
                            if (fragment != null)
                                getFragmentManager().beginTransaction()
                                        .hide(fragment)
                                        .commit();
                        } else {
                            Fragment fragment = getSupportFragmentManager().findFragmentByTag(currentFragmentState.toString());
                            if (fragment != null)
                                getSupportFragmentManager().beginTransaction().hide(fragment).commit();
                        }
                    }
                    if (PreferenceFragment.class.isAssignableFrom(clazz)) {
                        PreferenceFragment fragment = (PreferenceFragment) clazz.newInstance();
                        if (args != null)
                            fragment.setArguments(args);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.content, fragment, state.toString())
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Fragment fragment = (Fragment) clazz.newInstance();
                        fragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, fragment, state.toString())
                                .addToBackStack(null)
                                .commit();
                    }
                    currentFragmentState = state;
                }
            } else {
                SimpleAppLog.error("Could not found fragment state for class " + clazz.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            SimpleAppLog.error("Could not switch fragment",e);
        }
    }
}
