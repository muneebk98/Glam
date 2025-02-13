package com.example.glamfinal;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class chatbot extends AppCompatActivity {
    private TextView textViewConversation;
    private EditText editTextMessage;
    private Button buttonSend;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        textViewConversation = findViewById(R.id.textViewConversation);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        scrollView = findViewById(R.id.scrollView);

        buttonSend.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String message = editTextMessage.getText().toString().trim();
        if (!message.isEmpty()) {
            // Display the user's message in the TextView
            textViewConversation.append("You: " + message + "\n");
            // Clear the EditText after sending the message
            editTextMessage.setText("");

            // Simulate the bot's response (this method should call your chatbot API)
            callChatbotAPI(message);
        }
    }

    @SuppressLint("NewApi")
    private void callChatbotAPI(String message) {
        // This is a placeholder for your chatbot API call
        // Simulate a delay and display a bot response
        textViewConversation.postDelayed(() -> {// For text-only input, use the gemini-pro model
            GenerativeModel gm = new GenerativeModel(/* modelName */ "gemini-pro",
// Access your API key as a Build Configuration variable (see "Set up your API key" above)
                    /* apiKey */ "");
            GenerativeModelFutures model = GenerativeModelFutures.from(gm);

            Content content = new Content.Builder()
                    .addText(message)
                    .build();

            ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    String resultText = result.getText();
                    SpannableStringBuilder spannable = new SpannableStringBuilder(resultText);
                    applyBoldToAsterisksText(spannable);
                    textViewConversation.append("Bot: ");
                    textViewConversation.append(spannable);
                    textViewConversation.append("\n"); // New line for better readability
                }

                @Override
                public void onFailure(@NonNull Throwable t) {
                    t.printStackTrace();
                }
            }, this.getMainExecutor());

            // Scroll to the bottom to show the latest messages
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }, 0);  // Simulate network delay
    }

    private void applyBoldToAsterisksText(SpannableStringBuilder spannable) {
        Pattern p = Pattern.compile("\\*\\*(.*?)\\*\\*");
        Matcher m = p.matcher(spannable.toString());
        int adjustment = 0; // This will account for changes in length due to text replacement

        while (m.find()) {
            int start = m.start() - adjustment;
            int end = m.end() - adjustment;

            // Get the text without the asterisks using subSequence
            CharSequence textWithoutAsterisks = spannable.subSequence(start + 2, end - 2);

            // Replace the text with the asterisks removed
            spannable.replace(start, end, textWithoutAsterisks);

            // Apply the bold span to the text without the asterisks
            spannable.setSpan(new StyleSpan(Typeface.BOLD), start, start + textWithoutAsterisks.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Update the adjustment factor by the number of characters removed (4 per pair of **)
            adjustment += 4;
        }
    }


}
