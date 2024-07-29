package com.tandev.musichub.fragment.video;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.video.VideoSize;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.artist.SelectArtistAdapter;
import com.tandev.musichub.adapter.video.VideoAllAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.categories.VideoCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.bottomsheet.BottomSheetOptionPlaylist;
import com.tandev.musichub.bottomsheet.BottomSheetSelectQualityVideo;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.hub.HubVideo;
import com.tandev.musichub.model.video.ItemVideoStreaming;
import com.tandev.musichub.model.video.Video;
import com.tandev.musichub.model.video.section_relate.SectionRelateVideo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoFragment extends Fragment implements BottomSheetSelectQualityVideo.QualityVideoListener {
    private final long SEEK_TIME_MS = 5000; // 5 giây
    private PlayerView playerView;
    private ExoPlayer player;
    private ItemVideoStreaming videoStreamingLinks;
    private int quality_video = 0;
    private Handler handler;
    private Runnable updateProgressAction;
    private TextView ex_position, ex_duration;
    private DefaultTimeBar exo_time_bar;
    private boolean isControllerVisible = false;
    private boolean isFullScreen = false;

    private TextView txt_title_video;
    private TextView txt_view;
    private TextView txt_release;
    private TextView txt_more;
    private RoundedImageView thumbImageView;
    private TextView nameTextView;
    private TextView artistTextView;
    private TextView txt_like;
    private TextView txt_comment;

    private ArrayList<HubVideo> hubVideoArrayList;
    private VideoAllAdapter videoAllAdapter;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        getDataBundle();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews(View view) {
        Helper.changeNavigationColor(requireActivity(), R.color.gray, true);
        Helper.changeStatusBarColor(requireActivity(), R.color.black);

        txt_title_video = view.findViewById(R.id.txt_title_video);
        txt_view = view.findViewById(R.id.txt_view);
        txt_release = view.findViewById(R.id.txt_release);
        txt_more = view.findViewById(R.id.txt_more);
        thumbImageView = view.findViewById(R.id.thumbImageView);
        nameTextView = view.findViewById(R.id.nameTextView);
        artistTextView = view.findViewById(R.id.artistTextView);
        txt_like = view.findViewById(R.id.txt_like);
        txt_comment = view.findViewById(R.id.txt_comment);


        playerView = view.findViewById(R.id.player_view);
        player = new ExoPlayer.Builder(requireContext()).build();
        playerView.setPlayer(player);


        ex_position = playerView.findViewById(R.id.ex_position);
        ex_duration = playerView.findViewById(R.id.ex_duration);
        exo_time_bar = playerView.findViewById(R.id.exo_time_bar);

        RecyclerView rv_related_video = view.findViewById(R.id.rv_related_video);
        ImageView ex_back = playerView.findViewById(R.id.ex_back);
        ImageView ex_song = playerView.findViewById(R.id.ex_song);
        ImageView ex_setting = playerView.findViewById(R.id.ex_setting);
        ImageView ex_previous = playerView.findViewById(R.id.ex_previous);
        ImageView ex_replay = playerView.findViewById(R.id.ex_replay);
        ImageView exo_fullscreen = playerView.findViewById(R.id.exo_fullscreen);
        ImageView exo_play_pause = playerView.findViewById(R.id.exo_play_pause);
        ImageView ex_next = playerView.findViewById(R.id.ex_next);
        ImageView ex_forward = playerView.findViewById(R.id.ex_forward);

        handler = new Handler();
        updateProgressAction = new Runnable() {
            @Override
            public void run() {
                if (player != null) {
                    long currentPosition = player.getCurrentPosition();
                    long duration = player.getDuration();
                    ex_position.setText(formatTime(currentPosition));
                    ex_duration.setText(formatTime(duration));
                    exo_time_bar.setPosition(currentPosition);
                    exo_time_bar.setDuration(duration);
                    handler.postDelayed(this, 1000);
                }
            }
        };

        playerView.setControllerAutoShow(false);
        playerView.setControllerShowTimeoutMs(100);

        // Xử lý sự kiện click cho nút play/pause
        exo_play_pause.setOnClickListener(v -> {
            if (player.isPlaying()) {
                player.pause();
                handler.removeCallbacks(updateProgressAction);

            } else {
                player.play();
                handler.post(updateProgressAction);
            }
        });
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                updatePlayPauseButton();
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                updatePlayPauseButton();
            }

            private void updatePlayPauseButton() {
                if (player.isPlaying()) {
                    exo_play_pause.setImageResource(R.drawable.baseline_pause_24);
                } else {
                    exo_play_pause.setImageResource(R.drawable.ic_play);
                }
            }
        });
        playerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (isControllerVisible) {
                    playerView.hideController();
                } else {
                    playerView.showController();
                    playerView.setControllerShowTimeoutMs(1500);
                }
                isControllerVisible = !isControllerVisible;
            }
            return true;
        });

        exo_time_bar.addListener(new DefaultTimeBar.OnScrubListener() {
            @Override
            public void onScrubStart(TimeBar timeBar, long position) {
                handler.removeCallbacks(updateProgressAction); // Stop updating UI when user is scrubbing
            }

            @Override
            public void onScrubMove(TimeBar timeBar, long position) {
                ex_position.setText(formatTime(position)); // Update time position
            }

            @Override
            public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                player.seekTo(position); // Seek to the position
                handler.post(updateProgressAction); // Resume updating UI
            }
        });
        // Trong initViews hoặc sau khi khởi tạo player
        player.addListener(new Player.Listener() {
            @Override
            public void onVideoSizeChanged(VideoSize videoSize) {
                Player.Listener.super.onVideoSizeChanged(videoSize);
                updatePlayerViewSize(videoSize.width, videoSize.height);
            }
        });
        ex_back.setOnClickListener(view13 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        ex_setting.setOnClickListener(view1 -> {
            BottomSheetSelectQualityVideo bottomSheetSelectQualityVideo = new BottomSheetSelectQualityVideo(requireContext(), requireActivity(), quality_video, videoStreamingLinks);
            bottomSheetSelectQualityVideo.setQualityVideoListener(this);
            bottomSheetSelectQualityVideo.show(((AppCompatActivity) requireContext()).getSupportFragmentManager(), bottomSheetSelectQualityVideo.getTag());
        });
        ex_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ex_replay.setOnClickListener(view12 -> {
            long currentPosition = player.getCurrentPosition();
            long newPosition = Math.max(currentPosition - SEEK_TIME_MS, 0); // Đảm bảo không vượt quá thời gian 0
            player.seekTo(newPosition);
            player.play();
        });

        ex_forward.setOnClickListener(view12 -> {
            long currentPosition = player.getCurrentPosition();
            long newPosition = Math.min(currentPosition + SEEK_TIME_MS, player.getDuration()); // Đảm bảo không vượt quá thời gian video
            player.seekTo(newPosition);
            player.play();
        });


        exo_fullscreen.setOnClickListener(view12 -> {
            if (isFullScreen) {
                exitFullScreenMode();
            } else {
                enterFullScreenMode();
            }
            isFullScreen = !isFullScreen;
        });

        handler.post(updateProgressAction);

        hubVideoArrayList = new ArrayList<>();
        rv_related_video.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        videoAllAdapter = new VideoAllAdapter(hubVideoArrayList, requireActivity(), requireContext());
        rv_related_video.setAdapter(videoAllAdapter);


    }

    private void getDataBundle() {
        if (getArguments() != null) {
            String encodeId = getArguments().getString("encodeId");
            getVideo(encodeId);
            getRelatedVideo(encodeId);
        }
    }

    private void enterFullScreenMode() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        playerView.findViewById(R.id.custom_exo_player_control_view).setVisibility(View.GONE);
    }

    private void exitFullScreenMode() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        playerView.findViewById(R.id.custom_exo_player_control_view).setVisibility(View.VISIBLE);
    }

    private void updatePlayerViewSize(int videoWidth, int videoHeight) {
        if (videoWidth == 0 || videoHeight == 0) return;

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int newHeight = (int) ((float) videoHeight / videoWidth * screenWidth);

        // Update PlayerView size
        ViewGroup.LayoutParams playerViewParams = playerView.getLayoutParams();
        playerViewParams.height = newHeight;
        playerView.setLayoutParams(playerViewParams);

        // Update custom_exo_player_control_view size
        View customController = playerView.findViewById(R.id.custom_exo_player_control_view);
        if (customController != null) {
            ViewGroup.LayoutParams controllerParams = customController.getLayoutParams();
            controllerParams.height = newHeight;
            customController.setLayoutParams(controllerParams);
        }
    }

    private void getVideo(String encodeId) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    VideoCategories videoCategories = new VideoCategories();
                    Map<String, String> map = videoCategories.getVideo(encodeId);

                    Call<Video> call = service.VIDEO_CALL(encodeId, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<Video>() {
                        @Override
                        public void onResponse(@NonNull Call<Video> call, @NonNull Response<Video> response) {
                            if (response.isSuccessful()) {
                                Video data = response.body();
                                videoStreamingLinks = data.getData().getStreaming().getHls();
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        playVideo(videoStreamingLinks.p360, 0);
                                        updateUI(data);
                                    }
                                });
                            } else {
                                Log.d("Request Error", "Response code: " + response.code() + ", Message: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Video> call, @NonNull Throwable throwable) {
                            Log.e("Request Failed", "Error: " + throwable.getMessage(), throwable);
                        }
                    });
                } catch (Exception e) {
                    Log.e("TAG", "Error: " + e.getMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("Service Error", "Error creating service: " + e.getMessage(), e);
            }
        });
    }

    private void getRelatedVideo(String encodeId) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    VideoCategories videoCategories = new VideoCategories();
                    Map<String, String> map = videoCategories.getRelatedVideos(encodeId);

                    Call<SectionRelateVideo> call = service.SECTION_RELATE_VIDEO_CALL(encodeId, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<SectionRelateVideo>() {
                        @Override
                        public void onResponse(Call<SectionRelateVideo> call, Response<SectionRelateVideo> response) {
                            if (response.isSuccessful()) {
                                SectionRelateVideo data = response.body();
                                if (data != null && data.getErr() == 0) {
                                    updateUIRelateVideo(data);
                                }

                            } else {
                                Log.d("Request Error", "Response code: " + response.code() + ", Message: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<SectionRelateVideo> call, Throwable throwable) {

                        }
                    });
                } catch (Exception e) {
                    Log.e("TAG", "Error: " + e.getMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("Service Error", "Error creating service: " + e.getMessage(), e);
            }
        });
    }

    private void updateUIRelateVideo(SectionRelateVideo data) {
        hubVideoArrayList = data.getData().get(0).getItems();
        videoAllAdapter.setFilterList(hubVideoArrayList);
    }

    public interface ArtistFollowersCallback {
        void onFollowersFetched(int totalFollow);

        void onError(String error);
    }

    private void getArtist(String artistId, SelectArtistAdapter.ArtistFollowersCallback callback) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    SongCategories songCategories = new SongCategories();
                    Map<String, String> map = songCategories.getArtist(artistId);

                    Call<ResponseBody> call = service.ARTISTS_CALL(artistId, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                try {
                                    String responseBody = response.body().string();
                                    JSONObject jsonObject = new JSONObject(responseBody);

                                    if (jsonObject.getInt("err") == 0) {
                                        JSONObject data = jsonObject.getJSONObject("data");
                                        int totalFollow = data.getInt("totalFollow");
                                        requireActivity().runOnUiThread(() -> callback.onFollowersFetched(totalFollow));
                                    } else {
                                        requireActivity().runOnUiThread(() -> callback.onError("Error: "));
                                    }
                                } catch (Exception e) {
                                    requireActivity().runOnUiThread(() -> callback.onError("Error parsing response: " + e.getMessage()));
                                }
                            } else {
                                requireActivity().runOnUiThread(() -> callback.onError("Response unsuccessful: " + response.message()));
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                            requireActivity().runOnUiThread(() -> callback.onError("API call failed: " + throwable.getMessage()));
                        }
                    });
                } catch (Exception e) {
                    requireActivity().runOnUiThread(() -> callback.onError("Error: " + e.getMessage()));
                }
            }

            @Override
            public void onError(Exception e) {
                requireActivity().runOnUiThread(() -> callback.onError("Service creation error: " + e.getMessage()));
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(Video data) {
        txt_title_video.setText(data.getData().getTitle());
        txt_view.setText(Helper.convertToIntString(data.getData().getListen()) + " lượt xem");
        txt_release.setText(Helper.calculateYearsAgo(data.getData().getCreatedAt()));
        txt_like.setText(Helper.convertToIntString(data.getData().getLike()));
        txt_comment.setText(String.valueOf(data.getData().getComment()));
        Glide.with(requireContext()).load(data.getData().getArtist().getThumbnail()).into(thumbImageView);
        nameTextView.setText(data.getData().getArtist().getName());
        getArtist(data.getData().getArtist().getAlias(), new SelectArtistAdapter.ArtistFollowersCallback() {
            @Override
            public void onFollowersFetched(int totalFollow) {
                artistTextView.setText(Helper.convertToIntString(totalFollow) + " quan tâm");
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void playVideo(String url, long currentPosition) {
        player.stop();
        MediaSource mediaSource = new HlsMediaSource.Factory(new DefaultHttpDataSource.Factory())
                .createMediaSource(MediaItem.fromUri(url));
        player.setMediaSource(mediaSource);
        player.prepare();
        player.seekTo(currentPosition);
        player.play();
    }

    @SuppressLint("DefaultLocale")
    private String formatTime(long timeInMillis) {
        long seconds = (timeInMillis / 1000) % 60;
        long minutes = (timeInMillis / (1000 * 60)) % 60;
        long hours = (timeInMillis / (1000 * 60 * 60)) % 24;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
        handler.removeCallbacks(updateProgressAction);
    }

    @Override
    public void onQualityVideoSelected(int quality) {
        String url = "";
        if (quality == 1) {
            url = videoStreamingLinks.getP480();
            Toast.makeText(requireContext(), "Video sẽ phát với chất lượng 480p", Toast.LENGTH_SHORT).show();
        } else if (quality == 2) {
            url = videoStreamingLinks.getP720();
            Toast.makeText(requireContext(), "Video sẽ phát với chất lượng 720p", Toast.LENGTH_SHORT).show();
        } else if (quality == 3) {
            url = videoStreamingLinks.getP1080();
            Toast.makeText(requireContext(), "Video sẽ phát với chất lượng 1080p", Toast.LENGTH_SHORT).show();
        } else {
            url = videoStreamingLinks.getP360();
            Toast.makeText(requireContext(), "Video sẽ phát với chất lượng 360p", Toast.LENGTH_SHORT).show();
        }

        quality_video = quality;
        long currentPosition = player.getCurrentPosition();
        playVideo(url, currentPosition);
    }
}
