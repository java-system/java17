package demo;

//import org.apache.commons.lang3.time.StopWatch;
//import org.apache.hc.core5.net.URIBuilder;
import system.Dynamic;
import system.Sys;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        System.out.println("This is demo.Main");
        var ary = Dynamic.fromJson("{\"$json\": [11, 22.0, 33]}");
        Sys.echo(ary, "ary");
        ary.remove(1);
        Sys.echo(ary, "ary");
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
            URI uri = new org.apache.hc.core5.net.URIBuilder(url).addParameter(key, value)
                    .build();
            Sys.echo(uri.toString());
        } catch (URISyntaxException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }

        Path file = Paths.get("D:\\.repo\\base14\\nuget.org\\cs-globals\\Globals.Demo\\assets\\qiita-9ea0c8fd43b61b01a8da.json");
        Dynamic qiitaObj = null;
        try {
            String text = Files.readString(file, Charset.forName("UTF-8"));
            //Sys.echo(text);
            var stopWatch = new org.apache.commons.lang3.time.StopWatch();
            //計測スタート
            stopWatch.start();
            //1秒待機
            //Thread.sleep(1000);
            for (int i=0; i<1; i++) {
                qiitaObj = Dynamic.fromJson(text);
            }
            //計測終了
            stopWatch.stop();
            //結果を取得
            //Sys.echo(qiitaObj);
            System.out.println(stopWatch.formatTime());
        } catch (IOException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }
        qiitaObj
                .remove("body")
                .remove("rendered_body");
        qiitaObj.put("user", qiitaObj.get("user").get("id"));
        Sys.echo(qiitaObj);
        Sys.echoJson(qiitaObj);
        //Sys.echo(qiitaObj.asMap().get("title"), "title");
        //Sys.echo(qiitaObj.asMap().get("body"));
    }
}
