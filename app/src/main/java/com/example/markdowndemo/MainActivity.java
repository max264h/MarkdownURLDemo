package com.example.markdowndemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);

        String url = "https://raw.githubusercontent.com/mukeshsolanki/MarkdownView-Android/main/README.md";

        new Thread(() ->{
            try {
                URL markdownUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) markdownUrl.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder markdownBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    markdownBuilder.append(line);
                }
                reader.close();
                connection.disconnect();

                String markdown = markdownBuilder.toString();

                // 將 Markdown 轉換為 HTML
                Parser parser = Parser.builder().build();
                HtmlRenderer renderer = HtmlRenderer.builder().build();
                Node document = parser.parse(markdown);
                String html = renderer.render(document);

                runOnUiThread(() ->{
                    // 在 WebView 中顯示 HTML
                    webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
