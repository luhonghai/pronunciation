package com.cmg.android.bbcaccent;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.cmg.android.bbcaccent.adapter.CustomAdapterTeacher;
import com.cmg.android.bbcaccent.auth.AccountManager;
import com.cmg.android.bbcaccent.data.dto.ListStudentMappingTeacher;
import com.cmg.android.bbcaccent.data.dto.StudentMappingTeacher;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.fragment.Preferences;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.cmg.android.bbcaccent.utils.UUIDGenerator;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class TeacherActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.btnSend)
    Button btnSend;

    @Bind(R.id.lvItem)
    ListView lvItem;

    @Bind(R.id.txtTeacher)
    EditText txtTeacher;


    private AccountManager accountManager;
    private List<StudentMappingTeacher> studentMappingTeachers=null;
    private StudentMappingTeacher smt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.teacher);
        ButterKnife.bind(this);
        Gson gson=new Gson();
        accountManager=new AccountManager(this);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null) {
            if(extras.containsKey("studentMappingTeachers")) {
                String listTeacher = intent.getStringExtra("studentMappingTeachers");
                StudentMappingTeacher[] MappingTeachers = gson.fromJson(listTeacher, StudentMappingTeacher[].class);
                List<StudentMappingTeacher> list=Arrays.asList(MappingTeachers);
                studentMappingTeachers=list;
                if (studentMappingTeachers != null && studentMappingTeachers.size()>0) {
                    listTeacher(studentMappingTeachers);
                }
            }
        }else {
            sendMessageFromTeacher();
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mailTeacher=txtTeacher.getText().toString();
                if(mailTeacher!=null){
                    searchTeacher(mailTeacher);
                }else {
                    SweetAlertDialog d = new SweetAlertDialog(TeacherActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    d.setTitleText("Email can not null.");
                    d.setConfirmText(getString(R.string.dialog_ok));
                    d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
                    d.show();
                }
            }
        });
        if (studentMappingTeachers != null && studentMappingTeachers.size()>0) {
            listTeacher(studentMappingTeachers);
        }

    }
    private void listTeacher(List<StudentMappingTeacher> studentMappingTeachers){
        CustomAdapterTeacher customAdapterTeacher=null;
        customAdapterTeacher=new CustomAdapterTeacher(this,R.layout.lv_item,studentMappingTeachers);
        lvItem.setAdapter(customAdapterTeacher);
        customAdapterTeacher.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        StudentMappingTeacher studentMappingTeacher = (StudentMappingTeacher) v.getTag();
        String mailTeacher=studentMappingTeacher.getTeacherName();
        String username=studentMappingTeacher.getStudentName();
        String ids=studentMappingTeacher.getId();
        smt=studentMappingTeachers.get(studentMappingTeachers.indexOf(studentMappingTeacher));
        switch (id){
            case R.id.accept:
                sendStatusToTeacher("accept", mailTeacher);
                smt.setStatus("accept");
                smt.setIsDeleted(false);
                smt.setTeacherName(mailTeacher);
                smt.setStudentName(username);
                smt.setId(ids);
                listTeacher(studentMappingTeachers);
                break;
            case R.id.reject:
                sendStatusToTeacher("reject", mailTeacher);
                smt.setStatus("reject");
                smt.setIsDeleted(false);
                smt.setTeacherName(mailTeacher);
                smt.setStudentName(username);
                smt.setId(ids);
                listTeacher(studentMappingTeachers);
        }
    }

    private void sendStatusToTeacher(String status,String mailTeacher) {

        final UserProfile profile = Preferences.getCurrentProfile();
        if (profile == null || !profile.isLogin()) {
            SimpleAppLog.logJson("profile null.");
        } else {
            SimpleAppLog.logJson("Detect old profile: ", profile);
            accountManager.sendStatusToTeacher(profile, new AccountManager.AuthListener() {
                @Override
                public void onError(final String message, Throwable e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SweetAlertDialog d = new SweetAlertDialog(TeacherActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                            d.setTitleText("error");
                            d.setConfirmText(getString(R.string.dialog_ok));
                            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                            d.show();
                        }
                    });
                }

                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            SweetAlertDialog d = new SweetAlertDialog(TeacherActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                            d.setTitleText("successful");
                            d.setConfirmText(getString(R.string.dialog_ok));
                            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                            d.show();
                        }
                    });
                }
            }, status, mailTeacher);
        }

    }
    private void searchTeacher(final String mailTeacher) {

        final UserProfile profile = Preferences.getCurrentProfile();
        if (profile == null || !profile.isLogin()) {
            SimpleAppLog.logJson("profile null.");
        } else {
            SimpleAppLog.logJson("Detect old profile: ", profile);
            accountManager.searchTeacher(profile, new AccountManager.AuthListener() {
                @Override
                public void onError(final String message, Throwable e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            SweetAlertDialog d = new SweetAlertDialog(TeacherActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                            d.setTitleText("Email not exist.");
                            d.setConfirmText(getString(R.string.dialog_ok));
                            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }

                            });
                            d.show();
                        }
                    });
                }

                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SweetAlertDialog d = new SweetAlertDialog(TeacherActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                            d.setTitleText("successful");
                            d.setConfirmText(getString(R.string.dialog_ok));
                            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                            d.show();
                            String id = UUIDGenerator.generateUUID();
                            studentMappingTeachers.add(new StudentMappingTeacher(id, profile.getUsername(), mailTeacher, false, "accept"));
                            if (studentMappingTeachers != null && studentMappingTeachers.size()>0) {
                                listTeacher(studentMappingTeachers);
                            }
                        }
                    });
                }
            }, mailTeacher);
        }

    }

    public void sendMessageFromTeacher() {
        final UserProfile profile = Preferences.getCurrentProfile();
        if (profile == null || !profile.isLogin()) {
            SimpleAppLog.logJson("profile null.");
        } else {
            SimpleAppLog.logJson("Detect old profile: ", profile);
            accountManager.messageTeachers(profile, new AccountManager.AuthListeners() {
                @Override
                public void onError(final String message, Throwable e) {
                }

                @Override
                public void onSuccess(final List<StudentMappingTeacher> lists, final int number, Throwable e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            studentMappingTeachers = lists;
                            if (studentMappingTeachers != null && studentMappingTeachers.size()>0) {
                                listTeacher(studentMappingTeachers);
                            }
                        }
                    });
                }
            });
        }

    }
}
