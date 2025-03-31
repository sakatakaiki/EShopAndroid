package com.example.loginmvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.loginmvp.R;
import com.example.loginmvp.data.entities.ChatMessage;
import com.example.loginmvp.data.repository.ChatRepository;
import com.example.loginmvp.data.session.UserSession;
import com.example.loginmvp.ui.adapter.ChatAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private EditText edtMessage;
    private ImageButton btnSend;
    private Spinner userSpinner;
    private ChatAdapter chatAdapter;
    private ChatRepository chatRepository;
    private List<ChatMessage> chatMessages;
    private String currentUserId;
    private String receiverId;
    private FirebaseFirestore db;
    private Map<String, String> userMap; // Lưu email -> ID

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Lấy userId từ session
        UserSession userSession = UserSession.getInstance(requireContext());
        currentUserId = String.valueOf(userSession.getUserId());

        recyclerView = view.findViewById(R.id.recyclerChat);
        edtMessage = view.findViewById(R.id.edtMessage);
        btnSend = view.findViewById(R.id.btnSend);
        userSpinner = view.findViewById(R.id.userSpinner);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(requireContext(), chatMessages, currentUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(chatAdapter);

        chatRepository = new ChatRepository();
        db = FirebaseFirestore.getInstance();
        userMap = new HashMap<>();

        // Lấy role từ Firestore và thiết lập danh sách user
        fetchUserRole();

        btnSend.setOnClickListener(v -> {
            String messageText = edtMessage.getText().toString().trim();
            if (!messageText.isEmpty() && receiverId != null) {
                ChatMessage chatMessage = new ChatMessage(currentUserId, receiverId, messageText);
                chatRepository.sendMessage(chatMessage);
                edtMessage.setText("");
            }
        });




        return view;
    }

    private void fetchUserRole() {
        db.collection("users").whereEqualTo("id", currentUserId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String role = document.getString("role");

                        if ("admin".equals(role)) {
                            userSpinner.setVisibility(View.VISIBLE);
                            loadUserListForAdmin();
                        } else {
                            userSpinner.setVisibility(View.GONE);
                            findAdminId();
                        }
                    }
                });
    }

    private void loadUserListForAdmin() {
        db.collection("users").whereEqualTo("role", "user").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        List<String> userEmails = new ArrayList<>();
                        for (DocumentSnapshot doc : task.getResult()) {
                            String email = doc.getString("email");
                            String userId = doc.getString("id");
                            if (email != null && userId != null) {
                                userEmails.add(email);
                                userMap.put(email, userId);
                            }
                        }
                        setupUserSpinner(userEmails);
                    }
                });
    }



    private void setupUserSpinner(List<String> userEmails) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, userEmails);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(adapter);

        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedEmail = userEmails.get(position);
                receiverId = userMap.get(selectedEmail);
                loadMessages();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void findAdminId() {
        db.collection("users").whereEqualTo("role", "admin").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        receiverId = task.getResult().getDocuments().get(0).getString("id");
                        loadMessages();
                    }
                });
    }

    private void loadMessages() {
        if (receiverId == null || receiverId.isEmpty()) return;

        chatRepository.getMessages(currentUserId, receiverId, new ChatRepository.OnMessagesLoadedListener() {
            @Override
            public void onMessagesLoaded(List<ChatMessage> messages) {
                chatMessages.clear();
                chatMessages.addAll(messages);
                chatAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(chatMessages.size() - 1);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
