package careclues.careclueschat.feature.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import careclues.careclueschat.R;

public class BaseFragment extends Fragment {

    public void addFragment(Fragment fragment, Bundle bundle){
        if(bundle != null){
            fragment.setArguments(bundle);
        }
        if (getActivity() != null){
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_MainContainer,fragment,fragment.getClass().getName());
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
            fragmentTransaction.commit();
        }
    }

    public void popFragmentBackstack(String fragmentName, boolean isInclusive){
        if(getActivity() != null){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            if(isInclusive){
                fragmentManager.popBackStackImmediate(fragmentName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }else{
                fragmentManager.popBackStackImmediate(fragmentName, 0);
            }
        }

    }

    public void clearAllFragment() {
        try {
            if (getActivity() != null) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                int fragmentStackEntryCount = fragmentManager.getBackStackEntryCount();

                for (int i = 0; i < fragmentStackEntryCount - 1; i++) {
                    fragmentManager.popBackStack();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
