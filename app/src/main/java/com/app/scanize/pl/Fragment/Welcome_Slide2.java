package com.app.scanize.pl.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.scanize.pl.R;

public class Welcome_Slide2 extends Fragment {

    public static Welcome_Slide2 newInstance(String text) {

        Welcome_Slide2 f = new Welcome_Slide2();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome_slide2, container, false);

        VideoView videoView = v.findViewById(R.id.videoView2);
        String videoPath = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.intro_video;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.start();
        return v;
    }
}
