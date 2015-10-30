package com.cmg.android.bbcaccent;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.cmg.android.bbcaccent.adapter.ListMenuAdapter;
import com.cmg.android.bbcaccent.auth.AccountManager;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.data.dto.lesson.word.WordCollection;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonDBAdapterService;
import com.cmg.android.bbcaccent.extra.FragmentState;
import com.cmg.android.bbcaccent.extra.SwitchFragmentParameter;
import com.cmg.android.bbcaccent.fragment.Preferences;
import com.cmg.android.bbcaccent.service.SyncDataService;
import com.cmg.android.bbcaccent.utils.AnalyticHelper;
import com.cmg.android.bbcaccent.utils.AppLog;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.cmg.android.bbcaccent.view.dialog.LanguageDialog;
import com.google.gson.Gson;
import com.luhonghai.litedb.exception.LiteDatabaseException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends BaseActivity implements SearchView.OnQueryTextListener,
        SearchView.OnSuggestionListener {

    private FragmentState currentFragmentState = FragmentState.NULL;

    private Stack<FragmentState> fragmentStates = new Stack<FragmentState>();

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.listMenu)
    ListView listMenu;

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.imgAvatarCover)
    ImageView imgAvatarCover;

    @Bind(R.id.txtUserName)
    TextView txtUserName;

    @Bind(R.id.txtUserEmail)
    TextView txtUserEmail;

    private int listenerId;

    private boolean isDrawerOpened;

    private CursorAdapter adapter;

    private AccountManager accountManager;
    /**
     * Actionbar items
     */

    private MaterialMenuView materialMenu;

    private TextView txtTitle;

    private SearchView searchView;

    private Dialog dialogLanguage;

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
        ButterKnife.bind(this);
        initListMenu();
        initCustomActionBar();
        materialMenu.setVisibility(View.INVISIBLE);
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
        listenerId = MainBroadcaster.getInstance().register(new MainBroadcaster.ReceiverListener() {

            @Override
            public void onReceiveMessage(MainBroadcaster.Filler filler, Bundle bundle) {
                if (filler == MainBroadcaster.Filler.SWITCH_FRAGMENT) {
                    String className = bundle.getString(MainBroadcaster.Filler.Key.CLASS_NAME.toString());
                    SwitchFragmentParameter parameter = null;
                    if (bundle.containsKey(MainBroadcaster.Filler.Key.SWITCH_FRAGMENT_PARAMETER.toString())) {
                        Gson gson = new Gson();
                        parameter = gson.fromJson(bundle.getString(MainBroadcaster.Filler.Key.SWITCH_FRAGMENT_PARAMETER.toString()), SwitchFragmentParameter.class);
                    }
                    switchFragment(className, parameter, bundle);
                } else if (filler == MainBroadcaster.Filler.POP_BACK_STACK_FRAGMENT) {
                    popBackStackFragment();
                }
            }
        });
        displayRandomBackground();
        initDialogLanguage();
    }

    @OnItemClick(R.id.listMenu)
    public void clickListMenu(int position) {
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
            case SUBSCRIPTION:
                Intent i = new Intent(this, SubscriptionActivity.class);
                startActivity(i);
                break;
            default:
                switchFragment(menuItem, null, null);
        }
    }

    private void initListMenu() {
        ListMenuAdapter adapter = new ListMenuAdapter(this);
        listMenu.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void onClickMenuButton() {
        if (fragmentStates.size() > 0) {
            popBackStackFragment();
        } else {
            if (isDrawerOpened) {
                materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
                drawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        }
    }

    private void initCustomActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.main_action_bar);
            materialMenu = (MaterialMenuView) actionBar.getCustomView().findViewById(R.id.action_bar_menu);
            materialMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickMenuButton();
                }
            });
            txtTitle = (TextView) actionBar.getCustomView().findViewById(R.id.txtTitle);
        }
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
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        if (null != searchView) {
            searchView.setFocusable(true);
            searchView.performClick();
            searchView.requestFocus();
            searchView.setIconified(true);
            searchView.setQueryHint(getString(R.string.tint_search_word));
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            //  searchView.setIconifiedByDefault(false);
            searchView.setOnQueryTextListener(this);
            searchView.setOnSuggestionListener(this);
            adapter = new SimpleCursorAdapter(this, R.layout.search_word_item,
                        null,
                        new String[]{"WORD", "PRONUNCIATION"},
                        new int[]{R.id.txtWord, R.id.txtPhoneme},
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            searchView.setSuggestionsAdapter(adapter);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onClickMenuButton();
                break;
            case R.id.menu_feedback:
                switchFragment(FragmentState.FEEDBACK, null, null);
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
        try {
            final Cursor c = (searchText != null && searchText.length() > 0)  ? LessonDBAdapterService.getInstance().searchWord(searchText) : null;
            runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            searchView.getSuggestionsAdapter().changeCursor(c);

                    }
            });
        } catch (LiteDatabaseException e) {
            SimpleAppLog.error("Could not open word database",e);
        }
    }

    private Handler updateQueryHandler = new Handler();

    private String searchText;

    @Override
    public boolean onQueryTextChange(String s) {
        searchText = s;
        updateQueryHandler.removeCallbacks(updateQueryRunnable);
        updateQueryHandler.postDelayed(updateQueryRunnable, 200);
        //updateQuery();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainBroadcaster.getInstance().unregister(listenerId);
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
//            SweetAlertDialog dialogHelp = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
//            dialogHelp.setCustomImage(android.R.drawable.ic_menu_search);
//            dialogHelp.setTitleText(getString(R.string.help_search_icon_title));
//            dialogHelp.setContentText(getString(R.string.help_search_icon));
//            dialogHelp.setConfirmText(getString(R.string.dialog_ok));
//            dialogHelp.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                @Override
//                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                    sweetAlertDialog.dismissWithAnimation();
//                }
//            });
//            dialogHelp.show();
//            Preferences.setHelpStatusProfile(this, profile.getUsername(), UserProfile.HELP_SKIP);
//            switchFragment(FragmentState.HELP, null, null);
        } else if (profile.getHelpStatus() == UserProfile.HELP_SKIP) {
            AppLog.logString("Display help dialog");
            //showHelpDialog();
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
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.drawable.default_avatar)
                        .showImageOnFail(R.drawable.default_avatar)
                        .build();
                ImageLoader.getInstance().displayImage(profile.getProfileImage(), imgAvatar, options);
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
            String s = LessonDBAdapterService.getInstance().toObject(cursor, WordCollection.class).getWord();
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
                switchFragment(FragmentState.HELP, null, null);
                dialog.dismiss();
            }
        });

        dialog.setTitle(null);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void switchFragment(ListMenuAdapter.MenuItem menuItem, SwitchFragmentParameter parameter, Bundle args) {
        FragmentState state = FragmentState.fromMenuItem(menuItem);
        if (state != null) {
            switchFragment(state, parameter, args);
        } else {
            SimpleAppLog.error("Could not found fragment state of menu item " + menuItem.toString());
        }
    }

    private void switchFragment(FragmentState state, SwitchFragmentParameter parameter, Bundle args) {
        if (state.clazz == null) {
            SimpleAppLog.error("Could not found fragment state");
        } else {
            switchFragment(state.clazz, parameter, args);
        }
    }

    private void switchFragment(String className,SwitchFragmentParameter parameter, Bundle args) {
        try {
            Class<?> clazz = Class.forName(className);
            switchFragment(clazz, parameter, args);
        } catch (ClassNotFoundException e) {
            SimpleAppLog.error("Could not found fragment class " + className,e);
        }
    }

    private void popBackStackFragment() {
        if (fragmentStates.size() == 0) return;
        if (android.app.Fragment.class.isAssignableFrom(currentFragmentState.clazz)) {
            findViewById(R.id.contentV4).setVisibility(View.GONE);
            findViewById(R.id.content).setVisibility(View.VISIBLE);
            getFragmentManager().popBackStackImmediate();
        } else {
            findViewById(R.id.contentV4).setVisibility(View.VISIBLE);
            findViewById(R.id.content).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStackImmediate();
        }
        currentFragmentState = fragmentStates.pop();
        if (fragmentStates.size() > 0) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            materialMenu.animateState(MaterialMenuDrawable.IconState.BURGER);
        }
        switch (currentFragmentState) {
            case FREE_STYLE:
                if (searchView != null) searchView.setVisibility(View.VISIBLE);
                break;
            default:
                if (searchView != null) {
                    if (!searchView.isIconified())
                        searchView.setIconified(true);
                    searchView.setVisibility(View.GONE);
                }
                break;
        }
        txtTitle.setText(currentFragmentState.getTitle());
    }

    private void switchFragment(Class<?> clazz, SwitchFragmentParameter parameter, Bundle args) {
        try {
            if (parameter == null) parameter = new SwitchFragmentParameter();
            SimpleAppLog.debug("Switch to fragment class " + clazz.getName());
            FragmentState state = FragmentState.fromFragmentClassName(clazz.getName());
            materialMenu.setVisibility(View.VISIBLE);
            if (state != FragmentState.NULL) {
                if (isDrawerOpened) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                if (state != currentFragmentState) {
                    switch (state) {
                        case FREE_STYLE:
                            if (searchView != null) searchView.setVisibility(View.VISIBLE);
                            break;
                        default:
                            if (searchView != null) {
                                if (!searchView.isIconified())
                                    searchView.setIconified(true);
                                searchView.setVisibility(View.GONE);
                            }
                            break;
                    }
                    if (android.app.Fragment.class.isAssignableFrom(clazz)) {
                        findViewById(R.id.contentV4).setVisibility(View.GONE);
                        findViewById(R.id.content).setVisibility(View.VISIBLE);
                        android.app.Fragment fragment = getFragmentManager().findFragmentByTag(state.toString());
                        if (fragment == null || parameter.isCreateNew()) {
                            fragment = (android.app.Fragment) clazz.newInstance();
                            fragment.setArguments(args);

                        }
                        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        if (parameter.isAddToBackStack())
                            transaction.addToBackStack(state.toString());
                        if (parameter.isUseAnimation())
                            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                    R.anim.enter_from_left, R.anim.exit_to_right);
                        transaction.replace(R.id.content, fragment, state.toString());
                        transaction.commit();
                    } else {
                        findViewById(R.id.contentV4).setVisibility(View.VISIBLE);
                        findViewById(R.id.content).setVisibility(View.GONE);
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag(state.toString());
                        if (fragment == null || parameter.isCreateNew()) {
                            fragment = (Fragment) clazz.newInstance();
                            fragment.setArguments(args);
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        if (parameter.isAddToBackStack())
                            transaction.addToBackStack(state.toString());
                        if (parameter.isUseAnimation())
                            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                    R.anim.enter_from_left, R.anim.exit_to_right);
                        transaction.replace(R.id.contentV4, fragment, state.toString());
                        transaction.commit();
                    }
                    if (parameter.isAddToBackStack())
                        fragmentStates.push(currentFragmentState);

                    if (fragmentStates.size() > 0) {
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        materialMenu.animateState(MaterialMenuDrawable.IconState.ARROW);
                    } else {
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
                    }
                    if (parameter.getTitle() != null) {
                        state.setTitle(parameter.getTitle());
                    }
                    txtTitle.setText(state.getTitle());
                    currentFragmentState = state;
                }
            } else {
                SimpleAppLog.error("Could not found fragment state for class " + clazz.getName());
            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not switch fragment",e);
        }
    }

    @Override
    public void onBackPressed() {
        if (fragmentStates.size() > 0) {
            popBackStackFragment();
            return;
        } else {
            super.onBackPressed();
        }
    }

    private void displayRandomBackground() {
        String img = "drawable://" + R.drawable.london_cover;
        try {
            String dir = "background";
            String[] files = getResources().getAssets().list(dir);
            if (files != null && files.length > 0) {
                Random r = new Random();
                int index = 0;
                if (files.length > 1) {
                    index = r.nextInt(files.length + 1);
                    if (index < 0 || index > files.length) {
                        index = 0;
                    }
                }
                if (index != files.length) {
                    img = "assets://" + dir + File.separator + files[index];
                }
            }
        } catch (IOException e) {
            SimpleAppLog.error("could not display background", e);
        }
        SimpleAppLog.debug("display background: " + img);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.london_cover)
                .showImageOnFail(R.drawable.london_cover)
                .build();
        ImageLoader.getInstance().displayImage(img, imgAvatarCover, options);
    }

    private void initDialogLanguage() {
        if (searchView != null)
            searchView.setVisibility(View.GONE);
        dialogLanguage = new LanguageDialog(this);
        UserProfile userProfile = Preferences.getCurrentProfile();
        if (userProfile != null && userProfile.getSelectedCountry() == null) {
            dialogLanguage.show();
            dialogLanguage.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    switchFragment(ListMenuAdapter.MenuItem.LESSON, null, null);
                }
            });
        } else {
            switchFragment(ListMenuAdapter.MenuItem.LESSON, null, null);
        }
    }
}
