package com.tandev.musichub.fragment.artist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.artist.ArtistsAdapter;
import com.tandev.musichub.adapter.playlist.PlaylistAdapter;
import com.tandev.musichub.adapter.single.SingleAdapter;
import com.tandev.musichub.adapter.song.SongMoreAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.fragment.song.SongFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.artist.SectionArtistArtist;
import com.tandev.musichub.model.artist.SectionArtistPlaylist;
import com.tandev.musichub.model.artist.SectionArtistSong;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistFragment extends Fragment {
    private DataPlaylist dataPlaylistNewSong;
    boolean isFirstPlaylist = true;
    private RelativeLayout relative_header;
    private RelativeLayout relative_loading;
    private NestedScrollView nested_scroll;
    private TextView txt_name_artist;
    private TextView txt_view;
    private ImageView img_back;
    private ImageView img_more;
    private ImageView img_artist;
    private ProgressBar progress_image;
    private TextView txt_artist;
    private TextView txt_follow;
    private RoundedImageView img_song;
    private TextView txtTile;
    private TextView txtArtist;


    private RelativeLayout relative_new_song;


    private LinearLayout linear_noibat;
    private RelativeLayout relative_noibat;
    private RecyclerView rv_noibat;

    private RelativeLayout relative_single;
    private RecyclerView rv_single;

    private RelativeLayout relative_album;
    private RecyclerView rv_album;

    private RelativeLayout relative_mv;
    private RecyclerView rv_mv;

    private RelativeLayout relative_playlist;
    private RecyclerView rv_playlist;

    private RelativeLayout relative_xuathientrong;
    private RecyclerView rv_xuathientrong;

    private RelativeLayout relative_other_single;
    private RecyclerView rv_other_single;

    private RelativeLayout relative_info_single;
    private TextView txt_info;
    private TextView txt_name_real;
    private TextView txt_date_birth;
    private TextView txt_country;
    private TextView txt_genre;
    private String name, id;


    //noi bat
    private SongMoreAdapter noibatAdapter;
    private ArrayList<Items> itemsArrayListNoiBat;
    private SectionArtistSong sectionArtistSong;


    //single
    private SingleAdapter singleAdapter;
    private ArrayList<DataPlaylist> dataSingleArrayList;
    private SectionArtistPlaylist sectionArtistPlaylistSingle;


    //playlist
    private PlaylistAdapter playlistAdapter;
    private ArrayList<DataPlaylist> dataPlaylistArrayList;
    private SectionArtistPlaylist sectionArtistPlaylist;


    //xuat hien trong
    private PlaylistAdapter xuatHienTrongAdapter;
    private ArrayList<DataPlaylist> dataPlaylistXuatHienTrong;
    private SectionArtistPlaylist sectionArtistPlaylistXuatHienTrong;


    //other single
    private ArtistsAdapter otherSingleAdapter;
    private ArrayList<Artists> artistsArrayList;
    private SectionArtistArtist sectionArtistArtist;


    //player bottom
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        requireActivity().getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);

        Helper.changeNavigationColor(requireActivity(), R.color.gray, true);


        sharedPreferencesManager = new SharedPreferencesManager(requireContext());

        img_back = view.findViewById(R.id.img_back);
        img_more = view.findViewById(R.id.img_more);

        relative_header = view.findViewById(R.id.relative_header);
        relative_loading = view.findViewById(R.id.relative_loading);
        nested_scroll = view.findViewById(R.id.nested_scroll);
        txt_name_artist = view.findViewById(R.id.txt_name_artist);
        txt_view = view.findViewById(R.id.txt_view);

        img_artist = view.findViewById(R.id.img_artist);
        progress_image = view.findViewById(R.id.progress_image);
        txt_artist = view.findViewById(R.id.txt_artist);
        txt_follow = view.findViewById(R.id.txt_follow);

        img_song = view.findViewById(R.id.img_song);
        txtTile = view.findViewById(R.id.txtTile);
        txtArtist = view.findViewById(R.id.txtArtist);

        relative_new_song = view.findViewById(R.id.relative_new_song);

        linear_noibat = view.findViewById(R.id.linear_noibat);
        relative_noibat = view.findViewById(R.id.relative_noibat);
        rv_noibat = view.findViewById(R.id.rv_noibat);

        relative_single = view.findViewById(R.id.relative_single);
        rv_single = view.findViewById(R.id.rv_single);


        relative_album = view.findViewById(R.id.relative_album);
        rv_album = view.findViewById(R.id.rv_album);

        relative_mv = view.findViewById(R.id.relative_mv);
        rv_mv = view.findViewById(R.id.rv_mv);

        relative_playlist = view.findViewById(R.id.relative_playlist);
        rv_playlist = view.findViewById(R.id.rv_playlist);

        relative_xuathientrong = view.findViewById(R.id.relative_xuathientrong);
        rv_xuathientrong = view.findViewById(R.id.rv_xuathientrong);

        relative_other_single = view.findViewById(R.id.relative_other_single);
        rv_other_single = view.findViewById(R.id.rv_other_single);

        relative_info_single = view.findViewById(R.id.relative_info_single);

        txt_info = view.findViewById(R.id.txt_info);
        txt_name_real = view.findViewById(R.id.txt_name_real);
        txt_date_birth = view.findViewById(R.id.txt_date_birth);
        txt_country = view.findViewById(R.id.txt_country);
        txt_genre = view.findViewById(R.id.txt_genre);

        //noibat
        GridLayoutManager layoutManagerNhacMoi = new GridLayoutManager(requireContext(), 4, RecyclerView.HORIZONTAL, false);
        rv_noibat.setLayoutManager(layoutManagerNhacMoi);

        sectionArtistSong = new SectionArtistSong();
        itemsArrayListNoiBat = new ArrayList<>();
        noibatAdapter = new SongMoreAdapter(itemsArrayListNoiBat, 3, requireActivity(), requireContext());
        rv_noibat.setAdapter(noibatAdapter);


        //other single
        LinearLayoutManager layoutManagerOtherSingle = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_other_single.setLayoutManager(layoutManagerOtherSingle);

        sectionArtistArtist = new SectionArtistArtist();
        artistsArrayList = new ArrayList<>();
        otherSingleAdapter = new ArtistsAdapter(artistsArrayList, requireActivity(), requireContext());
        rv_other_single.setAdapter(otherSingleAdapter);


        //single
        LinearLayoutManager layoutManagerSingle = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_single.setLayoutManager(layoutManagerSingle);

        sectionArtistPlaylistSingle = new SectionArtistPlaylist();
        dataSingleArrayList = new ArrayList<>();
        singleAdapter = new SingleAdapter(dataSingleArrayList, requireActivity(), requireContext());
        rv_single.setAdapter(singleAdapter);


        //playlist
        LinearLayoutManager layoutManagerPlaylist = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_playlist.setLayoutManager(layoutManagerPlaylist);

        sectionArtistPlaylist = new SectionArtistPlaylist();
        dataPlaylistArrayList = new ArrayList<>();
        playlistAdapter = new PlaylistAdapter(dataPlaylistArrayList, requireActivity(), requireContext());
        rv_playlist.setAdapter(playlistAdapter);


        //xuat hien trong
        LinearLayoutManager layoutManagerPlaylistXuatHienTrong = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_xuathientrong.setLayoutManager(layoutManagerPlaylistXuatHienTrong);

        sectionArtistPlaylistXuatHienTrong = new SectionArtistPlaylist();
        dataPlaylistXuatHienTrong = new ArrayList<>();
        xuatHienTrongAdapter = new PlaylistAdapter(dataPlaylistXuatHienTrong, requireActivity(), requireContext());
        rv_xuathientrong.setAdapter(xuatHienTrongAdapter);

        img_back.setOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @SuppressLint("ObsoleteSdkInt")
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Kiểm tra nếu người dùng đã cuộn đến đầu trang
                if (scrollY <= 600) {
                    // Ẩn TextView khi người dùng cuộn trở lại đầu trang
                    txt_name_artist.setVisibility(View.GONE);
                    txt_view.setVisibility(View.VISIBLE);
                    relative_header.setBackgroundResource(android.R.color.transparent);
                    // Make the content appear under the status bar
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                        requireActivity().getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
                    }

                } else if (scrollY >= 800) {
                    // Hiển thị TextView khi người dùng cuộn xuống khỏi đầu trang
                    txt_name_artist.setVisibility(View.VISIBLE);
                    txt_view.setVisibility(View.GONE);
                    txt_name_artist.setText(name);
                    relative_header.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray));
                    Helper.changeStatusBarColor(requireActivity(), R.color.gray);
                }
            }
        });


        relative_new_song.setOnClickListener(view1 -> {
//            Intent intent = new Intent(ViewArtistActivity.this, ViewAlbumActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("album_endCodeId", dataPlaylistNewSong.getEncodeId());
//            intent.putExtras(bundle);
//
//            startActivity(intent);
        });
        linear_noibat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SongFragment songFragment = new SongFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("sectionId", sectionArtistSong.getSectionId());

                if (requireContext() instanceof MainActivity) {
                    ((MainActivity) requireContext()).replaceFragmentWithBundle(songFragment, bundle);
                }
            }
        });

        getDataBundle();
    }

    private void getDataBundle() {
        if (getArguments() != null) {
            String alias = getArguments().getString("alias");
            getArtist(alias);
        }
    }


    public int findItemPosition(ArrayList<Items> itemsArrayList, String targetEndcodeID) {
        for (int i = 0; i < itemsArrayList.size(); i++) {
            Items item = itemsArrayList.get(i);
            if (item != null && item.getEncodeId() != null) {
                if (targetEndcodeID.equals(item.getEncodeId())) {
                    return i; // Trả về vị trí của phần tử trong mảng
                }
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy phần tử
    }

    private void getArtist(String artistId) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    SongCategories songCategories = new SongCategories();
                    Map<String, String> map = songCategories.getArtist(artistId);

                    Call<ResponseBody> call = service.ARTISTS_CALL(artistId, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            String requestUrl = call.request().url().toString();
                            Log.d(">>>>>>>>>>>>>>>>>>>", " - " + requestUrl);

                            if (response.isSuccessful() && response.body() != null) {
                                try {
                                    String responseBody = response.body().string();
                                    JSONObject jsonObject = new JSONObject(responseBody);

                                    if (jsonObject.getInt("err") == 0) {
                                        JSONObject data = jsonObject.getJSONObject("data");
                                        id = data.getString("id");
                                        name = data.getString("name");
                                        int totalFollow = data.getInt("totalFollow");
                                        String thumbnailM = data.getString("thumbnailM");

                                        String real_name = data.getString("realname");
                                        String biography = data.getString("biography");
                                        String national = data.getString("national");
                                        String birthday = data.getString("birthday");


                                        //topAlbum
                                        JSONObject jsonObjectNewSong = data.optJSONObject("topAlbum");

                                        //song
                                        JSONArray jsonArray = data.optJSONArray("sections");


                                        for (int check = 0; check < jsonArray.length(); check++) {
                                            JSONObject jsonObjectSectionType = jsonArray.getJSONObject(check);
                                            String sectionId = jsonObjectSectionType.getString("sectionId");


                                            if (sectionId.equals("aSongs")) {
                                                JSONObject jsonObjectSong = jsonArray.getJSONObject(check);
                                                sectionArtistSong.setSectionType(jsonObjectSong.getString("sectionType"));
                                                sectionArtistSong.setViewType(jsonObjectSong.getString("viewType"));
                                                sectionArtistSong.setTitle(jsonObjectSong.getString("title"));
                                                sectionArtistSong.setLink(jsonObjectSong.getString("link"));
                                                sectionArtistSong.setSectionId(jsonObjectSong.getString("sectionId"));

                                                JSONArray innerItemsArray = jsonObjectSong.getJSONArray("items");
                                                ArrayList<Items> itemsArrayListSong = new ArrayList<>();
                                                for (int i = 0; i < innerItemsArray.length(); i++) {
                                                    JSONObject innerItemObject = innerItemsArray.getJSONObject(i);
                                                    Items items = Items.fromJson(innerItemObject.toString());
                                                    itemsArrayListSong.add(items);
                                                }
                                                sectionArtistSong.setItems(itemsArrayListSong);
                                            }

                                            if (sectionId.equals("aSingle")) {
                                                JSONObject jsonObjectSong = jsonArray.getJSONObject(check);
                                                sectionArtistPlaylistSingle.setSectionType(jsonObjectSong.getString("sectionType"));
                                                sectionArtistPlaylistSingle.setViewType(jsonObjectSong.getString("viewType"));
                                                sectionArtistPlaylistSingle.setTitle(jsonObjectSong.getString("title"));
                                                sectionArtistPlaylistSingle.setLink(jsonObjectSong.getString("link"));
                                                sectionArtistPlaylistSingle.setSectionId(jsonObjectSong.getString("sectionId"));

                                                JSONArray innerItemsArray = jsonObjectSong.getJSONArray("items");
                                                ArrayList<DataPlaylist> dataPlaylistArrayList = new ArrayList<>();
                                                for (int i = 0; i < innerItemsArray.length(); i++) {
                                                    JSONObject innerItemObject = innerItemsArray.getJSONObject(i);
                                                    DataPlaylist dataPlaylist = DataPlaylist.fromJson(innerItemObject.toString());
                                                    dataPlaylistArrayList.add(dataPlaylist);
                                                }
                                                sectionArtistPlaylistSingle.setItems(dataPlaylistArrayList);
                                            }

                                            if (sectionId.equals("aPlaylist")) {
                                                JSONObject jsonObjectSong = jsonArray.getJSONObject(check);

                                                if (isFirstPlaylist) {
                                                    sectionArtistPlaylist.setSectionType(jsonObjectSong.getString("sectionType"));
                                                    sectionArtistPlaylist.setViewType(jsonObjectSong.getString("viewType"));
                                                    sectionArtistPlaylist.setTitle(jsonObjectSong.getString("title"));
                                                    sectionArtistPlaylist.setLink(jsonObjectSong.getString("link"));
                                                    sectionArtistPlaylist.setSectionId(jsonObjectSong.getString("sectionId"));

                                                    JSONArray innerItemsArray = jsonObjectSong.getJSONArray("items");
                                                    ArrayList<DataPlaylist> dataPlaylistArrayList = new ArrayList<>();
                                                    for (int i = 0; i < innerItemsArray.length(); i++) {
                                                        JSONObject innerItemObject = innerItemsArray.getJSONObject(i);
                                                        DataPlaylist dataPlaylist = DataPlaylist.fromJson(innerItemObject.toString());
                                                        dataPlaylistArrayList.add(dataPlaylist);
                                                    }
                                                    sectionArtistPlaylist.setItems(dataPlaylistArrayList);

                                                    isFirstPlaylist = false; // Đã gán cho đối tượng đầu tiên
                                                } else {
                                                    //xuat hien trong
                                                    sectionArtistPlaylistXuatHienTrong.setSectionType(jsonObjectSong.getString("sectionType"));
                                                    sectionArtistPlaylistXuatHienTrong.setViewType(jsonObjectSong.getString("viewType"));
                                                    sectionArtistPlaylistXuatHienTrong.setTitle(jsonObjectSong.getString("title"));
                                                    sectionArtistPlaylistXuatHienTrong.setLink(jsonObjectSong.getString("link"));
                                                    sectionArtistPlaylistXuatHienTrong.setSectionId(jsonObjectSong.getString("sectionId"));

                                                    JSONArray innerItemsArray = jsonObjectSong.getJSONArray("items");
                                                    ArrayList<DataPlaylist> dataPlaylistArrayList = new ArrayList<>();
                                                    for (int i = 0; i < innerItemsArray.length(); i++) {
                                                        JSONObject innerItemObject = innerItemsArray.getJSONObject(i);
                                                        DataPlaylist dataPlaylist = DataPlaylist.fromJson(innerItemObject.toString());
                                                        dataPlaylistArrayList.add(dataPlaylist);
                                                    }
                                                    sectionArtistPlaylistXuatHienTrong.setItems(dataPlaylistArrayList);
                                                }
                                            }


                                            //other single
                                            if (sectionId.equals("aReArtist")) {
                                                JSONObject jsonObjectSong = jsonArray.getJSONObject(check);
                                                sectionArtistArtist.setSectionType(jsonObjectSong.getString("sectionType"));
                                                sectionArtistArtist.setViewType(jsonObjectSong.getString("viewType"));
                                                sectionArtistArtist.setTitle(jsonObjectSong.getString("title"));
                                                sectionArtistArtist.setLink(jsonObjectSong.getString("link"));
                                                sectionArtistArtist.setSectionId(jsonObjectSong.getString("sectionId"));

                                                JSONArray innerItemsArray = jsonObjectSong.getJSONArray("items");
                                                ArrayList<Artists> artistsArrayList = new ArrayList<>();
                                                for (int i = 0; i < innerItemsArray.length(); i++) {
                                                    JSONObject innerItemObject = innerItemsArray.getJSONObject(i);
                                                    Artists artists = Artists.fromJson(innerItemObject.toString());
                                                    artistsArrayList.add(artists);
                                                }
                                                sectionArtistArtist.setItems(artistsArrayList);
                                            }
                                        }


                                        requireActivity().runOnUiThread(new Runnable() {
                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void run() {
                                                txt_artist.setText(name);
                                                txt_follow.setText(Helper.convertToIntString(totalFollow) + " quan tâm");
                                                Glide.with(requireContext()).load(thumbnailM).into(img_artist);
                                                img_artist.setVisibility(View.VISIBLE);
                                                progress_image.setVisibility(View.GONE);


                                                //topAlbum
                                                if (jsonObjectNewSong != null) {
                                                    if (jsonObjectNewSong.length() > 0) {
                                                        dataPlaylistNewSong = DataPlaylist.fromJson(jsonObjectNewSong.toString());
                                                        relative_new_song.setVisibility(View.VISIBLE);
                                                        txtTile.setText(dataPlaylistNewSong.getTitle());
                                                        txtArtist.setText(dataPlaylistNewSong.getArtistsNames());
                                                        Glide.with(requireContext()).load(dataPlaylistNewSong.getThumbnail()).into(img_song);
                                                    } else {
                                                        relative_new_song.setVisibility(View.GONE);
                                                    }
                                                } else {
                                                    relative_new_song.setVisibility(View.GONE);
                                                }

                                                //noi_bat
                                                if (sectionArtistSong != null && sectionArtistSong.getItems() != null && !sectionArtistSong.getItems().isEmpty()) {
                                                    relative_noibat.setVisibility(View.VISIBLE);
                                                    itemsArrayListNoiBat = sectionArtistSong.getItems();
                                                    noibatAdapter.setFilterList(sectionArtistSong.getItems());
                                                } else {
                                                    relative_new_song.setVisibility(View.GONE);
                                                }

                                                //single
                                                if (sectionArtistPlaylistSingle != null && sectionArtistPlaylistSingle.getItems() != null && !sectionArtistPlaylistSingle.getItems().isEmpty()) {
                                                    relative_single.setVisibility(View.VISIBLE);
                                                    singleAdapter.setFilterList(sectionArtistPlaylistSingle.getItems());
                                                } else {
                                                    relative_single.setVisibility(View.GONE);
                                                }

                                                //playlist
                                                if (sectionArtistPlaylist != null && sectionArtistPlaylist.getItems() != null && !sectionArtistPlaylist.getItems().isEmpty()) {
                                                    relative_playlist.setVisibility(View.VISIBLE);
                                                    playlistAdapter.setFilterList(sectionArtistPlaylist.getItems());
                                                } else {
                                                    relative_playlist.setVisibility(View.GONE);
                                                }

                                                //xuat hien trong
                                                if (sectionArtistPlaylistXuatHienTrong != null && sectionArtistPlaylistXuatHienTrong.getItems() != null && !sectionArtistPlaylistXuatHienTrong.getItems().isEmpty()) {
                                                    relative_xuathientrong.setVisibility(View.VISIBLE);
                                                    xuatHienTrongAdapter.setFilterList(sectionArtistPlaylistXuatHienTrong.getItems());
                                                } else {
                                                    relative_xuathientrong.setVisibility(View.GONE);
                                                }


                                                //other_artist
                                                if (sectionArtistArtist != null && sectionArtistArtist.getItems() != null && !sectionArtistArtist.getItems().isEmpty()) {
                                                    relative_other_single.setVisibility(View.VISIBLE);
                                                    otherSingleAdapter.setFilterList(sectionArtistArtist.getItems());
                                                } else {
                                                    relative_other_single.setVisibility(View.GONE);
                                                }


                                                CharSequence styledText = Html.fromHtml(biography);
                                                txt_info.setText(styledText);
                                                txt_name_real.setText(real_name);
                                                txt_date_birth.setText(birthday);
                                                txt_country.setText(national);
                                                txt_genre.setText(national);

                                                relative_loading.setVisibility(View.GONE);
                                                nested_scroll.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    } else {
                                        Log.e("TAG", "Error: " + jsonObject.getString("msg"));
                                    }
                                } catch (Exception e) {
                                    Log.e("TAG", "Error parsing response: " + e.getMessage(), e);
                                }
                            } else {
                                Log.e("TAG", "Response unsuccessful: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                            Log.e("TAG", "API call failed: " + throwable.getMessage(), throwable);
                        }
                    });
                } catch (
                        Exception e) {
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