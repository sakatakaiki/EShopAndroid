package com.example.loginmvp.data.repository;

import com.example.loginmvp.data.entities.ChatMessage;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class ChatRepository {
    private final FirebaseFirestore db;
    private final CollectionReference chatCollection;

    public ChatRepository() {
        db = FirebaseFirestore.getInstance();
        chatCollection = db.collection("chats");
    }

    public void sendMessage(ChatMessage chatMessage) {
        chatCollection.add(chatMessage);
    }

    public void getMessages(String senderId, String receiverId, OnMessagesLoadedListener listener) {
        chatCollection
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) {
                        listener.onError(error);
                        return;
                    }

                    List<ChatMessage> messages = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        ChatMessage message = doc.toObject(ChatMessage.class);
                        if (message != null &&
                                (String.valueOf(message.getSenderId()).equals(senderId) && String.valueOf(message.getReceiverId()).equals(receiverId)) ||
                                (String.valueOf(message.getSenderId()).equals(receiverId) && String.valueOf(message.getReceiverId()).equals(senderId))) {
                            messages.add(message);
                        }
                    }
                    listener.onMessagesLoaded(messages);
                });
    }

    public interface OnMessagesLoadedListener {
        void onMessagesLoaded(List<ChatMessage> messages);
        void onError(Exception e);
    }
}