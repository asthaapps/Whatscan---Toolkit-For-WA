package com.whatscan.toolkit.forwa.Cleaner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Adapter.InnerDetailsAdapter;
import com.whatscan.toolkit.forwa.BuildConfig;
import com.whatscan.toolkit.forwa.GetSet.FileDetails;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class FilesFragment extends Fragment implements InnerDetailsAdapter.OnCheckboxListener {
    public Button button, date, name, size;
    public LottieAnimationView no_files;
    public InnerDetailsAdapter innerDetailsAdapter;
    public ArrayList<FileDetails> innerDataList = new ArrayList<>();
    public ArrayList<FileDetails> filesToDelete = new ArrayList<>();
    public ProgressDialog progressDialog;
    public CheckBox selectall;
    public boolean flag_d = true, flag_n = true, flag_s = true;
    public FirebaseAnalytics mFirebaseAnalytics;

    public static FilesFragment newInstance(String category, String path) {
        FilesFragment filesFragment = new FilesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putString("category", category);
        filesFragment.setArguments(bundle);
        return filesFragment;
    }

    private static long getFileSize(File file) {
        if (file != null && file.isFile()) {
            return file.length();
        }
        return 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;
        String category = null;
        String path = null;

        if (getArguments() != null) {
            path = getArguments().getString("path");
            category = getArguments().getString("category");
        } else {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            if (getActivity() != null)
                getActivity().finish();
        }

        if (category == null) {
            if (getActivity() != null)
                getActivity().finish();
            return null;
        }

        TextView select;
        RecyclerView recyclerView;
        RelativeLayout relativeLayout2, rlHeader;
        int IMAGES = 1;
        int VIDEOS = 2;
        int AUDIOS = 3;
        int FILE = 4;
        int VOICE = 6;
        switch (category) {
            default:
            case Constant.WALLPAPER:
            case Constant.IMAGE:
                rootView = inflater.inflate(R.layout.activity_image_clean, container, false);
                recyclerView = rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                innerDetailsAdapter = new InnerDetailsAdapter(IMAGES, getActivity(), innerDataList, this);
                break;
            case Constant.GIF:
            case Constant.VIDEO:
                rootView = inflater.inflate(R.layout.activity_image_clean, container, false);
                recyclerView = rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                innerDetailsAdapter = new InnerDetailsAdapter(VIDEOS, getActivity(), innerDataList, this);
                break;
            case Constant.VOICE:
                rootView = inflater.inflate(R.layout.activity_document_clean, container, false);
                recyclerView = rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                innerDetailsAdapter = new InnerDetailsAdapter(VOICE, getActivity(), innerDataList, this);
                break;
            case Constant.AUDIO:
                rootView = inflater.inflate(R.layout.activity_document_clean, container, false);
                recyclerView = rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                innerDetailsAdapter = new InnerDetailsAdapter(AUDIOS, getActivity(), innerDataList, this);
                break;
            case Constant.NONDEFAULT:
            case Constant.DOCUMENT:
                rootView = inflater.inflate(R.layout.activity_document_clean, container, false);
                recyclerView = rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                innerDetailsAdapter = new InnerDetailsAdapter(FILE, getActivity(), innerDataList, this);
                break;
        }

        button = rootView.findViewById(R.id.delete);
        date = rootView.findViewById(R.id.date);
        name = rootView.findViewById(R.id.name);
        size = rootView.findViewById(R.id.size);
        no_files = rootView.findViewById(R.id.nofiles);
        selectall = rootView.findViewById(R.id.selectall);
        select = rootView.findViewById(R.id.select);
        relativeLayout2 = rootView.findViewById(R.id.relativeLayout2);
        rlHeader = rootView.findViewById(R.id.rlHeader);

        if (Preference.getBooleanTheme(false)) {
            relativeLayout2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darkBlack));
            rlHeader.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darkBlack));
            select.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
        } else {
            relativeLayout2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
            rlHeader.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
            select.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack));
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(innerDetailsAdapter);


        new FetchFiles(this, category, fileDetails -> {
            if (fileDetails != null && !fileDetails.isEmpty()) {
                innerDataList.addAll(fileDetails);
                innerDetailsAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
                no_files.setVisibility(View.INVISIBLE);
            } else {
                progressDialog.dismiss();
                no_files.setVisibility(View.VISIBLE);
            }
        }).execute(path);

        date.setOnClickListener(v -> {
            if (!flag_n || !flag_s) {
                flag_n = true;
                flag_s = true;
            }
            if (flag_d) {
                flag_d = false;
                Collections.sort(innerDataList, (o1, o2) -> -o1.getMod().compareTo(o2.getMod()));
                innerDetailsAdapter.notifyDataSetChanged();
            } else {
                flag_d = true;
                Collections.sort(innerDataList, (o1, o2) -> o1.getMod().compareTo(o2.getMod()));
                Log.e("State", "Disabled");
                innerDetailsAdapter.notifyDataSetChanged();
            }
        });

        name.setOnClickListener(v -> {
            if (!flag_d || !flag_s) {
                flag_d = true;
                flag_s = true;
            }
            if (flag_n) {
                flag_n = false;
                Collections.sort(innerDataList, (o1, o2) -> o1.getName().compareTo(o2.getName()));
                Log.e("State", "Toggled");
                innerDetailsAdapter.notifyDataSetChanged();
            } else {
                flag_n = true;
                Collections.sort(innerDataList, (o1, o2) -> -o1.getName().compareTo(o2.getName()));
                Log.e("State", "Disabled");
                innerDetailsAdapter.notifyDataSetChanged();
            }
        });

        size.setOnClickListener(v -> {
            if (!flag_d || !flag_n) {
                flag_d = true;
                flag_n = true;
            }
            if (flag_s) {
                flag_s = false;
                Collections.sort(innerDataList, (o1, o2) -> -o1.getS().compareTo(o2.getS()));
                innerDetailsAdapter.notifyDataSetChanged();
            } else {
                flag_s = true;
                Collections.sort(innerDataList, (o1, o2) -> o1.getS().compareTo(o2.getS()));
                innerDetailsAdapter.notifyDataSetChanged();
            }
        });

        selectall.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                for (int i = 0; i < innerDataList.size(); i++) {
                    innerDataList.get(i).setSelected(true);
                }
                innerDetailsAdapter.notifyDataSetChanged();
            } else {
                for (int i = 0; i < innerDataList.size(); i++) {
                    innerDataList.get(i).setSelected(false);
                }
                innerDetailsAdapter.notifyDataSetChanged();
            }
        });

        button.setOnClickListener(v -> {
            if (!filesToDelete.isEmpty()) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Are you sure you want to delete selected files?")
                        .setCancelable(true)
                        .setPositiveButton("YES", (dialog, which) -> {
                            int success = -1;
                            ArrayList<FileDetails> deletedFiles = new ArrayList<>();
                            for (FileDetails details : filesToDelete) {
                                File file = new File(details.getPath());
                                if (file.exists()) {
                                    if (file.delete()) {
                                        deletedFiles.add(details);
                                        if (success == 0) {
                                            return;
                                        }
                                        success = 1;
                                    } else {
                                        Log.e("TEST", "" + file.getName() + " delete failed");
                                        success = 0;
                                    }
                                } else {
                                    Log.e("TEST", "" + file.getName() + " doesn't exists");
                                    success = 0;
                                }
                            }

                            filesToDelete.clear();

                            for (FileDetails deletedFile : deletedFiles) {
                                innerDataList.remove(deletedFile);
                            }
                            innerDetailsAdapter.notifyDataSetChanged();
                            if (selectall.isChecked()) {
                                Intent intent = new Intent(getContext(), ActivityCleaner.class);
                                startActivity(intent);
                            }
                            if (success == 0) {
                                Toast.makeText(getContext(), "Couldn't delete some files", Toast.LENGTH_SHORT).show();
                            } else if (success == 1) {
                                Toast.makeText(getContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                            button.setText(R.string.delete_items_blank);
                        }).setNegativeButton("NO", (dialog, which) -> dialog.dismiss()).create().show();
            }
        });

        return rootView;
    }

    @Override
    public void oncheckboxlistener(View view, List<FileDetails> updatedFiles) {
        filesToDelete.clear();

        for (FileDetails details : updatedFiles) {
            if (details.isSelected()) {
                filesToDelete.add(details);
            }
        }

        if (filesToDelete.size() > 0) {
            long totalFileSize = 0;

            for (FileDetails details : filesToDelete) {
                File file = new File(details.getPath());
                totalFileSize += file.length();
            }

            String size = Formatter.formatShortFileSize(getActivity(), totalFileSize);
            button.setText("Delete Selected Items (" + size + ")");
        } else {
            button.setText(R.string.delete_items_blank);
        }
    }

    private static class FetchFiles extends AsyncTask<String, Void, Object> {
        public String category;
        public OnFilesFetched onFilesFetched;
        public WeakReference<FilesFragment> filesFragmentWeakReference;

        FetchFiles(FilesFragment filesFragment, String category, OnFilesFetched onFilesFetched) {
            filesFragmentWeakReference = new WeakReference<>(filesFragment);
            this.category = category;
            this.onFilesFetched = onFilesFetched;
        }

        @Override
        protected void onPreExecute() {
            filesFragmentWeakReference.get().progressDialog = new ProgressDialog(filesFragmentWeakReference.get().getContext());
            filesFragmentWeakReference.get().progressDialog.setMessage("Please Wait");
            filesFragmentWeakReference.get().progressDialog.setCancelable(false);
            if (!filesFragmentWeakReference.get().progressDialog.isShowing()) {
                filesFragmentWeakReference.get().progressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(String... strings) {
            String path = strings[0];
            String extension;
            ArrayList<FileDetails> files = new ArrayList<>();

            if (path != null) {
                File directory = new File(path);
                File[] results = directory.listFiles();

                if (results != null) {
                    for (final File file : results) {
                        switch (category) {
                            case Constant.IMAGE:
                            case Constant.WALLPAPER:
                                if (file.isFile()) {
                                    if (!file.getName().endsWith(".nomedia")) {
                                        FileDetails fileDetails = new FileDetails();
                                        fileDetails.setName(file.getName());
                                        fileDetails.setPath(file.getPath());
                                        fileDetails.setImage(R.drawable.ic_w_wallpater);
                                        fileDetails.setMod(file.lastModified());
                                        fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().getContext(), getFileSize(file)));
                                        fileDetails.setS(getFileSize(file));
                                        files.add(fileDetails);
                                    }
                                } else if (file.isDirectory()) {
                                    if (!file.getName().equals("Sent")) {
                                        File[] res = file.listFiles();
                                        if (res != null) {
                                            for (File re : res) {
                                                if (!re.getName().endsWith(".nomedia")) {
                                                    FileDetails fileDetails = new FileDetails();
                                                    fileDetails.setName(re.getName());
                                                    fileDetails.setPath(re.getPath());
                                                    fileDetails.setImage(R.drawable.ic_w_wallpater);
                                                    fileDetails.setMod(re.lastModified());
                                                    fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().getContext(), getFileSize(re)));
                                                    fileDetails.setS(getFileSize(re));
                                                    files.add(fileDetails);
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case Constant.NONDEFAULT:
                            case Constant.DOCUMENT:
                                if (file.isFile()) {
                                    if (!file.getName().endsWith(".nomedia")) {
                                        FileDetails fileDetails = new FileDetails();
                                        fileDetails.setName(file.getName());
                                        fileDetails.setPath(file.getPath());
                                        fileDetails.setImage(R.drawable.ic_document);
                                        extension = FilenameUtils.getExtension((file.getPath()));
                                        fileDetails.setMod(file.lastModified());
                                        String mime = "*/*";
                                        File a = new File(file.getPath());
                                        Uri uri = FileProvider.getUriForFile(filesFragmentWeakReference.get().requireContext(), BuildConfig.APPLICATION_ID + ".provider", a);
                                        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                                        if (mimeTypeMap.hasExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {
                                            mime = Objects.requireNonNull(mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()))).split("/")[0];
                                        }
                                        switch (mime) {
                                            case "image":
                                                fileDetails.setExt(extension);
                                                break;
                                            case "video":
                                                fileDetails.setExt(extension);
                                                break;
                                            case "text":
                                                fileDetails.setExt(extension);
                                                break;
                                            case "application":
                                                fileDetails.setExt(extension);
                                                break;
                                            case "audio":
                                                fileDetails.setExt(extension);
                                                break;
                                            default:
                                                fileDetails.setImage(R.drawable.logo);
                                                break;
                                        }
                                        fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().getContext(), getFileSize(file)));
                                        fileDetails.setS(getFileSize(file));
                                        files.add(fileDetails);
                                    }
                                } else if (file.isDirectory()) {
                                    if (!file.getName().equals("Sent")) {
                                        File[] res = file.listFiles();
                                        if (res != null) {
                                            for (File re : res) {
                                                if (!re.getName().endsWith(".nomedia")) {
                                                    FileDetails fileDetails = new FileDetails();
                                                    fileDetails.setName(re.getName());
                                                    fileDetails.setPath(re.getPath());
                                                    fileDetails.setMod(re.lastModified());
                                                    fileDetails.setImage(R.drawable.ic_document);
                                                    extension = FilenameUtils.getExtension((re.getPath()));
                                                    String mime = "*/*";
                                                    File a = new File(re.getPath());
                                                    Uri uri = FileProvider.getUriForFile(filesFragmentWeakReference.get().requireContext(), BuildConfig.APPLICATION_ID + ".provider", a);
                                                    MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                                                    if (mimeTypeMap.hasExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {
                                                        mime = Objects.requireNonNull(mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()))).split("/")[0];
                                                    }
                                                    switch (mime) {
                                                        case "image":
                                                            fileDetails.setExt(extension);
                                                            break;
                                                        case "video":
                                                            fileDetails.setExt(extension);
                                                            break;
                                                        case "text":
                                                            fileDetails.setExt(extension);
                                                            break;
                                                        case "application":
                                                            fileDetails.setExt(extension);
                                                            break;
                                                        case "audio":
                                                            fileDetails.setExt(extension);
                                                            break;
                                                        default:
                                                            fileDetails.setImage(R.drawable.logo);
                                                            break;
                                                    }
                                                    fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().getContext(), re.isDirectory() ? FileUtils.sizeOfDirectory(re) : getFileSize(re)));
                                                    fileDetails.setS(re.isDirectory() ? FileUtils.sizeOfDirectory(re) : getFileSize(re));
                                                    files.add(fileDetails);
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case Constant.VIDEO:
                                if (file.isFile()) {
                                    if (!file.getName().endsWith(".nomedia")) {
                                        FileDetails fileDetails = new FileDetails();
                                        fileDetails.setName(file.getName());
                                        fileDetails.setPath(file.getPath());
                                        fileDetails.setImage(R.drawable.ic_videocam);
                                        fileDetails.setMod(file.lastModified());
                                        fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().getContext(), getFileSize(file)));
                                        fileDetails.setS(getFileSize(file));
                                        files.add(fileDetails);
                                    }
                                } else if (file.isDirectory()) {
                                    if (!file.getName().equals("Sent")) {
                                        File[] res = file.listFiles();
                                        if (res != null) {
                                            for (File re : res) {
                                                if (!re.getName().endsWith(".nomedia")) {
                                                    FileDetails fileDetails = new FileDetails();
                                                    fileDetails.setName(re.getName());
                                                    fileDetails.setPath(re.getPath());
                                                    fileDetails.setMod(re.lastModified());
                                                    fileDetails.setImage(R.drawable.ic_videocam);
                                                    fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().getContext(), getFileSize(re)));
                                                    fileDetails.setS(getFileSize(re));
                                                    files.add(fileDetails);
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case Constant.AUDIO:
                                if (file.isFile()) {
                                    if (!file.getName().endsWith(".nomedia")) {
                                        FileDetails fileDetails = new FileDetails();
                                        fileDetails.setName(file.getName());
                                        fileDetails.setPath(file.getPath());
                                        fileDetails.setMod(file.lastModified());
                                        fileDetails.setImage(R.drawable.ic_audio);
                                        extension = FilenameUtils.getExtension((file.getPath()));
                                        String mime = "*/*";
                                        File a = new File(file.getPath());
                                        Uri uri = FileProvider.getUriForFile(filesFragmentWeakReference.get().requireContext(), BuildConfig.APPLICATION_ID + ".provider", a);
                                        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                                        if (mimeTypeMap.hasExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {
                                            mime = Objects.requireNonNull(mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()))).split("/")[0];
                                        }
                                        switch (mime) {
                                            case "image":
                                                fileDetails.setExt(extension);
                                                break;
                                            case "video":
                                                fileDetails.setExt(extension);
                                                break;
                                            case "text":
                                                fileDetails.setExt(extension);
                                                break;
                                            case "application":
                                                fileDetails.setExt(extension);
                                                break;
                                            case "audio":
                                                fileDetails.setExt(extension);
                                                break;
                                            default:
                                                fileDetails.setImage(R.drawable.logo);
                                                break;
                                        }
                                        fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().getContext(), getFileSize(file)));
                                        fileDetails.setS(getFileSize(file));
                                        files.add(fileDetails);
                                    }
                                } else if (file.isDirectory()) {
                                    if (!file.getName().equals("Sent")) {
                                        File[] res = file.listFiles();
                                        if (res != null) {
                                            for (File re : res) {
                                                if (!re.getName().endsWith(".nomedia")) {
                                                    FileDetails fileDetails = new FileDetails();
                                                    fileDetails.setName(re.getName());
                                                    fileDetails.setPath(re.getPath());
                                                    fileDetails.setMod(re.lastModified());
                                                    fileDetails.setImage(R.drawable.ic_audio);
                                                    extension = FilenameUtils.getExtension((re.getPath()));
                                                    String mime = "*/*";
                                                    File a = new File(re.getPath());
                                                    Uri uri = FileProvider.getUriForFile(filesFragmentWeakReference.get().requireContext(), BuildConfig.APPLICATION_ID + ".provider", a);
                                                    MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                                                    if (mimeTypeMap.hasExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {
                                                        mime = Objects.requireNonNull(mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()))).split("/")[0];
                                                    }
                                                    switch (mime) {
                                                        case "image":
                                                            fileDetails.setExt(extension);
                                                            break;
                                                        case "video":
                                                            fileDetails.setExt(extension);
                                                            break;
                                                        case "text":
                                                            fileDetails.setExt(extension);
                                                            break;
                                                        case "application":
                                                            fileDetails.setExt(extension);
                                                            break;
                                                        case "audio":
                                                            fileDetails.setExt(extension);
                                                            break;
                                                        default:
                                                            fileDetails.setImage(R.drawable.logo);
                                                            break;
                                                    }
                                                    fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().getContext(), getFileSize(re)));
                                                    fileDetails.setS(getFileSize(re));
                                                    files.add(fileDetails);
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case Constant.GIF:
                                if (file.isFile()) {
                                    if (!file.getName().endsWith(".nomedia")) {
                                        FileDetails fileDetails = new FileDetails();
                                        fileDetails.setName(file.getName());
                                        fileDetails.setPath(file.getPath());
                                        fileDetails.setMod(file.lastModified());
                                        fileDetails.setImage(R.drawable.ic_gif);
                                        fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().getContext(), getFileSize(file)));
                                        fileDetails.setS(getFileSize(file));
                                        files.add(fileDetails);
                                    }
                                } else if (file.isDirectory()) {
                                    if (!file.getName().equals("Sent")) {
                                        File[] res = file.listFiles();
                                        if (res != null) {
                                            for (File re : res) {
                                                if (!re.getName().endsWith(".nomedia")) {
                                                    FileDetails fileDetails = new FileDetails();
                                                    fileDetails.setName(re.getName());
                                                    fileDetails.setPath(re.getPath());
                                                    fileDetails.setMod(re.lastModified());
                                                    fileDetails.setImage(R.drawable.ic_gif);
                                                    fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().getContext(), getFileSize(re)));
                                                    fileDetails.setS(getFileSize(re));
                                                    files.add(fileDetails);
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case Constant.VOICE:
                                if (file.isDirectory()) {
                                    if (!file.getName().equals("Sent")) {
                                        File[] res = file.listFiles();
                                        if (res != null) {
                                            for (File re : res) {
                                                if (!re.getName().endsWith(".nomedia")) {
                                                    FileDetails fileDetails = new FileDetails();
                                                    fileDetails.setName(re.getName());
                                                    fileDetails.setPath(re.getPath());
                                                    fileDetails.setMod(re.lastModified());
                                                    fileDetails.setImage(R.drawable.ic_voice);
                                                    extension = FilenameUtils.getExtension((re.getPath()));
                                                    String mime = "*/*";
                                                    File a = new File(re.getPath());
                                                    Uri uri = FileProvider.getUriForFile(filesFragmentWeakReference.get().requireContext(), BuildConfig.APPLICATION_ID + ".provider", a);
                                                    MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                                                    if (mimeTypeMap.hasExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {
                                                        mime = Objects.requireNonNull(mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()))).split("/")[0];
                                                    }
                                                    switch (mime) {
                                                        case "image":
                                                            fileDetails.setExt(extension);
                                                            break;
                                                        case "video":
                                                            fileDetails.setExt(extension);
                                                            break;
                                                        case "text":
                                                            fileDetails.setExt(extension);
                                                            break;
                                                        case "application":
                                                            fileDetails.setExt(extension);
                                                            break;
                                                        case "audio":
                                                            fileDetails.setExt(extension);
                                                            break;
                                                        default:
                                                            fileDetails.setImage(R.drawable.logo);
                                                            break;
                                                    }
                                                    fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().getContext(), getFileSize(re)));
                                                    fileDetails.setS(getFileSize(re));
                                                    files.add(fileDetails);
                                                }
                                            }
                                        }
                                    }
                                } else if (file.isFile()) {
                                    if (!file.getName().endsWith(".nomedia")) {
                                        FileDetails fileDetails = new FileDetails();
                                        fileDetails.setName(file.getName());
                                        fileDetails.setPath(file.getPath());
                                        fileDetails.setMod(file.lastModified());
                                        fileDetails.setImage(R.drawable.ic_voice);
                                        extension = FilenameUtils.getExtension((file.getPath()));
                                        String mime = "*/*";
                                        File a = new File(file.getPath());
                                        Uri uri = FileProvider.getUriForFile(filesFragmentWeakReference.get().requireContext(), BuildConfig.APPLICATION_ID + ".provider", a);
                                        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                                        if (mimeTypeMap.hasExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {
                                            mime = Objects.requireNonNull(mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()))).split("/")[0];
                                        }
                                        switch (mime) {
                                            case "image":
                                                fileDetails.setExt(extension);
                                                break;
                                            case "video":
                                                fileDetails.setExt(extension);
                                                break;
                                            case "text":
                                                fileDetails.setExt(extension);
                                                break;
                                            case "application":
                                                fileDetails.setExt(extension);
                                                break;
                                            case "audio":
                                                fileDetails.setExt(extension);
                                                break;
                                            default:
                                                fileDetails.setImage(R.drawable.logo);
                                                break;
                                        }
                                        fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().getContext(), getFileSize(file)));
                                        fileDetails.setS(getFileSize(file));
                                        files.add(fileDetails);
                                    }
                                }
                                break;
                            case Constant.STATUS:
                                if (file.isFile()) {
                                    if (!file.getName().endsWith(".nomedia")) {
                                        FileDetails fileDetails = new FileDetails();
                                        fileDetails.setName(file.getName());
                                        fileDetails.setPath(file.getPath());
                                        fileDetails.setImage(R.drawable.ic_status);
                                        fileDetails.setMod(file.lastModified());
                                        fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().getContext(), getFileSize(file)));
                                        fileDetails.setS(getFileSize(file));
                                        files.add(fileDetails);
                                    }
                                }
                        }
                    }
                }
                Collections.sort(files, (o1, o2) -> {
                    filesFragmentWeakReference.get().flag_d = false;
                    return -o1.getMod().compareTo(o2.getMod());
                });
            }
            return files;
        }

        @Override
        protected void onPostExecute(Object o) {
            List<FileDetails> files = (List<FileDetails>) o;
            if (onFilesFetched != null) {
                onFilesFetched.onFilesFetched(files);
            }
        }

        public interface OnFilesFetched {
            void onFilesFetched(List<FileDetails> fileDetails);
        }
    }
}