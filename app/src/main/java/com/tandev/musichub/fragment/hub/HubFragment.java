package com.tandev.musichub.fragment.hub;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.hub.HubVerticalAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.HubCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.api.type_adapter_Factory.home.HubSectionTypeAdapter;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.helper.ui.StatusBarUtil;
import com.tandev.musichub.model.hub.Hub;
import com.tandev.musichub.model.hub.HubSection;
import com.tandev.musichub.model.hub.SectionHubArtist;
import com.tandev.musichub.model.hub.SectionHubPlaylist;
import com.tandev.musichub.model.hub.SectionHubSong;
import com.tandev.musichub.model.hub.SectionHubVideo;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HubFragment extends Fragment {
    private Hub hub;
    private RelativeLayout relative_header;
    private ImageView img_back;
    private TextView txt_name_playlist, txt_view;
    private NestedScrollView nested_scroll;
    private RecyclerView rv_playlist_vertical;
    private ImageView img_playlist;
    private ProgressBar progress_image;

    private  ArrayList<SectionHubSong> sectionHubSongArrayList;
    private  ArrayList<SectionHubPlaylist> sectionHubPlaylists ;
    private  ArrayList<SectionHubVideo> sectionHubVideoArrayList ;
    private  ArrayList<SectionHubArtist> sectionHubArtistArrayList;
    private HubVerticalAdapter hubVerticalAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hub, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Helper.changeStatusBarTransparent(requireActivity());

        initViews(view);
        initAdapter();
        conFigViews();
        onClick();
        getDataBundle();


    }

    private void getDataBundle() {
        if (getArguments() != null) {
            String encodeId = getArguments().getString("encodeId");
            getHub(encodeId);
        }
    }

    private void initViews(View view) {
        relative_header = view.findViewById(R.id.relative_header);
        img_back = view.findViewById(R.id.img_back);
        txt_name_playlist = view.findViewById(R.id.txt_name_playlist);
        txt_view = view.findViewById(R.id.txt_view);
        nested_scroll = view.findViewById(R.id.nested_scroll);
        rv_playlist_vertical = view.findViewById(R.id.rv_playlist_vertical);

        img_playlist = view.findViewById(R.id.img_playlist);
        progress_image = view.findViewById(R.id.progress_image);

    }

    private void initAdapter() {
        sectionHubSongArrayList = new ArrayList<>();
        sectionHubPlaylists = new ArrayList<>();
        sectionHubVideoArrayList = new ArrayList<>();
        sectionHubArtistArrayList = new ArrayList<>();

        LinearLayoutManager layoutManagerPlaylist1 = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rv_playlist_vertical.setLayoutManager(layoutManagerPlaylist1);
        hubVerticalAdapter = new HubVerticalAdapter(requireContext(), requireActivity(), sectionHubSongArrayList, sectionHubPlaylists, sectionHubVideoArrayList, sectionHubArtistArrayList);
        rv_playlist_vertical.setAdapter(hubVerticalAdapter);
    }

    private void conFigViews() {
        nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @SuppressLint("ObsoleteSdkInt")
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Kiểm tra nếu người dùng đã cuộn đến đầu trang
                if (scrollY <= 600) {
                    // Ẩn TextView khi người dùng cuộn trở lại đầu trang
                    txt_name_playlist.setVisibility(View.GONE);
                    txt_view.setVisibility(View.VISIBLE);
                    relative_header.setBackgroundResource(android.R.color.transparent);
                    // Make the content appear under the status barr
                    Helper.changeStatusBarTransparent(requireActivity());

                } else if (scrollY >= 800) {
                    // Hiển thị TextView khi người dùng cuộn xuống khỏi đầu trang
                    txt_name_playlist.setVisibility(View.VISIBLE);
                    txt_view.setVisibility(View.GONE);
                    txt_name_playlist.setText(hub.getData().getTitle());
                    relative_header.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray));
                    Helper.changeStatusBarColor(requireActivity(), R.color.gray);
                }
            }
        });
    }

    private void onClick() {
        img_back.setOnClickListener(view -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void getHub(String encodeId) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    HubCategories hubCategories = new HubCategories();
                    Map<String, String> map = hubCategories.getHub(encodeId);

                    retrofit2.Call<ResponseBody> call = service.HUB_DETAIL_CALL(map.get("id"), map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d(">>>>>>>>>>>>>>>>>>", "getHub " + call.request().url());
                            if (response.isSuccessful() && response.body() != null) {
                                try {
                                    String jsonData = response.body().string();
                                    GsonBuilder gsonBuilder = new GsonBuilder();
                                    gsonBuilder.registerTypeAdapter(HubSection.class, new HubSectionTypeAdapter());
                                    Gson gson = gsonBuilder.create();

                                    hub = gson.fromJson(jsonData, Hub.class);

                                    if (hub != null && hub.getData() != null) {
                                        ArrayList<HubSection> items = hub.getData().getSections();
                                        for (HubSection item : items) {
                                            if (item instanceof SectionHubPlaylist) {
                                                SectionHubPlaylist sectionHubSong = (SectionHubPlaylist) item;
                                                sectionHubPlaylists.add(sectionHubSong);
                                            } else if (item instanceof SectionHubVideo) {
                                                SectionHubVideo sectionHubVideo = (SectionHubVideo) item;
                                                sectionHubVideoArrayList.add(sectionHubVideo);
                                            } else if (item instanceof SectionHubArtist) {
                                                SectionHubArtist sectionHubArtist = (SectionHubArtist) item;
                                                sectionHubArtistArrayList.add(sectionHubArtist);
                                            } else {
                                                SectionHubSong sectionHubSong = (SectionHubSong) item;
                                                sectionHubSongArrayList.add(sectionHubSong);
                                            }
                                        }
                                        RequestOptions requestOptions = new RequestOptions()
                                                .placeholder(R.color.gray)  // Placeholder khi đang tải ảnh
                                                .error(R.color.red);  // Ảnh lỗi nếu không thể tải ảnh

                                        Glide.with(requireContext())
                                                .load(hub.getData().getThumbnailHasText())  // Tải ảnh chính từ liên kết
                                                .apply(requestOptions)  // Áp dụng RequestOptions
                                                .into(img_playlist);  // Hiển thị ảnh chính lên ImageView
// Hiển thị ảnh chính lên ImageView
                                        progress_image.setVisibility(View.GONE);
                                        img_playlist.setVisibility(View.VISIBLE);
                                        hubVerticalAdapter.setFilterList(sectionHubSongArrayList, sectionHubPlaylists, sectionHubVideoArrayList, sectionHubArtistArrayList);
                                    } else {
                                        Log.d("TAG", "No data found in JSON");
                                    }

                                } catch (Exception e) {
                                    Log.e("TAG", "Error: " + e.getMessage(), e);
                                }
                            } else {
                                Log.d("TAG", "Response unsuccessful or empty body");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                            Log.d(">>>>>>>>>>>>>>>>>>", "getHub1111 " + call.request().url());
                        }
                    });

                } catch (Exception e) {
                    Log.e("TAG", "Error: " + e.getMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("TAG", "Service creation error: " + e.getMessage(), e);
            }
        });
    }
}