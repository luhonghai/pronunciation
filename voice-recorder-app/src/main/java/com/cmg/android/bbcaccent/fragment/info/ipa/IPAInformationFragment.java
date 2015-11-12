package com.cmg.android.bbcaccent.fragment.info.ipa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.dto.lesson.word.IPAMapArpabet;
import com.cmg.android.bbcaccent.extra.SwitchFragmentParameter;
import com.cmg.android.bbcaccent.fragment.BaseFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by luhonghai on 12/11/2015.
 */
public class IPAInformationFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ipa_information_fragment, null);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btnRP)
    public void onClickRpButton() {
        MainBroadcaster.getInstance().getSender().sendSwitchFragment(IPARPFragment.class, new SwitchFragmentParameter(true, true, true), null);
    }

    @OnClick(R.id.btnVowels)
    public void onClickVowelsButton() {
        Bundle bundle = new Bundle();
        bundle.putString(IPAMapArpabet.IPAType.class.getName(), IPAMapArpabet.IPAType.VOWEL.toString());
        MainBroadcaster.getInstance().getSender().sendSwitchFragment(IPAPhoneticFragment.class, new SwitchFragmentParameter(true, true, true), bundle);
    }

    @OnClick(R.id.btnConsonant)
    public void onClickConsonantButton() {
        Bundle bundle = new Bundle();
        bundle.putString(IPAMapArpabet.IPAType.class.getName(), IPAMapArpabet.IPAType.CONSONANT.toString());
        MainBroadcaster.getInstance().getSender().sendSwitchFragment(IPAPhoneticFragment.class, new SwitchFragmentParameter(true, true, true), bundle);

    }
}
