package com.tal.hide.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.tal.hide.R;

public class PairingProcess extends AsyncTask<Boolean, Integer, Void> {
    //progress bar for awaiting pairing
    ProgressBar progressBar;
    Context context;
    AlertDialog.Builder alertBuilder;
    AlertDialog alert;
    //construct with context
    public PairingProcess(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //create new progress bar
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleInverse);
        progressBar.setIndeterminate(false);
        progressBar.setMax(100);
        //init alert builder
        alertBuilder = new AlertDialog.Builder(context);
        //inflate
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialog = inflater.inflate(R.layout.pair_progress, null);
        alertBuilder.setView(dialog);
        alert = alertBuilder.create();
        alert.show();
    }

    @Override
    protected Void doInBackground(Boolean... booleans) {
        try {
            //check if server has replied with
            for (int i = 1; i <= 20; i++) {
                //publish every 5% percent
                publishProgress(i * 5);
                //loop every half a second
                Thread.sleep(500);
                //check if thread cancelled
                if(isCancelled()) break;
            }
        }catch (InterruptedException ie){ ie.printStackTrace(); }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
        //result successful so progress cancelled
        alert.dismiss();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //on complete
        alert.dismiss();
    }
}
