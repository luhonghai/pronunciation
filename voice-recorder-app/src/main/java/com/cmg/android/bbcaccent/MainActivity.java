package com.cmg.android.bbcaccent;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
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
import com.cmg.android.bbcaccent.subscription.IAPFactory;
import com.cmg.android.bbcaccent.utils.AnalyticHelper;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.AppLog;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.cmg.android.bbcaccent.view.cardview.CircleCardView;
import com.cmg.android.bbcaccent.view.dialog.DefaultCenterDialog;
import com.cmg.android.bbcaccent.view.dialog.FullscreenDialog;
import com.cmg.android.bbcaccent.view.dialog.LanguageDialog;
import com.cocosw.bottomsheet.BottomSheet;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.google.gson.Gson;
import com.luhonghai.litedb.exception.LiteDatabaseException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends BaseActivity implements SearchView.OnQueryTextListener,
        SearchView.OnSuggestionListener {

    public interface MainAction {
        void execute(MainActivity mainActivity);
    }

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

    private Menu menu;

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

    private long lastPopbackPress;

    private BillingProcessor bp;

    private Dialog dialogSubscription;

    private Dialog chooseActivateType;

    private Dialog dialogLicence;

    private SweetAlertDialog dialogProgress;

    private boolean willCloseAfterSubscription = false;

    private boolean doubleBackToExitPressedOnce = false;

    CallbackManager callbackManager;

    ShareDialog shareDialog;

    private void showProcessDialog() {
        dialogProgress = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.setTitleText(getString(R.string.processing));
        dialogProgress.setCancelable(false);
        dialogProgress.show();
    }

    private void hideProcessDialog() {
        if (dialogProgress != null && dialogProgress.isShowing())
            dialogProgress.dismissWithAnimation();
    }

    public void showActiveFullVersionDialog() {
        showActiveFullVersionDialog(false);
    }

    public void showActiveFullVersionDialog(boolean showLogout) {
        if (dialogSubscription == null) {
            dialogSubscription = new FullscreenDialog(this, R.layout.active_subscription);
            dialogSubscription.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (willCloseAfterSubscription) {
                        MainActivity.this.finish();
                    }
                }
            });
            AndroidHelper.updateShareButton((CircleCardView) dialogSubscription.findViewById(R.id.btnShare));
            dialogSubscription.findViewById(R.id.btnShare).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showShareAction();
                }
            });
            dialogSubscription.findViewById(R.id.btnActivate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chooseActivateType == null) {
                        chooseActivateType = new DefaultCenterDialog(MainActivity.this, R.layout.dialog_subscription);
                        chooseActivateType.findViewById(R.id.btnMonthly).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (bp.isInitialized()) {
                                    bp.subscribe(MainActivity.this, IAPFactory.Subscription.MONTHLY.toString());
                                }
                            }
                        });
                        chooseActivateType.findViewById(R.id.btnYearly).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (bp.isInitialized()) {
                                    bp.subscribe(MainActivity.this, IAPFactory.Subscription.YEARLY.toString());
                                }
                            }
                        });
                        chooseActivateType.findViewById(R.id.btnActivateLicense).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (dialogLicence == null) {
                                    dialogLicence = new DefaultCenterDialog(MainActivity.this, R.layout.dialog_license_subscription);
                                    UserProfile userProfile = Preferences.getCurrentProfile();
                                    List<UserProfile.LicenseData> licenseDataList = userProfile.getLicenseData();
                                    if (licenseDataList != null && licenseDataList.size() > 0) {
                                        dialogLicence.findViewById(R.id.llSwitchLicense).setVisibility(View.VISIBLE);
                                        Spinner spinner = (Spinner) dialogLicence.findViewById(R.id.spinnerLicense);
                                        ArrayAdapter<UserProfile.LicenseData> spinnerArrayAdapter = new ArrayAdapter<UserProfile.LicenseData>(MainActivity.this,
                                                android.R.layout.simple_spinner_item, licenseDataList);
                                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinner.setAdapter(spinnerArrayAdapter);
                                        dialogLicence.findViewById(R.id.btnSwitchLicense).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                doSwitchLicence();
                                            }
                                        });
                                    } else {
                                        dialogLicence.findViewById(R.id.llSwitchLicense).setVisibility(View.GONE);
                                    }
                                    dialogLicence.findViewById(R.id.btnActivateLicense).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            doActivateLicence();
                                        }
                                    });
                                }
                                if (!dialogLicence.isShowing())
                                    dialogLicence.show();
                            }
                        });

                    }
                    if (!chooseActivateType.isShowing())
                        chooseActivateType.show();
                }
            });
        }
        dialogSubscription.findViewById(R.id.btnLogout).setVisibility(showLogout ? View.VISIBLE : View.GONE);
        dialogSubscription.findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
        if (!dialogSubscription.isShowing())
            dialogSubscription.show();
    }

    private void doUpdateFullVersion() {
        initListMenu();
        willCloseAfterSubscription = false;
        MainBroadcaster.getInstance().getSender().sendMessage(MainBroadcaster.Filler.UPDATE_FULL_VERSION, null);
    }

    private void doSwitchLicence() {
        final UserProfile profile = Preferences.getCurrentProfile(this);
        if (profile != null) {
            UserProfile.LicenseData licenseData = (UserProfile.LicenseData) ((Spinner) dialogLicence.findViewById(R.id.spinnerLicense)).getSelectedItem();
            if (licenseData != null) {
                profile.setLicenseCode(licenseData.getCode());
                SimpleAppLog.error("Start switch license: " + profile.getLicenseCode());
                dialogLicence.findViewById(R.id.btnActivateLicense).setEnabled(false);
                dialogLicence.findViewById(R.id.btnSwitchLicense).setEnabled(false);
                showProcessDialog();
                accountManager.switchLicense(profile, new AccountManager.AuthListener() {
                    @Override
                    public void onError(final String message, Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialogLicence.findViewById(R.id.btnActivateLicense).setEnabled(true);
                                dialogLicence.findViewById(R.id.btnSwitchLicense).setEnabled(true);
                                hideProcessDialog();
                                SweetAlertDialog d = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
                                d.setTitleText(getString(R.string.could_not_activate_licence_code));
                                d.setContentText(message);
                                d.setConfirmText(getString(R.string.dialog_ok));
                                d.show();
                            }
                        });
                    }

                    @Override
                    public void onSuccess() {
                        profile.setIsActivatedLicence(true);
                        AnalyticHelper.sendEvent(AnalyticHelper.Category.SUBSCRIPTION, AnalyticHelper.Action.SWITCH_LICENCE, profile.getLicenseCode() + " " + profile.getUsername());
                        Preferences.updateProfile(MainActivity.this, profile);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                doUpdateFullVersion();
                                dialogLicence.findViewById(R.id.btnActivateLicense).setEnabled(true);
                                dialogLicence.findViewById(R.id.btnSwitchLicense).setEnabled(true);
                                hideProcessDialog();
                                SweetAlertDialog d = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                d.setTitleText(getString(R.string.switch_licence_success_title));
                                d.setContentText(getString(R.string.product_purchased_message));
                                d.setConfirmText(getString(R.string.dialog_ok));
                                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                        if (dialogLicence != null && dialogLicence.isShowing())
                                            dialogLicence.dismiss();
                                        if (chooseActivateType != null && chooseActivateType.isShowing())
                                            chooseActivateType.dismiss();
                                        if (dialogSubscription != null && dialogSubscription.isShowing())
                                            dialogSubscription.dismiss();
                                    }
                                });
                                d.show();
                            }
                        });
                    }
                });
            } else {
                SimpleAppLog.error("No selected licence found");
            }
        } else {
            SimpleAppLog.error("No profile found");
        }
    }

    private void doActivateLicence() {
        final UserProfile profile = Preferences.getCurrentProfile(this);
        if (profile != null) {
            profile.setLicenseCode(((TextView) dialogLicence.findViewById(R.id.txtCode)).getText().toString());
            SimpleAppLog.error("Start active license: " + profile.getLicenseCode());
            if (profile.getLicenseCode().length() > 0) {
                dialogLicence.findViewById(R.id.btnActivateLicense).setEnabled(false);
                showProcessDialog();
                accountManager.activeLicense(profile, new AccountManager.AuthListener() {
                    @Override
                    public void onError(final String message, Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialogLicence.findViewById(R.id.btnActivateLicense).setEnabled(true);
                                hideProcessDialog();
                                SweetAlertDialog d = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
                                d.setTitleText(getString(R.string.could_not_activate_licence_code));
                                d.setContentText(message);
                                d.setConfirmText(getString(R.string.dialog_ok));
                                d.show();
                            }
                        });
                    }

                    @Override
                    public void onSuccess() {
                        AnalyticHelper.sendEvent(AnalyticHelper.Category.SUBSCRIPTION, AnalyticHelper.Action.USE_LICENCE, profile.getLicenseCode() + " " + profile.getUsername());
                        profile.setIsActivatedLicence(true);
                        Preferences.updateProfile(MainActivity.this, profile);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                doUpdateFullVersion();
                                dialogLicence.findViewById(R.id.btnActivateLicense).setEnabled(true);
                                hideProcessDialog();
                                SweetAlertDialog d = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                d.setTitleText(getString(R.string.product_purchased_title));
                                d.setContentText(getString(R.string.product_purchased_message));
                                d.setConfirmText(getString(R.string.dialog_ok));
                                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                        if (dialogLicence != null && dialogLicence.isShowing())
                                            dialogLicence.dismiss();
                                        if (chooseActivateType != null && chooseActivateType.isShowing())
                                            chooseActivateType.dismiss();
                                        if (dialogSubscription != null && dialogSubscription.isShowing())
                                            dialogSubscription.dismiss();
                                    }
                                });
                                d.show();
                            }
                        });
                    }
                });
            } else {
                SweetAlertDialog d = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                d.setTitleText(getString(R.string.missing_licence_code));
                d.setContentText(getString(R.string.please_enter_licence_code));
                d.setConfirmText(getString(R.string.dialog_ok));
                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                d.show();
            }
        } else {
            SimpleAppLog.error("No profile found");
        }
    }

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
                    int count = 0;
                    if (bundle.containsKey(MainBroadcaster.Filler.Key.COUNT.toString())) {
                        count = bundle.getInt(MainBroadcaster.Filler.Key.COUNT.toString());
                    }
                    popBackStackFragment(count);
                } else if (filler == MainBroadcaster.Filler.CLOSE_MAIN_ACTIVITY) {
                    MainActivity.this.finish();
                }
            }
        });
        displayRandomBackground();
        initDialogLanguage();

        bp = IAPFactory.getBillingProcessor(this, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(String productId, TransactionDetails details) {

                UserProfile userProfile = Preferences.getCurrentProfile();
                AnalyticHelper.sendEvent(AnalyticHelper.Category.SUBSCRIPTION, AnalyticHelper.Action.BUY_SUBSCRIPTION, productId + " " + userProfile.getUsername());
                userProfile.setIsSubscription(true);
                Preferences.updateProfile(MainActivity.this, userProfile);
                doUpdateFullVersion();
                SweetAlertDialog d = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                d.setTitleText(getString(R.string.product_purchased_title));
                d.setContentText(getString(R.string.product_purchased_message));
                d.setConfirmText(getString(R.string.dialog_ok));
                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        if (chooseActivateType != null && chooseActivateType.isShowing())
                            chooseActivateType.dismiss();
                        if (dialogSubscription != null && dialogSubscription.isShowing())
                            dialogSubscription.dismiss();
                    }
                });
                d.show();
            }

            @Override
            public void onBillingError(int errorCode, Throwable error) {
                SimpleAppLog.error("could not complete purchase. Error code: " + errorCode,error);
                SweetAlertDialog d = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
                d.setTitleText(getString(R.string.could_not_purchase_title));
                d.setContentText(getString(R.string.could_not_purchase_message));
                d.setConfirmText(getString(R.string.dialog_ok));
                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                d.show();
            }

            @Override
            public void onBillingInitialized() {

            }

            @Override
            public void onPurchaseHistoryRestored() {

            }
        });

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        if (!Preferences.getCurrentProfile().isPro()) {
            accountManager.checkActivationDate(Preferences.getCurrentProfile(), new AccountManager.AuthListener() {
                @Override
                public void onError(String message, Throwable e) {
                    SimpleAppLog.error(message, e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            willCloseAfterSubscription = true;
                            showActiveFullVersionDialog(true);
                        }
                    });
                }

                @Override
                public void onSuccess() {
                }
            });
        }
    }

    private void showLogoutDialog() {
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
    }

    @OnItemClick(R.id.listMenu)
    public void clickListMenu(int position) {
        ListMenuAdapter.MenuItem menuItem = ((ListMenuAdapter)listMenu.getAdapter()).getMenuItems()[position];
        AnalyticHelper.sendEvent(AnalyticHelper.Category.DEFAULT, AnalyticHelper.Action.SELECT_MENU_ITEM, menuItem.toString());
        switch (menuItem) {
            case LOGOUT:
                showLogoutDialog();
                break;
            case ACTIVATE_SUBSCRIPTION:
                showActiveFullVersionDialog();
                break;
            case SUBSCRIPTION:
                Intent i = new Intent(this, SubscriptionActivity.class);
                startActivity(i);
                break;
            default:
                UserProfile profile = Preferences.getCurrentProfile();
                if (profile != null) {
                    profile.setLastSelectedMenuItem(menuItem.toString());
                    Preferences.updateProfile(MainApplication.getContext(), profile);
                }
                switchFragment(menuItem, null, null);
        }
    }

    private void initListMenu() {
        UserProfile userProfile = Preferences.getCurrentProfile();
        ListMenuAdapter.MenuItem[] menuItems = userProfile.isPro() ? ListMenuAdapter.FULL_MENU_ITEMS : ListMenuAdapter.LITE_MENU_ITEMS;
        ListMenuAdapter adapter = new ListMenuAdapter(this, menuItems);
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
        //super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        this.menu = menu;
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
                switchFragment(FragmentState.FEEDBACK, new SwitchFragmentParameter(false, true, true), null);
                break;
            case R.id.menu_ipa_information:
                switchFragment(FragmentState.IPA_INFORMATION, new SwitchFragmentParameter(false, true, true), null);
                break;
            case R.id.menu_share:
                showShareAction();
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
        if (bp != null) bp.release();
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

    public void popBackStackFragment(int count) {
        if (count <= 1) popBackStackFragment();
        for (int i = 0; i < count - 1; i++) {
            MainApplication.enablePopbackFragmentAnimation = false;
            popBackStackFragment();
            MainApplication.enablePopbackFragmentAnimation = true;
            lastPopbackPress = 0;
        }
        popBackStackFragment();
    }

    public void popBackStackFragment() {
        long now = System.currentTimeMillis();
        if (MainApplication.enablePopbackFragmentAnimation
                && lastPopbackPress != 0
                && now - lastPopbackPress < getResources().getInteger(android.R.integer.config_mediumAnimTime))
            return;
        if (fragmentStates.size() == 0) return;
        lastPopbackPress = now;
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
                if (menu != null) {
                    menu.setGroupVisible(R.id.group_freestyle, true);
                }
                break;
            default:
                if (searchView != null) {
                    if (!searchView.isIconified())
                        searchView.setIconified(true);
                }
                if (menu != null) {
                    menu.setGroupVisible(R.id.group_freestyle, false);
                }
                break;
        }
        AnalyticHelper.sendEvent(AnalyticHelper.Category.DEFAULT, AnalyticHelper.Action.SELECT_FRAGMENT, currentFragmentState.clazz.getName());
        MainApplication.getContext().setSkipHelpPopup(currentFragmentState == FragmentState.SETTINGS);
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
                    if (state == FragmentState.FEEDBACK) {
                        AndroidHelper.takeScreenShot(this);
                    }
                    AnalyticHelper.sendEvent(AnalyticHelper.Category.DEFAULT, AnalyticHelper.Action.SELECT_FRAGMENT, clazz.getName());
                    switch (state) {
                        case FREE_STYLE:
                            if (menu != null) {
                                menu.setGroupVisible(R.id.group_freestyle, true);
                            }
                            MainApplication.getContext().setSelectedWord(null);
                            break;
                        default:
                            if (searchView != null) {
                                if (!searchView.isIconified())
                                    searchView.setIconified(true);
                                if (menu != null) {
                                    menu.setGroupVisible(R.id.group_freestyle, false);
                                }
                            }
                            break;
                    }
                    MainApplication.getContext().setSkipHelpPopup(state == FragmentState.SETTINGS);
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
                        transaction.commitAllowingStateLoss();
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
                        transaction.commitAllowingStateLoss();
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
            UserProfile userProfile = Preferences.getCurrentProfile();
            if (userProfile.isPro()) {
                if (doubleBackToExitPressedOnce) {
                    this.finish();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please press BACK again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
                return;
            } else {
                willCloseAfterSubscription = true;
                showActiveFullVersionDialog();
            }
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
        if (menu != null)
            menu.setGroupVisible(R.id.group_freestyle, false);
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
            if (userProfile != null && userProfile.isPro()) {
                String lastMenuItem = userProfile.getLastSelectedMenuItem();
                ListMenuAdapter.MenuItem menuItem = null;
                if (lastMenuItem != null && lastMenuItem.length() > 0) {
                    menuItem = ListMenuAdapter.MenuItem.fromName(lastMenuItem);
                }
                if (menuItem != ListMenuAdapter.MenuItem.FREESTYLE && menuItem != ListMenuAdapter.MenuItem.LESSON) {
                    menuItem = ListMenuAdapter.MenuItem.FREESTYLE;
                }
                switchFragment(menuItem, null, null);
            } else {
                switchFragment(ListMenuAdapter.MenuItem.LESSON, null, null);
            }
        }
    }

    public BottomSheet.Builder getShareActions(String title, String text) {
        return getShareActions(title, text, null);
    }

    public BottomSheet.Builder getShareActions(final String title, final String text, final String url) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TITLE, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text + " " + url);
        BottomSheet.Builder builder = new BottomSheet.Builder(this).grid();
        PackageManager pm = this.getPackageManager();
        final List<ResolveInfo> list = pm.queryIntentActivities(shareIntent, 0);
        for (int i = 0; i < list.size(); i++) {
            builder.sheet(i, list.get(i).loadIcon(pm), list.get(i).loadLabel(pm));
        }
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(@NonNull DialogInterface dialog, int which) {
                ActivityInfo activityInfo = list.get(which).activityInfo;
                SimpleAppLog.debug("Choose share app: " + activityInfo.applicationInfo.packageName);
                switch (activityInfo.applicationInfo.packageName) {
                    case "com.google.android.apps.plus":
                        shareGooglePlus(title, text, url);
                        break;
                    case "com.facebook.katana":
                        shareFacebook(title, text, url);
                        break;
                    default:
                        ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName,
                                activityInfo.name);
                        Intent newIntent = (Intent) shareIntent.clone();
                        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        newIntent.setComponent(name);
                        startActivity(newIntent);
                        break;
                }
            }
        });
        builder.limit(com.cocosw.bottomsheet.R.integer.bs_initial_grid_row);
        return builder;
    }

    public void shareFacebook(String title, String description, String url) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(title)
                    .setContentDescription(
                            description)
                    .setContentUrl(Uri.parse(url))
                    .build();

            shareDialog.show(linkContent);
        }
    }

    public void shareGooglePlus(String title, String description, String url) {
        Intent shareIntent = new PlusShare.Builder(this)
                .setType("text/plain")
                .setText(description)
                .setContentUrl(Uri.parse(url))
                .getIntent();
        startActivityForResult(shareIntent, 0);
    }

    public void share(String title, String description, String url) {
        switch(Preferences.getCurrentProfile().getLoginType()) {
            case UserProfile.TYPE_EASYACCENT:
                getShareActions(title, description, url).show();
                break;
            case UserProfile.TYPE_FACEBOOK:
                shareFacebook(title, description, url);
                break;
            case UserProfile.TYPE_GOOGLE_PLUS:
                shareGooglePlus(title, description, url);
                break;
        }
    }

    private void showShareAction() {
        share("accenteasy - English pronunciation app", "I thought you might find this app useful to help you with English pronunciation", "https://play.google.com/store/apps/details?id=com.cmg.android.bbcaccent");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (bp != null && !bp.handleActivityResult(requestCode, resultCode, data))
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    public void executeAction(MainAction action) {
        action.execute(this);
    }
}
