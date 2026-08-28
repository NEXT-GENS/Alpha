package id.extension.komikindo

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class KomikIndo : Source() {
    override val name = "KomikIndo"
    override val baseUrl = "https://komikindo.id"
    override val language = "id"

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

    override fun pageList(chapter: Chapter): List<String> {
        val doc = http.get(chapter.url).body()
        return doc.select(".wp-manga-chapter-img img").map { it.attr("src") }
    }
}

// --- PERBAIKAN UTAMA: Class Source sekarang memiliki definisi fungsi ---
abstract class Source {
    abstract val name: String
    abstract val baseUrl: String
    abstract val language: String
    
    // Definisi fungsi abstract agar bisa di-override oleh KomikIndo
    abstract fun popularManga(page: Int): List<Manga>
    abstract fun mangaDetails(url: String): Manga
    abstract fun chapterList(manga: Manga): List<Chapter>
    abstract fun pageList(chapter: Chapter): List<String>
    
    val http = HttpClient()
}

// --- DATA CLASSES ---
data class Manga(val name: String, val url: String, val thumbnail: String, val description: String?, val artist: String?)
data class Chapter(val name: String, val url: String, val index: Int)

class HttpClient {
    fun get(url: String) = HttpResponse(org.jsoup.Jsoup.connect(url).get())
}

class HttpResponse(val doc: Document) {
    fun body() = doc
}
