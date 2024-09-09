package demo;

import org.apache.hc.core5.net.URIBuilder;
import system.Dynamic;
import system.Sys;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) {
        System.out.println("This is demo.Main");
        var o = Sys.fromJson("{\"a\": [1,2,3]}");
        Sys.echo(o);
        var d = Dynamic.fromJson("{\"b\": [1,2.0,3]}");
        Sys.echo(d);
        Sys.echo(d.get("b"));
        Sys.echo(d.get("b").getAt(1));
        d.get("b").asList().remove(0);
        Sys.echo(d);
        //d.asList().remove(0);
        Sys.echo(d.asMap().size(), "d.asMap().size()");
        Sys.echo(d.toJson(true));
        // "https://jsonplaceholder.typicode.com/posts/1"
        // ビルダーを使用してHttpClientインスタンスを作成
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)             // 明示的にHTTP/2を指定
                .followRedirects(HttpClient.Redirect.NORMAL)    // リダイレクトを有効化（HTTPS→HTTPを除く）
                .build();
        // ビルダーを使用してHttpRequestインスタンスを作成
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                .build();
        try {
            // リクエストを送信
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            // レスポンスボディを出力
            Sys.echo(response.body(), "response.body()");
            var bodyObj = Dynamic.fromJson(response.body());
            Sys.echo(bodyObj, "bodyObj");
            Sys.echo(bodyObj.toJson(true), "bodyObj.toJson(true)");
        } catch (IOException | InterruptedException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }

        String url = "http://www.google.com";
        String key = "article";
        String value = "alpha=beta";
        try {
            URI uri = new URIBuilder(url).addParameter(key, value)
                    .build();
            Sys.echo(uri.toString());
        } catch (URISyntaxException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }

    }
}
