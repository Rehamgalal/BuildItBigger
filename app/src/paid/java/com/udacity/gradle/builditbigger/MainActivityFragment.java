package com.udacity.gradle.builditbigger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    @Nullable
    private SimpleIdlingResource mIdlingResource;
    private static MyApi myApiService = null;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        // Create an ad request. Check logcat output for the hashed device ID to "d9dbe89ba49f02d4"
        Button tellJoke= (Button)root.findViewById(R.id.btntelljok);
        tellJoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> telJoke=new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        if(myApiService == null) {  // Only do this once
                            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                                    new AndroidJsonFactory(), null)
                                    // options for running against local devappserver
                                    // - 10.0.2.2 is localhost's IP address in Android emulator
                                    // - turn off compression when running against local devappserver
                                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                                    // .setApplicationName(fragment.getContext().getPackageName())
                                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                                        @Override
                                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                            abstractGoogleClientRequest.setDisableGZipContent(true);
                                        }
                                    });
                            // end options for devappserver

                            myApiService = builder.build();

                        }
                        try {
                            if (mIdlingResource != null) {
                                mIdlingResource.setIdleState(false);
                            }
                            return  myApiService.getJoke().execute().getData();
                        } catch (IOException e) {
                            return e.getMessage();
                        }

                    }

                    @Override
                    protected void onPostExecute(String joke) {
                        super.onPostExecute(joke);
                        Intent intent=new Intent(getActivity(), LibraryActivity.class);
                        intent.putExtra("joke",joke);
                        startActivity(intent);
                    }
                };
                telJoke.execute();

            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
    }
    @VisibleForTesting
    @Nullable
    public IdlingResource getIdlingResource(){
        if(mIdlingResource == null){
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
