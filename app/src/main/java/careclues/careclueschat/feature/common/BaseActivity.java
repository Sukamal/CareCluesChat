package careclues.careclueschat.feature.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import careclues.careclueschat.R;

public abstract class BaseActivity extends AppCompatActivity {

    private View view;

    public abstract int getContentLayout();

    public abstract void initComponents();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());
        ButterKnife.bind(this);
        initComponents();
    }


//    public void addFragment(Fragment fragment, Bundle bundle, boolean addBackStack) {
//        if (bundle != null) {
//            fragment.setArguments(bundle);
//        }
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.fl_MainContainer, fragment, fragment.getClass().getName());
//        if(addBackStack)
//            fragmentTransaction.addToBackStack(fragment.getClass().getName());
//        fragmentTransaction.commit();
//    }

    public void popFragmentBackstack(String fragmentName, boolean isInclusive) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (isInclusive) {
            fragmentManager.popBackStackImmediate(fragmentName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            fragmentManager.popBackStackImmediate(fragmentName, 0);
        }

    }

    public void clearAllFragment() {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            int fragmentStackEntryCount = fragmentManager.getBackStackEntryCount();

            for (int i = 0; i < fragmentStackEntryCount - 1; i++) {
                fragmentManager.popBackStack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
