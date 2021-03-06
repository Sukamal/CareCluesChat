package careclues.careclueschat.feature.common;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import careclues.careclueschat.R;

public class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    public void addFragment(final Fragment fragment, final boolean addtoBac, final Bundle bundle){


        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(bundle != null){
                    fragment.setArguments(bundle);
                }

                if (!getActivity().isFinishing() /*&& !isDestroyed()*/) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                fragmentManager.popBackStackImmediate(fragment.getClass().getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fl_MainContainer,fragment,fragment.getClass().getName());
                    if(addtoBac){
                        fragmentTransaction.addToBackStack(fragment.getClass().getName());
                    }
//                fragmentTransaction.commitAllowingStateLoss();
                    fragmentTransaction.commit();
                }



            }
        });
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
