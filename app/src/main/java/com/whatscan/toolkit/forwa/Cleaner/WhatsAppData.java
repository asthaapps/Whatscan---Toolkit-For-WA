package com.whatscan.toolkit.forwa.Cleaner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.Adapter.DetailsAdapter;
import com.whatscan.toolkit.forwa.GetSet.Details;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

@SuppressLint({"SetTextI18n", "DefaultLocale"})
public class WhatsAppData extends Fragment implements DetailsAdapter.OnItemClickListener {
    public final static String TAG = "MainActivity";
    public ArrayList<Details> dataList = new ArrayList<>();
    public TextView total_data, txtFiles;
    public DetailsAdapter detailsAdapter1;
    public String sent = "Sent";
    @SuppressWarnings("FieldCanBeLocal")
    public String path;
    @SuppressWarnings("FieldCanBeLocal")
    public String data_img, data_doc, data_vid, data_aud, data_gif, data_wall, data_voice, data_status, data_sticker, tot_dat;
    @SuppressWarnings("FieldCanBeLocal")
    public long sum = 0, size_img, size_doc, size_vid, size_wall, size_aud, size_gif, size_voice, size_status, size_sticker;
    public ArrayList<File> defaultList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_whatsapp_data, container, false);
        FindView(view);
        return view;
    }

    private void FindView(View view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            defaultList.add(new File(Constant.imagesReceivedPath11));
            defaultList.add(new File(Constant.documentsReceivedPath11));
            defaultList.add(new File(Constant.videosReceivedPath11));
            defaultList.add(new File(Constant.statuscache11));
            defaultList.add(new File(Constant.statusdownload11));
            defaultList.add(new File(Constant.audiosReceivedPath11));
            defaultList.add(new File(Constant.voiceReceivedPath11));
            defaultList.add(new File(Constant.wallReceivedPath11));
            defaultList.add(new File(Constant.gifReceivedPath11));
            defaultList.add(new File(Constant.stickerReceivedPath11));
        } else {
            defaultList.add(new File(Constant.imagesReceivedPath));
            defaultList.add(new File(Constant.documentsReceivedPath));
            defaultList.add(new File(Constant.videosReceivedPath));
            defaultList.add(new File(Constant.statuscache));
            defaultList.add(new File(Constant.statusdownload));
            defaultList.add(new File(Constant.audiosReceivedPath));
            defaultList.add(new File(Constant.voiceReceivedPath));
            defaultList.add(new File(Constant.wallReceivedPath));
            defaultList.add(new File(Constant.gifReceivedPath));
            defaultList.add(new File(Constant.stickerReceivedPath));
        }

        total_data = view.findViewById(R.id.data);
        txtFiles = view.findViewById(R.id.txtFiles);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);


        if (Preference.getBooleanTheme(false)) {
            total_data.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
            txtFiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
        } else {
            total_data.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack));
            txtFiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack));
        }

        detailsAdapter1 = new DetailsAdapter(getContext(), dataList, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(detailsAdapter1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Constant.IntProgress(getActivity());

        fetchFiles();
    }

    private void fetchFiles() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            new FetchFiles11(WhatsAppData.this).execute();
        } else {
            new FetchFiles(WhatsAppData.this).execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // fetchFiles();
    }

    @Override
    public void onImagesClicked() {
        Intent intent = new Intent(getActivity(), TabLayoutActivity.class);
        intent.putExtra("category", Constant.IMAGE);
        startActivity(intent);
    }

    @Override
    public void onDocumentsClicked() {
        Intent intent = new Intent(getActivity(), TabLayoutActivity.class);
        intent.putExtra("category", Constant.DOCUMENT);
        startActivity(intent);
    }

    @Override
    public void onVideosClicked() {
        Intent intent = new Intent(getActivity(), TabLayoutActivity.class);
        intent.putExtra("category", Constant.VIDEO);
        startActivity(intent);
    }

    @Override
    public void onStatusClicked() {
//        Intent intent = new Intent(getActivity(), TabLayoutActivity_test.class);
//        intent.putExtra("category", Constant.STATUS);
//        startActivity(intent);
    }

    @Override
    public void onAudiosClicked() {
        Intent intent = new Intent(getActivity(), TabLayoutActivity.class);
        intent.putExtra("category", Constant.AUDIO);
        startActivity(intent);
    }

    @Override
    public void onGifsClicked() {
        Intent intent = new Intent(getActivity(), TabLayoutActivity.class);
        intent.putExtra("category", Constant.GIF);
        startActivity(intent);
    }

    @Override
    public void onWallpapersClicked() {
        Intent intent = new Intent(getActivity(), TabLayoutActivity.class);
        intent.putExtra("category", Constant.WALLPAPER);
        startActivity(intent);
    }

    @Override
    public void onVoiceClicked() {
        Intent intent = new Intent(getActivity(), TabLayoutActivity.class);
        intent.putExtra("category", Constant.VOICE);
        startActivity(intent);
    }

    @Override
    public void onNonDefaultClicked(String path) {
        Intent intent = new Intent(getActivity(), TabLayoutActivity.class);
        intent.putExtra("category", Constant.NONDEFAULT);
        intent.putExtra("pathname", path);
        startActivity(intent);
    }

    private static class FetchFiles11 extends AsyncTask<Void, Void, String> {

        public WeakReference<WhatsAppData> mainActivityWeakReference;

        FetchFiles11(WhatsAppData mainActivity) {
            this.mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Constant.ShowProgress();
        }

        @Override
        protected String doInBackground(Void... voids) {
            File root = new File(Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/");
            File[] listOfFiles = root.listFiles();
            long tot_size = 0;
            if (listOfFiles != null) {
                for (int i = 0; i < listOfFiles.length; i++) {
                    long size = FileUtils.sizeOfDirectory(listOfFiles[i]);
                    Log.d(TAG, listOfFiles[i].getPath());
                    tot_size += size;
                }
            }
            mainActivityWeakReference.get().sum = tot_size;

            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images";

            File img = new File(mainActivityWeakReference.get().path);
            String img_sent = Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images/" + mainActivityWeakReference.get().sent;
            File imgs = new File(img_sent);

            if (!img.exists()) {
                String imgg = Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images";
                File image = new File(imgg);
                if (!image.exists()) {
                    if (!image.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!imgs.exists()) {
                            if (!imgs.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!imgs.exists()) {
                    if (!imgs.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_img = FileUtils.sizeOfDirectory(new File(Constant.imagesReceivedPath11));
                mainActivityWeakReference.get().data_img = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_img);
            }

            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Documents";
            File doc = new File(mainActivityWeakReference.get().path);

            String doc_sent = Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Documents/" + mainActivityWeakReference.get().sent;
            File dc = new File(doc_sent);

            if (!doc.exists()) {
                String docc = Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Documents";
                File docs = new File(docc);
                if (!docs.exists()) {
                    if (!docs.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!dc.exists()) {
                            if (!dc.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!dc.exists()) {
                    if (!dc.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_doc = FileUtils.sizeOfDirectory(new File(Constant.documentsReceivedPath11));
                mainActivityWeakReference.get().data_doc = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_doc);
            }


            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Video";
            File vid = new File(mainActivityWeakReference.get().path);
            String vid_sent = Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Video/" + mainActivityWeakReference.get().sent;
            File vidd = new File(vid_sent);

            if (!vid.exists()) {
                String vi = Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Video";
                File video = new File(vi);
                if (!video.exists()) {
                    if (!video.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!vidd.exists()) {
                            if (!vidd.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!vidd.exists()) {
                    if (!vidd.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_vid = FileUtils.sizeOfDirectory(new File(Constant.videosReceivedPath11));
                mainActivityWeakReference.get().data_vid = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_vid);
            }

            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Audio";
            File aud = new File(mainActivityWeakReference.get().path);
            String aud_sent = Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Audio/" + mainActivityWeakReference.get().sent;
            File audd = new File(aud_sent);

            if (!aud.exists()) {
                String audi = Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Audio";
                File audio = new File(audi);
                if (!audio.exists()) {
                    if (!audio.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!audd.exists()) {
                            if (!audd.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!audd.exists()) {
                    if (!audd.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_aud = FileUtils.sizeOfDirectory(new File(Constant.audiosReceivedPath11));
                mainActivityWeakReference.get().data_aud = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_aud);
            }


            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WallPaper";
            File wall = new File(mainActivityWeakReference.get().path);
            String wallpaper_sent = Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WallPaper/" + mainActivityWeakReference.get().sent;
            File file = new File(wallpaper_sent);

            if (!wall.exists()) {
                String wallp = Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/" + "WallPaper";
                File walls = new File(wallp);
                if (!walls.exists()) {
                    if (!walls.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!file.exists()) {
                            if (!file.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("FIle", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!file.exists()) {
                    if (!file.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_wall = FileUtils.sizeOfDirectory(new File(mainActivityWeakReference.get().path));
                mainActivityWeakReference.get().data_wall = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_wall);
            }


            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Animated Gifs";
            File gif = new File(mainActivityWeakReference.get().path);
            String gi_sent = Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Animated Gifs/" + mainActivityWeakReference.get().sent;
            File gii = new File(gi_sent);

            if (!gif.exists()) {
                String gifs = Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/" + "WhatsApp Animated Gifs";
                File gi = new File(gifs);
                if (!gi.exists()) {
                    if (!gi.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!gii.exists()) {
                            if (!gii.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!gii.exists()) {
                    if (!gii.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_gif = FileUtils.sizeOfDirectory(new File(Constant.gifReceivedPath11));
                mainActivityWeakReference.get().data_gif = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_gif);
            }

            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Stickers";
            File sticker = new File(mainActivityWeakReference.get().path);
            String sticker_sent = Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Stickers/" + mainActivityWeakReference.get().sent;
            File stick = new File(sticker_sent);

            if (!sticker.exists()) {
                String gifs = Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/" + "WhatsApp Stickers";
                File gi = new File(gifs);
                if (!gi.exists()) {
                    if (!gi.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!stick.exists()) {
                            if (!stick.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!stick.exists()) {
                    if (!stick.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_sticker = FileUtils.sizeOfDirectory(new File(Constant.stickerReceivedPath11));
                mainActivityWeakReference.get().data_sticker = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_sticker);
            }


            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Voice Notes";
            File vo = new File(mainActivityWeakReference.get().path);
            String voice_sent = Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Voice Notes/" + mainActivityWeakReference.get().sent;
            File file1 = new File(voice_sent);

            if (!vo.exists()) {
                String voi = Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Voice Notes";
                File voic = new File(voi);
                if (!voic.exists()) {
                    if (!voic.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!file1.exists()) {
                            if (!file1.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!file1.exists()) {
                    if (!file1.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_voice = FileUtils.sizeOfDirectory(new File(mainActivityWeakReference.get().path));
                mainActivityWeakReference.get().data_voice = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_voice);
            }


            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/.Status Download";
            File status = new File(mainActivityWeakReference.get().path);
            if (!status.exists()) {
                if (!status.mkdir()) {
                    Log.e("FIle", "Can't be created");
                } else {
                    Log.e("FIle", "created");
                }
            } else {
                mainActivityWeakReference.get().size_status = FileUtils.sizeOfDirectory(new File(mainActivityWeakReference.get().path));
                mainActivityWeakReference.get().data_status = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_status);
            }
            mainActivityWeakReference.get().tot_dat = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().sum);


            mainActivityWeakReference.get().dataList.clear();
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Images",
                    mainActivityWeakReference.get().data_img, Constant.imagesReceivedPath11,
                    R.drawable.ic_image,
                    R.color.colorTools));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Documents",
                    mainActivityWeakReference.get().data_doc, Constant.documentsReceivedPath11,
                    R.drawable.ic_document,
                    R.color.colorTools));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Videos",
                    mainActivityWeakReference.get().data_vid, Constant.videosReceivedPath11,
                    R.drawable.ic_videocam,
                    R.color.colorTools));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Statuses",
                    mainActivityWeakReference.get().data_status, Constant.statuscache11,
                    R.drawable.ic_status,
                    R.color.colorTools));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Audio files",
                    mainActivityWeakReference.get().data_aud, Constant.audiosReceivedPath11,
                    R.drawable.ic_audio,
                    R.color.colorTools));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Voice files",
                    mainActivityWeakReference.get().data_voice, Constant.voiceReceivedPath11,
                    R.drawable.ic_voice,
                    R.color.colorTools));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Wallpapers",
                    mainActivityWeakReference.get().data_wall, Constant.wallReceivedPath11,
                    R.drawable.ic_w_wallpater,
                    R.color.colorTools));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "GIFs",
                    mainActivityWeakReference.get().data_gif, Constant.gifReceivedPath11,
                    R.drawable.ic_gif,
                    R.color.colorTools));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Stickers",
                    mainActivityWeakReference.get().data_sticker, Constant.stickerReceivedPath11,
                    R.drawable.ic_stick,
                    R.color.colorTools));


            if (listOfFiles != null) {
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (!mainActivityWeakReference.get().defaultList.contains(listOfFiles[i])) {
                        long size = FileUtils.sizeOfDirectory(listOfFiles[i]);
                        String pathName = listOfFiles[i].getPath();
                        String folderName = pathName.substring(pathName.indexOf("a/") + 2);
                        if (folderName.startsWith("WhatsApp ")) {
                            folderName = folderName.substring(9);
                        }
                    }
                }
            }
            return mainActivityWeakReference.get().tot_dat;
        }

        @Override
        protected void onPostExecute(String s) {
            Constant.DismissProgress();
            mainActivityWeakReference.get().total_data.setText(s);
            mainActivityWeakReference.get().detailsAdapter1.notifyDataSetChanged();
        }
    }

    private static class FetchFiles extends AsyncTask<Void, Void, String> {

        public WeakReference<WhatsAppData> mainActivityWeakReference;

        FetchFiles(WhatsAppData mainActivity) {
            this.mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Constant.ShowProgress();
        }

        @Override
        protected String doInBackground(Void... voids) {

            File root = new File(Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/");
            File[] listOfFiles = root.listFiles();
            long tot_size = 0;
            if (listOfFiles != null) {
                for (int i = 0; i < listOfFiles.length; i++) {
                    long size = FileUtils.sizeOfDirectory(listOfFiles[i]);
                    Log.d(TAG, listOfFiles[i].getPath());
                    tot_size += size;
                }
            }
            mainActivityWeakReference.get().sum = tot_size;

            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Images";
            File img = new File(mainActivityWeakReference.get().path);
            String img_sent = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Images/" + mainActivityWeakReference.get().sent;
            File imgs = new File(img_sent);

            if (!img.exists()) {
                String imgg = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Images";
                File image = new File(imgg);
                if (!image.exists()) {
                    if (!image.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!imgs.exists()) {
                            if (!imgs.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!imgs.exists()) {
                    if (!imgs.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_img = FileUtils.sizeOfDirectory(new File(Constant.imagesReceivedPath));
                mainActivityWeakReference.get().data_img = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_img);
            }


            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Documents";
            File doc = new File(mainActivityWeakReference.get().path);
            String doc_sent = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Documents/" + mainActivityWeakReference.get().sent;
            File dc = new File(doc_sent);

            if (!doc.exists()) {
                String docc = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Documents";
                File docs = new File(docc);
                if (!docs.exists()) {
                    if (!docs.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!dc.exists()) {
                            if (!dc.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!dc.exists()) {
                    if (!dc.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_doc = FileUtils.sizeOfDirectory(new File(Constant.documentsReceivedPath));
                mainActivityWeakReference.get().data_doc = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_doc);
            }


            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Video";
            File vid = new File(mainActivityWeakReference.get().path);
            String vid_sent = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Video/" + mainActivityWeakReference.get().sent;
            File vidd = new File(vid_sent);

            if (!vid.exists()) {
                String vi = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Video";
                File video = new File(vi);
                if (!video.exists()) {
                    if (!video.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!vidd.exists()) {
                            if (!vidd.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!vidd.exists()) {
                    if (!vidd.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_vid = FileUtils.sizeOfDirectory(new File(Constant.videosReceivedPath));
                mainActivityWeakReference.get().data_vid = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_vid);
            }


            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Audio";
            File aud = new File(mainActivityWeakReference.get().path);
            String aud_sent = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Audio/" + mainActivityWeakReference.get().sent;
            File audd = new File(aud_sent);

            if (!aud.exists()) {
                String audi = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Audio";
                File audio = new File(audi);
                if (!audio.exists()) {
                    if (!audio.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!audd.exists()) {
                            if (!audd.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!audd.exists()) {
                    if (!audd.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_aud = FileUtils.sizeOfDirectory(new File(Constant.audiosReceivedPath));
                mainActivityWeakReference.get().data_aud = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_aud);
            }


            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WallPaper";
            File wall = new File(mainActivityWeakReference.get().path);
            String wallpaper_sent = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WallPaper/" + mainActivityWeakReference.get().sent;
            File file = new File(wallpaper_sent);

            if (!wall.exists()) {
                String wallp = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/" + "WallPaper";
                File walls = new File(wallp);
                if (!walls.exists()) {
                    if (!walls.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!file.exists()) {
                            if (!file.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("FIle", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!file.exists()) {
                    if (!file.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_wall = FileUtils.sizeOfDirectory(new File(mainActivityWeakReference.get().path));
                mainActivityWeakReference.get().data_wall = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_wall);
            }


            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Animated Gifs";
            File gif = new File(mainActivityWeakReference.get().path);
            String gi_sent = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Animated Gifs/" + mainActivityWeakReference.get().sent;
            File gii = new File(gi_sent);

            if (!gif.exists()) {
                String gifs = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/" + "WhatsApp Animated Gifs";
                File gi = new File(gifs);
                if (!gi.exists()) {
                    if (!gi.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!gii.exists()) {
                            if (!gii.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!gii.exists()) {
                    if (!gii.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_gif = FileUtils.sizeOfDirectory(new File(Constant.gifReceivedPath));
                mainActivityWeakReference.get().data_gif = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_gif);
            }


            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Stickers";
            File sticker = new File(mainActivityWeakReference.get().path);
            String sticker_sent = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Stickers/" + mainActivityWeakReference.get().sent;
            File stick = new File(sticker_sent);

            if (!sticker.exists()) {
                String gifs = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/" + "WhatsApp Stickers";
                File gi = new File(gifs);
                if (!gi.exists()) {
                    if (!gi.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!stick.exists()) {
                            if (!stick.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!stick.exists()) {
                    if (!stick.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_sticker = FileUtils.sizeOfDirectory(new File(Constant.stickerReceivedPath));
                mainActivityWeakReference.get().data_sticker = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_sticker);
            }


            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Voice Notes";
            File vo = new File(mainActivityWeakReference.get().path);
            String voice_sent = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Voice Notes/" + mainActivityWeakReference.get().sent;
            File file1 = new File(voice_sent);

            if (!vo.exists()) {
                String voi = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Voice Notes";
                File voic = new File(voi);
                if (!voic.exists()) {
                    if (!voic.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!file1.exists()) {
                            if (!file1.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!file1.exists()) {
                    if (!file1.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_voice = FileUtils.sizeOfDirectory(new File(mainActivityWeakReference.get().path));
                mainActivityWeakReference.get().data_voice = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_voice);
            }


            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Status Download";
            File status = new File(mainActivityWeakReference.get().path);
            if (!status.exists()) {
                if (!status.mkdir()) {
                    Log.e("FIle", "Can't be created");
                } else {
                    Log.e("FIle", "created");
                }
            } else {
                mainActivityWeakReference.get().size_status = FileUtils.sizeOfDirectory(new File(mainActivityWeakReference.get().path));
                mainActivityWeakReference.get().data_status = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().size_status);
            }
            mainActivityWeakReference.get().tot_dat = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), mainActivityWeakReference.get().sum);


            mainActivityWeakReference.get().dataList.clear();
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Images",
                    mainActivityWeakReference.get().data_img, Constant.imagesReceivedPath,
                    R.drawable.ic_image,
                    R.color.colorTools));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Documents",
                    mainActivityWeakReference.get().data_doc, Constant.documentsReceivedPath,
                    R.drawable.ic_document,
                    R.color.colorTools));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Videos",
                    mainActivityWeakReference.get().data_vid, Constant.videosReceivedPath,
                    R.drawable.ic_videocam,
                    R.color.colorTools));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Statuses",
                    mainActivityWeakReference.get().data_status, Constant.statuscache,
                    R.drawable.ic_status,
                    R.color.colorTools));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Audio files",
                    mainActivityWeakReference.get().data_aud, Constant.audiosReceivedPath,
                    R.drawable.ic_audio,
                    R.color.colorTools));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Voice files",
                    mainActivityWeakReference.get().data_voice, Constant.voiceReceivedPath,
                    R.drawable.ic_voice,
                    R.color.colorTools));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Wallpapers",
                    mainActivityWeakReference.get().data_wall, Constant.wallReceivedPath,
                    R.drawable.ic_w_wallpater,
                    R.color.colorTools));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "GIFs",
                    mainActivityWeakReference.get().data_gif, Constant.gifReceivedPath,
                    R.drawable.ic_gif,
                    R.color.colorTools));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Stickers",
                    mainActivityWeakReference.get().data_sticker, Constant.stickerReceivedPath,
                    R.drawable.ic_stick,
                    R.color.colorTools));


            if (listOfFiles != null) {
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (!mainActivityWeakReference.get().defaultList.contains(listOfFiles[i])) {
                        long size = FileUtils.sizeOfDirectory(listOfFiles[i]);
                        String pathName = listOfFiles[i].getPath();
                        String folderName = pathName.substring(pathName.indexOf("a/") + 2);
                        if (folderName.startsWith("WhatsApp ")) {
                            folderName = folderName.substring(9);
                        }
                        String data = Formatter.formatShortFileSize(mainActivityWeakReference.get().getContext(), size);
//                        mainActivityWeakReference.get().dataList.add(new Details(
//                                folderName,
//                                data, pathName,
//                                R.drawable.ic_image,
//                                R.color.colorTools));
                    }
                }
            }
            return mainActivityWeakReference.get().tot_dat;
        }

        @Override
        protected void onPostExecute(String s) {
            Constant.DismissProgress();
            mainActivityWeakReference.get().total_data.setText(s);
            mainActivityWeakReference.get().detailsAdapter1.notifyDataSetChanged();
        }
    }
}