package com.example.healthapp.ui.home.chatbot;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthapp.R;
import com.example.healthapp.adapter.ChatBotAdapter;
import com.example.healthapp.model.chatbot.ApiClient;
import com.example.healthapp.model.chatbot.ChatMessage;
import com.example.healthapp.model.chatbot.ChatRequest;
import com.example.healthapp.model.chatbot.ChatResponse;
import com.example.healthapp.model.chatbot.PerplexityApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatBotDialogFragment extends DialogFragment {
    private ChatBotAdapter adapter;
    private RecyclerView rvChat;
    private EditText etInput;
    private Button btnSend;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_chat_bot, container, false);

        rvChat = view.findViewById(R.id.rv_chat);
        etInput = view.findViewById(R.id.et_input);
        btnSend = view.findViewById(R.id.btn_send);

        adapter = new ChatBotAdapter();
        rvChat.setAdapter(adapter);
        rvChat.setLayoutManager(new LinearLayoutManager(requireContext()));

        btnSend.setOnClickListener(v -> {
            String message = etInput.getText().toString();
            if (!message.isEmpty()) {
                adapter.addMessage("user" , message);
                etInput.setText("");
                sendMessageToAI(message);
            }
        });


        // 다이얼로그 초기 메시지
        adapter.addMessage("system","안녕하세요!");
        return view;
    }
    private void sendMessageToAI(String userInput) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", "당신은 운동 앱의 챗봇입니다. 운동과 관련한 질문에만 200자 이내로 대답해주세요. 마크다운이나 참고한 링크의 주석 등 불필요한 형식([2],[3] 등)은 제외해주세요."));
        messages.add(new ChatMessage("user", userInput));

        PerplexityApi api = ApiClient.getApi();
        Call<ChatResponse> call = api.getChatResponse(new ChatRequest(messages));
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getChoices() != null && !response.body().getChoices().isEmpty()) {
                        String answer = response.body().getChoices().get(0).getMessage().getContent();
                        adapter.addMessage("system", answer);
                        scrollToLastMessage(rvChat);
                    } else {
                        adapter.addMessage("system","오류가 발생했습니다");
                    }
                } else {
                    adapter.addMessage("system","답변을 가져오는 데 실패했습니다. (코드: " + response.code() + ")");
                    if (response.errorBody() != null) {
                        try {
                            String error = response.errorBody().string();
                            Log.e("ChatBot", "Error: " + error); // 로그 출력
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                t.printStackTrace(); // 로그 확인
                adapter.addMessage("system","오류가 발생했습니다.");
            }

        });
    }
    private void scrollToLastMessage(RecyclerView rvChat) {
        if (adapter.getItemCount() > 0) {
            rvChat.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    }

}
