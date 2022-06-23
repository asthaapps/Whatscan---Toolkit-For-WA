package com.whatscan.toolkit.forwa.Prank.FakeChat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.Adapter.FackCallAdapter;
import com.whatscan.toolkit.forwa.GetSet.StatusData;
import com.whatscan.toolkit.forwa.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentCalls extends Fragment {
    public static Uri selectedImageUri, selectedImageUrinew;
    public byte[] bmyimage;
    public Button callNow;
    public TextInputEditText txtName;
    public String nameProfile, nameProfilenew;
    public CircleImageView user_profilepic, user_profilepicnew;
    public FloatingActionButton addCall;
    public LinearLayout llMain, ll_audio_call, ll_video_call, ll_audio_main, ll_video_main;
    public RadioButton audioCall, videoCall;
    public RecyclerView recyCall;
    public int callType = 0;
    public FackCallAdapter fackCallAdapter;
    public List<StatusData> totalotherlist = new ArrayList<>();

    public FragmentCalls() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calls, container, false);
        txtName = view.findViewById(R.id.user_name);
        user_profilepic = view.findViewById(R.id.user_profilepic);
        user_profilepicnew = view.findViewById(R.id.user_profilepicnew);
        callNow = view.findViewById(R.id.callnow);
        addCall = view.findViewById(R.id.addCall);
        llMain = view.findViewById(R.id.llMain);
        audioCall = view.findViewById(R.id.audioCall);
        videoCall = view.findViewById(R.id.videoCall);
        ll_audio_call = view.findViewById(R.id.ll_audio_call);
        ll_video_call = view.findViewById(R.id.ll_video_call);
        ll_audio_main = view.findViewById(R.id.ll_audio_main);
        ll_video_main = view.findViewById(R.id.ll_video_main);
        recyCall = view.findViewById(R.id.recyCall);

        audioCall.setChecked(true);
        ll_audio_main.setVisibility(View.VISIBLE);

        ShowCallList();

        user_profilepic.setOnClickListener(new btnUserProfilePicListner());
        user_profilepicnew.setOnClickListener(new btnUserProfilePicListnernew());
        callNow.setOnClickListener(new btnCallNowListner());

        addCall.setOnClickListener(v -> startActivity(new Intent(getActivity(), ActivityCallDetails.class)));

        ll_audio_call.setOnClickListener(v -> {
            callType = 1;
            audioCall.setChecked(true);
            videoCall.setChecked(false);
            ll_audio_main.setVisibility(View.VISIBLE);
            ll_video_main.setVisibility(View.GONE);
        });

        ll_video_call.setOnClickListener(v -> {
            callType = 2;
            audioCall.setChecked(false);
            videoCall.setChecked(true);
            ll_video_main.setVisibility(View.VISIBLE);
            ll_audio_main.setVisibility(View.VISIBLE);
        });
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void ShowCallList() {
        if (getOtherData().size() > 0) {
            recyCall.setVisibility(View.VISIBLE);
            llMain.setVisibility(View.GONE);
            fackCallAdapter = new FackCallAdapter(getContext(), getActivity(), totalotherlist);
            recyCall.setLayoutManager(new LinearLayoutManager(getContext()));
            recyCall.setAdapter(fackCallAdapter);
            fackCallAdapter.notifyDataSetChanged();
            return;
        }

        recyCall.setVisibility(View.GONE);
        llMain.setVisibility(View.VISIBLE);
    }

    public List<StatusData> getOtherData() {
        totalotherlist = new Gson().fromJson(requireContext().getSharedPreferences("data", 0).getString("mycall", ""), new TypeToken<List<StatusData>>() {
        }.getType());
        if (totalotherlist == null || totalotherlist.size() <= 0) {
            totalotherlist = new ArrayList<>();
            return new ArrayList<>();
        }
        return totalotherlist;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == 101) {
            onSelectFromGalleryResult(data);
        } else if (resultCode == -1 && requestCode == 201) {
            onSelectFromGalleryResultnew(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        try {
            selectedImageUri = data.getData();
            user_profilepic.setImageURI(selectedImageUri);
            bmyimage = saveImageInDB(selectedImageUri);
            getPath(selectedImageUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSelectFromGalleryResultnew(Intent data) {
        try {
            selectedImageUrinew = data.getData();
            user_profilepicnew.setImageURI(selectedImageUrinew);
            bmyimage = saveImageInDB(selectedImageUrinew);
            getPath(selectedImageUrinew);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] saveImageInDB(Uri selectedImageUri) {
        try {
            return getBytes(requireContext().openFileInput(String.valueOf(selectedImageUri)));
        } catch (IOException ioe) {
            Log.e("Hello1", "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
            return null;
        }
    }

    private byte[] getBytes(InputStream iStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int len = iStream.read(buffer);
            if (len == -1) {
                return byteBuffer.toByteArray();
            }
            byteBuffer.write(buffer, 0, len);
        }
    }

    private void getPath(Uri selectedImageUri) {
        Cursor cursor = requireActivity().managedQuery(selectedImageUri, new String[]{"_data"}, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        cursor.getString(column_index);
    }

    @Override
    public void onResume() {
        super.onResume();
        ShowCallList();
    }

    private class btnUserProfilePicListner implements View.OnClickListener {
        public void onClick(View view) {
            startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 101);
        }
    }

    private class btnUserProfilePicListnernew implements View.OnClickListener {
        public void onClick(View view) {
            startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 201);
        }
    }

    private class btnCallNowListner implements View.OnClickListener {
        public void onClick(View view) {
            if (callType == 1) {
                nameProfile = Objects.requireNonNull(txtName.getText()).toString();
                if (nameProfile.isEmpty()) {
                    txtName.setError("Enter Caller Name");
                    return;
                }
                Intent intent = new Intent(getContext(), ActivityFackCalls.class);
                intent.putExtra("NAME", nameProfile);
                intent.putExtra("TYPEONE", true);
                intent.putExtra("PROFILEPIC", selectedImageUri);
                startActivity(intent);
            } else if (callType == 2) {
                nameProfilenew = Objects.requireNonNull(txtName.getText()).toString();
                if (nameProfilenew.isEmpty()) {
                    txtName.setError("Enter Caller Name");
                    return;
                }
                Intent intent = new Intent(getContext(), ActivityFackCalls.class);
                intent.putExtra("NAMENEW", nameProfilenew);
                intent.putExtra("TYPETWO", true);
                intent.putExtra("PROFILEPIC", selectedImageUri);
                intent.putExtra("PROFILEPICNEW", selectedImageUrinew);
                startActivity(intent);
            } else {
                nameProfile = Objects.requireNonNull(txtName.getText()).toString();
                if (nameProfile.isEmpty()) {
                    txtName.setError("Enter Caller Name");
                    return;
                }
                Intent intent = new Intent(getContext(), ActivityFackCalls.class);
                intent.putExtra("NAME", nameProfile);
                intent.putExtra("TYPEONE", true);
                intent.putExtra("PROFILEPIC", selectedImageUri);
                startActivity(intent);
            }
        }
    }
}