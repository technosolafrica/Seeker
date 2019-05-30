package com.tal.hide;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface
 * to handle interaction events.
 * Use the {@link Loading#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Loading extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "message";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Knowingly Loading Fragment ";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnPairingListener mListener;

    public Loading() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Loading.
     */
    // TODO: Rename and change types and number of parameters
    public static Loading newInstance() {
        Loading fragment = new Loading();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View frame = inflater.inflate(R.layout.pair_progress, container, false);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            //get the info text field
            TextView info = frame.findViewById(R.id.txt_loading);
            //set new text :: to loading screeen
            info.setText(mParam1);
        }
        // Inflate the layout for this fragment
        return frame;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnPairingListener) {
            mListener = (OnPairingListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        this.setOnPairingListener(mListener);*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *     // TODO: Rename method, update argument and hook method into UI event
     *     public void onButtonPressed(Uri uri) {
     *         if (mListener != null) {
     *             mListener.onPairingInteraction(uri);
     *         }
     *     }
     *     void onPairingInteraction(Uri uri);
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public void setOnPairingListener(OnPairingListener callback){
        this.mListener = callback;
    }
    public interface OnPairingListener {
        // add pairing listener
        public void onPairingSuccess();
        public void onPairingFailure();
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG," on destroy view ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG," on destroy view ");
    }
}
