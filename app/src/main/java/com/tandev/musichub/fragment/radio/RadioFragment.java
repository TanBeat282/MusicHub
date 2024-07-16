package com.tandev.musichub.fragment.radio;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.ui.PlayerView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.radio.CommentRadioAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.RadioCategories;
import com.tandev.musichub.api.categories.SearchCategories;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.api.service.RetrofitClient;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.playlist.Playlist;
import com.tandev.musichub.model.radio.host.announcement.AnnouncementRadio;
import com.tandev.musichub.model.radio.host.detail.HostDetail;
import com.tandev.musichub.model.radio.host.info.HostInfo;
import com.tandev.musichub.model.radio.user.comment.CommentUserRadio;
import com.tandev.musichub.model.radio.user.comment.ItemDataCommentUserRadio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RadioFragment extends Fragment {
    private ImageView img_background;
    private ImageView img_back;
    private RoundedImageView img_avatar_host;
    private TextView txt_name_radio;
    private TextView txt_views;
    private TextView txt_reactions;
    private ImageView img_more;

    private LinearLayout linear_controller, linear_song_playing;
    private RoundedImageView thumbImageView;
    private TextView nameTextView;
    private TextView artistTextView;
    private ImageView btn_more;

    private RecyclerView rv_comment;

    private LinearLayout linear_comment_pin;
    private RoundedImageView img_avatar_host_comment_pin;
    private TextView txt_name_host;
    private TextView txt_comment_pin;

    private TextView txt_name_song;
    private ImageView img_list_song;

    private RoundedImageView img_user;
    private EditText edt_comment;

    private Handler handler;
    private Runnable runnable;

    private String endCodeId;
    private String bgColor = "#5e5a54";
    private ArrayList<ItemDataCommentUserRadio> itemDataCommentUserRadioArrayList;
    private CommentRadioAdapter commentRadioAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_radio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        requireActivity().getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        Helper.changeNavigationColor(requireActivity(), R.color.gray, true);

        initViews(view);
        onClick();
        getDataBundle();
    }

    private void getDataBundle() {
        if (getArguments() != null) {
            endCodeId = getArguments().getString("encodeId");
            getInfoRadio(endCodeId);
            getALlComment(endCodeId);
        }
    }

    private void initViews(View view) {
        img_background = view.findViewById(R.id.img_background);

        img_back = view.findViewById(R.id.img_back);
        img_avatar_host = view.findViewById(R.id.img_avatar_host);
        txt_name_radio = view.findViewById(R.id.txt_name_radio);
        txt_views = view.findViewById(R.id.txt_views);
        txt_reactions = view.findViewById(R.id.txt_reactions);
        img_more = view.findViewById(R.id.img_more);

        linear_controller = view.findViewById(R.id.linear_controller);
        linear_song_playing = view.findViewById(R.id.linear_song_playing);
        thumbImageView = view.findViewById(R.id.thumbImageView);
        nameTextView = view.findViewById(R.id.nameTextView);
        artistTextView = view.findViewById(R.id.artistTextView);
        btn_more = view.findViewById(R.id.btn_more);

        rv_comment = view.findViewById(R.id.rv_comment);

        linear_comment_pin = view.findViewById(R.id.linear_comment_pin);
        img_avatar_host_comment_pin = view.findViewById(R.id.img_avatar_host_comment_pin);
        txt_name_host = view.findViewById(R.id.txt_name_host);
        txt_comment_pin = view.findViewById(R.id.txt_comment_pin);

        txt_name_song = view.findViewById(R.id.txt_name_song);
        img_list_song = view.findViewById(R.id.img_list_song);

        img_user = view.findViewById(R.id.img_user);
        edt_comment = view.findViewById(R.id.edt_comment);

        itemDataCommentUserRadioArrayList = new ArrayList<>();

        rv_comment.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        commentRadioAdapter = new CommentRadioAdapter(itemDataCommentUserRadioArrayList, requireActivity(), requireContext());
        rv_comment.setAdapter(commentRadioAdapter);
    }

    private void onClick() {
        img_back.setOnClickListener(view -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        img_list_song.setOnClickListener(view -> {
            //
        });
    }

    private void startRepeatingTask() {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    getRefreshComment(endCodeId, String.valueOf(itemDataCommentUserRadioArrayList.get(itemDataCommentUserRadioArrayList.size() - 1).getId()));
                    getInfoRadio(endCodeId);
                } catch (Exception e) {
                    Log.e("TAG", "Error: " + e.getMessage(), e);
                }
                handler.postDelayed(this, 5000); // Lặp lại sau 5 giây
            }
        };
        handler.post(runnable); // Bắt đầu task
    }

    private void getInfoRadio(String encodeId) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    RadioCategories radioCategories = new RadioCategories();
                    Map<String, String> map = radioCategories.getInfoRadio(encodeId);

                    retrofit2.Call<HostInfo> call = service.INFO_RADIO_CALL(encodeId, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<HostInfo>() {
                        @Override
                        public void onResponse(Call<HostInfo> call, Response<HostInfo> response) {
                            Log.d(">>>>>>>>>>>>>>>>>>>", "getInfoRadio " + call.request().url().toString());

                            if (response.isSuccessful()) {
                                HostInfo hostInfo = response.body();
                                if (hostInfo != null) {
                                    updateUI(hostInfo);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<HostInfo> call, Throwable throwable) {

                        }
                    });
                } catch (Exception e) {
                    Log.e("TAG", "Error: " + e.getMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void getDetailRadio(String encodeId) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    RadioCategories radioCategories = new RadioCategories();
                    Map<String, String> map = radioCategories.getProgramDetailRadio(encodeId);

                    retrofit2.Call<HostDetail> call = service.PROGRAM_DETAIL_RADIO_CALL(encodeId, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<HostDetail>() {
                        @Override
                        public void onResponse(Call<HostDetail> call, Response<HostDetail> response) {
                            Log.d(">>>>>>>>>>>>>>>>>>>", "getDetailRadio " + call.request().url().toString());
                            if (response.isSuccessful()) {
                                HostDetail hostDetail = response.body();
                                if (hostDetail != null) {
                                    updateUIDetail(hostDetail);
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<HostDetail> call, Throwable throwable) {
                            Log.d(">>>>>>>>>>>>>>>>>>>", "getDetailRadioonFailure " + call.request().url().toString());
                        }
                    });
                } catch (Exception e) {
                    Log.e("TAG", "Error: " + e.getMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void getALlComment(String encodeId) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    RadioCategories radioCategories = new RadioCategories();
                    Map<String, String> map = radioCategories.getALlCommentRadio(encodeId);

                    retrofit2.Call<CommentUserRadio> call = service.COMMENT_RADIO_CALL(encodeId, "50", "1", map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<CommentUserRadio>() {
                        @Override
                        public void onResponse(Call<CommentUserRadio> call, Response<CommentUserRadio> response) {
                            Log.d(">>>>>>>>>>>>>", "getALlComment: " + call.request().url().toString());
                            if (response.isSuccessful()) {
                                CommentUserRadio commentUserRadio = response.body();
                                if (commentUserRadio != null) {
                                    updateUIComment(commentUserRadio);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CommentUserRadio> call, Throwable throwable) {

                        }
                    });
                } catch (Exception e) {
                    Log.e("TAG", "Error: " + e.getMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void getRefreshComment(String encodeId, String commentId) throws Exception {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    RadioCategories radioCategories = new RadioCategories();
                    Map<String, String> map = radioCategories.getRefreshCommentRadio(encodeId);

                    retrofit2.Call<CommentUserRadio> call = service.REFRESH_COMMENT_RADIO_CALL(encodeId, commentId, "newest", "50", "1", map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<CommentUserRadio>() {
                        @Override
                        public void onResponse(Call<CommentUserRadio> call, Response<CommentUserRadio> response) {
                            Log.d(">>>>>>>>>>>>>", "getRefreshComment: " + call.request().url().toString());
                            if (response.isSuccessful()) {
                                CommentUserRadio commentUserRadio = response.body();
                                if (commentUserRadio != null) {
                                    updateUIRefreshComment(commentUserRadio);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CommentUserRadio> call, Throwable throwable) {
                            Log.d(">>>>>>>>>>>>>", "getRefreshCommentonFailure: " + call.request().url().toString());
                        }
                    });
                } catch (Exception e) {
                    Log.e("TAG", "Error: " + e.getMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    private void getAnnouncementRadio(String encodeId, String commentId) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    RadioCategories radioCategories = new RadioCategories();
                    Map<String, String> map = radioCategories.getAnnouncementRadio(encodeId);

                    retrofit2.Call<AnnouncementRadio> call = service.ANNOUNCEMENT_RADIO_CALL(encodeId, commentId == null ? "0" : commentId, "after", "10", map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<AnnouncementRadio>() {
                        @Override
                        public void onResponse(Call<AnnouncementRadio> call, Response<AnnouncementRadio> response) {
                            Log.d(">>>>>>>>>>>>>", "getAnnouncementRadio: " + call.request().url().toString());
                            if (response.isSuccessful()) {
                                AnnouncementRadio announcementRadio = response.body();
                                if (announcementRadio != null) {
                                    if (announcementRadio.getData().getItems() != null) {
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AnnouncementRadio> call, Throwable throwable) {
                            Log.d(">>>>>>>>>>>>>", "getAnnouncementRadioonFailure: " + call.request().url().toString());
                        }
                    });
                } catch (Exception e) {
                    Log.e("TAG", "Error: " + e.getMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    private void updateUI(HostInfo hostInfo) {
        if (!isAdded()) {
            return;
        } else {
            requireContext();
        }

        Glide.with(requireContext()).load(hostInfo.getData().getThumbnail()).placeholder(R.drawable.holder).into(img_avatar_host);
        txt_name_radio.setText(hostInfo.getData().getTitle());
        txt_views.setText(convertIntToString(hostInfo.getData().getActiveUsers()));
        txt_reactions.setText(convertIntToString(hostInfo.getData().getTotalReaction()));

        Glide.with(requireContext()).load(hostInfo.getData().getThumbnailV()).placeholder(R.drawable.holder).into(img_background);

        if (hostInfo.getData().getPinMessage() != null) {

            bgColor = hostInfo.getData().getPinMessage().getBgColor() != null ? hostInfo.getData().getPinMessage().getBgColor() : bgColor;
            // Lấy GradientDrawable từ nền của LinearLayout
            GradientDrawable drawable = (GradientDrawable) linear_comment_pin.getBackground();
            // Đổi màu solid của GradientDrawable
            drawable.setColor(Color.parseColor(bgColor));

            txt_comment_pin.setTextColor(Color.parseColor(hostInfo.getData().getPinMessage().getTextColor() != null ? hostInfo.getData().getPinMessage().getTextColor() : "#ffffff"));
            txt_comment_pin.setText(Html.fromHtml(hostInfo.getData().getPinMessage().getContent())); // Nếu nội dung có chứa HTML

            Glide.with(requireContext()).load(hostInfo.getData().getPinMessage().getThumbnail()).placeholder(R.drawable.holder).into(img_avatar_host_comment_pin);
            txt_name_host.setText(hostInfo.getData().getPinMessage().getTitle());

            linear_comment_pin.setVisibility(View.VISIBLE);
        } else {
            linear_comment_pin.setVisibility(View.GONE);
        }
        getDetailRadio(hostInfo.getData().getProgram().getEncodeId());
    }

    @SuppressLint("SetTextI18n")
    private void updateUIDetail(HostDetail hostDetail) {
        txt_name_song.setText(hostDetail.getData().getCurrentMedia().getTitle() + " - " + hostDetail.getData().getCurrentMedia().getArtistsNames());

        Glide.with(requireContext()).load(hostDetail.getData().getCurrentMedia().getThumbnail()).placeholder(R.drawable.holder).into(thumbImageView);
        nameTextView.setText(hostDetail.getData().getCurrentMedia().getTitle());
        artistTextView.setText(hostDetail.getData().getCurrentMedia().getArtistsNames());
        GradientDrawable drawable = (GradientDrawable) linear_song_playing.getBackground();
        // Đổi màu solid của GradientDrawable
        drawable.setColor(Color.parseColor(bgColor));
    }

    private void updateUIComment(CommentUserRadio commentUserRadio) {
        if (commentUserRadio.getData().getItems() != null) {
            itemDataCommentUserRadioArrayList = commentUserRadio.getData().getItems();

            itemDataCommentUserRadioArrayList.sort((item1, item2) -> Integer.compare(item1.getId(), item2.getId()));

            commentRadioAdapter.setFilterList(itemDataCommentUserRadioArrayList);
            rv_comment.scrollToPosition(itemDataCommentUserRadioArrayList.size() - 1);
            startRepeatingTask();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateUIRefreshComment(CommentUserRadio commentUserRadio) {
        if (commentUserRadio.getData().getItems() != null) {
            // Sắp xếp danh sách bình luận theo ID
            itemDataCommentUserRadioArrayList.sort((item1, item2) -> Integer.compare(item1.getId(), item2.getId()));

            // Lấy danh sách các bình luận mới từ API
            ArrayList<ItemDataCommentUserRadio> newComments = commentUserRadio.getData().getItems();

            // Thêm các bình luận mới vào danh sách hiện tại
            itemDataCommentUserRadioArrayList.addAll(newComments);

            // Cập nhật adapter với danh sách bình luận đã được thêm mới
            commentRadioAdapter.notifyDataSetChanged();

            // Cuộn RecyclerView đến bình luận mới nhất
            rv_comment.scrollToPosition(itemDataCommentUserRadioArrayList.size() - 1);
        }
    }

    private String convertIntToString(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(number).replace(',', '.');
    }

}