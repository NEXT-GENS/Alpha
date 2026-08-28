package id.extension.komikindo

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.regex.Pattern

// Kita membuat class sendiri yang meniru fungsi MangaThemesia agar bisa di-build mandiri
class KomikIndo : Source() {
    override val name = "KomikIndo"
    override val baseUrl = "https://komikindo.ch"
    override val language = "id"

    // Fungsi untuk mengambil manga populer
    override fun popularManga(page: Int): List<Manga> {
        val doc = http.get("$baseUrl/manga-pop/page/$page").body()
        return doc.select(".page-item").map { element ->
            Manga(
                element.select(".post-title a").text(),
                element.select(".post-title a").attr("href"),
                element.select("img").attr("src"),
                null,
                null
            )
        }
    }

    // Fungsi untuk mengambil detail manga
    override fun mangaDetails(url: String): Manga {
        val doc = http.get(url).body()
        return Manga(
            doc.select("h1.entry-title").text(),
            url,
            doc.select(".post-content img").attr("src"),
            doc.select(".entry-content").text(),
            null
        )
    }

    // Fungsi untuk mengambil daftar chapter
    override fun chapterList(manga: Manga): List<Chapter> {
        val doc = http.get(manga.url).body()
        return doc.select(".wp-manga-chapter").mapIndexed { index, element ->
            Chapter(
                element.select("a").text(),
                element.select("a").attr("href"),
                index
            )
        }.reversed()
    }

    // Fungsi untuk mengambil gambar di dalam chapter
    override fun pageList(chapter: Chapter): List<String> {
        val doc = http.get(chapter.url).body()
        return doc.select(".wp-manga-chapter-img img").map { it.attr("src") }
    }
}

// --- CLASS PENDUKUNG (Agar tidak error Unresolved Reference) ---
// Karena kita standalone, kita harus mendefinisikan struktur data dasar di sini

abstract class Source {
    abstract val name: String
    abstract val baseUrl: String
    abstract val language: String
    val http = HttpClient()
}

data class Manga(val name: String, val url: String, val thumbnail: String, val description: String?, val artist: String?)
data class Chapter(val name: String, val url: String, val index: Int)

class HttpClient {
    fun get(url: String) = HttpResponse(org.jsoup.Jsoup.connect(url).get())
}

class HttpResponse(val doc: Document) {
    fun body() = doc
}
