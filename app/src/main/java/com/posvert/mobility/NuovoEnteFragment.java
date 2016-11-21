package com.posvert.mobility;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.posvert.mobility.common.Heap;
import com.posvert.mobility.common.ResponseHandler;
import com.posvert.mobility.common.ResponseHandlerPOST;
import com.posvert.mobility.common.URLBuilder;
import com.posvert.mobility.common.URLHelper;

import java.io.StringWriter;

import beans.Commento;
import beans.Ente;


/**
 * A simple
 * Activities that contain this fragment must implement the
 * {@link NuovoEnteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NuovoEnteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NuovoEnteFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText ente;
    private OnFragmentInteractionListener mListener;

    public NuovoEnteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NuovoEnteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NuovoEnteFragment newInstance(String param1, String param2) {
        NuovoEnteFragment fragment = new NuovoEnteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setStyle(STYLE_NORMAL, 0);
    }
    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vv = inflater.inflate(R.layout.fragment_nuovo_ente, container, false);

        ente = (EditText) vv.findViewById(R.id.ente);
        ente.requestFocus();

        Button ok = (Button) vv.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(NuovoEnteFragment.this);

                fragmentTransaction.commit();

                URLHelper.invokeURLPOST(getActivity(), buildUrl(), new ResponseHandlerPOST() {
                    @Override
                    public void parseResponse(String response) {

                    }

                    @Override
                    public String getJSONMessage() {
                        StringWriter sw = new StringWriter();
                        Ente u = getValori();
                        Gson gson = new Gson();
                        gson.toJson(u, sw);
                        String val = sw.toString();
                        return val;
                    }
                });


            }
        });
        return vv;
    }

    private Ente getValori() {
        Ente u = new Ente();

//        u.setUsername(Heap.getUserCorrente().getUsername());

        u.setNome(ente.getText().toString());

        return u;
    }

    private String buildUrl() {
        String url = URLHelper.buildWithPref(getActivity(), "enti", "creaEnte", true);
        return url;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
